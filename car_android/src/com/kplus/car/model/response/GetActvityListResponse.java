package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.ActivityInfoJson;
import com.kplus.car.parser.ApiField;

public class GetActvityListResponse extends Response {
	@ApiField("data")
	private ActivityInfoJson data;

	public ActivityInfoJson getData() {
		if(data == null)
			data = new ActivityInfoJson();
		return data;
	}

	public void setData(ActivityInfoJson data) {
		this.data = data;
	}
}
