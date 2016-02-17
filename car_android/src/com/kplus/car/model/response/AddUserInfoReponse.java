package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.response.request.AddUserInfoJson;
import com.kplus.car.parser.ApiField;

public class AddUserInfoReponse extends Response{
	@ApiField("data")
	private AddUserInfoJson data;

	public AddUserInfoJson getData() {
		if(data == null)
			data = new AddUserInfoJson();
		return data;
	}

	public void setData(AddUserInfoJson data) {
		this.data = data;
	}
}
