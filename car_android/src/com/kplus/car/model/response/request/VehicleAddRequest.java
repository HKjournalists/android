package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.util.MD5;
import com.kplus.car.util.StringUtils;

public class VehicleAddRequest extends BaseRequest<VehicleAddResponse> {
	
	@Override
	public String getApiMethodName() {
		return "/vehicle/add.htm";
	}

	@Override
	public Class<VehicleAddResponse> getResponseClass() {
		return VehicleAddResponse.class;
	}

	public void setParams(long userId, long uid, String vehicleNum, String cityId,
			String motorNum, String frameNum, long vehicleModelId, String desc, String picUrl) {
		addParams("userId", userId);
		if(uid != 0)
			addParams("uid", uid);
		addParams("vehicleNum", vehicleNum);
		addParams("cityId", cityId).addParams("motorNum", motorNum);
		addParams("frameNum", frameNum);
		addParams("vehicleModelId", vehicleModelId);
		addParams("desc", desc);
		addParams("picUrl", picUrl);
	}

	public void setParams(long userId,long uid, UserVehicle vehicle,String frameNum, String motorNum, String vehicleOwner, String ownerId, String driverName, String driverId, String account, String password, String vehicleRegCerNo) {
		addParams("userId", userId);
		if(uid != 0)
			addParams("uid", uid);
		addParams("vehicleNum", vehicle.getVehicleNum());
		addParams("cityId", vehicle.getCityId());
		if(!StringUtils.isEmpty(motorNum)){
			addParams("motorNum", motorNum);
		}
		if(!StringUtils.isEmpty(frameNum))
			addParams("frameNum", frameNum);
		if(!StringUtils.isEmpty(vehicleOwner)){
			addParams("vehicleOwner", vehicleOwner);
		}
		if(!StringUtils.isEmpty(ownerId)){
			addParams("ownerIdNo", ownerId);
		}
		if(!StringUtils.isEmpty(driverName)){
			addParams("drivingLicenseName", driverName);
		}
		if(!StringUtils.isEmpty(driverId)){
			addParams("drivingLicenseNo", driverId);
		}
		if(!StringUtils.isEmpty(account))
			addParams("account", account);
		if(!StringUtils.isEmpty(password)){
			addParams("password", password);
		}
		if(!StringUtils.isEmpty(vehicleRegCerNo))
			addParams("vehicleRegCerNo", vehicleRegCerNo);
	}
	
	public void setParams(long userId,long uid, UserVehicle vehicle) {
		addParams("userId", userId);
		if(uid != 0)
			addParams("uid", uid);
		addParams("vehicleNum", vehicle.getVehicleNum());
		addParams("cityId", vehicle.getCityId());
		if(!StringUtils.isEmpty(vehicle.getMotorNum()))
			addParams("motorNum", vehicle.getMotorNum());
		if(!StringUtils.isEmpty(vehicle.getFrameNum()))
			addParams("frameNum", vehicle.getFrameNum());
		if(vehicle.getVehicleModelId() != 0)
			addParams("vehicleModelId", vehicle.getVehicleModelId());
		if(!StringUtils.isEmpty(vehicle.getDescr()))
			addParams("desc", vehicle.getDescr());
		if(!StringUtils.isEmpty(vehicle.getPicUrl()))
			addParams("picUrl", vehicle.getPicUrl());
		if(!StringUtils.isEmpty(vehicle.getAccount()))
			addParams("account", vehicle.getAccount());
		if(!StringUtils.isEmpty(vehicle.getPassword()))
			addParams("password", vehicle.getPassword());
		if(!StringUtils.isEmpty(vehicle.getIssueDate()))
			addParams("issueDate", vehicle.getIssueDate());
		if(!StringUtils.isEmpty(vehicle.getRegDate()))
			addParams("regDate", vehicle.getRegDate());
		if(!StringUtils.isEmpty(vehicle.getVehicleOwner()))
			addParams("vehicleOwner", vehicle.getVehicleOwner());
		if(!StringUtils.isEmpty(vehicle.getVehicleRegCerNo()))
			addParams("vehicleRegCerNo", vehicle.getVehicleRegCerNo());
		if(vehicle.getVehicleType() != null)
			addParams("vehicleType", "" + vehicle.getVehicleType());
		if (!StringUtils.isEmpty(vehicle.getOwnerId()))
			addParams("ownerIdNo", vehicle.getOwnerId());
		if (!StringUtils.isEmpty(vehicle.getDriverName()))
			addParams("drivingLicenseName", vehicle.getDriverName());
		if (!StringUtils.isEmpty(vehicle.getDriverId()))
			addParams("drivingLicenseNo", vehicle.getDriverId());
	}
	
	@Override
	public Map<String, Object> getTextParams() {
		long time = System.currentTimeMillis();
		map.put("appkey", KplusConstants.CLIENT_APP_KEY);
		map.put("time", time);
		map.put("v", 1);
		if(KplusApplication.sUserId != 0)
			map.put("userId",KplusApplication.sUserId);
		if (jsonObject != null) {
			map.put("params", jsonObject.toString());
			map.put("sign",
					MD5.md5(KplusConstants.CLIENT_APP_KEY
							+ KplusConstants.CLIENT_APP_SECRET + time
							+ jsonObject.toString()));
		} else {
			map.put("sign",
					MD5.md5(KplusConstants.CLIENT_APP_KEY
							+ KplusConstants.CLIENT_APP_SECRET + time));
		}
		return map;
	}
}
