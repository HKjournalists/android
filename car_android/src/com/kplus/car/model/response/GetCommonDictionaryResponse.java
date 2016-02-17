package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.CommonDictionaryJson;
import com.kplus.car.parser.ApiField;

public class GetCommonDictionaryResponse extends Response {
	@ApiField("data")
	private CommonDictionaryJson data;

	public CommonDictionaryJson getData() {
		return data;
	}

	public void setData(CommonDictionaryJson data) {
		this.data = data;
	}
}
