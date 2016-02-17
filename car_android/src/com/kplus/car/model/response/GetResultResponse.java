package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetResultJson;
import com.kplus.car.parser.ApiField;

public class GetResultResponse extends Response {

	@ApiField("data")
	private GetResultJson data;

	public GetResultJson getData() {
		return data;
	}

	public void setData(GetResultJson data) {
		this.data = data;
	}

}
