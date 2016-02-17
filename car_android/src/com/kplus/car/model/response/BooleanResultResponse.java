package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.BooleanResultJson;
import com.kplus.car.parser.ApiField;

public class BooleanResultResponse extends Response {
	@ApiField("data")
	private BooleanResultJson data;

	public BooleanResultJson getData() {
		if(data == null)
			data = new BooleanResultJson();
		return data;
	}

	public void setData(BooleanResultJson data) {
		this.data = data;
	}
}
