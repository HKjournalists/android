package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.RegistClientJson;
import com.kplus.car.parser.ApiField;

public class ClientRegistResponse extends Response {

	@ApiField("data")
	private RegistClientJson data;

	public RegistClientJson getData() {
		return data;
	}

	public void setData(RegistClientJson data) {
		this.data = data;
	}

}
