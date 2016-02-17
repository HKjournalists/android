package com.chengniu.client.service.impl;

import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.HttpClientUtil;
import com.chengniu.client.common.SerializableUtils;
import com.chengniu.client.dao.BaoxianQuoteInfoDAO;
import com.chengniu.client.domain.MediaStatus;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
@Service("fanhuaService")
public class FanhuaServiceImpl implements FanhuaService {
	protected static final Logger log = LogManager
			.getLogger(FanhuaServiceImpl.class);
    @Autowired
    private BaoxianQuoteInfoDAO baoxianQuoteInfoDAO;

	@Autowired
	private BaoxianBaseInfoService baoxianBaseInfoService;
    @Autowired
    private BaoxianInformalQuoteService baoxianInformalQuoteService;
    @Autowired
    private BaoxianUnderwritingService baoxianUnderwritingService;
	@Autowired
	private BaoxianUnderwritingReportService baoxianUnderwritingReportService;
	@Autowired
	private BaoxianUnderwritingReportPayinfoService baoxianUnderwritingReportPayinfoService;
	@Autowired
	private BaoxianInsureInfoService baoxianInsureInfoService;
	@Autowired
	private BaoxianOrderService baoxianOrderService;

    @Autowired
    private BaoxianApi fanhuaApi;

	@Value("${fanhua.vehicleModel.url}")
	private String vecurl;

