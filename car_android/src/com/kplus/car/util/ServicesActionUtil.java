package com.kplus.car.util;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.login.YWLoginState;
import com.baidu.location.BDLocation;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.activity.ActivityCenterActivity;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.activity.EmergencyDetailActivity;
import com.kplus.car.activity.MainUIActivity;
import com.kplus.car.activity.MemberPrivilegeActivity;
import com.kplus.car.activity.MessageBoxActivity;
import com.kplus.car.activity.OrderListActivity;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.activity.RescueVehicleSelectActivity;
import com.kplus.car.activity.ShareInService;
import com.kplus.car.activity.VehicleServiceActivity;
import com.kplus.car.activity.WebViewActivity;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.utils.CNNumberUtils;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.GetUserInviteContentResponse;
import com.kplus.car.model.response.GetUserOpenImResponse;
import com.kplus.car.model.response.request.GetUserInviteContentRequest;
import com.kplus.car.model.response.request.GetUserOpenImRequest;
import com.umeng.fb.FeedbackAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Description：汽车服务与广告的点击操作工具类
 * <br/><br/>Created by FU ZHIXUE on 2015/8/17.
 * <br/><br/>
 */
public class ServicesActionUtil {

    // MotionType
    public static final String MOTION_TYPE_URL = "url";
    public static final String MOTION_TYPE_WEBAPP = "webapp";
    public static final String MOTION_TYPE_NATIVE = "native";
    public static final String MOTION_TYPE_TAB = "tab";
    public static final String MOTION_TYPE_SHARE = "share";
    public static final String MOTION_TYPE_CUSTOM = "custom";

    // MotionValue
    /**
     * 洗车页面
     */
    public static final String MOTION_VALUE_WASHING = "washing";
    /**
     * 行驶认证
     */
    public static final String MOTION_VALUE_AUTH = "auth";
    /**
     * 道路救援
     */
    public static final String MOTION_VALUE_RESCUE = "rescue";
    /**
     * 活动中心
     */
    public static final String MOTION_VALUE_ACTIVITY = "activity";
    /**
     * 消息盒子
     */
    public static final String MOTION_VALUE_MESSAGE = "message";
    /**
     * 在线客服
     */
    public static final String MOTION_VALUE_CUSTOMER = "customer";
    /**
     * 订单列表 我的订单
     */
    public static final String MOTION_VALUE_ORDERLIST = "orderList";

    // Tab
    /**
     * 我的车
     */
    public static final String MOTION_VALUE_TAB_VEHICLE = "vehicle";
    /**
     * 汽车服务
     */
    public static final String MOTION_VALUE_TAB_SERVICE = "service";
    /**
     * 管家
     */
    public static final String MOTION_VALUE_TAB_PROVIDER = "provider";
    /**
     * 个人中心
     */
    public static final String MOTION_VALUE_TAB_USER = "user";

    private final Context mContext;
    private final KplusApplication mApp;

    public ServicesActionUtil(Context context) {
        mContext = context;
        mApp = KplusApplication.getInstance();
    }

