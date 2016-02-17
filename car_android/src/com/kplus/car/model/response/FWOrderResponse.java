package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.FWOrderJson;
import com.kplus.car.parser.ApiField;

public class FWOrderResponse extends Response {
	@ApiField("data")
	private FWOrderJson data;

	public FWOrderJson getData() {
		return data;
	}

	public void setData(FWOrderJson data) {
		this.data = data;
	}
}
