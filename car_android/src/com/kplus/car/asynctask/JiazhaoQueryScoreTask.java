package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.JiazhaoQueryScoreResponse;
import com.kplus.car.model.response.request.JiazhaoQueryScoreRequest;

/**
 * Created by Administrator on 2015/5/29.
 */
public class JiazhaoQueryScoreTask extends AsyncTask<String, Void, JiazhaoQueryScoreResponse> {
    private KplusApplication mApplication;

    public JiazhaoQueryScoreTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected JiazhaoQueryScoreResponse doInBackground(String... params) {
        JiazhaoQueryScoreRequest request = new JiazhaoQueryScoreRequest();
        String id = params[0];
        request.setParams(id, "android", mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
