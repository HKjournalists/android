package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.ImageInfoJson;
import com.kplus.car.parser.ApiField;

public class GetImgItemResponse extends Response
{
	@ApiField("data")
	private ImageInfoJson data;

	public ImageInfoJson getData()
	{
		if(data == null)
			data = new ImageInfoJson();
		return data;
	}

	public void setData(ImageInfoJson data)
	{
		this.data = data;
	}
}
