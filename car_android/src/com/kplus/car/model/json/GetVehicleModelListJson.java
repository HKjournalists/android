package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.VehicleBrand;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetVehicleModelListJson extends BaseModelObj {

	@ApiListField("list")
	private List<VehicleBrand> list;
	@ApiField("total")
	private Integer total;

	public List<VehicleBrand> getList() {
		if (list == null)
			list = new ArrayList<VehicleBrand>();
		return list;
	}

	public void setList(List<VehicleBrand> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
