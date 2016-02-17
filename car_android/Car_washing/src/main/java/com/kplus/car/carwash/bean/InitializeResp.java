package com.kplus.car.carwash.bean;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/28.
 * <br/><br/>
 */
public class InitializeResp extends BaseInfo {

    private City locatedCity;

    private long recommendCityId;

    private BundleBean<City, Long> cityBundle;

    private BundleBean<OnSiteService, Long> serviceBundle;

    private BundleBean<Region, Long> regionBundle;

    private BundleBean<Car, String> commonCarBundle;

    private BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> supportCarTagBundle;

    private Contact commonUsedContact;
    private Car commonUsedCar;
    private Position commonUsedPosition;
    private long commonUsedServiceId;

    public City getLocatedCity() {
        return locatedCity;
    }

    public void setLocatedCity(City locatedCity) {
        this.locatedCity = locatedCity;
    }

    public long getRecommendCityId() {
        return recommendCityId;
    }

    public void setRecommendCityId(long recommendCityId) {
        this.recommendCityId = recommendCityId;
    }

    public BundleBean<City, Long> getCityBundle() {
        return cityBundle;
    }

    public void setCityBundle(BundleBean<City, Long> cityBundle) {
        this.cityBundle = cityBundle;
    }

    public BundleBean<OnSiteService, Long> getServiceBundle() {
        return serviceBundle;
    }

    public void setServiceBundle(BundleBean<OnSiteService, Long> serviceBundle) {
        this.serviceBundle = serviceBundle;
    }

    public BundleBean<Region, Long> getRegionBundle() {
        return regionBundle;
    }

    public void setRegionBundle(BundleBean<Region, Long> regionBundle) {
        this.regionBundle = regionBundle;
    }

    public BundleBean<Car, String> getCommonCarBundle() {
        return commonCarBundle;
    }

    public void setCommonCarBundle(BundleBean<Car, String> commonCarBundle) {
        this.commonCarBundle = commonCarBundle;
    }

    public Contact getCommonUsedContact() {
        return commonUsedContact;
    }

    public void setCommonUsedContact(Contact commonUsedContact) {
        this.commonUsedContact = commonUsedContact;
    }

    public Car getCommonUsedCar() {
        return commonUsedCar;
    }

    public void setCommonUsedCar(Car commonUsedCar) {
        this.commonUsedCar = commonUsedCar;
    }

    public Position getCommonUsedPosition() {
        return commonUsedPosition;
    }

    public void setCommonUsedPosition(Position commonUsedPosition) {
        this.commonUsedPosition = commonUsedPosition;
    }

    public long getCommonUsedServiceId() {
        return commonUsedServiceId;
    }

    public void setCommonUsedServiceId(long commonUsedServiceId) {
        this.commonUsedServiceId = commonUsedServiceId;
    }

    public BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> getSupportCarTagBundle() {
        return supportCarTagBundle;
    }

    public void setSupportCarTagBundle(BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> supportCarTagBundle) {
        this.supportCarTagBundle = supportCarTagBundle;
    }
}
