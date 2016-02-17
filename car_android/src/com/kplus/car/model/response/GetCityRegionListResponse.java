package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetCityRegionListJson;
import com.kplus.car.parser.ApiField;

public class GetCityRegionListResponse extends Response {

	@ApiField("data")
	private GetCityRegionListJson data;

	public GetCityRegionListJson getData() {
		if (data == null)
			data = new GetCityRegionListJson();
		return data;
	}

	public void setData(GetCityRegionListJson data) {
		this.data = data;
	}

}
