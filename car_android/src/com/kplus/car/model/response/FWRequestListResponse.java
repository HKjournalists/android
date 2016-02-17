package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.FWRequestListJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWRequestListResponse extends Response {
    @ApiField("data")
    private FWRequestListJson data;

    public FWRequestListJson getData() {
        return data;
    }

    public void setData(FWRequestListJson data) {
        this.data = data;
    }
}
