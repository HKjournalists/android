package com.chengniu.client.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class BaoxianPolicyPayinfo {
	private String id;

	private String orderNum;

	private BigDecimal price;

	private String payWay;

	private String tradeNum;

	private Date payTime;

	private String payAccount;

	private String operatorId;

	private String operatorName;

	private Date createTime;

	private String baoxianUnderwritingReportId;

	private String policyNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum == null ? null : orderNum.trim();
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay == null ? null : payWay.trim();
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum == null ? null : tradeNum.trim();
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount == null ? null : payAccount.trim();
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBaoxianUnderwritingReportId() {
		return baoxianUnderwritingReportId;
	}

	public void setBaoxianUnderwritingReportId(
			String baoxianUnderwritingReportId) {
		this.baoxianUnderwritingReportId = baoxianUnderwritingReportId == null ? null
				: baoxianUnderwritingReportId.trim();
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo == null ? null : policyNo.trim();
	}
}