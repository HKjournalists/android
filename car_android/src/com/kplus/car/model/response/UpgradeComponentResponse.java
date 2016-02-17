package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UpgradeComponentJson;
import com.kplus.car.parser.ApiField;

public class UpgradeComponentResponse extends Response{
	@ApiField("data")
	private UpgradeComponentJson data;

	public UpgradeComponentJson getData() {
		if(data == null)
			data = new UpgradeComponentJson();
		return data;
	}

	public void setData(UpgradeComponentJson data) {
		this.data = data;
	}
	
}
