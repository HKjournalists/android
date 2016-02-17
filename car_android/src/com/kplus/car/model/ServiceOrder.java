package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class ServiceOrder extends BaseModelObj{
	@ApiField("orderId")
	private Long orderId;//订单id
	@ApiField("orderNum")
	private String orderNum;//订单号
	@ApiField("status")
	private Integer status;//订单状态
	@ApiField("orderTime")
	private String orderTime;//订单时间
	@ApiField("serviceName")
	private String serviceName;//服务名
	@ApiField("serviceProviderName")
	private String serviceProviderName;//服务商名
	@ApiField("price")
	private Float price;//订单金额
	@ApiField("descr")
	private String descr;//描述
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
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
	public String getServiceProviderName() {
		return serviceProviderName;
	}
	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
}
