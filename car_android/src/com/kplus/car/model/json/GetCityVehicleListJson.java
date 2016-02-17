package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetCityVehicleListJson extends BaseModelObj {

	@ApiListField("list")
	private List<CityVehicle> list;
	@ApiField("total")
	private Integer total;

	public List<CityVehicle> getList() {
		if (list == null)
			list = new ArrayList<CityVehicle>();
		return list;
	}

	public void setList(List<CityVehicle> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
