package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.UserLicence;
import com.kplus.car.parser.ApiField;

public class GetUserLicenceResponse extends Response {

	@ApiField("data")
	private UserLicence data;

	public UserLicence getData() {
		if (data == null)
			data = new UserLicence();
		return data;
	}

	public void setData(UserLicence data) {
		this.data = data;
	}

}
