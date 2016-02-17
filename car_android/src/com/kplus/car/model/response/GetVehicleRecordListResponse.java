package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetVehicleRecordListJson;
import com.kplus.car.parser.ApiField;

public class GetVehicleRecordListResponse extends Response
{ 
	@ApiField("data")
	private GetVehicleRecordListJson data;
	
	public GetVehicleRecordListJson getData(){
		if(data == null){
			data = new GetVehicleRecordListJson();
		}
		return data;
	}
	
	public void setData(GetVehicleRecordListJson data){
		this.data = data;
	}
}
