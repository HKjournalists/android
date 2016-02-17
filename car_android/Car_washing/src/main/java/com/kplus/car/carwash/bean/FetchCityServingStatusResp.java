package com.kplus.car.carwash.bean;

import java.util.List;

/**
 * Created by Fu on 2015/5/18.
 */
public class FetchCityServingStatusResp extends BaseInfo {
    private List<ServingStatus> servingStatus;

    public List<ServingStatus> getServingStatus() {
        return servingStatus;
    }

    public void setServingStatus(List<ServingStatus> servingStatus) {
        this.servingStatus = servingStatus;
    }
}
