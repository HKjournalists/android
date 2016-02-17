package com.chengniu.client.service.impl;

import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.*;
import com.chengniu.client.domain.*;
import com.chengniu.client.event.QuoteFinishedEvent;
import com.chengniu.client.event.QuoteRequestCreatedEvent;
import com.chengniu.client.event.QuoteSubmitEvent;
import com.chengniu.client.event.UnderwritingSubmitedEvent;
import com.chengniu.client.event.dispatcher.EventDispatcher;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("baoxianInformalQuoteService")
public class BaoxianInformalQuoteServiceImpl implements
		BaoxianInformalQuoteService {
	protected static final Logger log = LogManager
			.getLogger(BaoxianInformalQuoteServiceImpl.class);

    @Autowired
    private FanhuaService fanhuaService;
    @Autowired
    private BaoxianApi fanhuaApi;

    @Autowired
	private BaoxianBaseInfoService baoxianBaseInfoService;
    @Autowired
	private BaoxianIntentService baoxianIntentService;
    @Autowired
	private BaoxianCompanyService baoxianCompanyService;
    @Autowired
	private BaoxianQuoteInfoDAO baoxianQuoteInfoDAO;

    @Autowired
	public BaoxianCarLicenseDAO baoxianCarLicenseDAO;
    @Autowired
	private BaoxianUnderwritingRequestDAO baoxianUnderwritingRequestDAO;
	@Autowired
	private BaoxianInformalReportDAO baoxianInformalReportDAO;
	@Autowired
	private BaoxianUnderwritingRequestOperationRecordDAO baoxianUnderwritingRequestOperationRecordDAO;

	@Autowired
	private EventDispatcher eventDispatcher;

	private EventHandler eventHandler = new EventHandler();

	@PostConstruct
	public void init() {
		eventDispatcher.register(eventHandler);
	}

	@Override
	public Map<String, Object> disposeRequestToPost(String intentId,
			String companys, String remark) {
		Map<String, Object> resmap = new HashMap<String, Object>();
		BaoxianUnderwritingRequest request = queryRequestByIntentId(intentId);
		if (!QuoteRequestStatus.isQuoteCommitable(request.getStatus())) {
			resmap.put("result", true);
			resmap.put("msg", "已经在报价");
			return resmap;
		}

		BaoxianIntent intent = this.baoxianIntentService.query(intentId);
		BaoxianBaseInfo baseInfo = this.baoxianBaseInfoService.query(
				intent.getBaoxianBaseInfoId(), intent.getUserId(),
				intent.getUserType());

		// 提交请求到机构
		List<BaoxianCompany> companies = Lists.newArrayList();
		String[] companyCodes = companys.split(",");
		if (companyCodes == null || companyCodes.length == 0) {
			resmap.put("result", false);
			resmap.put("msg", "必须提供机构代码");
			return resmap;
		}

		for (String companyCode : companyCodes) {
			BaoxianCompany company = this.baoxianCompanyService.query(
					companyCode, request.getCityCode());
			if (company == null) {
				resmap.put("result", false);
				resmap.put("msg", "找不到该保险公司：" + companyCode);
				return resmap;
			}

            if (company.getChannelStatus() == null || !company.getChannelStatus()
                    || company.getOpenInfo() == null
                    || (company.getOpenInfo() != intent.getUserType() && company.getOpenInfo() != 2)) {
                resmap.put("result", false);
                resmap.put("msg", "该保险公司：" + companyCode + "已关闭");
                return resmap;
            }

			companies.add(company);
		}

		String batch = com.chengniu.client.common.DateUtils.format(new Date(),
				"yyyyMMddHHmmss");

		String quoteId = null;
		Boolean resultBoolean = false;
		Map<String, Object> ret = null;
		try {
            // 已经有失效的报价先删除
            this.baoxianInformalReportDAO.updateDelete(request.getId());

            request.setStatus(QuoteRequestStatus.RequestSubmit.getValue());
            updateQuoteRequestStatus(request);

            // 提交到橙牛机构
			disposeQuoteRequestToChengniu(request, baseInfo, companies, remark,
					batch);

			// 提交到泛化的机构
			ret = disposeQuoteRequestToFanhua(request, baseInfo, companies,
					remark, batch);
			resultBoolean = (Boolean) ret.get("result");
			quoteId = (String) ret.get("quoteId");
		} catch (Exception e) {
			resultBoolean = false;
		}

		// 发送提交成功
		if (resultBoolean) {
			resmap.put("msg", "成功");

			request.setStatus(QuoteRequestStatus.Quoting.getValue());
			updateQuoteRequestStatus(request);

            log.debug("发布请求第三方系统报价事件 requestId:{}", request.getId());
            eventDispatcher.publish(QuoteSubmitEvent.create(request, quoteId,
                        null));

		} else {
			if (ret != null && ret.get("msg") != null) {
				resmap.put("msg", ret.get("msg"));
			} else {
				resmap.put("msg", "失败");
			}
		}

		resmap.put("result", resultBoolean);

		return resmap;
	}

	/**
	 * 提交报价请求到自营
	 * 
	 * @param request
	 * @param baseInfo
	 * @param companies
	 * @param remark
	 * @param batch
	 */
	private void disposeQuoteRequestToChengniu(
			BaoxianUnderwritingRequest request, BaoxianBaseInfo baseInfo,
			List<BaoxianCompany> companies, String remark, String batch) {
		for (BaoxianCompany company : companies) {
			if (company.getChannel() == null
					|| !"chengniu".equalsIgnoreCase(company.getChannel())) {
				continue;
			}

			BaoxianInformalReport report = baoxianInformalReportDAO
					.queryByRequestIdAndCompanyCode(request.getId(),
							company.getCode());
			if (report != null) {
				continue;
			}

			BaoxianInformalReport t = new BaoxianInformalReport();
			t.setId(SerializableUtils.allocUUID());
			t.setCreateTime(new Date());
			t.setUserType(request.getUserType());
			t.setBaoxianIntentId(request.getBaoxianIntentId());
			t.setBaoxianUnderwritingRequestId(request.getId());
			t.setVehicleModelCode(request.getVehicleModelCode());
			t.setVehicleModelName(request.getVehicleModelName());
			t.setVehicleModelPrice(request.getVehicleModelPrice());
			t.setStatus(0);
			t.setUserId(request.getUserId());
			t.setChannel(company.getChannel());
			t.setBaoxianCompanyCode(company.getCode());
			t.setCityCode(request.getCityCode());
			t.setMobile(request.getMobile());
			t.setCityName(request.getCityName());
			t.setBaoxianCompanyName(company.getCompanyName());
			t.setDeleted("N");
			t.setBatch(batch);
			this.baoxianInformalReportDAO.insert(t);
		}
	}

	/**
	 * 提交到泛华
	 * 
	 * @param request
	 * @param baseInfo
	 * @param companies
	 * @param remark
	 * @param batch
	 * @return
	 */
	private Map<String, Object> disposeQuoteRequestToFanhua(
			BaoxianUnderwritingRequest request, BaoxianBaseInfo baseInfo,
			List<BaoxianCompany> companies, String remark, String batch) {
		Map<String, Object> resmap = Maps.newHashMap();

		List<String> companyCodes = Lists.newArrayList();
		for (BaoxianCompany company : companies) {
			if (company.getChannel() == null
					|| "fanhua".equalsIgnoreCase(company.getChannel())) {
				companyCodes.add(company.getCode());
			}
		}

		if (companyCodes.isEmpty()) {
			resmap.put("result", true);
			return resmap;
		}

		Map<String, Object> ret = fanhuaApi.disposeQuoteRequest(request,
				baseInfo, companyCodes, remark);
		Boolean resultBoolean = (Boolean) ret.get("result");
		if (!resultBoolean) {
			resmap.put("result", false);
			resmap.put("msg", ret.get("msg"));
			return resmap;
		}

		// 生成报价
		String quoteId = (String) ret.get("mid");
		List<Map<String, String>> reportList;
		reportList = (List<Map<String, String>>) ret.get("reportList");

		for (Map<String, String> reportInfo : reportList) {
			final String sid = reportInfo.get("sid");

			BaoxianInformalReport report = this.baoxianInformalReportDAO
					.querySid(sid);
			if (report != null) {
				continue;
			}

			BaoxianInformalReport t = new BaoxianInformalReport();
			t.setId(SerializableUtils.allocUUID());
			t.setCreateTime(new Date());
			t.setUserType(request.getUserType());
			t.setBaoxianIntentId(request.getBaoxianIntentId());
			t.setBaoxianUnderwritingRequestId(request.getId());
			t.setVehicleModelCode(request.getVehicleModelCode());
			t.setVehicleModelName(request.getVehicleModelName());
			t.setVehicleModelPrice(request.getVehicleModelPrice());
			t.setStatus(0);
			t.setUserId(request.getUserId());
			t.setChannel("fanhua");
			t.setBaoxianCompanyCode(reportInfo.get("insComId"));
			t.setCityCode(request.getCityCode());
			t.setMobile(request.getMobile());
			t.setCityName(request.getCityName());
			t.setBaoxianCompanyName(reportInfo.get("companyName"));
			t.setMainQuoteId(quoteId);
			t.setBatch(batch);
			try {
				BaoxianCompany company = this.baoxianCompanyService.query(
						t.getBaoxianCompanyCode(), t.getCityCode());
				if (StringUtils.hasText(company.getPicSmall()))
					t.setBaoxianCompanyPic(company.getPicSmall());
				else
					t.setBaoxianCompanyPic(company.getPic());
			} catch (Exception e) {
			}
			t.setDeleted("N");
			t.setQuoteId(sid);
			this.baoxianInformalReportDAO.insert(t);
		}

		Map<String, Object> submitQuoteResult = fanhuaApi.submitQuoteRequest(
				request.getId(), quoteId);
		try {
			resultBoolean = Boolean.parseBoolean(submitQuoteResult
					.get("result").toString());
			if (resultBoolean != null && !resultBoolean) {
                String msg = (String)submitQuoteResult.get("msg");
				resmap.put("msg", msg);

                // 记录失败日志
                saveQuoteLog(request.getId(), Partner.SYSTEM, "请求泛华报价失败", msg);
			}
		} catch (Exception e) {
		}

		resmap.put("result", resultBoolean);
		resmap.put("quoteId", quoteId);

		return resmap;
	}

	@Override
	public boolean disposeMedia(String requestId) {
		BaoxianUnderwritingRequest request = baoxianUnderwritingRequestDAO
                .query(requestId);
		BaoxianQuoteInfoWithBLOBs quoteInfo = this.baoxianQuoteInfoDAO
				.queryLastRequestInfo(requestId, 0);

		return disposeUploadMedia(request, quoteInfo.getQuoteId(), true);
	}

	private boolean disposeUploadMedia(BaoxianUnderwritingRequest request,
                                       String quoteId, boolean isManual) {
        List<BaoxianBaseInfoMedia> list = baoxianBaseInfoService
				.queryMediaInfo(request.getBaoxianBaseInfoId());
		if (list == null || list.isEmpty()) {
			return false;
		}

		log.debug("开始上传影像资料:" + request.getId());
		boolean succ = fanhuaApi.disposeMedia(request.getId(), quoteId, list);
		log.debug("结束上传影像资料:" + request.getId() + " 结果：" + succ);

        if (isManual) {
            saveQuoteLog(request.getId(), Partner.SYSTEM, "上传影像" + (succ ? "成功" : "失败"), null);
        }

		return succ;
	}

	@Override
	public boolean disposeQuery(String reportId) {
		BaoxianInformalReport t = this.baoxianInformalReportDAO.query(reportId);
		boolean resultBoolean = false;
		try {
			String message = fanhuaApi.disposeQuoteQuery(t);
			return fanhuaService.handleNotification(message,
					t.getBaoxianUnderwritingRequestId());
		} catch (Exception e) {

		}
		return resultBoolean;
	}

	@Override
	public boolean handleFanhuaNotification(String underwritingId,
			JsonObject result) {
		Boolean resultBoolean = false;
		BaoxianUnderwritingRequest request = this.baoxianUnderwritingRequestDAO
				.query(underwritingId);
		if (request == null) {
			return false;
		}

		try {
			String state = result.get("state").getAsString();
			String sid = result.get("sid") != null ? result.get("sid")
					.getAsString() : "";
			String lastState = null;
			try {
				lastState = result.get("lastState").getAsString();
			} catch (Exception e1) {
			}
            String msg = null;
            try {
                msg = result.get("msg").getAsString();
            } catch (Exception e) {
            }

			BaoxianIntent intent = new BaoxianIntent();
			intent.setId(request.getBaoxianIntentId());

			if ("QuoteFailed".equalsIgnoreCase(state)
					|| ("BackToModify".equalsIgnoreCase(state) && ("Quoting"
							.equalsIgnoreCase(lastState) || !StringUtils
							.hasText(sid)))
					|| "QuoteCancel".equalsIgnoreCase(state)
					|| "Cancelled".equalsIgnoreCase(state)
					|| "QuoteSuccess".equalsIgnoreCase(state)) {
                saveQuoteLog(request.getId(), Partner.FANHUA, "报价失败", msg);
			}

			// 多方报价失败
			if (!StringUtils.hasText(sid)) {
				if (result.get("code").getAsInt() == -1
						&& ("QuoteFailed".equalsIgnoreCase(state) || ("BackToModify"
								.equalsIgnoreCase(state) && ("Quoting"
								.equalsIgnoreCase(lastState) || !StringUtils
								.hasText(sid))))) {
                    // 记录操作日志
                    saveQuoteLog(request.getId(), Partner.FANHUA, "多方报价失败", msg);

                    // 设置报价请求为报价中
					request.setStatus(QuoteRequestStatus.QuotingPending
							.getValue());
					request.setUpdateTime(new Date());
					this.baoxianUnderwritingRequestDAO.updateStatus(request);

                    // 设置意向为报价中
					BaoxianIntent baoxianintent = this.baoxianIntentService
							.query(intent.getId());
					intent.setStatus(IntentStatus.Quoting.getValue());
					if (baoxianintent != null
							&& baoxianintent.getStatus() != null
							&& baoxianintent.getStatus().intValue() < intent
									.getStatus().intValue()) {
						this.baoxianIntentService.updateIntentStatus(intent);
					}

                    // 设置报价状态为已提交
					List<BaoxianInformalReport> list = this.baoxianInformalReportDAO
							.queryByRequestId(request.getId());
					for (BaoxianInformalReport sb : list) {
						sb.setStatus(QuoteRequestStatus.RequestSubmit
								.getValue());
						sb.setMessage(msg);
						this.baoxianInformalReportDAO.update(sb);
					}
				}
				return true;
			}

			try {
				BaoxianInformalReport report = this.baoxianInformalReportDAO
						.querySid(sid);
				if (report == null) {
					log.debug("报价已经删除, 忽略{}", sid);
					return false;
				}

				resultBoolean = true;
				if ("Quoting".equalsIgnoreCase(state)) {
					resultBoolean = true;
				} else if ("QuoteSuccess".equalsIgnoreCase(state)) {
					// 如果已手动报价，直接丢弃
					if (report != null
							&& (report.getManual() == null || !report
									.getManual())) {
						FanhuaResponseParser.parseRequest(result, request);
						baoxianUnderwritingRequestDAO.update(request);
						if (report.getStatus() != null
								&& QuoteStatus.Canceled.getValue() != report
										.getStatus()) {
							FanhuaResponseParser.parseReport(result, report);
							report.setStatus(QuoteStatus.SUCCESS.getValue());
							report.setUpdateTime(new Date());
                            report.setMessage(msg);
							this.baoxianInformalReportDAO.update(report);
							syncQuoteRequestStatus(request);
						}

                        // 记录操作日志
                        saveQuoteLog(request.getId(), Partner.FANHUA,
                                "回写"+ report.getBaoxianCompanyName()+"报价成功",
                                msg);
					} else {
                        // 记录操作日志
                        saveQuoteLog(request.getId(), Partner.FANHUA,
                                "回写"+ report.getBaoxianCompanyName()+"报价成功（已手动报价不处理）",
                                msg);
                    }
				} else if ("QuoteFailed".equalsIgnoreCase(state)
						|| ("BackToModify".equalsIgnoreCase(state) && ("Quoting"
								.equalsIgnoreCase(lastState) || !StringUtils
								.hasText(sid)))) {
					// 如果已手动报价，直接丢弃
					if (report != null
							&& (report.getManual() == null || !report
									.getManual())) {
						report.setStatus(QuoteStatus.Failed.getValue());
						report.setUpdateTime(new Date());
						report.setMessage(msg);

						this.baoxianInformalReportDAO.update(report);
						syncQuoteRequestStatus(request);

                        // 记录操作日志
                        saveQuoteLog(request.getId(), Partner.FANHUA,
                                "回写"+ report.getBaoxianCompanyName()+"报价失败",
                                msg);
					} else {
                        // 记录操作日志
                        saveQuoteLog(request.getId(), Partner.FANHUA,
                                "回写"+ report.getBaoxianCompanyName()+"的报价失败（已手动报价不处理）",
                                msg);
                    }
				} else if ("Cancelled".equalsIgnoreCase(state)
						|| "QuoteCancel ".equalsIgnoreCase(state)) {
					// 如果已手动报价，直接丢弃
					if (report != null
							&& (report.getManual() == null || !report
									.getManual())) {
						report.setStatus(QuoteStatus.Canceled.getValue());
						report.setUpdateTime(new Date());
						report.setMessage(msg);

						this.baoxianInformalReportDAO.update(report);
						syncQuoteRequestStatus(request);

                        // 记录操作日志
                        saveQuoteLog(request.getId(), Partner.FANHUA,
                                "回写" + report.getBaoxianCompanyName() + "报价已取消",
                                msg);
					} else {
                        // 记录操作日志
                        saveQuoteLog(request.getId(), Partner.FANHUA,
                                "回写"+ report.getBaoxianCompanyName()+"报价已取消（已手动报价不处理）",
                                msg);
                    }
				} else {
					resultBoolean = false;
				}
			} catch (Exception e) {
				log.error("修改数据{}出错:{}", sid, e);
			}

			// 修改状态
			if (intent.getStatus() != null) {
				Integer count = this.baoxianInformalReportDAO
						.queryCountByRequestId(request.getId());
				if (count == null || count <= 0) {
					this.baoxianIntentService.updateIntentStatus(intent);
					request.setStatus(QuoteRequestStatus.QuoteFinished
							.getValue());
					syncQuoteRequestStatus(request);
				}
			}
		} catch (Exception e) {
			log.error("泛华回写报价处理失败{}:{}", underwritingId, e);
		}
		return resultBoolean;
	}

	@Override
	public BaoxianUnderwritingRequest queryRequestByIntentId(String intentId) {
		return this.baoxianUnderwritingRequestDAO.queryByIntentId(intentId);
	}

	@Override
	public BaoxianUnderwritingRequest queryRequest(String requestId) {
		return this.baoxianUnderwritingRequestDAO.query(requestId);
	}

	@Override
	public BaoxianInformalReport queryInformalReport(String informalReportId) {
		return baoxianInformalReportDAO.query(informalReportId);
	}

	@Override
	public boolean createQuoteRequest(BaoxianIntent intent,
			BaoxianBaseInfo baseInfo) {
		if (!IntentStatus.canRequestQuote(intent.getStatus())) {
			throw new RuntimeException("不能提交多方报价");
		}

		BaoxianUnderwritingRequest request = new BaoxianUnderwritingRequest();
		request.setBaoxianIntentId(intent.getId());
		request.setBaoxianBaseInfoId(baseInfo.getId());
        // 默认为缺少方案
		request.setStatus(QuoteRequestStatus.Invalid.getValue());
		request.setUserType(intent.getUserType());
		request.setUserId(intent.getUserId());
		request.setMobile(baseInfo.getMobile());
		request.setCityCode(baseInfo.getCityCode());
		request.setCityName(baseInfo.getCityName());
		request.setVehicleModelCode(baseInfo.getVehicleModelCode());
		request.setVehicleModelName(baseInfo.getVehicleModelName());
		try {
			request.setVehicleModelPrice(Integer.valueOf(baseInfo
					.getVehicleModelPrice()));
		} catch (Exception e) {
			request.setVehicleModelPrice(0);
		}
		request.setCreateTime(new Date());

		request.setId(SerializableUtils.serializable());
		boolean ret = baoxianUnderwritingRequestDAO.insert(request) > 0;
		if (ret) {
            log.debug("发布请求报价事件 requestId:{}", request.getId());
            QuoteRequestCreatedEvent e = QuoteRequestCreatedEvent.create(request, null);
            eventDispatcher.publish(e);
		}
		return ret;
	}

	@Override
	public boolean updateQuoteRequest(BaoxianUnderwritingRequest request,
			boolean isRenewRequest) {
		if (StringUtils.isEmpty(request.getId())) {
			return false;
		}
		boolean succ = baoxianUnderwritingRequestDAO.update(request) > 0;

        if (isRenewRequest && succ) {
            this.baoxianInformalReportDAO.updateDelete(request.getId());

            // 记录用户请求日志
            saveQuoteLog(request.getId(), Partner.USER, "请求系统报价", null);
        }

        return succ;
	}

	private boolean updateQuoteRequestStatus(BaoxianUnderwritingRequest request) {
		request.setUpdateTime(new Date());
		return baoxianUnderwritingRequestDAO.updateStatus(request) > 0;
	}

	@Override
	public List<BaoxianInformalReport> fetchQuoteList(String requestId) {
		return baoxianInformalReportDAO.queryByRequestId(requestId);
	}

	/**
	 * 同步报价请求状态
	 *
	 * @param request
	 */
	private void syncQuoteRequestStatus(BaoxianUnderwritingRequest request) {
		List<BaoxianInformalReport> reportList = this.fetchQuoteList(request
				.getId());

		if (reportList != null) {
			boolean finished = true;
			int count = 0;
			for (BaoxianInformalReport r : reportList) {
				if (r.getStatus() == QuoteStatus.Waiting.getValue()) {
					finished = false;
				} else if (QuoteStatus.isSuccess(r.getStatus())) {
					count += 1;
				}
			}

			if (finished
					&& QuoteRequestStatus.QuoteFinished.getValue() != request
							.getStatus()) {
				request.setStatus(QuoteRequestStatus.QuoteFinished.getValue());
				updateQuoteRequestStatus(request);

				// 发布事件
                log.debug("发布报价完成事件 requestId:{}", request.getId());
                eventDispatcher.publish(QuoteFinishedEvent.create(request,
                        count, null));

			}
		}
	}

	@Override
	public Map<String, Object> disposeCancel(String requestId, String reportId) {
		Map<String, Object> resmap = new HashMap<String, Object>();
		resmap.put("result", false);
		BaoxianUnderwritingRequest request = this.baoxianUnderwritingRequestDAO
				.query(requestId);
		if (request == null)
			return resmap;

        BaoxianIntent intent = baoxianIntentService.query(request.getBaoxianIntentId());
        if (!IntentStatus.isQuoteStage(intent.getStatus())) {
            resmap.put("msg", "报价单已经流转到核保环节，请到核保环节取消");
            return resmap;
        }

        boolean succ = true;
        boolean ft = true;
		if (StringUtils.hasText(reportId)) {
            // 取消单方报价
            BaoxianInformalReport report = baoxianInformalReportDAO.query(reportId);
            if (report == null) {
                resmap.put("result", false);
                resmap.put("msg", "报价单错误");
                return resmap;
            }

            if (StringUtils.hasText(report.getQuoteId())) {
                resmap.put("result", false);
                resmap.put("msg", "该报价单不能取消");
                return resmap;
            }

            report.setMessage(report.getBaoxianCompanyName() + "成功取消报价");
            report.setStatus(QuoteStatus.Canceled.getValue());
            this.baoxianInformalReportDAO.update(report);

            syncQuoteRequestStatus(request);
		} else {
            Map<String, Object> map = this.fanhuaApi.disposeCancel(requestId,
                    null);
            if (map == null) {
                return resmap;
            }

            succ = map.get("result") != null
                    && "true".equals(map.get("result").toString());
            resmap = map;

            if (!succ) {
                // 如果未提交，取消所有的报价
                String reason = (String)map.get("reason");
                if (reason != null && "unsubmit".equals(reason)) {
                    ft = true;
                    succ = true;
                }
            }
            if (!succ) {
                return resmap;
            }

            request.setStatus(QuoteRequestStatus.RequestSubmit.getValue());
            List<BaoxianInformalReport> list = fetchQuoteList(request.getId());
            for (BaoxianInformalReport sb : list) {
                if (sb != null
                        && (sb.getStatus() == null || sb.getStatus() != QuoteStatus.SUCCESS
                        .getValue())) {
                    sb.setMessage(sb.getBaoxianCompanyName() + "成功取消报价");
                    sb.setStatus(QuoteStatus.Canceled.getValue());
                    this.baoxianInformalReportDAO.update(sb);
                }
            }
            this.baoxianUnderwritingRequestDAO.updateStatus(request);
        }

		return resmap;
	}

	private boolean updateQuoteStatus(String requestId, String reportId,
			QuoteStatus status, boolean shouldSync) {
		BaoxianInformalReport t = new BaoxianInformalReport();
		t.setId(reportId);
		t.setStatus(status.getValue());
		boolean succ = baoxianInformalReportDAO.updateByPrimaryKeySelective(t) > 0;
		if (succ && shouldSync) {
			BaoxianUnderwritingRequest request = baoxianUnderwritingRequestDAO
					.query(requestId);
			syncQuoteRequestStatus(request);
		}
		return succ;
	}

	@Override
	public void resumeQuoteRequest(
			BaoxianUnderwritingRequest underwritingRequest) {
		underwritingRequest.setStatus(QuoteRequestStatus.RequestSubmit
				.getValue());
		List<BaoxianInformalReport> reportlsit = fetchQuoteList(underwritingRequest
				.getId());
		for (BaoxianInformalReport sb : reportlsit) {
			sb.setStatus(QuoteRequestStatus.SubmitFailed.getValue());
			updateQuoteStatus(underwritingRequest.getId(), sb.getId(),
					QuoteStatus.Waiting, false);
		}
		baoxianUnderwritingRequestDAO.updateStatus(underwritingRequest);
	}

	@Override
	public boolean validateInformalReport(BaoxianInformalReport report) {
		if ((report.getSyxStartDate() != null && report.getSyxStartDate()
				.getTime() <= System.currentTimeMillis())
				|| (report.getExpireTime() != null && report.getExpireTime()
						.getTime() <= System.currentTimeMillis())
				|| (report.getJqxStartDate() != null && report
						.getJqxStartDate().getTime() <= System
						.currentTimeMillis())) {
			updateQuoteStatus(report.getBaoxianUnderwritingRequestId(),
					report.getId(), QuoteStatus.Expired, false);
			return false;
		}
		return true;
	}

	@Override
	public boolean saveManualQuoteReport(BaoxianInformalReport report) {
		// 设置为手动报价
		report.setManual(true);

		boolean succ = baoxianInformalReportDAO.update(report) > 0;
		if (succ) {
			BaoxianUnderwritingRequest request = baoxianUnderwritingRequestDAO
					.query(report.getBaoxianUnderwritingRequestId());
			syncQuoteRequestStatus(request);
		}
		return succ;
	}

	@Override
	public boolean updateInformalReportFailed(BaoxianInformalReport report,
			String remark) {
		// 设置为手动报价
		report.setStatus(QuoteStatus.Failed.getValue());
		report.setMessage(remark);
		report.setManual(true);
		boolean succ = baoxianInformalReportDAO.update(report) > 0;
		if (succ) {
			BaoxianUnderwritingRequest request = baoxianUnderwritingRequestDAO
					.query(report.getBaoxianUnderwritingRequestId());
			syncQuoteRequestStatus(request);
		}
		return succ;
	}

	public void cancelPendingReport(String requestId) {
		BaoxianUnderwritingRequest req = this.baoxianUnderwritingRequestDAO
				.query(requestId);

		if (QuoteRequestStatus.isQuoteFailed(req.getStatus())
				|| QuoteRequestStatus.isFinished(req.getStatus())) {
			return;
		}

		List<BaoxianInformalReport> list = fetchQuoteList(requestId);
		for (BaoxianInformalReport sb : list) {
			if (sb == null) {
				continue;
			}

			if (sb.getStatus() == null
					|| sb.getStatus() == QuoteStatus.Waiting.getValue()) {
				updateQuoteStatus(requestId, sb.getId(), QuoteStatus.Canceled,
						false);
			}
		}

		syncQuoteRequestStatus(req);
	}

	private void saveQuoteLog(String requestId, String operatorName, String msg, String comment) {
        if (StringUtils.isEmpty(msg)) {
            log.debug("忽略操作日志：{} 内容：{} 备注：{}", requestId, msg, comment);
            return;
        }

        try {
            BaoxianUnderwritingRequestOperationRecord log = new BaoxianUnderwritingRequestOperationRecord();
            log.setRequestId(requestId);
            log.setAdminName(operatorName);
            if (!StringUtils.isEmpty(comment)) {
                msg = msg + "[" + comment + "]";
            }
            log.setRemark(msg);
            log.setCreateTime(new Date());
            this.baoxianUnderwritingRequestOperationRecordDAO.insert(log);
        } catch (Exception e) {
            log.debug("记录操作日志失败：{} 内容：{} 备注：{}", requestId, msg, comment);
        }
	}

	private final class EventHandler {

		@Subscribe
		public void didQuoteSubmited(QuoteSubmitEvent e) {
			BaoxianUnderwritingRequest request = e.getRequest();
			disposeUploadMedia(request, e.getQuoteId(), false);
		}

		@Subscribe
		public void didUnderwritingSubmited(UnderwritingSubmitedEvent e) {
			final BaoxianUnderwriting underwriting = e.getUnderwriting();

			// 取消未完成的试算请求
			cancelPendingReport(underwriting.getId());
		}

	}

}
