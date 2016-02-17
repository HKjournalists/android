package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetMessageResponse;
import com.kplus.car.model.response.request.GetMessageRequest;

/**
 * Created by Administrator on 2015/6/24.
 */
public class GetMessageTask extends AsyncTask<Void, Void, GetMessageResponse> {
    private KplusApplication mApplication;

    public GetMessageTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected GetMessageResponse doInBackground(Void... params) {
        try{
            GetMessageRequest request = new GetMessageRequest();
            request.setParams(mApplication.getCityId(), mApplication.getId());
            return mApplication.client.execute(request);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
