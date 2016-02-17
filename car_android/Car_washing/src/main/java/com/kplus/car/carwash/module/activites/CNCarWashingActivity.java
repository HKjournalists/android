package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.Address;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.CNWeather;
import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.bean.Contact;
import com.kplus.car.carwash.bean.Coupon;
import com.kplus.car.carwash.bean.FetchServingStatusByRegionsResp;
import com.kplus.car.carwash.bean.FetchUsableCouponsResp;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.bean.Position;
import com.kplus.car.carwash.bean.ServingStatus;
import com.kplus.car.carwash.bean.ServingTime;
import com.kplus.car.carwash.bean.SubmitOrderResp;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.callback.IPopupItemClickListener;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.OrderStatus;
import com.kplus.car.carwash.module.adapter.CNCashingServiceAdapter;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNPayResultUtil;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNResourcesUtil;
import com.kplus.car.carwash.utils.CNServicesUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNUserHabitsUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.DateUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.http.ApiHandler;
import com.kplus.car.carwash.utils.http.HttpRequestField;
import com.kplus.car.carwash.utils.http.HttpRequestHelper;
import com.kplus.car.carwash.widget.CNAppointmentTimeView;
import com.kplus.car.carwash.widget.CNCustomScrollView;
import com.kplus.car.carwash.widget.CNNavigationBar;
import com.kplus.car.carwash.widget.CNWashingServiceListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 上门洗车界面
 * Created on 15/5/10.
 * author Zhixue
 */
public class CNCarWashingActivity extends CNParentActivity implements CNViewClickUtil.NoFastClickListener, IPopupItemClickListener, TextWatcher {
    private static final String TAG = "CNCarWashingActivity";

    private View mView = null;

    /**
     * 选择的服务
     */
    private ArrayList<OnSiteService> mSelectedServiceDatas = null;
    private ArrayList<Long> mSelectedServiceIds = null;
    private Contact mSelectedContact = null;
    /**
     * 选择的车辆
     */
    private Car mSelectedCar = null;
    /**
     * 选择的位置
     */
    private Position mSelectedPosition = null;
    /**
     * 选择的位置所在的服务范围
     */
    private ArrayList<Long> mRegionsIds = null;
    /**
     * 选择的代金券
     */
    private Coupon mSelectedCoupon = null;
    /**
     * 选择的城市
     */
    private City mSelectedCity = null;
    /**
     * 选择的服务时间
     */
    private ServingStatus mSelectedServingStatus = null;
    /**
     * 选择服务的价格，如果选择代金券，要相减
     */
    private BigDecimal mTotalPrice = new BigDecimal(Const.NONE);
    /**
     * 优惠价格
     */
    private BigDecimal mCouponPrice = new BigDecimal(Const.NONE);

    /**
     * 服务总价
     */
    private BigDecimal mServiceAmount = new BigDecimal(Const.NONE);

    /**
     * 提交订单返回过来的值
     */
    private long mOrderId = Const.NEGATIVE;
    /**
     * 用于支付
     */
    private long mFormOrderId = Const.NEGATIVE;

    private boolean isNewOrder = true;

    private CNCashingServiceAdapter mAdapter = null;

    /**
     * 服务时间
     */
    private List<ServingStatus> mServingStatuses = null;

    /**
     * 下单时的车牌号
     */
    private String mSubmitOrderCarLicence = null;
    /**
     * 下单时的时间
     */
    private long mSubmitOrderTime = Const.NONE;
    /**
     * 选择提交过的地址
     */
    private String mStrSelectedPosition = null;
    /**
     * 选择提交过的服务时间
     */
    private String mStrSelectedServingTime = null;

    private EditText etName = null;
    private EditText etMobile = null;
    private TextView tvCarInfo = null;
    private TextView tvCarLocation = null;
    private TextView tvVoucherPrice = null;
    private TextView tvServiceTime = null;
    private RelativeLayout rlWorkerPhone = null;
    private EditText etWorkerPhone = null;
    private ToggleButton tbtnSwitch = null;

    private TextView tvTotalPrice = null;
    private TextView tvCouponPrice = null;
    private Button btnPay = null;

    /**
     * 支付失败
     */
    private boolean isPayFailed = false;

    private AppBridgeUtils mAppBridgeUtils = null;
    /**
     * 是否重新设置车辆位置
     */
    private boolean isCoverAddress = true;
    private int mLastClickVoucherPosition = Const.NEGATIVE;

    /**
     * 代金券数据源
     */
    private List<Coupon> mVoucherDatas = null;

    @Override
    protected void initData() {
        AppBridgeUtils.getIns().onEvent(this, "pageView_washCar ", "上门洗车浏览量");

        mAppBridgeUtils = AppBridgeUtils.getIns();

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSelectedServiceDatas = (ArrayList<OnSiteService>) bundle.get(CNCarWashingLogic.KEY_SERVICE_SELECTED);
        }

        isCoverAddress = true;
        if (mAppBridgeUtils.getUid() == 0) {
            // 取用户资料
            AppBridgeUtils.getIns().getUserInfo(mContext);
        }

