package com.kplus.car.receiver;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.activity.EmergencyLicenseActivity;
import com.kplus.car.service.RemindService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

public class RemindReceiver extends BroadcastReceiver
{
	private KplusApplication mApplication = KplusApplication.getInstance();
	private Dialog dialog;
	private String vehicleNumber;
	
	@Override
	public void onReceive(final Context context, Intent intent)
	{
		try{
			vehicleNumber = intent.getStringExtra("vehicleNumber");		
			Intent service = new Intent(context, RemindService.class);
			context.startService(service);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("橙牛汽车管家行驶证导入提醒闹钟");
			builder.setMessage("橙牛汽车管家提醒您，导入行驶证，立享认证豪礼。");
			builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					Intent intent;
					if (!isAppOnForeground()){
						intent = new Intent(mApplication,EmergencyLicenseActivity.class);
						intent.setAction(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("vehicleNumber", vehicleNumber);
					}
					else{
						intent = new Intent(mApplication,EmergencyLicenseActivity.class);
						intent.putExtra("vehicleNumber", vehicleNumber);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					}
					mApplication.startActivity(intent);
				}
			});
			builder.setNegativeButton("取消",new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();
				}
			});
			if(dialog == null){
				dialog = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(true);
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			}
			dialog.show();		
			mApplication.setRemindPendingIntent(null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	private boolean isAppOnForeground() {
		Log.d("processName", mApplication.packageName);
		ComponentName componentName = mApplication.activityManager
				.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = componentName.getPackageName();
		if (currentPackageName.equals(mApplication.packageName)) {
			return true;
		}
		return false;
	}
}
