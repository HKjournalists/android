package com.kplus.car.model.response.request;

import com.kplus.car.model.response.UnBindThirdPartResponse;

public class UnBindThirdPartRequest extends BaseRequest<UnBindThirdPartResponse> {

	@Override
	public String getApiMethodName() {
		return "/user/unBind.htm";
	}

	@Override
	public Class<UnBindThirdPartResponse> getResponseClass() {
		return UnBindThirdPartResponse.class;
	}
	
	public void setParams(long uid, String userName, int type){
		addParams("uid", uid).addParams("userName", userName).addParams("type", type);
	}

}
