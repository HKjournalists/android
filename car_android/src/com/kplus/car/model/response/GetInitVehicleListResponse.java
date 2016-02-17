package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetInitVehicleListJson;
import com.kplus.car.parser.ApiField;

public class GetInitVehicleListResponse extends Response {

	@ApiField("data")
	private GetInitVehicleListJson data;

	public GetInitVehicleListJson getData() {
		if (data == null)
			data = new GetInitVehicleListJson();
		return data;
	}

	public void setData(GetInitVehicleListJson data) {
		this.data = data;
	}

}
