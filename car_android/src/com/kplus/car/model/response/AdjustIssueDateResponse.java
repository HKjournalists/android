package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.AdjustIssueDateJson;
import com.kplus.car.parser.ApiField;

public class AdjustIssueDateResponse extends Response{
	@ApiField("data")
	private AdjustIssueDateJson data;

	public AdjustIssueDateJson getData() {
		if(data == null)
			data = new AdjustIssueDateJson();
		return data;
	}

	public void setData(AdjustIssueDateJson data) {
		this.data = data;
	}
}
