package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.request.RestrictDeleteRequest;

/**
 * Created by Administrator on 2015/5/29.
 */
public class RestrictDeleteTask extends AsyncTask<String, Void, GetResultResponse> {
    private KplusApplication mApplication;

    public RestrictDeleteTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected GetResultResponse doInBackground(String... params) {
        RestrictDeleteRequest request = new RestrictDeleteRequest();
        String id = params[0];
        request.setParams(id, mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
