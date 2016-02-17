package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.FWGetRequestResponse;
import com.kplus.car.model.response.request.FWGetRequestRequest;

/**
 * Created by Administrator on 2015/8/14.
 */
public class FWGetRequestTask extends AsyncTask<Void, Void, FWGetRequestResponse> {
    private KplusApplication mApplication;

    public FWGetRequestTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected FWGetRequestResponse doInBackground(Void... params) {
        FWGetRequestRequest req = new FWGetRequestRequest();
        req.setParams(null, mApplication.getId());
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
