package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/12.
 */
public class FWCityService extends BaseModelObj {
    @ApiField("id")
    private Long id;
    @ApiField("name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
