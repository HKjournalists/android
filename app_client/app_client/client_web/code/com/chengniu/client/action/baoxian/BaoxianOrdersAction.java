package com.chengniu.client.action.baoxian;

import com.chengniu.client.action.SuperAction;
import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.DateUtils;
import com.chengniu.client.common.MediaDictionary;
import com.chengniu.client.common.PayType;
import com.chengniu.client.domain.AutomaticStatus;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.daze.intergation.dto.VerityRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kplus.orders.execption.DisposeException;
import com.kplus.orders.rpc.common.MD5Util;
import com.kplus.orders.rpc.dto.OrderPaymentDTO;
import com.kplus.orders.rpc.dto.PageVO;
import com.kplus.orders.rpc.service.OrdersRPCService;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaoxianOrdersAction extends SuperAction {
	private static final long serialVersionUID = -2027055836778011409L;

	@Autowired
	public BaoxianBaseInfoService baoxianBaseInfoService;
	@Autowired
	public BaoxianInsureInfoService baoxianInsureInfoService;
	@Autowired
	private BaoxianUnderwritingService baoxianUnderwritingService;
	@Autowired
	public BaoxianUnderwritingReportService baoxianUnderwritingReportService;
	@Autowired
	public BaoxianReportPriceService baoxianReportPriceService;
	@Autowired
	private BaoxianUnderwritingReportPayinfoService baoxianUnderwritingReportPayinfoService;
	@Autowired
	private BaoxianOrderService baoxianOrderService;
	@Autowired
	public BaoxianCompanyService baoxianCompanyService;
	@Resource(name = "ordersRPCService")
	private OrdersRPCService ordersRPCService;

	private String bankNo;
	private String bankCode;
	private String bankName;

	private String ssx;
	private String payway;
	private String type;
	private String carId;
	private String baseInfoId;
	private String zrx;
	private String orderNum;
	private String id;
	public String status;
	private String medias;

	private Integer payType; /* 支付类型 */
    private String channel;  /* 支付渠道 */

	private String baoxianUnderwritingId;
	private String baoxianUnderwritingReportId;

	public String listReport() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SearchVO vo = this.getSearch();
		vo.setUserType(this.getCurrentOperator().getUserType());
		if (!StringUtils.hasText(this.getCurrentOperator().getUserId())) {
			return super.ajax("参数错误", "250");
		}
		if ("0".equals(status))
			vo.setStatus("0");
		else if ("1".equals(status)) {
			vo.setStatus("1");
		} else if ("2".equals(status)) {
			vo.setStatus("1");
			vo.setPayStatus("1");
		}
		vo.setUserId(this.getCurrentOperator().getUserId());
		PageVO<BaoxianUnderwritingDTO> pageVO = baoxianUnderwritingService
				.page(vo);
		if (pageVO != null) {
			for (BaoxianUnderwritingDTO u : pageVO.getEntityList()) {
				if (u != null) {
					try {
						// 简读状态
						u.setAutomaticStatus(AutomaticStatus.simplifiedStatus(u
								.getAutomaticStatus()));

						BaoxianCompany company = this.baoxianCompanyService
								.query(u.getBaoxianCompanyCode(),
										u.getCityCode());
						try {
							if (u.getPayStatus() == null
									|| u.getPayStatus() != 1) {
								Date expireTime = this.baoxianOrderService
										.queryLastPayTime(u.getExpireTime());
								u.setExpireTime(expireTime);
								u.setPrintTime(this.baoxianOrderService
										.queryPrintTime(new Date(), expireTime));
								if (u.getExpireTime() == null)
									u.setExpireTime(expireTime);
								if (u.getExpireTime() != null
										&& (expireTime.getTime() > u
												.getExpireTime().getTime()
												|| expireTime.getTime() < System
														.currentTimeMillis() || u
												.getExpireTime().getTime() <= System
												.currentTimeMillis())) {
									u.setOrderStatus(-1);
								}
							}
						} catch (Exception e1) {
						}
						BaoxianBaseInfo baseInfo = this.baoxianBaseInfoService
								.query(u.getBaoxianBaseInfoId(), u.getUserId(),
										u.getUserType());
						u.setForecastTime(baoxianBaseInfoService
								.queryLastWork(u.getCreateTime()));
						if (company != null) {
							u.setBaoxianCompanyRemark(company.getRemark());
							u.setBaoxianCompanyPic(company.getPic());
						}
						if (baseInfo != null) {
							u.setIdCardName(baseInfo.getIdCardName());
							u.setVehicleNum(baseInfo.getVehicleNum());
						}
						if (u.getFailType() == null)
							u.setFailType(0);
						try {
							if (u.getSyxStartDate() != null)
								u.setStartDate(DateUtils.format(
										u.getSyxStartDate(), "yyyy-MM-dd"));
							if (u.getJqxStartDate() != null)
								u.setStartDate(DateUtils.format(
										u.getJqxStartDate(), "yyyy-MM-dd"));
						} catch (Exception e) {
						}
					} catch (Exception e) {
					}
					if ((u.getOrderStatus() != null && u.getOrderStatus() != -1)
							&& u.getStatus() != null && u.getStatus() == 1) {
						if (StringUtils.hasText(u.getQuoteId()))
							u.setPayType("fanhua");
						else
							u.setPayType("chengniu");
					}
					if (u.getBaoxianUnderwritingReportId() != null)
						u.setReportStatus(1);
				}
			}
			resultMap.put("list", pageVO.getEntityList());
			resultMap.put("total", pageVO.getCountRecords());
		} else
			resultMap.put("total", 0);
		resultMap.put("time",
				DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return super.ajax(resultMap);
	}

	public String queryStaticInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("toubao", this.baoxianUnderwritingReportService
				.queryCount(this.getCurrentOperator().getUserId(), this
						.getCurrentOperator().getUserType()));
		resultMap.put("hebaoSuccess", this.baoxianUnderwritingService
				.queryCount(1, this.getCurrentOperator().getUserId(), this
						.getCurrentOperator().getUserType()));
		resultMap.put("hebao", this.baoxianUnderwritingService.queryCount(0,
				this.getCurrentOperator().getUserId(), this
						.getCurrentOperator().getUserType()));
		return super.ajax(resultMap);
	}

	public String queryReport() throws Exception {
		BaoxianUnderwriting u = this.baoxianUnderwritingService.query(id);
		if (u == null)
			return super.ajax("信息错误");

		BaoxianCompany company = this.baoxianCompanyService.query(
				u.getBaoxianCompanyCode(), u.getCityCode());
		BaoxianUnderwritingDTO dto = CommonUtil.simpleValueCopy(u,
				BaoxianUnderwritingDTO.class);

		// 简读状态
		dto.setAutomaticStatus(AutomaticStatus.simplifiedStatus(u
				.getAutomaticStatus()));

		BaoxianBaseInfo baseInfo = this.baoxianBaseInfoService.query(
				u.getBaoxianBaseInfoId(), u.getUserId(), u.getUserType());
		if (company != null)
			dto.setBaoxianCompanyPic(company.getPic());
		if (baseInfo != null) {
			dto.setIdCardName(baseInfo.getIdCardName());
			dto.setVehicleNum(baseInfo.getVehicleNum());
		}
		BaoxianUnderwritingReport report = this.baoxianUnderwritingReportService
				.queryByUnderwritingId(u.getId());
		if (u.getLastAutomaticTime() != null) {
			dto.setForecastTime(baoxianBaseInfoService.queryLastWork(u
					.getLastAutomaticTime()));
		} else {
			dto.setForecastTime(baoxianBaseInfoService.queryLastWork(u
					.getCreateTime()));
		}
		if (report != null)
			dto.setReportStatus(1);
		dto.setInsureInfo(this.baoxianInsureInfoService.query(u
				.getBaoxianInsureInfoId()));
		BaoxinUnderwritingChangeLogDTO changeLogDTO = this.baoxianUnderwritingService
				.queryRequestChangesInfo(u.getId());
		dto.setRequest(changeLogDTO.getChangeLogMap());
		dto.setChangesRemark(changeLogDTO.getRemark());

		dto.setMediaList(MediaDictionary
				.fixMediaTemplate(this.baoxianBaseInfoService.queryMediaInfo(u
						.getBaoxianBaseInfoId())));
		if (report != null) {
			dto = CommonUtil.simpleValueCopy(report, dto); // 已经支付时间已经过期
			// 已经支付时间已经过期
			if (report.getPayStatus() == null || report.getPayStatus() != 1) {
				dto.setExpireTime(this.baoxianOrderService
						.queryLastPayTime(report.getExpireTime()));
				if (dto.getExpireTime() == null)
					dto.setExpireTime(report.getExpireTime());
				if (report.getExpireTime() != null
						&& (dto.getExpireTime().getTime() > report
								.getExpireTime().getTime()
								|| dto.getExpireTime().getTime() < System
										.currentTimeMillis() || report
								.getExpireTime().getTime() <= System
								.currentTimeMillis())) {
					report.setStatus(-1);
				}
			}
			dto.setPrintTime(this.baoxianOrderService
					.queryPrintTime(new Date(), dto.getExpireTime()));
			if (report.getPayStatus() != null && report.getPayStatus() == 1) {
				BaoxianUnderwritingReportPayinfo payinfo = new BaoxianUnderwritingReportPayinfo();
				if (report.getUserType() == 0
						&& StringUtils.hasText(report.getQuoteId())) {
					OrderPaymentDTO payment = ordersRPCService
							.queryOrderPaymentByOrderId(ordersRPCService
									.queryByOrderNum(report.getOrdersNum())
									.getId());
					if (payment != null) {
						if (payment.getMoney() == null)
							payment.setMoney(0f);
						if (payment.getCash() == null)
							payment.setCash(0f);
						payinfo.setPrice(CommonUtil.add(
								new BigDecimal(payment.getMoney()),
								new BigDecimal(payment.getCash())));
						payinfo.setPayWay(PayType.asReadableName(payment
								.getPayType()));
						payinfo.setCreateTime(DateUtils.parse(
								payment.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
						payinfo.setTradeNum(payment.getPayNum());
					} else {
						BaoxianUnderwritingReportPayinfo info = this.baoxianUnderwritingReportPayinfoService
								.queryByReportId(report.getId());
						if (info != null) {
							payinfo.setCreateTime(info.getCreateTime());
							payinfo.setPrice(info.getPrice());
							payinfo.setPayWay(info.getPayWay());
							payinfo.setTradeNum(info.getTradeNum());
						}
					}
				} else if (report.getUserType() == 1) {
					BaoxianUnderwritingReportPayinfo info = this.baoxianUnderwritingReportPayinfoService
							.queryByReportId(report.getId());
					if (info != null) {
						payinfo.setCreateTime(info.getCreateTime());
						payinfo.setPrice(info.getPrice());
						payinfo.setPayWay(info.getPayWay());
						payinfo.setTradeNum(info.getTradeNum());
					}
				}
				dto.setPayinfo(payinfo);
			}

			dto.setOrderStatus(report.getStatus());
			if (dto.getInsureInfo() != null
					&& !StringUtils.hasText(dto.getInsureInfo()
							.getBaoxianPeisongId()))
				dto.setInsureInfo(null);
			dto.setBaoxianUnderwritingReportId(report.getId());
			if (dto.getInsureInfo() != null) {
				dto.setExpress(this.baoxianInsureInfoService
						.queryByBaoxianUnderwritingReportId(dto
								.getBaoxianUnderwritingReportId()));
			}
		}
		dto.setId(u.getId());
		if (report != null
				&& (report.getStatus() == null || report.getStatus() == 1)) {
			if (StringUtils.hasText(report.getQuoteId()))
				dto.setPayType("fanhua");
			else
				dto.setPayType("chengniu");
		}
		dto.setStatus(u.getStatus());
		return super.ajax(dto);
	}

	public String report() throws Exception {
		BaoxianUnderwriting info = CommonUtil.gson().fromJson(
				this.getClientParam(), BaoxianUnderwriting.class);
		info.setBaoxianBaseInfoId(baseInfoId);
		info.setUserType(this.getCurrentOperator().getUserType());
		info.setUserId(this.getCurrentOperator().getUserId());
		info.fixFieldsDefaults();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		BaoxianCompany company = this.baoxianCompanyService.query(
				info.getBaoxianCompanyCode(),
				this.baoxianBaseInfoService.query(baseInfoId, info.getUserId(),
						info.getUserType()).getCityCode());
		if (company == null
                || (company.getChannelStatus() == null
                || !company.getChannelStatus())
				|| company.getOpenInfo() == null
				|| company.getOpenInfo() == -1
				|| (company.getOpenInfo().compareTo(2) != 0 && info
						.getUserType().compareTo(company.getOpenInfo()) != 0)) {
			return super.ajax(resultMap, "999", "保险公司不正确");
		}
		resultMap.put("result", this.baoxianUnderwritingService.save(info));
		resultMap.put("id", info.getId());
		return super.ajax(resultMap);
	}

	public String closeOrders() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", this.baoxianUnderwritingService.updateClose(id,
				this.getCurrentOperator().getUserId(), this
						.getCurrentOperator().getUserType()));
		return super.ajax(resultMap);
	}

	/**
	 * 补全资料
	 *
	 * @return
	 * @throws Exception
	 */
	public String fixMediaInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);

		try {
			List<BaoxianBaseInfoMedia> mediaList;
			Type listType = new com.google.gson.reflect.TypeToken<List<BaoxianBaseInfoMedia>>() {
			}.getType();
			mediaList = new GsonBuilder().setDateFormat("yyyy-MM-dd")
					.disableHtmlEscaping().create().fromJson(medias, listType);

			BaoxianUnderwriting underwriting = baoxianUnderwritingService
					.query(id);
			for (BaoxianBaseInfoMedia media : mediaList) {
				media.setId(underwriting.getBaoxianBaseInfoId());
				baoxianBaseInfoService.updateMedia(media);
			}

			resultMap.put("result", true);

		} catch (DisposeException e) {
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
		}

		return super.ajax(resultMap);
	}

	/**
	 * 补全资料后继续核保
	 *
	 * @return
	 * @throws Exception
	 */
	public String fixMediaInfoAndResume() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);

		try {
			List<BaoxianBaseInfoMedia> mediaList;
			Type listType = new com.google.gson.reflect.TypeToken<List<BaoxianBaseInfoMedia>>() {
			}.getType();
			mediaList = new GsonBuilder().setDateFormat("yyyy-MM-dd")
					.disableHtmlEscaping().create().fromJson(medias, listType);
			if (mediaList != null && !mediaList.isEmpty()) {
				BaoxianUnderwriting underwriting = baoxianUnderwritingService
						.query(id);
				for (BaoxianBaseInfoMedia media : mediaList) {
					media.setId(underwriting.getBaoxianBaseInfoId());
					baoxianBaseInfoService.updateMedia(media);
				}
			}

			resultMap.put("result", this.baoxianUnderwritingService
					.resumeReport(id, this.getCurrentOperator().getUserId(),
							this.getCurrentOperator().getUserType(), "补全证件"));

		} catch (DisposeException e) {
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
		}

		return super.ajax(resultMap);
	}

	/**
	 * 重新报价
	 *
	 * @return
	 * @throws Exception
	 */
	public String submit() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", this.baoxianUnderwritingService.disposeUnderwriting(id));
		return super.ajax(resultMap);
	}

	/**
	 * 提交配送地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String peisong() throws Exception {
		BaoxianPeisong info = CommonUtil.gson().fromJson(this.getClientParam(),
				BaoxianPeisong.class);
		info.setUserId(this.getCurrentOperator().getUserId());
		if (!StringUtils.hasText(baoxianUnderwritingId)
				&& StringUtils.hasText(baoxianUnderwritingReportId)) {
			baoxianUnderwritingId = this.baoxianUnderwritingReportService
					.query(baoxianUnderwritingReportId)
					.getBaoxianUnderwritingId();
		}
		info.setUserType(this.getCurrentOperator().getUserType());
		Map<String, Object> map = new HashMap<String, Object>();
        BaoxianUnderwriting underwriting = baoxianUnderwritingService.query(baoxianUnderwritingId);
		map.put("result", this.baoxianInsureInfoService.disposePeisong(
                underwriting, info));
		map.put("id", info.getId());

		return super.ajax(map);
	}

	public String saveOrder() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BaoxianUnderwritingReport report = baoxianOrderService.createOrder(id,
                this.getCurrentOperator().getUserId(), this
                        .getCurrentOperator().getUserType());
		if (report != null) {
			BaoxianOrdersDTO ordersDTO = baoxianOrderService
					.queryOrderSummary(report);

			resultMap.put("orderNum", ordersDTO.getOrdersNum());
			// quoteId不为空，说明是手动报价单
			if (StringUtils.hasText(report.getQuoteId())) {
				resultMap.put("id", report.getBaoxianUnderwritingId());
				resultMap.put("result", true);
			} else {
				if (report.getUserType() == 0) {
					if (ordersDTO != null) {
						resultMap.put("id", ordersDTO.getOrderId());
						resultMap.put("result", true);
					} else {
						resultMap.put("result", false);
					}
				} else {
					if (!StringUtils.isEmpty(ordersDTO.getOrdersNum())) {
						resultMap.put("id", ordersDTO.getOrdersNum());
						resultMap.put("result", true);
					} else {
						resultMap.put("result", false);
					}
				}
			}
			resultMap.put("message", ordersDTO.getMsg());
		} else {
			resultMap.put("message", "提交失败");
			resultMap.put("result", false);
		}
		return super.ajax(resultMap);
	}

	public String queryPayStatus() throws Exception {
		BaoxianUnderwritingReport report = this.baoxianUnderwritingReportService
				.query(id);
		if (report == null)
			return super.ajax(true);
		boolean payable = baoxianOrderService.checkOrderPayable(report);
		return super.ajax(!payable);
	}

	// 支付
	public String pay() throws Exception {
		BaoxianUnderwritingReport report = this.baoxianUnderwritingReportService
				.query(id);
		if (report == null)
			return null;

		Map<String, Object> ret = baoxianOrderService.initiatePayment(report,
				getCurrentOperator(), type, bankNo, bankName, bankCode);
		return super.ajax(ret);
	}

	public String payback() throws Exception {
		try {
			log.info("支付结果通知{}订单类型{}", orderNum, type);
			if ("1".equals(type)) {
				String sign = orderNum.substring(0, 32);
				orderNum = orderNum.replace(sign, "");
				if (!MD5Util.encrypt(orderNum, "utf-8").equals(sign))
					return this.ajax(true);
				else
					return this.ajax(false);
			}
			return super.ajax(this.baoxianOrderService.disposeOrderPayment(
					orderNum, 0, payway, true));
		} catch (Exception e) {
			log.error("支付结果修改出错", e);
		}
		return super.ajax(false);
	}

	/**
	 * 橙牛官方支付渠道
	 *
	 * 参数: id 核保ID payType 支付类型
	 *
	 * @return **错误1** {"code": "401", "msg": "缺少参数"}
	 *
	 *         **错误2** {"code": "407", "msg": "系统忙"}
	 *
	 *         **错误3** {"code": "405", "msg": "参数错误"}
	 *
	 * @throws Exception
	 */
	public String officialPay() throws Exception {
		// 必填参数
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(payType)) {
			return super.ajax(null, "401", "缺少参数");
		}

		try {
			BaoxianUnderwritingReport report = baoxianUnderwritingReportService
					.query(id);
			if (report == null) {
				return super.ajax(null, "405", "参数错误");
			}

			// 目前只支持B端，自动核保的泛华单
			boolean isChannelPayable = report.getUserType() == 1
					&& !"yangguang".equals(report.getChannel());
			if (!isChannelPayable) {
				return super.ajax(null, "405", "不能通过该方式支付");
			}

			Map<String, String> ret = baoxianOrderService
					.initiateChengniuPayment(report,
							PayType.valueOfB2CPayType(payType).value(),
                            channel,
							ServletActionContext.getRequest().getRemoteAddr());

			return super.ajax(ret.get("signature"), ret.get("code"),
					ret.get("msg"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.ajax(null, "407", "系统忙");
	}

	public String offficialPayback() throws Exception {
		Map<String, Object> map = ActionContext.getContext().getParameters();
		for (String key : map.keySet()) {
			map = new Gson().fromJson(key, HashMap.class);
		}
		String orderNo = (String) map.get("orderNo");
		log.info("订单：{}异步通知回调开始", orderNo);
		String notifyType = (String) map.get("notifyType");
		String orderStatus = (String) map.get("orderStatus");
		Long amount = Long.valueOf(map.get("amount").toString());
		String instCode = (String) map.get("instCode");
		String channelCode = (String) map.get("channelCode");
		String payType = (String) map.get("payType");
		String buyerMail = (String) (map.get("buyerMail"));

		// 入款通知处理
		if ("01".equals(notifyType) && "99".equals(orderStatus)) {
			VerityRequest veritify = new VerityRequest();
			veritify.setAmount(amount);
			veritify.setInstCode(instCode);
			veritify.setOrderNo(orderNo);
			veritify.setChannelCode(channelCode);
			veritify.setUserType("1");
			Map<String, String> extendsion = new HashMap<String, String>();
			extendsion.put("payType", payType);
			extendsion.put("buyerMail", buyerMail);
			veritify.setExtendsion(extendsion);

			try {
				baoxianOrderService.disposeOrderPayment(orderNo, 1, payType,
						true);
			} catch (Exception e) {
				log.warn("支付异步回调修改状态异常：", e);

				// 修改订单状态失败，将失败订单入库
				baoxianOrderService.handleOrdersOfficialPayFailed(veritify);
			}
		}

		try {
			PrintWriter w = ServletActionContext.getResponse().getWriter();
			w.write("success");
			w.flush();
			w.close();
		} catch (Exception e) {
			log.error("回应错误", e);
		}
		log.info("异步通知回调结束");

		return NONE;
	}

	public String closeReport() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", this.baoxianUnderwritingService.updateClose(id,
				this.getCurrentOperator().getUserId(), this
						.getCurrentOperator().getUserType()));
		return super.ajax(resultMap);
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setSsx(String ssx) {
		this.ssx = ssx;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public void setBaseInfoId(String baseInfoId) {
		this.baseInfoId = baseInfoId;
	}

	public void setZrx(String zrx) {
		this.zrx = zrx;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMedias(String medias) {
		this.medias = medias;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setBaoxianUnderwritingId(String baoxianUnderwritingId) {
		this.baoxianUnderwritingId = baoxianUnderwritingId;
	}

	public void setBaoxianUnderwritingReportId(
			String baoxianUnderwritingReportId) {
		this.baoxianUnderwritingReportId = baoxianUnderwritingReportId;
	}

}