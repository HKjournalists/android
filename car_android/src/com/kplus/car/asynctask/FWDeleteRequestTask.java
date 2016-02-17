package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.request.FWDeleteRequestRequest;

/**
 * Created by Administrator on 2015/10/21.
 */
public class FWDeleteRequestTask extends AsyncTask<String, Void, GetResultResponse> {
    private KplusApplication mApplication;

    public FWDeleteRequestTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected GetResultResponse doInBackground(String... params) {
        FWDeleteRequestRequest request = new FWDeleteRequestRequest();
        request.setParams(mApplication.getId(), params[0]);
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