    @Override
    public String fetchPagniateVehicles(String keyword, String pageNum) {
        try {
            try {
                pageNum = String.valueOf(Integer.parseInt(pageNum));
            } catch (Exception e) {
                pageNum = "1";
            }
            String message = HttpClientUtil.doGet(vecurl + "/searchList?p="
                    + pageNum + "&key=" + keyword);
            message = message.replace("carModelList.count", "total");
            message = message.replace("carModelList", "list");
            message = message.replace("modelId", "vehicleCode");
            return message;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public boolean updatePeisongInQuoteStage(BaoxianUnderwriting underwriting, BaoxianInsureInfo peisong) {
        if (peisong != null) {
            peisong.setPeisongAddress(peisong.getPeisongAddress().replaceFirst(
                    peisong.getPeisongProvince(), ""));
            peisong.setPeisongAddress(peisong.getPeisongAddress().replaceFirst(
                    peisong.getPeisongCity(), ""));
            if (peisong.getPeisongTown() != null) {
                peisong.setPeisongAddress(peisong.getPeisongAddress()
                        .replaceFirst(peisong.getPeisongTown(), ""));
            }
        }
        return fanhuaApi.updatePeisongInQuote(underwriting, peisong);
    }

    public Boolean updatePeisong(String baoxianUnderwritingId) throws Exception {
		BaoxianInsureInfo peisong = this.baoxianInsureInfoService
				.queryByUnderwritingId(baoxianUnderwritingId);
		if (peisong != null) {
			peisong.setPeisongAddress(peisong.getPeisongAddress().replaceFirst(
					peisong.getPeisongProvince(), ""));
			peisong.setPeisongAddress(peisong.getPeisongAddress().replaceFirst(
					peisong.getPeisongCity(), ""));
			if (peisong.getPeisongTown() != null) {
                peisong.setPeisongAddress(peisong.getPeisongAddress()
                        .replaceFirst(peisong.getPeisongTown(), ""));
            }
		}

        BaoxianUnderwriting underwriting = baoxianUnderwritingService.query(baoxianUnderwritingId);
        BaoxianInformalReport report = baoxianInformalQuoteService.queryInformalReport(underwriting.getBaoxianInformalReportId());

        return fanhuaApi.updatePeisongInUnderwrite(baoxianUnderwritingId, report.getMainQuoteId(),
                report.getQuoteId(), peisong);
	}

	public Map<String, Object> disposePay(String id, String type,
			String bankNo, String bankName, String bankCode) throws Exception {
		Map<String, Object> resultMapInfo = new HashMap<String, Object>();
		resultMapInfo.put("result", false);
		try {
			BaoxianUnderwritingReport report = this.baoxianUnderwritingReportService
					.query(id);
			if (report == null
					|| (report.getPayStatus() != null && report.getPayStatus() == 1)) {
				return resultMapInfo;
			}
			if (report == null
					|| (report.getStatus() == null || report.getStatus() != 1)) {
				resultMapInfo.put("result", false);
				return resultMapInfo;
			}

            // 检查报价是否过期
            if (!this.baoxianOrderService.checkValidity(report)) {
                resultMapInfo.put("result", false);
                return resultMapInfo;
            }

            // 检查支付冷却时间，避免重复支付
			BaoxianQuoteInfoWithBLOBs lastPayInfo = this.baoxianQuoteInfoDAO
					.queryLastRequestInfo(report.getBaoxianUnderwritingId(), 8);
			if (lastPayInfo != null
					&& lastPayInfo.getRequestTime() != null
					&& (System.currentTimeMillis() - lastPayInfo
							.getRequestTime().getTime()) < 1000 * 40) {
				resultMapInfo.put("result", true);
				return resultMapInfo;
			}

			this.baoxianOrderService.createOrder(report.getId(),
                    report.getUserId(), report.getUserType());

			BaoxianUnderwritingReportPayinfo pay = baoxianUnderwritingReportPayinfoService
					.queryByReportId(report.getId());
            String paytype = "易联语音";
            if ("bestpay".equalsIgnoreCase(type)) {
                paytype = "翼支付";
            } else if ("99bill".equalsIgnoreCase(type)) {
                paytype = "快钱支付";
            } else if ("alipay".equalsIgnoreCase(type)
                    || "payali".equalsIgnoreCase(type)) {
                paytype = "支付宝支付";
            } else if ("wxpay.json".equalsIgnoreCase(type)) {
                paytype = "微信支付";
            }
			if (pay == null || pay.getStatus() == null
					|| (pay.getStatus() == 0 || pay.getStatus() == -1)) {
				this.baoxianOrderService.disposeOrderPayment(
						report.getOrdersNum(), report.getUserType(), paytype,
						false);
				pay = baoxianUnderwritingReportPayinfoService
						.queryByReportId(report.getId());
			} else if (pay != null && pay.getStatus() != null
					&& pay.getStatus() == 1) {
				resultMapInfo.put("result", true);
				return resultMapInfo;
			}

            Map<String, String> bank = fanhuaApi.queryBankInfo(bankNo, bankName, bankCode);
            String bankArea = null;
            try {
                BaoxianCityFanhua city = new BaoxianCityFanhua();
                city.setCityName(bank.get("bankProvince").toString());
                city.setKind(0);
                bankArea = this.baoxianBaseInfoService.queryCity(city).get(0).getCityCode();
            } catch (Exception e1) {
            }

            Map<String, Object> ret = fanhuaApi.initiatePay(report, type, bankArea, bank, pay);
            boolean succ = (Boolean)ret.get("result");
            if (succ) {
                String orderState = (String) ret.get("orderState");
                if ("10".equals(orderState) || "02".equals(orderState)) {
                    this.baoxianOrderService.handleOrderPayStatusChanged(id,
                            true);
                    pay.setStatus(1);
                }

                String requestBody = (String)ret.get("request");
                this.baoxianUnderwritingReportPayinfoService.updateRequest(
                        pay.getId(), requestBody);
                this.baoxianUnderwritingReportPayinfoService.updateTradeNum(
                        pay.getId(), pay.getTradeNum());

                if ("bestpay".equalsIgnoreCase(type)) {
                    String tradeNo = (String) ret.get("tradeNo");

                    int status;
                    if ("10".equals(orderState) || "02".equals(orderState)) {
                        status = 1;
                    } else if ("01".equals(orderState) || "00".equals(orderState)
                            || "06".equals(orderState)) {
                        status = 0;
                    } else {
                        status = -1;
                    }

                    String responseBody = (String)ret.get("response");
                    this.baoxianUnderwritingReportPayinfoService
                            .updateResponse(pay.getId(), tradeNo, status, responseBody);
                }
                resultMapInfo.put("payUrl", ret.get("payUrl"));
                resultMapInfo.put("result", true);
            }

		} catch (Exception e) {
			log.warn("提交支付请求{}出错", id, e);
		}
		return resultMapInfo;
	}

	@Override
	public boolean disposeManualNotifyPay(String id) throws Exception {
		BaoxianUnderwritingReportPayinfo payinfo = this.baoxianUnderwritingReportPayinfoService
				.queryByReportId(id);
		try {
            String message = fanhuaApi.syncPayInfo(payinfo);
			return this.handlePaymentResult(id, message);
		} catch (Exception e) {
			log.warn("提交支付请求{}出错", id, e);
		}
		return false;
	}

    @Override
    public boolean handlePaymentResult(String id, String response) {
        Map<String, String> resultMap = CommonUtil.gson().fromJson(response,
                new TypeToken<Map<String, String>>() {
                    private static final long serialVersionUID = 1L;
                }.getType());

        if ("0000".equals(resultMap.get("code").trim())) {
            String tradeNum = resultMap.get("paySerialNo");
            if(!StringUtils.hasText(tradeNum)) {
                tradeNum = resultMap.get("bizTransactionId");
            }

            int status;
            String orderState = resultMap.get("orderState");
            if ("10".equals(orderState) || "02".equals(orderState)) {
                BaoxianUnderwritingReportPayinfo pay = this.baoxianUnderwritingReportPayinfoService.query(id);
                baoxianOrderService.handleOrderPayStatusChanged(
                        pay.getBaoxianUnderwritingReportId(), true);
                status = 1;
            } else if ("01".equals(orderState) || "00".equals(orderState)
                    || "06".equals(orderState)) {
                status = 0;
            } else {
                BaoxianUnderwritingReportPayinfo pay = this.baoxianUnderwritingReportPayinfoService.query(id);
                baoxianOrderService.handleOrderPayStatusChanged(
                        pay.getBaoxianUnderwritingReportId(), false);
                status = -1;
            }

            return this.baoxianUnderwritingReportPayinfoService.updateResponse(id, tradeNum, status, response);
        }
        return false;
    }

    @Override
    public boolean handleNotification(String underwritingId, String body) throws Exception {
        BaoxianQuoteInfoWithBLOBs blobs = this.baoxianQuoteInfoDAO
                .queryLastRequestInfo(underwritingId, 1);
        if (blobs == null) {
            return true;
        }

        Boolean resultBoolean = false;
        BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
        try {
            info.setId(SerializableUtils.allocUUID());
            info.setStatus(false);
            info.setBaoxianUnderwritingId(underwritingId);
            info.setStep(3);
            info.setRequestTime(new Date());
            info.setStatus(false);

            JsonObject result = new JsonParser().parse(body).getAsJsonObject();
            String mid = result.get("mid") != null ? result.get("mid")
                    .getAsString() : "";
            info.setQuoteId(mid);
            String state = result.get("state").getAsString();
            info.setResponseStatus(state);

            log.info("通知报价{}泛华返回{}结果", underwritingId, body);

            if (!"Created".equalsIgnoreCase(state)) {
                // 如果quoteId不是最后一次的id，直接忽略
                BaoxianQuoteInfoWithBLOBs oldInfo = this.baoxianQuoteInfoDAO.queryByUnderwritingIdAndQuoteId(underwritingId,
                        mid);
                if (oldInfo != null) {
                    BaoxianQuoteInfoWithBLOBs lastQuoteInfo = this.baoxianQuoteInfoDAO
                            .queryLastRequestInfo(underwritingId, oldInfo.getStep());
                    if (lastQuoteInfo != null && oldInfo.getId().equals(lastQuoteInfo.getId())) {
                        // 是否报价消息
                        boolean succ = baoxianInformalQuoteService.handleFanhuaNotification(underwritingId, result);

                        // 核保消息
                        if (!succ) {
                            succ = baoxianUnderwritingService.handleFanhuaNotification(underwritingId, result);
                        }

                        // 支付消息
                        if (!succ) {
                            succ = baoxianOrderService.handleFanhuaNotification(underwritingId, result);
                        }
                        info.setStatus(succ);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            info.setStatus(false);
        }

        try {
            info.setResponse(body);
            info.setResponseTime(new Date());
            info.setQuoteId(blobs.getQuoteId());
            this.baoxianQuoteInfoDAO.insert(info);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("修改数据{}出错", underwritingId, e);
        }
        return resultBoolean;
    }

	@Override
	public Boolean disposeUnderwritingUploadMedia(String underwritingId) {
		BaoxianQuoteInfoWithBLOBs oldInfo = this.baoxianQuoteInfoDAO
				.queryLastRequestInfo(underwritingId, 0);
		if (oldInfo == null)
			return false;
		BaoxianUnderwriting underwriting = this.baoxianUnderwritingService
				.query(underwritingId);
		if (underwriting == null
				|| !MediaStatus.isReady(underwriting.getMediaStatus())) {
			return false;
		}
		String message = null;
		List<BaoxianBaseInfoMedia> list = baoxianBaseInfoService.queryMediaInfo(
                underwriting.getBaoxianBaseInfoId());
		if (list == null || list.isEmpty()) {
			return false;
		}

		try {
			this.baoxianUnderwritingService.updateMediaStatus(underwritingId,
					MediaStatus.UPLOADING.ordinal());
		} catch (Exception e1) {
		}

		boolean succ = disposeUploadMedia(underwritingId, null, list);
		log.info("提交影像数据{}泛华返回{}结果", oldInfo.getQuoteId(), message);
		if (succ) {
			try {
				this.baoxianUnderwritingService.updateMediaStatus(
						underwritingId, MediaStatus.UPLOADED.ordinal());
			} catch (Exception e1) {
			}
		}
		return false;
	}

	private boolean disposeUploadMedia(String id, String quoteId,
			List<BaoxianBaseInfoMedia> list) {
		if (list == null || list.isEmpty()) {
			return false;
		}

		if (StringUtils.isEmpty(quoteId)) {
			BaoxianQuoteInfoWithBLOBs oldInfo = this.baoxianQuoteInfoDAO
					.queryLastRequestInfo(id, 0);
			if (oldInfo == null) {
				return false;
			}

			quoteId = oldInfo.getQuoteId();
		}

		return fanhuaApi.disposeMedia(id, quoteId, list);
	}

}