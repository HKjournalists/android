package com.chengniu.client.pojo;

public class StatisticsMap {
	private String date;
	private Integer total;
	private Integer intentTotal;
	private Integer underwritingTotal;
	private Integer underwritingSuccessTotal;
	private Integer reportTotal;
	private Integer reportSuccessTotal;
	private Integer ordersTotal;
	private Integer underwritingReportTotal;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getIntentTotal() {
		return intentTotal;
	}

	public void setIntentTotal(Integer intentTotal) {
		this.intentTotal = intentTotal;
	}

	public Integer getUnderwritingTotal() {
		return underwritingTotal;
	}

	public void setUnderwritingTotal(Integer underwritingTotal) {
		this.underwritingTotal = underwritingTotal;
	}

	public Integer getReportTotal() {
		return reportTotal;
	}

	public void setReportTotal(Integer reportTotal) {
		this.reportTotal = reportTotal;
	}

	public Integer getOrdersTotal() {
		return ordersTotal;
	}

	public void setOrdersTotal(Integer ordersTotal) {
		this.ordersTotal = ordersTotal;
	}

	public Integer getUnderwritingReportTotal() {
		return underwritingReportTotal;
	}

	public void setUnderwritingReportTotal(Integer underwritingReportTotal) {
		this.underwritingReportTotal = underwritingReportTotal;
	}

	public Integer getUnderwritingSuccessTotal() {
		return underwritingSuccessTotal;
	}

	public void setUnderwritingSuccessTotal(Integer underwritingSuccessTotal) {
		this.underwritingSuccessTotal = underwritingSuccessTotal;
	}

	public Integer getReportSuccessTotal() {
		return reportSuccessTotal;
	}

	public void setReportSuccessTotal(Integer reportSuccessTotal) {
		this.reportSuccessTotal = reportSuccessTotal;
	}
}
