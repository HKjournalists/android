package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetRestrictNumJson;
import com.kplus.car.parser.ApiField;

public class GetRestrictNumResponse extends Response {

	@ApiField("data")
	private GetRestrictNumJson data;

	public GetRestrictNumJson getData() {
		if (data == null)
			data = new GetRestrictNumJson();
		return data;
	}

	public void setData(GetRestrictNumJson data) {
		this.data = data;
	}

}
