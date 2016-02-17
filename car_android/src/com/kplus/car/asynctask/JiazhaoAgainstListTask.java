package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.JiazhaoAgainstListResponse;
import com.kplus.car.model.response.request.JiazhaoAgainstListRequest;

/**
 * Created by Administrator on 2015/5/29.
 */
public class JiazhaoAgainstListTask extends AsyncTask<Void, Void, JiazhaoAgainstListResponse> {
    private KplusApplication mApplication;

    public JiazhaoAgainstListTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected JiazhaoAgainstListResponse doInBackground(Void... params) {
        JiazhaoAgainstListRequest req = new JiazhaoAgainstListRequest();
        req.setParams("android", mApplication.getId());
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
