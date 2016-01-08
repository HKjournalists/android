package com.alibaba.com.webview;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/12/8.
 */
public class AlibabaApplication extends Application {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaids() {
        return areaids;
    }

    public void setAreaids(String areaids) {
        this.areaids = areaids;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String userId = null;
    String areaids = null;
    String username = null;

    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Toast.makeText(AlibabaApplication.super.getApplicationContext(), "系统忙", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
