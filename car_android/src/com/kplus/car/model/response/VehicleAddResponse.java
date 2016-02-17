package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.VehicleAddResult;
import com.kplus.car.parser.ApiField;

public class VehicleAddResponse extends Response
{
	@ApiField("data")
	private VehicleAddResult data;

	public VehicleAddResult getData() {
		if(data == null)
			data = new VehicleAddResult();
		return data;
	}

	public void setData(VehicleAddResult data) {
		this.data = data;
	}
}
