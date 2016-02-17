package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/7/9.
 */
public class RestrictListJson extends BaseModelObj {
    @ApiListField("list")
    private List<RemindRestrict> list;

    public List<RemindRestrict> getList() {
        return list;
    }

    public void setList(List<RemindRestrict> list) {
        this.list = list;
    }
}
