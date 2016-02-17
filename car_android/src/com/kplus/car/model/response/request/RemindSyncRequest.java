package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

public class RemindSyncRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/remind/sync.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(String data, int type, String clientType, long uid) {
		addParams("data", data);
		if(uid != 0)
			addParams("uid", uid);
		addParams("type", type);
		addParams("clientType", clientType);
	}
}
