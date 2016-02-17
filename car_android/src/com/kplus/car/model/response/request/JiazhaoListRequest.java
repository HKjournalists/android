package com.kplus.car.model.response.request;

import com.kplus.car.model.response.JiazhaoListResponse;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class JiazhaoListRequest extends BaseRequest<JiazhaoListResponse> {
    @Override
    public String getApiMethodName() {
        return "/jiazhao/list.htm";
    }

    @Override
    public Class<JiazhaoListResponse> getResponseClass() {
        return JiazhaoListResponse.class;
    }

    public void setParams(String clientType, long uid) {
        if(uid != 0)
            addParams("uid", uid);
        addParams("clientType", clientType);
    }
}
