package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetAgencyServiceListResponse;

public class GetAgencyServiceListRequest extends
		BaseRequest<GetAgencyServiceListResponse> {

	@Override
	public String getApiMethodName() {
		return "/data/getAgencyServices.htm";
	}

	@Override
	public Class<GetAgencyServiceListResponse> getResponseClass() {
		return GetAgencyServiceListResponse.class;
	}

	public void setParams(String cityName) {
		addParams("cityName", cityName);
	}
}
