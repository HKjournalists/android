package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.Coupon;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class GetCouponListJson extends BaseModelObj {
    @ApiListField("list")
    private List<Coupon> list;
    @ApiField("total")
    private Integer total;

    public List<Coupon> getList() {
        return list;
    }

    public void setList(List<Coupon> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
