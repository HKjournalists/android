package com.kplus.car.model.response.request;

import com.kplus.car.model.response.FWSendRequestResponse;
import com.kplus.car.util.StringUtils;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWSendRequestRequest extends BaseRequest<FWSendRequestResponse> {
    @Override
    public String getApiMethodName() {
        return "/serviceRequest/request/put.htm";
    }

    @Override
    public Class<FWSendRequestResponse> getResponseClass() {
        return FWSendRequestResponse.class;
    }

    public void setParams(long uid, String cityId, int serviceType, double lat, double lon, String serviceName, String extendsion){
        addParams("uid", uid);
        addParams("cityId", cityId);
        addParams("serviceType", serviceType);
        addParams("serviceName", serviceName);
        if (lat != 0)
            addParams("latitude", lat);
        if (lon != 0)
            addParams("longitude", lon);
        if (!StringUtils.isEmpty(extendsion))
            addParams("extendsion", extendsion);
    }
}
