package com.kplus.car.carwash.bean;

import java.util.Date;

/**
 * Created by Fu on 2015/5/17.
 */
public class Coupon extends BaseInfo {
    private long id;
    private long userCouponId;
    private long couponId;
    private String name;
    private String info;
    private Integer amount;
    private Integer conditions;
    private String directType;
    private Date beginTime;
    private Date endTime;
    private Integer status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(long userCouponId) {
        this.userCouponId = userCouponId;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getConditions() {
        return conditions;
    }

    public void setConditions(Integer conditions) {
        this.conditions = conditions;
    }

    public String getDirectType() {
        return directType;
    }

    public void setDirectType(String directType) {
        this.directType = directType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
