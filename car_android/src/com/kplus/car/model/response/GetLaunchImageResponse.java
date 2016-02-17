package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.LaunchImage;
import com.kplus.car.parser.ApiField;

public class GetLaunchImageResponse extends Response {

	@ApiField("data")
	private LaunchImage data;

	public LaunchImage getData() {
		if (data == null)
			data = new LaunchImage();
		return data;
	}

	public void setData(LaunchImage data) {
		this.data = data;
	}

}
