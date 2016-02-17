package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kplus.car.KplusApplication;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.response.JiazhaoSaveResponse;
import com.kplus.car.model.response.request.JiazhaoSaveRequest;

/**
 * Created by Administrator on 2015/5/29.
 */
public class JiazhaoSaveTask extends AsyncTask<Jiazhao, Void, JiazhaoSaveResponse> {
    private KplusApplication mApplication;

    public JiazhaoSaveTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected JiazhaoSaveResponse doInBackground(Jiazhao... params) {
        JiazhaoSaveRequest request = new JiazhaoSaveRequest();
        Jiazhao jiazhao = params[0];
        request.setParams(jiazhao, "android", mApplication.getId());
        try {
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