        Log.trace(TAG, "用户资料：uid-->" +
                mAppBridgeUtils.getUid() + " pid-->" +
                mAppBridgeUtils.getPid() + " userId-->" +
                mAppBridgeUtils.getUserId() + " userBalance-->" + mAppBridgeUtils.getUserBalance());
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_car_washing_layout, null, false);

        // 已选择的服务
        CNWashingServiceListView lvServiceList = findView(mView, R.id.lvServiceList);
        mAdapter = new CNCashingServiceAdapter(mContext, null, new OnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                chooseServiceItems();
            }
        });
        lvServiceList.setAdapter(mAdapter);

        CNCustomScrollView svScrollView = findView(mView, R.id.svScrollView);
        etName = findView(mView, R.id.etName);
        etMobile = findView(mView, R.id.etMobile);
        RelativeLayout rlCarInfo = findView(mView, R.id.rlCarInfo);
        tvCarInfo = findView(mView, R.id.tvCarInfo);
        RelativeLayout rlCarLocation = findView(mView, R.id.rlCarLocation);
        tvCarLocation = findView(mView, R.id.tvCarLocation);
        RelativeLayout rlServiceTime = findView(mView, R.id.rlServiceTime);
        RelativeLayout rlVoucher = findView(mView, R.id.rlVoucher);
        tvVoucherPrice = findView(mView, R.id.tvVoucherPrice);
        tvServiceTime = findView(mView, R.id.tvServiceTime);
        rlWorkerPhone = findView(mView, R.id.rlWorkerPhone);
        etWorkerPhone = findView(mView, R.id.etWorkerPhone);
        tbtnSwitch = findView(mView, R.id.tbtnSwitch);

        tvTotalPrice = findView(mView, R.id.tvTotalPrice);
        tvCouponPrice = findView(mView, R.id.tvCouponPrice);
        btnPay = findView(mView, R.id.btnPay);

        rlWorkerPhone.setVisibility(View.GONE);
        etWorkerPhone.setEnabled(tbtnSwitch.isChecked());

        svScrollView.smoothScrollTo(0, 0);
        final String mobile = mAppBridgeUtils.getMobile();
        final String license = mAppBridgeUtils.getLicense();
        tvCarInfo.setText(license);
        etMobile.setText(mobile);

        mSelectedCity = mApp.mSelectedCity;
        long tempCityId = mAppBridgeUtils.getSelectedCityId();
        if (null != mSelectedCity) {
            tempCityId = mSelectedCity.getId();
        }
        final long cityId = tempCityId;

        CNProgressDialogUtil.showProgress(this);

        loadingUserHabits(cityId);

        CNViewClickUtil.setNoFastClickListener(etName, this);
        CNViewClickUtil.setNoFastClickListener(etMobile, this);
        CNViewClickUtil.setNoFastClickListener(rlCarInfo, this);
        CNViewClickUtil.setNoFastClickListener(rlCarLocation, this);
        CNViewClickUtil.setNoFastClickListener(rlServiceTime, this);
        CNViewClickUtil.setNoFastClickListener(rlVoucher, this);
        CNViewClickUtil.setNoFastClickListener(btnPay, this);

        etMobile.addTextChangedListener(this);
        initServiceTimePopupWindow();

        tbtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etWorkerPhone.setEnabled(isChecked);
                if (isChecked) {
                    showInput(etWorkerPhone);
                } else {
                    hideInput(etWorkerPhone);
                    etWorkerPhone.setText("");
                }
                setWorkerPhoneTips();
            }
        });
    }

    private void chooseServiceItems() {
        if (isShowPopups()) {
            return;
        }
        // 将已选择的所有服务都传过去
        Intent intent = new Intent(mContext, CNServicesActivity.class);
        intent.putExtra(CNCarWashingLogic.KEY_SERVICE_SELECTED, mSelectedServiceDatas);
        CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_SELECTED_SERVICE);
    }

    private void chooseServiceAddress() {
        Intent intent = new Intent(mContext, CNCarLocationActivity.class);
        intent.putExtra(CNCarWashingLogic.KEY_SREVING_LOCATION, mSelectedPosition);
        CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_CAR_LOCATION);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        etMobile.removeTextChangedListener(this); //解除文字改变事件
        if (!TextUtils.isDigitsOnly(s)) {
            CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_right_mobile));
        }
        etMobile.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInput(etName);
        hideInput(etMobile);
    }

    @Override
    protected void onDestroy() {
        CNProgressDialogUtil.dismissProgress(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowPopups()) {
                return false;
            } else {
                onBack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 上门洗车
        navBar.setNavTitle(getStringResources(R.string.cn_car_washing));
        // 返回
        navBar.setLeftBtn(Const.NONE, R.drawable.btn_back_selector, Const.NONE);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft) {
            onBack();
        }
        return true;
    }

    private void onBack() {
        hideInput(etName);
        hideInput(etMobile);

        Intent data = new Intent();
        // 选择的服务
        data.putExtra(CNCarWashingLogic.KEY_SERVICE_SELECTED, mSelectedServiceDatas);
        setResult(Activity.RESULT_OK, data);
        CNViewManager.getIns().pop(this);
    }

    private void setWorkerPhoneTips() {
        if (tbtnSwitch.isChecked()) {
            etWorkerPhone.setHint(CNResourcesUtil.getStringResources(R.string.cn_enter_worker_phone));
        } else {
            etWorkerPhone.setHint(CNResourcesUtil.getStringResources(R.string.cn_is_enter_worker_phone));
        }
    }

    /**
     * 点击选择服务时间的回调
     */
    private CNAppointmentTimeView.OnServiceTimeItemClickListener mServiceClickListener = new CNAppointmentTimeView.OnServiceTimeItemClickListener() {
        @Override
        public void onItemClick(ServingStatus servingStatus) {
            mServiceTimePopup.dismiss();
            if (null != servingStatus) {
                // 点击单元格了设置时间
                mSelectedServingStatus = servingStatus;
                ServingTime servingTime = mSelectedServingStatus.getTime();
                String time = DateUtil.getServingTime(servingTime, true);
                tvServiceTime.setText(time);

                // 如果选择的是今天，要显示是否指定汽车工
                boolean isToday = DateUtil.isToday(servingTime.getDay().getTime());
                if (isToday) {
                    rlWorkerPhone.setVisibility(View.VISIBLE);
                } else {
                    rlWorkerPhone.setVisibility(View.GONE);
                    tbtnSwitch.setChecked(false);
                }
                setWorkerPhoneTips();
            }
        }

        @Override
        public void onTimePopHeight(int popHeight) {
//            if (null != mServiceTimePopup) {
//                // 如果pop的高度超过了一定高度，限制此pop的高度
//                int maxHeight = (int) (mDeviceSizePoint.y * 0.6f);
//                if (popHeight > maxHeight) {
//                    mServiceTimePopup.setHeight(maxHeight);
//                } else {
//                    mServiceTimePopup.setHeight(popHeight);
//                }
//            }
        }
    };

    private boolean isShowPopups() {
        return (null != mServiceTimePopup && mServiceTimePopup.isShowing());
    }

    private void clearCursorFocus() {
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);
        etName.clearFocus();
        etName.setSelected(false);

        etMobile.setFocusable(false);
        etMobile.setFocusableInTouchMode(false);
        etMobile.clearFocus();
        etMobile.setSelected(false);
    }

    @Override
    public void onNoFastClick(View v) {
        if (isShowPopups()) {
            return;
        }
        // library 中不能用 switch找控件
        int vId = v.getId();
        if (vId == R.id.etName) {
            etName.setFocusable(true);
            etName.setFocusableInTouchMode(true);
            etName.requestFocus();
            showInput(etName);
        } else if (vId == R.id.etMobile) {
            etMobile.setFocusable(true);
            etMobile.setFocusableInTouchMode(true);
            etMobile.requestFocus();
            showInput(etMobile);
        } else if (vId == R.id.rlCarInfo) { // 请输入车辆信息
            hideInput(etName);
            hideInput(etMobile);
            clearCursorFocus();
            Intent intent = new Intent(mContext, CNCarInfoActivity.class);
            intent.putExtra(CNCarWashingLogic.KEY_SELECTED_CAR_INFO, mSelectedCar);
            intent.putExtra(CNCarWashingLogic.KEY_SERVICE_SELECTED, mSelectedServiceDatas);
            CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_CAR_INFO);
        } else if (vId == R.id.rlCarLocation) { // 请确认车辆位置
            hideInput(etName);
            hideInput(etMobile);
            clearCursorFocus();
            chooseServiceAddress();
        } else if (vId == R.id.rlServiceTime) { // 请服务时间
            hideInput(etName);
            hideInput(etMobile);
            clearCursorFocus();

            // 先要取位置后再跟根据位置去取服务时间
            String carLocation = tvCarLocation.getText().toString().trim();
            if (CNStringUtil.isEmpty(carLocation)
                    || null == mSelectedPosition) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_Please_make_sure_the_location));
                return;
            }

            if (null == mServiceTimePopup) {
                initServiceTimePopupWindow();
            }
            mServiceTimePopup.showAtLocation(findView(mView, R.id.rlWashingMian), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, Const.NONE, Const.NONE);
            backgroundAlpha(0.5f);
        } else if (vId == R.id.rlVoucher) { // 代金券
            hideInput(etName);
            hideInput(etMobile);
            clearCursorFocus();
            CNCarWashingLogic.startVoucherDialog(mContext, mVoucherDatas, this);
        }
        /**
         * 立即支付
         */
        else if (vId == R.id.btnPay) {
            AppBridgeUtils.getIns().onEvent(CNCarWashingActivity.this, "click_imediaorder_washCar ", "选择服务，下一步，立即下单");

            // 表示提交成功过，又点下单
            hideInput(etName);
            hideInput(etMobile);
            clearCursorFocus();

            // 如果不是新单时，做其他判断是否相同单
            if (!isNewOrder) {
                if (isPayFailed // 是支付失败时，重复点击直接显示支付框
                        || (CNStringUtil.isNotEmpty(mSubmitOrderCarLicence)
                        && mSubmitOrderCarLicence.equals(mSelectedCar.getLicense()))
                        && (CNStringUtil.isNotEmpty(mStrSelectedPosition)
                        && mStrSelectedPosition.equals(tvCarLocation.getText().toString()))
                        && (CNStringUtil.isNotEmpty(mStrSelectedServingTime)
                        && mStrSelectedServingTime.equals(tvServiceTime.getText().toString()))) {
                    isPayFailed = false;
                    // 如果是相同的单，30分钟内直接支付
                    long endTime = System.currentTimeMillis();
                    long tempTime = endTime - mSubmitOrderTime;
                    if (tempTime <= Const.HALF_HOUR) {
                        CNCarWashingLogic.startPayDialog(mContext, mFormOrderId, mTotalPrice);
                        return;
                    }
                }
            }

            // 为空判断
            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String carInfo = tvCarInfo.getText().toString().trim();
            String carLocation = tvCarLocation.getText().toString().trim();
            String serviceTime = tvServiceTime.getText().toString().trim();

            if (CNStringUtil.isEmpty(name)) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_name_hint));
                // 设置光标
                etName.setFocusable(true);
                etName.setFocusableInTouchMode(true);
                etName.requestFocus();
                etName.requestFocusFromTouch();
                return;
            } else if (CNStringUtil.isEmpty(mobile)) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_mobile_hint));
                // 设置光标
                etMobile.setFocusable(true);
                etMobile.setFocusableInTouchMode(true);
                etMobile.requestFocus();
                etMobile.requestFocusFromTouch();
                return;
            } else {
                if (!isCheckMobile(mobile)) {
                    etMobile.getText().clear();

                    etMobile.setFocusable(true);
                    etMobile.setFocusableInTouchMode(true);
                    etMobile.requestFocus();
                    etMobile.requestFocusFromTouch();
                    return;
                }
            }
            if (CNStringUtil.isEmpty(carInfo)
                    || null == mSelectedCar) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_carinfo_hint));
                return;
            } else if (CNStringUtil.isEmpty(carLocation)
                    || null == mSelectedPosition) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_car_location_hint));
                return;
            } else if (CNStringUtil.isEmpty(serviceTime)
                    || null == mSelectedServingStatus) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_service_time));
                return;
            } else if (null == mSelectedServiceDatas) {
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_selected_service));
                return;
            }

            // 如果没有品牌、颜色，不能提交订单
            if (null == mSelectedCar.getBrand()
                    || mSelectedCar.getBrand().getId() <= Const.NONE
                    || null == mSelectedCar.getModel()
                    || mSelectedCar.getModel().getId() <= Const.NONE
                    || null == mSelectedCar.getColor()
                    || CNStringUtil.isEmpty(mSelectedCar.getColor().getName())) {
                tvCarInfo.setTextColor(getColorResources(R.color.cn_red));
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_carinfo_msg));
                return;
            }

            String strLicense = mSelectedCar.getLicense();
            // 取第一个省
            String pro = strLicense.substring(Const.NONE, Const.ONE);
            String num = strLicense.substring(Const.ONE, strLicense.length());
            Pattern pattern = Pattern.compile(Const.REGEX_CHINESE);
            Matcher matcher = pattern.matcher(pro);
            // 如果第一位不是中文，号码长度不为6位，让用户确认
            if (!matcher.find() || num.length() != Const.CAR_LICENCE_LENGTH) {
                tvCarInfo.setTextColor(getColorResources(R.color.cn_red));
                CNCommonManager.makeText(mContext, getStringResources(R.string.cn_licence_error));
                return;
            }

            // 判断是否输入洗车工手机号
            String workerPhone = "";
            if (rlWorkerPhone.getVisibility() == View.VISIBLE
                    && tbtnSwitch.isChecked()) {
                workerPhone = etWorkerPhone.getText().toString().trim();
                if (CNStringUtil.isEmpty(workerPhone)) {
                    CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_worker_phone));
                    // 设置光标
                    etWorkerPhone.setFocusable(true);
                    etWorkerPhone.setFocusableInTouchMode(true);
                    etWorkerPhone.requestFocus();
                    etWorkerPhone.requestFocusFromTouch();
                    showInput(etWorkerPhone);
                    return;
                } else {
                    if (!isCheckWorkerMobile(workerPhone)) {
//                        etWorkerPhone.getText().clear();
                        etWorkerPhone.setFocusable(true);
                        etWorkerPhone.setFocusableInTouchMode(true);
                        etWorkerPhone.requestFocus();
                        etWorkerPhone.requestFocusFromTouch();
                        showInput(etWorkerPhone);
                        return;
                    }
                }
            }

            // 用户信息
            Map<String, Object> contant = new HashMap<>();
            contant.put(HttpRequestField.NAME, name);
            contant.put(HttpRequestField.MOBILE, mobile);

            // 提交订单成功后，弹出支付
            submitOrder(contant, workerPhone);
        }
    }

    private boolean isCheckMobile(String mobile) {
        Pattern pattern = Pattern.compile(Const.REGEX_MOBILE_PHONE);
        Matcher matcher = pattern.matcher(mobile);
        if (!matcher.find()) {
            CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_right_mobile));
            return false;
        }
        return true;
    }

    private boolean isCheckWorkerMobile(String mobile) {
        Pattern pattern = Pattern.compile(Const.REGEX_MOBILE_PHONE);
        Matcher matcher = pattern.matcher(mobile);
        if (!matcher.find()) {
            CNCommonManager.makeText(mContext, getStringResources(R.string.cn_worker_phone_is_error));
            return false;
        }
        return true;
    }

    private void setBtnPay(boolean isEnabled) {
        btnPay.setEnabled(isEnabled);
        if (isEnabled) {
            btnPay.setBackgroundResource(R.drawable.btn_pay_selector);
        } else {
            btnPay.setBackgroundResource(R.drawable.btn_pay_dis_selector);
        }
    }

    private void setCarPosition(Address address) {
//        String carLotion = address.getProvince() + address.getCity() + address.getDistrict() + address.getStreet() + address.getOther();
        String carLotion = address.getOther();
        tvCarLocation.setText(carLotion);
    }

    private void loadingUserHabits(final long cityId) {
        // 从缓存中读取数据
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                mSelectedCar = CNUserHabitsUtil.getIns().getCar(cityId);
                mSelectedPosition = CNUserHabitsUtil.getIns().getPosition(cityId);
                if (null == mSelectedServiceDatas || mSelectedServiceDatas.isEmpty()) {
                    long usedServiceId = CNUserHabitsUtil.getIns().getUsedServiceId(cityId);
                    OnSiteService usedService = CNServicesUtil.getIns().getServiceById(cityId, usedServiceId);
                    mSelectedServiceDatas = new ArrayList<>();
                    mSelectedServiceDatas.add(usedService);
                }
                mSelectedContact = CNUserHabitsUtil.getIns().getContact(cityId);
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                final String mobile = mAppBridgeUtils.getMobile();
                final String license = mAppBridgeUtils.getLicense();

                // 车辆信息
                if (null == mSelectedCar) {
                    if (CNStringUtil.isNotEmpty(license)) {
                        tvCarInfo.setText(license);
                    }
                } else {
                    setHabitsCar(mSelectedCar);
                }

                // 位置
                if (null != mSelectedPosition) {
                    setHabitsPosition(mSelectedPosition);
                }

                // 服务
                if (null != mSelectedServiceDatas && mSelectedServiceDatas.size() > Const.NONE) {
                    setHabitsServices(mSelectedServiceDatas);

                    for (int i = 0; i < mSelectedServiceDatas.size(); i++) {
                        mSelectedServiceDatas.get(i).setIsChecked(true);
                    }
                    // 计算服务价格和服务id
                    makeSelectedService();
                    getFetchCoupons(mSelectedServiceIds);
                }

                // 个人信息
                if (null != mSelectedContact) {
                    setHabitsContact(mSelectedContact);
                } else {
                    if (CNStringUtil.isNotEmpty(mobile)) {
                        etMobile.setText(mobile);
                        etMobile.setSelection(etMobile.getText().length());
                    }
                }
                clearCursorFocus();
                setBtnPay(true);

                CNProgressDialogUtil.dismissProgress(mContext);
                if (null != mSelectedPosition) {
                    fetchCityServingStatusByRegions(cityId, true);
                }
            }
        });
    }

    private void setHabitsPosition(Position carPosition) {
        if (isCoverAddress) {
            mSelectedPosition = carPosition;
            if (null != carPosition) {
                Address address = carPosition.getAddress();
                setCarPosition(address);
            } else {
                tvCarLocation.setText("");
            }
        }
    }

    private void setHabitsCar(final Car car) {
        mSelectedCar = car;
        if (null != car) {
            // 根据选择的服务查找tagId，再根据tagId，查找支持的车型，再比较是否支持此车型
            final CarBrand brand = car.getBrand();
            final CarModel model = car.getModel();

            mApp.getThreadPool().submit(new CNThreadPool.Job<Boolean>() {
                @Override
                public Boolean run() {
                    boolean isSupportCarModel = true;
                    if (null != mSelectedServiceDatas && !mSelectedServiceDatas.isEmpty()
                            && null != brand && brand.getId() != Const.NONE
                            && null != model && model.getId() != Const.NONE) {
                        OnSiteService service = mSelectedServiceDatas.get(Const.NONE);
                        isSupportCarModel = CNCarWashingLogic.isSupportCarModelByServiceId(service.getCityId(), service.getId(), brand.getId());
                    }
                    return isSupportCarModel;
                }
            }, new FutureListener<Boolean>() {
                @Override
                public void onFutureDone(Future<Boolean> future) {
                    boolean isSupportCarModel = future.get();
                    if (!isSupportCarModel) {
                        CNCommonManager.makeText(mContext, CNResourcesUtil.getStringResources(R.string.cn_not_support_car_model));
                        mSelectedCar = null;
                        return;
                    }

                    CarColor carColor = car.getColor();
                    StringBuilder strBuilder = new StringBuilder();
                    if (CNStringUtil.isNotEmpty(car.getLicense().trim())) {
                        strBuilder.append(car.getLicense());
                    }
                    if (null != brand) {
                        strBuilder.append(" ").append(brand.getName());
                    }

                    if (null != model) {
                        strBuilder.append(" ").append(model.getName());
                    }
                    if (null != carColor) {
                        strBuilder.append(" ").append(carColor.getName());
                    }
                    tvCarInfo.setText(strBuilder.toString());
                }
            });
        } else {
            final String license = mAppBridgeUtils.getLicense();
            tvCarInfo.setText(license);
        }
    }

    private void setHabitsContact(Contact contact) {
        if (null != contact) {
            etName.setText(contact.getName());
            etName.setSelection(etName.getText().length());
            etMobile.setText(contact.getMobile());
            etMobile.setSelection(etMobile.getText().length());
        }
    }

    private void setHabitsServices(List<OnSiteService> services) {
        mAdapter.clear();
        mAdapter.append(services);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 按服务范围获取库存
     */
    private void fetchCityServingStatusByRegions(long cityId, boolean isProgress) {
        double lng = Const.NONE;
        double lat = Const.NONE;
        if (null != mSelectedPosition) {
            lng = mSelectedPosition.getLongitude();
            lat = mSelectedPosition.getLatitude();
        }
        HttpRequestHelper.fetchCityServingStatusByRegions(mContext, isProgress, cityId, mRegionsIds, lng, lat, new ApiHandler(mContext) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);

                FetchServingStatusByRegionsResp fetchServingStatusByRegionsResp = (FetchServingStatusByRegionsResp) baseInfo;
                mServingStatuses = fetchServingStatusByRegionsResp.getServingStatus();

                servicetimeView.setServiceTimeCellData(mServingStatuses, mServiceClickListener);
                setWeather();
                CNProgressDialogUtil.dismissProgress(mContext);
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                CNProgressDialogUtil.dismissProgress(mContext);
                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                    CNCommonManager.makeText(mContext, baseInfo.getMsg());

                    if ("您的车辆不在服务范围内".equals(baseInfo.getMsg())) {
                        // 重新打开服务选择界面
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chooseServiceAddress();
                            }
                        }, 300);
                    }
                }
            }
        });
    }

    /**
     * 代金券
     */
    private void getFetchCoupons(ArrayList<Long> serviceIds) {
        HttpRequestHelper.getFetchUsableCoupons(mContext, serviceIds, new ApiHandler(mContext) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                FetchUsableCouponsResp fetchUsableCouponsResp = (FetchUsableCouponsResp) baseInfo;
                List<Coupon> coupons = fetchUsableCouponsResp.getCoupons();
                mServiceAmount = fetchUsableCouponsResp.getServiceAmount();
                // 代金券价格
//                BigDecimal mTotalCouponPrice = fetchUsableCouponsResp.getCouponPrice();

                mVoucherDatas = new ArrayList<>();
                if (null != coupons && coupons.size() > Const.NONE) {
                    // 默认添加一个不使用代金券项目
                    Coupon notUsePay = new Coupon();
                    notUsePay.setId(CNCarWashingLogic.TYPE_NOT_USE_COUPON_PAY);
                    coupons.add(Const.NONE, notUsePay);

                    // 第一个
                    mLastClickVoucherPosition = Const.ONE;
                    mSelectedCoupon = coupons.get(Const.ONE);
                    mSelectedCoupon.setIsChecked(true);
                    // 默认显示第一个代金券
                    mCouponPrice = new BigDecimal(mSelectedCoupon.getAmount());
                    setUserCouponPrice(mCouponPrice);
                    setPrice(mServiceAmount, mCouponPrice, true);

                    mVoucherDatas.addAll(coupons);
                } else {
                    // 代金券0元
                    mCouponPrice = new BigDecimal(Const.NONE);
                }

                // 设置显示价格
                setUserCouponPrice(mCouponPrice);
                setPrice(mServiceAmount, mCouponPrice, true);
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                mVoucherDatas = new ArrayList<>();
                // 设置显示价格
                mCouponPrice = new BigDecimal(Const.NONE);
                setUserCouponPrice(mCouponPrice);
                setPrice(mServiceAmount, mCouponPrice, true);

                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                    CNCommonManager.makeText(mContext, baseInfo.getMsg());

                    if ("有部分服务已经停止运营".equals(baseInfo.getMsg())) {
                        // 重新打开服务选择界面
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chooseServiceItems();
                            }
                        }, 300);
                    }
                }
            }
        });
    }

    /**
     * 提交订单
     */
    private void submitOrder(final Map<String, Object> contant, String workerMobile) {
        int serviceSize = mSelectedServiceDatas.size();
        ArrayList<Long> serviceIds = new ArrayList<>();
        for (int i = Const.NONE; i < serviceSize; i++) {
            OnSiteService siteService = mSelectedServiceDatas.get(i);
            serviceIds.add(siteService.getId());
        }

        Map<String, Object> carInfos = new HashMap<>();
        if (null != mSelectedCar) {
            carInfos.put(HttpRequestField.LICENSE, mSelectedCar.getLicense());

            // brand
            Map<String, Object> brand = new HashMap<>();
            brand.put(HttpRequestField.NAME, mSelectedCar.getBrand().getName());
            brand.put(HttpRequestField.ID, mSelectedCar.getBrand().getId());
            carInfos.put(HttpRequestField.BRAND, brand);

            // model
            Map<String, Object> model = new HashMap<>();
            model.put(HttpRequestField.NAME, mSelectedCar.getModel().getName());
            model.put(HttpRequestField.ID, mSelectedCar.getModel().getId());
            carInfos.put(HttpRequestField.MODEL, model);

            // color
            Map<String, Object> color = new HashMap<>();
            color.put(HttpRequestField.NAME, mSelectedCar.getColor().getName());
            color.put(HttpRequestField.RGB, mSelectedCar.getColor().getRgb());
            carInfos.put(HttpRequestField.COLOR, color);
        }

        // 车辆位置
        Map<String, Object> carPositions = new HashMap<>();
        carPositions.put(HttpRequestField.LATITUDE, mSelectedPosition.getLatitude());
        carPositions.put(HttpRequestField.LONGTITUDE, mSelectedPosition.getLongitude());
        // address
        Map<String, Object> address = new HashMap<>();
        address.put(HttpRequestField.PROVINCE, mSelectedPosition.getAddress().getProvince());
        address.put(HttpRequestField.CITY, mSelectedPosition.getAddress().getCity());
        address.put(HttpRequestField.DISTRICT, mSelectedPosition.getAddress().getDistrict());
        address.put(HttpRequestField.STREET, mSelectedPosition.getAddress().getStreet());
        address.put(HttpRequestField.OTHER, mSelectedPosition.getAddress().getOther());
        carPositions.put(HttpRequestField.ADDRESS, address);

        // 服务时间
        Map<String, Object> servTime = new HashMap<>();

        if (null != mSelectedServingStatus) {
            ServingTime servingTime = mSelectedServingStatus.getTime();

            String date = DateUtil.format(DateUtil.YYYY_MM_DD, servingTime.getDay().getTime());

            servTime.put(HttpRequestField.DAY, date);
            servTime.put(HttpRequestField.BEGIN_MINS, servingTime.getBeginInMins());
            servTime.put(HttpRequestField.END_IN_MINS, servingTime.getEndInMins());
        }

        // 代金券
        ArrayList<Long> coupons = new ArrayList<>();
        if (null != mSelectedCoupon) {
            coupons.add(mSelectedCoupon.getId());
        }

        // 服务价格使用总价格
        HttpRequestHelper.submitOrder(mContext, serviceIds, contant, carInfos,
                carPositions, servTime, coupons, mServiceAmount, mCouponPrice, workerMobile, new ApiHandler(mContext) {
                    @Override
                    public void onSuccess(BaseInfo baseInfo) {
                        super.onSuccess(baseInfo);
                        SubmitOrderResp submitOrderResp = (SubmitOrderResp) baseInfo;
                        isNewOrder = false;
                        if (null != submitOrderResp) {
                            mOrderId = submitOrderResp.getOrderId();
                            mFormOrderId = submitOrderResp.getFormOrderId();
                            boolean duplicated = submitOrderResp.isDuplicated();

                            if (!duplicated) {
                                // 用于做限制，防止重复提交相同订单
                                mSubmitOrderCarLicence = mSelectedCar.getLicense();
                                mSubmitOrderTime = System.currentTimeMillis();
                                mStrSelectedPosition = tvCarLocation.getText().toString();
                                mStrSelectedServingTime = tvServiceTime.getText().toString();

                                CNCarWashingLogic.startPayDialog(mContext, mFormOrderId, mTotalPrice);

                                long userServiceId = Const.NONE;
                                if (null != mSelectedServiceDatas && mSelectedServiceDatas.size() > Const.NONE) {
                                    userServiceId = mSelectedServiceDatas.get(Const.NONE).getId();
                                }

                                // 下单成功，把这次的消费习惯记录下来
                                CNUserHabitsUtil.getIns().add(mSelectedCity.getId(), mSelectedContact, mSelectedCar,
                                        mSelectedPosition, userServiceId);
                                CNUserHabitsUtil.save();
                            } else {
                                // 是重复订单
                                if (CNStringUtil.isNotEmpty(baseInfo.getMsg())) {
                                    CNCommonManager.makeText(mContext, baseInfo.getMsg());
                                }

                                // 有重复订单时才有
                                mTotalPrice = new BigDecimal(submitOrderResp.getPrice());
                                int status = submitOrderResp.getStatus();

                                // 待支付状态弹出支付，否则直接跳转到详情
                                if (status != OrderStatus.PAY_PENDING.value()) {
                                    // 把订单id传过去
                                    CNCarWashingLogic.startOrderDetailsActivity(mContext, mOrderId, true);
                                } else {
                                    CNCarWashingLogic.startPayDialog(mContext, mFormOrderId, mTotalPrice);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(BaseInfo baseInfo) {
                        super.onFailure(baseInfo);
                        if (null != baseInfo) {
                            Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                            if (CNStringUtil.isNotEmpty(baseInfo.getMsg())) {
                                if ("此时间段预约已满".equals(baseInfo.getMsg())
                                        || "此时间段不营业".equals(baseInfo.getMsg())) {
                                    fetchCityServingStatusByRegions(mSelectedCity.getId(), true);
                                } else {
                                    CNCommonManager.makeText(mContext, baseInfo.getMsg());
                                }
                            } else {
                                CNCommonManager.makeText(mContext, "出现未知异常！！！");
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data)
            return;

        switch (requestCode) {
            case CNCarWashingLogic.TYPE_SELECTED_SERVICE: // 选择服务返回
                if (resultCode == Activity.RESULT_OK) {
                    isNewOrder = true;
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        mSelectedServiceDatas = (ArrayList<OnSiteService>) bundle.get(CNCarWashingLogic.KEY_SERVICE_SELECTED);
                        mAdapter.clear();
                        mAdapter.append(mSelectedServiceDatas);
                        mAdapter.notifyDataSetChanged();
                    }
                    // 计算服务价格和服务id
                    makeSelectedService();
                    getFetchCoupons(mSelectedServiceIds);
                }
                break;
            case CNCarWashingLogic.TYPE_CAR_INFO: // 选择车辆信息
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        tvCarInfo.setTextColor(getColorResources(R.color.cn_common_cars_bgcolor));
                        mSelectedCar = (Car) bundle.get(CNCarWashingLogic.KEY_SELECTED_CAR_INFO);
                        setHabitsCar(mSelectedCar);
                    }
                }
                break;
            case CNCarWashingLogic.TYPE_CAR_LOCATION: // 选择位置
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        Position carPosition = (Position) bundle.get(CNCarWashingLogic.KEY_SELECT_CAR_LOCATION);
                        City city = (City) bundle.get(CNCarWashingLogic.KEY_SELECT_CAR_LOC_CITY);
                        // 选择的服务范围
                        mRegionsIds = (ArrayList<Long>) bundle.get(CNCarWashingLogic.KEY_SELECT_REGION_AREAS);
                        isCoverAddress = true;
                        // 设置车辆位置
                        setHabitsPosition(carPosition);

                        servicetimeView.clear();

                        if (null != city) {
                            // 根据服务获取库存
                            fetchCityServingStatusByRegions(city.getId(), true);
                        }
                    }
                }
                break;
            default:
                Bundle bundle = data.getExtras();
                if (null != bundle) {
                    String str = bundle.getString("pay_result");
                    CNCarWashingLogic.makeYinLianPayResult(mContext, str);
                }
                break;
        }
    }

    private void makeSelectedService() {
        // 计算选择的服务的价格
        mSelectedServiceIds = new ArrayList<>();
        if (null != mSelectedServiceDatas) {
            // 选择服务后，更新下面要支付的钱
            for (int i = Const.NONE; i < mSelectedServiceDatas.size(); i++) {
                OnSiteService service = mSelectedServiceDatas.get(i);
                mSelectedServiceIds.add(service.getId());
                if (i == Const.NONE) {
                    // 每次计算时都要重新设置，不然可能会跟默认的相加
                    mTotalPrice = service.getPrice();
                } else {
                    if (null == mTotalPrice) {
                        mTotalPrice = new BigDecimal(Const.NONE);
                    }
                    if (null != service.getPrice()) {
                        mTotalPrice = mTotalPrice.add(service.getPrice());
                    }
                }
            }
        }
    }

    /**
     * @param serviceAmount 服务价格
     * @param couponPrice   优惠的代金券价格
     */
    private boolean setPrice(BigDecimal serviceAmount, BigDecimal couponPrice, boolean isUse) {
        double payPrice;
        double price;
        if (null == serviceAmount) {
            serviceAmount = new BigDecimal(Const.NONE);
        }
        if (null == couponPrice) {
            couponPrice = new BigDecimal(Const.NONE);
        }

        if (isUse) {
            mTotalPrice = serviceAmount.subtract(couponPrice);
        } else {
            mTotalPrice = serviceAmount.add(couponPrice);
        }

        // 如果价格等于或小0时，默认为0.01
        if (mTotalPrice.compareTo(new BigDecimal(0f)) <= Const.NONE) {
            mTotalPrice = new BigDecimal(0.01f);
        }

        // 原服务价格 保留2位小数
        double serPices = serviceAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 优惠后的价格
        payPrice = mTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 优惠价格
        price = couponPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        String msg = String.format(getStringResources(R.string.cn_total_price), payPrice);
        tvTotalPrice.setText(msg);
        msg = String.format(getStringResources(R.string.cn_coupon_price), serPices, price);
        tvCouponPrice.setText(msg);
        return true;
    }

    private PopupWindow mServiceTimePopup = null;
    private CNAppointmentTimeView servicetimeView = null;

    private void initServiceTimePopupWindow() {
        View view = mInflater.inflate(R.layout.cn_service_time_layout, null);
        mServiceTimePopup = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mServiceTimePopup.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        //设置弹出窗体动画效果
        mServiceTimePopup.setAnimationStyle(R.style.PopupAnimation);
        mServiceTimePopup.setOutsideTouchable(true);
        mServiceTimePopup.setFocusable(true);
        mServiceTimePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        servicetimeView = findView(view, R.id.servicetimeView);
        servicetimeView.setServiceTimeCellData(mServingStatuses, mServiceClickListener);
    }

    private void setWeather() {
        if (null == mAppBridgeUtils) {
            mAppBridgeUtils = AppBridgeUtils.getIns();
        }
        if (null != mApp.mSelectedCity) {
            Log.trace(TAG, "加载天气...");
            long cityId = mApp.mSelectedCity.getId();
            // 先从本地获取天气，如果没有获取到就服务器上获取，获取到了通过广播发回来
            List<CNWeather> weathers = mAppBridgeUtils.getWeather(mContext, cityId);
            if (null != servicetimeView) {
                servicetimeView.setWeather(weathers);
            }
        }
    }

    @Override
    public void onClickPopupItem(int popupType, int position, View v, BaseInfo data) {
        switch (popupType) {
            case CNCarWashingLogic.DIALOG_VOUCHER_TYPE: // 点击代金券
                if (null == data) {
                    return;
                }
                mSelectedCoupon = (Coupon) data;

                if (mSelectedCoupon.getId() == CNCarWashingLogic.TYPE_NOT_USE_COUPON_PAY) {
                    // 不使用代金券
                    setVoucherIsSelected(position, false);
                    mLastClickVoucherPosition = Const.NEGATIVE;

                    mCouponPrice = new BigDecimal(Const.NONE);
                    setUserCouponPrice(mCouponPrice);
                    setPrice(mServiceAmount, mCouponPrice, false);
                    mSelectedCoupon = null;
                } else {
                    // 使用代金券
                    mCouponPrice = new BigDecimal(mSelectedCoupon.getAmount());
                    boolean isUse = setPrice(mServiceAmount, mCouponPrice, true);
                    if (isUse) {
                        mSelectedCoupon.setIsChecked(true);
                        // 设置该项为选择状态
                        if (null != mVoucherDatas && mVoucherDatas.size() > position) {
                            mVoucherDatas.get(position).setIsChecked(true);
                        }

                        // 设置选择的代金券
                        setUserCouponPrice(new BigDecimal(mSelectedCoupon.getAmount()));
                        setVoucherIsSelected(position, false);
                        mLastClickVoucherPosition = position;
                    } else {
                        mSelectedCoupon = null;
                        mCouponPrice = new BigDecimal(Const.NONE);
                    }
                }
                break;
        }
    }

    private void setVoucherIsSelected(int position, boolean isChecked) {
        if (mLastClickVoucherPosition != Const.NEGATIVE && mLastClickVoucherPosition != position) {
            if (null != mVoucherDatas && mVoucherDatas.size() > mLastClickVoucherPosition) {
                mVoucherDatas.get(mLastClickVoucherPosition).setIsChecked(isChecked);
            }
        }
    }

    /**
     * 设置显示选择的代金券金额
     *
     * @param price 代金券金额
     */
    private void setUserCouponPrice(BigDecimal price) {
        // 设置选择的代金券
        if (null == price) {
            price = new BigDecimal(Const.NONE);
        }
        if (price.compareTo(new BigDecimal(Const.NONE)) <= Const.NONE) {
            tvVoucherPrice.setText("");
        } else {
            double couPrice = price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String value = String.format(getStringResources(R.string.cn_order_price), couPrice);
            tvVoucherPrice.setText(value);
        }
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_PAY_RESULT_ACTION);
        filter.addAction(CustomBroadcast.ON_WEATHER_RESULT_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        String action = intent.getAction();
        if (CustomBroadcast.ON_PAY_RESULT_ACTION.equals(action)) {
            String result = intent.getStringExtra("result");
            boolean isPayResult = CNCarWashingLogic.makePayResult(result);
            if (isPayResult) {
                isPayFailed = false;
                // Notice 支付成功后手动把本地状态改为已支付，防止服务器支付结果慢的情况
                CNPayResultUtil.getIns().add(mOrderId, mFormOrderId, OrderStatus.PAID.value());
                CNProgressDialogUtil.dismissProgress(mContext);
                CNViewManager.getIns().pop(CNServicesDisplayActivity.class);
                CNViewManager.getIns().showActivity(mContext, CNPayFinishActivity.class, true);
            } else {
                isPayFailed = true;
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        } else if (CustomBroadcast.ON_WEATHER_RESULT_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                long cityId = bundle.getLong("cityId", 0);
                ArrayList<CNWeather> weathers = (ArrayList<CNWeather>) bundle.get("weathers");
                // 回来的结果是否与当前选择相等，天气异步获取的
                long selectedCityId = mApp.mSelectedCity.getId();
                if (selectedCityId == cityId) {
                    Log.trace(TAG, "通过广播收到天气情况...");
                    // 从服务器上获取回来的天气
                    if (null != servicetimeView) {
                        servicetimeView.setWeather(weathers);
                    }
                }
            }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha alpha值 0.0-1.0
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
