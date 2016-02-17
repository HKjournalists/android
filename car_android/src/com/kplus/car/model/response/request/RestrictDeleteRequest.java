package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

public class RestrictDeleteRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/restrict/delete.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(String id, long uid) {
		addParams("id", id);
		if(uid != 0)
			addParams("uid", uid);
	}
}
