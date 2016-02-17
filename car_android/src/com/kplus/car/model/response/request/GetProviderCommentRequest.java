package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetProviderCommentResponse;

/**
 * Created by Administrator on 2015/8/24.
 */
public class GetProviderCommentRequest extends BaseRequest<GetProviderCommentResponse> {
    @Override
    public String getApiMethodName() {
        return "/fw/getProviderComment.htm";
    }

    @Override
    public Class<GetProviderCommentResponse> getResponseClass() {
        return GetProviderCommentResponse.class;
    }

    public void setParams(String providerId, int pageIndex, int pageSize){
        addParams("providerId", providerId);
        if (pageIndex != 0)
            addParams("pageIndex", pageIndex);
        if (pageSize != 0)
            addParams("pageSize", pageSize);
    }
}
