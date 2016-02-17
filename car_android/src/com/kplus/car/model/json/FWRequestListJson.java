package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.FWRequestInfo;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWRequestListJson extends BaseModelObj {
    @ApiListField("resultList")
    private List<FWRequestInfo> list;
    @ApiField("count")
    private Integer total;

    public List<FWRequestInfo> getList() {
        return list;
    }

    public void setList(List<FWRequestInfo> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
