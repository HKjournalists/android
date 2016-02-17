package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.BaoyangBrandResponse;
import com.kplus.car.model.response.request.BaoyangBrandRequest;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangBrandTask extends AsyncTask<Void, Void, BaoyangBrandResponse> {
    private KplusApplication mApplication;

    public BaoyangBrandTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected BaoyangBrandResponse doInBackground(Void... params) {
        BaoyangBrandRequest req = new BaoyangBrandRequest();
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
