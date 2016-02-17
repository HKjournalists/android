package com.kplus.car.carwash.utils;

import android.os.SystemClock;
import android.view.View;

import com.kplus.car.carwash.common.Const;

/**
 * view 防止多次点击的工具类
 * Created by FuZhixue on 2015/5/5.
 */
public class CNViewClickUtil {

    public interface NoFastClickListener {
        void onNoFastClick(View v);
    }

    public static void setNoFastClickListener(View v, final NoFastClickListener listener) {
        if (null == v) {
            throw new NullPointerException("v is not null!");
        }
        if (null == listener)
            return;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFastClick()) {
                    listener.onNoFastClick(v);
                }
            }
        });
    }

    private static long mLastClickTime;

    private static boolean isFastClick() {
        long time = SystemClock.uptimeMillis();
        long timeOut = time - mLastClickTime;
        if (Const.NONE < timeOut && timeOut < 500) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }
}
