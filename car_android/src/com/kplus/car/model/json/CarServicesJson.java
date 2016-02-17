package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.CarServiceGroup;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Description：
 * <br/><br/>Created by FU ZHIXUE on 2015/8/13.
 * <br/><br/>
 */
public class CarServicesJson extends BaseModelObj {
    @ApiListField("cityId")
    private Long cityId;
    /**
     * 服务分组列表
     */
    @ApiListField("serviceGroups")
    private List<CarServiceGroup> serviceGroups;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public List<CarServiceGroup> getServiceGroups() {
        return serviceGroups;
    }

    public void setServiceGroups(List<CarServiceGroup> serviceGroups) {
        this.serviceGroups = serviceGroups;
    }
}
