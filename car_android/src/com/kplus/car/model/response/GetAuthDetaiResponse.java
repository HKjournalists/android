package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.AuthDetailJson;
import com.kplus.car.parser.ApiField;

public class GetAuthDetaiResponse extends Response{
	@ApiField("data")
	private AuthDetailJson data;

	public AuthDetailJson getData() {
		if(data == null)
			data = new AuthDetailJson();
		return data;
	}

	public void setData(AuthDetailJson data) {
		this.data = data;
	}
}
