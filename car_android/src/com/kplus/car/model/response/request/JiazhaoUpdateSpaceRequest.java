package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

public class JiazhaoUpdateSpaceRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/jiazhao/updateSpace.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(String id, boolean showIndex, String clientType, long uid) {
		addParams("id", id);
		addParams("showIndex", showIndex);
		if(uid != 0)
			addParams("uid", uid);
		addParams("clientType", clientType);
	}
}
