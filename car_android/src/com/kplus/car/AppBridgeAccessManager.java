package com.kplus.car;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.opentrade.OpenTradeService;
import com.alibaba.sdk.android.opentrade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.opentrade.callback.TradeResult;
import com.alibaba.sdk.android.session.model.Session;
import com.alipay.sdk.app.PayTask;
import com.baidu.location.BDLocation;
import com.kplus.car.asynctask.WeatherTask;
import com.kplus.car.carwash.bean.CNWeather;
import com.kplus.car.carwash.callback.IAppBridgeListener;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.model.Account;
import com.kplus.car.model.UserInfo;
import com.kplus.car.model.Weather;
import com.kplus.car.model.response.GetStringValueResponse;
import com.kplus.car.model.response.GetUserInfoResponse;
import com.kplus.car.model.response.WeatherResponse;
import com.kplus.car.model.response.request.GetUserInfoRequest;
import com.kplus.car.model.response.request.OrderPayRequest;
import com.kplus.car.payment.lianlian.BaseHelper;
import com.kplus.car.payment.lianlian.Constants;
import com.kplus.car.payment.lianlian.MobileSecurePayer;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.util.WeatherUtil;
import com.kplus.car.wxapi.WXPayListener;
import com.kplus.car.wxapi.WXShareListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Fu on 2015/5/20.
 */
public class AppBridgeAccessManager implements IAppBridgeListener, WXPayListener, WXShareListener {
    private static final String TAG = "AppBridgeAccessManager";

    private static final String TENCENT_PACKAGE = "com.tencent.mm";

    private static final int SHARE_WE_CHAT_FRIEND = 0;
    private static final int SHARE_WE_CHAR_COMENT = 1;

    private Context mContext = null;
    private IWXAPI iwxapi;

    public AppBridgeAccessManager() {
    }

    @Override
    public boolean isLog() {
        return KplusConstants.IS_LOG;
    }

    @Override
    public String getCarWashingApiUrl() {
        return KplusConstants.CAR_WASHING_API_URL;
    }

    @Override
    public void agentOnStart(Activity act) {
        EventAnalysisUtil.onStart(act);
    }

    @Override
    public void agentOnResume(Activity act) {
        EventAnalysisUtil.onResume(act);
    }

    @Override
    public void agentOnPause(Activity act) {
        EventAnalysisUtil.onPause(act);
    }

    @Override
    public void agentOnStop(Activity act) {
        EventAnalysisUtil.onStop(act);
    }

    @Override
    public void onEvent(Context context, String eventId) {
        EventAnalysisUtil.onEvent(context, eventId, null, null);
    }

    @Override
    public void onEvent(Context context, String eventId, String label) {
        EventAnalysisUtil.onEvent(context, eventId, label, null);
    }

    @Override
    public void onEvent(Context context, String eventId, String label, Map<String, String> map) {
        EventAnalysisUtil.onEvent(context, eventId, label, map);
    }

    @Override
    public String getClientAppKey() {
        if (TextUtils.isEmpty(KplusConstants.CLIENT_APP_KEY)) {
            Context context = KplusApplication.getInstance().getApplicationContext();
            KplusConstants.initData(context);
        }
        return KplusConstants.CLIENT_APP_KEY;
    }

    @Override
    public String getClientAppSecret() {
        if (TextUtils.isEmpty(KplusConstants.CLIENT_APP_SECRET)) {
            Context context = KplusApplication.getInstance().getApplicationContext();
            KplusConstants.initData(context);
        }
        return KplusConstants.CLIENT_APP_SECRET;
    }

    @Override
    public long getPid() {
        return KplusApplication.getInstance().getpId();
    }

    @Override
    public long getUid() {
        return KplusApplication.getInstance().getId();
    }

    @Override
    public long getUserId() {
        return KplusApplication.getInstance().getUserId();
    }

    @Override
    public double getLongitude() {
        BDLocation location = KplusApplication.getInstance().getLocation();
        if (null == location) {
            return -1;
        }
        return location.getLongitude();
    }

