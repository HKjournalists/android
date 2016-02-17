package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class ServicePhone extends BaseModelObj
{
	@ApiField("phoneNum")
	private String phoneNum;

	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}
	
}
