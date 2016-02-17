package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BaoyangItemResponse;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemRequest extends BaseRequest<BaoyangItemResponse> {
    @Override
    public String getApiMethodName() {
        return "/remind/baoyang.htm";
    }

    @Override
    public Class<BaoyangItemResponse> getResponseClass() {
        return BaoyangItemResponse.class;
    }

    public void setParams(String clientType, long uid) {
        if (uid != 0) {
            addParams("uid", uid);
        }
        addParams("clientType", clientType);
    }
}
