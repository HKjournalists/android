package com.kplus.car.model.json;

import java.util.ArrayList;
import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.CityRegion;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class GetCityRegionListJson extends BaseModelObj {

	@ApiListField("list")
	private List<CityRegion> list;
	@ApiField("total")
	private Integer total;

	public List<CityRegion> getList() {
		if (list == null)
			list = new ArrayList<CityRegion>();
		return list;
	}

	public void setList(List<CityRegion> list) {
		this.list = list;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
