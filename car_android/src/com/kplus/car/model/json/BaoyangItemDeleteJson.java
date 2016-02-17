package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/9.
 * <br/><br/>
 */
public class BaoyangItemDeleteJson extends BaseModelObj {
    @ApiField("result")
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
