package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class OrderAccount extends BaseModelObj {
	@ApiField("dealOrderCount")
	private Integer dealOrderCount;
	@ApiField("noPayOrderCount")
	private Integer noPayOrderCount;
	@ApiField("pendingOrderCount")
	private Integer pendingOrderCount;
	@ApiField("closeOrderCount")
	private Integer closeOrderCount;
	@ApiField("pendingFailOrderCount")
	private Integer pendingFailOrderCount;
	public Integer getDealOrderCount() {
		return dealOrderCount;
	}
	public void setDealOrderCount(Integer dealOrderCount) {
		this.dealOrderCount = dealOrderCount;
	}
	public Integer getNoPayOrderCount() {
		return noPayOrderCount;
	}
	public void setNoPayOrderCount(Integer noPayOrderCount) {
		this.noPayOrderCount = noPayOrderCount;
	}
	public Integer getPendingOrderCount() {
		return pendingOrderCount;
	}
	public void setPendingOrderCount(Integer pendingOrderCount) {
		this.pendingOrderCount = pendingOrderCount;
	}
	public Integer getCloseOrderCount() {
		return closeOrderCount;
	}
	public void setCloseOrderCount(Integer closeOrderCount) {
		this.closeOrderCount = closeOrderCount;
	}
	public Integer getPendingFailOrderCount() {
		return pendingFailOrderCount;
	}
	public void setPendingFailOrderCount(Integer pendingFailOrderCount) {
		this.pendingFailOrderCount = pendingFailOrderCount;
	}
	
}
