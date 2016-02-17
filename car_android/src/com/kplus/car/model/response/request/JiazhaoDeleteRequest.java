package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

public class JiazhaoDeleteRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/jiazhao/delete.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(String id, String clientType, long uid) {
		addParams("id", id);
		if(uid != 0)
			addParams("uid", uid);
		addParams("clientType", clientType);
	}
}
