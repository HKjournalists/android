package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWUIPushListener;
import com.alibaba.mobileim.login.YWLoginState;
import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.FWCityServiceTask;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.FWCityService;
import com.kplus.car.model.FWRequestInfo;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.model.json.FWSearchProviderJson;
import com.kplus.car.model.response.FWCityServiceResponse;
import com.kplus.car.model.response.FWRequestListResponse;
import com.kplus.car.model.response.FWSearchProviderResponse;
import com.kplus.car.model.response.FWSendRequestResponse;
import com.kplus.car.model.response.GetUserOpenImResponse;
import com.kplus.car.model.response.request.FWRequestListRequest;
import com.kplus.car.model.response.request.FWSearchProviderRequest;
import com.kplus.car.model.response.request.FWSendRequestRequest;
import com.kplus.car.model.response.request.GetUserOpenImRequest;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.FlowLayout;
import com.kplus.car.widget.SlideUpMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/8/11.
 */
public class GuanjiaHomeActivity extends BaseActivity implements ClickUtils.NoFastClickListener, IYWUIPushListener {
    private List<FWCityService> mListCityService;
    private FlowLayout mCityServiceLayout;
    private TextView mTvLeft;
    private TextView mTvRequestNum;
    private int mLastSelectIndex = -1;
    private int mTextLength = 0;
    private View mServiceHead;
    private SlideUpMenu mSlideUpMenu;
    private List<FWRequestInfo> mListRequest;
    private View mSearchLayout;
    private EditText mSearchInput;
    private TextView mNoServiceHint;
    private TextView mGuanjiaBtn;

