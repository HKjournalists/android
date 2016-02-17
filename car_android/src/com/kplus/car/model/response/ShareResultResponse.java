package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.ShareResult;
import com.kplus.car.parser.ApiField;

public class ShareResultResponse extends Response
{
	@ApiField("data")
	private ShareResult data;

	public ShareResult getData()
	{
		if(data == null)
			data = new ShareResult();
		return data;
	}

	public void setData(ShareResult data)
	{
		this.data = data;
	}
}
