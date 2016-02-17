package com.chengniu.client.service.impl;

import com.chengniu.client.common.*;
import com.chengniu.client.dao.BaoxianQuoteInfoDAO;
import com.chengniu.client.pojo.*;
import com.chengniu.client.service.BaoxianApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 泛华保险服务接口
 */
@Service("fanhuaApi")
public class FanhuaApiImpl implements BaoxianApi {

	protected static final Logger log = LogManager
			.getLogger(FanhuaApiImpl.class);

    @Value("${bank.info.url}")
    private String bankInfoUrl;
	@Value("${fanhua.restful.url}")
	private String restful;
    @Value("${fanhua.pmservice.url}")
    private String pmserviceurl;
    @Value("${fanhua.channel.id}")
    private String channelId;

    @Value("${fanhua.pay.notify.url}")
    private String payNotifyUrl;
    @Value("${fanhua.report.notify.url}")
    private String reportNotifyUrl;

	@Resource
	private BaoxianQuoteInfoDAO baoxianQuoteInfoDAO;

	@Override
	public Map<String, Object> disposeQuoteRequest(
			BaoxianUnderwritingRequest request, BaoxianBaseInfo baseInfo,
			List<String> companys, String remark) {
		Map<String, Object> resmap = new HashMap<String, Object>();

		BaoxianUnderwriting w = CommonUtil.simpleValueCopy(request,
                BaoxianUnderwriting.class);

		Gson gson = CommonUtil.gson();
		Map<String, Object> map = gson.fromJson(gson.toJson(w),
				new TypeToken<Map<String, Object>>() {
					private static final long serialVersionUID = 1L;
				}.getType());

		Map<String, Object> mapinfo = gson.fromJson(gson.toJson(baseInfo),
				new TypeToken<Map<String, Object>>() {
					private static final long serialVersionUID = 1L;
				}.getType());
		map.putAll(mapinfo);

        // 去掉整备质量的单位，默认为KG
        String fullLoad = (String)map.get("fullLoad");
        if (!StringUtils.isEmpty(fullLoad)) {
            fullLoad = fullLoad.replaceAll("[kKgG]", "");
            map.put("fullLoad", fullLoad);
        }

        // 新车未上牌改为新车
        if ("新车未上牌".equals(map.get("vehicleNum"))) {
            map.put("vehicleNum", "新车");
        }

		if (w.getJqxStartDate() == null) {
			w.setJqxStartDate(new Date());
		}
		if (w.getSyxStartDate() == null) {
			w.setSyxStartDate(new Date());
		}
		map.put("remark",
				MessageFormat.format("{0}: {1}", request.getId(), remark));
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(new Date());
		nowCalendar.add(Calendar.DATE, 1);
		map.put("providerInfos", companys);
		Calendar jqxCalendar = Calendar.getInstance();
		if (baseInfo.getGuohu() != null && baseInfo.getGuohu()
				&& baseInfo.getGuohuDate() == null) {
			try {
				baseInfo.setGuohuDate(new SimpleDateFormat("yyyy-MM-dd")
						.format(baseInfo.getRegisterDate()));
			} catch (Exception e) {
			}
		}
		Boolean resultBoolean = false;
		map.put("guohu", baseInfo.getGuohu());
		try {
			baseInfo.setGuohuDate(new SimpleDateFormat("yyyy-MM-dd")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(baseInfo
							.getGuohuDate())));
		} catch (Exception e) {
		}
		map.put("guohudate", baseInfo.getGuohuDate());
		jqxCalendar.setTime(w.getJqxStartDate());
		Calendar syxCalendar = Calendar.getInstance();
		syxCalendar.setTime(w.getSyxStartDate());
		if (nowCalendar.getTime().getTime() > jqxCalendar.getTime().getTime()) {
			w.setJqxStartDate(nowCalendar.getTime());
		}
		if (nowCalendar.getTime().getTime() > syxCalendar.getTime().getTime()) {
			w.setSyxStartDate(nowCalendar.getTime());
		}
		map.put("DriverIns", FanhuaConstant.DriverIns);
		try {
			BigDecimal vehicleModelPrice = new BigDecimal(
					baseInfo.getVehicleModelPrice());
			if (StringUtils.hasText(baseInfo.getVehicleModelPrice())
					&& vehicleModelPrice.intValue() >= 10000) {
				map.put("vehicleModelPrice", vehicleModelPrice);
			} else
				map.put("vehicleModelPrice",
						vehicleModelPrice.multiply(new BigDecimal(10000)));
		} catch (Exception e3) {
		}
		map.put("PassengerIns", FanhuaConstant.PassengerIns);
		map.put("efcExpireDate", DateUtils.addDays(
				DateUtils.addYears(w.getSyxStartDate(), 1), -1));
		map.put("ScratchIns", FanhuaConstant.ScratchIns);
		map.put("bizExpireDate", DateUtils.addDays(
				DateUtils.addYears(w.getJqxStartDate(), 1), -1));
		map.put("ThirdPartyIns", FanhuaConstant.ThirdPartyIns);
		try {
			map.put("registerDate", new SimpleDateFormat("yyyy-MM-dd")
					.format(baseInfo.getRegisterDate()));
		} catch (Exception e2) {
		}
		map.put("notifyUrl", reportNotifyUrl + request.getId());

