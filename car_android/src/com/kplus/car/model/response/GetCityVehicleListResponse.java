package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetCityVehicleListJson;
import com.kplus.car.parser.ApiField;

public class GetCityVehicleListResponse extends Response {

	@ApiField("data")
	private GetCityVehicleListJson data;

	public GetCityVehicleListJson getData() {
		if (data == null)
			data = new GetCityVehicleListJson();
		return data;
	}

	public void setData(GetCityVehicleListJson data) {
		this.data = data;
	}

}
