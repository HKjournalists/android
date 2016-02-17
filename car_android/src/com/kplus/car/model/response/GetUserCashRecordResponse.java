package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UserCashRecordJson;
import com.kplus.car.parser.ApiField;

public class GetUserCashRecordResponse extends Response{
	@ApiField("data")
	private UserCashRecordJson data;

	public UserCashRecordJson getData() {
		if(data == null)
			data = new UserCashRecordJson();
		return data;
	}

	public void setData(UserCashRecordJson data) {
		this.data = data;
	}
}
