package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BaoyangBrandResponse;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangBrandRequest extends BaseRequest<BaoyangBrandResponse> {
    @Override
    public String getApiMethodName() {
        return "/remind/queryBrand.htm";
    }

    @Override
    public Class<BaoyangBrandResponse> getResponseClass() {
        return BaoyangBrandResponse.class;
    }
}
