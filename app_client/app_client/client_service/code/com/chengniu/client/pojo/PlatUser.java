package com.chengniu.client.pojo;

import java.io.Serializable;
import java.util.Date;

public class PlatUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5064124982909164234L;

	private Long id;

	private String userName;

	private String viewName;

	private String appKeyPrx;

	private String appKey;

	private String appSecret;

	private Integer clientType;

	private Integer status;

	private Date createDatetime;

	private Date updateDatetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName == null ? null : viewName.trim();
	}

	public String getAppKeyPrx() {
		return appKeyPrx;
	}

	public void setAppKeyPrx(String appKeyPrx) {
		this.appKeyPrx = appKeyPrx == null ? null : appKeyPrx.trim();
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey == null ? null : appKey.trim();
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret == null ? null : appSecret.trim();
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
}