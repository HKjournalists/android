package com.kplus.car.model.response.request;

import com.kplus.car.model.response.JiazhaoAgainstListResponse;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class JiazhaoAgainstListRequest extends BaseRequest<JiazhaoAgainstListResponse> {
    @Override
    public String getApiMethodName() {
        return "/jiazhao/queryAgainstRecordList.htm";
    }

    @Override
    public Class<JiazhaoAgainstListResponse> getResponseClass() {
        return JiazhaoAgainstListResponse.class;
    }

    public void setParams(String clientType, long uid) {
        if(uid != 0)
            addParams("uid", uid);
        addParams("clientType", clientType);
    }
}
