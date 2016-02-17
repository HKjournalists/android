package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UserLoginJson;
import com.kplus.car.parser.ApiField;

public class UserLoginResponse extends Response{
	@ApiField("data")
	private UserLoginJson data;

	public UserLoginJson getData() {
		if(data == null)
			data = new UserLoginJson();
		return data;
	}

	public void setData(UserLoginJson data) {
		this.data = data;
	}
}
