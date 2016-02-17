package com.kplus.car.activity;


import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.receiver.RemindReceiver;
import com.kplus.car.util.EventAnalysisUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

public class Remind extends Activity
{
	private AlarmManager alarmManager;
	private KplusApplication mApplication;
	private String vehicleNumber;
	public SharedPreferences sp;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daze_emergency_licence_remind);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(lp);
		alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		mApplication = (KplusApplication) getApplication();
		sp = getSharedPreferences("kplussp", MODE_PRIVATE);
		if(getIntent().hasExtra("vehicleNumber")){
			vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		EventAnalysisUtil.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventAnalysisUtil.onResume(this);
	}
	
	public void onClick(View v){
		Intent i = null;
		i = new Intent(this,RemindReceiver.class);
		i.putExtra("vehicleNumber", vehicleNumber);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
		switch(v.getId()){
			case R.id.btFifteenMinutes:
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 15*60*1000, pi);
				mApplication.setRemindPendingIntent(pi);
				break;
			case R.id.btThirtyMinutes:
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30*60*1000, pi);
				mApplication.setRemindPendingIntent(pi);
				break;
			case R.id.btOneHour:
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60*60*1000, pi);
				mApplication.setRemindPendingIntent(pi);
				break;
			case R.id.btThreeHour:
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3*60*60*1000, pi);
				mApplication.setRemindPendingIntent(pi);
				break;
			case R.id.btSixHour:
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 6*60*60*1000, pi);
				mApplication.setRemindPendingIntent(pi);
				break;
			case R.id.btCancel:
				break;
				default:
					break;
		}
		finish();
	}
}
