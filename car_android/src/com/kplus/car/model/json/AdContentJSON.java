package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.AdContent;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiListField;

public class AdContentJSON extends BaseModelObj {
	@ApiListField("list")
	private List<AdContent> list;

	public List<AdContent> getList() {
		return list;
	}

	public void setList(List<AdContent> list) {
		this.list = list;
	}
}
