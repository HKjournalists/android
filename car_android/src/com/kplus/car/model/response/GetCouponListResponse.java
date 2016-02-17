package com.kplus.car.model.response;

import com.kplus.car.Response;
import com.kplus.car.model.json.GetCouponListJson;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/11/14.
 */
public class GetCouponListResponse extends Response {
    @ApiField("data")
    private GetCouponListJson data;

    public GetCouponListJson getData() {
        return data;
    }

    public void setData(GetCouponListJson data) {
        this.data = data;
    }
}
