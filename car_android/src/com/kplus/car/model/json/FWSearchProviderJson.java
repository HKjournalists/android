package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/9/9.
 */
public class FWSearchProviderJson extends BaseModelObj {
    @ApiField("id")
    private String id;
    @ApiField("cityId")
    private Long cityId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