		Object msg = null;
		BaoxianQuoteInfoWithBLOBs quo = new BaoxianQuoteInfoWithBLOBs();
		try {
			map.put("jqxStartDate", w.getJqxStartDate());
			map.put("syxStartDate", w.getSyxStartDate());

			// 保存请求记录
			quo.setId(SerializableUtils.allocUUID());
			quo.setRequest(StringUtils.trimAllWhitespace(FreemarkerUtils
					.process("/request.json", map, "/fanhua")));
			quo.setStatus(false);
			quo.setBaoxianUnderwritingId(request.getId());
			quo.setStep(0);
			quo.setRequestTime(new Date());
			quo.setRequestUrl(restful + "/TFService/restful/quote/request");
			this.baoxianQuoteInfoDAO.insert(quo);

			// 发起请求
			String message = null;
			try {
				message = HttpClientUtil.doPost(quo.getRequestUrl(),
						quo.getRequest());
				log.info("提交信息数据{}泛华返回{}结果", request.getId(), message);
			} catch (Exception e) {
				log.warn("提交{}数据到泛华", request.getId(), e);
			}

			// 处理返回结果
			Map<String, Object> resultMap = gson.fromJson(message,
					new TypeToken<Map<String, Object>>() {
						private static final long serialVersionUID = 8798729849366641784L;
					}.getType());

			msg = resultMap.get("msg");
			if ("-1".equals(resultMap.get("code"))) {
				resultBoolean = false;
			} else {
				String quoteId = resultMap.get("mid").toString();

				resmap.put("mid", quoteId);

				quo.setResponse(message);
				quo.setResponseTime(new Date());
				quo.setQuoteId(quoteId);
				quo.setStatus(true);

				List<Map<String, String>> reportList = Lists.newArrayList();

				JsonArray array = new JsonParser().parse(
						resultMap.get("sids").toString()).getAsJsonArray();
				Iterator<JsonElement> it = array.iterator();
				while (it.hasNext()) {
					JsonElement element = it.next();
					JsonObject object = element.getAsJsonObject();

					String sid = object.get("sid").getAsString();
					String insComId = object.get("insComId").getAsString();
					String companyName = object.get("companyName")
							.getAsString();

					Map<String, String> reportInfo = Maps.newHashMap();
					reportInfo.put("sid", sid);
					reportInfo.put("insComId", insComId);
					reportInfo.put("companyName", companyName);

					reportList.add(reportInfo);
				}
				resmap.put("reportList", reportList);
				resultBoolean = true;
			}
		} catch (Exception e) {
			log.warn("提交{}数据到泛华", request.getId(), e);
			resultBoolean = false;
			quo.setStatus(false);
		}

		try {
			this.baoxianQuoteInfoDAO.updateReponse(quo);
		} catch (Exception e) {
			log.warn("修改数据{}出错", request.getId(), e);
		}

		resmap.put("result", resultBoolean);

		if (resmap.get("msg") == null) {
			resmap.put("msg", msg);
		}

