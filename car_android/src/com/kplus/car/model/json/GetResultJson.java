package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class GetResultJson extends BaseModelObj {

	@ApiField("result")
	private Boolean result;
	@ApiField("url")
	private String url;

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
