package com.kplus.car.model.response.request;

import com.kplus.car.model.response.UserConfirmShareResponse;

public class UserConfirmShareRequest extends BaseRequest<UserConfirmShareResponse>{

	@Override
	public String getApiMethodName() {
		return "/user/confirmShare.htm";
	}

	@Override
	public Class<UserConfirmShareResponse> getResponseClass() {
		return UserConfirmShareResponse.class;
	}
	
	public void setParams(long uid, int type){
		addParams("uid", uid).addParams("type", type);
	}

}