    @Override
    public double getLatitude() {
        BDLocation location = KplusApplication.getInstance().getLocation();
        if (null == location) {
            return -1;
        }
        return location.getLatitude();
    }

    @Override
    public String getCity() {
        BDLocation location = KplusApplication.getInstance().getLocation();
        if (null == location || TextUtils.isEmpty(location.getCity())) {
            return "";
        }
        return location.getCity().replace("市", "");
    }

    @Override
    public String getSelectedCity() {
        return KplusApplication.getInstance().getCityName();
    }

    @Override
    public long getSelectedCityId() {
        String cityId = KplusApplication.getInstance().getCityId();
        if (!TextUtils.isEmpty(cityId)) {
            try {
                return Long.valueOf(cityId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public List<CNWeather> getWeatherForLocal(long cityId) {
        long selectedCityId = getSelectedCityId();
        if (cityId == selectedCityId) {
            // 是相同的城市，从数据库中获取
            List<Weather> weathers = KplusApplication.getInstance().dbCache.getWeathers();
            WeatherUtil.sortWeatherForDay(weathers);
            if (null != weathers) {
                List<CNWeather> weatherList = new ArrayList<>();

                for (Weather weather : weathers) {
                    CNWeather cnWeather = new CNWeather();
                    cnWeather.mDay = weather.getDateStr();
                    cnWeather.mPhenomenon = weather.getPhenomenon();
                    cnWeather.mType = weather.getType();
                    cnWeather.mWash = weather.getWash();
                    weatherList.add(cnWeather);
                }
                return weatherList;
            }
        }
        return null;
    }

    @Override
    public void getWeatherForServiceAsy(Context context, final long cityId) {
        mContext = context;
        new WeatherTask(KplusApplication.getInstance()) {
            @Override
            protected void onPostExecute(WeatherResponse response) {
                ArrayList<CNWeather> weatherList = new ArrayList<>();

                if (response != null && response.getCode() != null && response.getCode() == 0) {
                    List<Weather> weathers = response.getData().getList();
                    WeatherUtil.sortWeatherForDay(weathers);
                    if (null != weathers) {
                        for (Weather weather : weathers) {
                            CNWeather cnWeather = new CNWeather();
                            cnWeather.mDay = weather.getDateStr();
                            cnWeather.mPhenomenon = weather.getPhenomenon();
                            cnWeather.mType = weather.getType();
                            cnWeather.mWash = weather.getWash();
                            weatherList.add(cnWeather);
                        }
                    }
                }

                Intent intent = new Intent();
                intent.setAction(CustomBroadcast.ON_WEATHER_RESULT_ACTION);
                intent.putExtra("weathers", weatherList);
                intent.putExtra("cityId", cityId);
                mContext.sendBroadcast(intent);
            }
        }.execute(cityId);
    }

    @Override
    public boolean isFromInApp() {
        return KplusApplication.getInstance().isFromInApp;
    }

    @Override
    public boolean doShareWeChatMoment(Context context, int shareContentType, String webPageUrl, String content, String imagePath, String thumImagePath) {
        mContext = context;
        if (null == iwxapi) {
            iwxapi = WXAPIFactory.createWXAPI(mContext, KplusConstants.WECHAT_APPID, true);
            iwxapi.registerApp(KplusConstants.WECHAT_APPID);
        }
        if (isAppInstalled(TENCENT_PACKAGE)) {
            doShareWeChat(SHARE_WE_CHAR_COMENT, shareContentType, webPageUrl, content, imagePath, thumImagePath);
            return true;
        } else {
            ToastUtil.TextToast(context, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
        }
        return false;
    }

    @Override
    public boolean doShareWeCharFriend(Context context, int shareContentType, String webPageUrl, String content, String imagePath, String thumImagePath) {
        mContext = context;
        if (null == iwxapi) {
            iwxapi = WXAPIFactory.createWXAPI(mContext, KplusConstants.WECHAT_APPID, true);
            iwxapi.registerApp(KplusConstants.WECHAT_APPID);
        }
        if (isAppInstalled(TENCENT_PACKAGE)) {
            doShareWeChat(SHARE_WE_CHAT_FRIEND, shareContentType, webPageUrl, content, imagePath, thumImagePath);
            return true;
        } else {
            ToastUtil.TextToast(context, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
        }
        return false;
    }

    private void doShareWeChat(final int type, final int shareContentType, final String webPageUrl, final String content, final String imagePath, final String thumImagePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KplusApplication.getInstance().setWxShareListener(AppBridgeAccessManager.this);
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = webPageUrl;
                WXMediaMessage msg = new WXMediaMessage(webpage);

                msg.title = content;
                msg.messageAction = content;
                msg.messageExt = content;
                String desc = mContext.getResources().getString(R.string.daze_app_name);
//                desc += "上门洗车";
                msg.description = desc;

                switch (shareContentType) {
                    case CNCarWashingLogic.SHARE_IMAGE_CONTENT_TYPE:
                        // 如果是订单日志中分享的纯图片，如果没有找到图片时使用文本
                        if (!TextUtils.isEmpty(imagePath)) {
                            if (type == SHARE_WE_CHAR_COMENT) {
                                WXImageObject imageObject = new WXImageObject();
                                imageObject.imageData = CNImageUtils.readData(imagePath);
                                msg.mediaObject = imageObject;
                            }
                        }
//                        else {
//                            WXTextObject textObject = new WXTextObject();
//                            textObject.text = content;
//                            msg.mediaObject = textObject;
//                        }
                        break;
                    case CNCarWashingLogic.SHARE_TEXT_AND_IMAGE_CONTENT_TYPE:
                        Log.trace(TAG, "分享有图有文字");
                        break;
                }

                Bitmap thumImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.daze_icon);
                if (!TextUtils.isEmpty(thumImagePath)) {
                    // 大小不能超过32K 如果超过，会调不起来
                    Bitmap tempThum = CNImageUtils.antiOMDecodeFile(thumImagePath, 2);
                    if (null != tempThum) {
                        thumImage = tempThum;
                    }
                }
                msg.setThumbImage(thumImage);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;

                if (type == SHARE_WE_CHAR_COMENT) {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                } else if (type == SHARE_WE_CHAT_FRIEND) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                }
                iwxapi.sendReq(req);
            }
        }).start();
    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = KplusApplication.getInstance().getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
            e.printStackTrace();
        }
        return installed;
    }


