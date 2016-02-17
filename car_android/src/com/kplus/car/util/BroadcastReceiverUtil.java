package com.kplus.car.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastReceiverUtil {
    public static final String ACTION_CHANGE_TAB = "android.kplus.car.action.change.tab";//切换Tab
    public static final String ACTION_PAY_SUCCESS = "android.kplus.car.action.pay.success";//切换Tab

    public interface BroadcastListener{
        public void onReceiverBroadcast(Intent data);
    }

    public static BroadcastReceiver createBroadcastReceiver(final BroadcastListener listener){
        BroadcastReceiver receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                listener.onReceiverBroadcast(intent);
            }
        };
        return receiver;
    }

    public static void registerReceiver(Context context, String action, BroadcastReceiver receiver){
        IntentFilter filter = new IntentFilter(action);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).registerReceiver(receiver, filter);
    }

    public  static void unRegisterReceiver(Context context, BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(context.getApplicationContext()).unregisterReceiver(receiver);
    }
}
