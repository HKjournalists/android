package com.kplus.car.model;

import java.io.Serializable;
import java.util.Date;

import com.kplus.car.parser.ApiField;

public class Refund extends BaseModelObj implements Serializable{
	@ApiField("name")//退款账户用户名
	private String name;
	@ApiField("type")//退款方式
	private Integer type;
	@ApiField("account")//退款账号
	private String account;
	@ApiField("bank")//开户银行
	private String bank;
	
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
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
