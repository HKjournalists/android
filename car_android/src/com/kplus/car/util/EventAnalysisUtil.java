package com.kplus.car.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.carwash.utils.CNAppInfoUtil;
import com.lotuseed.android.Lotuseed;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 统计的工具类
 */
public class EventAnalysisUtil {
    private static final String TAG = "EventAnalysisUtil";

    private static Stack<Activity> mActivityStack = null;

    public static void push(Activity activity) {
        if (null == mActivityStack) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.push(activity);
    }

    public static void remove(Activity activity) {
        if (null == mActivityStack) {
            mActivityStack = new Stack<>();
        } else {
            mActivityStack.remove(activity);
        }
    }

    public static int getStackSize() {
        if (null == mActivityStack) {
            return 0;
        }
        return mActivityStack.size();
    }

    /**
     * 统计App激活日志
     *
     * @param context context
     */
    public static void analysisLoginActive(Context context) {
        int actSize = EventAnalysisUtil.getStackSize();
        // size 为0表示先激活的应用
        if (actSize == 0) {
            KplusApplication mApp = KplusApplication.getInstance();
            Map<String, String> values = new HashMap<>();
            values.put("systime", System.currentTimeMillis() + "");
            values.put("user_ip", CNAppInfoUtil.getIp(context));
            values.put("phone", mApp.getContactphone());
            values.put("user_id", mApp.getUserId() + "");
            values.put("client_id", CNAppInfoUtil.getDeviceId(context));
            values.put("uid", mApp.getId() + "");
            values.put("client_type", "Android");
            values.put("app_version", CNAppInfoUtil.getVersionName(context));
            values.put("client_version", CNAppInfoUtil.getDeviceModelAndVersion());
            values.put("city_code", mApp.getCityName());
            if (TextUtils.isEmpty(KplusConstants.appChannel)) {
                KplusConstants.initData(context);
            }
            values.put("ref", KplusConstants.appChannel);
            Lotuseed.onEvent("active", values);
        }
    }

    private EventAnalysisUtil() {
    }

    public static void onStart(Activity activity) {
        // 4.0以下实现
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            EventAnalysisUtil.analysisLoginActive(activity);
            push(activity);
        }
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
        Lotuseed.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
        Lotuseed.onPause(context);
    }

    public static void onStop(Activity activity) {
        // 4.0以下实现
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            remove(activity);
        }
    }

    public static void onEvent(Context ctx, String EVENT_ID, String EVENT_LABEL, Map<String, String> values) {
        if (null == values) {
            values = new HashMap<>();
        }
        KplusApplication mApp = KplusApplication.getInstance();
        values.put("user_id", mApp.getUserId() + "");
        values.put("uid", mApp.getId() + "");
        values.put("city_code", mApp.getCityName());
        if (TextUtils.isEmpty(KplusConstants.appChannel)) {
            KplusConstants.initData(ctx);
        }
        values.put("ref", KplusConstants.appChannel);

        MobclickAgent.onEvent(ctx, EVENT_ID, values);
        Lotuseed.onEvent(EVENT_ID, values);
    }
}
