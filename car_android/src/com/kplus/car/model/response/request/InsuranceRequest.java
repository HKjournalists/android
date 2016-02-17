package com.kplus.car.model.response.request;

import com.kplus.car.model.response.InsuranceResponse;

/**
 * Created by Administrator on 2015/3/26 0026.
 */
public class InsuranceRequest extends BaseRequest<InsuranceResponse> {
    @Override
    public String getApiMethodName() {
        return "/remind/baoxian.htm";
    }

    @Override
    public Class<InsuranceResponse> getResponseClass() {
        return InsuranceResponse.class;
    }

}
