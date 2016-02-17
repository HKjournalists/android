package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.GetMessageCountResponse;
import com.kplus.car.model.response.request.GetMessageCountRequest;

/**
 * Created by Administrator on 2015/6/24.
 */
public class GetMessageCountTask extends AsyncTask<Void, Void, GetMessageCountResponse> {
    private KplusApplication mApplication;

    public GetMessageCountTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected GetMessageCountResponse doInBackground(Void... params) {
        try{
            GetMessageCountRequest request = new GetMessageCountRequest();
            request.setParams(mApplication.getCityId(), mApplication.getId());
            return mApplication.client.execute(request);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
