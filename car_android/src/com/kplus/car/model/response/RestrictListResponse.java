package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.RestrictListJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/7/9.
 */
public class RestrictListResponse extends Response {
    @ApiField("data")
    private RestrictListJson data;

    public RestrictListJson getData() {
        if(data == null)
            data = new RestrictListJson();
        return data;
    }

    public void setData(RestrictListJson data) {
        this.data = data;
    }
}