    public void onClickAction(String motionType, String motionValue) {
        if (MOTION_TYPE_WEBAPP.equalsIgnoreCase(motionType)) {
            try {
                JSONObject jsonObject = new JSONObject(motionValue);
                String startPage = jsonObject.optString("startPage");
                String appId = jsonObject.optString("appId");
                Intent vehicleServiceIntent = new Intent(mContext, VehicleServiceActivity.class);
                vehicleServiceIntent.putExtra("appId", appId);

                long uid = mApp.getId();
                long pid = mApp.getpId();

                if (appId.equalsIgnoreCase("10000006")) {
                    startPage += ("?uid=" + uid);
                    if (uid != 0)
                        startPage += ("&pid=" + pid);
                }
                startPage = replaceData(startPage);
                vehicleServiceIntent.putExtra("startPage", startPage);
                mContext.startActivity(vehicleServiceIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (MOTION_TYPE_URL.equalsIgnoreCase(motionType)) {
            String url = CNStringUtil.getHttpUrl(motionValue);
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("url", url);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_from_right, 0);
        } else if (MOTION_TYPE_NATIVE.equalsIgnoreCase(motionType)) {
            // 洗车
            if (MOTION_VALUE_WASHING.equalsIgnoreCase(motionValue)) {
                long uid = mApp.getId();
                if (uid == 0) {
                    mContext.startActivity(new Intent(mContext, PhoneRegistActivity.class));
                } else {
                    CNCarWashingLogic.startCarWashingActivity(mContext, false);
                }
            }
            // 我的订单
            else if (MOTION_VALUE_ORDERLIST.equalsIgnoreCase(motionValue)) {
                // 订单列表
                long uid = mApp.getId();
                if (uid == 0) {
                    mContext.startActivity(new Intent(mContext, PhoneRegistActivity.class));
                } else {
                    Intent intent = new Intent(mContext, OrderListActivity.class);
                    mContext.startActivity(intent);
                }
            } else if (MOTION_VALUE_AUTH.equalsIgnoreCase(motionValue)) {
                mContext.startActivity(new Intent(mContext, MemberPrivilegeActivity.class));
            } else if (MOTION_VALUE_ACTIVITY.equalsIgnoreCase(motionValue)) {
                mContext.startActivity(new Intent(mContext, ActivityCenterActivity.class));
            } else if (MOTION_VALUE_RESCUE.equalsIgnoreCase(motionValue)) {
                rescue();
            } else if (MOTION_VALUE_MESSAGE.equalsIgnoreCase(motionValue)) {
                Intent intent = new Intent(mContext, MessageBoxActivity.class);
                mContext.startActivity(intent);
            } else if (MOTION_VALUE_CUSTOMER.equalsIgnoreCase(motionValue)) {
                customer();
            }
        } else if (MOTION_TYPE_TAB.equalsIgnoreCase(motionType)) {
            Intent intent = new Intent(BroadcastReceiverUtil.ACTION_CHANGE_TAB);
            int currentTab = 0;
            if (MOTION_VALUE_TAB_VEHICLE.equalsIgnoreCase(motionValue)) {
                currentTab = 1;
            } else if (MOTION_VALUE_TAB_SERVICE.equalsIgnoreCase(motionValue)) {
                currentTab = 0;
            } else if (MOTION_VALUE_TAB_PROVIDER.equalsIgnoreCase(motionValue)) {
                currentTab = 2;
            } else if (MOTION_VALUE_TAB_USER.equalsIgnoreCase(motionValue)) {
                currentTab = 3;
            }
            intent.putExtra("currentTab", currentTab);
            LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);
            if (!(((Activity) mContext).getParent() != null && ((Activity) mContext).getParent() instanceof TabActivity)) {
                if (!(mContext instanceof MainUIActivity))
                    ((Activity) mContext).finish();
            }
        } else if (MOTION_TYPE_SHARE.equalsIgnoreCase(motionType)) {
            doShare(motionValue);
        } else if (MOTION_TYPE_CUSTOM.equalsIgnoreCase(motionType)) {
        }
    }

    private void rescue() {
        ArrayList<VehicleAuth> vehicleAuths = new ArrayList<VehicleAuth>();
        ArrayList<VehicleAuth> vehicleAuthsTemp = (ArrayList<VehicleAuth>) mApp.dbCache.getVehicleAuths();
        if (vehicleAuthsTemp != null && !vehicleAuthsTemp.isEmpty()) {
            for (VehicleAuth va : vehicleAuthsTemp) {
                if (va.getBelong() != null && va.getBelong()) {
                    if (va.getStatus() != null && va.getStatus() == 2) {
                        vehicleAuths.add(va);
                    }
                }
            }
        }
        if (vehicleAuths.isEmpty()) {
            Intent intent = new Intent(mContext, MemberPrivilegeActivity.class);
            intent.putExtra("title", "认证送免费道路救援");
            mContext.startActivity(intent);
        } else {
            if (vehicleAuths.size() == 1) {
                if (vehicleAuths.get(0).getResidueDegree() <= 0) {
                    String vehicleNumber = vehicleAuths.get(0).getVehicleNum();
                    Intent intent = new Intent(mContext, AlertDialogActivity.class);
                    intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                    intent.putExtra("subAlertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_WHERT_TO_RESCUE);
                    intent.putExtra("message", "您本辆车的免费救援次数已经用完，使用该功能将会产生服务费用，是否继续？");
                    intent.putExtra("vehicleNumber", vehicleNumber);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, EmergencyDetailActivity.class);
                    intent.putExtra("vehicleNumber", vehicleAuths.get(0).getVehicleNum());
                    mContext.startActivity(intent);
                }
            } else {
                mContext.startActivity(new Intent(mContext, RescueVehicleSelectActivity.class));
            }
        }
    }

    private void customer() {
        if (mApp.isUseWKF()) {
            if (mApp.getId() == 0) {
                mContext.startActivity(new Intent(mContext, PhoneRegistActivity.class));
            } else if (StringUtils.isEmpty(mApp.getOpenImUserId())) {
                getUserOpenIm();
            } else {
                mApp.initTaobao();
                if (mApp.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success) {
                    Intent intent = mApp.mYWIMKIT.getChattingActivityIntent(new EServiceContact("橙牛汽车管家", 156887186));
                    mContext.startActivity(intent);
                } else if (mApp.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.logining) {
                    ToastUtil.TextToast(mContext, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
                } else {
                    ToastUtil.TextToast(mContext, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
                    mApp.loginTaobao(mApp.getOpenImUserId(), mApp.getOpenImPassWord());
                }
            }
        } else {
            FeedbackAgent agent = new FeedbackAgent(mContext);
            com.umeng.fb.model.UserInfo ui = agent.getUserInfo();
            if (ui == null)
                ui = new com.umeng.fb.model.UserInfo();
            Map<String, String> contact = ui.getContact();
            if (contact == null)
                contact = new HashMap<>();
            contact.put("uid", "" + mApp.getUserId());
            ui.setContact(contact);
            agent.setUserInfo(ui);
            agent.startFeedbackActivity();
        }
    }

    private void getUserOpenIm() {
        new AsyncTask<Void, Void, GetUserOpenImResponse>() {
            @Override
            protected GetUserOpenImResponse doInBackground(Void... params) {
                GetUserOpenImRequest request = new GetUserOpenImRequest();
                request.setParams(mApp.getId());
                try {
                    return mApp.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(GetUserOpenImResponse result) {
                if (result != null && result.getCode() != null && result.getCode() == 0) {
                    UserOpenIm data = result.getData();
                    if (data != null && !StringUtils.isEmpty(data.getOpenUserid()) && !StringUtils.isEmpty(data.getOpenPassword())) {
                        mApp.initTaobao();
                        mApp.loginTaobao(data.getOpenUserid(), data.getOpenPassword());
                        mApp.setOpenImUserId(data.getOpenUserid());
                        mApp.setOpenImPassWord(data.getOpenPassword());
                    } else {
                        ToastUtil.TextToast(mContext, !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } else {
                    ToastUtil.TextToast(mContext, result != null && !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            }
        }.execute();
    }

    private void doShare(String motionValue) {
        new GetInviteContentTask().execute(motionValue);
    }

    private class GetInviteContentTask extends AsyncTask<String, Void, GetUserInviteContentResponse> {
        private String errorText = "网络中断，请稍后重试";
        private int id = 0;

        @Override
        protected GetUserInviteContentResponse doInBackground(String... params) {
            try {
                GetUserInviteContentRequest request = new GetUserInviteContentRequest();
                id = Integer.parseInt(params[0]);
                request.setParams(mApp.getId(), id);
                return mApp.client.execute(request);
            } catch (Exception e) {
                e.printStackTrace();
                errorText = e.toString();
                return null;
            }
        }

        @Override
        protected void onPostExecute(GetUserInviteContentResponse result) {
            try {
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0) {
                        Intent intent = new Intent(mContext, ShareInService.class);
                        intent.putExtra("shareType", "23");
                        String title = result.getData().getTitle();
                        String summary = result.getData().getSummary();
                        String content = result.getData().getContent();
                        String inviteUrl = result.getData().getInviteUrl();
                        String imgUrl = result.getData().getImgUrl();
                        intent.putExtra("title", title);
                        intent.putExtra("summary", summary);
                        intent.putExtra("content", content);
                        intent.putExtra("inviteUrl", inviteUrl);
                        intent.putExtra("imgUrl", imgUrl);
                        intent.putExtra("contentType", id);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "获取分享内容失败", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
    private final String REPLACE_UID = "{{uId}}";
    private final String REPLACE_PID = "{{pId}}";
    private final String REPLACE_USER_ID = "{{userId}}";
    private final String REPLACE_CITY_ID = "{{cityId}}";
    private final String REPLACE_CITY_NAME = "{{cityName}}";
    private final String REPLACE_LAT = "{{lat}}";
    private final String REPLACE_LNG = "{{lng}}";
    private final String REPLACE_PROVINCE = "{{province}}";

    private Map<String, String> mReplaceMap;

    private String replaceData(String url) {
        initReplaceData();

        for (Map.Entry<String, String> entry : mReplaceMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null)
                url = url.replace(key, value);
        }
        return url;
    }

    private void initReplaceData() {
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
    }
}
