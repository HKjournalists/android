package com.kplus.car.carwash.callback;

import android.app.Activity;
import android.content.Context;

import com.kplus.car.carwash.bean.CNWeather;

import java.util.List;
import java.util.Map;

/**
 * Created by Fu on 2015/5/20.
 */
public interface IAppBridgeListener {

    boolean isLog();

    String getCarWashingApiUrl();

    void pay(Context context, long orderId, int payment);

    void getUserInfo(Context context);

    boolean doShareWeChatMoment(Context context, int shareContentType, String webPageUrl, String content, String imagePath, String thumImagePath);

    boolean doShareWeCharFriend(Context context, int shareContentType, String webPageUrl, String content, String imagePath, String thumImagePath);

    void  agentOnStart(Activity act);

    void agentOnResume(Activity act);

    void agentOnPause(Activity act);

    void agentOnStop(Activity act);

    void onEvent(Context context, String eventId);

    void onEvent(Context context, String eventId, String label);

    void onEvent(Context context, String eventId, String label, Map<String, String> map);

    String getClientAppKey();

    String getClientAppSecret();

    long getPid();

    long getUid();

    long getUserId();

    double getLongitude();

    double getLatitude();

    String getCity();

    String getSelectedCity();

    long getSelectedCityId();

    List<CNWeather> getWeatherForLocal(long cityId);

    void getWeatherForServiceAsy(Context context, long cityId);

    boolean isFromInApp();
}
