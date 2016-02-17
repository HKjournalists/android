package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class GetBalanceJson extends BaseModelObj {

	@ApiField("balance")
	private Integer balance;
	@ApiField("totalBalance")
	private Integer totalBalance;
	@ApiField("payBalance")
	private Integer payBalance;

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Integer totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Integer getPayBalance() {
		return payBalance;
	}

	public void setPayBalance(Integer payBalance) {
		this.payBalance = payBalance;
	}

}
