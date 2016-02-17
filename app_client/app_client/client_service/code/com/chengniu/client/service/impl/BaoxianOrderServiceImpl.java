package com.chengniu.client.service.impl;

import com.chengniu.client.common.*;
import com.chengniu.client.dao.BaoxianUnderwritingDateExceptionDAO;
import com.chengniu.client.dao.BaoxianUnderwritingReportDAO;
import com.chengniu.client.dao.BaoxianUnderwritingReportOperationRecordDAO;
import com.chengniu.client.domain.IntentStatus;
import com.chengniu.client.domain.Partner;
import com.chengniu.client.event.OrderPayStatusChangedEvent;
import com.chengniu.client.event.UnderwritingSuccessEvent;
import com.chengniu.client.event.dispatcher.EventDispatcher;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.daze.intergation.dto.*;
import com.daze.intergation.rpc.service.FundRPCFacade;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;
import com.kplus.orders.rpc.dto.OrderPaymentDTO;
import com.kplus.orders.rpc.dto.OrdersDTO;
import com.kplus.orders.rpc.service.OrdersRPCService;
import com.providers.rpc.pojo.OrderBaseInfoDto;
import com.providers.rpc.service.OrderBaseInfoRPCService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单服务
 */
@Service("baoxianOrderService")
public class BaoxianOrderServiceImpl implements BaoxianOrderService {

	protected static final Logger log = LogManager
			.getLogger(BaoxianOrderServiceImpl.class);

	@Autowired
	private BaoxianUnderwritingReportService baoxianUnderwritingReportService;

	@Autowired
	private BaoxianUnderwritingReportPayinfoService baoxianUnderwritingReportPayinfoService;

	@Autowired
	private BaoxianInformalQuoteService baoxianInformalQuoteService;

	@Autowired
	private BaoxianUnderwritingService baoxianUnderwritingService;

    @Autowired
    private BaoxianUnderwritingReportDAO baoxianUnderwritingReportDAO;

    @Autowired
    private BaoxianUnderwritingReportOperationRecordDAO baoxianUnderwritingReportOperationRecordDAO;

    @Autowired
    private BaoxianUnderwritingDateExceptionDAO baoxianUnderwritingDateExceptionDAO;

    @Autowired
    private BaoxianInsureInfoService baoxianInsureInfoService;

	@Autowired
	private FanhuaService fanhuaService;

    @Autowired
    private BaoxianApi fanhuaApi;

	@Autowired
	private YangguangService yangguangService;

	@Autowired
	private EventDispatcher eventDispatcher;

	@Resource(name = "ordersRPCService")
	private OrdersRPCService ordersRPCService;

	@Resource(name = "ordersBaseInfoRPCService")
	private OrderBaseInfoRPCService ordersBaseInfoRPCService;

	@Resource(name = "fundRPCFacade")
	private FundRPCFacade fundRPCFacade;

	@Value("${pay.callback.url}")
	private String payCallbackUrl;

	private EventHandler eventHandler = new EventHandler();

	@PostConstruct
	public void init() {
		eventDispatcher.register(eventHandler);
	}

