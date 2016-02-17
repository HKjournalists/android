package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetUserLicenceResponse;

public class GetUserLicenceRequest extends BaseRequest<GetUserLicenceResponse> {

	@Override
	public String getApiMethodName() {
		return "/clientLicence/getLicenceStatus.htm";
	}

	@Override
	public Class<GetUserLicenceResponse> getResponseClass() {
		return GetUserLicenceResponse.class;
	}

	public void setParams(long userId) {
		addParams("userId", userId);
	}
}
