package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.FWProviderListResponse;
import com.kplus.car.model.response.request.FWProviderListRequest;

/**
 * Created by Administrator on 2015/8/14.
 */
public class FWProviderListTask extends AsyncTask<String, Void, FWProviderListResponse> {
    private KplusApplication mApplication;

    public FWProviderListTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected FWProviderListResponse doInBackground(String... params) {
        FWProviderListRequest req = new FWProviderListRequest();
        req.setParams(params[0]);
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