	@Override
	public BaoxianUnderwritingReport createOrder(String reportId,
                                                 String userId, Integer userType) {
		BaoxianUnderwritingReport report = baoxianUnderwritingReportService
				.query(reportId);
		if (report == null || !report.getUserId().equals(userId)
				|| report.getUserType().compareTo(userType) != 0) {
			return null;
		} else {
			if (report.getStatus() != null && report.getStatus() == 1)
				return report;
			report.setOrdersTime(new Date());
			report.setPayStatus(0);
			report.setUpdateTime(new Date());
			if (userType == 0) {
				OrdersDTO orders = null;
				if (report.getStatus() == null || report.getStatus() != 1) {
					orders = new OrdersDTO();
					orders.setOrderTypeId(8);
					orders.setCityCode(report.getCityCode());
					orders.setUid(Long.parseLong(userId));
					orders.setContactPhone(report.getMobile());
					orders.setStatus(2);
					orders.setContactName(report.getMobile());
					orders.setPrice(report.getTotalPrice().floatValue());
					orders.setContent((report.getBaoxianCompanyName() != null ? report
							.getBaoxianCompanyName() : "")
							+ "保险订单" + report.getId());
					orders = ordersRPCService.createOrders(orders);
				} else {
					orders = this.ordersRPCService.queryByOrderNum(report
							.getOrdersNum());
				}
				report.setStatus(1);
				report.setBaoxianUnderwritingId(String.valueOf(orders.getId()));
				report.setOrdersNum(orders.getOrderNum());
			} else {
				report.setStatus(1);
				try {
					OrderBaseInfoDto arg0 = new OrderBaseInfoDto();
					arg0.setAmount(report.getTotalPrice()
							.multiply(new BigDecimal(100)).intValue());
					arg0.setOrderType("3");
					arg0.setBuyerCode(report.getUserId());
					arg0.setSellerCode(report.getBaoxianCompanyName());
					arg0.setStatus("19");
					arg0.setGoodsName((report.getBaoxianCompanyName() != null ? report
							.getBaoxianCompanyName() : "")
							+ "保险订单" + report.getId());
					arg0.setOutOrderNo(report.getId());
					report.setOrdersNum(ordersBaseInfoRPCService.newOrderInfo(
							arg0).getData());
				} catch (Exception e) {
					log.info("订单接口错误", e);
					report.setOrdersNum(SerializableUtils.serializable());
				}
			}
			this.updateSubmitOrders(report);
		}
		return report;
	}

    @Override
    public void updateSubmitOrders(BaoxianUnderwritingReport report) {
        baoxianUnderwritingReportDAO.updateSubmitOrders(report);
    }

    @Override
    public boolean updatePayStatus(BaoxianUnderwritingReport report) {
        return this.baoxianUnderwritingReportDAO.updatePayStatus(report) > 0;
    }

    @Override
    public boolean updateStatus(String id, int status) {
        BaoxianUnderwritingReport t = baoxianUnderwritingReportService.query(id);
        if (t == null)
            return false;
        if (t.getPayStatus() != null && t.getPayStatus() == 1)
            if (status == -1)
                return false;
        t.setStatus(status);
        return this.baoxianUnderwritingReportDAO.updateStatus(t) >= 0;
    }

    @Override
    public boolean updateExpressStatus(String id,
                                       BaoxianUnderwritingExpress info) {
        BaoxianUnderwritingReport t = baoxianUnderwritingReportService.query(id);
        if (t == null)
            return false;
        if (info != null) {
            if ((t.getExpressStatus() != null && t.getExpressStatus() == 1)) {
                return true;
            }
            t.setExpressStatus(1);
            t.setBaoxianUnderwritingExpressId(info.getId());
        } else
            t.setExpressStatus(-1);
        return this.baoxianUnderwritingReportDAO.updateExpressStatus(t) >= 0;
    }

    public boolean updateInsuerInfo(String id, String insuerInfoId) {
        BaoxianUnderwritingReport report = this.baoxianUnderwritingReportDAO
                .query(id);
        if (report == null
                || StringUtils.hasText(report.getBaoxianInsureInfoId()))
            return false;
        report.setBaoxianInsureInfoId(insuerInfoId);
        return this.baoxianUnderwritingReportDAO.updateInsuerInfoStatus(report) > 0;
    }

