package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/20.
 */
public class FetchCommonCarsResp extends BaseInfo {
    private BundleBean<Car, String> carBundle;

    public BundleBean<Car, String> getCarBundle() {
        return carBundle;
    }

    public void setCarBundle(BundleBean<Car, String> carBundle) {
        this.carBundle = carBundle;
    }
}
