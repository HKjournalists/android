package com.kplus.car.model;

import com.kplus.car.parser.ApiField;
import com.kplus.car.parser.ApiListField;

import java.util.List;

/**
 * Created by Administrator on 2015/9/23.
 */
public class ServiceImg extends BaseModelObj {
    @ApiField("img_url")
    private String img_url;
    @ApiField("sort")
    private Long sort;
    @ApiField("update_datetime")
    private String update_datetime;
    @ApiField("width")
    private Float width;
    @ApiField("height")
    private Float height;
    @ApiField("id")
    private Long id;
    @ApiField("service_group_id")
    private Long service_group_id;
    @ApiListField("points")
    private List<ServiceImgPoint> points;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getUpdate_datetime() {
        return update_datetime;
    }

    public void setUpdate_datetime(String update_datetime) {
        this.update_datetime = update_datetime;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getService_group_id() {
        return service_group_id;
    }

    public void setService_group_id(Long service_group_id) {
        this.service_group_id = service_group_id;
    }

    public List<ServiceImgPoint> getPoints() {
        return points;
    }

    public void setPoints(List<ServiceImgPoint> points) {
        this.points = points;
    }
}
