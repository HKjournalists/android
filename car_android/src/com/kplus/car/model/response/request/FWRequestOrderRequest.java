package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

/**
 * Created by Administrator on 2015/8/25.
 */
public class FWRequestOrderRequest extends BaseRequest<GetResultResponse> {
    @Override
    public String getApiMethodName() {
        return "/serviceRequest/requestOrder/put.htm";
    }

    @Override
    public Class<GetResultResponse> getResponseClass() {
        return GetResultResponse.class;
    }

    public void setParams(String requestCode, String providerId){
        addParams("requestCode", requestCode);
        addParams("providerId", providerId);
    }
}
