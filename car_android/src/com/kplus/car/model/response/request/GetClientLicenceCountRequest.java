package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetClientLicenceCountResponse;

public class GetClientLicenceCountRequest extends BaseRequest<GetClientLicenceCountResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/clientLicence/count.htm";
	}

	@Override
	public Class<GetClientLicenceCountResponse> getResponseClass()
	{
		// TODO Auto-generated method stub
		return GetClientLicenceCountResponse.class;
	}
}
