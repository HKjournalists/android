package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.utils.CNCitiesUtil;
import com.kplus.car.carwash.utils.CNInitializeApiDataUtil;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.model.Weather;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.WeatherUtil;
import com.kplus.car.widget.WeatherView;

import java.util.List;

/**
 * Description 天气、洗车指数界面
 * <br/><br/>Created by FU ZHIXUE on 2015/7/20.
 * <br/><br/>
 */
public class WeatherActivity extends BaseActivity implements ClickUtils.NoFastClickListener, WeatherView.IWeatherWashingCallback {
    private static final String TAG = "WeatherActivity";

    private Context mContext = null;

    private RelativeLayout llWeatherTop = null;
    private TextView tvDay = null;
    private ImageView ivWeatherIcon = null;
    private TextView tvTemperature = null;
    private TextView tvWashDescr = null;
    private TextView tvWashing = null;
    private LinearLayout llWashingTips = null;
    private WeatherView llWeather = null;

    private NotfiyBroadcast mBroadcast = null;
    private String mActionType = "";

    @Override
    protected void initView() {
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mActionType = bundle.getString("actionType");
        }

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_weather_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("洗车指数");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        llWeatherTop = findView(R.id.llWeatherTop);
        tvDay = findView(R.id.tvDay);
        ivWeatherIcon = findView(R.id.ivWeatherIcon);
        tvTemperature = findView(R.id.tvTemperature);
        tvWashDescr = findView(R.id.tvWashDescr);
        tvWashing = findView(R.id.tvWashing);
        llWashingTips = findView(R.id.llWashingTips);
        llWeather = findView(R.id.llWeather);

        // 设置不能点击下面的天气
        llWeather.setIsClickItem(false);
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 城市是否开通上门洗车显示ui
     *
     * @param hasWashingService
     */
    private void setCityHasWashingService(boolean hasWashingService) {
        if (hasWashingService) {
            tvWashing.setVisibility(View.VISIBLE);
            llWashingTips.setVisibility(View.GONE);
        } else {
            tvWashing.setVisibility(View.GONE);
            llWashingTips.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void loadData() {
        getLocServiceCity();

        // 获取天气数据
        List<Weather> weathers = mApplication.dbCache.getWeathers();
        WeatherUtil.sortWeatherForDay(weathers);
        llWeather.setWeatherData(weathers, this);
    }

    @Override
    public void onWeather(Weather weather) {
        tvDay.setText(weather.getDateStr());
        // Notice 要设置天气图标
        int iconResId = WeatherUtil.getWeatherIcon(weather.getPhenomenon(), false);
        ivWeatherIcon.setImageResource(iconResId);

        // 温度
        String temperatureMsg = "%1$s-%2$s℃/" + weather.getPhenomenon();
        String temperText = String.format(temperatureMsg, weather.getTemperatureNight(), weather.getTemperatureDay());
        tvTemperature.setText(temperText);

        long wash = weather.getWash();

        String strWashIndex = WeatherUtil.getWashIndexText(wash);
        tvWashDescr.setText(strWashIndex);

        String strWash = WeatherUtil.getWashText(wash);
        tvWashing.setText(strWash);
        int textColor = WeatherUtil.getTextColorOfToday(wash);
        tvWashing.setTextColor(getResources().getColor(textColor));

        int bgResId = WeatherUtil.getBgResIdOfToday(wash);
        llWeatherTop.setBackgroundResource(bgResId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播
        IntentFilter filter = getStickyIntentFilter();
        mBroadcast = new NotfiyBroadcast();
        registerReceiver(mBroadcast, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mBroadcast) {
            unregisterReceiver(mBroadcast);
            mBroadcast = null;
        }
        CNProgressDialogUtil.dismissProgress(mContext);
    }

    private void getLocServiceCity() {
        List<City> cities = CNCitiesUtil.getIns().getCities();
        if (null == cities || cities.isEmpty()) {
            CNProgressDialogUtil.showProgress(mContext, true, "请稍候...");
        } else {
            getWashingCities();
        }
        // 获取城市数据
        CNInitializeApiDataUtil.getIns().fetchCities(mContext);
    }

    private long getCityId() {
        long cityId = 0;
        String strCityId = mApplication.getCityId();

        if (!TextUtils.isEmpty(strCityId)) {
            try {
                cityId = Long.valueOf(strCityId);
            } catch (Exception e) {
                Log.trace(TAG, "strCityId" + strCityId + "转为long失败!!!\r\n" + e.getMessage());
                e.printStackTrace();
            }
        }
        return cityId;
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(tvWashing, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.tvWashing: // 跳转到洗车界面
                if (KplusApplication.getInstance().isUserLogin(true, "上门洗车需要绑定手机号")) {
                    EventAnalysisUtil.onEvent(mContext, "click_orderWashCar", "点击天气页面后，点击预约洗车", null);
                    CNCarWashingLogic.startCarWashingActivity(mContext, false);
                }
                break;
        }
    }

    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_CITIES_DATA_ACTION);
        return filter;
    }

    private class NotfiyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;

            String action = intent.getAction();
            /**
             * 获取城市回来了
             */
            if (CustomBroadcast.ON_CITIES_DATA_ACTION.equals(action)) {
                boolean isResult = intent.getBooleanExtra("result", false);
                if (isResult) {
                    // 成功
                    getWashingCities();
                } else {
                    // 失败
                    // 接口取失败 要占位置
                    tvWashing.setVisibility(View.GONE);
                    llWashingTips.setVisibility(View.INVISIBLE);
                }
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }
    }

    private void getWashingCities() {
        List<City> cities = CNCitiesUtil.getIns().getCities();
        boolean hasWashingService = false;
        if (null != cities) {
            long cityId = getCityId();
            final String cityName = mApplication.getCityName();
            for (City c : cities) {
                if (c.getId() == cityId && c.getName().equals(cityName)) {
                    // 找到相同城市，该城市已开通上门洗车服务
                    hasWashingService = true;
                    break;
                }
            }
        }
        setCityHasWashingService(hasWashingService);
        CNProgressDialogUtil.dismissProgress(mContext);
    }
}
