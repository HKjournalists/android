package com.kplus.car.model.response.request;

import com.kplus.car.Response;
import com.kplus.car.util.StringUtils;

public class VehicleRoportRequest extends BaseRequest<Response> {

	@Override
	public String getApiMethodName() {
		return "/vehicle/report.htm";
	}

	@Override
	public Class<Response> getResponseClass() {
		return Response.class;
	}
	
	public void setParams(String errorKey, String errorMsg, String vehicleNum, String userId){
		if(!StringUtils.isEmpty(errorKey))
			addParams("errorKey", errorKey);
		if(!StringUtils.isEmpty(errorMsg))
			addParams("errorMsg", errorMsg);
		if(!StringUtils.isEmpty(vehicleNum))
			addParams("vehicleNum", vehicleNum);
		if(!StringUtils.isEmpty(userId))
			addParams("userId", userId);
	}

}
