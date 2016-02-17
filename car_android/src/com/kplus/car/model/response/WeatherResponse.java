package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.WeatherJson;
import com.kplus.car.parser.ApiField;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/21.
 * <br/><br/>
 */
public class WeatherResponse extends Response {

    @ApiField("data")
    private WeatherJson data;

    public WeatherJson getData() {
        if (data == null)
            data = new WeatherJson();
        return data;
    }

    public void setData(WeatherJson data) {
        this.data = data;
    }
}
