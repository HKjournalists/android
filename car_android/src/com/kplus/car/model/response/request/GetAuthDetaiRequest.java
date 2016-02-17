package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetAuthDetaiResponse;

public class GetAuthDetaiRequest extends BaseRequest<GetAuthDetaiResponse>{

	@Override
	public String getApiMethodName() {
		return "/vehicle/getAuthDetail.htm";
	}

	@Override
	public Class<GetAuthDetaiResponse> getResponseClass() {
		return GetAuthDetaiResponse.class;
	}
	
	public void setParams(long userId, long uid, String vehicleNums){
		addParams("userId", userId).addParams("uid", uid).addParams("vehicleNums", vehicleNums);
	}

}
