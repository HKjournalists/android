package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

public class VehicleDelRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/vehicle/del.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(long userId, long uid, String vehicleNum) {
		addParams("userId", userId);
		if(uid != 0)
			addParams("uid", uid);
		addParams("vehicleNum", vehicleNum);
	}
}
