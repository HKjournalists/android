package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.AddRefundInfoJson;
import com.kplus.car.model.response.request.AddUserInfoJson;
import com.kplus.car.parser.ApiField;

public class AddRefundInfoReponse extends Response{
	@ApiField("data")
	private AddRefundInfoJson data;

	public AddRefundInfoJson getData() {
		if(data == null)
			data = new AddRefundInfoJson();
		return data;
	}

	public void setData(AddRefundInfoJson data) {
		this.data = data;
	}
}
