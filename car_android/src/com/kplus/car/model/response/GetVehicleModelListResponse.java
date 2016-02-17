package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetVehicleModelListJson;
import com.kplus.car.parser.ApiField;

public class GetVehicleModelListResponse extends Response {

	@ApiField("data")
	private GetVehicleModelListJson data;

	public GetVehicleModelListJson getData() {
		if (data == null)
			data = new GetVehicleModelListJson();
		return data;
	}

	public void setData(GetVehicleModelListJson data) {
		this.data = data;
	}

}
