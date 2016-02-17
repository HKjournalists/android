package com.kplus.car.carwash.bean;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public class FetchCitiesResp extends BaseInfo {
    private BundleBean<City, Long> cityBundle;

    public BundleBean<City, Long> getCityBundle() {
        return cityBundle;
    }

    public void setCityBundle(BundleBean<City, Long> cityBundle) {
        this.cityBundle = cityBundle;
    }
}
