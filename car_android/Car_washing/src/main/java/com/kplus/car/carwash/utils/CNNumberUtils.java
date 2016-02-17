package com.kplus.car.carwash.utils;

import com.kplus.car.carwash.common.Const;

/**
 * Description：数据转换处理工具类
 * <br/><br/>Created by Fu on 2015/5/12.
 * <br/><br/>
 */
public class CNNumberUtils {
    public static int floatToInt(float fl) {
        try {
            return (int) fl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Const.NONE;
    }

    public static double stringToDouble(String str) {
        if (CNStringUtil.isEmpty(str))
            return Const.NONE;
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Const.NONE;
    }

    public static long stringToLong(String str) {
        if (CNStringUtil.isEmpty(str))
            return Const.NONE;
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Const.NONE;
    }
}
