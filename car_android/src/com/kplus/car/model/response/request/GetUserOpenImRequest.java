package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetUserOpenImResponse;

public class GetUserOpenImRequest extends BaseRequest<GetUserOpenImResponse> {

	@Override
	public String getApiMethodName() {
		return "/user/getUserOpenIm.htm";
	}

	@Override
	public Class<GetUserOpenImResponse> getResponseClass() {
		return GetUserOpenImResponse.class;
	}
	
	public void setParams(long uid){
		addParams("uid", uid);
	}

}
