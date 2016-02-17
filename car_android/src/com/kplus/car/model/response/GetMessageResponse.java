package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.NoticeContentJson;
import com.kplus.car.parser.ApiField;

public class GetMessageResponse extends Response {
	@ApiField("data")
	private NoticeContentJson data;

	public NoticeContentJson getData() {
		if(data == null)
			data = new NoticeContentJson();
		return data;
	}

	public void setData(NoticeContentJson data) {
		this.data = data;
	}
}
