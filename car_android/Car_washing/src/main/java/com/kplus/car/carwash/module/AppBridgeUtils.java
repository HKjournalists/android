package com.kplus.car.carwash.module;

import android.app.Activity;
import android.content.Context;

import com.kplus.car.carwash.bean.CNWeather;
import com.kplus.car.carwash.callback.IAppBridgeListener;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 与主程序中间获取数据处理的工具类
 * Created by Fu on 2015/5/20.
 */
public final class AppBridgeUtils {

    private IAppBridgeListener mBridgeListener;

    private BigDecimal mUserBalance;
    private String mMobile;
    private String mLicense;

    public void setBridgeListener(IAppBridgeListener listener) {
        mBridgeListener = listener;
    }

    private static AppBridgeUtils ins;

    public static AppBridgeUtils getIns() {
        if (null == ins) {
            synchronized (AppBridgeUtils.class) {
                ins = new AppBridgeUtils();
            }
        }
        return ins;
    }

    private AppBridgeUtils() {
    }

    public void setUserBalance(BigDecimal balance) {
        if (null == balance) {
            balance = new BigDecimal(0);
        }
        mUserBalance = balance;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public BigDecimal getUserBalance() {
        if (null == mUserBalance) {
            mUserBalance = new BigDecimal(0);
        }
        return mUserBalance;
    }

    public void setCarLicense(String license) {
        mLicense = license;
    }

    public String getMobile() {
        return mMobile;
    }

    public String getLicense() {
        return mLicense;
    }

    public void onPay(Context context, long fromOrderId, int payment) {
        mBridgeListener.pay(context, fromOrderId, payment);
    }

    public boolean isLog() {
        return mBridgeListener.isLog();
    }

    public long getUid() {
        return mBridgeListener.getUid();
    }

    public long getPid() {
        return mBridgeListener.getPid();
    }

    public long getUserId() {
        return mBridgeListener.getUserId();
    }

    public void getUserInfo(Context context) {
        mBridgeListener.getUserInfo(context);
    }

    public boolean doShareWeChatMoment(Context context, int shareContentType, String webPageUrl, String content, String imagePath, String thumImagePath) {
        return mBridgeListener.doShareWeChatMoment(context, shareContentType, webPageUrl, content, imagePath, thumImagePath);
    }

    public boolean doShareWeCharFriend(Context context, int shareContentType, String webPageUrl, String content, String imagePath, String thumImagePath) {
        return mBridgeListener.doShareWeCharFriend(context, shareContentType, webPageUrl, content, imagePath, thumImagePath);
    }

    public void agentOnStart(Activity act) {
        mBridgeListener.agentOnStart(act);
    }

    public void agentOnResume(Activity act) {
        mBridgeListener.agentOnResume(act);
    }

    public void agentOnPause(Activity act) {
        mBridgeListener.agentOnPause(act);
    }

    public void agentOnStop(Activity act) {
        mBridgeListener.agentOnStop(act);
    }

    public void onEvent(Context context, String eventId) {
        mBridgeListener.onEvent(context, eventId);
    }

    public void onEvent(Context context, String eventId, String label) {
        mBridgeListener.onEvent(context, eventId, label);
    }

    public void onEvent(Context context, String eventId, String label, Map<String, String> map) {
        mBridgeListener.onEvent(context, eventId, label, map);
    }

    public String getCarWashingApiUrl() {
        return mBridgeListener.getCarWashingApiUrl();
    }

    public String getClientAppKey() {
        return mBridgeListener.getClientAppKey();
    }

    public String getClientAppSecret() {
        return mBridgeListener.getClientAppSecret();
    }

    public double getLongitude() {
        return mBridgeListener.getLongitude();
    }

    public double getLatitude() {
        return mBridgeListener.getLatitude();
    }

    /**
     * 外面定位到的城市
     *
     * @return 定位的城市名
     */
    public String getCity() {
        return mBridgeListener.getCity();
    }

    /**
     * 外面选择的城市
     *
     * @return 选择的城市名
     */
    public String getSelectedCity() {
        return mBridgeListener.getSelectedCity();
    }

    public long getSelectedCityId() {
        return mBridgeListener.getSelectedCityId();
    }

    public List<CNWeather> getWeather(Context context, long cityId) {
        long selectedCityId = getSelectedCityId();
        // 如果选择的城市与主程序中选择的城市不相同
        if (selectedCityId != cityId) {
            getWeatherForServiceAsy(context, cityId);
            return null;
        }
        // 如果没有从本地获取到天气从服务器上获取
        List<CNWeather> weathers = mBridgeListener.getWeatherForLocal(cityId);
        if (null == weathers || weathers.isEmpty()) {
            getWeatherForServiceAsy(context, cityId);
        }
        return weathers;
    }

    public void getWeatherForServiceAsy(Context context, long cityId) {
        mBridgeListener.getWeatherForServiceAsy(context, cityId);
    }

    public boolean isFromInApp() {
        return mBridgeListener.isFromInApp();
    }
}
