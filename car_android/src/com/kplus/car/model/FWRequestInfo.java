package com.kplus.car.model;

import com.kplus.car.parser.ApiField;

/**
 * Created by Administrator on 2015/8/12.
 */
public class FWRequestInfo extends BaseModelObj {
    private int id = 0;
    @ApiField("code")
    private String code;
    @ApiField("status")
    private String status;
    @ApiField("uid")
    private Long uid;
    @ApiField("version")
    private Integer version;
    @ApiField("joinInNum")
    private Integer joinInNum;
    @ApiField("createTime")
    private Long createTime;
    @ApiField("modifyTime")
    private Long modifyTime;
    @ApiField("providerId")
    private String providerId;
    @ApiField("extendsion")
    private String extendsion;
    @ApiField("serviceName")
    private String serviceName;
    @ApiField("serviceType")
    private Integer serviceType;
    @ApiField("openUserId")
    private String openUserId;
    @ApiField("telPhone")
    private String telPhone;
    @ApiField("price")
    private Float price;
    @ApiField("notifyNum")
    private Integer notifyNum;
    @ApiField("expectTime")
    private Long expectTime;
    @ApiField("nickName")
    private String nickName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getJoinInNum() {
        return joinInNum;
    }

    public void setJoinInNum(Integer joinInNum) {
        this.joinInNum = joinInNum;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getExtendsion() {
        return extendsion;
    }

    public void setExtendsion(String extendsion) {
        this.extendsion = extendsion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getNotifyNum() {
        return notifyNum;
    }

    public void setNotifyNum(Integer notifyNum) {
        this.notifyNum = notifyNum;
    }

    public Long getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Long expectTime) {
        this.expectTime = expectTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
