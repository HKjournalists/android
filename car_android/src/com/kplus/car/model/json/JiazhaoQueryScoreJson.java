package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

public class JiazhaoQueryScoreJson extends BaseModelObj {

	@ApiField("ljjf")
	private Integer ljjf;
	@ApiField("id")
	private String id;
	@ApiField("jszh")
	private String jszh;
	@ApiField("xm")
	private String xm;
	@ApiField("zjcx")
	private String zjcx;

	public Integer getLjjf() {
		return ljjf;
	}

	public void setLjjf(Integer ljjf) {
		this.ljjf = ljjf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJszh() {
		return jszh;
	}

	public void setJszh(String jszh) {
		this.jszh = jszh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getZjcx() {
		return zjcx;
	}

	public void setZjcx(String zjcx) {
		this.zjcx = zjcx;
	}
}
