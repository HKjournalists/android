package com.kplus.car.carwash.utils.http;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.kplus.car.carwash.bean.InitializeReq;
import com.kplus.car.carwash.manager.ApiManager;
import com.kplus.car.carwash.module.AppBridgeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fu on 2015/5/8.
 */
public class HttpRequestHelper {
    private static final String TAG = "HttpRequestHelper";

    /**
     * 初始化数据接口
     *
     * @param context
     * @param req
     * @param handler
     */
    public static void initialize(Context context, InitializeReq req, ApiHandler handler) {
        if (null == req) {
            throw new IllegalArgumentException("参数不能为null~~~");
        }

        String jsonParams = JSON.toJSONString(req);

        ApiManager.getInitialize(context, false, 0).initialize(HttpRequestParams.INITIALIZE, jsonParams, handler);
    }

    /**
     * 定位服务城市
     *
     * @param lng     longitude
     * @param lat     latitude
     * @param handler callback
     */
    public static void getLocServiceCity(Context context, double lng, double lat, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.LNG, lng);
        params.put(HttpRequestField.LAT, lat);

        ApiManager.getInitialize(context, false, 0).getLocServiceCity(HttpRequestParams.LOCATE_SERVING_CITY, params, handler);
    }

    /**
     * 定位服务城市
     *
     * @param lng     longitude
     * @param lat     latitude
     * @param handler callback
     */
    public static void getLocServiceCityV2(Context context, double lng, double lat, String city, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.LNG, lng);
        params.put(HttpRequestField.LAT, lat);
        params.put(HttpRequestField.CITY_NAME, city);

        ApiManager.getInitialize(context, false, 0).getLocServiceCityV2(HttpRequestParams.LOCATE_SERVING_CITY_V2, params, handler);
    }

    /**
     * 获取用户在指定城市的消费习惯
     *
     * @param context
     * @param cityId
     * @param handler
     */
    public static void getFetchUserHabits(Context context, long cityId, boolean isProgress, ApiHandler handler) {
        long uid = AppBridgeUtils.getIns().getUid();
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.CITY_ID, cityId);
        params.put(HttpRequestField.UID, uid);

        ApiManager.getInitialize(context, isProgress, 0).getFetchUserHabits(HttpRequestParams.FETCH_USER_HABITS, params, handler);
    }

    /**
     * 获取服务列表
     *
     * @param context
     * @param cityId
     * @param handler
     */
    public static void getFetchCityServices(Context context, long cityId, long version, long supportCarTagVersion, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.CITY_ID, cityId);
        params.put(HttpRequestField.VERSION, version);
        params.put(HttpRequestField.SUPPORT_CAR_TAG_VERSION, supportCarTagVersion);

        ApiManager.getInitialize(context, true, 0).getFetchCityServices(HttpRequestParams.FETCH_CITY_SERVICES, params, handler);
    }

    /**
     * 获取城市服务配置
     *
     * @param context
     * @param cityId
     * @param handler
     */
    public static void getFetchCityConfig(Context context, long cityId, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.CITY_ID, cityId);

        ApiManager.getInitialize(context, true, 0).getFetchCityConfig(HttpRequestParams.FETCH_CITY_CONFIG, params, handler);
    }

    /**
     * 获取城市服务状态
     *
     * @param context
     * @param cityId
     * @param handler
     */
    public static void getFetchCityServingStatus(Context context, boolean isProgress, long cityId, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.CITY_ID, cityId);

        ApiManager.getInitialize(context, isProgress, 0).getFetchCityServingStatus(HttpRequestParams.FETCH_CITY_SERVING_STATUS, params, handler);
    }

    /**
     * 按服务范围获取库存
     *
     * @param context
     * @param isProgress
     * @param cityId
     * @param regions
     * @param lng
     * @param lat
     * @param handler
     */
    public static void fetchCityServingStatusByRegions(Context context, boolean isProgress, long cityId, ArrayList<Long> regions, double lng, double lat, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.CITY_ID, cityId);
        params.put(HttpRequestField.LNG, String.valueOf(lng));
        params.put(HttpRequestField.LAT, String.valueOf(lat));
        params.put(HttpRequestField.REGIONS, regions);

        ApiManager.getInitialize(context, isProgress, 0).fetchCityServingStatusByRegions(HttpRequestParams.FETCH_CITY_SERVING_STATUS_BY_REGIONS, params, handler);
    }

    /**
     * 获取适用的代金券
     *
     * @param context
     * @param handler
     */
    public static void getFetchUsableCoupons(Context context, ArrayList<Long> serviceIds, ApiHandler handler) {
        long uid = AppBridgeUtils.getIns().getUid();
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.UID, uid);
        params.put(HttpRequestField.SERVICES, serviceIds);

        ApiManager.getInitialize(context, false, 0).getFetchUsableCoupons(HttpRequestParams.FETCH_USABLE_COUPONS, params, handler);
    }

    /**
     * 获取车型数据
     */
    public static void getFetchSupportCarModel(Context context, long brandVersion, long modelVersion, long colorVersion, long tagsVersion, boolean isProgress, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
//        params.put(HttpRequestField.BRAND_VERSION, brandVersion);
        params.put(HttpRequestField.MODEL_VERSION, modelVersion);
        params.put(HttpRequestField.COLOR_VERSION, colorVersion);
        params.put(HttpRequestField.TAGS_VERSION, tagsVersion);

        ApiManager.getInitialize(context, isProgress, 0).getFetchSupportCarModel(HttpRequestParams.FETCH_SUPPORT_CAR_MODELS, params, handler);
    }

    /**
     * 提交洗车订单
     *
     * @param context
     * @param handler
     */
    public static void submitOrder(Context context,
                                   List<Long> serviceIds,
                                   Map<String, Object> contant,
                                   Map<String, Object> carInfos,
                                   Map<String, Object> carPositions,
                                   Map<String, Object> servTimes,
                                   ArrayList<Long> coupons,
                                   BigDecimal price,
                                   BigDecimal couponPrice,
                                   String workerMobile,
                                   ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        long uid = AppBridgeUtils.getIns().getUid();
        long pid = AppBridgeUtils.getIns().getPid();
        long userId = AppBridgeUtils.getIns().getUserId();
        params.put(HttpRequestField.UID, uid);
        params.put(HttpRequestField.PID, pid);
        params.put(HttpRequestField.USER_ID, userId);
        // 服务列表
        params.put(HttpRequestField.SERVICE_IDS, serviceIds);
        // 用户信息
        params.put(HttpRequestField.CONTACT, contant);
        // 车辆信息
        params.put(HttpRequestField.CAR, carInfos);
        // 车辆位置
        params.put(HttpRequestField.CAR_POSITION, carPositions);
        // 服务时间
        params.put(HttpRequestField.SERVING_TIME, servTimes);
        // 代金券
        params.put(HttpRequestField.COUPON_IDS, coupons);
        params.put(HttpRequestField.PRICE, price);
        params.put(HttpRequestField.COUPON_PRICE, couponPrice);
        params.put(HttpRequestField.WORKER_MOBILE, workerMobile);

        ApiManager.getInitialize(context, true, 0).submitOrder(HttpRequestParams.SUBMIT_ORDER, params, handler);
    }

    /**
     * 获取洗车订单日志
     *
     * @param context
     * @param handler
     */
    public static void getFetchOrderLogs(Context context, long orderId, boolean isProgress, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.ORDER_ID, orderId);
        ApiManager.getInitialize(context, isProgress, 0).getFetchOrderLogs(HttpRequestParams.FETCH_ORDER_LOGS, params, handler);
    }

    /**
     * 取洗车订单列表
     *
     * @param context
     * @param pageIndex
     * @param pageSize
     * @param handler
     */
    public static void getFetchPaginationOrders(Context context, int pageIndex, int pageSize, ApiHandler handler) {
        long pid = AppBridgeUtils.getIns().getPid();
        long uid = AppBridgeUtils.getIns().getUid();

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.UID, uid);
        params.put(HttpRequestField.PID, pid);

        params.put(HttpRequestField.PAGE_INDEX, pageIndex);
        params.put(HttpRequestField.PAGE_SIZE, pageSize);

        ApiManager.getInitialize(context, false, 0).getFetchPaginationOrders(HttpRequestParams.FETCH_PAGINATION_ORDERS, params, handler);
    }

    /**
     * 取消订单
     *
     * @param context
     * @param orderId
     * @param handler
     */
    public static void cancelOrder(Context context, long orderId, ApiHandler handler) {
        long uid = AppBridgeUtils.getIns().getUid();

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.UID, uid);
        params.put(HttpRequestField.ORDER_ID, orderId);

        ApiManager.getInitialize(context, true, 0).cancelOrder(HttpRequestParams.CANCEL_ORDER, params, handler);
    }

    /**
     * 用户评价
     *
     * @param context
     * @param orderId
     * @param handler
     */
    public static void submitReview(Context context, long orderId, int rank, String content, ApiHandler handler) {
        long uid = AppBridgeUtils.getIns().getUid();

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.UID, uid);
        params.put(HttpRequestField.ORDER_ID, orderId);
        params.put(HttpRequestField.RANK, rank);
        params.put(HttpRequestField.CONTENT, content);

        ApiManager.getInitialize(context, true, 0).submitReview(HttpRequestParams.SUBMIT_REVIEW, params, handler);
    }

    /**
     * 获取常用车辆
     *
     * @param context
     * @param handler
     */
    public static void fetchCommonCars(Context context, long version, boolean isProgress, ApiHandler handler) {
        long uid = AppBridgeUtils.getIns().getUid();

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.UID, uid);
        params.put(HttpRequestField.VERSION, version);

        ApiManager.getInitialize(context, isProgress, 0).fetchCommonCars(HttpRequestParams.FETCH_COMMON_CARS, params, handler);
    }

    /**
     * 获取洗车订单
     *
     * @param context
     * @param orderId
     * @param isProgress
     * @param handler
     */
    public static void fetchOrder(Context context, long orderId, boolean isProgress, ApiHandler handler) {
        long uid = AppBridgeUtils.getIns().getUid();
        long pid = AppBridgeUtils.getIns().getPid();

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.UID, uid);
        params.put(HttpRequestField.P_ID, pid);
        params.put(HttpRequestField.ORDER_ID, orderId);

        ApiManager.getInitialize(context, isProgress, 0).fetchOrder(HttpRequestParams.FETCH_ORDER, params, handler);
    }

    /**
     * 获取开方的洗车城市列表
     *
     * @param context
     * @param version
     * @param isProgress
     * @param handler
     */
    public static void fetchCities(Context context, long version, boolean isProgress, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.VERSION, version);

        ApiManager.getInitialize(context, isProgress, 0).fetchCities(HttpRequestParams.FETCH_CITIES, params, handler);
    }

    public static void fetchCityRegions(Context context, long cityId, long version, boolean isProgress, ApiHandler handler) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(HttpRequestField.CITY_ID, cityId);
        params.put(HttpRequestField.VERSION, version);

        ApiManager.getInitialize(context, isProgress, 0).fetchCityRegions(HttpRequestParams.FETCH_CITY_REGIONS, params, handler);
    }

}
