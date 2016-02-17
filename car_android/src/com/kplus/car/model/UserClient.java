package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class UserClient extends BaseModelObj {

	@ApiField
	private long userId;
	@ApiField
	private String clientId;

	private int clientType;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

}
