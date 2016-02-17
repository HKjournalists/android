package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UserBalanceRecordJson;
import com.kplus.car.parser.ApiField;

public class GetUserBalanceRecordResponse extends Response{
	@ApiField("data")
	private UserBalanceRecordJson data;

	public UserBalanceRecordJson getData() {
		if(data == null)
			data = new UserBalanceRecordJson();
		return data;
	}

	public void setData(UserBalanceRecordJson data) {
		this.data = data;
	}
}
