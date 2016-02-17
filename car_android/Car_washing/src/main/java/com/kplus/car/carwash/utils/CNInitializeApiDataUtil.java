package com.kplus.car.carwash.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.BundleBean;
import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.bean.CarModelTag;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.bean.Contact;
import com.kplus.car.carwash.bean.FetchCitiesResp;
import com.kplus.car.carwash.bean.FetchCityRegionResp;
import com.kplus.car.carwash.bean.FetchCityServiceResp;
import com.kplus.car.carwash.bean.FetchSupportCarModelsResp;
import com.kplus.car.carwash.bean.InitializeReq;
import com.kplus.car.carwash.bean.InitializeResp;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.bean.Position;
import com.kplus.car.carwash.bean.Region;
import com.kplus.car.carwash.bean.ServiceSupportCarTag;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.utils.http.ApiHandler;
import com.kplus.car.carwash.utils.http.HttpRequestHelper;

/**
 * Description：初始化数据处理
 * <br/><br/>Created by FU ZHIXUE on 2015/7/29.
 * <br/><br/>
 */
public class CNInitializeApiDataUtil {
    private static final String TAG = "CNInitializeApiDataUtil";

    private static CNInitializeApiDataUtil ins = null;

    public static CNInitializeApiDataUtil getIns() {
        if (null == ins) {
            synchronized (CNInitializeApiDataUtil.class) {
                ins = new CNInitializeApiDataUtil();
            }
        }
        return ins;
    }

    private CNInitializeApiDataUtil() {
    }

    private long citiesVersion = 0;
    private long servicesVersion = 0;
    private long regionsVersion = 0;
    private long commonCarsVersion = 0;
    private long supportTagsVersion = 0;

