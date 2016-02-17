package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.FWSubmitOrderJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/18.
 */
public class FWSubmitOrderResponse extends Response {
    @ApiField("data")
    private FWSubmitOrderJson data;

    public FWSubmitOrderJson getData() {
        return data;
    }

    public void setData(FWSubmitOrderJson data) {
        this.data = data;
    }
}
