package com.chengniu.client.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class BaoxianUnderwritingReportPayinfo {
	private String id;

	private String tradeNum;

	private String payWay;

	private BigDecimal price;

	private Date createTime;

	private String operatorId;

	private String operatorName;

	private String baoxianUnderwritingReportId;

	private String responseInfo;

	private Date responseTime;

	private String requestInfo;

	private Integer status;

	private String message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum == null ? null : tradeNum.trim();
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay == null ? null : payWay.trim();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId == null ? null : operatorId.trim();
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName == null ? null : operatorName.trim();
	}

	public String getBaoxianUnderwritingReportId() {
		return baoxianUnderwritingReportId;
	}

	public void setBaoxianUnderwritingReportId(
			String baoxianUnderwritingReportId) {
		this.baoxianUnderwritingReportId = baoxianUnderwritingReportId == null ? null
				: baoxianUnderwritingReportId.trim();
	}

	public String getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo == null ? null : responseInfo.trim();
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo == null ? null : requestInfo.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message == null ? null : message.trim();
	}
}