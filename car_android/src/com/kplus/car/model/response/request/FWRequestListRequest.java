package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWRequestListResponse;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWRequestListRequest extends BaseRequest<FWRequestListResponse> {
    @Override
    public String getApiMethodName() {
        return "/serviceRequest/request/list.htm";
    }

    @Override
    public Class<FWRequestListResponse> getResponseClass() {
        return FWRequestListResponse.class;
    }

    public void setParams(long uid, long lastUpdateTime){
        addParams("uid", uid);
        if (lastUpdateTime != 0)
            addParams("lastUpdateTime", lastUpdateTime);
    }
}
