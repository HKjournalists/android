package com.kplus.car.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.Response;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.comm.DazeUserAccount;
import com.kplus.car.model.Advert;
import com.kplus.car.model.UserInfo;
import com.kplus.car.model.json.AdvertJson;
import com.kplus.car.model.json.ChangePhoneResultJson;
import com.kplus.car.model.json.ThirdpartBindResultJson;
import com.kplus.car.model.response.AddUserInfoReponse;
import com.kplus.car.model.response.BindThirdPartResponse;
import com.kplus.car.model.response.BooleanResultResponse;
import com.kplus.car.model.response.ChangePhoneResponse;
import com.kplus.car.model.response.GetAdvertDataResponse;
import com.kplus.car.model.response.GetLongValueResponse;
import com.kplus.car.model.response.UserLoginResponse;
import com.kplus.car.model.response.request.AddUserInfoRequest;
import com.kplus.car.model.response.request.BindThirdPartRequest;
import com.kplus.car.model.response.request.ChangePhoneRequest;
import com.kplus.car.model.response.request.GetAdvertDataRequest;
import com.kplus.car.model.response.request.GetPhoneVerificationCodeRequest;
import com.kplus.car.model.response.request.PhoneVerifyRequest;
import com.kplus.car.model.response.request.UserCheckRequest;
import com.kplus.car.model.response.request.UserLoginRequest;
import com.kplus.car.service.GetPromotionActivityInfoService;
import com.kplus.car.service.GetUserOpenImService;
import com.kplus.car.service.SynchronizationVehicle;
import com.kplus.car.util.SIMCardInfo;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PhoneRegistActivity extends BaseActivity implements OnClickListener {
    private static final int REQUEST_FOR_SELECT_BIND_OR_LOGIN = 1;

    private InputMethodManager mIMM = null;

    private EditText phoneText;
    private EditText codeText;
    private TextView codeBtn;
    private View confirmBtn;
    private View leftView;
    private ImageView ivLeft;
    private TextView tvTitle;
    private View qqView, weichatView, weiboView;
    private ImageView ivWechat, ivSinaWeibo, ivQQ;
    private View toHelp;
    private TextView tvSpeechVerify, tvSpeechVerifyHint;
    private View speechVerifyView;
    private CheckBox ckLicence;
    private TextView tvToLicence;
    private View checkLicenceView;

    private LinearLayout llNewUserTips = null;
    private ImageView ivNewUserTips = null;

    private String phonenumber;
    private int verifyType;//0短信1 语音
    private int sdkVersion;
    private int loginType;//0 手机 1 qq 2 微信 3 微博
    private long pId;
    private long id;
    private UserInfo userInfo;
    private List<DazeUserAccount> dazeUserAccounts;
    private boolean isMustPhone = false;
    private String strRequest = null;
    private String strResponseSuccess = null;
    private String strResponseFail = null;
    private String strCode;

    private boolean isNewUser = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.daze_phone_regist);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        mIMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        phoneText = (EditText) findViewById(R.id.phone_regist_phone);
        codeText = (EditText) findViewById(R.id.phone_regist_code);
        codeBtn = (TextView) findViewById(R.id.phone_regist_code_btn);
        confirmBtn = findViewById(R.id.phone_regist_info_confirm);
        leftView = findViewById(R.id.leftView);
        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        speechVerifyView = findViewById(R.id.speechVerifyView);
        tvSpeechVerify = (TextView) findViewById(R.id.tvSpeechVerify);
        tvSpeechVerifyHint = (TextView) findViewById(R.id.tvSpeechVerifyHint);
        qqView = findViewById(R.id.qqView);
        weichatView = findViewById(R.id.weichatView);
        weiboView = findViewById(R.id.weiboView);
        ivQQ = (ImageView) findViewById(R.id.ivQQ);
        ivSinaWeibo = (ImageView) findViewById(R.id.ivSinaWeibo);
        ivWechat = (ImageView) findViewById(R.id.ivWechat);
        toHelp = findViewById(R.id.toHelp);
        ckLicence = (CheckBox) findViewById(R.id.ckLicence);
        tvToLicence = (TextView) findViewById(R.id.tvToLicence);
        tvToLicence.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        checkLicenceView = findViewById(R.id.checkLicenceView);

        llNewUserTips = (LinearLayout) findViewById(R.id.llNewUserTips);
        ivNewUserTips = (ImageView) findViewById(R.id.ivNewUserTips);
    }

    protected void loadData() {
        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("phonenumber");
        if (!StringUtils.isEmpty(phonenumber))
            phoneText.setText(phonenumber);
        sdkVersion = android.os.Build.VERSION.SDK_INT;
        isMustPhone = intent.getBooleanExtra("isMustPhone", false);
        if (intent.hasExtra("isAuthen") && intent.getBooleanExtra("isAuthen", false))
            tvTitle.setText("车辆认证请先登录");
        else if (intent.hasExtra("isShare") && intent.getBooleanExtra("isShare", false))
            tvTitle.setText("分享请先登录");
        else
            tvTitle.setText("快速登录");
        // 一进入获取登录下面的广告
        getAdvertData(KplusConstants.ADVERT_USER_LOGIN);
    }

    @Override
    protected void setListener() {
        codeBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        leftView.setOnClickListener(this);
        speechVerifyView.setOnClickListener(this);
        qqView.setOnClickListener(this);
        weichatView.setOnClickListener(this);
        weiboView.setOnClickListener(this);
        toHelp.setOnClickListener(this);
        tvToLicence.setOnClickListener(this);
        checkLicenceView.setOnClickListener(this);
        qqView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (sdkVersion < 16)
                        ivQQ.setAlpha(128);
                    else
                        ivQQ.setImageAlpha(128);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (sdkVersion < 16)
                        ivQQ.setAlpha(255);
                    else
                        ivQQ.setImageAlpha(255);
                    if (isMustPhone) {
                        ToastUtil.TextToast(PhoneRegistActivity.this, "请绑定手机号", Toast.LENGTH_LONG, Gravity.CENTER);
                    } else {
                        if (!ckLicence.isChecked()) {
                            ToastUtil.TextToast(PhoneRegistActivity.this, "您未同意服务条款，无法登录", Toast.LENGTH_SHORT, Gravity.CENTER);
                        } else {
                            loginType = 1;
                            thirdPartLogin();
                        }
                    }
                }
                return false;
            }
        });
        weichatView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (sdkVersion < 16)
                        ivWechat.setAlpha(128);
                    else
                        ivWechat.setImageAlpha(128);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (sdkVersion < 16)
                        ivWechat.setAlpha(255);
                    else
                        ivWechat.setImageAlpha(255);
                    if (isMustPhone) {
                        ToastUtil.TextToast(PhoneRegistActivity.this, "请绑定手机号", Toast.LENGTH_LONG, Gravity.CENTER);
                    } else {
                        if (!ckLicence.isChecked()) {
                            ToastUtil.TextToast(PhoneRegistActivity.this, "您未同意服务条款，无法登录", Toast.LENGTH_SHORT, Gravity.CENTER);
                        } else {
                            loginType = 2;
                            thirdPartLogin();
                        }
                    }
                }
                return false;
            }
        });
        weiboView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (sdkVersion < 16)
                        ivSinaWeibo.setAlpha(128);
                    else
                        ivSinaWeibo.setImageAlpha(128);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (sdkVersion < 16)
                        ivSinaWeibo.setAlpha(255);
                    else
                        ivSinaWeibo.setImageAlpha(255);
                    if (isMustPhone) {
                        ToastUtil.TextToast(PhoneRegistActivity.this, "请绑定手机号", Toast.LENGTH_LONG, Gravity.CENTER);
                    } else {
                        if (!ckLicence.isChecked()) {
                            ToastUtil.TextToast(PhoneRegistActivity.this, "您未同意服务条款，无法登录", Toast.LENGTH_SHORT, Gravity.CENTER);
                        } else {
                            loginType = 3;
                            thirdPartLogin();
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        showloading(false);
        // 隐藏键盘
        mIMM.hideSoftInputFromWindow(phoneText.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(leftView)) {
            setResult(RESULT_CANCELED, getIntent());
            finish();
        } else if (v.equals(codeBtn)) {
            phonenumber = phoneText.getText().toString();
            if (StringUtils.isEmpty(phonenumber)) {
                ToastUtil.TextToast(PhoneRegistActivity.this, "请输入手机号", Toast.LENGTH_SHORT, Gravity.CENTER);
                return;
            }
            if (!SIMCardInfo.isMobileNO(phonenumber)) {
                ToastUtil.TextToast(PhoneRegistActivity.this, "手机号码错误", Toast.LENGTH_SHORT, Gravity.CENTER);
                return;
            }
            codeBtn.setEnabled(false);
            speechVerifyView.setEnabled(false);
            codeBtn.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
            tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
            codeText.requestFocus();
            verifyType = 0;
            getVerifyCode(phonenumber, "0");
        } else if (v.equals(confirmBtn)) {
            loginType = 0;
            phonenumber = phoneText.getText().toString();
            if (StringUtils.isEmpty(phonenumber)) {
                ToastUtil.TextToast(PhoneRegistActivity.this, "请输入手机号", Toast.LENGTH_SHORT, Gravity.CENTER);
                return;
            }
            strCode = codeText.getText().toString();
            if (StringUtils.isEmpty(strCode)) {
                ToastUtil.TextToast(PhoneRegistActivity.this, "请输入验证码", Toast.LENGTH_SHORT, Gravity.CENTER);
                return;
            }
            if (!ckLicence.isChecked()) {
                ToastUtil.TextToast(PhoneRegistActivity.this, "您未同意服务条款，无法登录", Toast.LENGTH_SHORT, Gravity.CENTER);
                return;
            }
//			getAccountSaved();
//			if(dazeUserAccounts != null && !dazeUserAccounts.isEmpty()){
//				userCheck();
//			}
//			else{
//				verify(phonenumber, strCode);
//			}
            verify(phonenumber, strCode);
        } else if (v.equals(speechVerifyView)) {
            phonenumber = phoneText.getText().toString();
            if (StringUtils.isEmpty(phonenumber)) {
                ToastUtil.TextToast(PhoneRegistActivity.this, "请输入手机号", Toast.LENGTH_SHORT, Gravity.CENTER);
                return;
            }
            if (!SIMCardInfo.isMobileNO(phonenumber)) {
                ToastUtil.TextToast(PhoneRegistActivity.this, "手机号码错误", Toast.LENGTH_SHORT, Gravity.CENTER);
                return;
            }
            codeBtn.setEnabled(false);
            speechVerifyView.setEnabled(false);
            codeBtn.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
            tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
            codeText.requestFocus();
            verifyType = 1;
            getVerifyCode(phonenumber, "1");
        } else if (v.equals(toHelp)) {
            Intent intent = new Intent(PhoneRegistActivity.this, VehicleServiceActivity.class);
            intent.putExtra("startPage", "http://weizhang.51zhangdan.com/content/help.html");
            intent.putExtra("appId", "null");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_from_right, 0);
        } else if (v.equals(tvToLicence)) {
            Intent intent = new Intent(PhoneRegistActivity.this, VehicleServiceActivity.class);
            intent.putExtra("startPage", KplusConstants.SERVER_URL + (KplusConstants.SERVER_URL.endsWith("/") ? "privacy.html" : "/privacy.html"));
            intent.putExtra("appId", "null");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_from_right, 0);
        } else if (v.equals(checkLicenceView)) {
            ckLicence.setChecked(!ckLicence.isChecked());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FOR_SELECT_BIND_OR_LOGIN) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.hasExtra("uid")) {
                        long uid = data.getLongExtra("uid", 0);
                        if (uid == 0) {
                            ToastUtil.TextToast(PhoneRegistActivity.this, "无效uid", Toast.LENGTH_SHORT, Gravity.CENTER);
                        } else {
                            if (loginType == 0)
                                bindPhone(uid);
                        }
                    } else {
                        if (loginType == 0)
                            verify(phonenumber, strCode);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getAccountSaved() {
        if (dazeUserAccounts != null)
            dazeUserAccounts.clear();
        dazeUserAccounts = mApplication.dbCache.getDazeUserAccounts();
        if (dazeUserAccounts != null && !dazeUserAccounts.isEmpty()) {
            List<DazeUserAccount> listTemp = new ArrayList<DazeUserAccount>();
            for (DazeUserAccount dua : dazeUserAccounts) {
                switch (loginType) {
                    case 0:
                        if (!StringUtils.isEmpty(dua.getPhoneLoginName()))
                            listTemp.add(dua);
                        break;
                    case 1:
                        if (!StringUtils.isEmpty(dua.getQqLoginName()))
                            listTemp.add(dua);
                        break;
                    case 2:
                        if (!StringUtils.isEmpty(dua.getWechatLoginName()))
                            listTemp.add(dua);
                        break;
                    case 3:
                        if (!StringUtils.isEmpty(dua.getWeiboLoginName()))
                            listTemp.add(dua);
                        break;
                }
                if (listTemp != null && !listTemp.isEmpty())
                    dazeUserAccounts.removeAll(listTemp);
            }
        }
    }

    private void thirdPartLogin() {

    }

    private void getVerifyCode(final String phone, final String type) {
        showloading(true);
        new AsyncTask<Object, Object, Response>() {
            private String errortext = "网络中断，请稍后重试";

            @Override
            protected Response doInBackground(Object... params) {
                GetPhoneVerificationCodeRequest request = new GetPhoneVerificationCodeRequest();
                request.setParams(phone, type);
                try {
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    errortext = "网络异常，请稍后重试";
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Response result) {
                if (result != null) {
                    if (result.getCode() != null && result.getCode() != 0) {
                        ToastUtil.TextToast(PhoneRegistActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                        codeBtn.setEnabled(true);
                        speechVerifyView.setEnabled(true);
                        codeBtn.setTextColor(getResources().getColor(R.color.daze_black2));
                        tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
                    } else {
                        Message message = new Message();
                        handler.sendMessageDelayed(message, 2);
                    }
                } else {
                    ToastUtil.TextToast(PhoneRegistActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
                    codeBtn.setEnabled(true);
                    speechVerifyView.setEnabled(true);
                    codeBtn.setTextColor(getResources().getColor(R.color.daze_black2));
                    tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
                }
                showloading(false);
            }
        }.execute();
    }

    private Handler handler = new Handler() {
        private int time = 59;

        public void handleMessage(android.os.Message msg) {
            if (verifyType == 0) {
                if (time > 0) {
                    codeBtn.setText("重新获取(" + time + ")");
                    time--;
                    Message message = new Message();
                    handler.sendMessageDelayed(message, 1000);
                } else {
                    codeBtn.setText("重新获取");
                    codeBtn.setTextColor(getResources().getColor(R.color.daze_black2));
                    tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
                    tvSpeechVerifyHint.setVisibility(View.GONE);
                    codeBtn.setEnabled(true);
                    speechVerifyView.setEnabled(true);
                    time = 60;
                }
            } else if (verifyType == 1) {
                if (time > 0) {
                    speechVerifyView.setVisibility(View.GONE);
                    tvSpeechVerifyHint.setVisibility(View.VISIBLE);
                    tvSpeechVerifyHint.setText("验证码将通过电话呼入并播报,请输入您听到的验证码。(" + time + ")");
                    time--;
                    Message message = new Message();
                    handler.sendMessageDelayed(message, 1000);
                } else {
                    speechVerifyView.setVisibility(View.VISIBLE);
                    tvSpeechVerifyHint.setVisibility(View.GONE);
                    codeBtn.setTextColor(getResources().getColor(R.color.daze_black2));
                    tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
                    codeBtn.setEnabled(true);
                    speechVerifyView.setEnabled(true);
                    time = 60;
                }
            }
        }

        ;
    };

    private void verify(final String phone, final String code) {
        showloading(true);
        new AsyncTask<Object, Object, GetLongValueResponse>() {
            private String errortext = "网络中断，请稍后重试";

            @Override
            protected GetLongValueResponse doInBackground(Object... params) {
                PhoneVerifyRequest request = new PhoneVerifyRequest();
                request.setParams(mApplication.getUserId(), phone, code);
                try {
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    errortext = "网络异常，请稍后重试";
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(GetLongValueResponse result) {
                showloading(false);
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0) {
                        pId = result.getData().getId();
                        if (pId == 0) {
                            ToastUtil.TextToast(PhoneRegistActivity.this, "验证失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                        } else {
                            login(phonenumber, loginType, null);
                        }
                    } else {
                        ToastUtil.TextToast(PhoneRegistActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } else {
                    ToastUtil.TextToast(PhoneRegistActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            }

        }.execute();
    }

    private void bind(final long uid, final String userName, final String nickName) {
        switch (loginType) {
            case 0:
                strRequest = "bind_phone";
                strResponseSuccess = "bind_phone_success";
                strResponseFail = "bind_phone_fail";
                break;
            case 1:
                strRequest = "bind_qq";
                strResponseSuccess = "bind_qq_success";
                strResponseFail = "bind_qq_fail";
                break;
            case 2:
                strRequest = "bind_wexin";
                strResponseSuccess = "bind_wexin_success";
                strResponseFail = "bind_wexin_fail";
                break;
            case 3:
                strRequest = "bind_weibo";
                strResponseSuccess = "bind_weibo_success";
                strResponseFail = "bind_weibo_fail";
                break;
        }
        new AsyncTask<Void, Void, BindThirdPartResponse>() {
            private String errortext = "网络中断，请稍后重试";

            protected void onPreExecute() {
                showloading(true);
            }

            @Override
            protected BindThirdPartResponse doInBackground(Void... params) {
                try {
                    BindThirdPartRequest request = new BindThirdPartRequest();
                    request.setParams(uid, userName, nickName, loginType);
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    errortext = e.toString();
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(BindThirdPartResponse result) {
                showloading(false);
                try {
                    if (result != null) {
                        if (result.getCode() != null && result.getCode() == 0) {
                            ThirdpartBindResultJson data = result.getData();
                            if (data != null) {
                                if (data.getResult() != null && data.getResult() && data.getUid() != null && data.getUid() != 0) {
                                    ToastUtil.TextToast(PhoneRegistActivity.this, "绑定成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                                    mApplication.setId(id);
                                    startService(new Intent(PhoneRegistActivity.this, SynchronizationVehicle.class));
                                    startService(new Intent(PhoneRegistActivity.this, GetPromotionActivityInfoService.class));
                                    finish();
                                } else {
                                    ToastUtil.TextToast(PhoneRegistActivity.this, "绑定失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                                }
                            } else {
                                ToastUtil.TextToast(PhoneRegistActivity.this, "绑定失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                            }
                        } else {
                            ToastUtil.TextToast(PhoneRegistActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                    } else {
                        ToastUtil.TextToast(PhoneRegistActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void sendNewUserBroad() {
        // 新用户弹出红包提示
        if (isNewUser) {
            // 发广播从主界面弹出广告
            Intent intent = new Intent("com.kplus.car.location.changed");
            intent.putExtra("adType", KplusConstants.ADVERT_NEW_USER);
            LocalBroadcastManager.getInstance(mApplication.getApplicationContext()).sendBroadcast(intent);
        }
    }

    private void login(final String name, final int type, final String nickName) {
        switch (type) {
            case 0:
                strRequest = "login_phone";
                strResponseSuccess = "login_phone_success";
                strResponseFail = "login_phone_fail";
                break;
            case 1:
                strRequest = "login_qq";
                strResponseSuccess = "login_qq_success";
                strResponseFail = "login_qq_fail";
                break;
            case 2:
                strRequest = "login_wexin";
                strResponseSuccess = "login_wexin_success";
                strResponseFail = "login_wexin_fail";
                break;
            case 3:
                strRequest = "login_weibo";
                strResponseSuccess = "login_weibo_success";
                strResponseFail = "login_weibo_fail";
                break;
        }
        new AsyncTask<Void, Void, UserLoginResponse>() {
            private String errorText = "网络中断，请稍后重试";

            protected void onPreExecute() {
                showloading(true);
            }

            @Override
            protected UserLoginResponse doInBackground(Void... params) {
                try {
                    UserLoginRequest request = new UserLoginRequest();
                    request.setParams(name, type, mApplication.getUserId(), nickName);
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorText = "网络异常，请稍后重试";
                    return null;
                }
            }

            protected void onPostExecute(UserLoginResponse result) {
                showloading(false);
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0) {
                        if (result.getData().getResult() != 0) {
                            id = result.getData().getResult();
                            isNewUser = false;
                            if (result.getData().getIsNewUser() != null) {
                                isNewUser = result.getData().getIsNewUser();
                            }

                            if (id != 0) {
                                ToastUtil.TextToast(PhoneRegistActivity.this, "登录成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                                mApplication.setId(id);

                                if (loginType != 0)
                                    updateUserInfo();
                                else {
                                    mApplication.setpId(pId);
                                    mApplication.dbCache.putValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE, phonenumber);
                                    try {
                                        DazeUserAccount dua = mApplication.dbCache.getDazeUserAccount(id);
                                        if (dua == null) {
                                            dua = new DazeUserAccount();
                                            dua.setUid(id);
                                        }
                                        dua.setPhoneLoginName(phonenumber);
                                        dua.setPhoneNumber(phonenumber);
                                        if (StringUtils.isEmpty(dua.getNickName()))
                                            dua.setNickName(phonenumber);
                                        mApplication.dbCache.saveDazeUserAccount(dua);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    LocalBroadcastManager.getInstance(PhoneRegistActivity.this).sendBroadcastSync(new Intent("com.kplus.car.login"));
                                    startService(new Intent(PhoneRegistActivity.this, SynchronizationVehicle.class));
                                    startService(new Intent(PhoneRegistActivity.this, GetPromotionActivityInfoService.class));
                                    startService(new Intent(PhoneRegistActivity.this, GetUserOpenImService.class));

                                    // 获取资料
                                    AppBridgeUtils.getIns().getUserInfo(PhoneRegistActivity.this);

                                    setResult(RESULT_OK);
                                    sendNewUserBroad();
                                    finish();
                                }
                            } else {
                                ToastUtil.TextToast(PhoneRegistActivity.this, "登录失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                            }
                        } else {
                            ToastUtil.TextToast(PhoneRegistActivity.this, "登录失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                    } else {
                        ToastUtil.TextToast(PhoneRegistActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } else {
                    ToastUtil.TextToast(PhoneRegistActivity.this, errorText, Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            }
        }.execute();
    }

    /**
     * 获取广告
     *
     * @param adType
     */
    private void getAdvertData(final String adType) {
        new AsyncTask<Void, Void, GetAdvertDataResponse>() {
            @Override
            protected GetAdvertDataResponse doInBackground(Void... params) {
                GetAdvertDataRequest request = new GetAdvertDataRequest();
                if (!StringUtils.isEmpty(mApplication.getCityId())) {
                    try {
                        request.setParams(Long.parseLong(mApplication.getCityId()), mApplication.getUserId(), mApplication.getId(), adType);
                        return mApplication.client.execute(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(GetAdvertDataResponse response) {
                super.onPostExecute(response);
                if (null != response && null != response.getCode()
                        && response.getCode() == 0) {
                    AdvertJson data = response.getData();
                    if (null != data) {
                        if (adType.equals(KplusConstants.ADVERT_USER_LOGIN)) {

                            String url = "";
                            List<Advert> userLogin = data.getUserLogin();
                            if (null != userLogin) {
                                for (int i = 0; i < userLogin.size(); i++) {
                                    Advert advert = userLogin.get(i);
                                    url = advert.getImgUrl();
                                    if (!TextUtils.isEmpty(url)) {
                                        break;
                                    }
                                }
                            }

                            // 登录下面的广告
                            if (!TextUtils.isEmpty(url)) {
                                llNewUserTips.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(url, ivNewUserTips);
                            } else {
                                llNewUserTips.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }.execute();
    }

    private void updateUserInfo() {
        new AsyncTask<Void, Void, AddUserInfoReponse>() {
            protected void onPreExecute() {
                showloading(true);
            }

            @Override
            protected AddUserInfoReponse doInBackground(Void... params) {
                try {
                    String iconUrl = null;
                    String nickName = null;
                    int sex = 0;
                    userInfo = mApplication.dbCache.getUserInfo();
                    if (userInfo == null) {
                        userInfo = new UserInfo();
                        userInfo.setUid(id);
                        userInfo.setIconUrl(iconUrl);
                        userInfo.setName(nickName);
                        userInfo.setSex(sex);
                    }
                    AddUserInfoRequest request = new AddUserInfoRequest();
                    request.setParams(mApplication.getId(), iconUrl, nickName, sex, null, null);
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(AddUserInfoReponse result) {
                startService(new Intent(PhoneRegistActivity.this, SynchronizationVehicle.class));
                startService(new Intent(PhoneRegistActivity.this, GetPromotionActivityInfoService.class));
                // 获取资料
                AppBridgeUtils.getIns().getUserInfo(PhoneRegistActivity.this);
                showloading(false);
                sendNewUserBroad();
                finish();
            }
        }.execute();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED, getIntent());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void userCheck() {
        new AsyncTask<Void, Void, BooleanResultResponse>() {
            private String errortext = "网络中断，请稍后重试";

            @Override
            protected BooleanResultResponse doInBackground(Void... arg0) {
                try {
                    UserCheckRequest request = new UserCheckRequest();
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    errortext = e.toString();
                    return null;
                }
            }

            protected void onPostExecute(BooleanResultResponse result) {
                showloading(false);
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0) {
                        if (result.getData() != null) {
                            if (result.getData().getResult() != null && result.getData().getResult()) {
                                Intent intent = new Intent(PhoneRegistActivity.this, SelectBindOrLoginActivity.class);
                                intent.putExtra("loginType", loginType);
                                startActivityForResult(intent, REQUEST_FOR_SELECT_BIND_OR_LOGIN);
                            }
                        } else {
                            ToastUtil.TextToast(PhoneRegistActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                    } else {
                        ToastUtil.TextToast(PhoneRegistActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } else {
                    ToastUtil.TextToast(PhoneRegistActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            }
        }.execute();
    }

    private void bindPhone(final long uid) {
        new AsyncTask<Void, Void, ChangePhoneResponse>() {
            private String errortext = "网络中断，请稍后重试";

            @Override
            protected ChangePhoneResponse doInBackground(Void... params) {
                try {
                    ChangePhoneRequest request = new ChangePhoneRequest();
                    request.setParams(mApplication.getUserId(), uid, phonenumber, strCode);
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    errortext = e.toString();
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(ChangePhoneResponse result) {
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0) {
                        ChangePhoneResultJson data = result.getData();
                        if (data != null) {
                            if (data.getResult() != null && data.getResult() && data.getUid() != null && data.getUid() != 0) {
                                id = data.getUid();
                                boolean isUidChange = (id == uid);
                                if (isUidChange) {
                                    mApplication.dbCache.deleteDazeUserAccount(uid);
                                }
                                ToastUtil.TextToast(PhoneRegistActivity.this, "绑定成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                                mApplication.setId(id);
                                startService(new Intent(PhoneRegistActivity.this, SynchronizationVehicle.class));
                                startService(new Intent(PhoneRegistActivity.this, GetPromotionActivityInfoService.class));
                                finish();
                            } else {
                                ToastUtil.TextToast(PhoneRegistActivity.this, "绑定手机号失败，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
                            }
                        } else {
                            ToastUtil.TextToast(PhoneRegistActivity.this, "绑定手机号失败，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                    } else {
                        ToastUtil.TextToast(PhoneRegistActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } else {
                    ToastUtil.TextToast(PhoneRegistActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            }

        }.execute();
    }
}
