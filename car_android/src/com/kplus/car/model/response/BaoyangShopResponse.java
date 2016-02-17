package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.BaoyangShopJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangShopResponse extends Response {
    @ApiField("data")
    private BaoyangShopJson data;

    public BaoyangShopJson getData() {
        if(data == null)
            data = new BaoyangShopJson();
        return data;
    }

    public void setData(BaoyangShopJson data) {
        this.data = data;
    }
}
