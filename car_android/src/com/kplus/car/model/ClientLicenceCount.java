package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

public class ClientLicenceCount extends BaseModelObj
{
	@ApiField("count")
	private Long count;

	public Long getCount()
	{
		return count;
	}

	public void setCount(Long count)
	{
		this.count = count;
	}
}
