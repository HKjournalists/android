package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetVehicleListJson;
import com.kplus.car.parser.ApiField;

public class GetVehicleListResponse extends Response
{
	@ApiField("data")
	private GetVehicleListJson data;

	public GetVehicleListJson getData()
	{
		if(data == null)
			data = new GetVehicleListJson();
		return data;
	}

	public void setData(GetVehicleListJson data)
	{
		this.data = data;
	}
}
