package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetLongValueResponse;

public class GetActivityInviteCodeRequest extends
		BaseRequest<GetLongValueResponse> {

	@Override
	public String getApiMethodName() {
		return "/activity/getInviteCode.htm";
	}

	public void setParams(long pId) {
		addParams("pId", pId);
	}

	@Override
	public Class<GetLongValueResponse> getResponseClass() {
		return GetLongValueResponse.class;
	}

}
