package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetServicePhoneResponse;

public class GetServicePhoneRequest extends BaseRequest<GetServicePhoneResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/clientLicence/getServicePhone.htm";
	}

	@Override
	public Class<GetServicePhoneResponse> getResponseClass()
	{
		return GetServicePhoneResponse.class;
	}
}
