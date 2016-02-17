package com.kplus.car.comm;

public class TaskInfo {
	private String vehicleNumber;
	private long updateTime;
	private long startTime;
	private int nCount;
	private int verifyCodeErrorCount = 0;
	private boolean hasEnterVerifyCode = false;
	private String sessionId;
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public int getnCount() {
		return nCount;
	}
	public void setnCount(int nCount) {
		this.nCount = nCount;
	}
	public boolean isHasEnterVerifyCode() {
		return hasEnterVerifyCode;
	}
	public void setHasEnterVerifyCode(boolean hasEnterVerifyCode) {
		this.hasEnterVerifyCode = hasEnterVerifyCode;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public int getVerifyCodeErrorCount() {
		return verifyCodeErrorCount;
	}
	public void setVerifyCodeErrorCount(int verifyCodeErrorCount) {
		this.verifyCodeErrorCount = verifyCodeErrorCount;
	}
}
