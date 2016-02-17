package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/4.
 */
public class BaoyangShop extends BaseModelObj {
    @ApiField("id")
    private String id;
    @ApiField("lon")
    private String lon;
    @ApiField("brand_name")
    private String brandName;
    @ApiField("address")
    private String address;
    @ApiField("tel")
    private String tel;
    @ApiField("lat")
    private String lat;
    @ApiField("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
