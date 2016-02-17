package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetPriceValueJson;
import com.kplus.car.parser.ApiField;

public class GetPriceValueResponse extends Response {

	@ApiField("data")
	private GetPriceValueJson data;

	public GetPriceValueJson getData() {
		if (data == null)
			data = new GetPriceValueJson();
		return data;
	}

	public void setData(GetPriceValueJson data) {
		this.data = data;
	}

}
