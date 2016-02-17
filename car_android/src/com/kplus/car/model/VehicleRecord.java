package com.kplus.car.model;

import java.util.List;

import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

public class VehicleRecord extends BaseModelObj
{
	@ApiField("total")
	private int total;
	@ApiField("vehicleNum")
	private String vehicleNum;
	@ApiListField("list")
	private List<AgainstRecord> list;
	
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	public String getVehicleNum()
	{
		return vehicleNum;
	}
	public void setVehicleNum(String vehicleNum)
	{
		this.vehicleNum = vehicleNum;
	}
	public List<AgainstRecord> getList()
	{
		return list;
	}
	public void setList(List<AgainstRecord> list)
	{
		this.list = list;
	}
}
