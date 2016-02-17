package com.chengniu.client.pojo;

import java.io.Serializable;

public class BaoxianBaseInfoMediaKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8450155874107284712L;

	private String id;

	private String code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}
}