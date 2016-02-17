package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class UserOpenIm extends BaseModelObj {
	@ApiField("openUserid")
	private String openUserid;
	@ApiField("openPassword")
	private String openPassword;
	public String getOpenUserid() {
		return openUserid;
	}
	public void setOpenUserid(String openUserid) {
		this.openUserid = openUserid;
	}
	public String getOpenPassword() {
		return openPassword;
	}
	public void setOpenPassword(String openPassword) {
		this.openPassword = openPassword;
	}
}
