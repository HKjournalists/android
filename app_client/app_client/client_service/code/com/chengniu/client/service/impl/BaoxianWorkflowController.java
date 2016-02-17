package com.chengniu.client.service.impl;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.DateUtils;
import com.chengniu.client.common.MediaDictionary;
import com.chengniu.client.common.Operator;
import com.chengniu.client.domain.AutomaticStatus;
import com.chengniu.client.domain.IntentStatus;
import com.chengniu.client.domain.QuoteRequestStatus;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 保险流程控制
 *
 * 1. 保存基本信息 1a. 保存信息 1b. 生成初步意愿
 *
 * 2. 补充信息
 *
 * 3. 提交报价方案 3a. 补全上年车船税缴税证明
 *
 * 4. 获取意向列表
 *
 * 5. 查看意向列表
 *
 * 6. 提交核保 6a. 保存配送地址 6b. 提交核保
 *
 * 7. 检查支付状态
 *
 * 8. 支付
 */
public class BaoxianWorkflowController {

	@Component
	private static class ApplicationContextHolder implements
			ApplicationContextAware {

		public static ApplicationContext sApplicationContext;

		public ApplicationContextHolder() {

		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext)
				throws BeansException {
			sApplicationContext = applicationContext;
		}
	}

	private BaoxianIntentService baoxianIntentService;
	private BaoxianInformalQuoteService baoxianInformalQuoteService;
	private BaoxianUnderwritingService baoxianUnderwritingService;
	private BaoxianUnderwritingReportService baoxianUnderwritingReportService;
	private BaoxianOrderService baoxianOrderService;
	private BaoxianBaseInfoService baoxianBaseInfoService;
	private BaoxianInsureInfoService baoxianInsureInfoService;

	private BaoxianCompanyService baoxianCompanyService;

	// 缓存的数据
	private BaoxianIntent intent;
	private BaoxianBaseInfo baseInfo;
	private BaoxianUnderwritingRequest underwritingRequest;
	private BaoxianUnderwriting underwriting;
	private BaoxianUnderwritingReport underwritingReport;

	public BaoxianWorkflowController() {
		final ApplicationContext ctx = ApplicationContextHolder.sApplicationContext;
		baoxianIntentService = (BaoxianIntentService) ctx
				.getBean("baoxianIntentService");
		baoxianInformalQuoteService = (BaoxianInformalQuoteService) ctx
				.getBean("baoxianInformalQuoteService");
		baoxianUnderwritingService = (BaoxianUnderwritingService) ctx
				.getBean("baoxianUnderwritingService");
		baoxianUnderwritingReportService = (BaoxianUnderwritingReportService) ctx
				.getBean("baoxianUnderwritingReportService");
		baoxianOrderService = (BaoxianOrderService) ctx
				.getBean("baoxianOrderService");
		baoxianBaseInfoService = (BaoxianBaseInfoService) ctx
				.getBean("baoxianBaseInfoService");
		baoxianInsureInfoService = (BaoxianInsureInfoService) ctx
				.getBean("baoxianInsureInfoService");

		baoxianCompanyService = (BaoxianCompanyService) ctx
				.getBean("baoxianCompanyService");
	}

	public BaoxianWorkflowController(String intentId) {
		this();
		BaoxianIntent intent = baoxianIntentService.query(intentId);
		if (intent == null) {
			throw new RuntimeException("参数错误");
		}
		this.intent = intent;
	}

	public BaoxianWorkflowController(BaoxianIntent intent) {
		this();

		this.intent = intent;
	}

	/**
	 * 创建意向
	 *
	 * @param baseInfo
	 * @return
	 */
	public BaoxianIntent submitInfoAndCreateIntent(BaoxianBaseInfo baseInfo) {
		if (intent != null) {
			throw new RuntimeException("不能创建多个意向");
		}

		BaoxianIntent intent = baoxianIntentService.submitIntent(baseInfo);
		this.intent = intent;

		return intent;
	}

