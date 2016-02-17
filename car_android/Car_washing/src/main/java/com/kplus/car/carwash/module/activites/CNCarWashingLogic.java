package com.kplus.car.carwash.module.activites;

import android.content.Context;
import android.content.Intent;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.bean.CarModelTag;
import com.kplus.car.carwash.bean.Coupon;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.bean.OrderLog;
import com.kplus.car.carwash.bean.OrderPayment;
import com.kplus.car.carwash.bean.Review;
import com.kplus.car.carwash.bean.ServiceSupportCarTag;
import com.kplus.car.carwash.callback.IPopupItemClickListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.module.LogAction;
import com.kplus.car.carwash.utils.CNCarModelTagUtil;
import com.kplus.car.carwash.utils.CNCarModelUtil;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNResourcesUtil;
import com.kplus.car.carwash.utils.CNSupportCarTagUtil;
import com.kplus.car.carwash.utils.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Description  基础逻辑类
 * Created by Fu on 2015/5/11.
 */
public class CNCarWashingLogic {
    private static final String TAG = "CNCarWashingLogic";

    /**
     * 标识当前位置的id
     */
    public static final int TYPE_MY_LOCATION_ID = -9999;
    /**
     * 不使用代金券支付的id
     */
    public static final int TYPE_NOT_USE_COUPON_PAY = -9998;

    public static final int TYPE_SELECTED_SERVICE = 1001;
    public static final int TYPE_CAR_INFO = 1002;
    /**
     * 选择位置的requestCode
     */
    public static final int TYPE_CAR_LOCATION = 1003;
    /**
     * 查看服务详情的requestCode
     */
    public static final int TYPE_REVIEW_SERVICE_DETAILS = 1004;
    /**
     * 去评价
     */
    public static final int TYPE_REVIEW_ORDER_DETAILS = 1005;
    /**
     * 查看订单详情
     */
    public static final int TYPE_ORDER_DETAILS = 1006;
    /**
     * 分享内容的requestCode
     */
    public static final int TYPE_SHARE_IMAGES_CODE = 1007;

    /**
     * 选择的服务
     */
    public static final String KEY_SERVICE_SELECTED = "key-service-selected";

    /**
     * 选择位置回来的参数名
     */
    public static final String KEY_SELECT_CAR_LOCATION = "key-car-location-result";
    /**
     * 车辆位置界面选择的城市
     */
    public static final String KEY_SELECT_CAR_LOC_CITY = "key-selected-car-loccity";
    /**
     * 选择的服务范围
     */
    public static final String KEY_SELECT_REGION_AREAS = "key-selected-region-areas";
    /**
     * 查看服务详情
     */
    public static final String KEY_REVIEW_SERVICE_DETAILS = "key-review_serdetails";
    /**
     * 选择的车辆信息
     */
    public static final String KEY_SELECTED_CAR_INFO = "key-selected-car-inf0";
    /**
     * 从我的订单中进入订单详情
     */
    public static final String KEY_ORDER_DETAILS = "key-order-details";
    public static final String KEY_ORDER_DETAILS_ORDER_ID = "key-order-details-orderid";
    /**
     * 上次的位置，进入地图时，直接显示
     */
    public static final String KEY_SREVING_LOCATION = "key-evaluate-success";
    /**
     * 查看日子图片的图片地址集合
     */
    public static final String KEY_REVIEW_LOG_PIC_URLS = "key-pic-urls";
    /**
     * 点击查看图片的第几张图片
     */
    public static final String KEY_REVIEW_LOG_PIC_POSITION = "key-pic-position";
    /**
     * 图片类型
     */
    public static final String KEY_REVIEW_PIC_TYPE = "key-pic-type";
    public static final int TYPE_PIC_HEADER = 0;
    public static final int TYPE_PIC_OTHER = 1;

    /**
     * 图片类型
     */
    public static final int SHARE_IMAGE_CONTENT_TYPE = 2;
    /**
     * 文本图片类型
     */
    public static final int SHARE_TEXT_AND_IMAGE_CONTENT_TYPE = 3;

