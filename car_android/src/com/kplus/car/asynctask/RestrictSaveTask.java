package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.response.RestrictSaveResponse;
import com.kplus.car.model.response.request.RestrictSaveRequest;

/**
 * Created by Administrator on 2015/7/8.
 */
public class RestrictSaveTask extends AsyncTask<RemindRestrict, Void, RestrictSaveResponse> {
    private KplusApplication mApplication;

    public RestrictSaveTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected RestrictSaveResponse doInBackground(RemindRestrict... params) {
        RestrictSaveRequest request = new RestrictSaveRequest();
        RemindRestrict restrict = params[0];
        request.setParams(restrict, mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
