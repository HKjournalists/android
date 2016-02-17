package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UseServiceRecordJson;
import com.kplus.car.parser.ApiField;

public class GetUseServiceRecordResponse extends Response
{
	@ApiField("data")
	private UseServiceRecordJson data;

	public UseServiceRecordJson getData()
	{
		if(data == null)
			data = new UseServiceRecordJson();
		return data;
	}

	public void setData(UseServiceRecordJson data)
	{
		this.data = data;
	}
}
