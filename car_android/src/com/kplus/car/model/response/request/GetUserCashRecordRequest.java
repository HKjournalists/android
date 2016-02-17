package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetUserCashRecordResponse;

public class GetUserCashRecordRequest extends BaseRequest<GetUserCashRecordResponse>{

	@Override
	public String getApiMethodName() {
		return "/user/getCashRecords.htm";
	}

	@Override
	public Class<GetUserCashRecordResponse> getResponseClass() {
		return GetUserCashRecordResponse.class;
	}
	
	public void setParams(long uid, int pageIndex, int pageSize){
		addParams("uid", uid).addParams("pageIndex", pageIndex).addParams("pageSize", pageSize);
	}

}
