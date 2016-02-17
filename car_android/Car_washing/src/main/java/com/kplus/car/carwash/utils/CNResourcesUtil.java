package com.kplus.car.carwash.utils;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.kplus.car.carwash.module.CNCarWashApp;

/**
 * Description：资源文件工具类
 * <br/><br/>Created by FU ZHIXUE on 2015/8/4.
 * <br/><br/>
 */
public class CNResourcesUtil {
    private CNResourcesUtil() {
    }

    public static String getStringResources(int id) {
        return CNCarWashApp.getIns().getApplicationContext().getResources().getString(id);
    }

    public static int getColorResources(int id) {
        return CNCarWashApp.getIns().getApplicationContext().getResources().getColor(id);
    }

    public static Drawable getDrawable(int id) {
        return CNCarWashApp.getIns().getApplicationContext().getResources().getDrawable(id);
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }
}
