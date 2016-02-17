package com.kplus.car.model.response.request;

import com.kplus.car.model.response.CarServicesResponse;

/**
 * Description：
 * <br/><br/>Created by FU ZHIXUE on 2015/8/13.
 * <br/><br/>
 */
public class CarServicesRequest  extends BaseRequest<CarServicesResponse> {
    @Override
    public String getApiMethodName() {
        return "/service/index.htm";
    }

    @Override
    public Class<CarServicesResponse> getResponseClass() {
        return CarServicesResponse.class;
    }

    public void setParams(String clientType, long cityid) {
        addParams("cityId", cityid);
        addParams("clientType", clientType);
    }
}
