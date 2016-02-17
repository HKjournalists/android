package com.kplus.car.carwash.bean;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public class FetchCityRegionResp extends BaseInfo {
    private static final long serialVersionUID = 1L;

    private BundleBean<Region, Long> regionBundle;

    public BundleBean<Region, Long> getRegionBundle() {
        return regionBundle;
    }

    public void setRegionBundle(BundleBean<Region, Long> regionBundle) {
        this.regionBundle = regionBundle;
    }
}
