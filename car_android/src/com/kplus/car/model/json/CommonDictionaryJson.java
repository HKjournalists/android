package com.kplus.car.model.json;

import java.util.List;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.CommonDictionary;
import com.kplus.car.parser.ApiListField;

public class CommonDictionaryJson extends BaseModelObj {
	@ApiListField("list")
	private List<CommonDictionary> list;

	public List<CommonDictionary> getList() {
		return list;
	}

	public void setList(List<CommonDictionary> list) {
		this.list = list;
	}
}
