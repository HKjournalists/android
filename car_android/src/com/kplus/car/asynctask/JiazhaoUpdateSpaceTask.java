package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.request.JiazhaoUpdateSpaceRequest;

/**
 * Created by Administrator on 2015/5/29.
 */
public class JiazhaoUpdateSpaceTask extends AsyncTask<String, Void, GetResultResponse> {
    private KplusApplication mApplication;

    public JiazhaoUpdateSpaceTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected GetResultResponse doInBackground(String... params) {
        JiazhaoUpdateSpaceRequest request = new JiazhaoUpdateSpaceRequest();
        String id = params[0];
        request.setParams(id, true, "android", mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