	/**
	 * 补全用户信息
	 *
	 * @param info
	 * @return
	 */
	public boolean updateUserInfo(BaoxianBaseInfo info) {
		guardIntentExist();

		boolean ret = baoxianIntentService.updateUserInfo(intent, info);
		intent.setStatus(IntentStatus.QUOTING_LOST.getValue());
		baoxianIntentService.updateIntentStatus(intent);
		if (!ret) {
			return false;
		}
		return createDefaultQuoteRequest();
	}

	/**
	 * 创建多方报价请求
	 */
	private boolean createDefaultQuoteRequest() {
		guardIntentExist();
		ensureBaseInfoLoaded();

		boolean succ = baoxianInformalQuoteService.createQuoteRequest(intent,
				baseInfo);
		return succ;
	}

	/**
	 * 补全影像资料
	 * 
	 * @param mediaList
	 */
	public void updateUserMediaInfo(List<BaoxianBaseInfoMedia> mediaList)
			throws Exception {
		guardIntentExist();
		ensureRequestLoaded();
		// 报价阶段或初步的时候可以提交
		if (IntentStatus.isQuoteStage(intent.getStatus())
				|| IntentStatus.isInformalIntentStage(intent.getStatus())) {
			if (underwritingRequest != null
					&& !QuoteRequestStatus.isMediaFixable(underwritingRequest
							.getStatus())) {
				throw new RuntimeException("不能补全资料");
			}
		} else if (IntentStatus.isUnderwritingState(intent.getStatus())) {
			// ensureUnderwritingLoaded();

		} else {
			throw new RuntimeException("不能补全资料");
		}

		for (BaoxianBaseInfoMedia media : mediaList) {
			media.setId(intent.getBaoxianBaseInfoId());
			baoxianBaseInfoService.updateMedia(media);
		}

		if (IntentStatus.isUnderwritingState(intent.getStatus())) {
			ensureUnderwritingLoaded();
			baoxianUnderwritingService.resumeReport(underwriting.getId(), null,
					null, "补全证件");

			// 设置为核保中
			intent.setStatus(IntentStatus.UnderwritingPending.getValue());
			baoxianIntentService.updateIntentStatus(intent);

		} else if (IntentStatus.isQuoteStage(intent.getStatus())) {
			baoxianInformalQuoteService.resumeQuoteRequest(underwritingRequest);

			// 设置为报价中
			intent.setStatus(IntentStatus.Quoting.getValue());
			baoxianIntentService.updateIntentStatus(intent);
		}
	}

	/**
	 * 提交报价请求初审
	 * 
	 * @param request
	 * @return
	 */
	public boolean submitQuoteRequest(BaoxianUnderwritingRequest request) {
		guardIntentExist();
		ensureRequestLoaded();

		if (!IntentStatus.isInformalIntentStage(intent.getStatus())
				&& !IntentStatus.isQuoteStage(intent.getStatus())) {
			return false;
		}

		if (underwritingRequest != null
				&& !QuoteRequestStatus.isRequestCommitable(underwritingRequest
						.getStatus())) {
			return false;
		}

		// 修改请求为提交初审
		request.setId(underwritingRequest.getId());
		request.setStatus(QuoteRequestStatus.RequestSubmit.getValue());
		baoxianInformalQuoteService.updateQuoteRequest(request, true);

		// 修改意向状态为报价中
		intent.setStatus(IntentStatus.Quoting.getValue());
		intent.setFollowStatus(6);
		baoxianIntentService.updateIntentStatus(intent);

		return true;
	}

