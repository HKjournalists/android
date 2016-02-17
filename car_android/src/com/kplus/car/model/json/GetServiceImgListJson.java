package com.kplus.car.model.json;

import com.kplus.car.model.BaseModelObj;
import com.kplus.car.model.ServiceImg;
import com.kplus.car.model.ServiceImgList;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/9/23.
 */
public class GetServiceImgListJson extends BaseModelObj {
    @ApiListField("groupList")
    private List<ServiceImgList> groupList;

    public List<ServiceImgList> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<ServiceImgList> groupList) {
        this.groupList = groupList;
    }
}
