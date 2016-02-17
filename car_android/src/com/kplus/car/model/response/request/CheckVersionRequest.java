package com.kplus.car.model.response.request;

import com.kplus.car.model.response.CheckVersionResponse;

public class CheckVersionRequest extends BaseRequest<CheckVersionResponse> {

	@Override
	public String getApiMethodName() {
		return "/app/checkVersion.htm";
	}

	@Override
	public Class<CheckVersionResponse> getResponseClass() {
		return CheckVersionResponse.class;
	}

	public void setParams(float version) {
		addParams("version", version);
	}
}
