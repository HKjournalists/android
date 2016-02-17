package com.kplus.car.model.response.request;

import com.kplus.car.model.response.AdjustIssueDateResponse;

public class AdjustIssueDateRequest extends BaseRequest<AdjustIssueDateResponse>{

	@Override
	public String getApiMethodName() {
		return "/vehicle/adjustIssueDate.htm";
	}

	@Override
	public Class<AdjustIssueDateResponse> getResponseClass() {
		return AdjustIssueDateResponse.class;
	}
	
	public void setParams(long userId, long uid, String vehicleNum, String adjustDate){
		addParams("userId", userId).addParams("uid", uid).addParams("vehicleNum", vehicleNum).addParams("adjustDate", adjustDate);
	}

}
