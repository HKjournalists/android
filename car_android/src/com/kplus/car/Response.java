package com.kplus.car;

import java.io.Serializable;
import java.util.Map;

import com.kplus.car.model.CityVehicle;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.Converters;
import com.kplus.car.parser.MapReader;
import com.kplus.car.parser.ModelObj;

public class Response implements Serializable, ModelObj {

	@ApiField("code")
	private Integer code;
	@ApiField("msg")
	private String msg;
	@ApiField("postDateTime")
	private Long postDateTime;

	private String body;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getPostDateTime() {
		return postDateTime;
	}

	public void setPostDateTime(Long postDateTime) {
		this.postDateTime = postDateTime;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void init(Map<?, ?> root, Class<ModelObj> clazz) {
		Converters.convert(clazz, new MapReader(root), this);
	}

}
