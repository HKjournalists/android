package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetInitVehicleListResponse;

public class GetInitVehicleListRequest extends
		BaseRequest<GetInitVehicleListResponse> {

	@Override
	public String getApiMethodName() {
		return "/data/initVehicles.htm";
	}

	@Override
	public Class<GetInitVehicleListResponse> getResponseClass() {
		return GetInitVehicleListResponse.class;
	}

	public void setParams(long userId, String list) {
		addParams("userId", userId).addParams("list", list);
	}
}
