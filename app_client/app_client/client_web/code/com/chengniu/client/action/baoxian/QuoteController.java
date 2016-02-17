package com.chengniu.client.action.baoxian;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.chengniu.bx.api.dto.BaoxianInformalReportDTO;
import com.chengniu.client.action.SuperController;
import com.chengniu.client.common.CommonUtil;
import com.chengniu.client.common.DateUtils;
import com.chengniu.client.pojo.BaoxianInformalReport;
import com.chengniu.client.pojo.BaoxianUnderwriting;
import com.chengniu.client.pojo.BaoxianUnderwritingReport;
import com.chengniu.client.pojo.SearchVO;
import com.chengniu.client.pojo.StatisticsMap;
import com.chengniu.client.service.BaoxianInformalQuoteService;
import com.chengniu.client.service.BaoxianOrderService;
import com.chengniu.client.service.BaoxianUnderwritingReportService;
import com.chengniu.client.service.BaoxianUnderwritingService;
import com.chengniu.client.service.FanhuaService;
import com.chengniu.client.service.StatisticsService;
import com.google.gson.GsonBuilder;
import com.kplus.orders.execption.DisposeException;

@Controller
@RequestMapping("/quote")
public class QuoteController extends SuperController {
	@Resource(name = "fanhuaService")
	private FanhuaService fanhuaService;

	@Autowired
	private BaoxianInformalQuoteService baoxianInformalQuoteService;
	@Autowired
	private BaoxianUnderwritingService baoxianUnderwritingService;
	@Resource
	private StatisticsService statisticsService;
	@Autowired
	private BaoxianOrderService baoxianOrderService;
	@Autowired
	private BaoxianUnderwritingReportService underwritingReportService;

