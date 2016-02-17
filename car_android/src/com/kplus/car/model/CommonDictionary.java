package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class CommonDictionary extends BaseModelObj {
	@ApiField("id")
	private Long id;
	@ApiField("type")
	private String type;
	@ApiField("value")
	private String value;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
