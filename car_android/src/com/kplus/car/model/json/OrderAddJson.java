package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class OrderAddJson extends BaseModelObj {

	@ApiField("orderId")
	private Long orderId;
	@ApiField("orderNum")
	private String orderNum;
	@ApiField("canPay")
	private Boolean canPay;
	@ApiField("orderInfoFlag")
	private String orderInfoFlag;

	public Long getOrderId() {
		return orderId == null ? 0 : orderId;
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

	public Boolean getCanPay() {
		return canPay == null ? false : canPay;
	}

	public void setCanPay(Boolean canPay) {
		this.canPay = canPay;
	}

	public String getOrderInfoFlag() {
		return orderInfoFlag;
	}

	public void setOrderInfoFlag(String orderInfoFlag) {
		this.orderInfoFlag = orderInfoFlag;
	}

}
