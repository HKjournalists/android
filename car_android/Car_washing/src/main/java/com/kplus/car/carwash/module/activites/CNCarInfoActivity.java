package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.bean.CarBrand;
import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.callback.IPopupItemClickListener;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.manager.DialogManager;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.adapter.CNCommonCarsAdapter;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNCarBrandUtil;
import com.kplus.car.carwash.utils.CNCarColorUtil;
import com.kplus.car.carwash.utils.CNCommonCarUtil;
import com.kplus.car.carwash.utils.CNInitializeApiDataUtil;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNResourcesUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.widget.CNNavigationBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：车辆信息
 * <br/><br/>Created by Fu on 2015/5/11.
 * <br/><br/>
 */
public class CNCarInfoActivity extends CNParentActivity implements CNViewClickUtil.NoFastClickListener,
        TextWatcher, IPopupItemClickListener, CNDialogActivity.IProviceItemCallback, CNDialogActivity.ICarBrandCallback {
    private static final String TAG = "CNCarInfoActivity";

    private View mView = null;
    private RecyclerView rlTimelineCarsList = null;
    private CNCommonCarsAdapter mAdapter = null;

    private TextView tvLicence = null;

    private EditText etLicence = null;

    private TextView tvCarModel = null;
    private TextView tvCarColor = null;

    private Car mCar = null;
    /**
     * 选择的品牌
     */
    private CarBrand mSelectedCarBrand = null;
    /**
     * 选择的车型
     */
    private CarModel mSelectedCarModel = null;
    /**
     * 选择的颜色
     */
    private CarColor mSelectedCarColor = null;

    private List<CarColor> mCacheCarColors = null;
    private List<CarBrand> mCacheCarBrands = null;
    private List<Car> mCacheCars = null;

    private ArrayList<OnSiteService> mSelectedServices = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mCar = (Car) bundle.get(CNCarWashingLogic.KEY_SELECTED_CAR_INFO);
            mSelectedServices = (ArrayList<OnSiteService>) bundle.get(CNCarWashingLogic.KEY_SERVICE_SELECTED);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_car_info, null);

        rlTimelineCarsList = findView(mView, R.id.rlTimelineCarsList);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rlTimelineCarsList.setLayoutManager(llm);

        mAdapter = new CNCommonCarsAdapter(mContext, null, new OnListItemClickListener() {
            @Override
            public void onClickItem(int position, View v) {
                mCar = mAdapter.getItem(position);
                // 选择的常用车辆
                if (null != mCar) {
                    mApp.getThreadPool().submit(new CNThreadPool.Job<Boolean>() {
                        @Override
                        public Boolean run() {
                            boolean isSupportCarModel = true;
                            CarBrand brand = mCar.getBrand();
                            if (null != mSelectedServices && !mSelectedServices.isEmpty()
                                    && null != brand && brand.getId() != Const.NONE) {
                                OnSiteService service = mSelectedServices.get(Const.NONE);
                                isSupportCarModel = CNCarWashingLogic.isSupportCarModelByServiceId(service.getCityId(), service.getId(), brand.getId());
                            }
                            return isSupportCarModel;
                        }
                    }, new FutureListener<Boolean>() {
                        @Override
                        public void onFutureDone(Future<Boolean> future) {
                            boolean isSupportCarModel = future.get();
                            if (isSupportCarModel) {
                                // 更新界面
                                setCarInfoToUI(mCar);
                            } else {
                                CNCommonManager.makeText(mContext, CNResourcesUtil.getStringResources(R.string.cn_not_support));
                            }
                        }
                    });
                }
            }
        });
        rlTimelineCarsList.setAdapter(mAdapter);

        LinearLayout llLicence = findView(mView, R.id.llLicence);
        tvLicence = findView(mView, R.id.tvLicence);
        ImageView ivLicenceArrow = findView(mView, R.id.ivLicenceArrow);
        etLicence = findView(mView, R.id.etLicence);

        RelativeLayout rlModel = findView(mView, R.id.rlModel);
        tvCarModel = findView(mView, R.id.tvCarModel);
        RelativeLayout rlCarColor = findView(mView, R.id.rlCarColor);
        tvCarColor = findView(mView, R.id.tvCarColor);

        // 更新界面
        setCarInfoToUI(mCar);

        CNViewClickUtil.setNoFastClickListener(llLicence, this);
        CNViewClickUtil.setNoFastClickListener(tvLicence, this);
        CNViewClickUtil.setNoFastClickListener(ivLicenceArrow, this);
        CNViewClickUtil.setNoFastClickListener(rlModel, this);
        CNViewClickUtil.setNoFastClickListener(rlCarColor, this);

        etLicence.addTextChangedListener(this);

        CNProgressDialogUtil.showProgress(mContext);

        loadingLocCommonCarData();
        loadingLocSupportData();
        CNInitializeApiDataUtil.getIns().fetchSupportCarModels(mContext);
    }

    private void loadingLocCommonCarData() {
        // 先从缓存获取
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                mCacheCars = CNCommonCarUtil.getIns().getCars();
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                if (null != mCacheCars && mCacheCars.size() > Const.NONE) {
                    setCommonCars(mCacheCars);
                }
            }
        });
    }

    private void loadingLocSupportData() {
        // 先从缓存获取
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                List<CarBrand> tempCarBrands = CNCarBrandUtil.getIns().getCarBrands();
                // 有品牌时，根据品牌
                if (null != tempCarBrands) {
                    mCacheCarBrands = new ArrayList<>();
                    if (null != mSelectedServices && mSelectedServices.size() > Const.NONE) {
                        OnSiteService service = mSelectedServices.get(Const.NONE);

                        for (int i = Const.NONE; i < tempCarBrands.size(); i++) {
                            CarBrand brand = tempCarBrands.get(i);
                            boolean isSupportCarModel = CNCarWashingLogic.isSupportCarModelByServiceId(service.getCityId(), service.getId(), brand.getId());
                            if (isSupportCarModel) {
                                // 支持车型，把品牌加进去
                                mCacheCarBrands.add(brand);
                                Log.trace(TAG, "支持品牌：" + brand.getName());
                            }
                        }
                    }
                    if (null != mCacheCarBrands) {
                        Log.trace(TAG, "还有" + mCacheCarBrands.size() + "个品牌");
                    }
                }
                mCacheCarColors = CNCarColorUtil.getIns().getCarColors();
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                if (null == mCacheCarColors || mCacheCarColors.size() == Const.NONE
                        || null == mCacheCarBrands || mCacheCarBrands.size() == Const.NONE) {
                    Log.trace(TAG, "没有缓存车辆品牌和颜色数据");
                } else {
                    CNProgressDialogUtil.dismissProgress(mContext);
                }
            }
        });
    }

    private void setCarInfoToUI(Car car) {
        if (null != car) {
            String lecense = car.getLicense().trim();
            if (CNStringUtil.isNotEmpty(lecense)) {
                // 取第一个省
                String pro = lecense.substring(Const.NONE, Const.ONE);
                String num = lecense.substring(Const.ONE, lecense.length());
                tvLicence.setText(pro);
                etLicence.setText(num);
            }
//            CarBrand brand = car.getBrand();
            CarModel model = car.getModel();

            String strCarModel = "";
            if (null != model) {
                strCarModel = model.getName();
            }
            tvCarModel.setText(strCarModel);

            CarColor color = car.getColor();
            if (null != color) {
                if (CNStringUtil.isNotEmpty(color.getName())) {
                    tvCarColor.setText(color.getName());
                } else {
                    tvCarColor.setText("");
                }
            } else {
                tvCarColor.setText("");
            }

            mSelectedCarBrand = car.getBrand();
            mSelectedCarModel = car.getModel();
            mSelectedCarColor = car.getColor();
        }
        etLicence.setSelection(etLicence.getText().length());
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return (isCheckSuccess() && isBack());
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击弹框的回调
     *
     * @param popupType 弹框类型
     * @param position  点击的位置
     * @param v         parent view
     * @param data      data
     */
    @Override
    public void onClickPopupItem(int popupType, int position, View v, BaseInfo data) {
        switch (popupType) {
            case CNCarWashingLogic.DIALOG_COLORS_TYPE:      // 颜色
                CarColor color = (CarColor) data;
                mSelectedCarColor = color;
                tvCarColor.setText(color.getName());
                break;
        }
    }

    @Override
    public void onProviceItem(String provice) {
        // 车牌的前缀
        String prfix = tvLicence.getText().toString();
        if (CNStringUtil.isEmpty(prfix) || !provice.equals(prfix)) {
            prfix = provice;
            tvLicence.setText(prfix);
        }
    }

    @Override
    public void onSelectedCarBrand(CarBrand brand, CarModel carModel) {
        // 品牌 车型
        mSelectedCarBrand = brand;
        mSelectedCarModel = carModel;
        if (null != carModel) {
            tvCarModel.setText(carModel.getName());
        } else {
            if (null != brand) {
                tvCarModel.setText(brand.getName());
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        etLicence.removeTextChangedListener(this); //解除文字改变事件
        etLicence.setText(s.toString().toUpperCase()); //转换
        etLicence.setSelection(s.length()); // 重新设置光标位置
        etLicence.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String strPrefix = tvLicence.getText().toString().trim();
        String strNum = etLicence.getText().toString().trim().toUpperCase();
        if (CNStringUtil.isNotEmpty(strPrefix)) {
            if (CNStringUtil.isNotEmpty(strNum)) {
                String msg = getStringResources(R.string.cn_is_first_letter);
                if (!Character.isLetter(strNum.charAt(Const.NONE))) {
                    CNCommonManager.makeText(mContext, msg);

                    etLicence.removeTextChangedListener(this); //解除文字改变事件
                    etLicence.getText().clear();
                    etLicence.addTextChangedListener(this);
                } else {
                    if (!CNStringUtil.isNumberOrLetter(strNum)) {
                        msg = getStringResources(R.string.cn_licence_type_err);
                        CNCommonManager.makeText(mContext, msg);

                        int indexTemp = Const.NEGATIVE;
                        int strLen = strNum.length();
                        for (int i = Const.NONE; i < strLen; i++) {
                            char c = strNum.charAt(i);
                            if (c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z') {
                                indexTemp = i;
                                break;
                            }
                        }
                        if (indexTemp != Const.NEGATIVE) {
                            String strError = null;
                            if (indexTemp < (strLen - Const.ONE)) {
                                strError = strNum.substring(indexTemp, indexTemp + Const.ONE);
                            } else if (indexTemp == (strLen - Const.ONE)) {
                                strError = strNum.substring(indexTemp);
                            }
                            if (CNStringUtil.isNotEmpty(strError)) {
                                strNum = strNum.replace(strError, "");
                                etLicence.setText(strNum);
                            }
                            if (CNStringUtil.isNotEmpty(strNum)) {
                                etLicence.setSelection(strNum.length());
                            }
                        }
                    }
                }
            } else {
                String msg = getStringResources(R.string.cn_licence_is_not_empty);
                CNCommonManager.makeText(mContext, msg);
            }
        }
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 上门洗车
        navBar.setNavTitle(getStringResources(R.string.cn_car_info));
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
        navBar.setRightBtn(getStringResources(R.string.cn_confirm));
    }

    private void setCommonCars(List<Car> cars) {
        mAdapter.clear();
        rlTimelineCarsList.setAdapter(mAdapter);
        mAdapter.append(cars);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft || viewId == R.id.tvNavRight) {
            if (viewId == R.id.tvNavLeft) {
                if (!isCheckSuccess()) {
                    return false;
                }
            }
            return isBack();
        }
        return true;
    }

    private boolean isCheckSuccess() {
        String licencePro = tvLicence.getText().toString().trim();
        String licenceNum = etLicence.getText().toString().trim();
        String carBrand = tvCarModel.getText().toString().trim();
        String carColor = tvCarColor.getText().toString().trim();

        if (CNStringUtil.isEmpty(licencePro)
                || CNStringUtil.isEmpty(licenceNum)
                || CNStringUtil.isEmpty(carBrand)
                || CNStringUtil.isEmpty(carColor)
                || null == mSelectedCarBrand
                || null == mSelectedCarColor
                || null == mSelectedCarModel) {
            String msg = getStringResources(R.string.cn_no_entry_carinfo);
            DialogManager.onAffirm(mContext, msg, new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    CNViewManager.getIns().pop(CNCarInfoActivity.this);
                }
            });
            return false;
        }
        return true;
    }

    private boolean isBack() {
        String licencePro = tvLicence.getText().toString().trim();
        String licenceNum = etLicence.getText().toString().trim();
        String carBrand = tvCarModel.getText().toString().trim();
        String carColor = tvCarColor.getText().toString().trim();
        // 为空判断
        String msg;
        if (CNStringUtil.isEmpty(licencePro)) {
            CNCommonManager.makeText(mContext, getStringResources(R.string.cn_enter_province));
            CNCarWashingLogic.startProviceDialog(mContext, tvLicence.getText().toString(), this);
            return false;
        } else if (CNStringUtil.isEmpty(licenceNum)) {
            msg = getStringResources(R.string.cn_enter_car_licence);
            CNCommonManager.makeText(mContext, msg);
            return false;
        } else if (licenceNum.length() != Const.CAR_LICENCE_LENGTH) { // 车牌长度验证
            msg = getStringResources(R.string.cn_licence_length_err);
            CNCommonManager.makeText(mContext, msg);
            return false;
        } else if (!CNStringUtil.isNumberOrLetter(licenceNum)) { // 车牌类型验证
            msg = getStringResources(R.string.cn_licence_type_err);
            CNCommonManager.makeText(mContext, msg);
            return false;
        } else if (CNStringUtil.isEmpty(carBrand)
                || null == mSelectedCarBrand
                || null == mSelectedCarModel) {
            msg = getStringResources(R.string.cn_enter_car_model);
            CNCommonManager.makeText(mContext, msg);
            return false;
        } else if (CNStringUtil.isEmpty(carColor)
                || null == mSelectedCarColor) {
            msg = getStringResources(R.string.cn_enter_car_color);
            CNCommonManager.makeText(mContext, msg);
            return false;
        }

        // 选择的车辆
        Car car = new Car();
        car.setLicense(licencePro + licenceNum.toUpperCase());
        car.setBrand(mSelectedCarBrand);
        car.setModel(mSelectedCarModel);
        car.setColor(mSelectedCarColor);

        Intent data = new Intent();
        data.putExtra(CNCarWashingLogic.KEY_SELECTED_CAR_INFO, car);
        setResult(Activity.RESULT_OK, data);
        CNViewManager.getIns().pop(this);
        return true;
    }

    @Override
    public void onNoFastClick(View v) {
        // 点击的时候隐藏键盘
        hideInput(etLicence);
        int vId = v.getId();
        if (vId == R.id.llLicence || vId == R.id.tvLicence || vId == R.id.ivLicenceArrow) {
            CNCarWashingLogic.startProviceDialog(mContext, tvLicence.getText().toString(), this);
        } else if (vId == R.id.rlModel) {
            CNCarWashingLogic.startBrandsDialog(mContext, mCacheCarBrands, mSelectedServices, this);
        } else if (vId == R.id.rlCarColor) {
            CNCarWashingLogic.startColorsDialog(mContext, mCacheCarColors, this);
        }
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_INITIALIZE_COMMOM_CARS_DATA_ACTION);
        filter.addAction(CustomBroadcast.ON_INITIALIZE_DATA_ACTION);
        filter.addAction(CustomBroadcast.ON_INITIALIZE_SUPPORT_CAR_DATA_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        if (null == intent) {
            return;
        }

        String action = intent.getAction();
        /**
         * 常用车辆的广播
         */
        if (CustomBroadcast.ON_INITIALIZE_COMMOM_CARS_DATA_ACTION.equals(action)) {
            loadingLocCommonCarData();
        }
        /**
         * 车品牌的广播
         * 车型
         * 颜色
         */
        else if (CustomBroadcast.ON_INITIALIZE_SUPPORT_CAR_DATA_ACTION.equals(action)) {
            boolean result = intent.getBooleanExtra("result", false);
            if (result) {
                loadingLocSupportData();
            } else {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }
        /**
         * 初始化数据失败的广播
         */
        else if (CustomBroadcast.ON_INITIALIZE_DATA_ACTION.equals(action)) {
            boolean result = intent.getBooleanExtra("result", false);
            if (!result) {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }
    }
}
