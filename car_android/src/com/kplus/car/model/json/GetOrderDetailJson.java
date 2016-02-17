package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.OrderDetail;
import com.kplus.car.model.OrderInfo;
import com.kplus.car.model.OrderPayment;
import com.kplus.car.model.RefundInfo;
import com.kplus.car.parser.ApiField;

public class GetOrderDetailJson extends BaseModelObj {

	@ApiField("orderDetail")
	private OrderDetail orderDetail;
	@ApiField("payment")
	private OrderPayment payment;
	@ApiField("userInfo")
	private OrderInfo userInfo;
	@ApiField("refund")
	private RefundInfo refund;

	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public OrderPayment getPayment() {
		return payment;
	}

	public void setPayment(OrderPayment payment) {
		this.payment = payment;
	}

	public OrderInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(OrderInfo userInfo) {
		this.userInfo = userInfo;
	}

	public RefundInfo getRefund() {
		return refund;
	}

	public void setRefund(RefundInfo refund) {
		this.refund = refund;
	}

}
