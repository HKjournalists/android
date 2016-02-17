package com.kplus.car.model.response.request;

import android.os.Build;

import com.kplus.car.BuildConfig;
import com.kplus.car.KplusApplication;
import com.kplus.car.model.response.BooleanResultResponse;

public class AdvertClickRequest extends BaseRequest<BooleanResultResponse> {
    @Override
    public String getApiMethodName() {
        return "/advert/click.htm";
    }

    @Override
    public Class<BooleanResultResponse> getResponseClass() {
        return BooleanResultResponse.class;
    }

    public void setParams(int clientVersion, String identity, long advertId, long cityId, long userId, long uid){
        addParams("clientType", "android");
        addParams("clientVersion",clientVersion);
        addParams("identity", identity);
        addParams("advertId", advertId);
        addParams("cityId", cityId);
        addParams("userId", userId);
        if(uid != 0)
            addParams("uid", uid);
    }
}
