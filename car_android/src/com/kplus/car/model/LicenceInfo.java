package com.kplus.car.model;

import java.io.Serializable;

import com.kplus.car.parser.ApiField;

public class LicenceInfo extends BaseModelObj implements Serializable {
	@ApiField("status")
	private Integer status;
	@ApiField("vehicleNum")
	private String vehicleNum;
	@ApiField("owner")
	private String owner;
	@ApiField("vehicleType")
	private String vehicleType;
	@ApiField("useProperty")
	private String useProperty;
	@ApiField("brandModel")
	private String brandModel;
	@ApiField("frameNum")
	private String frameNum;
	@ApiField("motorNum")
	private String motorNum;
	@ApiField("registerDate")
	private String registerDate;
	@ApiField("issueDate")
	private String issueDate;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getVehicleNum() {
		return vehicleNum;
	}
	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getUseProperty() {
		return useProperty;
	}
	public void setUseProperty(String useProperty) {
		this.useProperty = useProperty;
	}
	public String getBrandModel() {
		return brandModel;
	}
	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}
	public String getFrameNum() {
		return frameNum;
	}
	public void setFrameNum(String frameNum) {
		this.frameNum = frameNum;
	}
	public String getMotorNum() {
		return motorNum;
	}
	public void setMotorNum(String motorNum) {
		this.motorNum = motorNum;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
}
