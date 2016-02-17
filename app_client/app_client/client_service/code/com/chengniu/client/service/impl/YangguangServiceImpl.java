package com.chengniu.client.service.impl;

import com.chengniu.client.common.*;
import com.chengniu.client.dao.BaoxianQuoteInfoDAO;
import com.chengniu.client.domain.AutomaticStatus;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kplus.orders.rpc.common.MD5Util;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@Service("yangguangService")
public class YangguangServiceImpl implements YangguangService {
	protected static final Logger log = LogManager
			.getLogger(YangguangServiceImpl.class);
    @Autowired
	private BaoxianUnderwritingService baoxianUnderwritingService;
    @Autowired
    private BaoxianCompanyService baoxianCompanyService;
    @Autowired
	private BaoxianQuoteInfoDAO baoxianQuoteInfoDAO;
    @Autowired
    private BaoxianBaseInfoService baoxianBaseInfoService;
    @Autowired
    private BaoxianUnderwritingReportService baoxianUnderwritingReportService;
    @Autowired
    private BaoxianInsureInfoService baoxianInsureInfoService;
    @Autowired
    private BaoxianOrderService baoxianOrderService;

    @Value("${baoxian.orders}")
    private String ordersUrl;
    @Value("${baoxian.report}")
    private String reportUrl;

