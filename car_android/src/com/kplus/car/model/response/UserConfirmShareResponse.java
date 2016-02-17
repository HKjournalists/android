package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UserConfirmShareJson;
import com.kplus.car.parser.ApiField;

public class UserConfirmShareResponse extends Response	{
	@ApiField("data")
	private UserConfirmShareJson data;

	public UserConfirmShareJson getData() {
		if(data == null)
			data = new UserConfirmShareJson();
		return data;
	}

	public void setData(UserConfirmShareJson data) {
		this.data = data;
	}
}
