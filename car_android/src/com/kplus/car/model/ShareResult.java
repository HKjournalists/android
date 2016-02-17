package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class ShareResult extends BaseModelObj
{
	@ApiField("value")
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
}
