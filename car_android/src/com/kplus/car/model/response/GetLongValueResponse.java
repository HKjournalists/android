package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetLongValueJson;
import com.kplus.car.parser.ApiField;

public class GetLongValueResponse extends Response {

	@ApiField("data")
	private GetLongValueJson data;

	public GetLongValueJson getData() {
		if (data == null)
			data = new GetLongValueJson();
		return data;
	}

	public void setData(GetLongValueJson data) {
		this.data = data;
	}

}
