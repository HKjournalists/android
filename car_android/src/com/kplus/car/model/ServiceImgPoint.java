package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/9/23.
 */
public class ServiceImgPoint extends BaseModelObj {
    @ApiField("service_id")
    private Long service_id;
    @ApiField("update_datetime")
    private String update_datetime;
    @ApiField("width")
    private Float width;
    @ApiField("height")
    private Float height;
    @ApiField("id")
    private Long id;
    @ApiField("motion_type")
    private String motion_type;
    @ApiField("motion_value")
    private String motion_value;
    @ApiField("motion_value_relation")
    private String motion_value_relation;
    @ApiField("service_img_id")
    private Long service_img_id;
    @ApiField("create_datetime")
    private String create_datetime;
    @ApiField("x")
    private Float x;
    @ApiField("y")
    private Float y;
    @ApiField("name")
    private String name;
    @ApiField("transition_url")
    private String transition_url;

    public Long getService_id() {
        return service_id;
    }

    public void setService_id(Long service_id) {
        this.service_id = service_id;
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

    public String getMotion_type() {
        return motion_type;
    }

    public void setMotion_type(String motion_type) {
        this.motion_type = motion_type;
    }

    public String getMotion_value() {
        return motion_value;
    }

    public void setMotion_value(String motion_value) {
        this.motion_value = motion_value;
    }

    public String getMotion_value_relation() {
        return motion_value_relation;
    }

    public void setMotion_value_relation(String motion_value_relation) {
        this.motion_value_relation = motion_value_relation;
    }

    public Long getService_img_id() {
        return service_img_id;
    }

    public void setService_img_id(Long service_img_id) {
        this.service_img_id = service_img_id;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransition_url() {
        return transition_url;
    }

    public void setTransition_url(String transition_url) {
        this.transition_url = transition_url;
    }
}
