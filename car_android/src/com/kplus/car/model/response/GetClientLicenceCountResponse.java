package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.ClientLicenceCount;
import com.kplus.car.parser.ApiField;

public class GetClientLicenceCountResponse extends Response
{
	@ApiField("data")
	private ClientLicenceCount data;

	public ClientLicenceCount getData() {
		if (data == null)
			data = new ClientLicenceCount();
		return data;
	}

	public void setData(ClientLicenceCount data) {
		this.data = data;
	}
}
