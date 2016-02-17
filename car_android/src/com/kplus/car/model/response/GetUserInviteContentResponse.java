package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UserInviteContentJson;
import com.kplus.car.parser.ApiField;

public class GetUserInviteContentResponse extends Response{
	@ApiField("data")
	private UserInviteContentJson data;

	public UserInviteContentJson getData() {
		if(data == null)
			data = new UserInviteContentJson();
		return data;
	}

	public void setData(UserInviteContentJson data) {
		this.data = data;
	}
}
