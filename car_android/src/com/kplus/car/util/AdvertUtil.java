package com.kplus.car.util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.kplus.car.KplusApplication;
import com.kplus.car.model.Advert;
import com.kplus.car.model.response.BooleanResultResponse;
import com.kplus.car.model.response.request.AdvertClickRequest;

import java.util.HashMap;
import java.util.Map;

public class AdvertUtil {
    private Context mContext;
    private Advert mAdvert;
    private KplusApplication mApp;
    private String type;

    public AdvertUtil(Context context, Advert advert, String type) {
        mContext = context;
        mAdvert = advert;
        this.type = type;
        mApp = (KplusApplication) ((Activity) mContext).getApplication();
    }

    public void OnAdvertClick() {
        if (null != mAdvert) {
            // 更新广告点击
            advertClick(mAdvert);
            Map<String, String> map = new HashMap<>();
            map.put("ad_id", mAdvert.getId() + "");
            EventAnalysisUtil.onEvent(mContext, "click_ad", "广告点击", map);

            String motionType = mAdvert.getMotionType();
            String motionValue = mAdvert.getMotionValue();

            if (!StringUtils.isEmpty(motionType) && !StringUtils.isEmpty(motionValue)) {
                // 点击的Action汽车服务也要用，统一处理
                ServicesActionUtil action = new ServicesActionUtil(mContext);
                action.onClickAction(motionType, motionValue);
            }
        }
    }

    private void advertClick(final Advert advertInfo) {
        new AsyncTask<Void, Void, BooleanResultResponse>() {
            @Override
            protected BooleanResultResponse doInBackground(Void... params) {
                AdvertClickRequest request = new AdvertClickRequest();
                try {
                    request.setParams(mApp.apkVersionCode, type, advertInfo.getId(), Long.parseLong(mApp.getCityId()), mApp.getUserId(), mApp.getId());
                    return mApp.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
