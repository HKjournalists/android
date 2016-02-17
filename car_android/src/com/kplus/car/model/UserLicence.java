package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class UserLicence extends BaseModelObj {
	@ApiField("id")
	private Long id;
	@ApiField("status")
	private Integer status;
	@ApiField("drivingLicenceUrl")
	private String drivingLicenceUrl;
	@ApiField("vehicleNum")
	private String vehicleNum;
	@ApiField("owner")
	private String owner;
	@ApiField("brandModel")
	private String brandModel;
	@ApiField("motorNum")
	private String motorNum;
	@ApiField("frameNum")
	private String frameNum;
	@ApiField("issueDate")
	private String issueDate;
	@ApiField("descr")
	private String descr;
	@ApiField("adjustDate")
	private String adjustDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status == null ? 0 : status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDrivingLicenceUrl() {
		return drivingLicenceUrl;
	}

	public void setDrivingLicenceUrl(String drivingLicenceUrl) {
		this.drivingLicenceUrl = drivingLicenceUrl;
	}

	public String getVehicleNum()
	{
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum)
	{
		this.vehicleNum = vehicleNum;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getBrandModel()
	{
		return brandModel;
	}

	public void setBrandModel(String brandModel)
	{
		this.brandModel = brandModel;
	}

	public String getMotorNum()
	{
		return motorNum;
	}

	public void setMotorNum(String motorNum)
	{
		this.motorNum = motorNum;
	}

	public String getFrameNum()
	{
		return frameNum;
	}

	public void setFrameNum(String frameNum)
	{
		this.frameNum = frameNum;
	}

	public String getIssueDate()
	{
		return issueDate;
	}

	public void setIssueDate(String issueDate)
	{
		this.issueDate = issueDate;
	}

	public String getDescr()
	{
		return descr;
	}

	public void setDescr(String descr)
	{
		this.descr = descr;
	}

	public String getAdjustDate()
	{
		return adjustDate;
	}

	public void setAdjustDate(String adjustDate)
	{
		this.adjustDate = adjustDate;
	}

}
