package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetBalanceJson;
import com.kplus.car.parser.ApiField;

public class GetBalanceResponse extends Response {

	@ApiField("data")
	private GetBalanceJson data;

	public GetBalanceJson getData() {
		if (data == null)
			data = new GetBalanceJson();
		return data;
	}

	public void setData(GetBalanceJson data) {
		this.data = data;
	}

}
