package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetAgencyServiceListJson;
import com.kplus.car.parser.ApiField;

public class GetAgencyServiceListResponse extends Response {

	@ApiField("data")
	private GetAgencyServiceListJson data;

	public GetAgencyServiceListJson getData() {
		if (data == null)
			data = new GetAgencyServiceListJson();
		return data;
	}

	public void setData(GetAgencyServiceListJson data) {
		this.data = data;
	}

}
