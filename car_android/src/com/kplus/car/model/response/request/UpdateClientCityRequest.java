package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

public class UpdateClientCityRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/app/updateClientCity.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(long userId, long uid, long cityId) {
		addParams("userId", userId);
		if (uid != 0)
			addParams("uid", uid);
		addParams("cityId", cityId);
	}
}
