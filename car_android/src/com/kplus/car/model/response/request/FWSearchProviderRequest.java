package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWSearchProviderResponse;

public class FWSearchProviderRequest extends BaseRequest<FWSearchProviderResponse> {

	@Override
	public String getApiMethodName() {
		return "/fw/getProvider.htm";
	}

	@Override
	public Class<FWSearchProviderResponse> getResponseClass() {
		return FWSearchProviderResponse.class;
	}
	
	public void setParams(String telPhone){
		addParams("telPhone", telPhone);
	}

}
