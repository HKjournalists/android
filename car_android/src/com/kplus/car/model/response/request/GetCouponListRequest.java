package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetCouponListResponse;

/**
 * Created by Administrator on 2015/11/14.
 */
public class GetCouponListRequest extends BaseRequest<GetCouponListResponse> {
    @Override
    public String getApiMethodName() {
        return "/user/getCouponList.htm";
    }

    @Override
    public Class<GetCouponListResponse> getResponseClass() {
        return GetCouponListResponse.class;
    }

    public void setParams(long uid, int isAvalid, int pageIndex, int pageSize){
        addParams("uid", uid);
        addParams("isAvalid", isAvalid);
        addParams("pageIndex", pageIndex);
        addParams("pageSize", pageSize);
    }
}
