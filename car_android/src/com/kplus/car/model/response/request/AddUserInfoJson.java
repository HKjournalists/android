package com.kplus.car.model.response.request;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class AddUserInfoJson extends BaseModelObj{
	@ApiField("result")
	private Boolean result;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}
}
