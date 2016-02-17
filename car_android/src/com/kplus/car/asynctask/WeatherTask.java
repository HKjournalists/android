package com.kplus.car.asynctask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.kplus.car.KplusApplication;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.model.response.WeatherResponse;
import com.kplus.car.model.response.request.WeatherRequest;

/**
 * Description 请求天气的task
 * <br/><br/>Created by FU ZHIXUE on 2015/7/21.
 * <br/><br/>
 */
public class WeatherTask extends AsyncTask<Long, Void, WeatherResponse> {
    private static final String TAG = "WeatherTask";

    private KplusApplication mApplication = null;

    public WeatherTask(KplusApplication application) {
        mApplication = application;
    }

    @Override
    protected WeatherResponse doInBackground(Long... params) {
        long cityId = params[0];

        if (cityId == 0) {
            String strCityId = mApplication.getCityId();
            if (!TextUtils.isEmpty(strCityId)) {
                try {
                    cityId = Long.valueOf(strCityId);
                } catch (Exception e) {
                    Log.trace(TAG, "strCityId" + strCityId + "转为long失败!!!\r\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        WeatherRequest req = new WeatherRequest();
        req.setParams("android", cityId);
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            Log.trace(TAG, "调用接口返回异常-->\r\n" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
