package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.FWCityService;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 */
public class FWCityServiceJson extends BaseModelObj {
    @ApiListField("list")
    private List<FWCityService> list;
    @ApiField("total")
    private Integer total;

    public List<FWCityService> getList() {
        return list;
    }

    public void setList(List<FWCityService> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
