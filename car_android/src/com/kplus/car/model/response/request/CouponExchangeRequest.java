package com.kplus.car.model.response.request;

import com.kplus.car.model.response.CouponExchangeResponse;

/**
 * Created by Administrator on 2015/11/12.
 */
public class CouponExchangeRequest extends BaseRequest<CouponExchangeResponse> {
    @Override
    public String getApiMethodName() {
        return "/coupon/exchange.htm";
    }

    @Override
    public Class<CouponExchangeResponse> getResponseClass() {
        return CouponExchangeResponse.class;
    }

    public void setParams(String promotionCode, long uid){
        addParams("promotionCode", promotionCode);
        addParams("uid", uid);
    }
}
