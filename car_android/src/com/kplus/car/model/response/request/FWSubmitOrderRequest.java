package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWSubmitOrderResponse;
import com.kplus.car.util.StringUtils;

/**
 * Created by Administrator on 2015/8/18.
 */
public class FWSubmitOrderRequest extends BaseRequest<FWSubmitOrderResponse> {
    @Override
    public String getApiMethodName() {
        return "/fw/submitOrders.htm";
    }

    @Override
    public Class<FWSubmitOrderResponse> getResponseClass() {
        return FWSubmitOrderResponse.class;
    }

    public void setParams(long uid, long providerServiceId, long pId, float price, long userId, String providerId, String providerName, String cityId, String serviceName, String couponId, String requestCode){
        addParams("uid", uid);
        addParams("providerServiceId", providerServiceId);
        addParams("pId", pId);
        addParams("price", price);
        addParams("userId", userId);
        addParams("providerId", providerId);
        addParams("providerName", providerName);
        long nCityId = 0;
        if (!StringUtils.isEmpty(cityId))
            nCityId = Long.parseLong(cityId);
        addParams("cityId", nCityId);
        addParams("serviceName", serviceName);
        if (!StringUtils.isEmpty(couponId))
            addParams("couponId", couponId);
        addParams("requestCode", requestCode);
        addParams("v", 1);
    }
}
