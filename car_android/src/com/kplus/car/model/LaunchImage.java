package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class LaunchImage extends BaseModelObj {

	@ApiField("picUrl")
	private String picUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
