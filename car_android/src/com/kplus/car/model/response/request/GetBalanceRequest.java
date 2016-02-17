package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetBalanceResponse;

public class GetBalanceRequest extends BaseRequest<GetBalanceResponse> {

	@Override
	public String getApiMethodName() {
		return "/activity/getBalance.htm";
	}

	@Override
	public Class<GetBalanceResponse> getResponseClass() {
		return GetBalanceResponse.class;
	}

	public void setParams(long pId) {
		addParams("pId", pId);
	}
}
