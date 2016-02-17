package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWGetProviderResponse;

/**
 * Created by Administrator on 2015/8/28.
 */
public class FWGetProviderRequest extends BaseRequest<FWGetProviderResponse> {
    @Override
    public String getApiMethodName() {
        return "/serviceRequest/provider/get.htm";
    }

    @Override
    public Class<FWGetProviderResponse> getResponseClass() {
        return FWGetProviderResponse.class;
    }

    public void setParams(String providerId){
        addParams("providerId", providerId);
    }
}
