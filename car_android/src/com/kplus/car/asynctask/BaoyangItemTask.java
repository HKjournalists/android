package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.BaoyangItemResponse;
import com.kplus.car.model.response.request.BaoyangItemRequest;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemTask extends AsyncTask<Void, Void, BaoyangItemResponse> {

    private KplusApplication mApplication;

    public BaoyangItemTask(KplusApplication application) {
        mApplication = application;
    }


    @Override
    protected BaoyangItemResponse doInBackground(Void... params) {
        BaoyangItemRequest req = new BaoyangItemRequest();
        req.setParams("android", mApplication.getId());
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
