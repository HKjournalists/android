package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetCityRegionListResponse;

public class GetCityRegionListRequest extends
		BaseRequest<GetCityRegionListResponse> {

	@Override
	public String getApiMethodName() {
		return "/data/getCityRegions.htm";
	}

	@Override
	public Class<GetCityRegionListResponse> getResponseClass() {
		return GetCityRegionListResponse.class;
	}

	public void setParams(long cityId) {
		addParams("cityId", cityId);
	}
}
