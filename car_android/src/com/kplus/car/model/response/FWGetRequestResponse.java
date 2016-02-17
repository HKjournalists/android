package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.FWRequestInfo;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWGetRequestResponse extends Response {
    @ApiField("data")
    private FWRequestInfo data;

    public FWRequestInfo getData() {
        return data;
    }

    public void setData(FWRequestInfo data) {
        this.data = data;
    }
}
