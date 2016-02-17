package com.kplus.car.model.response.request;

import com.kplus.car.Response;

public class GetPenaltyDetailsRequest extends BaseRequest<Response>
{

	@Override
	public String getApiMethodName()
	{
		return "/penalty/getDetails.htm";
	}

	@Override
	public Class<Response> getResponseClass()
	{
		return null;
	}
	
	public void setParam(long orderId){
		addParams("orderId", orderId);
	}
}
