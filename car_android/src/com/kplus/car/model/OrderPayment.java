package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * 订单支付信息
 * 
 * @author Administrator
 * 
 */
public class OrderPayment extends BaseModelObj {

	@ApiField("payNum")
	private String payNum; // 支付流水号
	@ApiField("money")
	private Float money; // 支付金额
	@ApiField("payType")
	private Integer payType; // 支付类型
	@ApiField("createTime")
	private String createTime; // 支付时间
	@ApiField("cash")
	private Float cash;//余额支付金额

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Float getCash() {
		return cash;
	}

	public void setCash(Float cash) {
		this.cash = cash;
	}

}
