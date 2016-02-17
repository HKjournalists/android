package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.BaoyangBrandJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangBrandResponse extends Response {
    @ApiField("data")
    private BaoyangBrandJson data;

    public BaoyangBrandJson getData() {
        if(data == null)
            data = new BaoyangBrandJson();
        return data;
    }

    public void setData(BaoyangBrandJson data) {
        this.data = data;
    }
}