    /**
     * 调用初始化数据接口处理，处理完后直接缓存并通过广播通知界面数据已更新好
     *
     * @param context 上下文
     */
    public void initializeApiData(final Context context, final long cityId, String cityName) {
        final long uid = AppBridgeUtils.getIns().getUid();

        // 如果城市为空，默认杭州
        if (TextUtils.isEmpty(cityName)) {
            cityName = "杭州";
        }

        final String tempCityName = cityName;

//        long citiesVersion = CNCitiesUtil.getIns().getVersion();
//        long servicesVersion = CNServicesUtil.getIns().getVersion(cityId);
//        long regionsVersion = CNRegionUtil.getIns().getVersion(cityId);
//        long commonCarsVersion = CNCommonCarUtil.getIns().getVersion();
//        long supportTagsVersion = CNSupportCarTagUtil.getIns().getVersion(cityId);

        CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                citiesVersion = CNCitiesUtil.getIns().getVersion();
                servicesVersion = CNServicesUtil.getIns().getVersion(cityId);
                regionsVersion = CNRegionUtil.getIns().getVersion(cityId);
                commonCarsVersion = CNCommonCarUtil.getIns().getVersion();
                supportTagsVersion = CNSupportCarTagUtil.getIns().getVersion(cityId);
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                // 初始化参数
                InitializeReq req = new InitializeReq(uid, tempCityName, citiesVersion, servicesVersion, regionsVersion, commonCarsVersion, supportTagsVersion);

                HttpRequestHelper.initialize(context, req, new ApiHandler(context) {
                    @Override
                    public void onSuccess(BaseInfo baseInfo) {
                        super.onSuccess(baseInfo);

                        final InitializeResp resp = (InitializeResp) baseInfo;
                        final City locatedCity = resp.getLocatedCity();
                        final long recommendCityId = resp.getRecommendCityId();
                        final BundleBean<City, Long> cityBundle = resp.getCityBundle();
                        final BundleBean<OnSiteService, Long> serviceBundle = resp.getServiceBundle();
                        final BundleBean<Region, Long> regionBundle = resp.getRegionBundle();
                        final BundleBean<Car, String> commonCarBundle = resp.getCommonCarBundle();
                        final BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> supportCarTagBundle = resp.getSupportCarTagBundle();

                        // 用户信息
                        final Contact commonUsedContact = resp.getCommonUsedContact();
                        // 车辆信息
                        final Car commonUsedCar = resp.getCommonUsedCar();
                        // 车辆位置
                        final Position commonUsedPosition = resp.getCommonUsedPosition();
                        // 服务id
                        final long commonUsedServiceId = resp.getCommonUsedServiceId();

                        CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<Void>() {
                            @Override
                            public Void run() {
                                // 更新定位城市和推荐城市id
                                CNLocatedCityUtil.getIns().add(recommendCityId, locatedCity);
                                CNLocatedCityUtil.save();

                                long cityId = recommendCityId;
                                if (null != locatedCity) {
                                    cityId = locatedCity.getId();
                                }

                                // 更新用户消费习惯
                                CNUserHabitsUtil.getIns().add(cityId, commonUsedContact, commonUsedCar, commonUsedPosition, commonUsedServiceId);
                                CNUserHabitsUtil.save();

                                // 更新服务支持车型tag
                                CNSupportCarTagUtil.getIns().del(cityId, supportCarTagBundle.getVersion(), supportCarTagBundle.getDeletedList());
                                CNSupportCarTagUtil.getIns().add(cityId, supportCarTagBundle.getVersion(), supportCarTagBundle.getUpdatedList());
                                CNSupportCarTagUtil.save();

                                // 更新服务城市
                                boolean isDelCity = CNCitiesUtil.getIns().del(cityBundle.getVersion(), cityBundle.getDeletedList());
                                boolean isAddCity = CNCitiesUtil.getIns().add(cityBundle.getVersion(), cityBundle.getUpdatedList());
                                CNCitiesUtil.save();
                                if (isDelCity || isAddCity) {
                                    // 这个广播是洗车指数处接收的
                                    sendResult(CustomBroadcast.ON_CITIES_DATA_ACTION, true);
                                }

                                // 更新服务项目
                                boolean isDelServices = CNServicesUtil.getIns().del(cityId, serviceBundle.getVersion(), serviceBundle.getDeletedList());
                                boolean isAddServices = CNServicesUtil.getIns().add(cityId, serviceBundle.getVersion(), serviceBundle.getUpdatedList());
                                CNServicesUtil.save();
                                // 如果有改动则要发广播通知界面
                                if (isDelServices || isAddServices) {
                                    sendResult(CustomBroadcast.ON_INITIALIZE_SERVICES_DATA_ACTION, true);
                                }

                                // 更新服务区域
                                boolean isDelRegion = CNRegionUtil.getIns().del(cityId, regionBundle.getVersion(), regionBundle.getDeletedList());
                                boolean isAddRegion = CNRegionUtil.getIns().add(cityId, regionBundle.getVersion(), regionBundle.getUpdatedList());
                                CNRegionUtil.save();
                                // 如果有改动则要发广播通知界面
                                if (isDelRegion || isAddRegion) {
                                    sendResult(CustomBroadcast.ON_INITIALIZE_REGION_DATA_ACTION, true);
                                }

                                // 更新常用车辆
                                boolean isDelCars = CNCommonCarUtil.getIns().del(commonCarBundle.getVersion(), commonCarBundle.getDeletedList());
                                boolean isAddCars = CNCommonCarUtil.getIns().add(commonCarBundle.getVersion(), commonCarBundle.getUpdatedList());
                                CNCommonCarUtil.save();
                                // 如果有改动则要发广播通知界面
                                if (isDelCars || isAddCars) {
                                    sendResult(CustomBroadcast.ON_INITIALIZE_COMMOM_CARS_DATA_ACTION, true);
                                }
                                sendResult(CustomBroadcast.ON_INITIALIZE_DATA_ACTION, true);
                                return null;
                            }
                        });
                    }

                    @Override
                    public void onFailure(BaseInfo baseInfo) {
                        super.onFailure(baseInfo);
                        sendResult(CustomBroadcast.ON_INITIALIZE_DATA_ACTION, false);
                        if (null != baseInfo) {
                            Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                            CNCommonManager.makeText(context, baseInfo.getMsg());
                        }
                    }
                });
            }
        });
    }

    public void fetchCities(final Context context) {
        long version = CNCitiesUtil.getIns().getVersion();
        HttpRequestHelper.fetchCities(context, version, false, new ApiHandler(context) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                final FetchCitiesResp resp = (FetchCitiesResp) baseInfo;
                final BundleBean<City, Long> cityBundle = resp.getCityBundle();

                CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<Void>() {
                    @Override
                    public Void run() {
                        boolean isDel = CNCitiesUtil.getIns().del(cityBundle.getVersion(), cityBundle.getDeletedList());
                        boolean isAdd = CNCitiesUtil.getIns().add(cityBundle.getVersion(), cityBundle.getUpdatedList());
                        CNCitiesUtil.save();
                        // 删除或更新成功了才发广播通知更新
                        if (isDel || isAdd) {
                            sendResult(CustomBroadcast.ON_CITIES_DATA_ACTION, true);
                        }
                        return null;
                    }
                });
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                sendResult(CustomBroadcast.ON_CITIES_DATA_ACTION, false);
                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                    CNCommonManager.makeText(context, baseInfo.getMsg());
                }
            }
        });
    }

    public void fetchCityService(final Context context, final long cityId) {
        long version = CNServicesUtil.getIns().getVersion(cityId);
        long supportCarTagVersion = CNSupportCarTagUtil.getIns().getVersion(cityId);
        HttpRequestHelper.getFetchCityServices(context, cityId, version, supportCarTagVersion, new ApiHandler(context) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                FetchCityServiceResp resp = (FetchCityServiceResp) baseInfo;
                final BundleBean<OnSiteService, Long> serviceBundle = resp.getServiceBundle();
                final BundleBean<ServiceSupportCarTag, ServiceSupportCarTag> supportCarTagBundle = resp.getSupportCarTagBundle();

                CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<Void>() {
                    @Override
                    public Void run() {
                        // 更新服务支持车型tag
                        CNSupportCarTagUtil.getIns().del(cityId, supportCarTagBundle.getVersion(), supportCarTagBundle.getDeletedList());
                        CNSupportCarTagUtil.getIns().add(cityId, supportCarTagBundle.getVersion(), supportCarTagBundle.getUpdatedList());
                        CNSupportCarTagUtil.save();

                        // 更新服务项目
                        boolean isDelServices = CNServicesUtil.getIns().del(cityId, serviceBundle.getVersion(), serviceBundle.getDeletedList());
                        boolean isAddServices = CNServicesUtil.getIns().add(cityId, serviceBundle.getVersion(), serviceBundle.getUpdatedList());
                        CNServicesUtil.save();
                        // 如果有改动则要发广播通知界面
                        if (isDelServices || isAddServices) {
                            sendResult(CustomBroadcast.ON_INITIALIZE_SERVICES_DATA_ACTION, true);
                        }
                        return null;
                    }
                });
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                sendResult(CustomBroadcast.ON_INITIALIZE_SERVICES_DATA_ACTION, false);
                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                    CNCommonManager.makeText(context, baseInfo.getMsg());
                }
            }
        });
    }

    public void fetchCityRegions(final Context context, final long cityId) {
        long version = CNRegionUtil.getIns().getVersion(cityId);
        HttpRequestHelper.fetchCityRegions(context, cityId, version, false, new ApiHandler(context) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                FetchCityRegionResp resp = (FetchCityRegionResp) baseInfo;

                final BundleBean<Region, Long> regionBundle = resp.getRegionBundle();

                CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<Void>() {
                    @Override
                    public Void run() {
                        // 更新服务区域
                        boolean isDelRegion = CNRegionUtil.getIns().del(cityId, regionBundle.getVersion(), regionBundle.getDeletedList());
                        boolean isAddRegion = CNRegionUtil.getIns().add(cityId, regionBundle.getVersion(), regionBundle.getUpdatedList());
                        CNRegionUtil.save();
                        // 如果有改动则要发广播通知界面
                        if (isDelRegion || isAddRegion) {
                            sendResult(CustomBroadcast.ON_INITIALIZE_REGION_DATA_ACTION, true);
                        } else {
                            sendResult(CustomBroadcast.ON_INITIALIZE_REGION_DATA_ACTION, false);
                        }
                        return null;
                    }
                });
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                sendResult(CustomBroadcast.ON_INITIALIZE_REGION_DATA_ACTION, false);
                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                    CNCommonManager.makeText(context, baseInfo.getMsg());
                }
            }
        });
    }


    private long brandVersion = 0;
    private long modelVersion = 0;
    private long colorVersion = 0;
    private long tagsVersion = 0;

    public void fetchSupportCarModels(final Context context) {
//        long brandVersion = CNCarBrandUtil.getIns().getVersion();
//        long modelVersion = CNCarModelUtil.getIns().getVersion();
//        long colorVersion = CNCarColorUtil.getIns().getVersion();
//        long tagsVersion = CNCarModelTagUtil.getIns().getVersion();
        CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                brandVersion = CNCarBrandUtil.getIns().getVersion();
                modelVersion = CNCarModelUtil.getIns().getVersion();
                colorVersion = CNCarColorUtil.getIns().getVersion();
                tagsVersion = CNCarModelTagUtil.getIns().getVersion();
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                HttpRequestHelper.getFetchSupportCarModel(context, brandVersion, modelVersion, colorVersion, tagsVersion, false, new ApiHandler(context) {
                    @Override
                    public void onSuccess(BaseInfo baseInfo) {
                        super.onSuccess(baseInfo);

                        FetchSupportCarModelsResp resp = (FetchSupportCarModelsResp) baseInfo;
                        final BundleBean<CarBrand, Long> brandBundle = resp.getBrandBundle();
                        final BundleBean<CarModel, Long> modelBundle = resp.getModelBundle();
                        final BundleBean<CarColor, Long> colorBundle = resp.getColorBundle();
                        final BundleBean<CarModelTag, CarModelTag> carTagBundle = resp.getCarTagBundle();

                        CNCarWashApp.getIns().getThreadPool().submit(new CNThreadPool.Job<Void>() {
                            @Override
                            public Void run() {
                                // 更新车型分类
                                CNCarModelTagUtil.getIns().del(carTagBundle.getVersion(), carTagBundle.getDeletedList());
                                CNCarModelTagUtil.getIns().add(carTagBundle.getVersion(), carTagBundle.getUpdatedList());
                                CNCarModelTagUtil.save();

                                // 更新品牌
                                boolean isDelCarBrand = CNCarBrandUtil.getIns().del(brandBundle.getVersion(), brandBundle.getDeletedList());
                                boolean isAddCarBrand = CNCarBrandUtil.getIns().add(brandBundle.getVersion(), brandBundle.getUpdatedList());
                                CNCarBrandUtil.save();

                                // 更新车型
                                boolean isDelCarModel = CNCarModelUtil.getIns().del(modelBundle.getVersion(), modelBundle.getDeletedList());
                                boolean isAddCarModel = CNCarModelUtil.getIns().add(modelBundle.getVersion(), modelBundle.getUpdatedList());
                                CNCarModelUtil.save();

                                // 更新车辆颜色
                                boolean isDelCarColor = CNCarColorUtil.getIns().del(colorBundle.getVersion(), colorBundle.getDeletedList());
                                boolean isAddCarColor = CNCarColorUtil.getIns().add(colorBundle.getVersion(), colorBundle.getUpdatedList());
                                CNCarColorUtil.save();
                                // 如果有改动则要发广播通知界面
                                if (isDelCarBrand || isAddCarBrand
                                        || isDelCarModel || isAddCarModel
                                        || isDelCarColor || isAddCarColor) {
                                    sendResult(CustomBroadcast.ON_INITIALIZE_SUPPORT_CAR_DATA_ACTION, true);
                                } else {
                                    sendResult(CustomBroadcast.ON_INITIALIZE_SUPPORT_CAR_DATA_ACTION, false);
                                }
                                return null;
                            }
                        });
                    }

                    @Override
                    public void onFailure(BaseInfo baseInfo) {
                        super.onFailure(baseInfo);
                        sendResult(CustomBroadcast.ON_INITIALIZE_SUPPORT_CAR_DATA_ACTION, false);
                        if (null != baseInfo) {
                            Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                            CNCommonManager.makeText(context, baseInfo.getMsg());
                        }
                    }
                });
            }
        });
    }

    private void sendResult(String action, boolean isResult) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("result", isResult);
        CNCarWashApp.getIns().getApplicationContext().sendBroadcast(intent);
    }
}
