package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.BaoyangItemDeleteJson;
import com.kplus.car.parser.ApiField;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemDeleteResponse extends Response {
    @ApiField("data")
    private BaoyangItemDeleteJson data;

    public BaoyangItemDeleteJson getData() {
        return data;
    }

    public void setData(BaoyangItemDeleteJson data) {
        this.data = data;
    }
}
