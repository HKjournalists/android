package com.kplus.car.model;

import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/9/24.
 */
public class ServiceImgList extends BaseModelObj {
    @ApiListField("imgList")
    private List<ServiceImg> imgList;

    public List<ServiceImg> getImgList() {
        return imgList;
    }

    public void setImgList(List<ServiceImg> imgList) {
        this.imgList = imgList;
    }
}
