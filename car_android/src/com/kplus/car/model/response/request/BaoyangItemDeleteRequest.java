package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BaoyangItemDeleteResponse;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemDeleteRequest extends BaseRequest<BaoyangItemDeleteResponse> {

    @Override
    public String getApiMethodName() {
        return "/remind/deletebaoyang.htm";
    }

    @Override
    public Class<BaoyangItemDeleteResponse> getResponseClass() {
        return BaoyangItemDeleteResponse.class;
    }

    public void setParams(int id, String clientType, long uid) {
        addParams("id", id);
        if (uid != 0) {
            addParams("uid", uid);
        }
        addParams("clientType", clientType);
    }
}