package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class GetRestrictNumJson extends BaseModelObj {

	@ApiField("num")
	private String num;
	@ApiField("restrictNums")
	private String restrictNums;
	@ApiField("message")
	private String message;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getRestrictNums() {
		return restrictNums;
	}

	public void setRestrictNums(String restrictNums) {
		this.restrictNums = restrictNums;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
