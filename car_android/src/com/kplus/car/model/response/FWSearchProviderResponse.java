package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.FWSearchProviderJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/9/9.
 */
public class FWSearchProviderResponse extends Response {
    @ApiField("data")
    private FWSearchProviderJson data;

    public FWSearchProviderJson getData() {
        return data;
    }

    public void setData(FWSearchProviderJson data) {
        this.data = data;
    }
}
