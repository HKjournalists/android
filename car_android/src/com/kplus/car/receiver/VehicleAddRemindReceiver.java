package com.kplus.car.receiver;

import java.util.List;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.service.RemindService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class VehicleAddRemindReceiver extends BroadcastReceiver {
	private KplusApplication mApplication = KplusApplication.getInstance();
	private SharedPreferences sp;
	@Override
	public void onReceive(final Context context, Intent intent) {
		sp = context.getSharedPreferences("kplussp", Context.MODE_PRIVATE);
		sp.edit().putBoolean("hasRemind", false).commit();
		Intent service = new Intent(context, RemindService.class);
		context.startService(service);
		Intent i = new Intent(context, AlertDialogActivity.class);
		i.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
		i.putExtra("title", "提醒");
		i.putExtra("message", "橙牛汽车管家提醒您添加你的爱车");
		i.putExtra("subAlertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_ADD_VEHICLE);
		if(!isAppRunning()){
			i.setAction(Intent.ACTION_MAIN);
			i.addCategory(Intent.CATEGORY_LAUNCHER);
		}
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
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
					Log.i(packageName, "costtime===>" + (System.currentTimeMillis() - begintime));
				}
				return true;
			}
		}
		if(KplusConstants.isDebug){
			Log.i(packageName, packageName + "is not running");
			Log.i(packageName, "costtime===>" + (System.currentTimeMillis() - begintime));
		}
		return false;
	}

}
