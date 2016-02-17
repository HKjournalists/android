package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianQuoteInfo {
	private String id;

	private String baoxianUnderwritingId;

	private Date requestTime;

	private String requestUrl;

	private Date responseTime;

	private String responseStatus;

	private Boolean status;

	private String totalCharge;

	private String quoteId;

	private String subQuoteId;

	private Integer step;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getBaoxianUnderwritingId() {
		return baoxianUnderwritingId;
	}

	public void setBaoxianUnderwritingId(String baoxianUnderwritingId) {
		this.baoxianUnderwritingId = baoxianUnderwritingId == null ? null
				: baoxianUnderwritingId.trim();
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl == null ? null : requestUrl.trim();
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus == null ? null : responseStatus
				.trim();
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(String totalCharge) {
		this.totalCharge = totalCharge == null ? null : totalCharge.trim();
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId == null ? null : quoteId.trim();
	}

	public String getSubQuoteId() {
		return subQuoteId;
	}

	public void setSubQuoteId(String subQuoteId) {
		this.subQuoteId = subQuoteId;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}
}