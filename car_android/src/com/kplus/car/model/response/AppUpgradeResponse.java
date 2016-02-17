package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.AppVersionJson;
import com.kplus.car.parser.ApiField;

public class AppUpgradeResponse extends Response
{
	@ApiField("data")
	private AppVersionJson data;

	public AppVersionJson getData()
	{
		if(data == null)
			data = new AppVersionJson();
		return data;
	}

	public void setData(AppVersionJson data)
	{
		this.data = data;
	}
}
