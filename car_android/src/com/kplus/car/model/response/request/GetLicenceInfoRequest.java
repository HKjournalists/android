package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetLicenceInfoReponse;

public class GetLicenceInfoRequest extends BaseRequest<GetLicenceInfoReponse> {

	@Override
	public String getApiMethodName() {
		return "/vehicle/getLicenceInfo.htm";
	}

	@Override
	public Class<GetLicenceInfoReponse> getResponseClass() {
		return GetLicenceInfoReponse.class;
	}
	
	public void setParams(String vehicleNum, String picUrl){
		addParams("vehicleNum", vehicleNum).addParams("picUrl", picUrl);
	}

}
