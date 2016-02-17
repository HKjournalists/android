package com.chengniu.client.common;

import java.io.Serializable;

public class Operator implements Serializable {
	private static final long serialVersionUID = 3643109930960175753L;
	private String userId;

	private Integer userType;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCurrentIp() {
		return currentIp;
	}

	public void setCurrentIp(String currentIp) {
		this.currentIp = currentIp;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * 手机
	 */
	private String phone;
	/**
	 * 当前ip
	 */
	private String currentIp;
	/**
	 * 设备id
	 */
	private String clientId;
	/**
	 * 设备类型
	 */
	private String clientType;
	/**
	 * app版本
	 */
	private String appVersion;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
}