	/**
	 * 获取意向详情信息
	 * 
	 * @return
	 */
	public BaoxianIntentDTO loadIntentDetail() {
		guardIntentExist();

		BaoxianIntentDTO ret = new BaoxianIntentDTO();
		// 意向
		ret.setIntent(intent);

		// 基础信息
		BaoxianBaseInfo baseInfo = baoxianBaseInfoService.query(
				intent.getBaoxianBaseInfoId(), intent.getUserId(),
				intent.getUserType());
		ret.setBaseInfo(baseInfo);

		// 影像资料
		ret.setMediaList(MediaDictionary
				.fixMediaTemplate(this.baoxianBaseInfoService
						.queryMediaInfo(intent.getBaoxianBaseInfoId())));

		int status = intent.getStatus();
		if (IntentStatus.isQuoteStage(status)) { // 报价阶段
			BaoxianUnderwritingRequest request = baoxianInformalQuoteService
					.queryRequestByIntentId(intent.getId());
			if (request != null) {
				ret = CommonUtil.simpleValueCopy(request,
						BaoxianIntentDTO.class);
			}

			List<BaoxianInformalReport> reportList = baoxianInformalQuoteService
					.fetchQuoteList(request.getId());
			ret.setInformalReportList(reportList);
		} else {
			BaoxianUnderwriting underwriting = baoxianUnderwritingService
					.queryByIntentId(intent.getId());
			ret = CommonUtil.simpleValueCopy(underwriting, ret);
			if (underwriting.getLastAutomaticTime() != null) {
				ret.setForecastTime(baoxianBaseInfoService
						.queryLastWork(underwriting.getLastAutomaticTime()));
			} else {
				ret.setForecastTime(baoxianBaseInfoService
						.queryLastWork(underwriting.getCreateTime()));
			}
			// 简读状态
			ret.setAutomaticStatus(AutomaticStatus
					.simplifiedStatus(underwriting.getAutomaticStatus()));
			// 返回报价历史
			BaoxianUnderwritingRequest request = baoxianInformalQuoteService
					.queryRequestByIntentId(intent.getId());
			// if (request != null) {
			// ret = CommonUtil.simpleValueCopy(request,
			// BaoxianIntentDTO.class);
			// }
			List<BaoxianInformalReport> reportList = baoxianInformalQuoteService
					.fetchQuoteList(request.getId());
			ret.setInformalReportList(reportList);
			BaoxianCompany company = this.baoxianCompanyService.query(
					underwriting.getBaoxianCompanyCode(),
					underwriting.getCityCode());
			if (company != null) {
				ret.setBaoxianCompanyPic(company.getPic());
				ret.setBaoxianCompanyCode(company.getCode());
				ret.setBaoxianCompanyName(company.getCompanyName());
				ret.setBaoxianCompanyRemark(company.getRemark());
			}

			// 证件
			if (baseInfo != null) {
				ret.setIdCardName(baseInfo.getIdCardName());
				ret.setVehicleNum(baseInfo.getVehicleNum());
			}

			// 投保方案修改记录
			BaoxinUnderwritingChangeLogDTO changeLogDTO = this.baoxianUnderwritingService
					.queryRequestChangesInfo(underwriting.getId());
			ret.setRequest(changeLogDTO.getChangeLogMap());
			ret.setChangesRemark(changeLogDTO.getRemark());
			ret.setInsureInfo(this.baoxianInsureInfoService
					.queryByUnderwritingId(underwriting.getId()));
			// 报价
			BaoxianUnderwritingReport report = baoxianUnderwritingReportService
					.queryByUnderwritingId(underwriting.getId());
			if (report != null) {
				ret.setBaoxianUnderwritingReportId(report.getId());
				ret.setReportStatus(1);

				// 拷贝核保价格
				ret = CommonUtil.simpleValueCopy(report, ret); // 已经支付时间已经过期

				// 已经支付时间已经过期
				Date expireTime = null;
				if (report.getPayStatus() == null || report.getPayStatus() != 1) {
					expireTime = this.baoxianOrderService.queryLastPayTime(report.getExpireTime());
					if (expireTime == null
							|| (report.getExpireTime() != null && (expireTime
									.getTime() > report.getExpireTime()
									.getTime()))) {
						expireTime = report.getExpireTime();
					}
					if (report.getExpireTime() != null
							&& (expireTime.getTime() < System
									.currentTimeMillis() || report
									.getExpireTime().getTime() <= System
									.currentTimeMillis())) {
						report.setStatus(-1);
					}
				}
				ret.setExpireTime(expireTime);

				Date printTime = this.baoxianOrderService
						.queryPrintTime(new Date(), expireTime);
				ret.setPrintTime(printTime);

				// 支付信息
				if (report.getPayStatus() != null && report.getPayStatus() == 1) {
					BaoxianUnderwritingReportPayinfo payinfo = baoxianOrderService
							.queryOrderPayment(report);
					ret.setPayinfo(payinfo);
				}

				ret.setOrderStatus(report.getStatus());
				if (ret.getInsureInfo() != null
						&& !StringUtils.hasText(ret.getInsureInfo()
								.getBaoxianPeisongId())) {
					ret.setInsureInfo(null);
				}
				ret.setBaoxianUnderwritingReportId(report.getId());
				if (ret.getInsureInfo() != null) {
					ret.setExpress(this.baoxianInsureInfoService
							.queryByBaoxianUnderwritingReportId(ret
									.getBaoxianUnderwritingReportId()));
				}
			}

			ret.setId(underwriting.getId());
			if (report != null
					&& (report.getStatus() == null || report.getStatus() == 1)) {
				ret.setPayType("fanhua");
				if (report.isManualUnderwriting()) {
					ret.setPayType("chengniu");
				}
			}
			ret.setStatus(underwriting.getStatus());
		}

		return ret;
	}

