package com.kplus.car.service;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.BooleanResultResponse;
import com.kplus.car.model.response.request.UpgradeDataRequest;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class UpgradeDataService extends IntentService {
	private KplusApplication mApplication;
	private SharedPreferences sp;
	
	public UpgradeDataService() {
		super("UpgradeDataService");
	}

	public UpgradeDataService(String name) {
		super(name);
	}
	
	@Override
	public void onCreate() {
		mApplication = (KplusApplication) getApplication();
		sp = getSharedPreferences("kplussp", MODE_PRIVATE);
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		if(mApplication.getId() != 0){
			if(sp.getBoolean("isCouponDevided", false) == false){
				try{
					UpgradeDataRequest request = new UpgradeDataRequest();
					request.setParams(mApplication.getUserId(), mApplication.getpId(), mApplication.getId());
					BooleanResultResponse result = mApplication.client.execute(request);
					if(result != null && result.getCode() != null && result.getCode() == 0 && result.getData() != null && result.getData().getResult() != null && result.getData().getResult() == true){
						Log.i("UpgradeDataService", "代金券拆分成功");
						sp.edit().putBoolean("isCouponDevided", true).commit();
					}
					else{
						int count = sp.getInt("couponDevideCount", 0);
						if(count < 2){
							startService(new Intent(UpgradeDataService.this, UpgradeDataService.class));
						}
						sp.edit().putInt("couponDevideCount", ++count).commit();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}
