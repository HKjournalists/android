package com.kplus.car.model.response.request;

import com.kplus.car.model.response.AppUpgradeResponse;

public class AppUpdateRequest extends BaseRequest<AppUpgradeResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/app/upgrade.htm";
	}

	@Override
	public Class<AppUpgradeResponse> getResponseClass()
	{
		return AppUpgradeResponse.class;
	}
	
	public void setParams(long userId){
		addParams("userId", userId);
	}
}
