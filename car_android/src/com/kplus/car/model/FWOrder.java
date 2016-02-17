package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class FWOrder extends BaseModelObj {
	@ApiField("orderId")
	private Long orderId;
	@ApiField("status")
	private Integer status;
	@ApiField("orderTime")
	private String orderTime;
	@ApiField("serviceName")
	private String serviceName;
	@ApiField("providerName")
	private String providerName;
	@ApiField("orderType")
	private Integer orderType;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
}
