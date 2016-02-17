package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.model.Weather;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.WeatherUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/21.
 * <br/><br/>
 */
public class WeatherView extends LinearLayout implements ClickUtils.NoFastClickListener {

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private IWeatherWashingCallback mCallback = null;

    private int total_Col = 4;
    private int mWidth = 0;
    private List<Weather> mWeathers = null;

    private boolean isClickItem = true;

    public void setIsClickItem(boolean isClickItem) {
        this.isClickItem = isClickItem;
    }

    public WeatherView(Context context) {
        this(context, null);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    private <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    public void setWeatherData(List<Weather> weathers, IWeatherWashingCallback callback) {
        mCallback = callback;

        if (null == weathers || weathers.isEmpty()) {
            return;
        }

        if (null != mWeathers) {
            mWeathers.clear();
        }
        mWeathers = new ArrayList<>(weathers);

        total_Col = weathers.size();
        int sWidth = KplusApplication.getInstance().getnScreenWidth();
        mWidth = sWidth / total_Col;

        this.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth, LayoutParams.MATCH_PARENT);

        // 温度
        String temperatureMsg = "%1$s-%2$s℃";

        for (int i = 0; i < mWeathers.size(); i++) {
            Weather weather = mWeathers.get(i);

            LinearLayout llCellItem = (LinearLayout) mInflater.inflate(R.layout.weather_item_layout, null, false);
            llCellItem.setLayoutParams(params);
            llCellItem.setTag(i);
            if (isClickItem) {
                ClickUtils.setNoFastClickListener(llCellItem, this);
            }

            TextView tvDay = findView(llCellItem, R.id.tvDay);
            ImageView ivWeatherIcon = findView(llCellItem, R.id.ivWeatherIcon);
            TextView tvWeather = findView(llCellItem, R.id.tvWeather);
            TextView tvTemperature = findView(llCellItem, R.id.tvTemperature);
            TextView tvWashDescr = findView(llCellItem, R.id.tvWashDescr);

            tvDay.setText(weather.getDateStr());
            // Notice 要设置天气图标   灰色图标
            int iconResId = WeatherUtil.getWeatherIcon(weather.getPhenomenon(), true);
            ivWeatherIcon.setImageResource(iconResId);

            tvWeather.setText(weather.getPhenomenon());

            String temperText = String.format(temperatureMsg, weather.getTemperatureNight(), weather.getTemperatureDay());
            tvTemperature.setText(temperText);

            long wash = weather.getWash();

            String strWashIndex = WeatherUtil.getWashIndexText(wash);
            tvWashDescr.setText(strWashIndex);

            int textColor = WeatherUtil.getWashIndexTextColor(wash);
            tvWashDescr.setTextColor(textColor);

            int bgColor;
            if (i % 2 == 0) {
                bgColor = R.color.daze_white1;
            } else {
                bgColor = R.color.daze_white;
            }
            bgColor = mContext.getResources().getColor(bgColor);
            llCellItem.setBackgroundColor(bgColor);

            if (i == 0 && weather.getType() == 1) {
                // 返回当天的天气到外面
                if (null != mCallback) {
                    mCallback.onWeather(weather);
                }
            }

            this.addView(llCellItem, i);
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.llCellItem:
                if (null != v.getTag()) {
                    int position = (int) v.getTag();
                    Weather weather = mWeathers.get(position);
                    if (null != mCallback) {
                        mCallback.onWeather(weather);
                    }
                }
                break;
        }
    }

    public interface IWeatherWashingCallback {
        void onWeather(Weather weather);
    }
}
