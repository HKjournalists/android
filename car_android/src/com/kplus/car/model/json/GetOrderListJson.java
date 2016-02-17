package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.Order;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetOrderListJson extends BaseModelObj{

	@ApiListField("list")
	private List<Order> list;
	@ApiField("total")
	private Integer total;

	public List<Order> getList() {
		if (list == null)
			list = new ArrayList<Order>();
		return list;
	}

	public void setList(List<Order> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