		return resmap;
	}

	public Map<String, Object> submitQuoteRequest(String requestId, String mid) {
		BaoxianQuoteInfoWithBLOBs quo = new BaoxianQuoteInfoWithBLOBs();
		Gson gson = CommonUtil.gson();
		Object msg = null;
		Boolean resultBoolean = false;
		try {
			quo.setId(SerializableUtils.allocUUID());
			quo.setStatus(false);
			quo.setBaoxianUnderwritingId(requestId);
			quo.setStep(1);
            quo.setQuoteId(mid);
			quo.setRequestTime(new Date());
			quo.setRequestUrl(new StringBuilder().append(restful)
					.append("/TFService/restful/quote/").append(mid)
					.append("/submitQuote").toString());
			this.baoxianQuoteInfoDAO.insert(quo);

			String message = null;
			try {
				Thread.sleep(250 * 20);// 不延时泛华会可能报错
				message = HttpClientUtil.doPost(quo.getRequestUrl(), null);
			} catch (Exception e) {
				log.warn("提交{}数据到泛华", mid, e);
			}
			log.info("提交报价信息数据{}泛华返回{}结果", mid, message);

			Map<String, Object> resultMap = gson.fromJson(message,
					new TypeToken<Map<String, Object>>() {
					}.getType());

			// 报错了,再试一次,可能泛华数据还没有同步
			if ("-1".equals(resultMap.get("code"))) {
				msg = resultMap.get("msg");
				try {
					if (msg != null && msg.toString().indexOf("未找到") > 0) {
						Thread.sleep(250 * 20);// 不延时泛华会可能报错
						message = HttpClientUtil.doPost(quo.getRequestUrl(),
								null);
						log.info("提交报价信息数据{}泛华返回{}结果", mid, message);
						resultMap = gson.fromJson(message,
								new TypeToken<Map<String, Object>>() {
								}.getType());
					}
				} catch (Exception e) {
				}
			}
			if ("-1".equals(resultMap.get("code"))) {
				msg = resultMap.get("msg");
				resultBoolean = false;
			} else {
				quo.setResponse(message);
				quo.setResponseTime(new Date());
				resultBoolean = true;
				quo.setQuoteId(mid);
				quo.setStatus(true);
				resultBoolean = true;
			}
		} catch (Exception e) {
			log.warn("提交{}数据到泛华", mid, e);
			quo.setStatus(false);
		}
		try {
			this.baoxianQuoteInfoDAO.updateReponse(quo);
		} catch (Exception e) {
			log.warn("修改数据{}出错", mid, e);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", resultBoolean);
		map.put("msg", msg);

		return map;
	}

	@Override
	public boolean disposeMedia(String requestId, String quoteId,
			List<BaoxianBaseInfoMedia> list) {
		if (list == null || list.isEmpty()) {
			return false;
		}

		if (StringUtils.isEmpty(quoteId)) {
			BaoxianQuoteInfoWithBLOBs oldInfo = this.baoxianQuoteInfoDAO
					.queryLastRequestInfo(requestId, 0);
			if (oldInfo == null) {
				return false;
			}
			quoteId = oldInfo.getQuoteId();
		}

		BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();

		String message = null;
		List<Map<String, String>> mediaList = new ArrayList<Map<String, String>>();
		for (BaoxianBaseInfoMedia m : list) {
			try {
				if (!StringUtils.isEmpty(m.getUrl())) {
					Map<String, String> mediaMap = new HashMap<String, String>();
					mediaMap.put("type", m.getCode());
					mediaMap.put("url", m.getUrl());
					mediaList.add(mediaMap);
				}
			} catch (Exception e) {
			}
		}
		if (mediaList.isEmpty()) {
			return false;
		}

		String content = "{\"imageInfos\":"
				+ CommonUtil.gson().toJson(mediaList) + "}";
		try {
			info.setId(SerializableUtils.allocUUID());
			info.setStatus(false);
			info.setBaoxianUnderwritingId(requestId);
			info.setStep(2);
            info.setQuoteId(quoteId);
			// info.setRequest(content);
			info.setRequestTime(new Date());
			info.setRequestUrl(restful + "/TFService/restful/image/" + quoteId
					+ "/upload");
			this.baoxianQuoteInfoDAO.insert(info);

			message = HttpClientUtil.doPost(info.getRequestUrl(), content);
			log.info("提交影像数据{}泛华返回{}结果", quoteId, message);
			info.setStatus(true);
		} catch (Exception e) {
			message = e.getMessage();
			info.setStatus(false);
			log.warn("提交影像数据{}出错", quoteId, e);
		}
		try {
			info.setResponse(message);
			info.setResponseTime(new Date());
			info.setQuoteId(quoteId);
			this.baoxianQuoteInfoDAO.updateReponse(info);
		} catch (Exception e) {
			log.warn("修改数据{}出错", quoteId, e);
		}
		try {
			Map<String, String> resultMap = CommonUtil.gson().fromJson(message,
					new TypeToken<Map<String, String>>() {
					}.getType());
			if ("0".equals(resultMap.get("code"))) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public String disposeQuoteQuery(BaoxianInformalReport t) {
		String message = null;
		try {
			String sid = t.getQuoteId();

			String requestUrl = new StringBuilder().append(restful)
					.append("/TFService/restful/quote/").append(sid)
					.append("/query").toString();
			try {
				message = HttpClientUtil.doGet(requestUrl);
				log.info("查询报价信息数据{}泛华返回{}结果", sid, message);
			} catch (Exception e) {
				log.warn("查询{}数据", sid, e);
			}
		} catch (Exception e) {
			log.warn("查询{}数据泛华", t.getId(), e);
		}
		return message;
	}

    @Override
    public Map<String, Object> disposeCancel(String id, String sid) {
        Map<String, Object> resmap = new HashMap<String, Object>();
        Boolean resultBoolean = false;
        BaoxianQuoteInfoWithBLOBs quo = new BaoxianQuoteInfoWithBLOBs();
        try {
            BaoxianQuoteInfoWithBLOBs oldquo = this.baoxianQuoteInfoDAO
                    .queryLastRequestInfo(id, 0);
            resmap.put("result", resultBoolean);
            if (oldquo == null) {
                resmap.put("reason", "unsubmit");
                return resmap;
            }

            quo.setId(SerializableUtils.allocUUID());
            quo.setBaoxianUnderwritingId(oldquo.getBaoxianUnderwritingId());
            quo.setStatus(false);
            quo.setStep(9);
            quo.setRequestTime(new Date());
            quo.setRequestUrl(new StringBuilder().append(restful)
                    .append("/TFService/restful/quote/").append(StringUtils.hasText(sid) ? sid : oldquo.getQuoteId())
                    .append("/cancel").toString());
            this.baoxianQuoteInfoDAO.insert(quo);
            String message = null;
            try {
                message = HttpClientUtil.doPost(quo.getRequestUrl(), null);
            } catch (Exception e) {
                log.warn("提交{}数据到泛华", sid, e);
            }
            log.info("提交报价信息数据{}泛华返回{}结果", sid, message);
            Gson gson = CommonUtil.gson();
            Map<String, Object> resultMap = gson.fromJson(message,
                    new TypeToken<Map<String, Object>>() {
                        private static final long serialVersionUID = 7078381204623679737L;
                    }.getType());
            Object state = resultMap.get("state");
            resmap.put("msg", resultMap.get("msg"));
            if ("0".equals(resultMap.get("code"))) {
                quo.setResponse(message);
                quo.setResponseTime(new Date());
                quo.setQuoteId(sid);
                resmap.put("state", resultMap.get("state"));
                resmap.put("sid", resultMap.get("sid"));
                resmap.put("mid", resultMap.get("mid"));
                resmap.put("code", resultMap.get("code"));
                quo.setStatus(true);
                if (state != null
                        && "Cancelled".equalsIgnoreCase(state.toString()))
                    resultBoolean = true;
            }
        } catch (Exception e) {
            log.warn("提交{}数据到泛华", sid, e);
            quo.setStatus(false);
        }
        try {
            this.baoxianQuoteInfoDAO.updateReponse(quo);
        } catch (Exception e) {
            log.warn("修改数据{}出错", sid, e);
        }
        resmap.put("result", resultBoolean);
        return resmap;
    }

	@Override
	public Map<String, Object> disposeUnderwriting(BaoxianUnderwriting underwriting,
			BaoxianInformalReport report) {
		String message = null;
        String errorMesage = null;
		Boolean resultBoolean = false;
		BaoxianQuoteInfoWithBLOBs olod = this.baoxianQuoteInfoDAO
				.queryLastRequestInfo(report.getBaoxianUnderwritingRequestId(),
						0);
		BaoxianQuoteInfoWithBLOBs quo = new BaoxianQuoteInfoWithBLOBs();
		try {
			quo.setId(SerializableUtils.allocUUID());
			quo.setRequest(report.getQuoteId());
			quo.setStatus(false);
			quo.setBaoxianUnderwritingId(report
					.getBaoxianUnderwritingRequestId());
			quo.setStep(2);
			quo.setRequestTime(new Date());
			quo.setRequestUrl(new StringBuilder().append(restful)
					.append("/TFService/restful/quote/")
					.append(olod.getQuoteId()).append("/submitInsure/")
					.append(report.getQuoteId()).toString());
			this.baoxianQuoteInfoDAO.insert(quo);
			try {
				message = HttpClientUtil.doPost(quo.getRequestUrl(), null);
			} catch (Exception e) {
				log.warn("提交核保{}数据到泛华", report.getQuoteId(), e);
			}
			log.info("提交核保信息数据{}泛华返回{}结果", report.getQuoteId(), message);

			Gson gson = CommonUtil.gson();
			Map<String, Object> resultMap = gson.fromJson(message,
					new TypeToken<Map<String, Object>>() {
					}.getType());
			if ("-1".equals(resultMap.get("code"))) {
                errorMesage = (String)resultMap.get("msg");
				resultBoolean = false;
			} else {
                errorMesage = (String)resultMap.get("msg");
				quo.setResponse(message);
				quo.setResponseTime(new Date());
				resultBoolean = true;
				quo.setQuoteId(olod.getQuoteId());
				quo.setStatus(true);
			}
		} catch (Exception e) {
			log.warn("提交{}数据到泛华", report.getQuoteId(), e);
			quo.setStatus(false);
		}
		try {
			this.baoxianQuoteInfoDAO.updateReponse(quo);
		} catch (Exception e) {
			log.warn("修改数据{}出错", report.getQuoteId(), e);
		}

        Map<String, Object> ret = Maps.newHashMap();
        ret.put("result", resultBoolean);
        ret.put("msg", errorMesage);
		return ret;
	}

    @Override
    public boolean updatePeisongInQuote(BaoxianUnderwriting underwriting, BaoxianInsureInfo peisong) {
        Map<String, Object> map = new HashMap<String, Object>();// 配送信息
        map.put("peisong", peisong);
        String message = null;

        BaoxianQuoteInfoWithBLOBs latestQuote = this.baoxianQuoteInfoDAO
                .queryLastRequestInfo(underwriting.getId(), 0);

        BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
        try {
            String request = StringUtils.trimAllWhitespace(FreemarkerUtils
                    .process("/peisong.json", map, "/fanhua"));

            info.setId(SerializableUtils.allocUUID());
            info.setRequest(request);
            info.setStatus(false);
            info.setBaoxianUnderwritingId(underwriting.getId());
            info.setStep(0);
            info.setRequestTime(new Date());
            info.setRequestUrl(restful + "/TFService/restful/quote/"
                    + latestQuote.getQuoteId() + "/alter/dealOffer/deliveryInfo");
            this.baoxianQuoteInfoDAO.insert(info);
            try {
                message = HttpClientUtil.doPost(info.getRequestUrl(),
                        info.getRequest());
            } catch (Exception e) {
                log.warn("报价阶段修改配送地址{}到泛华", underwriting.getId(), e);
            }
            log.info("报价阶段修改配送信息数据{}泛华返回结果{}", underwriting.getId(), message);
            Map<String, Object> resultMap = CommonUtil.gson().fromJson(message,
                    new TypeToken<Map<String, Object>>() {
                    }.getType());
            if ("0".equals(resultMap.get("code"))) {
                info.setStatus(true);
            } else {
                info.setStatus(false);
            }
            info.setResponse(message);
            info.setResponseTime(new Date());
            info.setQuoteId(latestQuote.getQuoteId());
        } catch (Exception e) {
            log.warn("报价阶段修改配送地址到泛华{}", underwriting.getId(), e);
            info.setStatus(false);
        }

        try {
            this.baoxianQuoteInfoDAO.updateReponse(info);
        } catch (Exception e) {
            log.warn("报价阶段修改配送地址{}出错", underwriting.getId(), e);
        }
        return info.getStatus();
    }

    @Override
    public Boolean updatePeisongInUnderwrite(String underwritingId, String mid,
                                             String sid, BaoxianInsureInfo peisong) {
        Map<String, Object> map = new HashMap<String, Object>();// 配送信息
        map.put("peisong", peisong);
        String message = null;
        BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
        try {
            info.setId(SerializableUtils.allocUUID());
            String request = StringUtils.trimAllWhitespace(FreemarkerUtils
                    .process("/peisong.json", map, "/fanhua"));
            info.setRequest(request);
            info.setStatus(false);
            info.setBaoxianUnderwritingId(underwritingId);
            info.setStep(0);
            info.setRequestTime(new Date());
            info.setRequestUrl(restful + "/TFService/restful/quote/"
                    + mid + "/update/" + sid + "/deliveryInfo");
            this.baoxianQuoteInfoDAO.insert(info);
            try {
                message = HttpClientUtil.doPost(info.getRequestUrl(),
                        info.getRequest());
            } catch (Exception e) {
                log.warn("提交{}配送地址到泛华", underwritingId, e);
            }
            log.info("提交配送信息数据{}泛华返回结果{}", underwritingId, message);
            Map<String, String> resultMap = CommonUtil.gson().fromJson(message,
                    new TypeToken<Map<String, String>>() {
                    }.getType());
            if ("0".equals(resultMap.get("code"))) {
                info.setStatus(true);
            } else {
                info.setStatus(false);
            }
            info.setResponse(message);
            info.setResponseTime(new Date());
            info.setQuoteId(mid);
        } catch (Exception e) {
            log.warn("提交配送地址到泛华{}", underwritingId, e);
            info.setStatus(false);
        }
        try {
            this.baoxianQuoteInfoDAO.updateReponse(info);
        } catch (Exception e) {
            log.warn("修改配送地址{}出错", underwritingId, e);
        }
        return info.getStatus();
    }

    @Override
    public List<BaoxianCompanyFanhua> queryCompaniesInCity(String cityCode) {
        String url = new StringBuilder().append(restful)
                .append("/TFService/restful/quote/getProviderInfo/").append(cityCode)
                .append('/').append(channelId).toString();
        try {
            String message = HttpClientUtil.doGet(url);
            log.info("获取泛华机构数据{}泛华返回{}结果", cityCode, message);
            List<BaoxianCompanyFanhua> fanhuaList = parseCompanies(cityCode, message);
            return fanhuaList;
        } catch (Exception e) {
            log.warn("获取泛华机构数据", e);
        }
        return null;
    }

    private List<BaoxianCompanyFanhua> parseCompanies(String cityCode, String message) {
        List<BaoxianCompanyFanhua> companyFanhuaList = Lists.newArrayList();

        try {
            Map<String, List<Map<String, String>>> resultMap = CommonUtil.gson().fromJson(message,
                    new TypeToken<Map<String, List<Map<String, String>>>>() {
                    }.getType());
            List<Map<String, String>> providers = resultMap.get("providerInfos");
            for (Map<String, String> provider : providers) {
                BaoxianCompanyFanhua fanhua = new BaoxianCompanyFanhua();
                fanhua.setCityCode(cityCode);
                fanhua.setCode(provider.get("insComId"));
                fanhua.setName(provider.get("insName"));
                fanhua.setBizType(provider.get("bizType"));

                companyFanhuaList.add(fanhua);
            }
        } catch (Exception e) {

        }

        return companyFanhuaList;
    }

    @Override
    public Map<String, Object> initiatePay(BaoxianUnderwritingReport report, String type,
                                            String bankArea, Map<String, String> bank,
                                            BaoxianUnderwritingReportPayinfo pay) {
        Map<String, Object> resultMapInfo = Maps.newHashMap();
        resultMapInfo.put("result", false);

        try {
            String file = "/pay.json";
            if ("bestpay".equalsIgnoreCase(type)) {
                file = "/paybest.json";
            } else if ("99bill".equalsIgnoreCase(type)) {
                file = "/pay99bill.json";
            } else if ("alipay".equalsIgnoreCase(type)
                    || "payali".equalsIgnoreCase(type)) {
                file = "/payali.json";
            } else if ("wxpay.json".equalsIgnoreCase(type)) {
                file = "/paywx.json";
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("quoteId", report.getQuoteId());
            if (bank != null) {
                map.putAll(bank);
            }
            map.put("bankArea", bankArea);
            map.put("notifyUrl", payNotifyUrl);
            map.put("amount", pay.getPrice());
            map.put("insOrg", report.getBaoxianCompanyCode());
            map.put("bizId", report.getQuoteId());
            map.put("nonceStr", SerializableUtils.allocUUID());
            map.put("areaCode", report.getCityCode());
            map.put("payinfoid", pay.getId());

            BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
            info.setId(SerializableUtils.allocUUID());
            info.setStatus(false);
            info.setBaoxianUnderwritingId(report.getBaoxianUnderwritingId());
            info.setStep(8);
            info.setRequestTime(new Date());
            info.setRequestUrl(pmserviceurl
                    + "/PMService/restful/paymentService/pay");
            String request = StringUtils.trimAllWhitespace(FreemarkerUtils
                    .process(file, map, "/fanhua"));
            String tmp = request.replace("###", "");
            Map<String, Object> tmpmap = CommonUtil.gson().fromJson(tmp,
                    new TypeToken<Map<String, Object>>() {
                    }.getType());
            Map<String, Object> sortMap = new TreeMap<String, Object>();
            sortMap.putAll(tmpmap);
            Iterator<Map.Entry<String, Object>> it = sortMap.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            request = request.replace("###", "");
            info.setRequest(request);
            this.baoxianQuoteInfoDAO.insert(info);
            log.info("提交支付{}请求{}泛华", info.getRequestUrl(), info.getRequest());

            String message = HttpClientUtil.doPost(info.getRequestUrl(),
                    info.getRequest());
            log.info("提交支付请求{}泛华返回{}结果", report.getId(), message);

            info.setStatus(true);
            try {
                info.setResponse(message);
                info.setResponseTime(new Date());
                info.setQuoteId(report.getQuoteId());
                this.baoxianQuoteInfoDAO.updateReponse(info);
            } catch (Exception e) {
            }

            Map<String, String> resultMap = CommonUtil.gson().fromJson(message,
                    new TypeToken<Map<String, String>>() {
                    }.getType());
            if ("0000".equals(resultMap.get("code").trim())) {
                pay.setTradeNum(resultMap.get("orderNo"));
                resultMapInfo.put("payUrl", resultMap.get("payUrl"));
                String orderState = resultMap.get("orderState");
                resultMapInfo.put("state", orderState);
                resultMapInfo.put("result", true);
                resultMapInfo.put("request", request);
                resultMapInfo.put("response", message);

                String tradeNum = resultMap.get("paySerialNo");
                if (!StringUtils.hasText(tradeNum)) {
                    tradeNum = resultMap.get("bizTransactionId");
                }
                resultMapInfo.put("tradeNo", tradeNum);
            }
        } catch (Exception e) {

        }
        return resultMapInfo;
    }

    @Override
    public Map<String, String> queryBankInfo(String bankNo, String bankName,
                                              String bankCode) {
        Map<String, String> bank = null;
        if (!StringUtils.hasText(bankNo)) {
            return bank;
        }
        try {
            String response = HttpClientUtil.doGet(bankInfoUrl + "?code="
                    + bankNo);
            bank = CommonUtil.gson().fromJson(response,
                    new TypeToken<Map<String, String>>() {
                        private static final long serialVersionUID = 1L;
                    }.getType());
            if (bank != null) {
                bank.put("bankName", bankName);
                bank.put("bankCode", bankCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bank;
    }

    @Override
    public String syncPayInfo(BaoxianUnderwritingReportPayinfo payinfo) {
        try {
            BaoxianQuoteInfoWithBLOBs info = new BaoxianQuoteInfoWithBLOBs();
            info.setRequestUrl(pmserviceurl
                    + "/PMService/restful/paymentService/"
                    + payinfo.getTradeNum() + "/paymentResult");
            String message = HttpClientUtil.doGet(info.getRequestUrl());
            log.info("提交获取支付信息{}请求{}泛华", info.getRequestUrl(), info.getRequest());
            return message;
        } catch (Exception e) {

        }
        return null;
    }
}