    /**
     * 分享订单日志
     */
    public static final int SHARE_TYPE_ORDERLOG = 1;
    /**
     * 分享服务
     */
    public static final int SHARE_TYPE_SERVICES = 2;

    /**
     * 分享类型
     */
    public static final String KEY_SHARE_TYPE = "key-share-type";
    /**
     * 分享的图片
     */
    public static final String KEY_SHARE_IMAGES = "key-share-images";
    /**
     * 分享服务传的参数
     */
    public static final String KEY_SHARE_CONTENT = "key-share-content";

    /**
     * 分享的内容
     */
    public static final String SHARE_CONTENT = "我使用了橙牛汽车管家的上门洗车服务，我的爱车焕然一新！http://chengniu.com/m19/";
    public static final String SHARE_CONTENT_SERVICE = "%1$s，省时省力省钱，推荐你也试试！";
    /**
     * 分享跳转的url
     */
    public static final String SHARE_WEB_URL = "http://chengniu.com/m19/";

    /**
     * 分享结果
     */
    public static final String KEY_SHARE_RESULT_CODE = "ky-share-result-code";
    /**
     * 分享成功
     */
    public static final int SHARE_RESULT_SUCCESS = 0;
    /**
     * 分享失败
     */
    public static final int SHARE_RESULT_FAILED = 1;
    /**
     * 分享取消
     */
    public static final int SHARE_RESULT_CANCEL = 2;

    // 服务状态 1: 正在服务  0:  暂停服务
    /**
     * 0:  暂停服务
     */
    public static final int SERVICE_STAT_EXP = 0;

    /**
     * 服务时间可预约
     */
    public static final int SERVING_STATU_ENABLED = 1;
    /**
     * 该车型可服务
     */
    public static final int CAR_MODEL_SERVICE = 1;
    /**
     * 该车型不提供服务
     */
    public static final int CAR_MODEL_NOT_SERVICE = 0;
    /**
     * 服务时间中的每一个item的高度比例
     */
    public static final float SERVING_TIME_ITEM_HEIGHT = 0.15F;

    /**
     * 缩略图的后缀类型
     */
    public static final String THUM_IMAGE_TYPE100x100x100 = "@100w_100h_100p";
    /**
     * 缩略图的后缀类型
     */
    public static final String THUM_IMAGE_TYPE120x120x120 = "@120w_120h_120p";
    /**
     * 弹框值类型
     */
    public static final String KEY_DIALOG_VALUE = "key-dialog-value";
    public static final String KEY_DIALOG_PARAM1 = "key-dialog-param1";
    public static final String KEY_DIALOG_PARAM2 = "key-dialog-param2";
    /**
     * 支付类型弹框
     */
    public static final int DIALOG_PAY_TYPE = 1;
    /**
     * 城市列表类型弹框
     */
    public static final int DIALOG_CITIES_TYPE = 2;
    /**
     * 车辆颜色类型弹框
     */
    public static final int DIALOG_COLORS_TYPE = 3;
    /**
     * 车辆品牌类型弹框
     */
    public static final int DIALOG_BRAND_TYPE = 4;
    /**
     * 位置类型
     */
    public static final int DIALOG_LOCATION_TYPE = 5;
    /**
     * 代金券类型
     */
    public static final int DIALOG_VOUCHER_TYPE = 6;
    /**
     * 车牌省份类型
     */
    public static final int DIALOG_PROVICE_TYPE = 7;

    /**
     * 根据显示的数据条数，及数据总数计算分页多少页
     *
     * @param loadDataCount  每页显示数据的条数
     * @param totalDataCount 数据总数
     * @return 分页多少页
     */
    public static long getTotalPageCount(long loadDataCount, long totalDataCount) {
        if (loadDataCount <= Const.NONE) {
            loadDataCount = Const.ONE;
        }

        long totalPageCount = totalDataCount / loadDataCount;

        if (totalDataCount % loadDataCount != Const.NONE) {
            totalPageCount += Const.ONE;
        }

        if (totalPageCount <= Const.NONE) {
            totalPageCount = Const.ONE;
        }
        return totalPageCount;
    }

