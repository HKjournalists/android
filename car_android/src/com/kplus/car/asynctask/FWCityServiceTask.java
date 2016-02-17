package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.FWCityServiceResponse;
import com.kplus.car.model.response.request.FWCityServiceRequest;

/**
 * Created by Administrator on 2015/8/12.
 */
public class FWCityServiceTask extends AsyncTask<Void, Void, FWCityServiceResponse> {
    private KplusApplication mApplication;

    public FWCityServiceTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected FWCityServiceResponse doInBackground(Void... params) {
        FWCityServiceRequest req = new FWCityServiceRequest();
        req.setParams(mApplication.getCityId());
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
