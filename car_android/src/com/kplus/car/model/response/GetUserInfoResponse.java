package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetUserInfoJson;
import com.kplus.car.parser.ApiField;

public class GetUserInfoResponse extends Response{
	@ApiField("data")
	private GetUserInfoJson data;

	public GetUserInfoJson getData() {
		if(data == null)
			data = new GetUserInfoJson();
		return data;
	}

	public void setData(GetUserInfoJson data) {
		this.data = data;
	}
}
