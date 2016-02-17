package com.kplus.car.model.response.request;

import com.kplus.car.Response;

public class GetTabRequest extends BaseRequest<Response>
{

	@Override
	public String getApiMethodName()
	{
		return "/clientTab/getTab.htm";
	}

	@Override
	public Class<Response> getResponseClass()
	{
		return Response.class;
	}
	
	public void setParams(String versionIdentity)
	{
		addParams("versionIdentity", versionIdentity);
	}
}