    @ResponseBody
    @RequestMapping(value = "/yangguang/payback")
    public String back(@RequestBody String body) throws Exception {
        try {
            // 阳光支付回调
            log.info("阳光支付结果返回{}数据{}", body);
            body = new String(body.getBytes("gbk"), "utf-8");
            String id = body.substring(
                    body.indexOf("<orderNo>") + "<orderNo>".length(),
                    body.indexOf("</orderNo>"));
            String policyNo = body.substring(body.indexOf("<policyNo>")
                    + "<policyNo>".length(), body.indexOf("</policyNo>"));

            this.baoxianOrderService.handleOrderPayStatusChanged(id, true);
            this.baoxianOrderService.updatePolicyNo(id, policyNo);
            return "<?xml version=\"1.0\" encoding=\"GBK\"?><response><isSuccess>T</isSuccess><orderNo>"
                    + id + "</orderNo></response>";
        } catch (Exception e) {
            ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getResponse().setStatus(250);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/midnotify/{id}", method = RequestMethod.POST)
    public String midnotify(@RequestBody String body, @PathVariable String id)
            throws Exception {
        try {
            log.info("泛华返回{}数据{}", id, body);
            fanhuaService.handleNotification(id, body);
        } catch (Exception e) {
            ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getResponse().setStatus(250);
            return "fail";
        }
        return "success";
    }

	@ResponseBody
	@RequestMapping("/statistics")
	public String statistics(@RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime,
			@RequestParam(required = false) Integer userType,
			@RequestParam(required = false) String statisticsType,
			@RequestParam(required = false) String cityCode) {
		SearchVO vo = new SearchVO();
		if (!StringUtils.hasText(endTime)) {
			endTime = DateUtils.format(new Date(), "yyyy-MM-dd");
		}
		if (!StringUtils.hasText(startTime)) {
			Calendar c = Calendar.getInstance();
			c.setTime(DateUtils.parse(endTime, "yyyy-MM-dd"));
			c.add(Calendar.DATE, -7);
			startTime = DateUtils.format(c.getTime(), "yyyy-MM-dd");
		}
		vo.setStartTime(startTime);
		vo.setEndTime(endTime);
		vo.setUserType(userType);
		vo.setStatisticsType(statisticsType);
		vo.setCityCode(cityCode);
		Map<String, StatisticsMap> map = this.statisticsService
				.queryStatisticsMap(vo);
		if (map == null || map.isEmpty())
			return "";
		String message = CommonUtil.gson().toJson(map);
		return message;
	}

	@ResponseBody
	@RequestMapping("/upload/media/{id}")
	public String media(@PathVariable String id, String type) throws Exception {
		Boolean message = Boolean.FALSE;
		try {
			if (type != null && type.startsWith("quote")) {
				message = baoxianInformalQuoteService.disposeMedia(id);
			} else {
				message = this.fanhuaService.disposeUnderwritingUploadMedia(id);
			}
		} catch (Exception e) {
			message = Boolean.FALSE;
		}
		return String.valueOf(message);
	}

	@ResponseBody
	@RequestMapping("/query/{reportId}")
	public String query(@PathVariable String reportId) throws Exception {
		try {
			return String.valueOf(this.baoxianInformalQuoteService
					.disposeQuery(reportId));
		} catch (Exception e) {
		}
		return String.valueOf(Boolean.FALSE);
	}

	@ResponseBody
	@RequestMapping(value = "/pay/{reportId}")
	public String pay(@PathVariable String reportId) throws Exception {
		try {
			Map<String, Object> resultMap = this.fanhuaService.disposePay(
					reportId, "99bill", null, null, null);
			return resultMap.get("result").toString();
		} catch (Exception e) {
		}
		return String.valueOf(Boolean.FALSE);
	}

	@ResponseBody
	@RequestMapping(value = "/manualNotifyPay/{reportId}")
	public String manualNotifyPay(@PathVariable String reportId)
			throws Exception {
		try {
			return String.valueOf(this.fanhuaService
                    .disposeManualNotifyPay(reportId));
		} catch (Exception e) {
		}
		return String.valueOf(Boolean.FALSE);
	}

	@ResponseBody
	@RequestMapping(value = "/underwriting/{underwritingId}")
	public String underwriting(@PathVariable String underwritingId)
			throws Exception {
		Boolean message = Boolean.FALSE;
		try {
			message = this.baoxianUnderwritingService
					.disposeUnderwriting(underwritingId);
		} catch (Exception e) {
		}
		return String.valueOf(message);
	}

	@ResponseBody
	@RequestMapping({ "/cancel/{requestId}" })
	public String cancel(@PathVariable String requestId,
			@RequestParam(required = false) String id) throws Exception {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("result", Boolean.FALSE);
		message.put("msg", "操作失败");
		try {
			BaoxianUnderwritingReport report = underwritingReportService
					.queryByUnderwritingId(requestId);
			if (report != null && !StringUtils.isEmpty(report.getOrdersNum())) {
				message = baoxianOrderService.disposeCancel(report);
			} else {
				BaoxianUnderwriting underwriting = baoxianUnderwritingService
						.query(requestId);
				if (underwriting != null) {
					message = baoxianUnderwritingService
							.disposeCancel(underwriting);
				} else {
					message = this.baoxianInformalQuoteService.disposeCancel(
							requestId, id);
				}
			}
		} catch (Exception e) {
			message.put("result", false);
		}
		return URLEncoder.encode(CommonUtil.gson().toJson(message), "utf-8");
	}

	@ResponseBody
	@RequestMapping(value = "/log/{underwritingId}")
	public String log(@PathVariable String underwritingId) throws Exception {
		log.info(underwritingId);
		return "true";
	}

	@ResponseBody
	@RequestMapping(value = "/payback/{id}")
	public String payback(@RequestBody String body, @PathVariable String id)
			throws Exception {
		try {
			log.info("泛华支付结果返回{}数据{}", id, body);
			if (!fanhuaService.handlePaymentResult(id, body))
				throw new DisposeException("操作出错");
		} catch (Exception e) {
			((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getResponse().setStatus(250);
			return "fail";
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/midreport/{id}/{company}", method = RequestMethod.POST)
	public String midreport(@PathVariable String id,
			@PathVariable String company, @RequestBody String remark)
			throws Exception {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("result", Boolean.FALSE);
		message.put("msg", "操作失败");
		try {
			message = this.baoxianInformalQuoteService.disposeRequestToPost(id,
					company, remark);
		} catch (Exception e) {
			((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getResponse().setStatus(250);
			message.put("result", Boolean.FALSE);
		}
		return URLEncoder.encode(CommonUtil.gson().toJson(message), "utf-8");
	}

	@ResponseBody
	@RequestMapping(value = "/manualReport/{id}", method = RequestMethod.POST)
	public String updateInformalReport(@RequestBody String body,
			@PathVariable String id) {
		BaoxianInformalReportDTO info = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping()
				.create().fromJson(body, BaoxianInformalReportDTO.class);

		BaoxianInformalReport report = baoxianInformalQuoteService
				.queryInformalReport(id);
		if (report == null) {
			return "false";
		}

		report = CommonUtil.simpleValueCopy(info, report);
		boolean ret = baoxianInformalQuoteService.saveManualQuoteReport(report);
		return ret ? "true" : "false";
	}

	@ResponseBody
	@RequestMapping(value = "/markReportFailed/{id}", method = RequestMethod.POST)
	public String reportInformalReportFailed(@PathVariable String id,
			@RequestBody String remark) {
		BaoxianInformalReport report = baoxianInformalQuoteService
				.queryInformalReport(id);
		if (report == null) {
			return "false";
		}

		boolean succ = baoxianInformalQuoteService.updateInformalReportFailed(
				report, remark);
		return succ ? "true" : "false";
	}

}