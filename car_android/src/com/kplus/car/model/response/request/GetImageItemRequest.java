package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetImgItemResponse;

public class GetImageItemRequest extends BaseRequest<GetImgItemResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/activity/getImgItems.htm";
	}

	@Override
	public Class<GetImgItemResponse> getResponseClass()
	{
		return GetImgItemResponse.class;
	}
	
	public void setParams(String clientIdentity, long uid, long userId){
		addParams("clientIdentity", clientIdentity);
		if(uid != 0)
			addParams("uid", uid);
		if(userId != 0)
			addParams("userId", userId);
	}
}
