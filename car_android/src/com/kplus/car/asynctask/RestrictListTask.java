package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.RestrictListResponse;
import com.kplus.car.model.response.request.RestrictListRequest;

/**
 * Created by Administrator on 2015/7/9.
 */
public class RestrictListTask extends AsyncTask<Void, Void, RestrictListResponse> {
    private KplusApplication mApplication;

    public RestrictListTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected RestrictListResponse doInBackground(Void... params) {
        RestrictListRequest request = new RestrictListRequest();
        request.setParams(mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
