package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.common.Const;

/**
 * 打日志的工具类
 *
 * @author Fu Zhixue
 * @date 2015/4/28.
 */
public class Log {
    /**
     * Log Tag
     */
    private static final String TAG = "kplusCar";

    /**
     * debug mode
     */
    private static boolean bDebug = true;

    /**
     * Debug trace
     * @param TAG
     * @param content
     */
    public static void trace(String TAG, String content) {
        if (isLoggable() && bDebug)
            android.util.Log.d(Log.TAG, TAG + " : " + content);
    }

    /**
     * Debug trace
     * @param TAG
     * @param content
     * @param tr
     */
    public static void trace(String TAG, String content, Throwable tr) {
        if (isLoggable() && bDebug)
            android.util.Log.d(Log.TAG, TAG + " : " + content, tr);
    }

    /**
     * exception print
     * @param TAG
     * @param content
     * @param force
     */
    public static void trace(String TAG, String content, boolean force) {
        if (isLoggable() && (force || bDebug))
            android.util.Log.d(Log.TAG, TAG + " : " + content);
    }

    /**
     *
     * @return true 才会打日志，如果是debug模式下，是一直打日志的
     */
    private static boolean isLoggable() {
        if (Const.IS_DEBUG) {
            // 一直打印日志
            return true;
        }
        // 在命令行中设置之后可打印日志
        return android.util.Log.isLoggable(TAG, android.util.Log.DEBUG);
    }
}
