package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.CloseOrderJson;
import com.kplus.car.parser.ApiField;

public class CloseOrderResponse extends Response{
	@ApiField("data")
	private CloseOrderJson data;

	public CloseOrderJson getData() {
		if(data == null)
			data = new CloseOrderJson();
		return data;
	}

	public void setData(CloseOrderJson data) {
		this.data = data;
	}
	
}
