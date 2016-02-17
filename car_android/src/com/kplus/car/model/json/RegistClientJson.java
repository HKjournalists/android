package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class RegistClientJson  extends BaseModelObj{

	@ApiField("userId")
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
