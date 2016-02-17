package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.OrderAddJson;
import com.kplus.car.parser.ApiField;

public class OrderAddResponse extends Response {

	@ApiField("data")
	private OrderAddJson data;

	public OrderAddJson getData() {
		return data;
	}

	public void setData(OrderAddJson data) {
		this.data = data;
	}

}
