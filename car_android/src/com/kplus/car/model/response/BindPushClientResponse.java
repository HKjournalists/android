package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.BindPushClient;
import com.kplus.car.parser.ApiField;

public class BindPushClientResponse extends Response{
	@ApiField("data")
	private BindPushClient data;

	public BindPushClient getData() {
		if(data == null)
			data = new BindPushClient();
		return data;
	}

	public void setData(BindPushClient data) {
		this.data = data;
	}
	
}
