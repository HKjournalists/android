package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.RemindQueryResponse;
import com.kplus.car.model.response.request.RemindQueryRequest;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class RemindQueryTask extends AsyncTask<Void, Void, RemindQueryResponse> {
    private KplusApplication mApplication;

    public RemindQueryTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected RemindQueryResponse doInBackground(Void... voids) {
        RemindQueryRequest req = new RemindQueryRequest();
        req.setParams("android", mApplication.getId());
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
