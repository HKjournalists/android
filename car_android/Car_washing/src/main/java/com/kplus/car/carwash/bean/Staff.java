package com.kplus.car.carwash.bean;


import java.security.Timestamp;

/**
 * Created by Fu on 2015/5/19.
 */
public class Staff extends BaseInfo {
    private long id;
    private String name;
    private String mobile;
    private String password;
    private String idNo;
    private int sex;
    private String birthday;
    private String avatar;
    private int status;
    private long cityId;
    private String cityName;
    private String provinceName;
    private long latestLat;
    private long latestLng;
    private Timestamp latestReportTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public long getLatestLat() {
        return latestLat;
    }

    public void setLatestLat(long latestLat) {
        this.latestLat = latestLat;
    }

    public long getLatestLng() {
        return latestLng;
    }

    public void setLatestLng(long latestLng) {
        this.latestLng = latestLng;
    }

    public Timestamp getLatestReportTime() {
        return latestReportTime;
    }

    public void setLatestReportTime(Timestamp latestReportTime) {
        this.latestReportTime = latestReportTime;
    }
}
