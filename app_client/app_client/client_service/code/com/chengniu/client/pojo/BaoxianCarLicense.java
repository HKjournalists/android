package com.chengniu.client.pojo;

import java.util.Date;

public class BaoxianCarLicense {
    private String id;

    private Integer userType;

    private String userId;

    private String drivingLicenceUrl;

    private Integer status;

    private String vehicleNum;

    private String motorNum;

    private String frameNum;

    private String vehicleType;

    private String owner;

    private String address;

    private String useProperty;

    private String brandModel;

    private String registerDate;

    private String issueDate;

    private String adjustDate;

    private String descr;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getDrivingLicenceUrl() {
        return drivingLicenceUrl;
    }

    public void setDrivingLicenceUrl(String drivingLicenceUrl) {
        this.drivingLicenceUrl = drivingLicenceUrl == null ? null : drivingLicenceUrl.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum == null ? null : vehicleNum.trim();
    }

    public String getMotorNum() {
        return motorNum;
    }

    public void setMotorNum(String motorNum) {
        this.motorNum = motorNum == null ? null : motorNum.trim();
    }

    public String getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum == null ? null : frameNum.trim();
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType == null ? null : vehicleType.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getUseProperty() {
        return useProperty;
    }

    public void setUseProperty(String useProperty) {
        this.useProperty = useProperty == null ? null : useProperty.trim();
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel == null ? null : brandModel.trim();
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate == null ? null : registerDate.trim();
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate == null ? null : issueDate.trim();
    }

    public String getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(String adjustDate) {
        this.adjustDate = adjustDate == null ? null : adjustDate.trim();
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr == null ? null : descr.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}