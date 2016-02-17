package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetVehicleModelListResponse;

public class GetVehicleModelListRequest extends
		BaseRequest<GetVehicleModelListResponse> {

	@Override
	public String getApiMethodName() {
		return "/data/getVehicleModels.htm";
	}

	@Override
	public Class<GetVehicleModelListResponse> getResponseClass() {
		return GetVehicleModelListResponse.class;
	}
}
