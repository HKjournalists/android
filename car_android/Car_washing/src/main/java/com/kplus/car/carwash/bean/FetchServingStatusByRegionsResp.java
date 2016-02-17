package com.kplus.car.carwash.bean;

import java.util.List;

/**
 * Created by Fu on 2015/6/18.
 */
public class FetchServingStatusByRegionsResp extends BaseInfo {
    private static final long serialVersionUID = 1L;

    private List<ServingStatus> servingStatus;

    public List<ServingStatus> getServingStatus() {
        return servingStatus;
    }

    public void setServingStatus(List<ServingStatus> servingStatus) {
        this.servingStatus = servingStatus;
    }
}
