package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetSelfServiceListJson;
import com.kplus.car.parser.ApiField;

public class GetSelfServiceListResponse extends Response {

	@ApiField("data")
	private GetSelfServiceListJson data;

	public GetSelfServiceListJson getData() {
		if (data == null)
			data = new GetSelfServiceListJson();
		return data;
	}

	public void setData(GetSelfServiceListJson data) {
		this.data = data;
	}

}
