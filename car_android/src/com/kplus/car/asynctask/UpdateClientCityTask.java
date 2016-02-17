package com.kplus.car.asynctask;

import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.request.JiazhaoDeleteRequest;
import com.kplus.car.model.response.request.UpdateClientCityRequest;
import com.kplus.car.util.StringUtils;

/**
 * Created by Administrator on 2015/5/29.
 */
public class UpdateClientCityTask extends AsyncTask<Void, Void, GetResultResponse> {
    private KplusApplication mApplication;

    public UpdateClientCityTask(KplusApplication application){
        mApplication = application;
    }

    @Override
    protected GetResultResponse doInBackground(Void... params) {
        try {
            UpdateClientCityRequest request = new UpdateClientCityRequest();
            String cityId = mApplication.getCityId();
            long nCityId = 0;
            if (!StringUtils.isEmpty(cityId))
                nCityId = Long.parseLong(cityId);
            request.setParams(mApplication.getUserId(), mApplication.getId(), nCityId);
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
