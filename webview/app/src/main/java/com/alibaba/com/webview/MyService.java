package com.alibaba.com.webview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.i("myservice", "创建服务");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("myservice", "绑定服务");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("myservice", "启动服务");
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText("阿凡提").setContentText("亲,你该买车了").setTicker("你有新的消息").setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
        Log.i("myservice", "启动");
        return super.onStartCommand(intent, flags, startId);
    }

}