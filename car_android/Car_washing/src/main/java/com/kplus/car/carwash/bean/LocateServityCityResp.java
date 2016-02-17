package com.kplus.car.carwash.bean;

import java.util.List;

/**
 * Created by Fu on 2015/5/17.
 */
public class LocateServityCityResp extends BaseInfo {
    private City locatedCity;
    /**
     * 推荐城市id
     */
    private long recommendCityId;
    private List<City> servingCities;

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

    public List<City> getServingCities() {
        return servingCities;
    }

    public void setServingCities(List<City> servingCities) {
        this.servingCities = servingCities;
    }
}
