package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BaoyangShopResponse;
import com.kplus.car.util.StringUtils;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangShopRequest extends BaseRequest<BaoyangShopResponse> {
    @Override
    public String getApiMethodName() {
        return "/remind/shop.htm";
    }

    @Override
    public Class<BaoyangShopResponse> getResponseClass() {
        return BaoyangShopResponse.class;
    }

    public void setParams(String name, String cityCode, String area, String lat, String lon, String brandName){
        if (!StringUtils.isEmpty(name))
            addParams("name", name);
        addParams("cityCode", cityCode);
        if (!StringUtils.isEmpty(area))
            addParams("area", area);
        if (!StringUtils.isEmpty(lat))
            addParams("lat", lat);
        if (!StringUtils.isEmpty(lon))
            addParams("lon", lon);
        if (!StringUtils.isEmpty(brandName))
            addParams("brandName", brandName);
    }
}
