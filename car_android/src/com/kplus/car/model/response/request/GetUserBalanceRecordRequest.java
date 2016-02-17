package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetUserBalanceRecordResponse;

public class GetUserBalanceRecordRequest extends BaseRequest<GetUserBalanceRecordResponse>{

	@Override
	public String getApiMethodName() {
		return "/activity/getBalanceRecords.htm";
	}

	@Override
	public Class<GetUserBalanceRecordResponse> getResponseClass() {
		return GetUserBalanceRecordResponse.class;
	}
	
	public void setParams(long pId, int pageIndex, int pageSize){
		addParams("pId", pId).addParams("pageIndex", pageIndex).addParams("pageSize", pageSize);
	}

}
