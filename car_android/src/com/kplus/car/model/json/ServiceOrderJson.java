package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.ServiceOrder;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class ServiceOrderJson extends BaseModelObj{
	@ApiListField("list")
	private List<ServiceOrder> list;
	@ApiField("total")
	private Integer total;
	
	public List<ServiceOrder> getList() {
		return list;
	}
	public void setList(List<ServiceOrder> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