    /**
     * 添加评论内容到日志列表中
     *
     * @param logs   日志
     * @param review 评价
     */
    public static ArrayList<OrderLog> addReviewContent(ArrayList<OrderLog> logs, Review review) {
        if (null != review && null != logs) {
            OrderLog log = new OrderLog();
            log.setAction(LogAction.REVIEW.name().toUpperCase());
            log.setContent("评价");
            log.setCreateTime(review.getCreateTime());
            // 评价放到第一条，是第一条显示
            logs.add(Const.NONE, log);
        }
        return logs;
    }

    /**
     * 获取分享时要整合的图片的url
     *
     * @param logs 订单日志
     * @return 四张图片url
     */
    public static ArrayList<String> getOrderLogUrls(List<OrderLog> logs) {
        if (null == logs || logs.isEmpty()) {
            return null;
        }

        ArrayList<String> mServicePicUrls = null;
        for (int i = Const.NONE; i < logs.size(); i++) {
            OrderLog log = logs.get(i);
            String action = log.getAction().toUpperCase();
            if (LogAction.START.name().toUpperCase().equals(action)
                    || LogAction.FINISH.name().toUpperCase().equals(action)) {
                if (null != log.getImages()) {
                    if (null == mServicePicUrls) {
                        mServicePicUrls = new ArrayList<>();
                    }
                    for (int k = Const.NONE; k < log.getImages().size(); k++) {
                        if (k == 2)
                            break;
                        // 前面两张图片
                        String url = log.getImages().get(k);
                        mServicePicUrls.add(url);

                        // 先把图片加载到内存或sd中
                        ImageLoader.getInstance().loadImage(url, CNImageUtils.getImageOptions(), null);
                    }
                }
            }
        }
        return mServicePicUrls;
    }

    /**
     * 当前城市是否开通服务
     *
     * @return true 已开通，false 未开通
     */
    public static boolean hasServingInCity(Context context) {
        boolean hasServing;
        CNCarWashApp mApp = CNCarWashApp.getIns();
        // 比较当前城市与推荐城市是否相同，不相同表示未开通
        if (null == mApp.mLocatedCity
                || (mApp.mRecommendCityId != Const.NONE
                && mApp.mLocatedCity.getId() != mApp.mRecommendCityId)) {
            hasServing = false;
            CNCommonManager.makeText(context, "您当前的城市暂不支持该服务，请切换城市");
        } else {
            hasServing = true;
        }
        return hasServing;
    }

    /**
     * 是否有余额
     *
     * @return true 有余额，false 没有余额
     */
    public static boolean hasBalance() {
        BigDecimal userBalance = AppBridgeUtils.getIns().getUserBalance();
        return !(null == userBalance || userBalance.compareTo(new BigDecimal(Const.NONE)) <= Const.NONE);
    }

    /**
     * 余额是否足够
     *
     * @return true 够，false 不够
     */
    public static boolean isBalanceEnough(BigDecimal price) {
        // 有余额时，则判断是否足够
        if (hasBalance()) {
            BigDecimal userBalance = AppBridgeUtils.getIns().getUserBalance();
            if (null == userBalance || userBalance.compareTo(price) < Const.NONE) {
                // 没有足够的钱
                return false;
            }
            // 有余额，且足够
            return true;
        }
        // 没有余额
        return false;
    }

    public static void startCarWashingActivity(Context context, boolean isFinish) {
        CNViewManager.getIns().showActivity(context, CNServicesDisplayActivity.class, isFinish,
                R.anim.cn_slide_in_from_right, R.anim.cn_slide_out_to_left);
    }

