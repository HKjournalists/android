package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.JiazhaoSaveJson;
import com.kplus.car.parser.ApiField;

public class JiazhaoSaveResponse extends Response {

	@ApiField("data")
	private JiazhaoSaveJson data;

	public JiazhaoSaveJson getData() {
		return data;
	}

	public void setData(JiazhaoSaveJson data) {
		this.data = data;
	}

}
