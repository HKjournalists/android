package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.AgencyService;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetAgencyServiceListJson extends BaseModelObj{

	@ApiListField("list")
	private List<AgencyService> list;
	@ApiField("total")
	private Integer total;

	public List<AgencyService> getList() {
		if (list == null)
			list = new ArrayList<AgencyService>();
		return list;
	}

	public void setList(List<AgencyService> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
