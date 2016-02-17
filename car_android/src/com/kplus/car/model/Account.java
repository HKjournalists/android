package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class Account extends BaseModelObj {
	@ApiField("userName")
	private String userName;//用户名
	@ApiField("nickname")
	private String nickname;//用户名
	@ApiField("type")
	private Integer type;//关联帐号类型，0=手机号;1=QQ;2=微信;3=微博
	@ApiField("pid")
	private Long pid;//手机用户ID,type=0时出现
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
