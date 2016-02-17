package com.kplus.car.model.json;

import com.kplus.car.model.BaoyangItem;
import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemJson extends BaseModelObj {
    @ApiListField("list")
    private List<BaoyangItem> list;
    @ApiField("total")
    private Integer total;

    public List<BaoyangItem> getList() {
        return list;
    }

    public void setList(List<BaoyangItem> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
