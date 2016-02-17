package com.kplus.car.carwash.bean;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public class InitializeReq {
    private long uid;
    private String cityName;

    private long citiesVersion;
    private long servicesVersion;
    private long regionsVersion;
    private long commonCarsVersion;
    private long supportTagsVersion;

    public InitializeReq() {

    }

    public InitializeReq(long uid, String cityName, long citiesVersion, long servicesVersion, long regionsVersion, long commonCarsVersion, long supportTagsVersion) {
        this.uid = uid;
        this.cityName = cityName;
        this.citiesVersion = citiesVersion;
        this.servicesVersion = servicesVersion;
        this.regionsVersion = regionsVersion;
        this.commonCarsVersion = commonCarsVersion;
        this.supportTagsVersion = supportTagsVersion;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getCitiesVersion() {
        return citiesVersion;
    }

    public void setCitiesVersion(long citiesVersion) {
        this.citiesVersion = citiesVersion;
    }

    public long getServicesVersion() {
        return servicesVersion;
    }

    public void setServicesVersion(long servicesVersion) {
        this.servicesVersion = servicesVersion;
    }

    public long getRegionsVersion() {
        return regionsVersion;
    }

    public void setRegionsVersion(long regionsVersion) {
        this.regionsVersion = regionsVersion;
    }

    public long getCommonCarsVersion() {
        return commonCarsVersion;
    }

    public void setCommonCarsVersion(long commonCarsVersion) {
        this.commonCarsVersion = commonCarsVersion;
    }

    public long getSupportTagsVersion() {
        return supportTagsVersion;
    }

    public void setSupportTagsVersion(long supportTagsVersion) {
        this.supportTagsVersion = supportTagsVersion;
    }
}
