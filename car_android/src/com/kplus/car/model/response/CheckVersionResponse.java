package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.AppVersion;
import com.kplus.car.parser.ApiField;

public class CheckVersionResponse extends Response {

	@ApiField("data")
	private AppVersion data;

	public AppVersion getData() {
		if (data == null)
			data = new AppVersion();
		return data;
	}

	public void setData(AppVersion data) {
		this.data = data;
	}

}
