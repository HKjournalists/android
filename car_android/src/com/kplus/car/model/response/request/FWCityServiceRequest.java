package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWCityServiceResponse;

public class FWCityServiceRequest extends BaseRequest<FWCityServiceResponse> {

	@Override
	public String getApiMethodName() {
		return "/fw/getCityService.htm";
	}

	@Override
	public Class<FWCityServiceResponse> getResponseClass() {
		return FWCityServiceResponse.class;
	}
	
	public void setParams(String cityId){
		addParams("cityId", cityId);
	}

}
