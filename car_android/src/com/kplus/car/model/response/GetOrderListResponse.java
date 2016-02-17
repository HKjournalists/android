package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetOrderListJson;
import com.kplus.car.parser.ApiField;

public class GetOrderListResponse extends Response {

	@ApiField("data")
	private GetOrderListJson data;

	public GetOrderListJson getData() {
		if (data == null)
			data = new GetOrderListJson();
		return data;
	}

	public void setData(GetOrderListJson data) {
		this.data = data;
	}

}
