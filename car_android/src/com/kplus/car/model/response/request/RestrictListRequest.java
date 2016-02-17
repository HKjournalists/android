package com.kplus.car.model.response.request;

import com.kplus.car.model.response.RestrictListResponse;

/**
 * Created by Administrator on 2015/7/9.
 */
public class RestrictListRequest extends BaseRequest<RestrictListResponse> {
    @Override
    public String getApiMethodName() {
        return "/restrict/list.htm";
    }

    @Override
    public Class<RestrictListResponse> getResponseClass() {
        return RestrictListResponse.class;
    }

    public void setParams(long uid){
        if(uid != 0)
            addParams("uid", uid);
    }
}
