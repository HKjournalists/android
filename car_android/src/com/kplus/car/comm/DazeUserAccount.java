package com.kplus.car.comm;

public class DazeUserAccount {
	private Long uid;
	private String nickName;
	private String phoneNumber;
	private String phoneLoginName;
	private String qqLoginName;
	private String wechatLoginName;
	private String weiboLoginName;
	private String creatDatetime;
	private String qqCreatDatetime;
	private String wechatCreatDatetime;
	private String weiboCreatDatetime;
	private Integer status;//0 登出 1登入
	
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneLoginName() {
		return phoneLoginName;
	}
	public void setPhoneLoginName(String phoneLoginName) {
		this.phoneLoginName = phoneLoginName;
	}
	public String getQqLoginName() {
		return qqLoginName;
	}
	public void setQqLoginName(String qqLoginName) {
		this.qqLoginName = qqLoginName;
	}
	public String getWechatLoginName() {
		return wechatLoginName;
	}
	public void setWechatLoginName(String wechatLoginName) {
		this.wechatLoginName = wechatLoginName;
	}
	public String getWeiboLoginName() {
		return weiboLoginName;
	}
	public void setWeiboLoginName(String weiboLoginName) {
		this.weiboLoginName = weiboLoginName;
	}
	public String getCreatDatetime() {
		return creatDatetime;
	}
	public void setCreatDatetime(String creatDatetime) {
		this.creatDatetime = creatDatetime;
	}
	public String getQqCreatDatetime() {
		return qqCreatDatetime;
	}
	public void setQqCreatDatetime(String qqCreatDatetime) {
		this.qqCreatDatetime = qqCreatDatetime;
	}
	public String getWechatCreatDatetime() {
		return wechatCreatDatetime;
	}
	public void setWechatCreatDatetime(String wechatCreatDatetime) {
		this.wechatCreatDatetime = wechatCreatDatetime;
	}
	public String getWeiboCreatDatetime() {
		return weiboCreatDatetime;
	}
	public void setWeiboCreatDatetime(String weiboCreatDatetime) {
		this.weiboCreatDatetime = weiboCreatDatetime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
