package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.FWSendRequestJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWSendRequestResponse extends Response {
    @ApiField("data")
    private FWSendRequestJson data;

    public FWSendRequestJson getData() {
        return data;
    }

    public void setData(FWSendRequestJson data) {
        this.data = data;
    }
}
