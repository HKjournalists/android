package com.kplus.car.model;

import java.util.Date;

import com.kplus.car.parser.ApiField;

public class RefundInfo extends BaseModelObj{
	@ApiField("name")//退款账户用户名
	private String name;
	@ApiField("type")//退款方式
	private Integer type;
	@ApiField("account")//退款账号
	private String account;
	@ApiField("createDatetime")//退款时间
	private String createDatetime;
	

	public String getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}		
}
