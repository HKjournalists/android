package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class GetHomeActivityJson extends BaseModelObj {

	@ApiField("valid")
	private Boolean valid;
	@ApiField("link")
	private String link;
	@ApiField("img")
	private String img;

	public Boolean getValid() {
		return valid == null ? false : valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
