package com.kplus.car.asynctask;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.kplus.car.KplusApplication;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.model.response.CarServicesResponse;
import com.kplus.car.model.response.request.CarServicesRequest;

/**
 * Description：获取汽车服务Task
 * <br/><br/>Created by FU ZHIXUE on 2015/8/13.
 * <br/><br/>
 */
public class CarServicesTask extends AsyncTask<Long, Void, CarServicesResponse> {

    private static final String TAG = "CarServicesTask";

    private KplusApplication mApplication = null;

    public CarServicesTask(KplusApplication application) {
        mApplication = application;
    }

    @Override
    protected CarServicesResponse doInBackground(Long... params) {
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

        if (cityId == 0) {
            cityId = 92;
        }

        CarServicesRequest req = new CarServicesRequest();
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
