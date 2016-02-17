package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.JiazhaoListResponse;
import com.kplus.car.model.response.request.JiazhaoListRequest;

/**
 * Created by Administrator on 2015/5/29.
 */
public class JiazhaoListTask extends AsyncTask<Void, Void, JiazhaoListResponse> {
    private KplusApplication mApplication;

    public JiazhaoListTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected JiazhaoListResponse doInBackground(Void... params) {
        JiazhaoListRequest req = new JiazhaoListRequest();
        req.setParams("android", mApplication.getId());
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
