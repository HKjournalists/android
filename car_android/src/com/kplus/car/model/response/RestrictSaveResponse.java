package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.RestrictSaveJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/7/8.
 */
public class RestrictSaveResponse extends Response {
    @ApiField("data")
    private RestrictSaveJson data;

    public RestrictSaveJson getData() {
        return data;
    }

    public void setData(RestrictSaveJson data) {
        this.data = data;
    }
}
