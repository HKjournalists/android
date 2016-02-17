package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.ThirdpartBindResultJson;
import com.kplus.car.parser.ApiField;

public class BindThirdPartResponse extends Response {
	@ApiField("data")
	private ThirdpartBindResultJson data;

	public ThirdpartBindResultJson getData() {
		if(data == null)
			data = new ThirdpartBindResultJson();
		return data;
	}

	public void setData(ThirdpartBindResultJson data) {
		this.data = data;
	}
}
