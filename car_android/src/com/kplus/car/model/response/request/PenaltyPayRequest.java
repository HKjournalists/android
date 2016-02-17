package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetStringValueResponse;

public class PenaltyPayRequest extends BaseRequest<GetStringValueResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/penalty/pay.htm";
	}

	@Override
	public Class<GetStringValueResponse> getResponseClass()
	{
		return GetStringValueResponse.class;
	}
	
	public void setParams(long orderId, int payType){
		addParams("orderId", orderId);
		addParams("payType", payType);
	}
}
