package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.SelfService;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetSelfServiceListJson extends BaseModelObj{

	@ApiListField("list")
	private List<SelfService> list;
	@ApiField("total")
	private Integer total;

	public List<SelfService> getList() {
		if (list == null)
			list = new ArrayList<SelfService>();
		return list;
	}

	public void setList(List<SelfService> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
