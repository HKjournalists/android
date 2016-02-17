package com.kplus.car.model.response.request;

import com.kplus.car.Response;

public class GetPenaltyOrdersRequest extends BaseRequest<Response>
{

	@Override
	public String getApiMethodName()
	{
		return "/penalty/getOrders.htm";
	}

	@Override
	public Class<Response> getResponseClass()
	{
		return null;
	}
	
	public void setParam(long pId){
		addParams("pid", pId);
	}
}
