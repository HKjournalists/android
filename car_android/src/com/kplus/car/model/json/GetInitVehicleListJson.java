package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetInitVehicleListJson extends BaseModelObj {

	@ApiListField("list")
	private List<GetInitVehicleJson> list;
	@ApiField("total")
	private Integer total;

	public List<GetInitVehicleJson> getList() {
		if (list == null)
			list = new ArrayList<GetInitVehicleJson>();
		return list;
	}

	public void setList(List<GetInitVehicleJson> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
