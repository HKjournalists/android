package com.kplus.car.model.response.request;

import com.kplus.car.model.response.WeatherResponse;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/21.
 * <br/><br/>
 */
public class WeatherRequest extends BaseRequest<WeatherResponse> {

    @Override
    public String getApiMethodName() {
        return "/data/getWeather.htm";
    }

    @Override
    public Class<WeatherResponse> getResponseClass() {
        return WeatherResponse.class;
    }

    public void setParams(String clientType, long cityid) {
        addParams("cityId", cityid);
        addParams("clientType", clientType);
    }
}
