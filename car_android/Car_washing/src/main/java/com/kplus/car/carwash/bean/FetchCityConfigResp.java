package com.kplus.car.carwash.bean;


import java.util.List;

/**
 * Created by Fu on 2015/5/17.
 */
public class FetchCityConfigResp extends BaseInfo {
    private List<Region> areas;

    public List<Region> getAreas() {
        return areas;
    }

    public void setAreas(List<Region> areas) {
        this.areas = areas;
    }
}