    private void sendShareResult(int resultCode) {
        Intent intent = new Intent();
        intent.setAction(CustomBroadcast.ON_SHARE_RESULT_ACTION);
        intent.putExtra(CNCarWashingLogic.KEY_SHARE_RESULT_CODE, resultCode);
        mContext.sendBroadcast(intent);
    }

    @Override
    public void onShareSuccess(BaseResp response) {
        // 成功
        sendShareResult(CNCarWashingLogic.SHARE_RESULT_SUCCESS);
        ToastUtil.TextToast(mContext, "分享成功", Toast.LENGTH_SHORT, Gravity.CENTER);
        KplusApplication.getInstance().setWxShareListener(null);
    }

    @Override
    public void onShareCancel(BaseResp response) {
        sendShareResult(CNCarWashingLogic.SHARE_RESULT_CANCEL);
        ToastUtil.TextToast(mContext, "已取消分享", Toast.LENGTH_SHORT, Gravity.CENTER);
        KplusApplication.getInstance().setWxShareListener(null);
    }

    @Override
    public void onShareFail(BaseResp response) {
        sendShareResult(CNCarWashingLogic.SHARE_RESULT_FAILED);
        ToastUtil.TextToast(mContext, "分享失败", Toast.LENGTH_SHORT, Gravity.CENTER);
        KplusApplication.getInstance().setWxShareListener(null);
    }

