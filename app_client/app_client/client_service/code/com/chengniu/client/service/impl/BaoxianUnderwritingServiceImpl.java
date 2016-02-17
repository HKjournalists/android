package com.chengniu.client.service.impl;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.*;
import com.chengniu.client.domain.AutomaticStatus;
import com.chengniu.client.domain.IntentStatus;
import com.chengniu.client.domain.MediaStatus;
import com.chengniu.client.domain.Partner;
import com.chengniu.client.event.UnderwritingSubmitedEvent;
import com.chengniu.client.event.UnderwritingVerifyFailedEvent;
import com.chengniu.client.event.dispatcher.EventDispatcher;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kplus.orders.rpc.dto.PageVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

@Service("baoxianUnderwritingService")
public class BaoxianUnderwritingServiceImpl implements
		BaoxianUnderwritingService {
    protected static final Logger log = LogManager.getLogger(BaoxianUnderwritingServiceImpl.class);

	@Autowired
	private BaoxianUnderwritingChangeLogDAO baoxianUnderwritingChangeLogDAO;
	@Autowired
	private BaoxianUnderwritingOperationRecordDAO baoxianUnderwritingOperationRecordDAO;
	@Autowired
	private BaoxianUnderwritingRequestDAO baoxianUnderwritingRequestDAO;
	@Autowired
	private BaoxianUnderwritingDAO baoxianUnderwritingDAO;
	@Autowired
	private BaoxianBaseInfoService baoxianBaseInfoService;
    @Autowired
    private BaoxianIntentService baoxianIntentService;
    @Autowired
    private BaoxianInformalQuoteService baoxianInformalQuoteService;
    @Autowired
    private BaoxianInsureInfoService baoxianInsureInfoService;
	@Autowired
    private BaoxianUnderwritingReportService baoxianUnderwritingReportService;
	@Autowired
    private BaoxianQuoteInfoDAO baoxianQuoteInfoDAO;
	@Autowired
    private BaoxianCompanyService baoxianCompanyService;
    @Autowired
    private BaoxianApi fanhuaApi;

	@Autowired
	private EventDispatcher eventDispatcher;

    @Override
    public boolean disposeUnderwriting(String underwritingId) {
        BaoxianUnderwriting underwriting = query(underwritingId);
        BaoxianInformalReport report = baoxianInformalQuoteService.queryInformalReport(
                underwriting.getBaoxianInformalReportId());

        Map<String, Object> ret = fanhuaApi.disposeUnderwriting(underwriting, report);
        Boolean succ = (Boolean)ret.get("result");
        String msg = (String)ret.get("msg");
        if (!succ) {
            if (StringUtils.isEmpty(msg)) {
                msg = "失败";
            }
            msg = "提交核保：" + msg;
            updateAutomatic(underwritingId, AutomaticStatus.REPORT_FAILED, msg);
        } else {
            if (StringUtils.isEmpty(msg)) {
                msg = "成功";
            }
            msg = "提交核保：" + msg;
            updateAutomatic(underwritingId, AutomaticStatus.SUBMITED, msg);

            // 修改配送地址
            baoxianInsureInfoService.disposePeisong(underwriting);
        }
        return succ;
    }

    @Override
    public Map<String, Object> disposeCancel(BaoxianUnderwriting underwriting) {
        BaoxianInformalReport report = baoxianInformalQuoteService.queryInformalReport(
                underwriting.getBaoxianInformalReportId());
        boolean succ = true;
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put("result", false);

        if (!StringUtils.isEmpty(report.getQuoteId())) {
            Map<String, Object> ret = fanhuaApi.disposeCancel(underwriting.getId(), null);
            succ = ret.get("result") != null
                    && "true".equals(ret.get("result").toString());
            resmap = ret;
            if (succ) {
                updateAutomatic(underwriting.getId(), AutomaticStatus.REPORT_FAILED, "核保失败：主动取消核保请求");
            }
        }
        return resmap;
    }

    @Override
    public boolean handleFanhuaNotification(String underwritingId, JsonObject result) {
        BaoxianUnderwriting request = baoxianUnderwritingDAO.query(underwritingId);
        if (request == null) {
            return false;
        }

        Boolean resultBoolean = false;
        try {
            String quoteId = result.get("sid") != null ? result.get("sid")
                    .getAsString() : "";
            String state = result.get("state").getAsString();
            String lastState = null;
            try {
                lastState = result.get("lastState").getAsString();
            } catch (Exception e1) {
            }
            String msg = "";
            try {
                msg = (result.get("msg") == null || !result
                        .get("msg").isJsonPrimitive()) ? null : result
                        .get("msg").getAsString();
            } catch (Exception e) {
            }

            // 记录日志修改状态
            BaoxianIntent intent = new BaoxianIntent();
            intent.setId(request.getBaoxianIntentId());
            if ("Verifying".equalsIgnoreCase(state)) {
                intent.setStatus(IntentStatus.UnderwritingPending.getValue());
                resultBoolean = true;

            } else if ("VerifySuccess".equalsIgnoreCase(state)) {
                saveUnderwritingLog(request.getId(), Partner.FANHUA, "回写核保成功", msg);

                try {
                    this.baoxianUnderwritingReportService.disposeFanhuaReport(
                            underwritingId, quoteId, result);
                    updateAutomatic(underwritingId,
                            AutomaticStatus.REPORT_SUCCEED,
                            msg);
                    intent.setStatus(IntentStatus.UnderwritingSuccess.getValue());
                    resultBoolean = true;
                } catch (Exception e) {
                }
            } else if ("Cantinsure".equalsIgnoreCase(state)
                    || "VerifyFailed".equalsIgnoreCase(state)
                    || "VerifyError".equalsIgnoreCase(state)
                    || "Cantinsure".equalsIgnoreCase(state)
                    || "Cancelled".equalsIgnoreCase(state)
                    || "ErrorClose".equalsIgnoreCase(state)
                    || "TimeOut".equalsIgnoreCase(state)
                    || ("BackToModify".equalsIgnoreCase(state) && "Verifying".equalsIgnoreCase(lastState))) {
                updateAutomatic(underwritingId,
                        AutomaticStatus.REPORT_FAILED, msg);

                intent.setStatus(IntentStatus.UnderwritingPending.getValue());

                saveUnderwritingLog(request.getId(), Partner.FANHUA, "回写核保失败", msg);

                resultBoolean = true;
            }

            // 修改状态
            if (intent.getStatus() != null) {
                baoxianIntentService.updateIntentStatus(intent);
            }

        } catch (Exception e) {
            log.warn("查询报价信息{}", underwritingId, e);
        }
        return resultBoolean;
    }

    /**
	 * 保存需要核保的信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean save(BaoxianUnderwriting info) {
		if (info != null) {
			boolean isFromReport = StringUtils.hasText(info
					.getBaoxianInformalReportId());

			BaoxianBaseInfo baseInfo = this.baoxianBaseInfoService.query(
					info.getBaoxianBaseInfoId(), info.getUserId(),
					info.getUserType());
			if (baseInfo == null)
				return false;
			info.setCreateTime(new Date());
			if (info.getXubao() == null)
				info.setXubao(false);
			info.setStatus(0);
			if (isFromReport) {
				info.setMediaStatus(MediaStatus.VERIFIED.ordinal());
			} else {
				info.setMediaStatus(MediaStatus.VERIFY_PENDING.ordinal());
			}
			info.setDeleted(false);
			info.setCityCode(baseInfo.getCityCode());
			info.setCityName(baseInfo.getCityName());
			info.setMobile(baseInfo.getMobile());
			info.setVehicleModelCode(baseInfo.getVehicleModelCode());
			if (info.getUserType() == 1)
				info.setFailType(-2);
			info.setVehicleModelName(baseInfo.getVehicleModelName());
			info.setVehicleModelPrice(Double.valueOf(
					baseInfo.getVehicleModelPrice()).intValue());
			BaoxianCompany company = this.baoxianCompanyService.query(
					info.getBaoxianCompanyCode(), baseInfo.getCityCode());
			if (company == null
                    || company.getChannelStatus() == null
                    || !company.getChannelStatus()
					|| company.getOpenInfo() == null
					|| company.getOpenInfo() == -1
					|| (company.getOpenInfo().compareTo(2) != 0 && info
							.getUserType().compareTo(company.getOpenInfo()) != 0)) {
				return false;
			} else {
				info.setSupportAutomatic(company.getSupportAutomatic());
				info.setChannel(company.getChannel());
			}

			boolean succ = false;
			boolean isNew = StringUtils.isEmpty(info.getId());
			if (!isNew) {
				BaoxianUnderwriting underwriting = this.baoxianUnderwritingDAO
						.query(info.getId());
				isNew = underwriting == null;
			} else {
				info.setId(SerializableUtils.serializable());
			}

			if (isNew) {
				succ = baoxianUnderwritingDAO.insert(info) > 0;
			} else {
				succ = baoxianUnderwritingDAO.update(info) > 0;
			}

			if (succ) {
				if (null == baoxianUnderwritingRequestDAO.query(info.getId())) {
					baoxianUnderwritingRequestDAO.insertSelective(CommonUtil
							.simpleValueCopy(info,
									BaoxianUnderwritingRequest.class));
				}

                // 记录日志
                if (isNew) {
                    saveUnderwritingLog(info.getId(), Partner.USER, "请求核保", null);
                }
			}
			return succ;
		} else
			return false;
	}

	@Override
	public boolean resumeReport(BaoxianUnderwriting underwriting,
			String reportId) {
		underwriting.setStatus(0);
		underwriting.setAutomaticStatus(0);
		underwriting.setFailType(0);
		underwriting.setBaoxianInformalReportId(reportId);
		return baoxianUnderwritingDAO.updateStatus(underwriting) > 0;
	}

	public boolean updateInsuerInfo(String id, String insuerInfoId) {
		BaoxianUnderwriting underwriting = this.baoxianUnderwritingDAO
				.query(id);
		if (underwriting == null
				|| StringUtils.hasText(underwriting.getBaoxianInsureInfoId()))
			return true;
		underwriting.setBaoxianInsureInfoId(insuerInfoId);
		return this.baoxianUnderwritingDAO.updateInsuerInfoStatus(underwriting) > 0;
	}

	@Override
	public boolean resumeReport(String id, String userId, Integer userType, String reason) {
		BaoxianUnderwriting t = this.query(id);

		// 如果是等待补全资料，需要验证资料是否已经补全
		if (t.getStatus() == -1
				&& MediaStatus.isWaitingForFixed(t.getMediaStatus())) {
			boolean isMediaUploaded = true;
			List<BaoxianBaseInfoMedia> mediaList = this.baoxianBaseInfoService
					.queryMediaInfo(id);
			if (mediaList != null && !mediaList.isEmpty()) {
				for (BaoxianBaseInfoMedia media : mediaList) {
					if (media.getStatus() == MediaStatus.SHORTAGE.ordinal()) {
						isMediaUploaded = false;
						break;
					}
				}
			}

			if (!isMediaUploaded) {
				return false;
			}

			// 设置为核保中
			t.setStatus(0);
			t.setMediaStatus(MediaStatus.VERIFY_PENDING.ordinal());
			t.setAutomaticStatus(AutomaticStatus.PENDING.rawValue());

			this.baoxianUnderwritingDAO.updateStatus(t);

            saveUnderwritingLog(id, Partner.USER, "恢复报价请求", reason);
		}

		return true;
	}

	public boolean updateStatusReported(BaoxianUnderwritingRequest request,
			BaoxianUnderwriting t) {
		t.setStatus(1);
		saveUnderwritingChanges(request, t);
		return this.baoxianUnderwritingDAO.updateStatusReported(t) > 0;
	}

	public boolean updateCantUnderwritingFail(String id, String message) {
		BaoxianUnderwriting t = this.query(id);
		t.setId(id);
		if (StringUtils.hasText(message)) {
			t.setFailType(3);
			t.setAutomaticMessage(message);
		}
		if (t.getStatus() != null && t.getStatus() == 1)
			return false;
		if (t.getAutomaticStatus() != null
				&& t.getAutomaticStatus() != AutomaticStatus.REPORT_SUCCEED
						.rawValue())
			t.setAutomaticStatus(AutomaticStatus.REPORT_FAILED.rawValue());
		if (t.getAutomaticStatus() == null)
			t.setAutomaticStatus(AutomaticStatus.REPORT_FAILED.rawValue());
		t.setStatus(-1);
		if (message != null) {
			String[] tst = message.split("：");
			message = tst[tst.length - 1];
		}
		t.setMessage(message);
		boolean succ = this.baoxianUnderwritingDAO.updateStatus(t) > 0;
		if (succ) {
			// 通知核保失败
            log.debug("发布核保失败事件 underwritingId:{}", t.getId());
            eventDispatcher.publish(UnderwritingVerifyFailedEvent.create(t,
                    null));
		}
		return succ;
	}

	public boolean updateStatusFail(String id, String message, int reason) {
		BaoxianUnderwriting t = this.query(id);
		if (t.getStatus() != null && t.getStatus() == 1) {
			return false;
		}

		t.setId(id);
		if (StringUtils.hasText(message)) {
			t.setFailType(3);
			t.setAutomaticMessage(message);
		}

		if (t.getAutomaticStatus() == null
				|| t.getAutomaticStatus() != AutomaticStatus.REPORT_SUCCEED
						.rawValue()) {
			t.setAutomaticStatus(AutomaticStatus.REPORT_FAILED.rawValue());
		}

		t.setStatus(-1);

		boolean succ = this.baoxianUnderwritingDAO.updateStatus(t) > 0;
		if (succ && reason != -1) {
			// 通知核保失败
            log.debug("发布核保失败事件 underwritingId:{}", t.getId());
            eventDispatcher.publish(UnderwritingVerifyFailedEvent.create(t,
                    null));
		}
		return succ;
	}

	public boolean updateYangguangFaid(String id, String message) {
		try {
			BaoxianUnderwriting t = this.query(id);
			BaoxianUnderwritingReport report = this.baoxianUnderwritingReportService
					.queryByUnderwritingId(t.getId());
			if (report != null
					&& (report.getStatus() == 1 || !"yangguang".equals(report
							.getChannel()))) {
				return false;
			}
			t.setStatus(-1);
			t.setMessage(message);
			boolean succ = this.baoxianUnderwritingDAO.updateStatus(t) > 0;
			return succ;
		} catch (Exception e) {
		}
		return false;
	}

    private void saveUnderwritingLog(String underwritingId, String operatorName, String msg, String comment) {
        if (StringUtils.isEmpty(msg)) {
            log.debug("忽略操作日志：{} 内容：{} 备注：{}", underwritingId, msg, comment);
            return;
        }
        try {
            BaoxianUnderwritingOperationRecord log = new BaoxianUnderwritingOperationRecord();
            log.setUnderwritingId(underwritingId);
            if (!StringUtils.isEmpty(comment)) {
                msg = msg + "[" + comment + "]";
            }
            log.setRemark(msg);
            log.setAdminId(null);
            log.setAdminName(operatorName);
            log.setCreateTime(new Date());
            baoxianUnderwritingOperationRecordDAO.insert(log);
        } catch (Exception e) {

        }
    }

	@Override
	public String querySession(String id, Integer step) {
		try {
			return this.baoxianQuoteInfoDAO.queryLastRequestInfo(id, step)
					.getQuoteId();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public BaoxianUnderwriting query(String id) {
		return this.baoxianUnderwritingDAO.query(id);
	}

	@Override
	public BaoxianUnderwriting queryByIntentId(String intentId) {
		return baoxianUnderwritingDAO.queryByIntentId(intentId);
	}

	@SuppressWarnings("serial")
	public BaoxinUnderwritingChangeLogDTO queryRequestChangesInfo(String id) {
		BaoxinUnderwritingChangeLogDTO result = new BaoxinUnderwritingChangeLogDTO();
		Map<String, BaoxianUnderwritingChangeLog> resultMap = new HashMap<String, BaoxianUnderwritingChangeLog>();
		result.setChangeLogMap(resultMap);
		BaoxianUnderwritingRequest request = this.baoxianUnderwritingRequestDAO
				.query(id);
		BaoxianUnderwriting underwriting = this.query(id);
		Gson gson = CommonUtil.gson();
		Map<String, String> underwritingMap = gson.fromJson(
				gson.toJson(underwriting),
				new TypeToken<Map<String, String>>() {
				}.getType());
		if (request == null)
			return result;

		if (!StringUtils.isEmpty(request.getRemark())) {
			result.setRemark(request.getRemark());
		}

		Map<String, String> requestMap = gson.fromJson(gson.toJson(request),
				new TypeToken<Map<String, String>>() {
				}.getType());
		Iterator<Entry<String, String>> it = underwritingMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();

			try {
				final String fieldName = entry.getKey();
				final String newValue = entry.getValue();
				final String oldValue = requestMap.get(fieldName);

				if (newValue.equalsIgnoreCase(oldValue))
					continue;

				String status = requestMap.get(entry.getKey() + "Status");

				final boolean updated = status != null
						&& !status.equalsIgnoreCase("0");
				if (updated) {
					BaoxianUnderwritingChangeLog log = new BaoxianUnderwritingChangeLog();
					log.setFieldName(fieldName);
					log.setNewValue(tryTranslateBooleanToDigit(newValue));
					log.setOldValue(tryTranslateBooleanToDigit(oldValue));
					log.setOperation(Integer.parseInt(status));
					resultMap.put(entry.getKey(), log);
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	private String tryTranslateBooleanToDigit(String value) {
		if ("true".equalsIgnoreCase(value)) {
			return "1";
		} else if ("false".equalsIgnoreCase(value)) {
			return "0";
		} else {
			return value;
		}
	}

	@Override
	public PageVO<BaoxianUnderwritingDTO> page(SearchVO search) {
		return this.baoxianUnderwritingDAO.page(search);
	}

	@Override
	public boolean updateAutomatic(String id, AutomaticStatus status,
			String message) {
		BaoxianUnderwriting t = new BaoxianUnderwriting();
		t.setId(id);
		t.setAutomaticStatus(status.rawValue());
		t.setLastAutomaticTime(new Date());
		t.setAutomaticMessage(message);
		boolean succ = this.baoxianUnderwritingDAO.updateAutomatic(t) > 0;
		if (succ) {
			// 通知自动核保已提交
			if (status == AutomaticStatus.SUBMITED) {
                log.debug("发布提交自动核保事件 underwritingId:{}", t.getId());
                eventDispatcher.publish(UnderwritingSubmitedEvent.create(t,
                        null));
			}
		}

		return succ;
	}

	@Override
	public boolean updateClose(String id, String userId, Integer userType) {
		BaoxianUnderwriting t = new BaoxianUnderwriting();
		t.setId(id);
		t.setUserId(userId);
		t.setUserType(userType);
		return this.baoxianUnderwritingDAO.close(t) > 0;
	}

	@Override
	public int queryCount(Integer status, String userId, Integer userType) {
		BaoxianUnderwriting t = new BaoxianUnderwriting();
		t.setUserId(userId);
		t.setStatus(status);
		t.setUserType(userType);
		return this.baoxianUnderwritingDAO.queryCount(t);
	}

	@Override
	public int queryReportCount(Integer status, String userId,
			Integer userType, Date beginCreateDate, Date endCreateDate) {
		return this.baoxianUnderwritingDAO.queryReportCountInPeriod(status,
                userId, userType, beginCreateDate, endCreateDate);
	}

	@Override
	public int queryRequestCount(Integer status, String userId,
			Integer userType, Date beginCreateDate, Date endCreateDate) {
        return this.baoxianUnderwritingDAO.queryRequestCountInPeriod(status,
                userId, userType, beginCreateDate, endCreateDate);
	}

	@Override
	public boolean updateMediaStatus(String id, Integer status) {
		BaoxianUnderwriting t = new BaoxianUnderwriting();
		t.setId(id);
		t.setMediaStatus(status);
		boolean succ = this.baoxianUnderwritingDAO.updateMediaStatus(t) > 0;
		return succ;
	}

	/**
	 * 对比方案，保存方案修改
	 * 
	 * @param request
	 * @param writing
	 * @return
	 */
	private boolean saveUnderwritingChanges(BaoxianUnderwritingRequest request,
			BaoxianUnderwriting writing) {
		if (writing != null && request != null) {
			String adminId = null;
			String adminName = "系统";
			try {
				Method[] sourceMethods = request.getClass().getMethods();
				Method[] desMethods = writing.getClass().getMethods();
				for (Method m : sourceMethods) {
					String methodName = m.getName();
					if (methodName.startsWith("set")
							&& methodName.endsWith("Status")) {
						String propName = methodName.substring(3,
								methodName.length() - 6);
						if (!StringUtils.isEmpty(propName)) {
							String getPropMethodName = "get" + propName;

							Object oldValue = null;
							for (Method sm : sourceMethods) {
								if (getPropMethodName.equals(sm.getName())) {
									try {
										oldValue = sm.invoke(request);
									} catch (Exception e) {
									}
									break;
								}
							}

							Object newValue = null;
							for (Method dm : desMethods) {
								if (getPropMethodName.equals(dm.getName())) {
									try {
										newValue = dm.invoke(writing);
									} catch (Exception e) {

									}
									break;
								}
							}

							Object oldStatus = null;
							String getStatusName = methodName.replaceFirst(
									"set", "get");
							for (Method dm : sourceMethods) {
								if (getStatusName.equals(dm.getName())) {
									try {
										oldStatus = dm.invoke(request);
									} catch (Exception e) {

									}
									break;
								}
							}

							Byte newStatus = checkStatus(oldValue, newValue);
							if (!newStatus.equals(oldStatus)) {
								String propId = propName.substring(0, 1)
										.toLowerCase() + propName.substring(1);
								saveInsuranceChangeLog(writing.getId(), propId,
										String.valueOf(writing.getJqx()),
										String.valueOf(writing.getJqx()),
										adminId, adminName);
								try {
									m.invoke(request, newStatus);
								} catch (Exception e) {

								}
							}
						}
					}
				}

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 增加
	 * 
	 * @param underwritingId
	 * @param cloumn
	 * @param oldValue
	 * @param newValue
	 * @param adminId
	 * @param adminName
	 */
	private void saveInsuranceChangeLog(String underwritingId, String cloumn,
			String oldValue, String newValue, String adminId, String adminName) {
		BaoxianUnderwritingChangeLog log = new BaoxianUnderwritingChangeLog();
		log.setBaoxianUnderwritingId(underwritingId);
		log.setFieldName(cloumn);
		log.setNewValue(newValue);
		log.setOldValue(oldValue);
		log.setOperatorId(adminId);
		log.setOperatorName(adminName);
		log.setCreateTime(new Date());

		baoxianUnderwritingChangeLogDAO.insert(log);
	}

	private byte checkStatus(Object oldValue, Object newValue) {
		if (oldValue instanceof String) {
			return checkStatus((String) oldValue, (String) newValue);
		} else if (oldValue instanceof Boolean) {
			return checkStatus((Boolean) oldValue, (Boolean) newValue);
		} else if (oldValue instanceof Integer) {
			return checkStatus((Integer) oldValue, (Integer) newValue);
		}
		return BaoxianUnderwritingChangeLog.OPERATION_UNCHANGE;
	}

	private byte checkStatus(Integer oldValue, Integer newValue) {
		if (oldValue.equals(newValue)) {
			return BaoxianUnderwritingChangeLog.OPERATION_UNCHANGE;
		} else {
			if (oldValue == 0) {
				return BaoxianUnderwritingChangeLog.OPERATION_ADD;
			} else {
				if (newValue == 0) {
					return BaoxianUnderwritingChangeLog.OPERATION_DELETED;
				} else {
					return BaoxianUnderwritingChangeLog.OPERATION_UPDATE;
				}
			}
		}
	}

	private byte checkStatus(String oldValue, String newValue) {
		Float.valueOf(oldValue).intValue();

		return checkStatus(Float.valueOf(oldValue).intValue(),
				Float.valueOf(newValue).intValue());
	}

	private byte checkStatus(Boolean oldValue, Boolean newValue) {
		return checkStatus(oldValue == null || !oldValue ? 0 : 1,
				newValue == null || !newValue ? 0 : 1);
	}

}