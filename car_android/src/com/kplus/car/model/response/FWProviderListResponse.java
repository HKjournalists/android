package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.FWProviderInfo;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWProviderListResponse extends Response {
    @ApiListField("data")
    private List<FWProviderInfo> data;

    public List<FWProviderInfo> getData() {
        return data;
    }

    public void setData(List<FWProviderInfo> data) {
        this.data = data;
    }
}
