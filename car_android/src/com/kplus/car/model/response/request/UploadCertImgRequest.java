package com.kplus.car.model.response.request;

import com.kplus.car.model.response.UploadCertImgResponse;

public class UploadCertImgRequest extends BaseRequest<UploadCertImgResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/uploadImg/uploadCertImg.htm";
	}

	@Override
	public Class<UploadCertImgResponse> getResponseClass()
	{
		return UploadCertImgResponse.class;
	}
	
	public void setParams(int type){
		addParams("type", type);
	}
}
