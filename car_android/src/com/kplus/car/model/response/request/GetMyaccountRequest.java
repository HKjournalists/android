package com.kplus.car.model.response.request;

import com.kplus.car.Response;

public class GetMyaccountRequest extends BaseRequest<Response> {

	@Override
	public String getApiMethodName() {
		return "/activity/myaccount.htm";
	}

	public void setParams(long pId) {
		addParams("pId", pId);
	}

	@Override
	public Class<Response> getResponseClass() {
		return null;
	}

}