    @Override
    public void getUserInfo(Context context) {
        this.mContext = context;
        new AsyncTask<Void, Void, GetUserInfoResponse>() {

            @Override
            protected GetUserInfoResponse doInBackground(Void... params) {
                try {
                    GetUserInfoRequest request = new GetUserInfoRequest();
                    request.setParams(KplusApplication.getInstance().getId());
                    return KplusApplication.getInstance().client.execute(request);
                } catch (Exception e) {
                    Log.trace(TAG, "获取用户资料出现异常！" + e);
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(GetUserInfoResponse result) {
                try {
                    if (result != null && result.getCode() != null && result.getCode() == 0) {

                        UserInfo userInfo = result.getData().getUserInfo();
                        Float cashbalance = userInfo.getCashBalance();
                        float fCashbalance = (cashbalance == null ? 0.00f : cashbalance);

                        KplusApplication app = KplusApplication.getInstance();
                        app.dbCache.saveUserInfo(userInfo);
                        String mobile = "";
                        List<Account> accounts = userInfo.getAccounts();
                        if (accounts != null && !accounts.isEmpty()) {
                            app.dbCache.saveAccounts(accounts);
                            for (Account account : accounts) {
                                // 手机号
                                if (account.getType().equals(0)) {
                                    mobile = account.getUserName();
                                }
                            }
                        }

                        // 设置用户余额，传到洗车那边
                        app.setCarWashingUserBalance(fCashbalance);
                        app.setCarWashingMobile(mobile);

                        // 发广播通知已经获取到用户资料了
                        Intent intent = new Intent();
                        intent.setAction(CustomBroadcast.ON_GET_USER_INFO_ACTION);
                        mContext.sendBroadcast(intent);

                        Log.trace(TAG, "获取到的用户资料：uid-->" + userInfo.getUid()
                                + " pid-->" + app.getpId() + " userId-->" + app.getUserId());
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(CustomBroadcast.ON_GET_USER_INFO_ACTION);
                        mContext.sendBroadcast(intent);
                        Log.trace(TAG, "用户信息获取失败");
                    }
                } catch (Exception e) {
                    Log.trace(TAG, "解析获取用户资料结果出现异常！" + e);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public void pay(final Context context, final long orderId, int payment) {
        mContext = context;
        KplusApplication mApplication = (KplusApplication) ((Activity) mContext).getApplication();
        if (payment == KplusConstants.OPENTRADE_PAY) {
            if (!mApplication.isUseOpentrade()) {
                payment = KplusConstants.ALI_PAY;
            }
        } else if (payment == KplusConstants.BALANCE_OPENTRADE_PAY) {
            if (!mApplication.isUseOpentrade()) {
                payment = KplusConstants.BALANCE_ALI_PAY;
            }
        }
        final int type = payment;
        new AsyncTask<Object, Object, GetStringValueResponse>() {
            @Override
            protected GetStringValueResponse doInBackground(Object... params) {
                OrderPayRequest request = new OrderPayRequest();
                request.setParams(orderId, type);
                try {
                    return KplusApplication.getInstance().client.execute(request);
                } catch (Exception e) {
                    Log.trace(TAG, "获取支付订单失败！" + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(GetStringValueResponse result) {
                if (result != null) {
                    if (result.getCode() == 0) {
                        final String orderInfo = result.getData().getValue();

                        Log.trace(TAG, "支付的订单信息-->orderInfo--->" + orderInfo);

                        // Notice 如果添加或修改支付类型，这里是第三步

                        if (type == KplusConstants.ALI_PAY
                                || type == KplusConstants.BALANCE_ALI_PAY) {
                            aliPay(orderInfo);
                        } else if (type == KplusConstants.OPENTRADE_PAY
                                || type == KplusConstants.BALANCE_OPENTRADE_PAY) {
                            alibabaSDKPay(orderInfo);
                        } else if (type == KplusConstants.UPOMP_PAY
                                || type == KplusConstants.BALANCE_UPOMP_PAY) {
                            yinlianPay(result.getData().getValue());
                        } else if (type == KplusConstants.LIANLIAN_PAY
                                || type == KplusConstants.LIANLIAN_WEBPAY
                                || type == KplusConstants.BALANCE_LIANLIAN_PAY
                                || type == KplusConstants.BALANCE_LIANLIAN_WEBPAY) {
                            lianlianPay(result.getData().getValue());
                        } else if (type == KplusConstants.WECHAT_PAY
                                || type == KplusConstants.BALANCE_WECHAT_PAY) {
                            iwxapi = WXAPIFactory.createWXAPI(mContext, KplusConstants.WECHAT_APPID, true);
                            iwxapi.registerApp(KplusConstants.WECHAT_APPID);
                            KplusApplication.getInstance().setWxPayListener(AppBridgeAccessManager.this);
                            wechatPay(orderInfo);
                        } else {
                            ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                            sendResult("ok");
                        }
                    } else {
                        Log.trace(TAG, "支付异常：" + result.getBody());
                        sendResult("error");
                        Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sendResult("error");
                    Toast.makeText(context, "支付失败，请稍候重试", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onPaySuccess(BaseResp response) {
        KplusApplication.getInstance().setWxPayListener(null);
        ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
        sendResult("ok");
    }

    @Override
    public void onPayCancel(BaseResp response) {
        KplusApplication.getInstance().setWxPayListener(null);
        ToastUtil.TextToast(mContext, "支付取消", Toast.LENGTH_SHORT, Gravity.CENTER);
        sendResult("cancel");
    }

    @Override
    public void onPayFail(BaseResp response) {
        KplusApplication.getInstance().setWxPayListener(null);
        String msg = StringUtils.isEmpty(response.errStr) ? "支付失败" : response.errStr;
        ToastUtil.TextToast(mContext, msg, Toast.LENGTH_SHORT, Gravity.CENTER);
        sendResult("error");
    }

    private void sendResult(String result) {
        Intent intent = new Intent();
        intent.setAction(CustomBroadcast.ON_PAY_RESULT_ACTION);
        intent.putExtra("result", result);
        mContext.sendBroadcast(intent);
    }

    private static final int ALI_PAY = 0x01;
    private static final int RQF_PAY = 0x02;

    private void aliPay(final String orderInfo) {
        new Thread() {
            public void run() {
                PayTask payTask = new PayTask((Activity) mContext);
                String result = payTask.pay(orderInfo);
                Log.trace(TAG, "aliPay-->result is-->" + result);
                Message msg = new Message();
                msg.what = ALI_PAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void alibabaSDKPay(final String orderInfo) {
        AlibabaSDK.asyncInit(mContext.getApplicationContext(), new InitResultCallback() {
            @Override
            public void onSuccess() {
                Log.trace(TAG, "AlibabaSDK初始化成功");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openTradePay(orderInfo);
                    }
                }, 1000);
            }

            @Override
            public void onFailure(int i, String s) {
                sendResult("error");
                ToastUtil.TextToast(mContext, StringUtils.isEmpty(s) ? "初始化异常" : s, Toast.LENGTH_SHORT, Gravity.CENTER);
            }
        });
    }

    private void openTradePay(final String orderInfo) {
        try {
            final JSONObject json = new JSONObject(orderInfo);
            OpenAccountService oas = AlibabaSDK.getService(OpenAccountService.class);
            Session session = oas.getSession();
            if (session != null && session.isLogin()) {
                showPayOrder(json);
            } else {
                oas.tokenLogin((Activity) mContext, json.optString("authToken"), new LoginCallback() {
                    @Override
                    public void onSuccess(OpenAccountSession session) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showPayOrder(json);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        sendResult("error");
                        ToastUtil.TextToast(mContext, StringUtils.isEmpty(s) ? "支付失败" : s, Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                });
            }
        } catch (Exception e) {
            sendResult("error");
            ToastUtil.TextToast(mContext, "支付失败", Toast.LENGTH_SHORT, Gravity.CENTER);
            e.printStackTrace();
        }
    }

    private void showPayOrder(final JSONObject json) {
        OpenTradeService ots = AlibabaSDK.getService(OpenTradeService.class);
        try {
            ots.showPayOrder((Activity) mContext, Long.parseLong(json.optString("orderId")), json.optString("isvOrderId"), null, json.getLong("amount"), json.optString("closeTime"), new TradeProcessCallback() {
                @Override
                public void onPaySuccess(TradeResult tradeResult) {
                    sendResult("ok");
                    ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                }

                @Override
                public void onFailure(int i, String s) {
                    if (!TextUtils.isEmpty(s) && "USER_CANCEL".equals(s)) {
                        sendResult("cancel");
                        ToastUtil.TextToast(mContext, "支付取消", Toast.LENGTH_SHORT, Gravity.CENTER);
                    } else {
                        sendResult("error");
                        ToastUtil.TextToast(mContext, StringUtils.isEmpty(s) ? "支付失败" : s, Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                }
            });
        } catch (Exception e) {
            sendResult("error");
            ToastUtil.TextToast(mContext, "支付失败", Toast.LENGTH_SHORT, Gravity.CENTER);
            e.printStackTrace();
        }
    }

    private void yinlianPay(final String orderInfo) {
        int ret = UPPayAssistEx.startPay((Activity) mContext, null, null, orderInfo, "00");
        if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
            UPPayAssistEx.installUPPayPlugin(mContext);
        }
    }

    private void lianlianPay(String orderInfo) {
        MobileSecurePayer msp = new MobileSecurePayer();
        msp.pay(orderInfo, mHandler, RQF_PAY, (Activity) mContext, false);
    }

    private void wechatPay(final String orderInfo) {
        if (!StringUtils.isEmpty(orderInfo)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject(orderInfo);
                        PayReq request = new PayReq();
                        request.appId = json.optString("appid");
                        request.partnerId = json.optString("partnerid");
                        request.prepayId = json.optString("prepayid");
                        request.packageValue = json.optString("package");
                        request.nonceStr = json.optString("noncestr");
                        request.timeStamp = json.optString("timestamp");
                        request.sign = json.optString("sign");
                        iwxapi.sendReq(request);
                    } catch (Exception e) {
                        sendResult("error");
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            sendResult("error");
        }
    }

    private Handler mHandler = new Handler(KplusApplication.getInstance().getMainLooper()) {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            switch (msg.what) {
                case ALI_PAY:
//                    Map<String, Object> item = new HashMap<String, Object>();
//                    item.put("price", totalPrice-orderSeiviceChargeDeduction - orderPriceDeduvtion);
                    try {
                        String tradeStatus = "resultStatus={";
                        int imemoStart = result.indexOf("resultStatus=");
                        imemoStart += tradeStatus.length();

                        String memo = "};memo={";
                        int imemoEnd = result.indexOf(memo);
                        tradeStatus = result.substring(imemoStart, imemoEnd);

                        if (tradeStatus.equals("9000")) {
                            sendResult("ok");
                            ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
//                            TCAgent.onEvent(mContext, "Core_Response", "payOrder_alipay_success", item);
                        } else {
                            if (tradeStatus.equals("8000")) {
                                sendResult("ok");
                                ToastUtil.TextToast(mContext, "支付结果确认中", Toast.LENGTH_SHORT, Gravity.CENTER);
                            } else {
                                int memoStart = imemoEnd + memo.length();
                                int memoEnd = result.indexOf("};result");
                                memo = result.substring(memoStart, memoEnd);
                                sendResult("error");
                                ToastUtil.TextToast(mContext, memo, Toast.LENGTH_SHORT, Gravity.CENTER);
//                                TCAgent.onEvent(mContext, "Core_Response", "payOrder_alipay_fail", item);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendResult("error");
//                        TCAgent.onEvent(mContext, "Core_Response", "payOrder_alipay_fail", item);
                    }
                    break;
                case RQF_PAY:
                    String strRet = (String) msg.obj;
                    JSONObject objContent = BaseHelper.string2JSON(strRet);
                    String retCode = objContent.optString("ret_code");
                    String retMsg = objContent.optString("ret_msg");
                    // 成功
                    if (com.kplus.car.payment.lianlian.Constants.RET_CODE_SUCCESS.equals(retCode)) {
                        sendResult("ok");
                        ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                    } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                        sendResult("ok");
                        // TODO 处理中，掉单的情形
                        String resulPay = objContent.optString("result_pay");
                        if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
                            ToastUtil.TextToast(mContext, objContent.optString("ret_msg") + "交易状态码：" + retCode, Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                    } else {
                        sendResult("error");
                        // TODO 失败
                        ToastUtil.TextToast(mContext, retMsg + "，交易状态码:" + retCode, Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
