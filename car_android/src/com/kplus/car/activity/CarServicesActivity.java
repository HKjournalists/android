package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.adapter.GLExpandableAdapter;
import com.kplus.car.asynctask.CarServicesTask;
import com.kplus.car.carwash.utils.CNNumberUtils;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.fragment.AdvertFragment;
import com.kplus.car.model.Advert;
import com.kplus.car.model.CarService;
import com.kplus.car.model.CarServiceGroup;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.ServiceImgList;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.json.AdvertJson;
import com.kplus.car.model.response.CarServicesResponse;
import com.kplus.car.model.response.GetAdvertDataResponse;
import com.kplus.car.model.response.GetServiceImgListResponse;
import com.kplus.car.model.response.request.GetAdvertDataRequest;
import com.kplus.car.model.response.request.GetServiceImgListRequest;
import com.kplus.car.util.BroadcastReceiverUtil;
import com.kplus.car.util.CarServicesUtil;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.AbnormalView;
import com.kplus.car.widget.CarServicesTopView;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshBase;
import com.kplus.car.widget.pulltorefresh.library.PullToRefreshScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Description：汽车服务界面
 * <br/><br/>Created by FU ZHIXUE on 2015/8/10.
 * <br/><br/>
 */
public class CarServicesActivity extends BaseActivity implements ClickUtils.NoFastClickListener,
        CarServicesTopView.OnItemClickListener, AbnormalView.IAbormalViewCallback {

    private static final String TAG = "CarServicesActivity";

    private Context mContext = null;

    private LinearLayout llLocation = null;
    private TextView tvLocation = null;

    /**
     * 异常情况显示的界面
     */
    private AbnormalView llError = null;
    private PopupWindow mPopupWindow;
    private PopupWindow mHintPopup;
    private PullToRefreshScrollView prsScrollView = null;
    private LinearLayout llAdvert = null;
    private CarServicesTopView llServices = null;
    private ExpandableListView elvList = null;
    /**
     * 是否有缓存数据
     */
    private boolean hasLocalCacheData = false;
    /**
     * 标识第一次进入时定时要转菊花
     */
    private boolean isFristLoc = false;
    private boolean isAddVehicle = false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    List<UserVehicle> list = mApplication.dbCache.getVehicles();
                    if ((list == null || list.size() == 0) && mApplication.getHomeAdvert() == null && mApplication.getNewUserAdvert() == null){
                        showAddVehiclePopup();
                    }
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        mContext = this;

        setContentView(R.layout.activity_car_services);

        llLocation = findView(R.id.llLocation);
        tvLocation = findView(R.id.tvLocation);
        TextView tvTitle = findView(R.id.tvTitle);

        prsScrollView = findView(R.id.svScrollView);

        llError = findView(R.id.llError);

        llAdvert = findView(R.id.llAdvert);
        llServices = findView(R.id.llServices);
        elvList = findView(R.id.elvList);

        tvTitle.setText(getResources().getString(R.string.car_services_title));

        prsScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        prsScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
        prsScrollView.getLoadingLayoutProxy().setRefreshingLabel("加载中...");
        prsScrollView.getLoadingLayoutProxy().setReleaseLabel("释放更新");
        prsScrollView.getLoadingLayoutProxy().setRefreshingLabelIm("刷新成功");
        prsScrollView.getLoadingLayoutProxy().setmEndLabel("刷新成功");

        mServiceAdapter = new GLExpandableAdapter(mContext, null, null);
        elvList.setAdapter(mServiceAdapter);
        elvList.setGroupIndicator(null);

        prsScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                fetchCarServices(getSelectCityId());
            }
        });

        elvList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                CarServiceGroup group = mServiceAdapter.getGroup(groupPosition);

                // 是否显示分组名称
                boolean showName = (null == group.getShowName() ? false : group.getShowName());
                // 首页展示数 NOTICE 0 表示不限，全部显示
                int indexServiceCount = (null == group.getIndexServiceCount() ? 0 : group.getIndexServiceCount());
                // 包含服务数
                int serviceCount = (null == group.getServiceCount() ? 0 : group.getServiceCount());

                // 不显示分组名称时，要全部加载数据
                boolean isOnClick = showName && !(indexServiceCount == 0 || serviceCount <= indexServiceCount);

                if (isOnClick) {
                    Intent intent = new Intent(mContext, CarMoreServiceActivity.class);
                    intent.putExtra(CarServicesUtil.KEY_PARAM_VALUE, group);
                    mContext.startActivity(intent);
                }
                return true;
            }
        });
        elvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CarService carService = mServiceAdapter.getChild(groupPosition, childPosition);
                // 点击服务
                CarServicesUtil.onClickCarServiceItem(mContext, carService);
                return true;
            }
        });
        prsScrollView.getRefreshableView().smoothScrollTo(0, 0);
    }

    private <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(llLocation, this);
        llError.setAbormalViewCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CNProgressDialogUtil.dismissProgress(mContext);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationChangeReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(vehicleSyncReceiver);
        super.onDestroy();
    }

    @Override
    protected void loadData() {
        LocalBroadcastManager.getInstance(this).registerReceiver(locationChangeReceiver, new IntentFilter("com.kplus.car.location.changed"));
        LocalBroadcastManager.getInstance(this).registerReceiver(vehicleSyncReceiver, new IntentFilter(KplusConstants.ACTION_GET_SYN));
        setCityAndFetchServices();
    }

    /**
     * 从本地缓存中加载数据
     */
    private void loadingLocData() {
        List<CarServiceGroup> groups = mApplication.dbCache.getCarServices(getSelectCityId());
        if (null == groups || groups.isEmpty()) {
            hasLocalCacheData = false;
        } else {
            hasLocalCacheData = true;
            fillData(groups);
        }
    }

    private List<CarServiceGroup> mGroups = null;
    private List<List<CarService>> mChildServices = null;
    private GLExpandableAdapter mServiceAdapter = null;

    /**
     * 填充数据
     *
     * @param groups 分组数据
     */
    private void fillData(List<CarServiceGroup> groups) {
        if (null == groups || groups.isEmpty()) {
            return;
        }
        if (null == mGroups) {
            mGroups = new ArrayList<>();
        } else {
            mGroups.clear();
        }
        if (null == mChildServices) {
            mChildServices = new ArrayList<>();
        } else {
            mChildServices.clear();
        }
        setCarServicesIsError(false);
        // 填充数据
        for (int i = 0; i < groups.size(); i++) {
            CarServiceGroup serviceGroup = groups.get(i);
            boolean isSystem = (null == serviceGroup.getSystem() ? false : serviceGroup.getSystem());
            if (isSystem) {
                // 填充快捷入口服务
                llServices.setCellData(serviceGroup.getServices(), this);
            } else {
                if (null != serviceGroup.getServices() && serviceGroup.getServices().size() > 0) {
                    mGroups.add(serviceGroup);
                    mChildServices.add(filterCarServices(serviceGroup));
                }
            }
        }
        // 填充列表服务
        setCarServices();
    }

    private List<CarService> filterCarServices(CarServiceGroup serviceGroup) {
        // 是否显示分组名称
        boolean showName = (null == serviceGroup.getShowName() ? false : serviceGroup.getShowName());
        // 首页展示数 NOTICE 0 表示不限
        int indexServiceCount = (null == serviceGroup.getIndexServiceCount() ? 0 : serviceGroup.getIndexServiceCount());
        // 包含服务数
        int serviceCount = (null == serviceGroup.getServiceCount() ? 0 : serviceGroup.getServiceCount());
        // 服务列表
        List<CarService> carAllServices = serviceGroup.getServices();

        List<CarService> carServices = new ArrayList<>();
        if (!showName) {
            // 不显示分组名称时，要全部加载数据
            carServices.addAll(carAllServices);
        } else {
            // 0 表示不限，全部显示
            if (indexServiceCount == 0 || serviceCount <= indexServiceCount) {
                // 如果包含服务数<=首页展示数，就全部显示，不显示可点击的详情列表
                carServices.addAll(carAllServices);
            } else {
                // 只取出首页展示数量
                for (int i = 0; i < carAllServices.size(); i++) {
                    if (i >= indexServiceCount) {
                        break;
                    }
                    CarService carService = carAllServices.get(i);
                    carServices.add(carService);
                }
            }
        }
        return carServices;
    }

    private void setCarServices() {
        mServiceAdapter.clear();
        mServiceAdapter.appendGroups(mGroups);
        mServiceAdapter.appendChild(mChildServices);
        for (int i = 0; i < mServiceAdapter.getGroupCount(); i++) {
            elvList.expandGroup(i);
        }
        prsScrollView.getRefreshableView().smoothScrollTo(0, 0);
    }

    private void setCarServiceImgs(List<ServiceImgList> groupList){
        mServiceAdapter.setCarServiceImgs(groupList);
    }

    private long getSelectCityId() {
        return CNNumberUtils.stringToLong(mApplication.getCityId());
    }

    /**
     * 设置城市并获取服务
     */
    private void setCityAndFetchServices() {
        String cityName = mApplication.getCityName();
        if (StringUtils.isEmpty(cityName)) {
            isFristLoc = true;
            tvLocation.setText("请选择");
            setCarServicesIsError(true);
            llError.setAbnormalContent(AbnormalView.ERROR_POSITIONING);
        } else if (!TextUtils.isEmpty(cityName) && !tvLocation.getText().equals(cityName)) {
            isFristLoc = false;
            tvLocation.setText(cityName);
            request();
        }
    }

    private void request() {
        // 重新设置城市后获取广告
        getAdvertData(KplusConstants.ADVERT_SERVICE_HEAD_NEW);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 从本地缓存加载数据
                loadingLocData();
                if (!hasLocalCacheData) {
                    CNProgressDialogUtil.showProgress(mContext, "正在加载，请稍候...");
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchCarServices(getSelectCityId());
                        }
                    }, 100);
                } else {
                    fetchCarServices(getSelectCityId());
                }
            }
        }, 5);
    }

    private void setCarServicesIsError(boolean isError) {
        if (!isError) {
            prsScrollView.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
        } else {
            prsScrollView.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取洗车服务
     *
     * @param cityId 城市id
     */
    private void fetchCarServices(final long cityId) {
        if (cityId != 0) {
            new CarServicesTask(mApplication) {
                @Override
                protected void onPostExecute(CarServicesResponse response) {
                    CNProgressDialogUtil.dismissProgress(mContext);
                    if (null != prsScrollView && prsScrollView.isRefreshing()) {
                        prsScrollView.onRefreshComplete();
                    }

                    if (null != response && null != response.getCode() && response.getCode() == 0) {
                        Log.trace(TAG, "获取到数据-->" + response.getBody());

                        List<CarServiceGroup> groups = response.getData().getServiceGroups();
                        long nCityId = (null == response.getData().getCityId() ? getSelectCityId() : response.getData().getCityId());
                        mApplication.dbCache.saveCarService(groups, nCityId);
                        hasLocalCacheData = true;
                        // 填充数据
                        fillData(groups);
                        new AsyncTask<Void, Void, GetServiceImgListResponse>(){
                            @Override
                            protected GetServiceImgListResponse doInBackground(Void... params) {
                                GetServiceImgListRequest request = new GetServiceImgListRequest();
                                request.setParams(cityId);
                                try {
                                    return mApplication.client.execute(request);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(GetServiceImgListResponse response) {
                                if (response != null && response.getCode() != null && response.getCode() == 0){
                                    List<ServiceImgList> groupList = response.getData().getGroupList();
                                    setCarServiceImgs(groupList);
                                }
                            }
                        }.execute();
                        //添加车辆引导
                        if ("1".equals(mApplication.dbCache.getValue(KplusConstants.DB_KEY_NEVER_REMIND)))
                            return;
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                        if (date.equals(mApplication.dbCache.getValue(KplusConstants.DB_KEY_NEXT_TIME)))
                            return;
                        mHandler.sendEmptyMessageDelayed(1, 2000);
                    } else {
                        // 请求失败，没有缓存数据时要显示异常情况
                        if (!hasLocalCacheData) {
                            setCarServicesIsError(true);
                            llError.setAbnormalContent(AbnormalView.ERROR_REQUEST_FAILED);
                        }
                    }
                }
            }.execute(cityId);
        }
    }

    /**
     * 获取广告
     *
     * @param adType 类型
     */
    private void getAdvertData(final String adType) {
        new AsyncTask<Void, Void, GetAdvertDataResponse>() {
            @Override
            protected GetAdvertDataResponse doInBackground(Void... params) {
                GetAdvertDataRequest request = new GetAdvertDataRequest();
                if (!StringUtils.isEmpty(mApplication.getCityId())) {
                    try {
                        long cityId = CNNumberUtils.stringToLong(mApplication.getCityId());
                        request.setParams(cityId, mApplication.getUserId(), mApplication.getId(), adType);
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
                if (null != response && null != response.getCode() && response.getCode() == 0) {
                    AdvertJson data = response.getData();
                    if (null != data) {
                        if (adType.equals(KplusConstants.ADVERT_SERVICE_HEAD_NEW)) {
                            List<Advert> adverts = data.getServiceHeadNew();
                            setAdverts(adverts);
                        }
                    } else {
                        setAdverts(null);
                    }
                } else {
                    setAdverts(null);
                }
            }
        }.execute();
    }

    /**
     * 设置广告
     *
     * @param adverts 广告
     */
    private void setAdverts(List<Advert> adverts) {
        llAdvert.removeAllViews();
        if (null != adverts && !adverts.isEmpty()) {
            llAdvert.setVisibility(View.VISIBLE);
            mApplication.setServiceHeadAdvert(adverts);
            if (!isFinishing()) {
                AdvertFragment adf = AdvertFragment.newInstance(KplusConstants.ADVERT_SERVICE_HEAD_NEW);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_top, 0)
                        .add(R.id.llAdvert, adf)
                        .commitAllowingStateLoss();
            }
        } else {
            llAdvert.setVisibility(View.GONE);
        }
    }

    /**
     * 点击上面8个服务的回调
     *
     * @param carService 点击的服务
     */
    @Override
    public void onCellItemClick(CarService carService) {
        CarServicesUtil.onClickCarServiceItem(this, carService);
    }

    @Override
    public void onLocationFailed() {
        // 打开城市选择界面
        chooseCity();
    }

    @Override
    public void onRequestFailed() {
        // 重新请求
        request();
    }

    @Override
    public void onNoSupportCity() {
        // 打开城市选择界面
        chooseCity();
    }

    @Override
    public void onNoNetwork() {
        // 重新请求
        request();
    }

    @Override
    public void onPositioning() {
        // 打开城市选择界面
        chooseCity();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.llLocation: // 点击城市
                chooseCity();
                break;
            case R.id.never_remind:
                mPopupWindow.dismiss();
                mApplication.dbCache.putValue(KplusConstants.DB_KEY_NEVER_REMIND, "1");
                View layout = LayoutInflater.from(this).inflate(R.layout.popup_image_bottom, null, false);
                mHintPopup = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mHintPopup.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
                ClickUtils.setNoFastClickListener(layout.findViewById(R.id.popup_img), this);
                break;
            case R.id.next_time:
                mPopupWindow.dismiss();
                mApplication.dbCache.putValue(KplusConstants.DB_KEY_NEXT_TIME, new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
                break;
            case R.id.add_vehicle:
                mPopupWindow.dismiss();
                mApplication.dbCache.putValue(KplusConstants.DB_KEY_NEXT_TIME, new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
                if(mApplication.getId() == 0){
                    ToastUtil.TextToast(this, "添加车辆需要绑定手机号", Toast.LENGTH_SHORT, Gravity.CENTER);
                    Intent intent = new Intent(this, PhoneRegistActivity.class);
                    intent.putExtra("isMustPhone", true);
                    startActivityForResult(intent, Constants.REQUEST_TYPE_ADD);
                }
                else {
                    Intent intent = new Intent(this, VehicleAddNewActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_TYPE_VEHICLE);
                }
                break;
            case R.id.popup_img:
                mHintPopup.dismiss();
                break;
        }
    }

    private void chooseCity() {
        Intent intent = new Intent(this, CitySelectActivity.class);
        intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_HOME);
        intent.putExtra("cityName", mApplication.getCityName());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, Constants.REQUEST_TYPE_CITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_TYPE_CITY) {
            if (resultCode == RESULT_OK) {
                ArrayList<CityVehicle> listTemp = data.getParcelableArrayListExtra("selectedCity");
                if (null != listTemp && !listTemp.isEmpty()) {
                    CityVehicle cv = listTemp.get(0);
                    String cityName = cv.getName();
                    if (!TextUtils.isEmpty(cityName)) {
                        mApplication.setCityName(cityName);
                        setCityAndFetchServices();
                    }
                    if (null != cv.getProvince()) {
                        mApplication.setProvince(cv.getProvince());
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_TYPE_SWITCH_CITY) {
            if (resultCode == RESULT_OK) {
                setCityAndFetchServices();
            }
        } else if (requestCode == Constants.REQUEST_TYPE_LOGIN) {
            if (resultCode == RESULT_OK) {
                CarServicesUtil.onLoginResult(mContext);
            }
        } else if (requestCode == Constants.REQUEST_TYPE_ADD) {
            if (resultCode == RESULT_OK) {
                isAddVehicle = true;
            }
        } else if (requestCode == Constants.REQUEST_TYPE_VEHICLE) {
            if (resultCode == Constants.RESULT_TYPE_ADDED) {
                Intent intent = new Intent(BroadcastReceiverUtil.ACTION_CHANGE_TAB);
                intent.putExtra("currentTab", 1);
                LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    private BroadcastReceiver locationChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFristLoc) {
                isFristLoc = false;
            }
            if (!StringUtils.isEmpty(mApplication.getCityName())) {
                // 定位成功
                setCityAndFetchServices();
            } else {
                if (!hasLocalCacheData) {
                    // 如果没有城市缓存数据，设置定位失败
                    setCarServicesIsError(true);
                    if (null != llError) {
                        llError.setAbnormalContent(AbnormalView.ERROR_LOCATION_FAILED);
                    }
                }
            }
        }
    };

    private BroadcastReceiver vehicleSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAddVehicle){
                isAddVehicle = false;
                List<UserVehicle> list = mApplication.dbCache.getVehicles();
                if ((list == null || list.size() == 0)){
                    Intent it = new Intent(CarServicesActivity.this, VehicleAddNewActivity.class);
                    startActivityForResult(it, Constants.REQUEST_TYPE_VEHICLE);
                }
                else {
                    Intent it = new Intent(BroadcastReceiverUtil.ACTION_CHANGE_TAB);
                    intent.putExtra("currentTab", 1);
                    LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(it);
                }
            }
        }
    };

    private void showAddVehiclePopup(){
        View layout = LayoutInflater.from(this).inflate(R.layout.popup_add_vehicle_guide, null, false);
        mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.never_remind), this);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.next_time), this);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.add_vehicle), this);
    }
}
