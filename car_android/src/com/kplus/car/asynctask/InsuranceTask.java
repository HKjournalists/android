package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.InsuranceResponse;
import com.kplus.car.model.response.RemindQueryResponse;
import com.kplus.car.model.response.request.InsuranceRequest;
import com.kplus.car.model.response.request.RemindQueryRequest;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class InsuranceTask extends AsyncTask<Void, Void, InsuranceResponse> {
    private KplusApplication mApplication;

    public InsuranceTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected InsuranceResponse doInBackground(Void... voids) {
        InsuranceRequest req = new InsuranceRequest();
        try {
            return mApplication.client.execute(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
