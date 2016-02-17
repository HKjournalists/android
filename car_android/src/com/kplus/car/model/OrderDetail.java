package com.kplus.car.model;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

/**
 * 订单详细内容
 * 
 * @author Administrator
 * 
 */
public class OrderDetail extends BaseModelObj {

	@ApiField("orderNum")
	private String orderNum; // 订单号
	@ApiField("vehicleNum")
	private String vehicleNum; // 车牌号
	@ApiField("orderTime")
	private String orderTime; // 下单时间
	@ApiField("status")
	private Integer status; // 状态
	@ApiField("price")
	private Float totalPrice; // 支付总价
	@ApiField("balancePay")
	private Integer balancePay; // （代金券抵扣服务费金额）
	@ApiField("couponPay")
	private Float couponPay;//代金券抵扣本金金额
	@ApiField("canPay")
	private Boolean canPay; // 能否支付
	@ApiField("orderInfoFlag")
	private String orderInfoFlag; // 补充信息
	@ApiListField("againsts")
	private List<OrderAgainst> againsts; // 违章记录
	@ApiField("updateTime")
	private String updateTime;//订单更新时间
	@ApiField("desc")
	private String desc;
	@ApiField("serviceCost")
	private Float serviceCost;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Float getTotalPrice() {
		return totalPrice == null ? 0 : totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getBalancePay() {
		return balancePay == null ? 0 : balancePay;
	}

	public void setBalancePay(Integer balancePay) {
		this.balancePay = balancePay;
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

	public List<OrderAgainst> getAgainsts() {
		if (againsts == null)
			againsts = new ArrayList<OrderAgainst>();
		return againsts;
	}

	public void setAgainsts(List<OrderAgainst> againsts) {
		this.againsts = againsts;
	}

	public String getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public Float getCouponPay() {
		return couponPay;
	}

	public void setCouponPay(Float couponPay) {
		this.couponPay = couponPay;
	}

	public Float getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(Float serviceCost) {
		this.serviceCost = serviceCost;
	}

}
