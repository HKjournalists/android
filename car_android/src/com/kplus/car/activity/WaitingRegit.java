package com.kplus.car.activity;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.util.EventAnalysisUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

public class WaitingRegit extends Activity
{
	private static final int REQUEST_FOR_SET_TIME = 1;
	private static final int REQUEST_FOR_NETWORK = 2;
	private static final int REQUEST_FOR_DATE = 3;
	private static final int REQUEST_FOR_BIND_FAIL = 4;
	private View rootView;
	private KplusApplication mApplication;
	
	private BroadcastReceiver receicer = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent != null && intent.getBooleanExtra("issuccess", false)){
				WaitingRegit.this.setResult(RESULT_OK);
				finish();
			}
			else{
				if(intent.getBooleanExtra("isNetWorkDisconnected", false)){
					Intent alertIntent = new Intent(WaitingRegit.this, AlertDialogActivity.class);
					alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
					alertIntent.putExtra("message", "网络中断，请稍候重试");
					startActivityForResult(alertIntent, REQUEST_FOR_NETWORK);
				}
				else{
					if(intent.getBooleanExtra("isTimeError", false)){
						Intent alertIntent = new Intent(WaitingRegit.this, AlertDialogActivity.class);
						alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
						alertIntent.putExtra("message", "手机时间错误，请重新设置！");
						startActivityForResult(alertIntent, REQUEST_FOR_DATE);
					}
					else{
						boolean createIdSuccess = intent.getBooleanExtra("createIdSuccess", true);
						Intent alertIntent = new Intent(WaitingRegit.this, AlertDialogActivity.class);
						alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
						alertIntent.putExtra("message", createIdSuccess ? "设备绑定失败，请稍候重试" : "设备ID生成失败，请稍候重试");
						startActivityForResult(alertIntent, REQUEST_FOR_BIND_FAIL);
					}
				}				
			}
		}		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daze_waiting);
		mApplication = (KplusApplication) getApplication();
		rootView = findViewById(R.id.rootView);
		rootView.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				return;
			}
		});
		IntentFilter filter = new IntentFilter(KplusConstants.ACTION_GET_USERID);
		getApplicationContext().registerReceiver(receicer, filter);
	}
	
	@Override
	protected void onResume()
	{
		if(mApplication.getUserId() != 0)
			finish();
		super.onResume();
		EventAnalysisUtil.onResume(this);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		EventAnalysisUtil.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		getApplicationContext().unregisterReceiver(receicer);
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_SET_TIME){
			mApplication.regist();
		}
		else if(requestCode == REQUEST_FOR_NETWORK || requestCode == REQUEST_FOR_BIND_FAIL){
			finish();
			Intent i = new Intent("finish");
			LocalBroadcastManager.getInstance(this).sendBroadcast(i);
		}
		else if(requestCode == REQUEST_FOR_DATE){
			try{
				startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), REQUEST_FOR_SET_TIME);
			}
			catch(Exception e){
				e.printStackTrace();
				startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), REQUEST_FOR_SET_TIME);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
