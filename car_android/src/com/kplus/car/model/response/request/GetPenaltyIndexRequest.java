package com.kplus.car.model.response.request;

import com.kplus.car.Response;

public class GetPenaltyIndexRequest extends BaseRequest<Response>
{

	@Override
	public String getApiMethodName()
	{
		return "/penalty/index.htm";
	}

	@Override
	public Class<Response> getResponseClass()
	{
		return null;
	}
}
