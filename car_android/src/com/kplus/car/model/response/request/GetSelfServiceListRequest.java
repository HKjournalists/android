package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetSelfServiceListResponse;

public class GetSelfServiceListRequest extends
		BaseRequest<GetSelfServiceListResponse> {

	@Override
	public String getApiMethodName() {
		return "/data/getSelfServices.htm";
	}

	@Override
	public Class<GetSelfServiceListResponse> getResponseClass() {
		return GetSelfServiceListResponse.class;
	}

	public void setParams(String cityName, String regionName) {
		addParams("cityName", cityName).addParams("regionName", regionName);
	}
}
