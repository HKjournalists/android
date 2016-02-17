package com.kplus.car.model.response.request;

import com.kplus.car.model.LicenceInfo;
import com.kplus.car.model.response.VehicleAuthResponse;
import com.kplus.car.util.StringUtils;

public class VehicleAuthRequest extends BaseRequest<VehicleAuthResponse>{

	@Override
	public String getApiMethodName() {
		return "/vehicle/auth.htm";
	}

	@Override
	public Class<VehicleAuthResponse> getResponseClass() {
		return VehicleAuthResponse.class;
	}
	
	public void setParams(long userId, long uid, String vehicleNum, String picUrl, LicenceInfo liceninfo){
		addParams("userId", userId).addParams("uid", uid).addParams("vehicleNum", vehicleNum).addParams("picUrl", picUrl);
		if(liceninfo != null){
			if(!StringUtils.isEmpty(liceninfo.getOwner()))
				addParams("owner", liceninfo.getOwner());
			if(!StringUtils.isEmpty(liceninfo.getVehicleType()))
				addParams("vehicleType", liceninfo.getVehicleType());
			if(!StringUtils.isEmpty(liceninfo.getUseProperty()))
				addParams("useProperty", liceninfo.getUseProperty());
			if(!StringUtils.isEmpty(liceninfo.getBrandModel()))
				addParams("brandModel", liceninfo.getBrandModel());
			if(!StringUtils.isEmpty(liceninfo.getFrameNum()))
				addParams("frameNum", liceninfo.getFrameNum());
			if(!StringUtils.isEmpty(liceninfo.getMotorNum()))
				addParams("motorNum", liceninfo.getMotorNum());
			if(!StringUtils.isEmpty(liceninfo.getRegisterDate()))
				addParams("registerDate", liceninfo.getRegisterDate());
			if(!StringUtils.isEmpty(liceninfo.getIssueDate()))
				addParams("issueDate", liceninfo.getIssueDate());
		}
	}

}
