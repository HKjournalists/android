package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.JiazhaoQueryScoreJson;
import com.kplus.car.parser.ApiField;

public class JiazhaoQueryScoreResponse extends Response {

	@ApiField("data")
	private JiazhaoQueryScoreJson data;

	public JiazhaoQueryScoreJson getData() {
		return data;
	}

	public void setData(JiazhaoQueryScoreJson data) {
		this.data = data;
	}

}
