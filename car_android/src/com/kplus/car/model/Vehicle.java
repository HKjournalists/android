package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class Vehicle extends BaseModelObj
{
	@ApiField("vehicleNum")
	private String vehicleNum;
	@ApiField("cityId")
	private String cityId;
	@ApiField("motorNum")
	private String motorNum;
	@ApiField("frameNum")
	private String frameNum;
	@ApiField("vehicleModelId")
	private Long vehicleModelId;
	@ApiField("desc")
	private String desc;
	@ApiField("cityName")
	private String cityName;
	@ApiField("vehicleAuth")
	private VehicleAuth vehicleAuth;
	@ApiField("account")
	private String account;
	@ApiField("password")
	private String password;
	@ApiField("vehicleType")
	private Integer vehicleType;
	
	public String getVehicleNum()
	{
		return vehicleNum;
	}
	public void setVehicleNum(String vehicleNum)
	{
		this.vehicleNum = vehicleNum;
	}
	public String getCityId()
	{
		return cityId;
	}
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
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
	public Long getVehicleModelId()
	{
		return vehicleModelId;
	}
	public void setVehicleModelId(Long vehicleModelId)
	{
		this.vehicleModelId = vehicleModelId;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	public String getCityName()
	{
		return cityName;
	}
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	public VehicleAuth getVehicleAuth() {
		return vehicleAuth;
	}
	public void setVehicleAuth(VehicleAuth vehicleAuth) {
		this.vehicleAuth = vehicleAuth;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(Integer vehicleType) {
		this.vehicleType = vehicleType;
	}
}
