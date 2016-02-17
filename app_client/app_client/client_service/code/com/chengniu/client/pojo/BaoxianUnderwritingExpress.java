package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianUnderwritingExpress {
	private String id;

	private String company;

	private String orderNum;

	private Date expressTime;

	private String operatorId;

	private String operatorName;

	private Date createTime;

	private String baoxianUnderwritingReportId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company == null ? null : company.trim();
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum == null ? null : orderNum.trim();
	}

	public Date getExpressTime() {
		return expressTime;
	}

	public void setExpressTime(Date expressTime) {
		this.expressTime = expressTime;
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
}