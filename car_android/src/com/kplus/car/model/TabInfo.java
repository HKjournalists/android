package com.kplus.car.model;

public class TabInfo
{
	private String identity;
	private String name;
	private String description;
	private int orderId;
	private int isValid;
	
	public String getIdentity()
	{
		return identity;
	}
	public void setIdentity(String identity)
	{
		this.identity = identity;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getOrderId()
	{
		return orderId;
	}
	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}
	public int getIsValid()
	{
		return isValid;
	}
	public void setIsValid(int isValid)
	{
		this.isValid = isValid;
	}
}
