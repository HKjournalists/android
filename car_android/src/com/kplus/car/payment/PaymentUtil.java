package com.kplus.car.payment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.container.command.DazeCommandDelegate;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.model.response.GetStringValueResponse;
import com.kplus.car.model.response.request.OrderPayRequest;
import com.kplus.car.payment.lianlian.BaseHelper;
import com.kplus.car.payment.lianlian.Constants;
import com.kplus.car.payment.lianlian.MobileSecurePayer;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXPayListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import org.json.JSONObject;

/**
 * Created by CN-11 on 2015/6/12.
 */
public class PaymentUtil {
    private static final int ALI_PAY = 0x01;
    private static final int RQF_PAY = 0x02;
    private Context mContext;
    private KplusApplication mApplication;
    private Handler mHandler;
    private long orderId;
    private View progressView;
    private DazeInvokedUrlCommand payCommand;
    private DazeCommandDelegate delegate;
    private IWXAPI iwxapi;
    private PayResultLisenter payResultLisenter;
    public PaymentUtil(Context context, long orderId, View progressView, DazeInvokedUrlCommand payCommand, DazeCommandDelegate delegate){
        mContext = context;
        mApplication = (KplusApplication)((Activity)context).getApplication();
        this.orderId = orderId;
        this.progressView = progressView;
        this.payCommand = payCommand;
        this.delegate = delegate;
        iwxapi = WXAPIFactory.createWXAPI(mContext, KplusConstants.WECHAT_APPID, true);
        iwxapi.registerApp(KplusConstants.WECHAT_APPID);
        createHandler();
    }

