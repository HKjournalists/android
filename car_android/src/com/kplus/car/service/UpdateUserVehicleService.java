package com.kplus.car.service;

import java.util.List;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.util.StringUtils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class UpdateUserVehicleService extends IntentService {
	private KplusApplication mApplication;
	
	public UpdateUserVehicleService(){
		super("UpdateUserVehicleService");
	}

	public UpdateUserVehicleService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(mApplication == null)
			mApplication = (KplusApplication) getApplication();
		List<UserVehicle> listUVs = mApplication.dbCache.getVehicles();
		List<VehicleAuth> listVAs = mApplication.dbCache.getVehicleAuths();
		updateVehicles(listUVs, listVAs);
	}

	private void updateVehicles(List<UserVehicle> listUVs, List<VehicleAuth> listVA){
		for(UserVehicle uv : listUVs){
			String vn = uv.getVehicleNum();
			String fn = uv.getFrameNum();
			String mn = uv.getMotorNum();
			if(!StringUtils.isEmpty(vn)){
				for(VehicleAuth va : listVA){
					String vn1 = va.getVehicleNum();
					if(!StringUtils.isEmpty(vn1) && vn.equals(vn1)){
						boolean isUpdate = false;
						String fn1 = va.getFrameNum();
						String mn1 = va.getMotorNum();
						if(!StringUtils.isEmpty(fn1)){
							if(StringUtils.isEmpty(fn)){
								isUpdate = true;
								uv.setFrameNum(fn1);
							}
							else{
								if(!fn1.equals(fn)){
									isUpdate = true;
									uv.setFrameNum(fn1);
								}
							}
						}
						if(!StringUtils.isEmpty(mn1)){
							if(StringUtils.isEmpty(mn)){
								isUpdate = true;
								uv.setMotorNum(mn1);
							}
							else{
								if(!mn1.equals(mn)){
									isUpdate = true;
									uv.setMotorNum(mn1);
								}
							}
						}
						if(isUpdate){
							mApplication.dbCache.updateVehicle(uv);
							UpdateUservehicle(uv);
						}
						break;
					}
				}
			}
		}
	}
	
	private void UpdateUservehicle(final UserVehicle uv){
		VehicleAddResponse result = null;
		try{
			VehicleAddRequest request = new VehicleAddRequest();
			request.setParams(mApplication.getUserId(), mApplication.getId(), uv);
			result = mApplication.client.execute(request);
		}
		catch(Exception e){
			e.printStackTrace();
			result = null;
		}
		if(result != null){
			if(result.getCode() != null && result.getCode() == 0){
				if(result.getData() != null && result.getData().getResult() != null && result.getData().getResult()){
					if(KplusConstants.isDebug){
						Log.i("UpdateUserVehicleService", "update vehicle ===>" 
								+ "  vehicleNumber:" + uv.getVehicleNum()
								+ (StringUtils.isEmpty(uv.getFrameNum()) ? "" : "  frameNumber:" + uv.getFrameNum())
								+ (StringUtils.isEmpty(uv.getMotorNum()) ? "" : "  motorNumber:" + uv.getMotorNum()));
					}
				}
			}
		}
	}
}
