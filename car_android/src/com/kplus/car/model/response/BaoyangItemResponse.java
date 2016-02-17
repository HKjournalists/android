package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.BaoyangItemJson;
import com.kplus.car.parser.ApiField;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemResponse extends Response {
    @ApiField("data")
    private BaoyangItemJson data;

    public BaoyangItemJson getData() {
        if (data == null)
            data = new BaoyangItemJson();
        return data;
    }

    public void setData(BaoyangItemJson data) {
        this.data = data;
    }
}
