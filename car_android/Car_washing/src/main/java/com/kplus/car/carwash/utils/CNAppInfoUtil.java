package com.kplus.car.carwash.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Description：获取应用、设置信息
 * <br/><br/>Created by FU ZHIXUE on 2015/8/17.
 * <br/><br/>
 */
public final class CNAppInfoUtil {
    private static final String TAG = "CNAppInfoUtil";

    private CNAppInfoUtil() {
    }

    /**
     * 获取设备唯一标示
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    /**
     * 获取设备的生成厂家、机型及系统的版本
     *
     * @return
     */
    public static String getDeviceModelAndVersion() {
        String release = Build.VERSION.RELEASE;// 固件版本
        String manufacture = Build.MANUFACTURER;// 生产厂家
        String model = Build.MODEL;// 机型

        String strInfo = manufacture + "," + model + "," + release;
        Log.trace(TAG, strInfo);
        strInfo = manufacture + "," + model;
        return strInfo;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取内测版本code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取ip
     *
     * @param context
     * @return
     */
    public static String getIp(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ip = null;
        if (wifiInfo != null) {
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        }
        if (TextUtils.isEmpty(ip)) {
            // 使用GPRS
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
        }
        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

}
