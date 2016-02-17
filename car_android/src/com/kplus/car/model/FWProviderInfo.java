package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/12.
 */
public class FWProviderInfo extends BaseModelObj {
    @ApiField("providerId")
    private String providerId;
    @ApiField("name")
    private String name;
    @ApiField("assessNum")
    private Integer assessNum;
    @ApiField("score")
    private Integer score;
    @ApiField("address")
    private String address;
    @ApiField("phone")
    private String phone;
    @ApiField("openUid")
    private String openUid;
    @ApiField("introduce")
    private String introduce;
    @ApiField("serviceArea")
    private String serviceArea;
    @ApiField("distance")
    private Long distance;
    @ApiField("price")
    private Float price;
    @ApiField("imgUrl")
    private String imgUrl;
    @ApiField("totalServiceCount")
    private Integer totalServiceCount;
    @ApiField("orderStatus")
    private String orderStatus;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAssessNum() {
        return assessNum;
    }

    public void setAssessNum(Integer assessNum) {
        this.assessNum = assessNum;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenUid() {
        return openUid;
    }

    public void setOpenUid(String openUid) {
        this.openUid = openUid;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getTotalServiceCount() {
        return totalServiceCount;
    }

    public void setTotalServiceCount(Integer totalServiceCount) {
        this.totalServiceCount = totalServiceCount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
