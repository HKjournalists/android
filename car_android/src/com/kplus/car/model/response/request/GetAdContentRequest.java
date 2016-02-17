package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetAdContentReponse;

public class GetAdContentRequest extends BaseRequest<GetAdContentReponse> {

	@Override
	public String getApiMethodName() {
		return "/activity/getAdContent.htm";
	}

	@Override
	public Class<GetAdContentReponse> getResponseClass() {
		return GetAdContentReponse.class;
	}

}
