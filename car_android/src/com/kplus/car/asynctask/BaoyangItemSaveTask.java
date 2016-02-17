package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.BaoyangItemSaveResponse;
import com.kplus.car.model.response.request.BaoyangItemSaveRequest;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemSaveTask extends AsyncTask<String, Void, BaoyangItemSaveResponse> {
    private KplusApplication mApplication;

    public BaoyangItemSaveTask(KplusApplication application) {
        mApplication = application;
    }

    @Override
    protected BaoyangItemSaveResponse doInBackground(String... params) {
        BaoyangItemSaveRequest request = new BaoyangItemSaveRequest();
        String item = params[0];
        request.setParams(item, "android", mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
