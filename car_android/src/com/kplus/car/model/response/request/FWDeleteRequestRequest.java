package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetResultResponse;

/**
 * Created by Administrator on 2015/10/21.
 */
public class FWDeleteRequestRequest extends BaseRequest<GetResultResponse> {
    @Override
    public String getApiMethodName() {
        return "/serviceRequest/request/del.htm";
    }

    @Override
    public Class<GetResultResponse> getResponseClass() {
        return GetResultResponse.class;
    }

    public void setParams(long uid, String requestCode){
        addParams("uid", uid);
        addParams("requestCode", requestCode);
    }
}
