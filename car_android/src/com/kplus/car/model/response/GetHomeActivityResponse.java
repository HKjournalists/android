package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetBalanceJson;
import com.kplus.car.model.json.GetHomeActivityJson;
import com.kplus.car.parser.ApiField;

public class GetHomeActivityResponse extends Response {

	@ApiField("data")
	private GetHomeActivityJson data;

	public GetHomeActivityJson getData() {
		if (data == null)
			data = new GetHomeActivityJson();
		return data;
	}

	public void setData(GetHomeActivityJson data) {
		this.data = data;
	}

}
