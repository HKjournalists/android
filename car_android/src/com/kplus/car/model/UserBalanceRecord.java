package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class UserBalanceRecord extends BaseModelObj{
	@ApiField("time")
	private String time;//发生时间，格式yyyy-MM-dd HH:mm
	@ApiField("balancePay")
	private Integer balancePay;//余额变化值，带正负符号
	@ApiField("content")
	private String content;//描述说明
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getBalancePay() {
		return balancePay;
	}
	public void setBalancePay(Integer balancePay) {
		this.balancePay = balancePay;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
