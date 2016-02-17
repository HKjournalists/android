package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.UploadCertImgJson;
import com.kplus.car.parser.ApiField;

public class UploadCertImgResponse extends Response
{
	@ApiField("data")
	private UploadCertImgJson data;

	public UploadCertImgJson getData()
	{
		if(data == null){
			data = new UploadCertImgJson();
		}
		return data;
	}

	public void setData(UploadCertImgJson data)
	{
		this.data = data;
	}
	
}