    public static void startOrderDetailsActivity(Context context, long orderId, boolean isFinish) {
        if (null == context)
            return;

        Intent data = new Intent(context, CNOrderDetailsActivity.class);
        // 把订单id传过去
        data.putExtra(CNCarWashingLogic.KEY_ORDER_DETAILS_ORDER_ID, orderId);
        CNViewManager.getIns().showActivity(context, data, isFinish);
    }

    /**
     * 封装打开Dialog的intent
     *
     * @param context    上下文context
     * @param dialogType Dialog类型
     * @return Dialog的intent
     */
    private static Intent getDialogInteng(final Context context, final int dialogType) {
        Intent intent = new Intent(context, CNDialogActivity.class);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_VALUE, dialogType);
        return intent;
    }

    /**
     * 支付
     *
     * @param context     上下文context
     * @param fromOrderId 订单Id
     * @param price       支付价格
     */
    public static void startPayDialog(final Context context, long fromOrderId, BigDecimal price) {
        Intent intent = getDialogInteng(context, CNCarWashingLogic.DIALOG_PAY_TYPE);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_PARAM1, fromOrderId);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_PARAM2, price);
        context.startActivity(intent);
        CNDialogActivity.setShowProgressCallback(new CNDialogActivity.IShowProgressCallback() {
            @Override
            public void showProgress() {
                CNProgressDialogUtil.showProgress(context, true, com.kplus.car.carwash.R.string.cn_paying);
            }
        });
    }

    /**
     * 城市列表
     *
     * @param context                上下文context
     * @param popupItemClickListener 点击列表的回调
     */
    public static void startCitiesDialog(final Context context, IPopupItemClickListener popupItemClickListener) {
        Intent intent = getDialogInteng(context, CNCarWashingLogic.DIALOG_CITIES_TYPE);
        context.startActivity(intent);
        CNDialogActivity.setPopupItemClickListener(popupItemClickListener);
    }

    /**
     * 颜色弹框
     *
     * @param context                上下文context
     * @param colors                 颜色
     * @param popupItemClickListener 点击列表的回调
     */
    public static void startColorsDialog(final Context context, List<CarColor> colors, IPopupItemClickListener popupItemClickListener) {
        ArrayList<CarColor> tempColors = new ArrayList<>();
        if (null != colors) {
            tempColors.addAll(colors);
        }
        Intent intent = getDialogInteng(context, CNCarWashingLogic.DIALOG_COLORS_TYPE);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_PARAM1, tempColors);
        context.startActivity(intent);
        CNDialogActivity.setPopupItemClickListener(popupItemClickListener);
    }

    /**
     * 品牌弹框
     *
     * @param context          上下文context
     * @param brands           车辆品牌
     * @param carBrandCallback 点击车辆车型的回调
     */
    public static void startBrandsDialog(final Context context, List<CarBrand> brands, ArrayList<OnSiteService> selectedServices, CNDialogActivity.ICarBrandCallback carBrandCallback) {
        ArrayList<CarBrand> tempBrands = new ArrayList<>();
        if (null != brands) {
            tempBrands.addAll(brands);
        }
        Intent intent = getDialogInteng(context, CNCarWashingLogic.DIALOG_BRAND_TYPE);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_PARAM1, tempBrands);
        intent.putExtra(CNCarWashingLogic.KEY_SERVICE_SELECTED, selectedServices);
        context.startActivity(intent);
        CNDialogActivity.setCarBrandCallback(carBrandCallback);
    }

    /**
     * 地址弹框
     *
     * @param context                上下文context
     * @param poiLocationRoads       poi地址
     * @param popupItemClickListener 点击列表的回调
     */
    public static void startLocationDialog(final Context context, List<BaseInfo> poiLocationRoads,
                                           IPopupItemClickListener popupItemClickListener) {
        ArrayList<BaseInfo> tempPoi = new ArrayList<>();
        if (null != poiLocationRoads) {
            tempPoi.addAll(poiLocationRoads);
        }
        Intent intent = getDialogInteng(context, CNCarWashingLogic.DIALOG_LOCATION_TYPE);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_PARAM1, tempPoi);
        context.startActivity(intent);
        CNDialogActivity.setPopupItemClickListener(popupItemClickListener);
    }

    /**
     * 代金券类型
     *
     * @param context                上下文context
     * @param coupons                代金券
     * @param popupItemClickListener 点击列表的回调
     */
    public static void startVoucherDialog(final Context context, List<Coupon> coupons, IPopupItemClickListener popupItemClickListener) {
        ArrayList<Coupon> tempCoupons = new ArrayList<>();
        if (null != coupons) {
            tempCoupons.addAll(coupons);
        }
        Intent intent = getDialogInteng(context, CNCarWashingLogic.DIALOG_VOUCHER_TYPE);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_PARAM1, tempCoupons);
        context.startActivity(intent);
        CNDialogActivity.setPopupItemClickListener(popupItemClickListener);
    }

    /**
     * 车牌省份类型
     *
     * @param context             上下文context
     * @param strLicence          已选择的车牌的省份
     * @param proviceItemCallback 点击省份的回调
     */
    public static void startProviceDialog(final Context context, String strLicence, CNDialogActivity.IProviceItemCallback proviceItemCallback) {
        Intent intent = getDialogInteng(context, CNCarWashingLogic.DIALOG_PROVICE_TYPE);
        intent.putExtra(CNCarWashingLogic.KEY_DIALOG_PARAM1, strLicence);
        context.startActivity(intent);
        CNDialogActivity.setProviceItemCallback(proviceItemCallback);
    }

    /**
     * 获取支付方式
     */
    public static List<OrderPayment> getPayMent() {
        // 获取支付类型数据源
        boolean hasBalance = CNCarWashingLogic.hasBalance();

        List<OrderPayment> mOrderpayments = new ArrayList<>();

        // Notice 如果添加或修改支付类型，这里是第二步

        // 账户余额
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setPayType(Const.BALANCE_PAY);
        orderPayment.setPayName(CNResourcesUtil.getStringResources(R.string.cn_balance));
        // 有余额是选中的
        orderPayment.setIsCheck(false);
        orderPayment.setPayIcon(R.drawable.cn_icon_balance);
        orderPayment.setIsEnabled(hasBalance);
        mOrderpayments.add(orderPayment);

        // 支付宝客户端支付
        orderPayment = new OrderPayment();
//        orderPayment.setPayType(Const.ALI_PAY);
        orderPayment.setPayType(Const.OPENTRADE_PAY);
        orderPayment.setPayName(CNResourcesUtil.getStringResources(R.string.cn_alipay_type));
        orderPayment.setPayDesc(CNResourcesUtil.getStringResources(R.string.cn_alipay_desc));
        orderPayment.setPayIcon(R.drawable.cn_icon_alipay);
        orderPayment.setIsCheck(true);
        orderPayment.setIsEnabled(true);
        mOrderpayments.add(orderPayment);

        // Notice 银行卡支付去掉
//            // 银行卡支付
//            orderPayment = new OrderPayment();
//            orderPayment.setPayType(Const.UPOMP_PAY);
//            orderPayment.setPayName(getStringResources(R.string.cn_bank_card_type));
//            orderPayment.setPayDesc(getStringResources(R.string.cn_pay_type_desc));
//            orderPayment.setPayIcon(R.drawable.cn_icon_upomp);
//            orderPayment.setIsEnabled(isEnabledOtherPay);
//            mOrderpayments.add(orderPayment);

        // 连连支付
        orderPayment = new OrderPayment();
        orderPayment.setPayType(Const.LIANLIAN_PAY);
        orderPayment.setPayName(CNResourcesUtil.getStringResources(R.string.cn_lianlian_type));
        orderPayment.setPayDesc(CNResourcesUtil.getStringResources(R.string.cn_pay_type_desc));
//            orderPayment.setPayDesc("");
        orderPayment.setPayIcon(R.drawable.cn_icon_upomp);
        orderPayment.setIsCheck(false);
        orderPayment.setIsEnabled(true);
        mOrderpayments.add(orderPayment);

        // 微信客户端支付
        orderPayment = new OrderPayment();
        orderPayment.setPayType(Const.WECHAT_PAY);
        orderPayment.setPayName(CNResourcesUtil.getStringResources(R.string.cn_wechat_type));
        orderPayment.setPayDesc(CNResourcesUtil.getStringResources(R.string.cn_wechat_desc));
        orderPayment.setPayIcon(R.drawable.cn_icon_wechatpay);
        orderPayment.setIsCheck(false);
        orderPayment.setIsEnabled(true);
        mOrderpayments.add(orderPayment);

        return mOrderpayments;
    }

    /**
     * 处理银联支付结果
     *
     * @param context    上下文
     * @param pay_result 支付结果
     */
    public static void makeYinLianPayResult(Context context, String pay_result) {
        String msg = "";
        String result = "";
        if ("success".equalsIgnoreCase(pay_result)) {
            msg = "支付成功！";
            result = "ok";
        } else if ("fail".equalsIgnoreCase(pay_result)) {
            msg = "支付失败！";
            result = "error";
        } else if ("cancel".equalsIgnoreCase(pay_result)) {
            msg = "用户取消了支付";
            result = "cancel";
        }

        Log.trace(TAG, "makeYinLianPayResult-->银联支付回来--->" + msg);
        CNCommonManager.makeText(context, msg);

        Intent intent = new Intent();
        intent.setAction(CustomBroadcast.ON_PAY_RESULT_ACTION);
        intent.putExtra("result", result);
        context.sendBroadcast(intent);
    }

    /**
     * 处理支付结果
     *
     * @param result 支付结果
     * @return true 支付成功，false支付失败
     */
    public static boolean makePayResult(String result) {
        boolean isPaySuccess = true;
        if ("ok".equalsIgnoreCase(result)) {
            isPaySuccess = true;
            Log.trace(TAG, "支付成功");
        } else if ("error".equalsIgnoreCase(result)) {
            isPaySuccess = false;
            Log.trace(TAG, "支付失败");
        } else if ("cancel".equalsIgnoreCase(result)) {
            isPaySuccess = false;
            Log.trace(TAG, "支付取消");
        }
        return isPaySuccess;
    }

    /**
     * 用于存放点击选择时的服务
     */
    private static Map<Long, OnSiteService> mCheckService = new HashMap<>();

    public static void addOrDelForMap(long id, OnSiteService siteService) {
        if (null == getServiceForMap(id)) {
            addToMap(id, siteService);
        } else {
            delForMap(id);
        }
    }

    public static void addToMap(long id, OnSiteService siteService) {
        mCheckService.put(id, siteService);
    }

    public static void delForMap(Long id) {
        if (mCheckService.size() > Const.ONE) {
            // 要至少选择一项
            mCheckService.remove(id);
        }
    }

    public static OnSiteService getServiceForMap(long id) {
        return mCheckService.get(id);
    }

    public static Map<Long, OnSiteService> getServiceMap() {
        return mCheckService;
    }

    public static void clearForMap() {
        mCheckService.clear();
    }

    public static int getServiceSize() {
        return mCheckService.size();
    }

    /**
     * 获取选择的服务车型是否支持
     *
     * @param cityId
     * @param serviceId
     * @param brandId
     * @return
     */
    public static boolean isSupportCarModelByServiceId(long cityId, long serviceId, long brandId) {
        boolean isSupportCarModel = true;
        List<ServiceSupportCarTag> supportCarTags = CNSupportCarTagUtil.getIns().getTagId(cityId, serviceId);
        List<CarModelTag> carModelTags = CNCarModelTagUtil.getIns().getCarModelByTags(supportCarTags);
        List<CarModel> carModels = CNCarModelUtil.getIns().getCarModels(brandId, carModelTags);
        if (null == carModels || carModels.isEmpty()) {
            isSupportCarModel = false;
        }
        return isSupportCarModel;
    }
}
