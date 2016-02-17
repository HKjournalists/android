package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.ChangePhoneResultJson;
import com.kplus.car.parser.ApiField;

public class ChangePhoneResponse extends Response {
	@ApiField("data")
	private ChangePhoneResultJson data;

	public ChangePhoneResultJson getData() {
		if(data == null)
			data = new ChangePhoneResultJson();
		return data;
	}

	public void setData(ChangePhoneResultJson data) {
		this.data = data;
	}
}
