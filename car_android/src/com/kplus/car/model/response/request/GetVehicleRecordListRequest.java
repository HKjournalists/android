package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetVehicleRecordListResponse;

public class GetVehicleRecordListRequest extends BaseRequest<GetVehicleRecordListResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/against/getMultiRecords.htm";
	}

	@Override
	public Class<GetVehicleRecordListResponse> getResponseClass()
	{
		return GetVehicleRecordListResponse.class;
	}
	
	public void setParams(long userId, String vehicleNums) {
		addParams("userId", userId).addParams("vehicleNums", vehicleNums);
	}
}
