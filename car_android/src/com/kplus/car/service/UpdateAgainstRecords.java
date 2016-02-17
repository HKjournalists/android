package com.kplus.car.service;

import java.util.List;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.response.GetAgainstRecordListResponse;
import com.kplus.car.model.response.request.GetAgainstRecordListRequest;
import android.app.IntentService;
import android.content.Intent;

public class UpdateAgainstRecords extends IntentService{
	private KplusApplication mApplication;
	private String vehicleNumber;
	
	public UpdateAgainstRecords(){
		super("UpdateAgainstRecords");
	}

	public UpdateAgainstRecords(String name) {
		super(name);
	}
	
	@Override
	public void onCreate() {
		mApplication = (KplusApplication) getApplication();
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		vehicleNumber = intent.getStringExtra("vehicleNumber");
		GetAgainstRecordListRequest request = new GetAgainstRecordListRequest();
		try {
			UserVehicle uv = mApplication.dbCache.getVehicle(vehicleNumber);
			long updateTime = 0;
			try{
				if(uv != null)
					updateTime = Long.parseLong(uv.getUpdateTime());
			}
			catch(Exception E){
				updateTime = 0;
				E.printStackTrace();
			}
			request.setParams(mApplication.getUserId(), vehicleNumber, updateTime);
			GetAgainstRecordListResponse result =  mApplication.client.execute(request);
			if(result != null && result.getCode() != null && result.getCode() == 0){
				List<AgainstRecord> listAr = result.getData().getList() ;
				if(listAr != null && !listAr.isEmpty()){
					if(result.getData().getResultType() != null && result.getData().getResultType() == 1){
						mApplication.dbCache.deleteImageAgainstRecord(vehicleNumber);
						for(AgainstRecord ar : listAr)
							ar.setResultType(1);
					}
					mApplication.dbCache.saveAgainstRecords(listAr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
