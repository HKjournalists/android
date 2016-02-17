package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.UserInfo;
import com.kplus.car.parser.ApiField;

public class GetUserInfoJson extends BaseModelObj{
	@ApiField("userInfo")
	private UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
