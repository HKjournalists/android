package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.WeatherActivity;
import com.kplus.car.model.Weather;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.WeatherUtil;

/**
 * Created by Administrator on 2015/7/22.
 */
public class WeatherViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private Weather mWeather;
    private Context mContext;
    private KplusApplication mApp;
    private TextView mPhenomenon;
    private TextView mTemperature;
    private TextView mWashDesc;
    private ImageView mIcon;

    public WeatherViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mPhenomenon = (TextView) itemView.findViewById(R.id.phenomenon);
        mTemperature = (TextView) itemView.findViewById(R.id.temperature);
        mWashDesc = (TextView) itemView.findViewById(R.id.wash_desc);
        mIcon = (ImageView) itemView.findViewById(R.id.icon);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.layout), this);
    }

    public void bind(){
        mWeather = mApp.dbCache.getWeather(1);
        updateUI();
    }

    private void updateUI(){
        if (mWeather != null){
            mPhenomenon.setText(mWeather.getPhenomenon());
            mTemperature.setText(mWeather.getTemperatureNight() + "-" + mWeather.getTemperatureDay() + "℃");
//            mWashDesc.setText(mWeather.getWashDescr());
            mWashDesc.setText(WeatherUtil.getHomeWashText(mWeather.getWash()));
            mIcon.setImageResource(WeatherUtil.getWeatherIcon(mWeather.getPhenomenon(), true));
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.layout:
                EventAnalysisUtil.onEvent(mContext, "click_weather", "点击天气页面", null);
                Intent it = new Intent(mContext, WeatherActivity.class);
                mContext.startActivity(it);
                break;
        }
    }
}