    private void createHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                switch (msg.what) {
                    case ALI_PAY:
                        try {
                            String tradeStatus = "resultStatus={";
                            int imemoStart = result.indexOf("resultStatus=");
                            imemoStart += tradeStatus.length();
                            String memo = "};memo={";
                            int imemoEnd = result.indexOf(memo);
                            tradeStatus = result.substring(imemoStart, imemoEnd);
                            if (tradeStatus.equals("9000")) {
                                ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                                if(payResultLisenter != null)
                                    payResultLisenter.onPaySuccess();
                                sendResult(true);
                            }
                            else{
                                if(tradeStatus.equals("8000")){
                                    ToastUtil.TextToast(mContext, "支付结果确认中", Toast.LENGTH_SHORT, Gravity.CENTER);
                                }
                                else{
                                    int memoStart = imemoEnd + memo.length();
                                    int memoEnd = result.indexOf("};result");
                                    memo = result.substring(memoStart, memoEnd);
                                    ToastUtil.TextToast(mContext, memo, Toast.LENGTH_SHORT, Gravity.CENTER);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RQF_PAY:
                        String strRet = (String) msg.obj;
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");
                        // 成功
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                            sendResult(true);
                            ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                            if(payResultLisenter != null)
                                payResultLisenter.onPaySuccess();
                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            // TODO 处理中，掉单的情形
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
                                ToastUtil.TextToast(mContext, objContent.optString("ret_msg") + "交易状态码：" + retCode, Toast.LENGTH_SHORT, Gravity.CENTER);
                            }

                        } else {
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

    public void payRequest(int payType){
        if(payType == KplusConstants.OPENTRADE_PAY){
            if(!mApplication.isUseOpentrade()){
                payType = KplusConstants.ALI_PAY;
            }
        }
        else if(payType == KplusConstants.BALANCE_OPENTRADE_PAY){
            if(!mApplication.isUseOpentrade()){
                payType = KplusConstants.BALANCE_ALI_PAY;
            }
        }
        final int type = payType;
        showProgress(true);
        new AsyncTask<Object, Object, GetStringValueResponse>() {
            @Override
            protected GetStringValueResponse doInBackground(Object... params) {
                OrderPayRequest request = new OrderPayRequest();
                request.setParams(orderId, type);
                try {
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(final GetStringValueResponse result) {
                showProgress(false);
                if (result != null) {
                    if (result.getCode() == 0) {
                        if(type == KplusConstants.ALI_PAY || type == KplusConstants.BALANCE_ALI_PAY || type == KplusConstants.ALI_WEBPAY || type == KplusConstants.BALANCE_ALI_WEB){
							aliPay(result.getData().getValue());
                        }
                        else if(type == KplusConstants.OPENTRADE_PAY || type == KplusConstants.BALANCE_OPENTRADE_PAY){
                            AlibabaSDK.asyncInit(mContext.getApplicationContext(), new InitResultCallback() {
                                @Override
                                public void onSuccess() {
                                    showProgress(true);
//                                    ToastUtil.TextToast(mContext, "初始化成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                                    Log.i("PaymentUtil", "AlibabaSDK初始化成功");
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            openTradePay(result.getData().getValue());
                                        }
                                    }, 1000);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    ToastUtil.TextToast(mContext, StringUtils.isEmpty(s) ? "初始化异常" : s, Toast.LENGTH_SHORT, Gravity.CENTER);
                                }
                            });
//                            openTradePay(result.getData().getValue());
                        }
                        else if(type == KplusConstants.UPOMP_PAY || type == KplusConstants.BALANCE_UPOMP_PAY){
                            yinlianPay(result.getData().getValue());
                        }
                        else if(type == KplusConstants.LIANLIAN_PAY || type == KplusConstants.LIANLIAN_WEBPAY || type == KplusConstants.BALANCE_LIANLIAN_PAY || type == KplusConstants.BALANCE_LIANLIAN_WEBPAY){
                            lianlianPay(result.getData().getValue());
                        }
                        else if(type == KplusConstants.WECHAT_PAY || type == KplusConstants.BALANCE_WECHAT_PAY){
                            if(mApplication.getWxPayListener() == null)
                                mApplication.setWxPayListener((WXPayListener)mContext);
                            wechatPay(result.getData().getValue());
                        }
                    } else {
                        Toast.makeText(mContext,	result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();
    }

    private void showProgress(boolean show){
        if(show){
            if(progressView != null)
                progressView.setVisibility(View.VISIBLE);
        }
        else{
            if(progressView != null)
                progressView.setVisibility(View.GONE);
        }
    }

    private void aliPay(final String orderInfo) {
        new Thread() {
            public void run() {
                PayTask payTask = new PayTask((Activity)mContext);
                String result = payTask.pay(orderInfo);
                Message msg = new Message();
                msg.what = ALI_PAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void openTradePay(final String orderInfo){
        try {
            final JSONObject json = new JSONObject(orderInfo);
            OpenAccountService oas = AlibabaSDK.getService(OpenAccountService.class);
            Session session = oas.getSession();
            if(session != null && session.isLogin()){
                showPayOrder(json);
            }
            else{
                oas.tokenLogin((Activity)mContext, json.optString("authToken"), new LoginCallback() {
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
                        ToastUtil.TextToast(mContext, StringUtils.isEmpty(s) ? "支付失败" : s, Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showProgress(false);
    }

    private void showPayOrder(final JSONObject json){
        OpenTradeService ots = AlibabaSDK.getService(OpenTradeService.class);
        try {
            ots.showPayOrder((Activity)mContext, Long.parseLong(json.optString("orderId")), json.optString("isvOrderId"), null, json.getLong("amount"), json.optString("closeTime"), new TradeProcessCallback() {
                @Override
                public void onPaySuccess(TradeResult tradeResult) {
                    sendResult(true);
                    ToastUtil.TextToast(mContext, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                    if(payResultLisenter != null)
                        payResultLisenter.onPaySuccess();
                }

                @Override
                public void onFailure(int i, String s) {
                    if(s != null && s.equals("USER_CANCEL"))
                        ToastUtil.TextToast(mContext, "取消支付", Toast.LENGTH_SHORT, Gravity.CENTER);
                    else
                        ToastUtil.TextToast(mContext, StringUtils.isEmpty(s) ? "支付失败" : s, Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            ToastUtil.TextToast(mContext, "支付失败", Toast.LENGTH_SHORT, Gravity.CENTER);
        }
    }

    private void wechatPay(final String orderInfo){
        if(!StringUtils.isEmpty(orderInfo)){
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
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void yinlianPay(final String orderInfo) {
        int ret = UPPayAssistEx.startPay((Activity)mContext, null, null, orderInfo, "00");
        if(ret == UPPayAssistEx.PLUGIN_NOT_FOUND){
            UPPayAssistEx.installUPPayPlugin(mContext);
        }
    }

    private void lianlianPay(String orderInfo){
        MobileSecurePayer msp = new MobileSecurePayer();
        msp.pay(orderInfo, mHandler, RQF_PAY, (Activity)mContext, false);
    }

    private void sendResult(boolean isSuccess){
        if(payCommand != null){
            try{
                JSONObject resultJson = new JSONObject();
                resultJson.put("isSuccess", isSuccess);
                sendResult(resultJson, payCommand, false);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendResult(JSONObject result, DazeInvokedUrlCommand command, boolean isKeepCallback){
        try{
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("responseData", result);
            responseJSON.put("responseId", command.getCallbackId());
            responseJSON.put("keepCallback", isKeepCallback);
            String response = responseJSON.toString();
            response.replaceAll("\\\\", "\\\\\\\\");
            response.replaceAll("\\\"", "\\\\\\\"");
            response.replaceAll("\\\'", "\\\\\\\'");
            response.replaceAll("\\\n", "\\\\\\\n");
            response.replaceAll("\\\r", "\\\\\\\r");
            response.replaceAll("\\\f", "\\\\\\\f");
            response.replaceAll("\\\u2028", "\\\\\\\u2028");
            response.replaceAll("\\\u2029", "\\\\\\\u2029");
            String javascriptCommand = "DazeJSBridge.__invokeJS('" + response + "')";
            if(delegate != null){
                delegate.evalJS(javascriptCommand);
            }
            else{
                Log.e("TAG", "无法找到对应的 commandDelegate 对象");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public PayResultLisenter getPayResultLisenter() {
        return payResultLisenter;
    }

    public void setPayResultLisenter(PayResultLisenter payResultLisenter) {
        this.payResultLisenter = payResultLisenter;
    }
}
