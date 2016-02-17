package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWGetRequestResponse;
import com.kplus.car.util.StringUtils;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWGetRequestRequest extends BaseRequest<FWGetRequestResponse> {
    @Override
    public String getApiMethodName() {
        return "/serviceRequest/request/get.htm";
    }

    @Override
    public Class<FWGetRequestResponse> getResponseClass() {
        return FWGetRequestResponse.class;
    }

    public void setParams(String requestCode, long uid){
        if (!StringUtils.isEmpty(requestCode))
            addParams("requestCode", requestCode);
        if (uid != 0)
            addParams("uid", uid);
    }
}
