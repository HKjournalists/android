package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class GetLongValueJson extends BaseModelObj {

	@ApiField("id")
	private Long id;

	public Long getId() {
		return id == null ? 0 : id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
