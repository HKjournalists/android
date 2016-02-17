package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetRestrictNumResponse;

public class GetRestrictNumRequest extends BaseRequest<GetRestrictNumResponse> {

	@Override
	public String getApiMethodName() {
		return "/data/getRestrict.htm";
	}

	@Override
	public Class<GetRestrictNumResponse> getResponseClass() {
		return GetRestrictNumResponse.class;
	}

	public void setParams(String cityName) {
		addParams("cityName", cityName);
	}
}