    @Override
    public boolean updatePolicyStatus(String baoxianUnderwritingId, String quoteId, JsonObject result) {
        String state = result.get("state").getAsString();
        if (!"VerifySuccess".equalsIgnoreCase(state)
                && !"Payed".equalsIgnoreCase(state)
                && !"UnderWriteSuccess".equalsIgnoreCase(state)
                && !"Interrupted".equalsIgnoreCase(state)
                && !"Cancelled".equalsIgnoreCase(state)
                && !"PayIng".equalsIgnoreCase(state)
                && !"Cancelled PayIng".equalsIgnoreCase(state)
                && !"UnderWriteFailed".equalsIgnoreCase(state)
                && !"Finished".equalsIgnoreCase(state))
            return false;
        BaoxianUnderwritingReport report = baoxianUnderwritingReportService.queryByUnderwritingId
                (baoxianUnderwritingId);
        // 没有收到报价
        if (report == null) {
            report = baoxianUnderwritingReportService.disposeFanhuaReport(baoxianUnderwritingId,
                    quoteId, result);
        }
        report.setPolicyStatus(state);

        boolean alreadyUnderwrited = report.getUnderwritingStatus() != null
                && report.getUnderwritingStatus() == 1;
        if ("Interrupted".equalsIgnoreCase(state)
                || "PayIng".equalsIgnoreCase(state)
                || "Cancelled PayIng".equalsIgnoreCase(state)
                || "UnderWriteSuccess".equalsIgnoreCase(state)
                || "Finished".equalsIgnoreCase(state))
            report.setUnderwritingStatus(1);
        else if ("UnderWriteFailed".equalsIgnoreCase(state))
            report.setUnderwritingStatus(-1);
        else
            report.setUnderwritingStatus(0);
        try {
            JsonObject dealback = result.get("dealBack").getAsJsonObject();
            try {
                report.setJqxPolicyNo(dealback.get("efcPolicyCode")
                        .getAsString());
                report.setSyxPolicyNo(dealback.get("bizPolicyCode")
                        .getAsString());
            } catch (Exception e1) {
            }
            report.setExpireTime(com.kplus.orders.rpc.common.DateUtils.parse(dealback.get("payLimitDate")
                    .getAsString(), "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
        }
        boolean succ = this.baoxianUnderwritingReportDAO
                .updatePolicyStatus(report) > 0;

        // 发送已承包通知
        if (!alreadyUnderwrited && "UnderWriteSuccess".equalsIgnoreCase(state)) {
            log.debug("发布已承保事件 reportId:{}", report.getId());
            eventDispatcher.publish(UnderwritingSuccessEvent.create(report));
        }
        return succ;
    }

    public boolean updatePropNum(String id, String jqxPolicyNo,
                                 String syxPolicyNo) {
        BaoxianUnderwritingReport report = baoxianUnderwritingReportService.query(id);
        report.setJqxPolicyNo(jqxPolicyNo);
        report.setSyxPolicyNo(syxPolicyNo);
        return this.baoxianUnderwritingReportDAO.updatePropNum(report) > 0;
    }

    @Deprecated
    public boolean updatePolicyNo(String id, String policyNo) {
        // 保单是两个的
        BaoxianUnderwritingReport report = baoxianUnderwritingReportService.query(id);
        report.setPolicyNo(policyNo);
        return this.baoxianUnderwritingReportDAO.updatePolicyNo(report) > 0;
    }

    public Date queryLastPayTime(Date date) {
        try {
            StringBuilder sb = new StringBuilder();
            BaoxianUnderwritingDateException dateinfo = this.baoxianUnderwritingDateExceptionDAO
                    .queryNextWorkDay(date);
            sb.append(com.kplus.orders.rpc.common.DateUtils.format(dateinfo.getDate(), "yyyy-MM-dd "));
            sb.append("17:00:00");
            return com.kplus.orders.rpc.common.DateUtils.parse(sb.toString(), "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Date queryPrintTime(Date date, Date expireTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if (hour >= 17 && minute >= 0) {
            c.add(Calendar.DATE, 1);
        }
        StringBuilder sb = new StringBuilder();
        BaoxianUnderwritingDateException dateinfo = this.baoxianUnderwritingDateExceptionDAO
                .queryWorkDayByDate(c.getTime());
        sb.append(com.kplus.orders.rpc.common.DateUtils.format(dateinfo.getDate(), "yyyy-MM-dd "));
        sb.append("17:00:00");
        Date printTime = com.kplus.orders.rpc.common.DateUtils.parse(sb.toString(), "yyyy-MM-dd HH:mm:ss");
        if (expireTime != null && printTime.getTime() > expireTime.getTime())
            printTime = expireTime;
        if (printTime.getTime() < System.currentTimeMillis())
            printTime = null;
        return printTime;
    }

	@Override
	public BaoxianOrdersDTO queryOrderSummary(BaoxianUnderwritingReport report) {
		BaoxianOrdersDTO ret = new BaoxianOrdersDTO();
		ret.setValid(false);
		ret.setOrdersNum(report.getOrdersNum());
		if (!report.isManualUnderwriting()) { // 自动核保
			ret.setUnderwritingId(report.getBaoxianUnderwritingId());
			ret.setValid(true);
		} else { // 手动核保
			if (report.getUserType() == BaoxianBaseInfo.USER_TYPE.PERSON.value) {
				// C端创建订单
				OrdersDTO order = ordersRPCService.queryByOrderNum(report
						.getOrdersNum());
				if (order != null) {
					ret.setOrderId("" + order.getId());
					ret.setValid(true);
				} else {
					ret.setValid(false);
				}
			} else {
				// 商户端字段核保不需走自有的支付，不创建订单
				if (!StringUtils.isEmpty(report.getOrdersNum())) {
					ret.setOrdersNum(report.getOrdersNum());
					ret.setValid(true);
				} else {
					ret.setValid(false);
				}
			}
		}
		ret.setMsg(report.getMessage());

		return ret;
	}

	@Override
	public BaoxianUnderwritingReportPayinfo queryOrderPayment(
			BaoxianUnderwritingReport report) {
		OrderPaymentDTO payment = null;
		if (report.isManualUnderwriting() && report.getUserType() == 0) {
			Long orderId = ordersRPCService.queryByOrderNum(
					report.getOrdersNum()).getId();
			payment = ordersRPCService.queryOrderPaymentByOrderId(orderId);
			if (payment != null) {
				if (payment.getMoney() == null) {
					payment.setMoney(0f);
				}
				if (payment.getCash() == null) {
					payment.setCash(0f);
				}
			}
		}
		BaoxianUnderwritingReportPayinfo payinfo = new BaoxianUnderwritingReportPayinfo();
		if (payment != null) {
			payinfo.setPrice(CommonUtil.add(new BigDecimal(payment.getMoney()),
					new BigDecimal(payment.getCash())));
			payinfo.setPayWay(PayType.asReadableName(payment.getPayType()));
			payinfo.setCreateTime(DateUtils.parse(payment.getCreateTime(),
                    "yyyy-MM-dd HH:mm:ss"));
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
		return payinfo;
	}

	@Override
	public boolean checkOrderPayable(BaoxianUnderwritingReport report) {
		if (report == null) {
			return false;
		}

		if (report.getPayStatus() == null || report.getPayStatus() != 1) {
			BaoxianUnderwritingReportPayinfo pay = baoxianUnderwritingReportPayinfoService
					.queryByReportId(report.getId());
			if (pay != null && pay.getStatus() != null && pay.getStatus() == 1) {
				return false;
			}

			if (report.isManualUnderwriting()) {
				try {
					disposeOrderPayment(report.getOrdersNum(),
							report.getUserType(), null, false);
				} catch (Exception e) {

				}
			} else {
				// 自动核保的单，如果是未支付，先同步第三方支付状态
				try {
					BaoxianUnderwriting underwriting = baoxianUnderwritingService
							.query(report.getBaoxianUnderwritingId());
					baoxianInformalQuoteService.disposeQuery(underwriting
							.getBaoxianInformalReportId());
					report = baoxianUnderwritingReportService.query(report
							.getId());
				} catch (Exception e) {
				}
			}

			pay = baoxianUnderwritingReportPayinfoService
					.queryByReportId(report.getId());
			if (pay != null && pay.getStatus() != null && pay.getStatus() == 1) {
				return false;
			}
		}

		return (report.getPayStatus() == null || report.getPayStatus() != 1);
	}

	@Override
	public Map<String, Object> initiatePayment(
			BaoxianUnderwritingReport underwritingReport, Operator operator,
			String type, String bankNo, String bankCode, String bankName) {
		Map<String, Object> resultMap = Maps.newHashMap();
		if ("yangguang".equals(underwritingReport.getChannel())) {
			try {
				Map<String, Object> result = yangguangService.disposePay(
						underwritingReport.getId(), operator);
				resultMap.putAll(result);

                // 记录日志
                saveOperationRecord(underwritingReport.getId(), Partner.USER,
                        "尝试使用阳光支付", null);

			} catch (Exception e) {
				resultMap.put("result", false);
			}
		} else {
			try {
				Map<String, Object> result = fanhuaService.disposePay(
						underwritingReport.getId(), type, bankNo, bankName,
						bankCode);
				resultMap.putAll(result);

                // 记录日志
                saveOperationRecord(underwritingReport.getId(), Partner.USER,
                        "尝试使用泛华"+type+"支付", null);

			} catch (Exception e) {
				resultMap.put("result", false);
			}
		}
		return resultMap;
	}

	@Override
	public Map<String, String> initiateChengniuPayment(
            BaoxianUnderwritingReport report, Integer payType, String channel, String remoteIp) {
		Map<String, String> ret = Maps.newHashMap();

		boolean isPaid = (report.getPayStatus() != null && report
				.getPayStatus() == 1);
		if (isPaid) {
			ret.put("code", "405");
			ret.put("msg", "该订单已支付，请求耐心等待处理结果");
			return ret;
		}

		boolean isReportSucces = (report.getStatus() != null && report
				.getStatus() == 1);
		if (!isReportSucces) {
			ret.put("code", "405");
			ret.put("msg", "该订单暂时无法支付，请等待核保完成");
			return ret;
		}

		boolean isReportExpired = !checkValidity(report);
		if (isReportExpired) {
			updateStatus(report.getId(), -1);

			ret.put("code", "405");
			ret.put("msg", "该报价已失效，无法进行支付");
			return ret;
		}
		createOrder(report.getId(), report.getUserId(), report.getUserType());

		BaoxianUnderwritingReportPayinfo pay = baoxianUnderwritingReportPayinfoService
				.queryByReportId(report.getId());
		if (pay == null || pay.getStatus() == null
				|| (pay.getStatus() == 0 || pay.getStatus() == -1)) {
			disposeOrderPayment(report.getOrdersNum(), report.getUserType(),
					payType.toString(), false);
			pay = baoxianUnderwritingReportPayinfoService
					.queryByReportId(report.getId());
		}

		if (pay != null && pay.getStatus() != null && pay.getStatus() == 1) {
			ret.put("code", "502");
			ret.put("msg", "该订单已支付，请勿重新支付");
			return ret;
		}

		// 支付签名串
		FundInRequest request = PayType.findFundInChannel(payType);
		request.setAmount(new BigDecimal(100).multiply(report.getTotalPrice())
                .longValue());
		request.setOrderNo(report.getOrdersNum());
		request.setGoodsName("橙牛保险订单");
		request.setPurpose("橙牛保险订单");
		request.setIp(remoteIp);

        if (report.getUserType() == BaoxianBaseInfo.USER_TYPE.PROVIDER.value) {
            if ("appstore".equalsIgnoreCase(channel)) {
                request.setKey("201");
            } else {
                request.setKey("200");
            }
        } else {
            request.setKey("100");
        }

		request.setNotifyUrl(payCallbackUrl);

		// 扩展字段
		{
			Map<String, String> extendsion = new HashMap<String, String>();
			extendsion.put("userId", "" + report.getUserId());
			extendsion.put("payType", payType + "");
			// 含有现金支付的支付类型
			if ("#5#6#7#8#9#11#15#14#17#".contains("#" + payType + "#")) {
				extendsion.put("balance", "true");
			}
			request.setExtendsion(extendsion);
		}

		request.setUserType("1");
		FundInResult result = fundRPCFacade.fundIn(request);
		if (!result.isSuccess()) {
			ret.put("code", result.getResultCode());
			ret.put("msg", result.getResultMessage());
			return ret;
		}
		boolean isResume = true;
		BaoxianUnderwritingReportPayinfo payInfo;
		payInfo = baoxianUnderwritingReportPayinfoService
				.queryByReportId(report.getId());
		if (payInfo == null) {
			payInfo = new BaoxianUnderwritingReportPayinfo();
			payInfo.setBaoxianUnderwritingReportId(report.getId());
			payInfo.setCreateTime(new Date());
			payInfo.setOperatorId("系统操作");
			payInfo.setOperatorName("系统操作");
			isResume = false;
		}
		payInfo.setPayWay(PayType.asReadableName(payType));
		payInfo.setPrice(report.getTotalPrice());
		payInfo.setStatus(0);
		if (!isResume) {
			baoxianUnderwritingReportPayinfoService.save(payInfo);
		} else {
			baoxianUnderwritingReportPayinfoService.update(payInfo);
		}

		ret.put("code", "0");
		ret.put("msg", "");
		ret.put("signature", result.getSign());

        // 记录日志
        saveOperationRecord(report.getId(), Partner.USER,
                "尝试使用橙牛" + payInfo.getPayWay() + "进行支付", null);

		return ret;
	}

	@Override
	public void handleOrdersOfficialPayFailed(VerityRequest veritify) {
		BaoxianUnderwritingReport report = baoxianUnderwritingReportService
                .queryByOrderNum(veritify.getOrderNo(),
                        Integer.valueOf(veritify.getUserType()));
		BaoxianUnderwritingReportPayinfo payInfo;
		payInfo = baoxianUnderwritingReportPayinfoService
				.queryByReportId(report.getId());

		baoxianUnderwritingReportPayinfoService.updateResponse(payInfo.getId(),
                veritify.getInstCode(), -1,
                CommonUtil.gson().toJson(veritify));
	}

	@Override
	public boolean handleOrderPayStatusChanged(String reportId, boolean success) {
		BaoxianUnderwritingReport report = baoxianUnderwritingReportService
				.query(reportId);
		if (report.getPayStatus() == null || report.getPayStatus() != 1) {
			if (success) {
				report.setPayStatus(1);
				report.setPolicyStatus("Payed");
			} else {
				if (report.getPayStatus() == -1)
					return true;
				report.setPayStatus(-1);
			}

			boolean ret = updatePayStatus(report);

			// 通知
			if (report.getPayStatus() != null && report.getPayStatus() == 1) {
                // 记录日志
                saveOperationRecord(report.getId(), Partner.SYSTEM, "订单支付成功", null);

                log.debug("发布订单支付状态变化事件 reportId:{}", report.getId());
                eventDispatcher.publish(OrderPayStatusChangedEvent.create(
                        report, null));
			}
			return ret;
		}
		return true;
	}

    @Override
    public boolean handleFanhuaNotification(String underwritingId, JsonObject result) throws Exception {
        BaoxianUnderwritingReport report = baoxianUnderwritingReportService.queryByUnderwritingId(underwritingId);
        if (report == null) {
            return false;
        }

        Boolean resultBoolean = false;

        try {
            String quoteId = result.get("sid") != null ? result.get("sid").getAsString() : "";
            String state = result.get("state").getAsString();
            String msg = null;
            try {
               msg = (result.get("msg") == null || !result.get("msg")
                        .isJsonPrimitive()) ? null : result.get("msg")
                        .getAsString();
            } catch (Exception e) {
            }

            try {
                BaoxianIntent intent = new BaoxianIntent();
                intent.setId(report.getBaoxianIntentId());
                if ("UnderWriteFailed".equalsIgnoreCase(state)
                        || "Interrupted".equalsIgnoreCase(state)) {
                    intent.setStatus(IntentStatus.UnderwritingCanceled.getValue());
                    saveOperationRecord(report.getId(), Partner.FANHUA, "承保失败", msg);

                } else if ("UnderWriteSuccess".equalsIgnoreCase(state)) {
                    intent.setStatus(IntentStatus.UnderwritingGranted.getValue());
                    saveOperationRecord(report.getId(), Partner.FANHUA, "承保成功", msg);
                }

                // 补齐报价
                if ("UnderWriteFailed".equalsIgnoreCase(state)
                        || "Interrupted".equalsIgnoreCase(state)
                        || "PayIng".equalsIgnoreCase(state)
                        || "Cancelled PayIng".equalsIgnoreCase(state)
                        || "UnderWriteSuccess".equalsIgnoreCase(state)
                        || "Finished".equalsIgnoreCase(state)) {
                    resultBoolean = updatePolicyStatus(underwritingId, quoteId, result);

                } else if ("DistributeSuccess".equalsIgnoreCase(state)
                        || "DistributeFailed".equalsIgnoreCase(state)) {
                    resultBoolean = true;
                    try {
                        BaoxianUnderwritingExpress t = new BaoxianUnderwritingExpress();
                        JsonObject expressInfo = result.get("dealOffer")
                                .getAsJsonObject().get("deliveryInfo")
                                .getAsJsonObject();
                        t.setCompany(expressInfo.get("logisticsCompany")
                                .getAsString());
                        t.setBaoxianUnderwritingReportId(report.getId());
                        t.setExpressTime(org.apache.commons.lang3.time.DateUtils.parseDate(expressInfo
                                        .get("sendDateStart").getAsString(),
                                "yyyy-MM-dd HH:mm:ss"));
                        t.setOrderNum(expressInfo.get("expressNumber")
                                .getAsString());
                        this.baoxianInsureInfoService.disposeExpress(
                                report, t);
                    } catch (Exception e) {
                        updateExpressStatus(report.getId(), null);
                    }

                    saveOperationRecord(report.getId(), Partner.FANHUA, "回写保单已配送", msg);
                }
            } catch (Exception e) {
                log.warn("修改数据{}出错", quoteId, e);
            }

        } catch (Exception e) {
            log.warn("查询报价信息{}", underwritingId, e);
            throw e;
        }
        return resultBoolean;
    }


    @Override
	public boolean disposeOrderPayment(String orderNum, Integer userType,
			String payway, boolean success) {
		BaoxianUnderwritingReport report = baoxianUnderwritingReportService
				.queryByOrderNum(orderNum, userType);
		if (report != null
				&& (report.getPayStatus() == null || report.getPayStatus() != 1)) {
			// 不是自动核保的订单
			if (report.isManualUnderwriting()) {
				if (report.getUserType() == 0) {
					OrdersDTO dto = this.ordersRPCService
							.queryByOrderNum(report.getOrdersNum());
					OrderPaymentDTO paymentdot = ordersRPCService
							.queryOrderPaymentByOrderId(dto.getId());
					if (paymentdot == null)
						return false;
					if (dto.getStatus() != null && dto.getStatus() >= 3
							&& dto.getStatus() <= 10) {
						// 已经支付
						report.setPayStatus(1);
						report.setUserType(userType);
						BaoxianUnderwritingReportPayinfo info = new BaoxianUnderwritingReportPayinfo();
						info.setBaoxianUnderwritingReportId(report.getId());
						info.setCreateTime(new Date());
						info.setTradeNum(paymentdot.getPayNum());
						info.setPayWay(PayType.asReadableName(paymentdot
								.getPayType()));
						info.setPrice(CommonUtil.add(
								new BigDecimal(paymentdot.getMoney()),
								new BigDecimal(paymentdot.getCash())));
						info.setOperatorId("系统操作");
						info.setOperatorName("系统操作");
						info.setStatus(report.getPayStatus());
						info.setResponseTime(new Date());
						this.baoxianUnderwritingReportPayinfoService.save(info);
						this.baoxianUnderwritingReportPayinfoService
								.updateStatus(info.getId(),
										report.getPayStatus());
						boolean ret = updatePayStatus(report);
						if (ret) {
                            // 记录日志
                            saveOperationRecord(report.getId(), Partner.SYSTEM, "订单支付成功", null);

                            log.debug("发布支付状态变化事件 reportId:{}", report.getId());
                            eventDispatcher.publish(OrderPayStatusChangedEvent.create(report, null));

						}
						return ret;
					}
				} else if (report.getUserType() == 1) {
					FundInRequest request = PayType.findFundInChannel(Integer
							.valueOf(payway));
					FundInQueryRequest req = new FundInQueryRequest();
					req.setInstCode(request.getInstCode());
					req.setChannelCode(request.getChannelCode());
					req.setInstOrderNo(report.getOrdersNum());
					req.setAmount(new BigDecimal(100).multiply(
							report.getTotalPrice()).longValue());
					req.setCreateTime(report.getCreateTime());
					FundInQueryResult result = null;
					try {
						result = fundRPCFacade.fundInQuery(req);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (result != null
							&& (result.isSuccess() || "00000".equals(result
									.getResultCode()))) {
						report.setPayStatus(1);
						BaoxianUnderwritingReportPayinfo info = new BaoxianUnderwritingReportPayinfo();
						info.setBaoxianUnderwritingReportId(report.getId());
						info.setCreateTime(new Date());
						info.setTradeNum(req.getInstOrderNo());
						info.setPayWay(PayType.asReadableName(Integer
								.valueOf(payway)));
						info.setPrice(new BigDecimal(result.getAmount())
								.divide(new BigDecimal(100)));
						info.setOperatorId("系统操作");
						info.setOperatorName("系统操作");
						info.setStatus(report.getPayStatus());
						info.setResponseTime(new Date());
						this.baoxianUnderwritingReportPayinfoService.save(info);
						this.baoxianUnderwritingReportPayinfoService
								.updateStatus(info.getId(),
										report.getPayStatus());
						boolean ret = updatePayStatus(report);
						if (ret) {
                            // 记录日志
                            saveOperationRecord(report.getId(), Partner.SYSTEM, "订单支付成功", null);

                            log.debug("发布支付状态变化事件 reportId:{}", report.getId());
                            eventDispatcher.publish(OrderPayStatusChangedEvent
                                    .create(report, null));
						}
						return ret;
					}
				}
			} else {
				BaoxianUnderwritingReportPayinfo info = new BaoxianUnderwritingReportPayinfo();
				info.setBaoxianUnderwritingReportId(report.getId());
				info.setCreateTime(new Date());
				info.setTradeNum(report.getOrdersNum());
				info.setPayWay(payway);
				info.setPrice(report.getTotalPrice());
				info.setOperatorId("系统操作");
				info.setOperatorName("系统操作");
				this.baoxianUnderwritingReportPayinfoService.save(info);
				if (success)
					report.setPayStatus(1);
				else
					report.setPayStatus(0);
				report.setUserType(userType);
				boolean ret = updatePayStatus(report);
				if (ret && success) {
                    // 记录日志
                    saveOperationRecord(report.getId(), Partner.SYSTEM, "订单支付成功", null);

					// 发布支付成功事件
                    log.debug("发布支付状态变化事件 reportId:{}", report.getId());
                    eventDispatcher.publish(OrderPayStatusChangedEvent.create(
                            report, null));
				}
				return ret;
			}
		}
		return false;
	}

    @Override
    public Map<String, Object> disposeCancel(BaoxianUnderwritingReport report) {
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put("result", false);

        if (!StringUtils.isEmpty(report.getQuoteId())) {
            Map<String, Object> ret = fanhuaApi.disposeCancel(report.getBaoxianUnderwritingId(), report.getQuoteId());
            boolean succ = ret.get("result") != null
                    && "true".equals(ret.get("result").toString());
            resmap = ret;
            if (succ) {
                report.setStatus(-1);
                baoxianUnderwritingReportService.save(report);

                saveOperationRecord(report.getBaoxianUnderwritingId(), Partner.USER, "取消订单", null);
            }
        }
        return resmap;
    }

    @Override
    public boolean checkValidity(BaoxianUnderwritingReport report) {
        if ((report.getSyxStartDate() != null && report.getSyxStartDate()
                .getTime() <= System.currentTimeMillis())
                || (report.getExpireTime() != null && report
                .getExpireTime().getTime() <= System
                .currentTimeMillis())
                || (report.getJqxStartDate() != null && report
                .getJqxStartDate().getTime() <= System
                .currentTimeMillis())) {
            updateStatus(report.getId(), -1);
            return false;
        }

        return true;
    }

    private void saveOperationRecord(String reportId, String operatorName, String msg, String comment) {
        if (StringUtils.isEmpty(msg)) {
            log.debug("忽略操作日志：{} 内容：{} 备注：{}", reportId, msg, comment);
            return;
        }

        try {
            BaoxianUnderwritingReportOperationRecord log = new BaoxianUnderwritingReportOperationRecord();
            log.setReportId(reportId);
            if (!StringUtils.isEmpty(comment)) {
                msg = msg + "[" + comment + "]";
            }
            log.setRemark(msg);
            log.setAdminId(null);
            log.setAdminName(operatorName);
            log.setCreateTime(new Date());
            baoxianUnderwritingReportOperationRecordDAO.insert(log);
        } catch (Exception e) {

        }
    }

	private final class EventHandler {

		@Subscribe
		public void didOrderPaid(OrderPayStatusChangedEvent e) {
			final BaoxianUnderwritingReport report = e.getReport();
			if (report.getPayStatus() != null && report.getPayStatus() == 1) {
				// 同步订单状态到基础订单库
				if (report.getPayStatus() == 1 && report.getUserType() == 0) {
					try {
						OrdersDTO dto = ordersRPCService.queryByOrderNum(report
								.getOrdersNum());
						if (dto.getStatus() != null && dto.getStatus() >= 3
								&& dto.getStatus() <= 10) {
							ordersRPCService.updateOrdersStatusByOrderNum(
									dto.getUid(), dto.getOrderNum(), 12,
									"保险订单支付完成,请勿退款");
						}
					} catch (Exception s) {
					}
				} else if (report.getPayStatus() == 1
						&& report.getUserType() == 1) {
					ordersBaseInfoRPCService.changeOrderStatus(
							report.getOrdersNum(), report.getId(), "3", "19",
							"29");
				}
			}
		}
	}
}