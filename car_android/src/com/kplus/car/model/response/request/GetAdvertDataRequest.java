package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetAdvertDataResponse;
import com.kplus.car.util.StringUtils;

public class GetAdvertDataRequest extends BaseRequest<GetAdvertDataResponse> {
    @Override
    public String getApiMethodName() {
        return "/advert/getData.htm";
    }

    @Override
    public Class<GetAdvertDataResponse> getResponseClass() {
        return GetAdvertDataResponse.class;
    }

    public void setParams(long cityId, long userId, long uid, String identity){
        addParams("clientType", "android");
        if(cityId != 0)
            addParams("cityId", cityId);
        addParams("userId", userId);
        if(uid != 0)
            addParams("uid", uid);
        if(!StringUtils.isEmpty(identity))
            addParams("identity", identity);
    }
}