	public Map<String, String> disposePreReport(String vehicleNum,
			String idCardName, String cityCode, String mobile, Operator op) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", "400");

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			BaoxianCityMapping citymapp = this.baoxianBaseInfoService
					.queryCity(cityCode);
			map.put("vehicleNum", vehicleNum);
			map.put("cityCode", citymapp.getCityCodeYangguang());
			map.put("mobile", mobile);
			map.put("idCardName", idCardName);
			String message = null;
			String session = SerializableUtils.allocUUID();
			message = postToYangguang("095", map, session, op.getUserId());
			message = postToYangguang("100", map, session, op.getUserId());
			message = parseBody(message, resultMap);
			if (StringUtils.hasText(message)) {
				Map<String, Map<String, String>> result = Xml2JsonUtil
						.xml2JSONByMap(message);
				BaoxianBaseInfo info = new BaoxianBaseInfo();
				info.setId(SerializableUtils.allocUUID());
				info.setUserId(op.getUserId());
				try {
					info.setVehicleModelName(result.get("vehicleModelName")
							.get("value"));
				} catch (Exception e) {
				}
				info.setCityCode(cityCode);
				info.setCityName(citymapp.getCityNameYangguang());
				info.setMobile(mobile);
				info.setIdCardName(idCardName);
				info.setVehicleNum(vehicleNum);
				Map<String, String> node;
				if ((node = result.get("frameNum")) != null) {
					info.setFrameNum(node.get("value"));
				}
				info.setVehicleNum(vehicleNum);
				if ((node = result.get("motorNum")) != null) {
					info.setMotorNum(node.get("value"));
				}
				if ((node = result.get("idCardNum")) != null) {
					info.setIdCardNum(node.get("value"));
				}
				if ((node = result.get("idCardName")) != null) {
					info.setIdCardName(node.get("value"));
				}
				info.setVehicleModelPrice("0");
				try {
					info.setRegisterDate(DateUtils.parseDate(
							result.get("registerDate").get("value"),
							"yyyy-MM-dd"));
				} catch (Exception e) {
				}
				if ("0".equals(info.getMotorNum())) {
					info.setGuohu(true);
					if ((node = result.get("guohudate")) != null) {
						info.setGuohuDate(node.get("value"));
					}
				} else
					info.setGuohu(false);
				info.setUserType(op.getUserType());
				info.setMobile(mobile);
				this.baoxianBaseInfoService.save(info);
				resultMap.put("id", info.getId());
				resultMap.put("result", Xml2JsonUtil.xml2JSON(message));
				resultMap.put("session", session);
				return resultMap;
			}
		} catch (Exception e) {
			log.warn("提交获取车辆信息{}数据到阳光", vehicleNum, e);
			resultMap.put("errorMessage", "系统忙");
		}
		return resultMap;
	}

	@Override
	public Map<String, String> disposeCommitBaseInfo(String param,
			String session, Operator op) throws Exception {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
				.disableHtmlEscaping().create();
		BaoxianBaseInfo baseInfo = gson.fromJson(param, BaoxianBaseInfo.class);
		Map<String, Object> parmmap = gson.fromJson(param,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		baseInfo.setUserType(op.getUserType());
		baseInfo.setUserId(op.getUserId());
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", "400");

		try {
			if (!this.baoxianBaseInfoService.save(baseInfo)) {
				resultMap.put("errorMessage", "系统忙");
				return resultMap;
			}
			baseInfo = this.baoxianBaseInfoService.query(baseInfo.getId(),
					baseInfo.getUserId(), baseInfo.getUserType());
			Map<String, Object> map = gson.fromJson(gson.toJson(baseInfo),
					new TypeToken<Map<String, Object>>() {
					}.getType());
			String message = null;
			if (map != null && map.get("vehicleModelCode") == null) {
				String vehicleId = parmmap.get("vehicleId").toString();
				map.put("vehicleId", vehicleId);
			} else if (map != null)
				map.put("vehicleId", map.get("vehicleModelCode"));
			try {
				map.put("registerDate", new SimpleDateFormat("yyyy-MM-dd")
						.format(baseInfo.getRegisterDate()));
			} catch (Exception e) {
			}
			BaoxianQuoteInfoWithBLOBs quote = this.baoxianQuoteInfoDAO
					.queryByQuoteId(session);
			String baoxianunderwritingid = null;
			if (quote != null
					&& StringUtils.hasText(quote.getBaoxianUnderwritingId()))
				baoxianunderwritingid = quote.getBaoxianUnderwritingId();
			else
				baoxianunderwritingid = SerializableUtils.serializable();
			map.put("seats", parmmap.get("seats"));
			map.put("baoxianunderwritingid", baoxianunderwritingid);
			message = postToYangguang("105", map, session, op.getUserId());
			message = parseBody(message, resultMap);
			if (StringUtils.hasText(message)) {
				Map<String, Map<String, String>> result = Xml2JsonUtil
						.xml2JSONByMap(message);
				BaoxianUnderwriting underwriting = new BaoxianUnderwriting();
				underwriting.setBaoxianBaseInfoId(baseInfo.getId());
				underwriting.setId(baoxianunderwritingid);
				underwriting.setBaoxianCompanyCode(parmmap.get(
						"baoxianCompanyCode").toString());
				underwriting.setUserType(op.getUserType());
				underwriting.setCityCode(baseInfo.getCityCode());
				underwriting.setMobile(baseInfo.getMobile());
				BaoxianCompany company = this.baoxianCompanyService.query(
						underwriting.getBaoxianCompanyCode(),
						underwriting.getCityCode());
				if (company == null
                        || company.getChannelStatus() == null
                        || !company.getChannelStatus()
						|| company.getOpenInfo() == null
						|| company.getOpenInfo() == -1
						|| (company.getOpenInfo() != 2 && company.getOpenInfo() != underwriting
								.getUserType()))
					resultMap.put("errorMessage", "保险公司不正确，请重新选择");
				else {
					underwriting.setCityName(baseInfo.getCityName());
					underwriting
							.setBaoxianCompanyName(company.getCompanyName());
					underwriting.setUserId(op.getUserId());
					if ("100".equals(resultMap.get("status"))) {
						underwriting.setStatus(0);
						underwriting
								.setAutomaticStatus(AutomaticStatus.REPORT_SUCCEED
										.rawValue());
					}
					BaoxianUnderwritingReport report = disposeSaveReport(
							underwriting, session, result);
					// 修改临时的id
					this.baoxianQuoteInfoDAO.updateUnderWritingId(
							quote.getBaoxianUnderwritingId(),
							report.getBaoxianUnderwritingId());
					resultMap.put("id", report.getBaoxianUnderwritingId());
					resultMap.put("result", gson.toJson(result));
					resultMap.put("session", session);
				}
				return resultMap;
			}
		} catch (Exception e) {
			log.warn("提交信息{}数据到阳光", baseInfo != null ? baseInfo.getId() : "", e);
			resultMap.put("errorMessage", "系统忙");
			throw e;
		}

		return resultMap;
	}

	public Map<String, String> disposeReport(BaoxianUnderwriting w,
			String session) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", "400");

		w.setChannel("yangguang");
		BaoxianUnderwriting baoxianunderwriting = this.baoxianUnderwritingService
				.query(w.getId());
		if (baoxianunderwriting == null) {
			resultMap.put("errorMessage", "核保请求不存在");
			return resultMap;
		}
		w.setBaoxianBaseInfoId(baoxianunderwriting.getBaoxianBaseInfoId());
		w.setBaoxianCompanyCode(baoxianunderwriting.getBaoxianCompanyCode());
		w.setUserType(baoxianunderwriting.getUserType());
		w.setUserId(baoxianunderwriting.getUserId());
		if (!baoxianUnderwritingService.save(w)) {
			resultMap.put("errorMessage", "系统忙");
			return resultMap;
		}
		BaoxianCompany commpany = baoxianCompanyService.query(
				w.getBaoxianCompanyCode(), w.getCityCode());
		if (w == null || !"yangguang".equals(w.getChannel())) {
			resultMap.put("errorMessage", "投保公司配置异常");
			return resultMap;
		}
		if (commpany == null
                || commpany.getChannelStatus() == null
                || !commpany.getChannelStatus()
				|| (commpany.getOpenInfo() == null
						|| commpany.getOpenInfo() == -1 || (commpany
						.getOpenInfo() != 2 && commpany.getOpenInfo() != w
						.getUserType()))) {
			resultMap.put("errorMessage", "投保公司配置异常");
			return resultMap;
		}

		if (w.getJqxStartDate() == null)
			w.setJqxStartDate(new Date());
		if (w.getSyxStartDate() == null)
			w.setSyxStartDate(new Date());
		Gson gson = CommonUtil.gson();
		Map<String, Object> map = gson.fromJson(gson.toJson(w),
				new TypeToken<Map<String, Object>>() {
				}.getType());
		if (w.getJqxStartDate().getTime() < System.currentTimeMillis())
			w.setJqxStartDate(new Date());
		if (w.getSyxStartDate().getTime() < System.currentTimeMillis())
			w.setSyxStartDate(new Date());
		map.put("remark", w.getId());
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(new Date());
		nowCalendar.add(Calendar.DATE, 1);
		Calendar jqxCalendar = Calendar.getInstance();
		jqxCalendar.setTime(w.getJqxStartDate());
		Calendar syxCalendar = Calendar.getInstance();
		syxCalendar.setTime(w.getSyxStartDate());
		if (nowCalendar.getTime().getTime() > jqxCalendar.getTime().getTime()) {
			w.setJqxStartDate(nowCalendar.getTime());
		}
		if (nowCalendar.getTime().getTime() > syxCalendar.getTime().getTime()) {
			w.setSyxStartDate(nowCalendar.getTime());
		}
		map.put("PassengerIns", FanhuaConstant.PassengerIns);
		map.put("efcExpireDate", DateUtils.addDays(
				DateUtils.addYears(w.getSyxStartDate(), 1), -1));
		map.put("ScratchIns", FanhuaConstant.ScratchIns);
		map.put("DriverIns", FanhuaConstant.DriverIns);
		map.put("ScratchIns", FanhuaConstant.ScratchIns);
		map.put("ThirdPartyIns", FanhuaConstant.ThirdPartyIns);
		map.put("ThirdPartyIns", FanhuaConstant.ThirdPartyIns);

		try {
			map.put("baoxianunderwritingid", w.getId());
			map.put("jqxStartDate", w.getJqxStartDate());
			map.put("syxStartDate", w.getSyxStartDate());
			this.baoxianUnderwritingService.updateAutomatic(w.getId(),
					AutomaticStatus.PENDING, "请求处理");
			try {
				String message = postToYangguang("110", map, session,
						w.getUserId());
				log.info("提交核保信息数据{}阳光返回{}结果", w.getId(), message);
				message = parseBody(message, resultMap);

				String status = resultMap.get("status");
				if (!"100".equals(status)) {
					this.baoxianUnderwritingService.updateAutomatic(w.getId(),
							AutomaticStatus.REPORT_FAILED,
							resultMap.get("errorMessage"));
					this.baoxianUnderwritingService.updateStatusFail(w.getId(),
							"报价失败", 0);
				} else {
					this.baoxianUnderwritingService.updateAutomatic(w.getId(),
							AutomaticStatus.REPORT_SUCCEED, "已经报价");
				}

				if (StringUtils.hasText(message)) {
					resultMap.put("id", w.getId());
					Map<String, Map<String, String>> result = Xml2JsonUtil
							.xml2JSONByMap(message);
					disposeSaveReport(w, session, result);
					resultMap.put("result", gson.toJson(result));
					resultMap.put("session", session);
					return resultMap;
				}
			} catch (Exception e) {
				log.warn("提交{}数据到阳光", w.getId(), e);
				try {
					this.baoxianUnderwritingService.updateStatusFail(w.getId(),
							"报价失败", -1);
				} catch (Exception e1) {
				}
			}
		} catch (Exception e) {
			log.warn("提交{}数据到阳光", w.getId(), e);
			try {
				this.baoxianUnderwritingService.updateStatusFail(w.getId(),
						e.getMessage(), -1);
			} catch (Exception e1) {
			}
		}
		return resultMap;
	}

	public Map<String, String> disposeUnderwriting(String session, Operator op)
			throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", "400");

		try {
			Map<String, Object> map = Maps.newHashMap();
			BaoxianQuoteInfoWithBLOBs quote = this.baoxianQuoteInfoDAO
					.queryByQuoteId(session);
			if (quote != null) {
				map.put("baoxianunderwritingid",
						quote.getBaoxianUnderwritingId());
			}
			String message = postToYangguang("115", map, session,
					op.getUserId());

			message = parseBody(message, resultMap);
			String status = resultMap.get("status");
			if ("100".equals(status)) {
				quote = this.baoxianQuoteInfoDAO.queryByQuoteId(session);
				BaoxianUnderwriting t = new BaoxianUnderwriting();
				t.setId(quote.getBaoxianUnderwritingId());
				this.baoxianUnderwritingService.updateStatusReported(null, t);
			}

			if (StringUtils.hasText(message)) {
				resultMap.put("id", session);
				Map<String, Map<String, String>> result = Xml2JsonUtil
						.xml2JSONByMap(message);
				resultMap.put("result", CommonUtil.gson().toJson(result));
				resultMap.put("status", status);
				resultMap.put("session", session);
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("errorMessage", "系统忙");
		}
		return resultMap;
	}

	@Override
	public BaoxianUnderwritingReport disposeSaveReport(
			BaoxianUnderwriting info, String session,
			Map<String, Map<String, String>> resultReport) throws Exception {
		if (info == null)
			return null;

		// 保存核保请求
		BaoxianUnderwriting uninfo = CommonUtil.mapCopyToObject(resultReport,
				BaoxianUnderwriting.class);
		BaoxianUnderwritingReport report = CommonUtil.mapCopyToObject(
				resultReport, BaoxianUnderwritingReport.class);
		info = CommonUtil.simpleValueCopy(info, uninfo);
		uninfo.setCcs(true);
		if (uninfo.getJqx() == null || uninfo.getJqx() != true)
			uninfo.setJqx(report.getJqxPrice() != null
					&& report.getJqxPrice().compareTo(new BigDecimal(0)) > 0);
		this.baoxianUnderwritingService.save(uninfo);

		BaoxianUnderwriting underwriting = baoxianUnderwritingService
				.query(uninfo.getId());
		if (underwriting == null)
			return null;

		// 保存报价
		BaoxianUnderwritingReport oldReport = baoxianUnderwritingReportService
				.queryByUnderwritingId(info.getId());
		if (oldReport != null) {
			report.setId(oldReport.getId());
		}
		report.setBaoxianIntentId(info.getBaoxianIntentId());
		report.setBaoxianUnderwritingId(uninfo.getId());
		report.setBaoxianCompanyCode(underwriting.getBaoxianCompanyCode());
		report.setMobile(underwriting.getMobile());
		report.setBaoxianCompanyName(underwriting.getBaoxianCompanyName());
		report.setUserType(underwriting.getUserType());
		report.setCityCode(underwriting.getCityCode());
		report.setQuoteId(session);
		report.setUnderwritingStatus(0);
		report.setOperatorTime(new Date());
		report.setVehicleModelPrice(underwriting.getVehicleModelPrice());
		report.setUserId(underwriting.getUserId());
		try {
			report.setUn(new Double(resultReport.get("presentVal").get("value")));
		} catch (Exception e) {
		}
		report.setUpdateTime(new Date());
		report.setOperatorId("阳光核保");
		report.setChannel(info.getChannel());
		report.setExpressStatus(0);
		report.setEnableOrder(true);
		report.setCityName(underwriting.getCityName());
		report.setVehicleModelCode(underwriting.getVehicleModelCode());
		report.setVehicleModelName(underwriting.getVehicleModelName());
		report.setCreateTime(new Date());
		report.setStatus(0);
		report.setPayStatus(0);
		this.baoxianUnderwritingReportService.save(report);
		return report;
	}

	public Map<String, String> disposePeisong(String id, BaoxianPeisong info,
			String session, Operator op) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", "400");

		try {
			BaoxianUnderwriting underw = this.baoxianUnderwritingService
					.query(id);
			BaoxianUnderwritingReport report = this.baoxianUnderwritingReportService
					.queryByUnderwritingId(underw.getId());
			if (underw == null || !underw.getUserId().equals(op.getUserId())
					|| underw.getUserType().compareTo(op.getUserType()) != 0) {
				resultMap.put("errorMessage", "订单信息错误");
				return resultMap;
			}

			info.setId(null);
			if (!this.baoxianInsureInfoService.disposePeisong(underw, info)) {
				resultMap.put("errorMessage", "出错了，请稍后重试");
				return resultMap;
			}

			underw = this.baoxianUnderwritingService.query(id);
			BaoxianInsureInfo peisong = this.baoxianInsureInfoService
					.query(underw.getBaoxianInsureInfoId());
			Gson gson = CommonUtil.gson();
			BaoxianBaseInfo baseInfo = this.baoxianBaseInfoService.query(
					underw.getBaoxianBaseInfoId(), op.getUserId(),
					op.getUserType());
			if (!StringUtils.hasText(baseInfo.getToubaorenCardName()))
				baseInfo.setToubaorenCardName(baseInfo.getIdCardName());
			if (!StringUtils.hasText(baseInfo.getBeibaorenCardName()))
				baseInfo.setBeibaorenCardName(baseInfo.getIdCardName());
			if (!StringUtils.hasText(baseInfo.getBeibaorenCardNum()))
				baseInfo.setBeibaorenCardNum(baseInfo.getIdCardNum());
			if (!StringUtils.hasText(baseInfo.getToubaorenCardNum()))
				baseInfo.setToubaorenCardNum(baseInfo.getIdCardNum());
			Map<String, Object> map = gson.fromJson(gson.toJson(baseInfo),
					new TypeToken<Map<String, Object>>() {
					}.getType());
			if (peisong != null && info != null) {
				peisong.setPeisongProvince(info.getProvinceCode());
				peisong.setPeisongCity(info.getCityCode());
				peisong.setPeisongTown(info.getTownCode());
				if (StringUtils.isEmpty(peisong.getIdCardAddress())) {
					peisong.setIdCardAddress(peisong.getPeisongAddress());
				}
			}
			map.put("peisong", peisong);
			map.put("id", report.getId());
			map.put("baoxianunderwritingid", report.getBaoxianUnderwritingId());
			map.put("sendDate", DateUtils.addDays(new Date(), 7));
			String message = null;
			message = postToYangguang("120", map, session, op.getUserId());

			message = parseBody(message, resultMap);
			String status = resultMap.get("status");
			if (!"100".equals(status) && !"200".equals(status)) {
				this.baoxianUnderwritingService.updateYangguangFaid(
						report.getBaoxianUnderwritingId(),
						resultMap.get("errorMessage"));
				this.baoxianOrderService.updateStatus(id, -1);
			}

			if (StringUtils.hasText(message)) {
				String s[] = message.split("<ProposalNo>");
				String SyxPropNum = null;
				String JqxPropNum = null;
				if (s != null) {
					for (String ss : s) {
						if (ss.indexOf("</ProposalNo>") > 0) {
							String[] proposalnos = ss.split("</ProposalNo>");
							if (proposalnos != null)
								for (String st : proposalnos) {
									if (st.indexOf("<") < 0 && st != null
											&& st.length() > 0)
										if (ss.indexOf("type=\"force\"") >= 0)
											SyxPropNum = st;
										else
											JqxPropNum = st;
								}
						}
					}
					this.baoxianOrderService.updatePropNum(report.getId(),
                            JqxPropNum, SyxPropNum);
				}
				resultMap.put("id", session);
				resultMap.put(
						"result",
						CommonUtil.gson().toJson(
								Xml2JsonUtil.xml2JSONByMap(message)));
				resultMap.put("session", session);
				return resultMap;
			}
		} catch (Exception e) {
			log.warn("提交配送信息{}数据到阳光", id, e);
			resultMap.put("errorMessage", "系统忙");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> disposePay(String id, Operator op)
			throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", "400");
		try {
			BaoxianUnderwritingReport report = this.baoxianUnderwritingReportService
					.query(id);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("baoxianunderwritingid", report.getBaoxianUnderwritingId());
			String message = postToYangguang("125", map, report.getQuoteId(),
					op.getUserId());

			message = parseBody(message, resultMap);
			String status = resultMap.get("status");
			if (!"100".equals(status) && !"200".equals(status)) {
				this.baoxianUnderwritingService.updateYangguangFaid(
						report.getBaoxianUnderwritingId(),
						resultMap.get("errorMessage"));
				this.baoxianOrderService.updateStatus(id, -1);
			} else if ("100".equals(status)) {
				String bizProposalNo = null;
				int idx = message.indexOf("SubOrder type=\"biz\"");
				if (idx > 0) {
					idx = message.indexOf("<ProposalNo>", idx);
					if (idx > 0) {
						int endIdx = message.indexOf("</ProposalNo>", idx);
						if (endIdx > 0) {
							bizProposalNo = message.substring(idx
									+ "<ProposalNo>".length(), endIdx);
							idx = endIdx;
						}
					}
				}

				String forceProposalNo = null;
				idx = message.indexOf("SubOrder type=\"force\"");
				if (idx > 0) {
					idx = message.indexOf("<ProposalNo>", idx);
					if (idx > 0) {
						int endIdx = message.indexOf("</ProposalNo>", idx);
						if (endIdx > 0) {
							forceProposalNo = message.substring(idx
									+ "<ProposalNo>".length(), endIdx);
							idx = endIdx;
						}
					}
				}
				this.baoxianOrderService.updatePropNum(report.getId(), bizProposalNo,
                        forceProposalNo);

				resultMap.put(
						"result",
						CommonUtil.gson().toJson(
								Xml2JsonUtil.xml2JSONByMap(message)));

				String proposalNo = forceProposalNo;
				if (StringUtils.isEmpty(proposalNo)) {
					proposalNo = bizProposalNo;
				}
				// 获取投保人信息
				BaoxianUnderwriting underwriting = this.baoxianUnderwritingService
						.query(report.getBaoxianUnderwritingId());
				BaoxianBaseInfo baseInfo = this.baoxianBaseInfoService.query(
						underwriting.getBaoxianBaseInfoId(),
						underwriting.getUserId(), underwriting.getUserType());
				String toubaorenCardName = baseInfo.getIdCardName();

				String payUrl = MessageFormat.format(
						ordersUrl,
						new Object[] {
								EncryptDecryptData.encode(proposalNo),
								EncryptDecryptData
										.encodeChinese(toubaorenCardName) })
						.trim();
				resultMap.put("url", payUrl);
				this.baoxianOrderService.updateStatus(id, 1);
			}

			if (StringUtils.hasText(message)) {
				resultMap.put("id", report.getId());
			}
		} catch (Exception e) {
			resultMap.put("errorMessage", "系统忙");
		}

		Map<String, Object> ret = Maps.newHashMap();
		ret.putAll(resultMap);
		return ret;
	}

	@Override
	public Map<String, String> disposeLatestReport(String session) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("status", "400");

		BaoxianQuoteInfoWithBLOBs quote = this.baoxianQuoteInfoDAO
				.queryLastByQuoteIdAndStep(session, 110);
		if (quote == null) {
			quote = this.baoxianQuoteInfoDAO.queryLastByQuoteIdAndStep(session,
					105);
		}
		if (quote != null) {
			String message = quote.getResponse();
			if (StringUtils.hasText(message)) {
				message = message.replace("encoding=\"GBK\"",
						"encoding=\"utf-8\"");

				message = parseBody(message, resultMap);
				if (StringUtils.hasText(message)) {
					resultMap.put("id", quote.getBaoxianUnderwritingId());
					Map<String, Map<String, String>> result = Xml2JsonUtil
							.xml2JSONByMap(message);
					resultMap.put("result", CommonUtil.gson().toJson(result));
					resultMap.put("session", session);
					return resultMap;
				} else {
					resultMap.put("id", quote.getBaoxianUnderwritingId());
					resultMap.put("session", session);
					return resultMap;
				}
			}
		}
		return null;
	}

	public String postToYangguang(String requestType,
			Map<String, Object> tpmap, String session, String sellerId)
			throws Exception {
		if (tpmap == null)
			tpmap = new HashMap<String, Object>();
		PrivateKey ygPrivate = PartnerSignerUtil
				.getPrivateKey(PartnerSignerUtil.PARTNER_PRIVATE_KEY);
		String content = FreemarkerUtils.process(requestType + ".xml", tpmap,
				"/yangguang/");
		content = content.replace("\r", "").replace("  ", "").replace("\n", "")
				.replace("	", "").replace("Inputname", "Input name");
		content = content.substring(content.indexOf("<Request>"),
				content.lastIndexOf("</Request>") + "</Request>".length());
		String sign = PartnerSignerUtil
				.sign(content.getBytes("GBK"), ygPrivate);
		tpmap.put("SellerId", sellerId);
		if (!StringUtils.hasText(session) && requestType.equals("095")) {
			session = MD5Util.encrypt(CommonUtil.gson().toJson(tpmap), "utf-8");
		}
		tpmap.put("SendTime",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		tpmap.put("SessionId", session);
		tpmap.put("sign", sign);
		tpmap.put("RequestType", requestType);
		tpmap.put("content", content);
		content = FreemarkerUtils.process("baoxian.xml", tpmap, "/yangguang/");
		content = (content.replace("\r", "").replace("\n", "")
				.replace("  ", "").replace("	", "").replace("Inputname",
				"Input name")).trim();
		BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
		info.setId(SerializableUtils.allocUUID());
		info.setQuoteId(session);
		info.setRequest(content);
		info.setStatus(false);
		try {
			info.setBaoxianUnderwritingId(session);
			info.setBaoxianUnderwritingId(tpmap.get("baoxianunderwritingid")
					.toString());
		} catch (Exception e1) {
		}
		try {
			info.setStep(Integer.parseInt(requestType));
		} catch (Exception e1) {
		}
		try {
			info.setRequestTime(new Date());
			info.setRequestUrl(reportUrl);
			this.baoxianQuoteInfoDAO.insert(info);
		} catch (Exception e1) {
		}
		String message = null;
		try {
			message = HttpClientUtil.doPost(reportUrl, content, "gbk");
			info.setStatus(true);
		} catch (Exception e) {
			info.setStatus(false);
			log.warn("修改数据{}出错", info.getQuoteId(), e);
		}
		try {
			info.setResponse(message);
			info.setResponseTime(new Date());
			this.baoxianQuoteInfoDAO.updateReponse(info);
		} catch (Exception e) {
			this.baoxianQuoteInfoDAO.updateReponse(info);
			log.warn("修改数据{}出错", info.getQuoteId(), e);
		}
		return message;
	}

	private String parseBody(String message, Map<String, String> resultMap) {
		String status = "400";
		String errorMessage = null;

		if (StringUtils.hasText(message)) {
			try {
				status = message.substring(
						message.indexOf("<Status>") + "<Status>".length(),
						message.indexOf("</Status>")).trim();
				if (!"100".equals(status) && !"200".equals(status)) {
					errorMessage = message.substring(
							message.indexOf("<ErrorMessage>")
									+ "<ErrorMessage>".length(),
							message.indexOf("</ErrorMessage>"));
				} else {
					final int errorMessageIdx = message
							.indexOf("<ErrorMessage>");
					if (errorMessageIdx >= 0) {
						errorMessage = message.substring(errorMessageIdx
								+ "<ErrorMessage>".length(),
								message.indexOf("</ErrorMessage>"));
					}
				}
			} catch (Exception e) {
			}

			message = message.substring(message.indexOf("<Response>")
					+ "<Response>".length(), message.indexOf("</Response>"));

			if (message.indexOf("TagsList") > 0) {
				message = message.substring(message.indexOf("<TagsList>"),
						message.length());
			}
		}

		if (!StringUtils.hasText(message)) {
			message = "";
			if (StringUtils.isEmpty(errorMessage)) {
				errorMessage = "该车暂不支持阳光保险网上投保";
			}
		}

		resultMap.put("status", status);
		resultMap.put("errorMessage", normalizeError(status, errorMessage));

		return message;
	}

	private String normalizeError(String status, String errorMessage) {
		if (StringUtils.isEmpty(errorMessage))
			return errorMessage;

		if ("400".equals(status) || "300".equals(status)) {
			return errorMessage + "。请根据错误提示修正投保信息或选择其他保险公司核保。";
		}
		return errorMessage;
	}
}