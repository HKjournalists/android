package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.ServicePhone;
import com.kplus.car.parser.ApiField;

public class GetServicePhoneResponse extends Response
{
	@ApiField("data")
	private ServicePhone data;

	public ServicePhone getData()
	{
		if(data == null)
			data = new ServicePhone();
		return data;
	}

	public void setData(ServicePhone data)
	{
		this.data = data;
	}
}
