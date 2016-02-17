package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/17.
 */
public class FetchCityServiceResp extends BaseInfo {
    private BundleBean<OnSiteService, Long> serviceBundle;

    private BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> supportCarTagBundle;

    public BundleBean<OnSiteService, Long> getServiceBundle() {
        return serviceBundle;
    }

    public void setServiceBundle(BundleBean<OnSiteService, Long> serviceBundle) {
        this.serviceBundle = serviceBundle;
    }

    public BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> getSupportCarTagBundle() {
        return supportCarTagBundle;
    }

    public void setSupportCarTagBundle(BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> supportCarTagBundle) {
        this.supportCarTagBundle = supportCarTagBundle;
    }
}
