package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.UserCashRecord;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class UserCashRecordJson extends BaseModelObj{
	@ApiListField("list")
	private List<UserCashRecord> list;
	@ApiField("total")
	private Integer total;
	
	public List<UserCashRecord> getList() {
		return list;
	}
	public void setList(List<UserCashRecord> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
