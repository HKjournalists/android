package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.BindInviteRecordJson;
import com.kplus.car.parser.ApiField;

public class BindInviteRecordResponse extends Response{
	@ApiField("data")
	private BindInviteRecordJson data;

	public BindInviteRecordJson getData() {
		if(data == null)
			data = new BindInviteRecordJson();
		return data;
	}

	public void setData(BindInviteRecordJson data) {
		this.data = data;
	}
}
