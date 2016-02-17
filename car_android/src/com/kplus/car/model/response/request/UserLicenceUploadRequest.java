package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetUserLicenceResponse;

public class UserLicenceUploadRequest extends BaseRequest<GetUserLicenceResponse> {

	@Override
	public String getApiMethodName() {
		return "/clientLicence/uploadLicence.htm";
	}

	@Override
	public Class<GetUserLicenceResponse> getResponseClass() {
		return GetUserLicenceResponse.class;
	}

	public void setParams(long userId, long pId) {
		addParams("userId", userId);
		if(pId != 0)
			addParams("pId", pId);
	}
}
