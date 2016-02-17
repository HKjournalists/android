package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.MessageCountJson;
import com.kplus.car.parser.ApiField;

public class GetMessageCountResponse extends Response {
	@ApiField("data")
	private MessageCountJson data;

	public MessageCountJson getData() {
		if(data == null)
			data = new MessageCountJson();
		return data;
	}

	public void setData(MessageCountJson data) {
		this.data = data;
	}
}
