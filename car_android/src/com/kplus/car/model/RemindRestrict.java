package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/8.
 */
public class RemindRestrict extends BaseModelObj {
    @ApiField("id")
    private String id;
    @ApiField("isHidden")
    private String isHidden = "0";
    @ApiField("vehicleNum")
    private String vehicleNum;
    @ApiField("messageRemind")
    private String messageRemind = "0";
    @ApiField("phone")
    private String phone;
    @ApiField("remindDateType")
    private String remindDateType = "1";
    @ApiField("remindDate")
    private String remindDate = "20:00";
    @ApiField("vehicleCityId")
    private String vehicleCityId;
    private Integer dbid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getMessageRemind() {
        return messageRemind;
    }

    public void setMessageRemind(String messageRemind) {
        this.messageRemind = messageRemind;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemindDateType() {
        return remindDateType;
    }

    public void setRemindDateType(String remindDateType) {
        this.remindDateType = remindDateType;
    }

    public String getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(String remindDate) {
        this.remindDate = remindDate;
    }

    public Integer getDbid() {
        return dbid;
    }

    public void setDbid(Integer dbid) {
        this.dbid = dbid;
    }

    public String getVehicleCityId() {
        return vehicleCityId;
    }

    public void setVehicleCityId(String vehicleCityId) {
        this.vehicleCityId = vehicleCityId;
    }
}
