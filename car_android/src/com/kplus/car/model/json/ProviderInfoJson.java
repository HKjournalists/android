package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.ProviderInfo;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by admin on 2015/6/3.
 */
public class ProviderInfoJson extends BaseModelObj {
    @ApiField("total")
    private Integer total;
    @ApiListField("list")
    private List<ProviderInfo> list;

    public List<ProviderInfo> getList() {
        return list;
    }

    public void setList(List<ProviderInfo> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
