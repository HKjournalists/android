package com.kplus.car;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class KplusConstants {
    public static final String SERVER_URL = BuildConfig.API_URL_MAIN_APP;
//        public static final String SERVER_URL = "http://app.qichekb.com";
//    public static final String SERVER_URL = "http://192.168.10.53:8101";
//    public static final String SERVER_URL = "http://192.168.10.3:9880";
//    public static final String SERVER_URL = "http://183.136.220.27:9880";
//    public static final String SERVER_URL = "http://121.40.72.129:8081";
//    public static final String SERVER_URL = "http://192.168.10.38:8080/car_client";
//    public static final String SERVER_URL = "http://192.168.10.108:8080/car_client_truck";

    public static final String ANALYZE_URL = "http://stat.chengniu.com";

    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * ////////////////////////洗车业务api配置  打正式包时要注意//////////////////////////
     * ///////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 根据debug或release自动获取服务器地址
     */
    public static final String CAR_WASHING_API_URL = BuildConfig.API_URL_CAR_WASHING;
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
//    public static final String CAR_WASHING_API_URL = "http://192.168.10.96:8080";
    /**
     * 开发服务地址
     */
//    public static final String CAR_WASHING_API_URL = "http://192.168.10.9:9090";
    /**
     * /////////////////////////////////////////////////////////////////////////////////
     * ////////////////////////////洗车业务api配置End///////////////////////////////////
     * ///////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 洗车里面是否打印日志 debug时会打印
     */
    public static final boolean IS_LOG = BuildConfig.IS_LOG;

    public static final boolean isDebug = false;

//    public static final String CLIENT_APP_KEY = BuildConfig.APP_KEY; //"30001";
//    public static final String CLIENT_APP_SECRET = BuildConfig.APP_SECRET; //"LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";

    public static final String WECHAT_APPID = "wx60ff70d6a7378cd2";
    public static final String WECHAT_APPSECRET = "b75243b0eb6e69fc74e65e7e438123a8";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DB_KEY_USERID = "user_id";// 本地数据库_用户IDkey
    public static final String DB_KEY_USER = "user";// 本地数据库_用户名key
    public static final String DB_KEY_CLIENT = "client";// 本地数据库_设备号key
    public static final String DB_KEY_PHONEID = "pId";// 本地数据库_绑定手机ID
    public static final String DB_KEY_ORDER_CONTACTNAME = "contactName";// 本地数据库_订单联系人姓名
    public static final String DB_KEY_ORDER_CONTACTPHONE = "contactPhone";// 本地数据库_订单联系号码
    public static final String DB_KEY_ORDER_STATUS_CHANGE = "order_status_change";// 本地数据库_订单状态变更
    public static final String DB_KEY_VEHICLE_OWNERNAME_PREFIX = "ownerName";// 本地数据库_车主姓名
    public static final String DB_KEY_VEHICLE_DRIVING_PREFIX = "driving_prefix_";// 本地数据库_车辆行驶证正面复印件
    public static final String DB_KEY_VEHICLE_DRIVING_PREFIX2 = "driving_prefix2_";// 本地数据库_车辆行驶证背面复印件
    public static final String DB_KEY_AGAINST_QUERY_TIME = "against_query_time_";// 本地数据库_违章数据更新时间
    public static final String DB_KEY_CITY_DATA_TIME = "city_data_time";// 本地数据库_城市规则更新时间
    public static final String DB_KEY_BRAND_MODE_TIME = "Brand_mode_time";// 本地数据库_车型更新时间
    public static final String DB_KEY_TAB_SERVICE_CHANGE = "service_tab_status_change";
    public static final String DB_KEY_EMERGENCY_DRIVING = "emergency_driving";// 本地数据库_紧急救援车辆行驶证
    public static final String DB_KEY_PUSH_START_TIME = "push_start_time";// 违章推送接收开始时间
    public static final String DB_KEY_PUSH_END_TIME = "push_end_time";// 违章推送接收结束时间
    public static final String DB_KEY_NEW_MESSAGE_NUMBER = "daze_new_message_number";// 新消息数
    public static final String DB_KEY_CITY_ID = "daze_city_id";
    public static final String DB_KEY_CITY_NAME = "daze_city_name";
    public static final String DB_KEY_PROVINCE = "daze_province";
    public static final String DB_KEY_ADDRESS = "daze_address";
    public static final String DB_KEY_NEVER_REMIND = "daze_never_remind";//添加车辆引导不再提醒
    public static final String DB_KEY_NEXT_TIME = "daze_next_time";//添加车辆引导下次再说

    public static final String ACTION_PROCESS_CUSTOM_MESSAGE = "GexinSdkMsgReceiver_process_custom_message";//认证透传信息
    public static final String ACTION_GET_USERID = "GexinSdkMsgReceiver_get_userid";//获取到userid
    public static final String ACTION_GET_SYN = "GexinSdkMsgReceiver_get_syn";//获取到车辆同步数据

    public static final int SELECT_CITY_FROM_TYPE_AUTHENTICATION = 1;
    public static final int SELECT_CITY_FROM_TYPE_VEHICLE_ADD = 2;
    public static final int SELECT_CITY_FROM_TYPE_VEHICLE_DETAIL = 3;
    public static final int SELECT_CITY_FROM_TYPE_USERINFO = 4;
    public static final int SELECT_CITY_FROM_TYPE_HOME = 5;

    public static final int FLAG_CANCEL_ADD = 33;

    public static final int SHARE_ORDER = 3;
    public static final int SHARE_APP = 4;

    public static final int PAGE_SIZE = 10;

    public static final int ALI_PAY = 1;//支付宝手机支付
    public static final int ALI_WEBPAY = 2;//支付宝wap网页支付
    public static final int UPOMP_PAY = 3;//银联手机支付
    public static final int UPOMP_WEBPAY = 4;//银联网页支付
    public static final int BALANCE_PAY = 5;//余额支付
    public static final int BALANCE_ALI_PAY = 6;//支付宝手机、余额支付
    public static final int BALANCE_ALI_WEB = 7;//支付宝wap网页、余额支付
    public static final int BALANCE_UPOMP_PAY = 8;//银联手机、余额支付
    public static final int BALANCE_UPOMP_WEBPAY = 9;//银联网页、余额支付
    public static final int WECHAT_PAY = 10;//微信支付
    public static final int BALANCE_WECHAT_PAY = 11;//微信、余额支付
    public static final int LIANLIAN_PAY = 12;//连连
    public static final int LIANLIAN_WEBPAY = 13;//连连网页
    public static final int BALANCE_LIANLIAN_PAY = 14;//连连、余额支付
    public static final int BALANCE_LIANLIAN_WEBPAY = 15;//连连网页、余额支付
    public static final int OPENTRADE_PAY = 16;
    public static final int BALANCE_OPENTRADE_PAY = 17;

    public static final int ALERT_VEHICLE_MODE_SELF_DEFINE = 1;
    public static final int ALERT_CHOOSE_WHETHER_EXECUTE = 2;
    public static final int ALERT_ONLY_SHOW_MESSAGE = 3;
    public static final int ALERT_INPUT_VERIFY_CODE = 4;
    public static final int ALERT_INPUT_VEHICLE_OWNER_NAME = 5;
    public static final int ALERT_JIAZHAO_RESULT = 6;
    public static final int ALERT_PROMO_CODE_EXCHANGE = 7;
    public static final int ALERT_INPUT_REMARK = 8;
    public static final int ALERT_INPUT_SHOP_NAME = 9;
    public static final int ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_VIEW_AGAINST_RESULT = 1;
    public static final int ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_ADD_VEHICLE = 3;
    public static final int ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_WHERT_TO_REMIND = 4;
    public static final int ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_WHERT_TO_RESCUE = 5;
    public static final int ALERT_JIAZHAO_RESULT_SUBTYPE_JIAZHAO_SUCCESS = 11;
    public static final int ALERT_JIAZHAO_RESULT_SUBTYPE_JIAZHAO_FAILED = 12;
    public static final int TYPE_CAN_PAY_ONLINE = 1;//可在线缴费
    public static final int TYPE_DISPOSING = 2;//处理中
    public static final int TYPE_CAN_TICKET_PAY = 3;//罚单代缴
    public static final int TYPE_NOT_SUPPORT = 4;//不可处理
    public static final int TYPE_DISPOSED = 5;//已处理

    public static final int REGIST_RESULT_SUCCESS = 0;
    public static final int REGIST_RESULT_IDEL = 1;
    public static final int REGIST_RESULT_REGISTING = 2;
    public static final int REGIST_RESULT_FAIL = 3;
    public static final int REGIST_RESULT_NETWORK_DISCONNECT = 4;
    public static final int REGIST_RESULT_TIME_ERROR = 5;
    public static final int REGIST_RESULT_GET_DEVICE_ID_FAIL = 6;

    public static final String START_UP_VERSION = "1";

    public static final String OPENIM_SECRET = "b6274baa7d302b72a4cf579bc48a9ae3";

    public static String orderStatus(int status) {
        switch (status) {
            case 1:
                return "暂时无法支付";
            case 2:
                return "待支付";
            case 3:
//                return "已支付,请完善证件信息";
                return "上传证件";
            case 4:
//                return "已支付,请完善证件信息";
                return "上传证件";
            case 5:
                return "审核中";
            case 6:
//                return "未审核通过";
                return "审核失败";
            case 7:
                return "审核通过";
            case 8:
                return "已评价";
            case 10:
                return "处理中";
            case 11:
                return "处理失败";
            case 12:
                return "处理成功";
            case 13:
                return "正在退款";
            case 14:
                return "已退款";
            case 20:
                return "已关闭";
            case -1:
                return "已删除";

            default:
                return "状态异常";
        }
    }

    public static String serviceorderStatus(int status) {
        switch (status) {
            case 1:
                return "暂时无法支付";
            case 2:
                return "待支付";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 10:
                return "已支付";
            case 8:
                return "已评价";
            case 11:
                return "处理失败";
            case 12:
                return "处理成功";
//            return "处理完成";
            case 13:
                return "正在退款";
            case 14:
                return "已退款";
            case 20:
                return "已关闭";
            case -1:
                return "已删除";

            default:
                return "状态异常";
        }
    }

    public static String c2cOrderStatus(int status) {
        switch (status) {
            case 1:
                return "暂时无法支付";
            case 2:
                return "待支付";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return "已支付";
            case 10:
                return "处理中";
            case 11:
                return "处理失败";
            case 12:
                return "处理成功";
            case 13:
                return "待退款";
            case 14:
                return "已退款";
            case 15:
                return "确认完成";
            case 16:
                return "已评价";
            case 20:
                return "已关闭";
            case -1:
                return "已删除";

            default:
                return "状态异常";
        }
    }

    public static String orderStatusDesc(int status) {
        switch (status) {
            case 1:
                return "客服将在2小时内联系您，请耐心等待";
            case 2:
                return "支付完成后再上传代办所需的各项资料";
            case 3:
                return "请尽快上传代办所需的各项资料";
            case 4:
                return "请尽快上传代办所需的各项资料";
            case 5:
                return "资料已齐备，正在审核资料是否符合代办要求";
            case 6:
                return "证照不清晰或其他原因，客服将主动与您联系，请耐心等待";
            case 7:
                return "已进入处理队列，2-7个工作日内处理完毕";
            case 10:
                return "正在处理，请耐心等待";
            case 11:
                return "无法处理您的违章，所有款项将在3个工作日内退回你的付款账户";
            case 12:
                return "由于交管系统数据同步有时差，最新状态可能需要一定的延时。建议您过一天再到交管系统确认处理状态";
            case 13:
                return "所有款项将在3个工作日内退回您的付款账户";
            case 14:
                return "已退款到您的付款账户，请注意查收";
            case 20:
                return "该订单已关闭，如有需要，请重新下单";
            case -1:
                return "订单已删除";

            default:
                return "订单数据错误，请稍后重试";
        }
    }

    public static String orderStatusBg(int status) {
        switch (status) {
            case 2: // 待付款
                return "daze_text_orange_s_bg";
            case 5: // 待审核
            case 10: // 处理中
                return "daze_text_blue_s_bg";
            case 6: // 审核失败
            case 11: // 处理失败
                return "daze_text_red_s_bg";
            case 3: // 已付款
            case 4: // 未完善证件信息
            case 7: // 审核通过
            case 12: // 处理成功
                return "daze_text_green_s_bg";
            case 13: // 待退款
            case 14: // 已退款
            case 20: // 已关闭
                return "daze_text_grey_s_bg";

            default:
                return "daze_text_red_s_bg";
        }
    }

    public static String orderStatusIcon(int status) {
        switch (status) {
            case 2: // 待付款
            case 5: // 待审核
            case 10: // 处理中
            case 13: // 待退款
                return "daze_icon_warn";
            case 6: // 审核失败
            case 11: // 处理失败
                return "daze_icon_error";
            case 3: // 已付款
            case 4: // 未完善证件信息
            case 7: // 审核通过
            case 12: // 处理成功
            case 14: // 已退款
            case 20: // 已关闭
                return "daze_icon_success";

            default:
                return "daze_icon_warn";
        }
    }

    public static String orderStatusLayoutBg(int status) {
        switch (status) {
            case 2: // 待付款
            case 5: // 待审核
            case 10: // 处理中
            case 13: // 待退款
                return "daze_light_blue1";
            case 6: // 审核失败
            case 11: // 处理失败
                return "daze_light_red";
            case 3: // 已付款
            case 4: // 未完善证件信息
            case 7: // 审核通过
            case 12: // 处理成功
            case 14: // 已退款
            case 20: // 已关闭
                return "daze_light_green";

            default:
                return "daze_light_blue1";
        }
    }

    public static final int UPLOAD_AUTH_LICENCE_PHOTO = 10;
    public static final int UPLOAD_TICKET = 11;
    public static final int UPLOAD_DRIVING = 1;
    public static final int UPLOAD_DRIVER = 2;
    public static final int UPLOAD_CARD = 3;
    public static final int UPLOAD_CARD2 = 6;

    //广告位标识
    public static final String ADVERT_VEHICLE_BODY = "vehicleBody";//我的菜中部
    public static final String ADVERT_SERVICE_HEAD = "serviceHead";//汽车服务头部
    public static final String ADVERT_SERVICE_HEAD_NEW = "serviceHeadNew";// 汽车服务原生版头部
    public static final String ADVERT_PROVIDER_HEAD = "providerHead";//管家头部
    public static final String ADVERT_USER_BODY = "userBody";//个人中心中部
    public static final String ADVERT_HOME = "home";//首页弹屏
    public static final String ADVERT_TAB = "tab";//tab菜单
    public static final String ADVERT_VEHICLE_DETAIL_HEAD = "vehicleDetailHead";//车辆详情头部
    public static final String ADVERT_OIL_HEAD = "oilHead";//油卡充值头部
    public static final String ADVERT_USER_LOGIN = "userLogin";// 登录页面下方广告位
    public static final String ADVERT_NEW_USER = "newUser";// 新用户弹窗

    public static String appChannel = null;
    public static String CLIENT_APP_KEY = null;
    public static String CLIENT_APP_SECRET = null;

    public static void initData(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appChannel = info.metaData.getString("UMENG_CHANNEL");
            initKeyAndSecret();
            if (isDebug) {
                Log.i("KplusConstants", "appChannel===>" + appChannel);
                Log.i("KplusConstants", "CLIENT_APP_KEY===>" + CLIENT_APP_KEY);
                Log.i("KplusConstants", "CLIENT_APP_SECRET===>" + CLIENT_APP_SECRET);
            }
        } catch (Exception e) {
            appChannel = "android_default";
            CLIENT_APP_KEY = "10000";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
            e.printStackTrace();
        }
    }

    private static void initKeyAndSecret() {
        if (appChannel.equals("android_default")) {
            CLIENT_APP_KEY = "10000";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("Grayscale_existing")) {
            CLIENT_APP_KEY = "fb43a6a9d9";
            CLIENT_APP_SECRET = "0043476e190ed99d";
        } else if (appChannel.equals("Grayscale_new")) {
            CLIENT_APP_KEY = "c34776900c";
            CLIENT_APP_SECRET = "a710af6fed27bcf8";
        } else if (appChannel.equals("android_xiaomi")) {
            CLIENT_APP_KEY = "30002";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_360")) {
            CLIENT_APP_KEY = "30003";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_91")) {
            CLIENT_APP_KEY = "30004";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_yyb")) {
            CLIENT_APP_KEY = "30005";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidu")) {
            CLIENT_APP_KEY = "30006";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_lenovo")) {
            CLIENT_APP_KEY = "30007";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_huawei")) {
            CLIENT_APP_KEY = "30008";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_mumayi")) {
            CLIENT_APP_KEY = "30009";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_yyh")) {
            CLIENT_APP_KEY = "30010";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_jifeng")) {
            CLIENT_APP_KEY = "30011";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_hiapk")) {
            CLIENT_APP_KEY = "30012";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_uc")) {
            CLIENT_APP_KEY = "30014";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidutg")) {
            CLIENT_APP_KEY = "30017";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_wandoujia")) {
            CLIENT_APP_KEY = "30001";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_keke")) {
            CLIENT_APP_KEY = "30030";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_taobao")) {
            CLIENT_APP_KEY = "30033";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_jinli")) {
            CLIENT_APP_KEY = "30037";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_common")) {
            CLIENT_APP_KEY = "30099";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baiducpd")) {
            CLIENT_APP_KEY = "30039";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_360tg")) {
            CLIENT_APP_KEY = "30040";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_yybsdk")) {
            CLIENT_APP_KEY = "30041";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_yybtg")) {
            CLIENT_APP_KEY = "30042";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_fensi")) {
            CLIENT_APP_KEY = "30043";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_meizu")) {
            CLIENT_APP_KEY = "30044";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baiducpt")) {
            CLIENT_APP_KEY = "30045";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_360cpd")) {
            CLIENT_APP_KEY = "30046";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_360cpt")) {
            CLIENT_APP_KEY = "30047";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_yybxjl")) {
            CLIENT_APP_KEY = "30048";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baiduwm")) {
            CLIENT_APP_KEY = "30049";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidutg01")) {
            CLIENT_APP_KEY = "30050";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidutg02")) {
            CLIENT_APP_KEY = "30051";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidutg03")) {
            CLIENT_APP_KEY = "30052";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidutg04")) {
            CLIENT_APP_KEY = "30053";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidutg05")) {
            CLIENT_APP_KEY = "30054";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_jinshanliebao")) {
            CLIENT_APP_KEY = "30055";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpa01")) {
            CLIENT_APP_KEY = "30057";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpa02")) {
            CLIENT_APP_KEY = "30058";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpc01")) {
            CLIENT_APP_KEY = "30059";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpc02")) {
            CLIENT_APP_KEY = "30060";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpt01")) {
            CLIENT_APP_KEY = "30061";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpt02")) {
            CLIENT_APP_KEY = "30062";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_weixin_gdt")) {
            CLIENT_APP_KEY = "30063";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_baidutb")) {
            CLIENT_APP_KEY = "30064";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_shenmass")) {
            CLIENT_APP_KEY = "30065";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_xinlangxw")) {
            CLIENT_APP_KEY = "30066";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_jinritt")) {
            CLIENT_APP_KEY = "30067";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_sougouss")) {
            CLIENT_APP_KEY = "30068";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_lenovocpd")) {
            CLIENT_APP_KEY = "30069";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_91zhuomian")) {
            CLIENT_APP_KEY = "30070";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_zaker")) {
            CLIENT_APP_KEY = "30073";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_sohu")) {
            CLIENT_APP_KEY = "30074";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_qqbrowser")) {
            CLIENT_APP_KEY = "30075";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_weixingg")) {
            CLIENT_APP_KEY = "30076";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_weixingw")) {
            CLIENT_APP_KEY = "30077";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_appfenxiang")) {
            CLIENT_APP_KEY = "30078";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_91cpd")) {
            CLIENT_APP_KEY = "30079";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_hiapkcpd")) {
            CLIENT_APP_KEY = "30080";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_momo")) {
            CLIENT_APP_KEY = "30081";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_jinrittdt")) {
            CLIENT_APP_KEY = "30082";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_guangdaintong")) {
            CLIENT_APP_KEY = "30018";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_shipin")) {
            CLIENT_APP_KEY = "30083";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_fenghuang")) {
            CLIENT_APP_KEY = "30084";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_weixinggz")) {
            CLIENT_APP_KEY = "30085";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpa03")) {
            CLIENT_APP_KEY = "30086";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpa04")) {
            CLIENT_APP_KEY = "30087";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_ggcpa05")) {
            CLIENT_APP_KEY = "30088";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_qixiang")) {
            CLIENT_APP_KEY = "30089";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_alipay")) {
            CLIENT_APP_KEY = "30090";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_wandoujia_t")) {
            CLIENT_APP_KEY = "30029";
            CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
        } else if (appChannel.equals("android_yidianzixun")) {
            CLIENT_APP_KEY = "bbdd86e48b";
            CLIENT_APP_SECRET = "3d6df40a1ea7d43f";
        } else if (appChannel.equals("android_kupai")) {
            CLIENT_APP_KEY = "2ec0e025a6";
            CLIENT_APP_SECRET = "3eed86877b8f374b";
        } else if (appChannel.equals("android_dianxin01")) {
            CLIENT_APP_KEY = "67b07619bb";
            CLIENT_APP_SECRET = "898bb980834d087a";
        } else if (appChannel.equals("android_dianxin02")) {
            CLIENT_APP_KEY = "d8308021b1";
            CLIENT_APP_SECRET = "738dd0e2fafe90cf";
        } else if (appChannel.equals("android_dianxin03")) {
            CLIENT_APP_KEY = "ab45733d0d";
            CLIENT_APP_SECRET = "bc9d5078961a23cb";
        } else if (appChannel.equals("android_tequan")) {
            CLIENT_APP_KEY = "d9b8e9007f";
            CLIENT_APP_SECRET = "dfbda0c4879accb1";
        }
    }
}
