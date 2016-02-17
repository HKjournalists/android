package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.FWProviderInfo;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/28.
 */
public class FWGetProviderResponse extends Response {
    @ApiField("data")
    private FWProviderInfo data;

    public FWProviderInfo getData() {
        return data;
    }

    public void setData(FWProviderInfo data) {
        this.data = data;
    }
}
