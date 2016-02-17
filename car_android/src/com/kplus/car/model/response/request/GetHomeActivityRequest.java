package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetHomeActivityResponse;

public class GetHomeActivityRequest extends
		BaseRequest<GetHomeActivityResponse> {

	@Override
	public String getApiMethodName() {
		return "/activity/getHomeActivity.htm";
	}

	@Override
	public Class<GetHomeActivityResponse> getResponseClass() {
		return GetHomeActivityResponse.class;
	}

}
