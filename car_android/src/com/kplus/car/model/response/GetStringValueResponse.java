package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetStringValueJson;
import com.kplus.car.parser.ApiField;

public class GetStringValueResponse extends Response {

	@ApiField("data")
	private GetStringValueJson data;

	public GetStringValueJson getData() {
		if (data == null)
			data = new GetStringValueJson();
		return data;
	}

	public void setData(GetStringValueJson data) {
		this.data = data;
	}

}
