package com.chengniu.client.action.baoxian;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.chengniu.client.action.SuperAction;
import com.chengniu.client.common.MediaDictionary;
import com.chengniu.client.domain.QuoteRequestStatus;
import com.chengniu.client.pojo.BaoxianBaseInfoMediaDTO;
import com.chengniu.client.pojo.BaoxianCompany;
import com.chengniu.client.pojo.BaoxianInformalReport;
import com.chengniu.client.pojo.BaoxianInformalReportSummaryDTO;
import com.chengniu.client.pojo.BaoxianUnderwritingRequest;
import com.chengniu.client.service.BaoxianBaseInfoService;
import com.chengniu.client.service.BaoxianCompanyService;
import com.chengniu.client.service.BaoxianInformalQuoteService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 保险报价信息查询接口
 */
public class BaoxianQuoteAction extends SuperAction {

	@Autowired
	private BaoxianInformalQuoteService baoxianInformalQuoteService;

	@Autowired
	private BaoxianCompanyService baoxianCompanyService;

	@Autowired
	private BaoxianBaseInfoService baoxianBaseInfoService;

	private String id;

	private String informalReportId;

	/**
	 * 获取报价状态列表接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public String fetchIntentQuotes() throws Exception {
		if (StringUtils.isEmpty(id)) {
			return super.ajax("参数错误");
		}

		BaoxianUnderwritingRequest request = baoxianInformalQuoteService
				.queryRequestByIntentId(id);
		if (request == null) {
			return super.ajax("参数错误");
		}

		List<BaoxianInformalReportSummaryDTO> informalReportSummaryDTOList = Lists
				.newArrayList();
		List<BaoxianInformalReport> reportList = baoxianInformalQuoteService
				.fetchQuoteList(request.getId());
		if (reportList != null && !reportList.isEmpty()) {
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

				BaoxianCompany company = this.baoxianCompanyService.query(
						informalReport.getBaoxianCompanyCode(),
						informalReport.getCityCode());
				if (company != null) {
					r.setCompanyUrl(company.getPicSmall());
					r.setCompanyRemark(company.getRemark());
				}

				informalReportSummaryDTOList.add(r);
			}
		}

		if (informalReportSummaryDTOList.isEmpty()) {
			List<BaoxianCompany> localCompanys = baoxianCompanyService.list(
					request.getCityCode(), request.getUserType());
			if (localCompanys != null) {
				for (BaoxianCompany company : localCompanys) {
					if (company.getOpenInfo() != null
                            && (company.getChannelStatus() != null && company.getChannelStatus())
							&& (company.getOpenInfo() == request.getUserType() || company
									.getOpenInfo() == 2)) {
						BaoxianInformalReportSummaryDTO r = new BaoxianInformalReportSummaryDTO();
						if (request.getStatus() != null
								&& request.getStatus().intValue() == QuoteRequestStatus.QuoteFailed
										.getValue())
							r.setStatus(QuoteRequestStatus.SubmitFailed
									.getValue());
						else
							r.setStatus(0);
						r.setCompanyCode(company.getCode());
						r.setCompanyName(company.getName());
						r.setCompanyUrl(company.getPicSmall());
						r.setCompanyRemark(company.getRemark());

						informalReportSummaryDTOList.add(r);
					}
				}
			}
		}

		List<BaoxianBaseInfoMediaDTO> mediaDTOList = MediaDictionary
				.fixMediaTemplate(baoxianBaseInfoService.queryMediaInfo(request
						.getBaoxianBaseInfoId()));

		Map<String, Object> ret = Maps.newHashMap();

		ret.put("status", request.getStatus());
		ret.put("failType", request.getFailType());
		ret.put("msg", request.getFailMessage());
		ret.put("request", request);
		ret.put("medias", mediaDTOList);
		ret.put("reportList", informalReportSummaryDTOList);

		return super.ajax(ret);
	}

	private List<BaoxianInformalReportSummaryDTO> fetchDummyReportList(
			BaoxianUnderwritingRequest underwritingRequest) {
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
                        && (company.getChannelStatus() != null && company.getChannelStatus())
						&& (company.getOpenInfo() == underwritingRequest
								.getUserType() || company.getOpenInfo() == 2)) {
					BaoxianInformalReportSummaryDTO r = new BaoxianInformalReportSummaryDTO();

					r.setStatus(0);
					r.setCompanyCode(company.getCode());
					r.setCompanyName(company.getName());
					r.setCompanyUrl(company.getPicSmall());

					dummayReportList.add(r);
				}
			}
		}
		return dummayReportList;
	}

	/**
	 * 获取报价详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String fetchQuoteDetail() throws Exception {
		if (StringUtils.isEmpty(informalReportId)) {
			return super.ajax("参数错误");
		}

		BaoxianInformalReport report = baoxianInformalQuoteService
				.queryInformalReport(informalReportId);
		if (report == null) {
			return super.ajax("参数错误");
		}

		BaoxianUnderwritingRequest request = baoxianInformalQuoteService
				.queryRequestByIntentId(report.getBaoxianIntentId());
		if (request == null) {
			return super.ajax("参数错误");
		}

		Map<String, Object> ret = Maps.newHashMap();
		ret.put("request", request);
		ret.put("report", report);
		return super.ajax(report);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInformalReportId(String informalReportId) {
		this.informalReportId = informalReportId;
	}
}
