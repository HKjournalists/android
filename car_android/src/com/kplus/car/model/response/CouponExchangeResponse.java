package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.CouponExchangeJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/11/12.
 */
public class CouponExchangeResponse extends Response {
    @ApiField("data")
    private CouponExchangeJson data;

    public CouponExchangeJson getData() {
        return data;
    }

    public void setData(CouponExchangeJson data) {
        this.data = data;
    }
}
