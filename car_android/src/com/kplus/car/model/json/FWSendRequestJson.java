package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/13.
 */
public class FWSendRequestJson extends BaseModelObj {
    @ApiField("requestCode")
    private String requestCode;
    @ApiField("providerNum")
    private Integer providerNum;

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Integer getProviderNum() {
        return providerNum;
    }

    public void setProviderNum(Integer providerNum) {
        this.providerNum = providerNum;
    }
}
