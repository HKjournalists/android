package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.BaoyangItemDeleteResponse;
import com.kplus.car.model.response.request.BaoyangItemDeleteRequest;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemDeleteTask extends AsyncTask<Integer, Void, BaoyangItemDeleteResponse> {

    private KplusApplication mApplication;

    public BaoyangItemDeleteTask(KplusApplication application) {
        mApplication = application;
    }

    @Override
    protected BaoyangItemDeleteResponse doInBackground(Integer... params) {
        BaoyangItemDeleteRequest request = new BaoyangItemDeleteRequest();
        Integer id = params[0];
        request.setParams(id, "android", mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
