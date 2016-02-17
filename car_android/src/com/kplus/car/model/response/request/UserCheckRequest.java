package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BooleanResultResponse;

public class UserCheckRequest extends BaseRequest<BooleanResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/user/check.htm";
	}

	@Override
	public Class<BooleanResultResponse> getResponseClass() {
		return BooleanResultResponse.class;
	}
	
	public void setParams(String userName, int type){
		addParams("userName", userName);
		addParams("type", type);
	}

}
