package com.kplus.car.model.response.request;

import com.kplus.car.model.response.RemindQueryResponse;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class RemindQueryRequest extends BaseRequest<RemindQueryResponse> {
    @Override
    public String getApiMethodName() {
        return "/remind/query.htm";
    }

    @Override
    public Class<RemindQueryResponse> getResponseClass() {
        return RemindQueryResponse.class;
    }

    public void setParams(String clientType, long uid) {
        if(uid != 0)
            addParams("uid", uid);
        addParams("clientType", clientType);
    }
}
