package com.kplus.car.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.carwash.utils.CNNumberUtils;
import com.kplus.car.model.CarService;
import com.kplus.car.model.CarServiceGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：汽车服务工具类
 * <br/><br/>Created by FU ZHIXUE on 2015/8/13.
 * <br/><br/>
 */
public class CarServicesUtil {

    public static final String KEY_PARAM_VALUE = "key-param-value";
    private static final String TRANSITION_APP_ID = "99999999";

    private static final String REPLACE_UID = "{{uId}}";
    private static final String REPLACE_PID = "{{pId}}";
    private static final String REPLACE_USER_ID = "{{userId}}";
    private static final String REPLACE_CITY_ID = "{{cityId}}";
    private static final String REPLACE_CITY_NAME = "{{cityName}}";
    private static final String REPLACE_LAT = "{{lat}}";
    private static final String REPLACE_LNG = "{{lng}}";
    private static final String REPLACE_PROVINCE = "{{province}}";
    private static final String REPLACE_SERVICE_ID = "{{serviceId}}";

    private static Map<String, String> mReplaceMap;
    private static CarService mLastService = null;

    private CarServicesUtil() {
    }

    public static void onLoginResult(Context context) {
        onClickCarServiceItem(context, mLastService);
    }

    private static String replaceData(String url, CarService carService) {
        initReplaceData(carService);

        for (Map.Entry<String, String> entry : mReplaceMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null)
                url = url.replace(key, value);
        }
        return url;
    }

    private static void initReplaceData(CarService carService) {
        KplusApplication mApp = KplusApplication.getInstance();

        long uid = mApp.getId();
        long pid = mApp.getpId();
        long userId = mApp.getUserId();
        long cityId = CNNumberUtils.stringToLong(mApp.getCityId());
        String cityName = mApp.getCityName();
        String province = mApp.getProvince();
        BDLocation location = mApp.getLocation();
        double lng = 0;
        double lat = 0;
        if (null != location) {
            lng = location.getLongitude();
            lat = location.getLatitude();
        }
        long serviceId = (carService.getId() == null ? 0 : carService.getId());
        if (null == mReplaceMap) {
            mReplaceMap = new HashMap<>();
        } else {
            mReplaceMap.clear();
        }
        mReplaceMap.put(REPLACE_UID, uid + "");
        mReplaceMap.put(REPLACE_PID, pid + "");
        mReplaceMap.put(REPLACE_USER_ID, userId + "");
        mReplaceMap.put(REPLACE_CITY_ID, cityId + "");
        mReplaceMap.put(REPLACE_CITY_NAME, cityName);
        mReplaceMap.put(REPLACE_LAT, lat + "");
        mReplaceMap.put(REPLACE_LNG, lng + "");
        mReplaceMap.put(REPLACE_PROVINCE, province);
        mReplaceMap.put(REPLACE_SERVICE_ID, "" + serviceId);
    }

    /**
     * 点击汽车服务的处理入口统一
     *
     * @param carService 点击的服务
     */
    public static void onClickCarServiceItem(Context context, CarService carService) {
        if (null == context || null == carService) {
            return;
        }

        String motionType = carService.getMotionType();
        String motionValue = carService.getMotionValue();
        String transitionUrl = carService.getTransitionUrl();

        Map<String, String> map = new HashMap<>();
        map.put("serviceName", carService.getName());
        EventAnalysisUtil.onEvent(context, "click_qcfw_service", "点击汽车服务项目", map);

        mLastService = carService.copy();

        if (!StringUtils.isEmpty(motionType)) {
            long uid = KplusApplication.getInstance().getId();

            // 判断是否Url类型的动作
            if (ServicesActionUtil.MOTION_TYPE_URL.equals(motionType)
                    || ServicesActionUtil.MOTION_TYPE_WEBAPP.equals(motionType)) {
                // 如果过渡页URL不为空 伪造过渡页需要的motionValue
                if (!TextUtils.isEmpty(transitionUrl)) {
                    motionType = ServicesActionUtil.MOTION_TYPE_WEBAPP;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("appId", TRANSITION_APP_ID);
                        jsonObject.put("startPage", transitionUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    motionValue = jsonObject.toString();
                }
            } else if (ServicesActionUtil.MOTION_TYPE_NATIVE.equals(motionType)) {
                // 上门汽车、我的订单要登录
                if ((ServicesActionUtil.MOTION_VALUE_WASHING.equalsIgnoreCase(motionValue)
                        || ServicesActionUtil.MOTION_VALUE_ORDERLIST.equalsIgnoreCase(motionValue))
                        && uid == 0) {
                    Intent intent = new Intent(context, PhoneRegistActivity.class);
                    ((Activity) context).startActivityForResult(intent, Constants.REQUEST_TYPE_LOGIN);
                    return;
                }
            }

            if (!TextUtils.isEmpty(motionValue)) {
                // 如果链接 需要uId就是说明需要登录
                int uIdIndex = motionValue.indexOf(REPLACE_UID);
                int pIdIndex = motionValue.indexOf(REPLACE_PID);
                int userIdIndex = motionValue.indexOf(REPLACE_USER_ID);
                if ((uIdIndex != -1 || pIdIndex != -1 || userIdIndex != -1)
                        && uid == 0) {
                    // 判断是否已经登陆 并执行相应事件
                    mLastService.setMotionType(motionType);
                    Intent intent = new Intent(context, PhoneRegistActivity.class);
                    ((Activity) context).startActivityForResult(intent, Constants.REQUEST_TYPE_LOGIN);
                    return;
                }
                // 数据替换
                motionValue = replaceData(motionValue, carService);
            }

            // 点击的相关操作
            ServicesActionUtil servicesAction = new ServicesActionUtil(context);
            servicesAction.onClickAction(motionType, motionValue);
        }

        mLastService = null;
    }

    /**
     * 获取服务推荐标签
     *
     * @param flag flag
     * @return resId
     */
    public static int getServiceFlag(int flag) {
        int resId;
        switch (flag) {
            case 1: // 新
                resId = R.drawable.icon_new;
                break;
            case 2: // 热
                resId = R.drawable.icon_hot;
                break;
            case 3: // 折
                resId = R.drawable.icon_discount;
                break;
            case 4: // 免
                resId = R.drawable.icon_free;
                break;
            default:
                resId = 0;
                break;
        }
        return resId;
    }

    public static void sortGroup(List<CarServiceGroup> serviceGroups) {
        Collections.sort(serviceGroups, new Comparator<CarServiceGroup>() {
            @Override
            public int compare(CarServiceGroup o1, CarServiceGroup o2) {
                if (null == o1.getSort() || null == o2.getSort()) {
                    return -1;
                }
                long o1Sort = o1.getSort();
                long o2Sort = o2.getSort();
                if (o1Sort < o2Sort) {
                    return -1;
                } else if (o1Sort == o2Sort) {
                    return 0;
                } else if (o1Sort > o2Sort) {
                    return 1;
                }
                return -1;
            }
        });
    }

    public static void sortServices(List<CarService> services) {
        Collections.sort(services, new Comparator<CarService>() {
            @Override
            public int compare(CarService o1, CarService o2) {
                if (null == o1.getSort() || null == o2.getSort()) {
                    return -1;
                }
                long o1Sort = o1.getSort();
                long o2Sort = o2.getSort();
                if (o1Sort < o2Sort) {
                    return -1;
                } else if (o1Sort == o2Sort) {
                    return 0;
                } else if (o1Sort > o2Sort) {
                    return 1;
                }
                return -1;
            }
        });
    }
}
