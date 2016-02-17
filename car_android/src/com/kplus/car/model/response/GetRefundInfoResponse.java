package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetRefundInfoJson;
import com.kplus.car.model.json.GetUserInfoJson;
import com.kplus.car.parser.ApiField;

public class GetRefundInfoResponse extends Response{
	@ApiField("data")
	private GetRefundInfoJson data;

	public GetRefundInfoJson getData() {
		if(data == null)
			data = new GetRefundInfoJson();
		return data;
	}

	public void setData(GetRefundInfoJson data) {
		this.data = data;
	}
}
