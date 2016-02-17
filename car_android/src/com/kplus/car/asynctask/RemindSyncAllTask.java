package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kplus.car.KplusApplication;
import com.kplus.car.model.BaoyangRecord;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.model.RemindChedai;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.response.request.RemindSyncRequest;

import java.util.List;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class RemindSyncAllTask extends AsyncTask<Void, Void, Void> {
    private KplusApplication mApplication;

    public RemindSyncAllTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            RemindSyncRequest request = new RemindSyncRequest();
            List<RemindInfo> info = mApplication.dbCache.getRemindInfo();
            String data = new Gson().toJson(info);
            request.setParams(data, 6, "android", mApplication.getId());
            mApplication.client.executePost(request);
            request = new RemindSyncRequest();
            List<RemindChexian> chexian = mApplication.dbCache.getRemindChexian();
            data = new Gson().toJson(chexian);
            request.setParams(data, 2, "android", mApplication.getId());
            mApplication.client.executePost(request);
            request = new RemindSyncRequest();
            List<RemindBaoyang> baoyang = mApplication.dbCache.getRemindBaoyang();
            data = new Gson().toJson(baoyang);
            request.setParams(data, 3, "android", mApplication.getId());
            mApplication.client.executePost(request);
            request = new RemindSyncRequest();
            List<RemindNianjian> nianjian = mApplication.dbCache.getRemindNianjian();
            data = new Gson().toJson(nianjian);
            request.setParams(data, 4, "android", mApplication.getId());
            mApplication.client.executePost(request);
            request = new RemindSyncRequest();
            List<RemindChedai> chedai = mApplication.dbCache.getRemindChedai();
            data = new Gson().toJson(chedai);
            request.setParams(data, 5, "android", mApplication.getId());
            mApplication.client.executePost(request);
            List<RemindCustom> custom = mApplication.dbCache.getRemindCustom();
            data = new Gson().toJson(custom);
            request.setParams(data, 0, "android", mApplication.getId());
            mApplication.client.executePost(request);
            List<BaoyangRecord> record = mApplication.dbCache.getBaoyangRecord();
            data = new Gson().toJson(record);
            request.setParams(data, -1, "android", mApplication.getId());
            mApplication.client.executePost(request);
        }catch (Exception e){}
        return null;
    }
}
