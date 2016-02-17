package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class AdContent extends BaseModelObj {
	@ApiField("content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
