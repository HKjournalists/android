package com.kplus.car.carwash.common;

import com.kplus.car.carwash.module.AppBridgeUtils;

/**
 * 常量类，存放全局常量，个别专属需求常量单独写类
 *
 * @author Fu Zhixue
 * @date 2015/4/28.
 */
public final class Const {
    // TODO 正式版本，改为 false
    /**
     * 正式版本，改为false
     */
//    public static final boolean IS_DEBUG = true;
    public static final boolean IS_DEBUG = AppBridgeUtils.getIns().isLog();

    public static final int NEGATIVE = -1;
    public static final int NONE = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    /**
     * 车牌字母数字的长度
     */
    public static final int CAR_LICENCE_LENGTH = 6;

    /**
     * 1s
     */
    public static final int ONE_SECOND = 1000;
    /**
     * 1m
     */
    public static final int ONE_MINUTE = 6 * ONE_SECOND;
    /**
     * 半小时
     */
    public static final int HALF_HOUR = 30 * ONE_MINUTE;
    /**
     * 1小时
     */
    public static final int ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 分页的条数
     */
    public static final int PAGE_SIZE = 20;

    /**
     * 手机号正则表达式
     */
    public static final String REGEX_MOBILE_PHONE = "^(13|15|18|17)[0-9]{9}$";

    /**
     * ////////////////////////////////////////////////////////////////////////////////////////////
     * ////////////////////////////////////////支付方式类型////////////////////////////////////////
     * ///////////////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 支付宝手机支付
     */
    public static final int ALI_PAY = 1;
    /**
     * 支付宝wap网页支付
     */
    public static final int ALI_WEBPAY = 2;
    /**
     * 银联手机支付
     */
    public static final int UPOMP_PAY = 3;
    /**
     * 银联网页支付
     */
    public static final int UPOMP_WEBPAY = 4;
    /**
     * 余额支付
     */
    public static final int BALANCE_PAY = 5;
    /**
     * 支付宝手机、余额支付
     */
    public static final int BALANCE_ALI_PAY = 6;
    /**
     * 支付宝wap网页、余额支付
     */
    public static final int BALANCE_ALI_WEB = 7;
    /**
     * 银联手机、余额支付
     */
    public static final int BALANCE_UPOMP_PAY = 8;
    /**
     * 银联网页、余额支付
     */
    public static final int BALANCE_UPOMP_WEBPAY = 9;
    /**
     * 微信支付
     */
    public static final int WECHAT_PAY = 10;
    /**
     * 微信、余额支付
     */
    public static final int BALANCE_WECHAT_PAY = 11;
    /**
     * 连连
     */
    public static final int LIANLIAN_PAY = 12;
    /**
     * 连连网页
     */
    public static final int LIANLIAN_WEBPAY = 13;
    /**
     * 连连、余额支付
     */
    public static final int BALANCE_LIANLIAN_PAY = 14;
    /**
     * 连连网页、余额支付
     */
    public static final int BALANCE_LIANLIAN_WEBPAY = 15;
    /**
     * 新的支付宝类型
     */
    public static final int OPENTRADE_PAY = 16;
    /**
     * 新的支付宝、余额类型
     */
    public static final int BALANCE_OPENTRADE_PAY = 17;

    /**
     * ////////////////////////////////////////////////////////////////////////////////////////////
     * ////////////////////////////////////////支付方式类型END//////////////////////////////////////
     * ///////////////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 缓存数据的目录
     */
    public static final String CACHE_FILE_FOLRDER = "washingfile/";
    /**
     * 中文的正则表达式
     */
    public static final String REGEX_CHINESE = "^([\\u4e00-\\u9fa5]+)$";
}
