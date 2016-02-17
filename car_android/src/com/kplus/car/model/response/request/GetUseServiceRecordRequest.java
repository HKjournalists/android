package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetUseServiceRecordResponse;

public class GetUseServiceRecordRequest extends BaseRequest<GetUseServiceRecordResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/clientLicence/getUseServiceRecord.htm";
	}

	@Override
	public Class<GetUseServiceRecordResponse> getResponseClass()
	{
		return GetUseServiceRecordResponse.class;
	}
	
	public void setParams(long id){
		addParams("id",id);
	}
}
