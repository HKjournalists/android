package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class UserLoginJson extends BaseModelObj{
	@ApiField("result")
	private Long result;
	@ApiField("isNewUser")
	private Boolean isNewUser;

	public Long getResult() {
		return result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public Boolean getIsNewUser() {
		return isNewUser;
	}

	public void setIsNewUser(Boolean isNewUser) {
		this.isNewUser = isNewUser;
	}
}
