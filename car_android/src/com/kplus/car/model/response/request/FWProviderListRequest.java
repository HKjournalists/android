package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWProviderListResponse;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWProviderListRequest extends BaseRequest<FWProviderListResponse> {
    @Override
    public String getApiMethodName() {
        return "/serviceRequest/requestProvider/list.htm";
    }

    @Override
    public Class<FWProviderListResponse> getResponseClass() {
        return FWProviderListResponse.class;
    }

    public void setParams(String requestCode){
        addParams("requestCode", requestCode);
    }
}
