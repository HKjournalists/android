package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.bean.LocateServityCityResp;
import com.kplus.car.carwash.bean.OnSiteService;
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
import com.kplus.car.carwash.module.adapter.CNServiceAdapter;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNBDLocation;
import com.kplus.car.carwash.utils.CNCarBrandUtil;
import com.kplus.car.carwash.utils.CNCarColorUtil;
import com.kplus.car.carwash.utils.CNCitiesUtil;
import com.kplus.car.carwash.utils.CNInitializeApiDataUtil;
import com.kplus.car.carwash.utils.CNLocatedCityUtil;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNServicesUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.http.ApiHandler;
import com.kplus.car.carwash.utils.http.HttpRequestHelper;
import com.kplus.car.carwash.widget.CNNavigationBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：服务展示界面，也是洗车新版下单的入口
 * <br/><br/>Created by FU ZHIXUE on 2015/7/28.
 * <br/><br/>
 */
public class CNServicesDisplayActivity extends CNParentActivity implements OnListItemClickListener,
        CNViewClickUtil.NoFastClickListener, CNBDLocation.TLLocationCallbackListener {
    private static final String TAG = "CNServicesDisplayActivity";

    private View mView = null;
    private RecyclerView rlServiceList = null;
    private CNServiceAdapter mAdapter = null;

    private TextView tvTotalServices = null;
    private Button btnNext = null;
    private LinearLayout llWashingTips = null;
    private LinearLayout llWashingEmptyTips = null;

    /**
     * 选择的城市
     */
    private City mSelectedCity = null;
    private AppBridgeUtils mAppBridgeUtils = null;
    private CNBDLocation mLocation = null;
    private boolean isGetLocServiceCity = false;
    /**
     * 标识是否设置定位城市
     * 如果是定位回来的城市去获取数据直接调用接口获取接口数据 isSetLocatedCity = true
     * 如果外面选择的城市和定位的是相同城市，初始化数据回来则重新设置 isSetLocatedCity = false
     * 如果切换城市时，调用初始化数据接口回来，不重新设置 isSetLocatedCity = true
     */
    private boolean isSetLocatedCity = false;

    @Override
    protected void initData() {
        AppBridgeUtils.getIns().onEvent(this, "pageView_wash_ServicesDisplay", "上门洗车服务展示浏览量");

        mAppBridgeUtils = AppBridgeUtils.getIns();
        // 先去取经纬度，如果没有再进行定位
        double latitude = AppBridgeUtils.getIns().getLatitude();
        double longitude = AppBridgeUtils.getIns().getLongitude();

        if (latitude == Const.NEGATIVE ||
                longitude == Const.NEGATIVE) {
            mLocation = new CNBDLocation();
            mLocation.initLocation(mContext);
            mLocation.setLocationCallbackListener(this);
            mLocation.isGetCityName(true);
            mLocation.startLocation();
        } else {
            isGetLocServiceCity = true;
        }

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
        mView = mInflater.inflate(R.layout.cn_services_display_layout, null, false);
        rlServiceList = findView(mView, R.id.rlServiceList);

        tvTotalServices = findView(mView, R.id.tvTotalServices);
        btnNext = findView(mView, R.id.btnNext);
        llWashingTips = findView(mView, R.id.llWashingTips);
        llWashingEmptyTips = findView(mView, R.id.llWashingEmptyTips);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rlServiceList.setLayoutManager(llm);

        mAdapter = new CNServiceAdapter(mContext, null, this);
        rlServiceList.setAdapter(mAdapter);

        CNViewClickUtil.setNoFastClickListener(btnNext, this);

        CNProgressDialogUtil.showProgress(mContext);

        long cityId = mAppBridgeUtils.getSelectedCityId();
        String selectedCity = mAppBridgeUtils.getSelectedCity();

        isSetLocatedCity = false;
        if (isGetLocServiceCity) {
            // 获取外面定位的城市
            double lat = mAppBridgeUtils.getLatitude();
            double lng = mAppBridgeUtils.getLongitude();
            String city = mAppBridgeUtils.getCity();

            if (!selectedCity.equals(city)) {
                isSetLocatedCity = true;
                getLocServiceCity(lng, lat, city);
            }
        }

        loadingLocData(cityId, selectedCity, true);

        // 先把品牌、颜色加载到内存中
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                CNCarBrandUtil.getIns().getCarBrands();
                CNCarColorUtil.getIns().getCarColors();
//                CNCarModelUtil.getIns().get();
//                CNCarModelTagUtil.getIns().get();
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CNInitializeApiDataUtil.getIns().fetchSupportCarModels(mContext);
                    }
                }, 2000);
            }
        });
    }

    private long getCurrentCityId() {
        long cityId = mAppBridgeUtils.getSelectedCityId();
        if (null != mSelectedCity) {
            cityId = mSelectedCity.getId();
        }
        return cityId;
    }

    private String getCurrentCityName() {
        String cityName = mAppBridgeUtils.getSelectedCity();
        if (null != mSelectedCity) {
            cityName = mSelectedCity.getName();
        }
        return cityName;
    }

    /**
     * 读取本地缓存，切换城市时也要调用
     */
    private void loadingLocData(final long cityId, final String cityName, final boolean isFetchInService) {
        // 切换城市把前面选择的清空
        CNCarWashingLogic.clearForMap();

        if (null != mTask && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            mTask.cancel(true);
        }
        mTask = new LoadingTask(cityId, cityName, isFetchInService);
        mTask.execute();
    }

    private LoadingTask mTask = null;

    private class LoadingTask extends AsyncTask<Void, Void, List<OnSiteService>> {
        final long cityId;
        final String cityName;
        final boolean isFetchInService;

        public LoadingTask(final long cityId, final String cityName, final boolean isFetchInService) {
            this.cityId = cityId;
            this.cityName = cityName;
            this.isFetchInService = isFetchInService;
        }

        @Override
        protected List<OnSiteService> doInBackground(Void... params) {
            return CNServicesUtil.getIns().getServices(cityId);
        }

        @Override
        protected void onPostExecute(List<OnSiteService> onSiteServices) {
            setDisplayData(onSiteServices);

            // 如果是第一次进入或切换城市时，先加载本地缓存数据，再从服务器接口获取数据
            if (isFetchInService) {
                CNInitializeApiDataUtil.getIns().initializeApiData(mContext, cityId, cityName);
            } else {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }
    }

    private void loadingSelectedCity() {
        City locatedCity = CNLocatedCityUtil.getIns().getLocatedCity();
        if (null != locatedCity && locatedCity.getId() == mAppBridgeUtils.getSelectedCityId()) {
            setSelectedCity(locatedCity);
        }
    }

    private void setLocatedCity(City locatedCity, long recommendCityId, String cityName) {
        if (recommendCityId != 0) {
            mApp.mRecommendCityId = recommendCityId;
        }
        if (null != locatedCity) {
            mApp.mLocatedCity = locatedCity;
            if (recommendCityId == 0) {
                mApp.mRecommendCityId = locatedCity.getId();
            }
        } else {
            List<City> cities = CNCitiesUtil.getIns().getCities();
            if (null != cities) {
                for (City city : cities) {
                    if (cityName.replace("市", "").equals(city.getName())) {
                        mApp.mLocatedCity = city;
                        if (mApp.mRecommendCityId <= Const.NONE) {
                            mApp.mRecommendCityId = city.getId();
                        }
                        break;
                    }
                }
            }
        }
    }

    private void loadingCities() {
        List<City> cities = CNCitiesUtil.getIns().getCities();

        if (null == cities) {
            return;
        }

        if (null == mApp.mCitys) {
            mApp.mCitys = new ArrayList<>();
        }
        mApp.mCitys.clear();
        mApp.mCitys.addAll(cities);

        // 添加定位到当前位置到列表中显示
        City c = new City();
        c.setName(getStringResources(R.string.cn_my_city));
        c.setId(CNCarWashingLogic.TYPE_MY_LOCATION_ID);

        mApp.mCitys.add(Const.NONE, c);
    }

    /**
     * 获取当前定位的城市
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param city      城市
     */
    private void getLocServiceCity(double longitude, double latitude, final String city) {
        HttpRequestHelper.getLocServiceCityV2(this, longitude, latitude, city, new ApiHandler(this) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                LocateServityCityResp citys = (LocateServityCityResp) baseInfo;
                setLocatedCity(citys.getLocatedCity(), citys.getRecommendCityId(), city);
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                }
            }
        });
    }

    private void setDisplayData(List<OnSiteService> services) {
        mAdapter.clear();
        if (null != services && services.size() > Const.NONE) {
            setBtnEnabled(true);
            setCityIsNotWashing(false);
            mAdapter.appendData(services);
            mAdapter.notifyDataSetChanged();
            setSelectedItemCount();
        } else {
            setBtnEnabled(false);
            rlServiceList.setVisibility(View.GONE);
            llWashingTips.setVisibility(View.GONE);
            llWashingEmptyTips.setVisibility(View.VISIBLE);
            tvTotalServices.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }
    }

    private void setCityIsNotWashing(boolean isNotWashing) {
        if (!isNotWashing) {
            rlServiceList.setVisibility(View.VISIBLE);
            llWashingTips.setVisibility(View.GONE);
            llWashingEmptyTips.setVisibility(View.GONE);

            tvTotalServices.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        } else {
            rlServiceList.setVisibility(View.GONE);
            llWashingTips.setVisibility(View.VISIBLE);
            llWashingEmptyTips.setVisibility(View.GONE);

            tvTotalServices.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
        loadingSelectedCity();
        loadingCities();
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 选择服务
        navBar.setNavTitle(getStringResources(R.string.cn_select_service));
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
        // 位置
        navBar.setRightBtn(mAppBridgeUtils.getSelectedCity(), R.drawable.btn_icon_location_selector, Const.NONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mLocation) {
            mLocation.stopLocation();
            mLocation = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mLocation) {
            mLocation.stopLocation();
            mLocation = null;
        }
        mApp.release();
        CNCarWashingLogic.clearForMap();
        CNProgressDialogUtil.dismissProgress(this);
        super.onDestroy();
    }

    private void setSelectedCity(City city) {
        if (null != city && city.getId() != Const.NONE) {
            mSelectedCity = city;
            mApp.mSelectedCity = mSelectedCity;
            mNavigationBar.setRightBtn(mSelectedCity.getName(), R.drawable.btn_icon_location_selector, Const.NONE);
        }
    }

    @Override
    public void onNoFastClick(View v) {
        // library 中不能用 switch找控件
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft) {
            CNViewManager.getIns().pop(this);
        } else if (viewId == R.id.tvNavRight) {
            CNCarWashingLogic.startCitiesDialog(mContext, mPopupItemClickListener);
        }
        return true;
    }

    private void setSelectedItemCount() {
        String text = mContext.getResources().getString(R.string.cn_selected_services);
        text = String.format(text, CNCarWashingLogic.getServiceSize());
        tvTotalServices.setText(text);
    }

    private void setBtnEnabled(boolean isEnabled) {
        btnNext.setEnabled(isEnabled);
        if (isEnabled) {
            btnNext.setBackgroundResource(R.drawable.btn_pay_selector);
        } else {
            btnNext.setBackgroundResource(R.drawable.btn_pay_dis_selector);
        }
    }

    private IPopupItemClickListener mPopupItemClickListener = new IPopupItemClickListener() {
        @Override
        public void onClickPopupItem(int popupType, int position, View v, BaseInfo data) {
            switch (popupType) {
                case CNCarWashingLogic.DIALOG_CITIES_TYPE:
                    if (v.getId() == R.id.rlCityItem) {
                        isSetLocatedCity = true;
                        City city = (City) data;
                        setBtnEnabled(true);
                        // 定位当前位置
                        if (city.getId() == CNCarWashingLogic.TYPE_MY_LOCATION_ID) {
                            city = mApp.mLocatedCity;
                        }
                        if (null != city) {
                            boolean isRequest = true;
                            // 如果选择的当前城市是一样的，不要再次请求
                            if (null != mSelectedCity && city.getId() == mSelectedCity.getId()) {
                                isRequest = false;
                            }
                            if (isRequest) {
                                CNProgressDialogUtil.showProgress(mContext);
                                // 设置选择的城市
                                setSelectedCity(city);
                                loadingLocData(city.getId(), city.getName(), true);
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onSuccess(double latitude, double longitude, final String city) {
        Log.trace(TAG, "GPS获取成功：latitude：" + latitude + "，longitude：" + longitude + "，city：" + city);
        isSetLocatedCity = false;
        String selectedCity = mAppBridgeUtils.getSelectedCity();
        if (!selectedCity.equals(city)) {
            isSetLocatedCity = true;
            getLocServiceCity(longitude, latitude, city);
        }
        // 定位的城市id为0
        long cityId = Const.NONE;
        CNInitializeApiDataUtil.getIns().initializeApiData(mContext, cityId, city);
    }

    @Override
    public void onFailed(String errorMsg) {
        if (Const.IS_DEBUG) {
            CNCommonManager.makeText(mContext, "GPS获取失败：" + errorMsg);
        }
        Log.trace(TAG, "GPS获取失败：" + errorMsg);
        // 定位失败默认杭州
        isSetLocatedCity = false;
        // 定位的城市id为0
        long cityId = Const.NONE;
        CNInitializeApiDataUtil.getIns().initializeApiData(mContext, cityId, "杭州");
    }

    /**
     * 点击item
     */
    @Override
    public void onClickItem(int position, View v) {
        int vId = v.getId();
        if (vId == R.id.btnServiceDetails) { // 查看详情
            OnSiteService service = mAdapter.getItem(position);
            Map<String, String> map = new HashMap<>();
            map.put("service_name", service.getName());
            mAppBridgeUtils.onEvent(mContext, "look_washCar", "洗车项目查看", map);

            Intent intent = new Intent(mContext, CNServiceDetailsActivity.class);
            intent.putExtra(CNCarWashingLogic.KEY_REVIEW_SERVICE_DETAILS, service);
            CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_REVIEW_SERVICE_DETAILS);
        } else if (vId == R.id.rlService) {
            mAppBridgeUtils.onEvent(mContext, "click_next_washCar", "选择服务，下一步");
            // 点击item选择
            OnSiteService siteService = mAdapter.getItem(position);
            setServiceResult(siteService);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (AppBridgeUtils.getIns().isFromInApp()) {
                Intent it = new Intent("finish");
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);
            }
            CNViewManager.getIns().pop(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setServiceResult(OnSiteService service) {
        if (null != service) {
            ArrayList<OnSiteService> services = new ArrayList<>();
            services.add(service);

            // 跳转到下单界面
            Intent data = new Intent(mContext, CNCarWashingActivity.class);
            // 选择的服务
            data.putExtra(CNCarWashingLogic.KEY_SERVICE_SELECTED, services);
            CNViewManager.getIns().showActivity(mContext, data, CNCarWashingLogic.TYPE_SELECTED_SERVICE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data)
            return;

        switch (requestCode) {
            case CNCarWashingLogic.TYPE_REVIEW_SERVICE_DETAILS: // 从详情中点击下单回来要更新选择状态
                if (resultCode == Activity.RESULT_OK) {
                    OnSiteService service = null;
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        service = (OnSiteService) bundle.get(CNCarWashingLogic.KEY_REVIEW_SERVICE_DETAILS);
                    }
                    setServiceResult(service);
                }
                break;
        }
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_CITIES_DATA_ACTION);
        filter.addAction(CustomBroadcast.ON_INITIALIZE_DATA_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        if (null == intent) {
            return;
        }

        String action = intent.getAction();
        if (CustomBroadcast.ON_CITIES_DATA_ACTION.equals(action)) {
            loadingCities();
        }
        /**
         * 初始化数据的广播
         */
        else if (CustomBroadcast.ON_INITIALIZE_DATA_ACTION.equals(action)) {
            boolean result = intent.getBooleanExtra("result", false);
            if (result) {
                // 取城市
                if (!isSetLocatedCity) {
                    City locatedCity = CNLocatedCityUtil.getIns().getLocatedCity();
                    long recoCityId = CNLocatedCityUtil.getIns().getRecommendCityId();

                    setLocatedCity(locatedCity, recoCityId, mAppBridgeUtils.getSelectedCity());
                }
                // 设置选择的城市
                loadingSelectedCity();
                // 先调用初始化接口，回来后再调用消费习惯接口，回来再显示数据
                long cityId = getCurrentCityId();
                String cityName = getCurrentCityName();
                // 获取到数据从本地列表
                loadingLocData(cityId, cityName, false);
            } else {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }
    }
}