	/**
	 * 加载意向摘要信息
	 *
	 * @return
	 */
	public BaoxianIntentSummaryDTO loadIntentSummary() {
		guardIntentExist();
		BaoxianIntentSummaryDTO ret = new BaoxianIntentSummaryDTO();
		ret.setId(intent.getId());
		ret.setStatus(intent.getStatus());
		// 基础信息
		BaoxianBaseInfo baseInfo = baoxianBaseInfoService.query(
				intent.getBaoxianBaseInfoId(), intent.getUserId(),
				intent.getUserType());
		ret.setVehicleNum(baseInfo.getVehicleNum());
		ret.setCityName(baseInfo.getCityName());
		ret.setSubmitTime(DateUtils.format(intent.getCreateTime(),
				DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
		int status = intent.getStatus();
		if (IntentStatus.isOrderStage(status)) { // 出单阶段
			BaoxianUnderwriting underwriting = baoxianUnderwritingService
					.queryByIntentId(intent.getId());
			if (underwriting.getLastAutomaticTime() != null) {
				ret.setForecastTime(baoxianBaseInfoService
						.queryLastWork(underwriting.getLastAutomaticTime()));
			} else {
				ret.setForecastTime(baoxianBaseInfoService
						.queryLastWork(underwriting.getCreateTime()));
			}

			ret.setUnderwritingCompanyCode(underwriting.getBaoxianCompanyCode());
			ret.setUnderwritingCompanyName(underwriting.getBaoxianCompanyName());

			BaoxianCompany company = this.baoxianCompanyService.query(
					underwriting.getBaoxianCompanyCode(),
					underwriting.getCityCode());
			if (company != null) {
				ret.setUnderwritingCompanyUrl(company.getPic());
				ret.setUnderwritingCompanyRemark(company.getRemark());
			}

			// 简读状态
			ret.setAutomaticStatus(AutomaticStatus
					.simplifiedStatus(underwriting.getAutomaticStatus()));
			ret.setUnderwritingStatus(underwriting.getStatus());

			// 报价
			BaoxianUnderwritingReport report = baoxianUnderwritingReportService
					.queryByUnderwritingId(underwriting.getId());
			if (report != null) {
				ret.setPayStatus(report.getPayStatus() != null ? report
						.getPayStatus() : 0);
				ret.setExpressStatus(report.getExpressStatus());
				if (report.getTotalPrice() != null) {
					ret.setActualPrice(report.getTotalPrice().toPlainString());
				}

				// 已经支付时间已经过期
				Date expireTime = null;
				if (report.getPayStatus() == null || report.getPayStatus() != 1) {
					expireTime = this.baoxianOrderService.queryLastPayTime(report.getExpireTime());
					if (expireTime == null) {
						expireTime = report.getExpireTime();
					}

					if (report.getExpireTime() != null
							&& (expireTime.getTime() > report.getExpireTime()
									.getTime()
									|| expireTime.getTime() < System
											.currentTimeMillis() || report
									.getExpireTime().getTime() <= System
									.currentTimeMillis())) {
						report.setStatus(-1);
						ret.setOrderStatus(-1);
					}
				}
				ret.setOrderStatus(report.getStatus());

				if (expireTime != null) {
					ret.setExpireTime(DateUtils.format(expireTime,
							DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
					Date printTime = this.baoxianOrderService.queryPrintTime(new Date(),
                            expireTime);
					ret.setPrintTime(DateUtils.format(printTime,
							DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
				}
			}

		} else if (IntentStatus.isUnderwritingState(status)) { // 核保阶段
			BaoxianUnderwriting underwriting = baoxianUnderwritingService
					.queryByIntentId(intent.getId());
			if (underwriting.getLastAutomaticTime() != null) {
				ret.setForecastTime(baoxianBaseInfoService
						.queryLastWork(underwriting.getLastAutomaticTime()));
			} else {
				ret.setForecastTime(baoxianBaseInfoService
						.queryLastWork(underwriting.getCreateTime()));
			}
			ret.setUnderwritingCompanyCode(underwriting.getBaoxianCompanyCode());
			ret.setUnderwritingCompanyName(underwriting.getBaoxianCompanyName());

			BaoxianCompany company = this.baoxianCompanyService.query(
					underwriting.getBaoxianCompanyCode(),
					underwriting.getCityCode());
			if (company != null) {
				ret.setUnderwritingCompanyUrl(company.getPic());
				ret.setUnderwritingCompanyRemark(company.getRemark());
			}

			// 简读状态
			ret.setAutomaticStatus(AutomaticStatus
					.simplifiedStatus(underwriting.getAutomaticStatus()));

			// 影像资料状态
			ret.setMediaStatus(underwriting.getMediaStatus());

			// 核保状态
			ret.setUnderwritingStatus(underwriting.getStatus());

			// 错误
			ret.setFailMessage(underwriting.getMessage());

			// 报价
			BaoxianUnderwritingReport report = baoxianUnderwritingReportService
					.queryByUnderwritingId(underwriting.getId());
			if (report != null) {
				if (report.getTotalPrice() != null) {
					ret.setQuotePrice(report.getTotalPrice().toPlainString());
				}

				// 已经支付时间已经过期
				Date expireTime = null;
				if (report.getPayStatus() == null || report.getPayStatus() != 1) {
					expireTime = this.baoxianOrderService.queryLastPayTime(report.getExpireTime());
					if (expireTime == null) {
						expireTime = report.getExpireTime();
					}

					if (report.getExpireTime() != null
							&& (expireTime.getTime() > report.getExpireTime()
									.getTime()
									|| expireTime.getTime() < System
											.currentTimeMillis() || report
									.getExpireTime().getTime() <= System
									.currentTimeMillis())) {
						report.setStatus(-1);
						ret.setOrderStatus(-1);
					}
				}
				if (expireTime != null) {
					ret.setExpireTime(DateUtils.format(expireTime,
							DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
					Date printTime = this.baoxianOrderService.queryPrintTime(new Date(),
                            expireTime);
					ret.setPrintTime(DateUtils.format(printTime,
							DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));
				}
			}

		} else if (IntentStatus.isQuoteStage(status)) { // 报价阶段
			ensureRequestLoaded();

			if (underwritingRequest != null) {
				ret.setFailType(underwritingRequest.getFailType());
				ret.setFailMessage(underwritingRequest.getFailMessage());
				List<BaoxianInformalReport> reportList = baoxianInformalQuoteService
						.fetchQuoteList(underwritingRequest.getId());
				if (reportList != null && !reportList.isEmpty()) {
					List<BaoxianInformalReportSummaryDTO> informalReportSummaryDTOList = Lists
							.newArrayList();
					for (BaoxianInformalReport informalReport : reportList) {
						BaoxianInformalReportSummaryDTO r = new BaoxianInformalReportSummaryDTO();

						r.setId(informalReport.getId());
						r.setStatus(informalReport.getStatus());
						r.setCompanyCode(informalReport.getBaoxianCompanyCode());
						r.setCompanyName(informalReport.getBaoxianCompanyName());

						if (informalReport.getTotalPrice() != null) {
							r.setTotalPrice(informalReport.getTotalPrice()
									.toPlainString());
						}

						BaoxianCompany company = this.baoxianCompanyService
								.query(informalReport.getBaoxianCompanyCode(),
										informalReport.getCityCode());
						if (company != null) {
							r.setCompanyUrl(company.getPic());
							r.setCompanyRemark(company.getRemark());
						}

						informalReportSummaryDTOList.add(r);
					}
					ret.setReportList(informalReportSummaryDTOList);
				}

				if (ret.getReportList() == null
						|| ret.getReportList().isEmpty()) {
					List<BaoxianInformalReportSummaryDTO> dummayReportList = fetchDummyReportList();
					ret.setReportList(dummayReportList);
				}
			}
		}
		return ret;
	}

	private List<BaoxianInformalReportSummaryDTO> fetchDummyReportList() {
		ensureRequestLoaded();

		if (underwritingRequest == null) {
			return null;
		}

		List<BaoxianInformalReportSummaryDTO> dummayReportList = Lists
				.newArrayList();
		List<BaoxianCompany> localCompanys = baoxianCompanyService.list(
				underwritingRequest.getCityCode(),
				underwritingRequest.getUserType());

		if (localCompanys != null) {
			for (BaoxianCompany company : localCompanys) {
				if (company.getOpenInfo() != null
                        && company.getChannelStatus() != null && company.getChannelStatus()
						&& (company.getOpenInfo() == underwritingRequest
								.getUserType() || company.getOpenInfo() == 2)) {
					BaoxianInformalReportSummaryDTO r = new BaoxianInformalReportSummaryDTO();

					r.setStatus(0);
					r.setCompanyCode(company.getCode());
					r.setCompanyName(company.getName());
					r.setCompanyUrl(company.getPicSmall());
					r.setCompanyRemark(company.getRemark());

					dummayReportList.add(r);
				}
			}
		}
		return dummayReportList;
	}

	/**
	 * 创建核保请求
	 * 
	 * @param reportId
	 * @return
	 */
	public boolean createUnderwriting(String reportId) {
		guardIntentExist();
		ensureUnderwritingLoaded();

		if (underwriting == null) {
			submitUnderwritingRequest(reportId);
		} else {
			if (!reportId.equals(underwriting.getBaoxianInformalReportId())) {
				baoxianUnderwritingService.resumeReport(underwriting, reportId);
			}
		}

		return underwriting != null;
	}

	/**
	 * 提交配送地址
	 *
	 * @param info
	 * @return
	 */
	public boolean submitDeliveryAddress(BaoxianPeisong info) {
		guardIntentExist();
		ensureUnderwritingLoaded();

		if (underwriting == null) {
			return false;
		}

		boolean ret = baoxianInsureInfoService.disposePeisong(
                underwriting, info);
		return ret;
	}

	/**
	 * 提交核保请求
	 * 
	 * @param reportId
	 */
	public void submitUnderwritingRequest(String reportId) {
		guardIntentExist();
		ensureRequestLoaded();

		if (!IntentStatus.isQuoteStage(intent.getStatus())) {
			throw new RuntimeException("当前状态无法提交核保");
		}

		BaoxianInformalReport report = baoxianInformalQuoteService
				.queryInformalReport(reportId);

		if (!baoxianInformalQuoteService.validateInformalReport(report)) {
			throw new RuntimeException("报价已过期，请重新报价");
		}

		// 创建核保请求
		BaoxianUnderwriting underwriting = CommonUtil.simpleValueCopy(
				underwritingRequest, BaoxianUnderwriting.class);
		underwriting.setBaoxianBaseInfoId(intent.getBaoxianBaseInfoId());
		underwriting.setBaoxianInformalReportId(report.getId());
		underwriting.setUserType(intent.getUserType());
		underwriting.setUserId(intent.getUserId());
		underwriting.setCityCode(report.getCityCode());
		underwriting.setCityName(report.getCityName());
		underwriting.setBaoxianCompanyCode(report.getBaoxianCompanyCode());
		underwriting.setBaoxianCompanyName(report.getBaoxianCompanyName());
		underwriting.setChannel(report.getChannel());
		// 需要知道保险公司是否支持自动核保
		underwriting.setAutomaticStatus(0);
		underwriting.setSupportAutomatic(true);
		underwriting.fixFieldsDefaults();
		boolean ret = baoxianUnderwritingService.save(underwriting);
		if (!ret) {
			throw new RuntimeException("提交核保请求失败");
		}

		// 修改意向状态
		intent.setStatus(IntentStatus.UnderwritingPending.getValue());
		baoxianIntentService.updateIntentStatus(intent);

        baoxianInformalQuoteService.cancelPendingReport(underwritingRequest.getId());

        this.underwriting = underwriting;
	}

	/**
	 * 创建订单
	 * 
	 * @return
	 */
	public BaoxianOrdersDTO createOrder(Operator operator) {
		guardReportExist();

		BaoxianUnderwritingReport report = baoxianOrderService.createOrder(
                underwritingReport.getId(), operator.getUserId(),
                operator.getUserType());
		if (report != null) {
			BaoxianOrdersDTO ret = baoxianOrderService
					.queryOrderSummary(report);
			if (ret != null && ret.isValid()) {
				ret.setIntentId(intent.getId());

				// 修改状态为待支付
				if (!IntentStatus.isOrderStage(intent.getStatus())) {
					intent.setStatus(IntentStatus.PayPending.getValue());
					baoxianIntentService.updateIntentStatus(intent);
				}
			}
			return ret;
		}

		return null;
	}

	/**
	 * 检查支付状态
	 * 
	 * @return
	 */
	public boolean checkPayable() {
		guardReportExist();

		boolean ret = baoxianOrderService.checkOrderPayable(underwritingReport);
		return ret;
	}

	/**
	 * 发起支付
	 * 
	 * @param type
	 * @param bankNo
	 * @param bankCode
	 * @param bankName
	 * @return
	 */
	public Map<String, Object> initiatePayment(Operator operator, String type,
			String bankNo, String bankCode, String bankName) {
		guardReportExist();
		return baoxianOrderService.initiatePayment(underwritingReport,
				operator, type, bankNo, bankCode, bankName);
	}

	/**
	 * 确保intent存在
	 */
	private void guardIntentExist() {
		if (intent == null) {
			throw new RuntimeException("参数错误");
		}
	}

	/**
	 * 确保报价请求已加载
	 */
	private void ensureRequestLoaded() {
		if (underwritingRequest == null) {
			underwritingRequest = baoxianInformalQuoteService
					.queryRequestByIntentId(intent.getId());
		}
	}

	private void ensureBaseInfoLoaded() {
		guardIntentExist();
		if (baseInfo == null) {
			baseInfo = baoxianBaseInfoService.query(
					intent.getBaoxianBaseInfoId(), intent.getUserId(),
					intent.getUserType());
		}
	}

	/**
	 * 确保核保请求已加载
	 */
	private void ensureUnderwritingLoaded() {
		if (underwriting == null) {
			underwriting = baoxianUnderwritingService.queryByIntentId(intent
					.getId());
		}
	}

	/**
	 * 确保核保报价已加载
	 */
	private void ensureReportLoaded() {
		guardUnderwritingExist();

		if (underwritingReport == null) {
			underwritingReport = baoxianUnderwritingReportService
					.queryByUnderwritingId(underwriting.getId());
		}
	}

	/**
	 * 确保核保请求存在
	 */
	private void guardUnderwritingExist() {
		ensureUnderwritingLoaded();
		if (underwriting == null) {
			throw new RuntimeException("参数错误");
		}
	}

	/**
	 * 确保核保报价存在
	 */
	private void guardReportExist() {
		ensureReportLoaded();
		if (underwritingReport == null) {
			throw new RuntimeException("参数错误");
		}
	}

}
