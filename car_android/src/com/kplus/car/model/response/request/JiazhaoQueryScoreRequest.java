package com.kplus.car.model.response.request;

import com.kplus.car.model.response.JiazhaoQueryScoreResponse;

public class JiazhaoQueryScoreRequest extends BaseRequest<JiazhaoQueryScoreResponse> {

	@Override
	public String getApiMethodName() {
		return "/jiazhao/queryScore.htm";
	}

	@Override
	public Class<JiazhaoQueryScoreResponse> getResponseClass() {
		return JiazhaoQueryScoreResponse.class;
	}

	public void setParams(String id, String clientType, long uid) {
		addParams("id", id);
		if(uid != 0)
			addParams("uid", uid);
		addParams("clientType", clientType);
	}
}