    private BroadcastReceiver locationChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTvLeft.setText(mApplication.getCityName());
            getCityService();
        }
    };

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getGuanjiaHistory();
        }
    };

    private BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mServiceHead.setVisibility(View.GONE);
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guanjia_home);
        mCityServiceLayout = (FlowLayout) findViewById(R.id.city_service);
        mTvLeft = (TextView) findViewById(R.id.tvLeft);
        mServiceHead = findViewById(R.id.service_head);
        mTvRequestNum = (TextView) findViewById(R.id.request_num);
        mSearchLayout = findViewById(R.id.search_layout);
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mNoServiceHint = (TextView) findViewById(R.id.no_service_hint);
        mGuanjiaBtn = (TextView) findViewById(R.id.guanjia_btn);
    }

    @Override
    protected void loadData() {
        mListRequest = mApplication.dbCache.getRequestInfo();
        if (mListRequest == null)
            mListRequest = new ArrayList<>();
        if (!StringUtils.isEmpty(mApplication.getCityId())){
            mTvLeft.setText(mApplication.getCityName());
            getCityService();
        }
        if (mApplication.getId() != 0){
            int currentTab = getIntent().getIntExtra("currentTab", 0);
            if (currentTab == 3){
                String providerId = getIntent().getStringExtra("providerId");
                String serviceType = getIntent().getStringExtra("serviceType");
                String cityId = getIntent().getStringExtra("cityId");
                Intent intent = new Intent(this, VehicleServiceActivity.class);
                intent.putExtra("appId", "10000012");
                intent.putExtra("startPage", "supplier-detail.html?id=" + providerId + "&serviceId=" + serviceType + "&cityId=" + cityId + "&userId=" + mApplication.getUserId());
                startActivity(intent);
            }
            getGuanjiaHistory();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(locationChangeReceiver, new IntentFilter("com.kplus.car.location.changed"));
        LocalBroadcastManager.getInstance(this).registerReceiver(loginReceiver, new IntentFilter("com.kplus.car.login"));
        LocalBroadcastManager.getInstance(this).registerReceiver(logoutReceiver, new IntentFilter("com.kplus.car.logout"));
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mGuanjiaBtn, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.annotation), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rlMessages), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.search), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.search_btn), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.search_bg), this);
        ClickUtils.setNoFastClickListener(mServiceHead, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume() {
        super.onResume();
        if (!mTvLeft.getText().equals(mApplication.getCityName())){
            mTvLeft.setText(mApplication.getCityName());
            getCityService();
        }
        if (mApplication.getId() != 0){
            if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
                getUserOpenIm();
            }
            else{
                mApplication.initTaobao();
                mApplication.mYWIMKIT.registerPushListener(this);
                if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
                    int count = mApplication.mYWIMKIT.getUnreadCount();
                    TextView messageCount = (TextView) findViewById(R.id.tvMessageNumber);
                    if (count > 0) {
                        messageCount.setVisibility(View.VISIBLE);
                        messageCount.setText(String.valueOf(count));
                    } else {
                        messageCount.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationChangeReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutReceiver);
        super.onDestroy();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.service_head:
                if (mApplication.isUserLogin(false, null)){
                    Intent intent = new Intent(this, GuanjiaHistoryActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_TYPE_GUANJIA_HISTORY);
                }
                break;
            case R.id.guanjia_btn:
                if (mListCityService != null && mListCityService.size() > 0){
                    if (mApplication.getId() == 0){
                        Intent intent = new Intent(this, PhoneRegistActivity.class);
                        intent.putExtra("isMustPhone", true);
                        startActivityForResult(intent, Constants.REQUEST_TYPE_LOGIN);
                    }
                    else {
                        sendRequest();
                    }
                }
                break;
            case R.id.annotation:
                Intent intent = new Intent(this, VehicleServiceActivity.class);
                intent.putExtra("appId", "10000012");
                intent.putExtra("startPage", "auth.html?noApply=true");
                startActivity(intent);
                break;
            case R.id.leftView:
                intent = new Intent(this, CitySelectActivity.class);
                intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_HOME);
                intent.putExtra("cityName", mApplication.getCityName());
                startActivityForResult(intent, Constants.REQUEST_TYPE_CITY);
                break;
            case R.id.rlMessages:
                EventAnalysisUtil.onEvent(this, "click_news_rightTitleBar", "在线客服", null);
                if(mApplication.getId() == 0){
                    startActivity(new Intent(this, PhoneRegistActivity.class));
                }
                else if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
                    getUserOpenIm();
                }
                else{
                    mApplication.initTaobao();
                    mApplication.mYWIMKIT.registerPushListener(this);
                    if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
                        intent = mApplication.mYWIMKIT.getConversationActivityIntent();
                        startActivity(intent);
                    }
                    else if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.logining ){
                        ToastUtil.TextToast(this, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                    else{
                        ToastUtil.TextToast(this, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
                        mApplication.loginTaobao(mApplication.getOpenImUserId(), mApplication.getOpenImPassWord());
                    }
                }
                break;
            case R.id.search:
                if (mApplication.isUserLogin(false, null)){
                    if (mSearchLayout.getVisibility() == View.VISIBLE)
                        mSearchLayout.setVisibility(View.GONE);
                    else {
                        mSearchLayout.setVisibility(View.VISIBLE);
                        mSearchInput.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mSearchInput, InputMethodManager.SHOW_FORCED);
                        EventAnalysisUtil.onEvent(this, "click_search_rightTitleBar", "查找", null);
                    }
                }
                break;
            case R.id.search_btn:
                EventAnalysisUtil.onEvent(this, "click_searchProvider_search", "查找服务商", null);
                final String phone = mSearchInput.getText().toString();
                if (StringUtils.isEmpty(phone)){
                    ToastUtil.TextToast(this, "请输入服务商手机号", Toast.LENGTH_SHORT, Gravity.CENTER);
                    return;
                }
                new AsyncTask<Void, Void, FWSearchProviderResponse>(){
                    @Override
                    protected FWSearchProviderResponse doInBackground(Void... params) {
                        FWSearchProviderRequest request = new FWSearchProviderRequest();
                        request.setParams(phone);
                        try {
                            return mApplication.client.execute(request);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(FWSearchProviderResponse response) {
                        if (response != null && response.getCode() != null && response.getCode() == 0){
                            mSearchLayout.setVisibility(View.GONE);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mSearchInput.getWindowToken(), 0);
                            FWSearchProviderJson data = response.getData();
                            Intent intent = new Intent(GuanjiaHomeActivity.this, VehicleServiceActivity.class);
                            intent.putExtra("appId", "10000012");
                            intent.putExtra("startPage", "supplier-detail.html?id=" + data.getId() + "&cityId=" + data.getCityId() + "&userId=" + mApplication.getUserId() + "#nocache");
                            startActivity(intent);
                        }
                        else if (response != null){
                            ToastUtil.TextToast(GuanjiaHomeActivity.this, "没有找到指定服务商", Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                    }
                }.execute();
                break;
            case R.id.search_bg:
                mSearchLayout.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchInput.getWindowToken(), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constants.REQUEST_TYPE_CITY:
                if (resultCode == RESULT_OK){
                    ArrayList<CityVehicle> listTemp = data.getParcelableArrayListExtra("selectedCity");
                    if(listTemp != null && !listTemp.isEmpty()) {
                        CityVehicle cv = listTemp.get(0);
                        String cityName = cv.getName();
                        if (cityName != null) {
                            mApplication.setCityName(cityName);
                            mTvLeft.setText(cityName);
                            getCityService();
                        }
                        if(cv.getProvince() != null)
                            mApplication.setProvince(cv.getProvince());
                    }
                }
                break;
            case Constants.REQUEST_TYPE_LOGIN:
                if (resultCode == RESULT_OK)
                    sendRequest();
                break;
            case Constants.REQUEST_TYPE_GUANJIA_HISTORY:
                mListRequest = mApplication.dbCache.getRequestInfo();
                if (mListRequest == null)
                    mListRequest = new ArrayList<>();
                break;
        }
    }

    private void sendRequest(){
        if (mLastSelectIndex == -1){
            ToastUtil.makeShortToast(this, "请选择您需要的服务");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("service_name", mListCityService.get(mLastSelectIndex).getName());
        EventAnalysisUtil.onEvent(this, "click_yjgj", "一键管家", map);
        if (mSlideUpMenu == null){
            mSlideUpMenu = new SlideUpMenu(this, R.color.daze_translucence2);
            mSlideUpMenu.setContentView(R.layout.menu_send_request, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.close:
                            mSlideUpMenu.hide();
                            break;
                        case R.id.request:
                            HashMap<String, String> map = new HashMap<>();
                            map.put("service_name", mListCityService.get(mLastSelectIndex).getName());
                            EventAnalysisUtil.onEvent(GuanjiaHomeActivity.this, "click_callgj_yjgj", "呼叫管家", map);
                            if (mTextLength > 120){
                                ToastUtil.makeShortToast(GuanjiaHomeActivity.this, "说明在120个字以内");
                                return;
                            }
                            mSlideUpMenu.hide();
                            EditText editText = (EditText) findViewById(R.id.input);
                            final String extendsion = editText.getText().toString();
                            new AsyncTask<Void, Void, FWSendRequestResponse>() {
                                @Override
                                protected FWSendRequestResponse doInBackground(Void... params) {
                                    try {
                                        FWCityService cityService = mListCityService.get(mLastSelectIndex);
                                        FWSendRequestRequest request = new FWSendRequestRequest();
                                        double lat = 0, lon = 0;
                                        if (mApplication.getLocation() != null) {
                                            lat = mApplication.getLocation().getLatitude();
                                            lon = mApplication.getLocation().getLongitude();
                                        }
                                        request.setParams(mApplication.getId(), mApplication.getCityId(), cityService.getId().intValue(), lat, lon, cityService.getName(), extendsion);
                                        return mApplication.client.execute(request);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                }

                                @Override
                                protected void onPostExecute(FWSendRequestResponse response) {
                                    if (response != null && response.getCode() != null && response.getCode() == 0) {
                                        getGuanjiaHistory();
                                        Intent intent = new Intent(GuanjiaHomeActivity.this, GuanjiaSubmitSuccessActivity.class);
                                        startActivity(intent);
                                    } else if (response != null)
                                        ToastUtil.makeShortToast(GuanjiaHomeActivity.this, response.getMsg());
                                    else
                                        ToastUtil.makeShortToast(GuanjiaHomeActivity.this, "网络中断，请稍后重试");
                                }
                            }.execute();
                            break;
                    }
                }
            });
            final TextView num = (TextView) findViewById(R.id.actual_num);
            final TextView hint = (TextView) findViewById(R.id.text_length_hint);
            final int orange = getResources().getColor(R.color.daze_orangered5);
            final int red = getResources().getColor(R.color.daze_darkred2);
            EditText editText = (EditText) findViewById(R.id.input);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mTextLength += (count - before);
                    num.setText(String.valueOf(mTextLength));
                    if (mTextLength <= 120) {
                        hint.setVisibility(View.GONE);
                        num.setTextColor(orange);
                    } else {
                        hint.setVisibility(View.VISIBLE);
                        num.setTextColor(red);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        TextView name = (TextView) findViewById(R.id.selected_service_name);
        name.setText(mListCityService.get(mLastSelectIndex).getName());
        EditText editText = (EditText) findViewById(R.id.input);
        editText.setText("");
        mSlideUpMenu.show();
    }

    private void getGuanjiaHistory(){
        new AsyncTask<Void, Void, FWRequestListResponse>(){
            @Override
            protected FWRequestListResponse doInBackground(Void... params) {
                try {
                    FWRequestListRequest req = new FWRequestListRequest();
                    req.setParams(mApplication.getId(), 0);
                    return mApplication.client.execute(req);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(FWRequestListResponse response) {
                if (response != null && response.getCode() != null && response.getCode() == 0){
                    mListRequest = response.getData().getList();
                    mApplication.dbCache.saveRequestInfo(mListRequest);
                    if (mListRequest.size() > 0){
                        mServiceHead.setVisibility(View.VISIBLE);
                        mTvRequestNum.setText(String.valueOf(mListRequest.size()));
                    }
                    else {
                        mServiceHead.setVisibility(View.GONE);
                    }
                }
            }
        }.execute();
    }

    private void getCityService(){
        new FWCityServiceTask(mApplication){
            @Override
            protected void onPostExecute(FWCityServiceResponse response) {
                mLastSelectIndex = -1;
                if (response != null && response.getCode() != null && response.getCode() == 0){
                    mListCityService = response.getData().getList();
                    mCityServiceLayout.removeAllViews();
                    if (mListCityService != null && mListCityService.size() > 0){
                        mNoServiceHint.setVisibility(View.GONE);
                        mGuanjiaBtn.setBackgroundResource(R.drawable.orange_corner_2_selector);
                        for (int i = 0; i < mListCityService.size(); i++){
                            final int index = i;
                            FWCityService cityService = mListCityService.get(i);
                            final TextView tv = (TextView) LayoutInflater.from(GuanjiaHomeActivity.this).inflate(R.layout.item_city_service, mCityServiceLayout, false);
                            tv.setText(cityService.getName());
                            ClickUtils.setNoFastClickListener(tv, new ClickUtils.NoFastClickListener() {
                                @Override
                                public void onNoFastClick(View v) {
                                    if (mLastSelectIndex == index)
                                        return;
                                    if (mLastSelectIndex >= 0){
                                        TextView tvOld = (TextView) mCityServiceLayout.getChildAt(mLastSelectIndex);
                                        tvOld.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
                                        tvOld.setBackgroundColor(getResources().getColor(R.color.daze_white_smoke6));
                                    }
                                    mLastSelectIndex = index;
                                    tv.setTextColor(getResources().getColor(R.color.daze_white));
                                    tv.setBackgroundColor(getResources().getColor(R.color.daze_orangered5));
                                }
                            });
                            mCityServiceLayout.addView(tv);
                        }
                    }
                    else {
                        mNoServiceHint.setVisibility(View.VISIBLE);
                        mGuanjiaBtn.setBackgroundResource(R.drawable.gray_corner_2);
                    }
                }
            }
        }.execute();
    }

    private void getUserOpenIm(){
        new AsyncTask<Void, Void, GetUserOpenImResponse>(){
            @Override
            protected GetUserOpenImResponse doInBackground(Void... params) {
                GetUserOpenImRequest request = new GetUserOpenImRequest();
                request.setParams(mApplication.getId());
                try {
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(GetUserOpenImResponse result) {
                if(result != null && result.getCode() != null && result.getCode() == 0){
                    UserOpenIm data = result.getData();
                    if(data != null && !StringUtils.isEmpty(data.getOpenUserid()) && !StringUtils.isEmpty(data.getOpenPassword())){
                        mApplication.initTaobao();
                        mApplication.loginTaobao(data.getOpenUserid(), data.getOpenPassword());
                        mApplication.setOpenImUserId(data.getOpenUserid());
                        mApplication.setOpenImPassWord(data.getOpenPassword());
                        mApplication.mYWIMKIT.registerPushListener(GuanjiaHomeActivity.this);
                    }
                    else{
                        ToastUtil.TextToast(GuanjiaHomeActivity.this, !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                }
                else{
                    ToastUtil.TextToast(GuanjiaHomeActivity.this, result != null && !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            }
        }.execute();
    }

    @Override
    public void onMessageComing() {
        if (mApplication.mYWIMKIT != null) {
            int count = mApplication.mYWIMKIT.getUnreadCount();
            TextView messageCount = (TextView) findViewById(R.id.tvMessageNumber);
            if (count > 0) {
                messageCount.setVisibility(View.VISIBLE);
                messageCount.setText(String.valueOf(count));
            } else {
                messageCount.setVisibility(View.GONE);
            }
        }
    }
}
