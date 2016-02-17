package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.parser.ApiField;

public class GetUserOpenImResponse extends Response {
	@ApiField("data")
	private UserOpenIm data;

	public UserOpenIm getData() {
		if(data == null)
			data = new UserOpenIm();
		return data;
	}

	public void setData(UserOpenIm data) {
		this.data = data;
	}
}
