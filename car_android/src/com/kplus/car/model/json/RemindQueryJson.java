package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.ArrayList;
import java.util.List;

public class RemindQueryJson extends BaseModelObj {

	@ApiField("value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
