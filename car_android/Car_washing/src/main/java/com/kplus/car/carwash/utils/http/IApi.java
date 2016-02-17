package com.kplus.car.carwash.utils.http;

import java.util.LinkedHashMap;

/**
 * Created by Fu on 2015/5/17.
 */
public interface IApi {

    void initialize(String funcKey, String jsonParams, ApiHandler handler);

    void getLocServiceCity(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getLocServiceCityV2(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchUserHabits(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchCityServices(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchCityConfig(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchCityServingStatus(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void fetchCityServingStatusByRegions(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchUsableCoupons(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void submitOrder(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchSupportCarModel(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchOrderLogs(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void getFetchPaginationOrders(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void cancelOrder(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void submitReview(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void fetchCommonCars(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void fetchOrder(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void fetchCities(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);

    void fetchCityRegions(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler);
}
