package com.kplus.car.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.comm.TaskInfo;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.response.GetAgainstRecordListResponse;
import com.kplus.car.model.response.request.GetAgainstRecordListRequest;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class AsyncTaskService extends Service{
	private KplusApplication mApplication;
	private int startId;
	public static boolean isRunning = false;
	private Handler mHandler = new Handler();
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mApplication = (KplusApplication) getApplication();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isRunning = true;
		this.startId = startId;
		new TaskScanThread().start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void getRecords(final String vehicleNumber, final long updateTime){
		new AsyncTask<Void, Void, GetAgainstRecordListResponse>() {
			@Override
			protected GetAgainstRecordListResponse doInBackground(
					Void... params) {
				GetAgainstRecordListRequest request = new GetAgainstRecordListRequest();
				request.setParams(mApplication.getUserId(), vehicleNumber, updateTime);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(GetAgainstRecordListResponse result) {
				if(KplusConstants.isDebug){
					if(result != null && result.getBody() != null)
						System.out.println("AsyncTaskService===>result = " + result.getBody());
				}
				if(mApplication.containsTask(vehicleNumber)){
					UserVehicle vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
					if(vehicle != null){
						if(result != null && result.getCode() != null && result.getCode() == 0 && result.getData() != null && result.getData().getType() != null){
							String dataType = result.getData().getType();
							boolean add = false;
							if(dataType.contains("0")){
								add = true;
								mApplication.removeTask(vehicleNumber);
								vehicle.setUpdateTime("" + result.getPostDateTime());
								vehicle.setReturnTime(System.currentTimeMillis());
								List<AgainstRecord> listResult = result.getData().getList();
								EventAnalysisUtil.onEvent(AsyncTaskService.this, "click_wz_success", "查询违章成功", null);
								if(listResult != null && !listResult.isEmpty()){
									if(result.getData().getResultType() != null && result.getData().getResultType() == 1){
										mApplication.dbCache.deleteImageAgainstRecord(vehicleNumber);
										for(AgainstRecord ar : listResult)
											ar.setResultType(1);
									}
									mApplication.dbCache.saveAgainstRecords(listResult);
									vehicle.setNewRecordNumber(listResult.size());
								}
								mApplication.dbCache.updateVehicle(vehicle);
							}
							if(dataType.contains("1")){
								mApplication.removeTask(vehicleNumber);
								List<CityVehicle> cityVehicles = result.getData().getRule();
								mApplication.dbCache.saveCityVehicles(cityVehicles);
								mApplication.updateCities(cityVehicles);
								vehicle.setReturnTime(System.currentTimeMillis());
								vehicle.setHasRuleError(true);
								mApplication.dbCache.updateVehicle(vehicle);
							}
							if(dataType.contains("2")){
								mApplication.removeTask(vehicleNumber);
								vehicle.setReturnTime(System.currentTimeMillis());
								vehicle.setHasParamError(true);
								mApplication.dbCache.updateVehicle(vehicle);
							}
							if(dataType.contains("3")){
								mApplication.removeTask(vehicleNumber);
								vehicle.setReturnTime(System.currentTimeMillis());
								String strCity = result.getData().getCity();
								strCity = strCity.replaceAll(" ", "");
								strCity = strCity.replaceAll("，", ",");
								strCity = strCity.replaceAll("：", "\\:");
								strCity = strCity.replace("{", "");
								strCity = strCity.replace("}", "");
								strCity = strCity.replaceAll("=", "\\:");
//								vehicle.setCityUnValid(strCity);
								mApplication.dbCache.updateVehicle(vehicle);
								String cityIds = null;
								try{
									String[] cityParams = strCity.split(",");
									if(cityParams != null && cityParams.length > 0){
										for(int i=0;i<cityParams.length;i++){
											if(cityParams[i] != null){
												String[] subCityParams = cityParams[i].split("\\:");
												if(subCityParams != null && subCityParams.length > 1){
													if(cityIds == null)
														cityIds = subCityParams[0];
													else
														cityIds += ("," + subCityParams[0]);
												}
											}
										}
									}
								}
								catch(Exception e){
									e.printStackTrace();
								}
								List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(cityIds);
								if(cvs != null && !cvs.isEmpty()){
									for(CityVehicle cv : cvs){
										cv.setValid(false);
									}
									mApplication.dbCache.saveCityVehicles(cvs);
								}
//								mApplication.dbCache.deleteAgainstRecordByCityIds(cityIds);
							}
							TaskInfo ti = mApplication.getTask(vehicleNumber);
							if(dataType.contains("4")){
								TaskInfo taskInfo = mApplication.getTask(vehicleNumber);
								if(taskInfo != null){
									System.out.println("AsyncTaskService===>nCount = " + taskInfo.getnCount());
									if(taskInfo.getnCount() >= 5){
										mApplication.removeTask(vehicleNumber);
										Intent intent = new Intent("com.kplus.car.getagainstRecords");
										intent.putExtra("vehicleNumber", vehicleNumber);
//										intent.putExtra("fail", true);
										LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//										vehicle.setHasSearchFail(true);
										mApplication.dbCache.updateVehicle(vehicle);
										showResult(vehicleNumber, false, false);
									}
								}
							}
							else if((dataType.contains("5") && ti != null && !ti.isHasEnterVerifyCode()) || dataType.contains("8")){
//								if(ti.getnCount() >= 4){
//									mApplication.removeTask(vehicleNumber);
//									Intent intent = new Intent("com.kplus.car.getagainstRecords");
//									intent.putExtra("vehicleNumber", vehicleNumber);
//									intent.putExtra("fail", true);
//									intent.putExtra("verifyCodeError", true);
//									LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//									vehicle.setHasSearchFail(true);
//									mApplication.dbCache.updateVehicle(vehicle);
//									showResult(vehicleNumber, false, true);
//									return;
//								}
								if(dataType.contains("5")){
									String sessionId = result.getData().getSessionId();
									if(!StringUtils.isEmpty(sessionId)){
										ti.setSessionId(sessionId);
										Intent intent = new Intent(getApplicationContext(), AlertDialogActivity.class);
										intent.putExtra("alertType", KplusConstants.ALERT_INPUT_VERIFY_CODE);
										intent.putExtra("vehicleNumber", vehicleNumber);
										intent.putExtra("sessionId", sessionId);
										if(!isAppRunning()){
											intent.setAction(Intent.ACTION_MAIN);
											intent.addCategory(Intent.CATEGORY_LAUNCHER);				
										}
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);
									}
								}
								else if(dataType.contains("8")){
									if(ti != null){
										String sessionId = ti.getSessionId();
										if(!StringUtils.isEmpty(sessionId)){
											Intent intent = new Intent(getApplicationContext(), AlertDialogActivity.class);
											intent.putExtra("alertType", KplusConstants.ALERT_INPUT_VERIFY_CODE);
											intent.putExtra("vehicleNumber", vehicleNumber);
											intent.putExtra("sessionId", sessionId);
											if(!isAppRunning()){
												intent.setAction(Intent.ACTION_MAIN);
												intent.addCategory(Intent.CATEGORY_LAUNCHER);				
											}
											intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(intent);
										}
									}
								}
							}
							else{
								if(dataType.contains("0") || dataType.contains("1") || dataType.contains("2") || dataType.contains("3")){
									Intent intent = new Intent("com.kplus.car.getagainstRecords");
									intent.putExtra("type", dataType);
									intent.putExtra("vehicleNumber", vehicleNumber);
									LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
									showResult(vehicleNumber, add,false);
								}
							}
						}
						else{
							mApplication.removeTask(vehicleNumber);
							Intent intent = new Intent("com.kplus.car.getagainstRecords");
							intent.putExtra("vehicleNumber", vehicleNumber);
							intent.putExtra("fail", true);
							LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
							vehicle.setHasSearchFail(true);
							mApplication.dbCache.updateVehicle(vehicle);
							showResult(vehicleNumber, false, false);
						}
					}
				}
			}
		}.execute();
	}
	
	class TaskScanThread extends Thread{
		@Override
		public void run() {
			Map<String, TaskInfo> tasks = null;
			while(!(tasks = mApplication.getTasks()).isEmpty()){
				try {
					long correntTime = System.currentTimeMillis();
					Iterator<Entry<String, TaskInfo>> iterator = tasks.entrySet().iterator();
					while(iterator.hasNext()){
						final TaskInfo taskInfo = iterator.next().getValue();
						if(taskInfo != null){
							long startTime = taskInfo.getStartTime();
							int nCount = taskInfo.getnCount();
							if(nCount < 5){
								if((correntTime - startTime) > nCount*(15000 + 15000*nCount)/2){
									nCount++;
									taskInfo.setnCount(nCount);
									mHandler.post(new Runnable() {										
										@Override
										public void run() {
											getRecords(taskInfo.getVehicleNumber(), taskInfo.getUpdateTime());
										}
									});
								}
							}
						}
					}
					Thread.sleep(5*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			mHandler.post(new Runnable() {				
				@Override
				public void run() {
					isRunning = false;
					stopSelf(startId);
				}
			});
			super.run();
		}
		
	}
	
	private void showResult(String vehicleNumber, boolean add, boolean verifyCodeError){
		Intent intentComplete = new Intent(mApplication, AlertDialogActivity.class);
		intentComplete.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
		intentComplete.putExtra("vehicleNumber", vehicleNumber);
		intentComplete.putExtra("title", "您的违章查询任务已完成");
		intentComplete.putExtra("message", "小牛很高兴地告诉您，你提交的[查询车辆" + vehicleNumber + "违章]任务已经执行完毕");
		intentComplete.putExtra("leftButtonText", "关闭");
		intentComplete.putExtra("rightButtonText", "查看结果");
		intentComplete.putExtra("subAlertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_VIEW_AGAINST_RESULT);
		intentComplete.putExtra("add", add);
		intentComplete.putExtra("verifyCodeError", verifyCodeError);
		intentComplete.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		if(!isAppRunning()){
//			intentComplete.setAction(Intent.ACTION_MAIN);
//			intentComplete.addCategory(Intent.CATEGORY_LAUNCHER);				
//		}
		startActivity(intentComplete);
	}
	
	private boolean isAppRunning(){
		long begintime = System.currentTimeMillis();
		ActivityManager activityManager = (ActivityManager)mApplication.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rtis = activityManager.getRunningTasks(100);
		String packageName = mApplication.getPackageName();
		for(RunningTaskInfo rti : rtis){
			String topActivityPackageName = rti.topActivity.getPackageName();
			String baseActivityPackageName = rti.baseActivity.getPackageName();
			if(topActivityPackageName.equals(packageName) && baseActivityPackageName.equals(packageName)){
				if(KplusConstants.isDebug){
					Log.i(packageName, packageName + "is running");
					Log.i(packageName, "costtime===>" + (System.currentTimeMillis() - begintime)/1000);
				}
				return true;
			}
		}
		if(KplusConstants.isDebug){
			Log.i(packageName, packageName + "is not running");
			Log.i(packageName, "costtime===>" + (System.currentTimeMillis() - begintime)/1000);
		}
		return false;
	}
}
