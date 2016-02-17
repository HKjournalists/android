package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetOrderDetailJson;
import com.kplus.car.parser.ApiField;

public class GetOrderDetailResponse extends Response {

	@ApiField("data")
	private GetOrderDetailJson data;

	public GetOrderDetailJson getData() {
		if (data == null)
			data = new GetOrderDetailJson();
		return data;
	}

	public void setData(GetOrderDetailJson data) {
		this.data = data;
	}

}
