package com.kplus.car.carwash.utils.http;

import com.kplus.car.carwash.module.AppBridgeUtils;

/**
 * Created by Fu on 2015/5/8.
 */
public class HttpRequestParams {
    /**
     * 正式环境地址
     */
//    public static final String CAR_WASHING_API_URL = "http://washing.chengniu.com";
    /**
     * 内网服务器地址
     */
//    public static final String CAR_WASHING_API_URL = "http://192.168.10.3:9090";
    /**
     * 外网服务器地址
     */
//    public static final String CAR_WASHING_API_URL = "http://183.136.220.27:9090";
    /**
     * 海洋服务地址
     */
//    public static final String CAR_WASHING_API_URL = "http://192.168.10.72:8080/car-washing-portal";
    /**
     * 开发服务地址
     */
//    public static final String CAR_WASHING_API_URL = "http://192.168.10.9:9090";

    /**
     * 取api接口地址
     *
     * @return
     */
    public static String getApiUrl() {
        return AppBridgeUtils.getIns().getCarWashingApiUrl();
    }


    /**
     * 接口参数类型
     */
    public static final String CONTENT_TYPE = "application/json";

    /**
     * 接口版本
     */
    public static final int API_VERSION = 2;

    /**
     * 初始化数据接口
     */
    public static final String INITIALIZE = "/api/service/initialize";

    /**
     * 定位服务城市
     */
    public static final String LOCATE_SERVING_CITY = "/api/service/locate_serving_city";
    /**
     * 定位服务城市
     */
    public static final String LOCATE_SERVING_CITY_V2 = "/api/service/locate_serving_city_v2";
    /**
     * 获取用户在指定城市的消费习惯
     */
    public static final String FETCH_USER_HABITS = "/api/user/fetch_user_habits";
    /**
     * 获取服务列表
     */
    public static final String FETCH_CITY_SERVICES = "/api/service/fetch_city_services";
    /**
     * 获取城市服务配置
     */
    public static final String FETCH_CITY_CONFIG = "/api/service/fetch_city_config";
    /**
     * 获取城市服务状态
     */
    public static final String FETCH_CITY_SERVING_STATUS = "/api/service/fetch_city_serving_status";
    /**
     * 按服务范围获取库存
     */
    public static final String FETCH_CITY_SERVING_STATUS_BY_REGIONS = "/api/service/fetch_serving_status_by_regions";
    /**
     * 获取适用的代金券
     */
    public static final String FETCH_USABLE_COUPONS = "/api/user/fetch_usable_coupons";
    /**
     * 提交洗车订单
     */
    public static final String SUBMIT_ORDER = "/api/order/submit_order";
    /**
     * 获取车型数据
     */
    public static final String FETCH_SUPPORT_CAR_MODELS = "/api/service/fetch_support_car_models";
    /**
     * 获取洗车订单日志
     */
    public static final String FETCH_ORDER_LOGS = "/api/order/fetch_order_logs";
    /**
     * 获取洗车订单列表
     */
    public static final String FETCH_PAGINATION_ORDERS = "/api/order/fetch_pagination_orders";
    /**
     * 取消订单
     */
    public static final String CANCEL_ORDER = "/api/order/cancel_order";
    /**
     * 用户评价
     */
    public static final String SUBMIT_REVIEW = "/api/order/submit_review";
    /**
     * 获取常用车辆
     */
    public static final String FETCH_COMMON_CARS = "/api/user/fetch_common_cars";

    /**
     * 获取洗车订单
     */
    public static final String FETCH_ORDER = "/api/order/fetch_order";

    /**
     * 获取开方的洗车城市
     */
    public static final String FETCH_CITIES = "/api/service/fetch_cities";
    /**
     * 获取城市服务范围
     */
    public static final String FETCH_CITY_REGIONS = "/api/service/fetch_city_regions";
}
