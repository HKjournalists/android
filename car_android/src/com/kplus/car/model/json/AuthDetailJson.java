package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class AuthDetailJson extends BaseModelObj{
	@ApiListField("list")
	private List<VehicleAuth> list;
	@ApiField("total")
	private Integer total;
	public List<VehicleAuth> getList() {
		return list;
	}
	public void setList(List<VehicleAuth> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
