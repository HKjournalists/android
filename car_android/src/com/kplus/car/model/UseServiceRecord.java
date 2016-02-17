package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class UseServiceRecord extends BaseModelObj
{
	@ApiField("vehicleNum")
	private String vehicleNum;
	@ApiField("serviceType")
	private String serviceType;
	@ApiField("useTime")
	private String useTime;
	@ApiField("serviceFee")
	private String serviceFee;
	@ApiField("serviceResult")
	private String serviceResult;
	public String getVehicleNum()
	{
		return vehicleNum;
	}
	public void setVehicleNum(String vehicleNum)
	{
		this.vehicleNum = vehicleNum;
	}
	public String getServiceType()
	{
		return serviceType;
	}
	public void setServiceType(String serviceType)
	{
		this.serviceType = serviceType;
	}
	public String getUseTime()
	{
		return useTime;
	}
	public void setUseTime(String useTime)
	{
		this.useTime = useTime;
	}
	public String getServiceFee()
	{
		return serviceFee;
	}
	public void setServiceFee(String serviceFee)
	{
		this.serviceFee = serviceFee;
	}
	public String getServiceResult()
	{
		return serviceResult;
	}
	public void setServiceResult(String serviceResult)
	{
		this.serviceResult = serviceResult;
	}
}
