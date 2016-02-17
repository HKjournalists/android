package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.ResiDueDegree;
import com.kplus.car.parser.ApiField;

public class GetResidueDegreeResponse extends Response
{
	@ApiField("data")
	private ResiDueDegree data;

	public ResiDueDegree getData()
	{
		if(data == null)
			data = new ResiDueDegree();
		return data;
	}

	public void setData(ResiDueDegree data)
	{
		this.data = data;
	}
}
