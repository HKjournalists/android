package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.CouponExchangeResponse;
import com.kplus.car.model.response.request.CouponExchangeRequest;

/**
 * Created by Administrator on 2015/11/12.
 */
public class CouponExchangeTask extends AsyncTask<String, Void, CouponExchangeResponse> {
    private KplusApplication mApplication;

    public CouponExchangeTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected CouponExchangeResponse doInBackground(String... params) {
        CouponExchangeRequest req = new CouponExchangeRequest();
        req.setParams(params[0], mApplication.getId());
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
