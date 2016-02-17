package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetProviderByOpenUserIdResponse;

/**
 * Created by admin on 2015/6/3.
 */
public class GetProviderByOpenUserIdRequest extends BaseRequest<GetProviderByOpenUserIdResponse> {
    @Override
    public String getApiMethodName() {
        return "/fw/getProviderByOpenUserId.htm";
    }

    @Override
    public Class<GetProviderByOpenUserIdResponse> getResponseClass() {
        return GetProviderByOpenUserIdResponse.class;
    }

    public void setParams(String openUserId){
        addParams("openUserId", "[\""+ openUserId + "\"]");
    }
}
