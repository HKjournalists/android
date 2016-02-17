package com.kplus.car.carwash.bean;

/**
 * Created by Fu on 2015/5/18.
 */
public class FetchSupportCarModelsResp extends BaseInfo {
    private static final long serialVersionUID = 1L;

    private BundleBean<CarBrand, Long> brandBundle;
    private BundleBean<CarModel, Long> modelBundle;
    private BundleBean<CarColor, Long> colorBundle;
    private BundleBean<CarModelTag, CarModelTag> carTagBundle;

    public BundleBean<CarBrand, Long> getBrandBundle() {
        return brandBundle;
    }

    public void setBrandBundle(BundleBean<CarBrand, Long> brandBundle) {
        this.brandBundle = brandBundle;
    }

    public BundleBean<CarModel, Long> getModelBundle() {
        return modelBundle;
    }

    public void setModelBundle(BundleBean<CarModel, Long> modelBundle) {
        this.modelBundle = modelBundle;
    }

    public BundleBean<CarColor, Long> getColorBundle() {
        return colorBundle;
    }

    public void setColorBundle(BundleBean<CarColor, Long> colorBundle) {
        this.colorBundle = colorBundle;
    }

    public BundleBean<CarModelTag, CarModelTag> getCarTagBundle() {
        return carTagBundle;
    }

    public void setCarTagBundle(BundleBean<CarModelTag, CarModelTag> carTagBundle) {
        this.carTagBundle = carTagBundle;
    }
}
