package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class GetStringValueJson extends BaseModelObj {

	@ApiField("value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
