package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BaoyangItemSaveResponse;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemSaveRequest extends BaseRequest<BaoyangItemSaveResponse> {

    @Override
    public String getApiMethodName() {
        return "/remind/savebaoyang.htm";
    }

    @Override
    public Class<BaoyangItemSaveResponse> getResponseClass() {
        return BaoyangItemSaveResponse.class;
    }

    public void setParams(String item, String clientType, long uid) {
        addParams("item", item);
        if (uid != 0) {
            addParams("uid", uid);
        }
        addParams("clientType", clientType);
    }
}