package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.VehicleAuthJson;
import com.kplus.car.parser.ApiField;

public class VehicleAuthResponse extends Response{
	@ApiField("data")
	private VehicleAuthJson data;

	public VehicleAuthJson getData() {
		if(data == null)
			data = new VehicleAuthJson();
		return data;
	}

	public void setData(VehicleAuthJson data) {
		this.data = data;
	}
}
