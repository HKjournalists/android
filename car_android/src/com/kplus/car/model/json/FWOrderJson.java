package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.FWOrder;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class FWOrderJson extends BaseModelObj {
	@ApiListField("list")
	private List<FWOrder> list;
	@ApiField("total")
	private Integer total;
	public List<FWOrder> getList() {
		return list;
	}
	public void setList(List<FWOrder> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
