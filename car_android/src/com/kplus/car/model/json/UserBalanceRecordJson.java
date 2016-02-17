package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.UserBalanceRecord;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class UserBalanceRecordJson extends BaseModelObj{
	@ApiListField("list")
	private List<UserBalanceRecord> list;
	@ApiField("total")
	private Integer total;
	public List<UserBalanceRecord> getList() {
		return list;
	}
	public void setList(List<UserBalanceRecord> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
