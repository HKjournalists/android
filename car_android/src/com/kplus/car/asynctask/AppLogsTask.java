package com.kplus.car.asynctask;

import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.carwash.utils.CNAppInfoUtil;
import com.kplus.car.model.AppLogBody;
import com.kplus.car.model.AppLogEvent;
import com.kplus.car.model.AppLogHeader;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.request.AppLogsRequest;
import com.kplus.car.util.MD5;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/9/21.
 */
public class AppLogsTask extends AsyncTask<Void, Void, GetResultResponse> {
    private KplusApplication mApplication;
    private String mEventLabel;
    private HashMap<String, String> mParams;

    public AppLogsTask(KplusApplication application, String eventLabel, HashMap<String, String> params){
        mApplication = application;
        mEventLabel = eventLabel;
        mParams = params;
    }

    @Override
    protected GetResultResponse doInBackground(Void... params) {
        try {
            AppLogsRequest request = new AppLogsRequest();
            AppLogHeader appLogHeader = new AppLogHeader();
            appLogHeader.setPackage_name("com.kplus.car");
            appLogHeader.setChannel(KplusConstants.appChannel);
            appLogHeader.setOs("Android");
            appLogHeader.setOs_version(Build.VERSION.RELEASE);
            appLogHeader.setDevice_model(Build.MODEL);
            appLogHeader.setApp_version(CNAppInfoUtil.getVersionName(mApplication));
            appLogHeader.setDevice_id(String.valueOf(mApplication.getUserId()));
            AppLogBody appLogBody = new AppLogBody();
            AppLogEvent appLogEvent = new AppLogEvent();
            String sessionId = MD5.md5(mApplication.getUserId() + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
            appLogEvent.setSession_id(sessionId);
            appLogEvent.setLabel(mEventLabel);
            appLogEvent.setParams(mParams);
            appLogBody.setEvent(appLogEvent);
            String header = new Gson().toJson(appLogHeader);
            String body = new Gson().toJson(appLogBody);
            request.setParams(header, body);
            return mApplication.client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
