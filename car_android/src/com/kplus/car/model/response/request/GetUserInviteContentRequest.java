package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetUserInviteContentResponse;

public class GetUserInviteContentRequest extends BaseRequest<GetUserInviteContentResponse>{

	@Override
	public String getApiMethodName() {
		return "/user/inviteContent.htm";
	}

	@Override
	public Class<GetUserInviteContentResponse> getResponseClass() {
		return GetUserInviteContentResponse.class;
	}
	
	public void setParams(long uid, long type){
		addParams("uid", uid).addParams("type", type);
	}

}
