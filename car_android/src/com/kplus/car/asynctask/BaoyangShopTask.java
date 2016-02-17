package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.BaoyangShopResponse;
import com.kplus.car.model.response.request.BaoyangShopRequest;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangShopTask extends AsyncTask<String, Void, BaoyangShopResponse> {
    private KplusApplication mApplication;

    public BaoyangShopTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected BaoyangShopResponse doInBackground(String... params) {
        BaoyangShopRequest req = new BaoyangShopRequest();
        String lat = null;
        String lon = null;
        if (mApplication.getLocation() != null){
            lat = String.valueOf(mApplication.getLocation().getLatitude());
            lon = String.valueOf(mApplication.getLocation().getLongitude());
        }
        req.setParams(params[0], mApplication.getCityId(), null, lat, lon, params[1]);
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
