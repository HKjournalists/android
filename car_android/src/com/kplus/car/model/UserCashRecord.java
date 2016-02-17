package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class UserCashRecord extends BaseModelObj{//户现金余额使用记录
	@ApiField("time")
	private String time;//发生时间，格式yyyy-MM-dd HH:mm
	@ApiField("change")
	private Integer change;//余额变化值，带正负符号
	@ApiField("content")
	private String content;//描述说明
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getChange() {
		return change;
	}
	public void setChange(Integer change) {
		this.change = change;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
