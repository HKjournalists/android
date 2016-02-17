package com.kplus.car.service;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.activity.MessageBoxActivity;
import com.kplus.car.util.StringUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class NotificationService extends Service {
	private int startId;
	private KplusApplication mApplication;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mApplication == null)
			mApplication = (KplusApplication) getApplication();
		if(sp == null)
			sp = getSharedPreferences("kplussp", MODE_PRIVATE);
		this.startId = startId;
		String noticeContent = intent.getStringExtra("noticeContent");
		if(!StringUtils.isEmpty(noticeContent)){
			showNotification(noticeContent);
			if(intent.hasExtra("newMessage") && intent.getBooleanExtra("newMessage", false)){
				String messageNumber = mApplication.dbCache.getValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER);
				int nMessageNumber = 0;
				if(!StringUtils.isEmpty(messageNumber)){
					try{
						nMessageNumber = Integer.parseInt(messageNumber);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				nMessageNumber++;
				mApplication.dbCache.putValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER, String.valueOf(nMessageNumber));
				Intent i = new Intent("com.kplus.car.GexinSdkMsgReceiver.newmessage");
				i.putExtra("newMessage", nMessageNumber);
				LocalBroadcastManager.getInstance(NotificationService.this).sendBroadcast(i);
			}
		}
		else{
			stopSelf(startId);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void showNotification(String noticeContent) {
		try{
			// 定义Notification的各种属性
			Notification notification = new Notification();
			notification.icon = R.drawable.daze_icon;
			notification.tickerText = noticeContent;
			notification.when = System.currentTimeMillis();
			// 点击后自动清除Notification
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			// 设置通知的事件消息
			CharSequence contentTitle = "橙牛汽车管家"; // 通知栏标题
			Intent intent = new Intent(this, MessageBoxActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(
					mApplication, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(mApplication, contentTitle,
					noticeContent, contentIntent);
			// 把Notification传递给NotificationManager
			int notifyId = sp.getInt("notifyId", 1);
			mApplication.notificationManager.notify(notifyId, notification);
			sp.edit().putInt("notifyId", ++notifyId).commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			stopSelf(startId);
		}
	}

}
