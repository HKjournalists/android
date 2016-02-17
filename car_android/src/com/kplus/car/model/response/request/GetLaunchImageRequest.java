package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetLaunchImageResponse;

public class GetLaunchImageRequest extends BaseRequest<GetLaunchImageResponse> {

	@Override
	public String getApiMethodName() {
		return "/app/getLaunchImage.htm";
	}

	@Override
	public Class<GetLaunchImageResponse> getResponseClass() {
		return GetLaunchImageResponse.class;
	}

	public void setParams(long lastUpdateTime) {
		addParams("lastUpdateTime", lastUpdateTime);
	}
}
