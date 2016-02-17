package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.FWCityServiceJson;
import com.kplus.car.parser.ApiField;

public class FWCityServiceResponse extends Response {
	@ApiField("data")
	private FWCityServiceJson data;

	public FWCityServiceJson getData() {
		return data;
	}

	public void setData(FWCityServiceJson data) {
		this.data = data;
	}
}
