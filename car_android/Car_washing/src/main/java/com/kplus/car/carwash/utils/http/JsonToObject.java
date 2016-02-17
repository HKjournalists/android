package com.kplus.car.carwash.utils.http;

import com.alibaba.fastjson.JSON;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.utils.Log;

import org.json.JSONObject;

/**
 * Description：fastjson相互转换的类
 * <br/><br/>Created by Fu on 2015/5/17.
 * <br/><br/>
 */
public class JsonToObject {

    private static final String TAG = "JsonToObject";

    public static BaseInfo getBaseInfo(JSONObject resonse) {
        return getJsonToModel(resonse, BaseInfo.class);
    }

    public static BaseInfo getJsonToModel(JSONObject resonse, Class<? extends BaseInfo> cls) {
        try {
            return JSON.parseObject(resonse.toString(), cls);
        } catch (Exception e) {
            Log.trace(TAG, "解析json到实体转换失败！");
            e.printStackTrace();
        }
        return null;
    }

    public static String getModeltoJson(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            Log.trace(TAG, "实体转换json失败！");
            e.printStackTrace();
        }
        return null;
    }
}
