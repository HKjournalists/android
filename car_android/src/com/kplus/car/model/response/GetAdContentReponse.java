package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.AdContentJSON;
import com.kplus.car.parser.ApiField;

public class GetAdContentReponse extends Response {
	@ApiField("data")
	private AdContentJSON data;

	public AdContentJSON getData() {
		if(data == null)
			data = new AdContentJSON();
		return data;
	}

	public void setData(AdContentJSON data) {
		this.data = data;
	}
}
