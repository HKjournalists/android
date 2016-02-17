package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class InsuranceJson extends BaseModelObj {
    @ApiField("value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
