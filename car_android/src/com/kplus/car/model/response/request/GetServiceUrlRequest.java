package com.kplus.car.model.response.request;

import com.kplus.car.KplusConstants;
import com.kplus.car.Response;

public class GetServiceUrlRequest extends BaseRequest<Response>
{

	@Override
	public String getApiMethodName()
	{
		return null;
	}

	@Override
	public Class<Response> getResponseClass()
	{
		return Response.class;
	}
	
	public void setParams(long uid, long userId, String serviceId, String cityId){
		addParams("uid", uid);
		addParams("userId", userId);
		addParams("serviceId", serviceId);
		addParams("cityId", cityId);
	}

}
