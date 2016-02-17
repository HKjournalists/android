package com.kplus.car.service;

import com.alibaba.mobileim.login.YWLoginState;
import com.kplus.car.KplusApplication;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.model.response.GetUserOpenImResponse;
import com.kplus.car.model.response.request.GetUserOpenImRequest;
import com.kplus.car.util.StringUtils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class GetUserOpenImService extends Service {
	private KplusApplication mApplication;
	private int startId;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		mApplication = (KplusApplication) getApplication();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.startId = startId;
		if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
			getUserOpenIm();
		}
		else{
			mApplication.initTaobao();
			if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
				
			}
			else if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.logining ){
			}
			else{
				mApplication.loginTaobao(mApplication.getOpenImUserId(), mApplication.getOpenImPassWord());
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void getUserOpenIm(){
		new AsyncTask<Void, Void, GetUserOpenImResponse>(){
			@Override
			protected GetUserOpenImResponse doInBackground(Void... params) {
				GetUserOpenImRequest request = new GetUserOpenImRequest();
				request.setParams(mApplication.getId());
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(GetUserOpenImResponse result) {
				if(result != null && result.getCode() != null && result.getCode() == 0){
					UserOpenIm data = result.getData();
					if(data != null && !StringUtils.isEmpty(data.getOpenUserid()) && !StringUtils.isEmpty(data.getOpenPassword())){
						mApplication.initTaobao();
						mApplication.loginTaobao(data.getOpenUserid(), data.getOpenPassword());
						mApplication.setOpenImUserId(data.getOpenUserid());
						mApplication.setOpenImPassWord(data.getOpenPassword());
					}
				}
				stopSelf(startId);
			}
		}.execute();
	}

}
