package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.Address;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.bean.PoiLocationRoad;
import com.kplus.car.carwash.bean.Position;
import com.kplus.car.carwash.bean.Region;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.callback.IPopupItemClickListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.manager.DialogManager;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.adapter.CNSearchKeyAdapter;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNInitializeApiDataUtil;
import com.kplus.car.carwash.utils.CNNumberUtils;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNRegionUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.widget.CNNavigationBar;
import com.kplus.car.carwash.widget.CNServiceScopeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择位置界面
 * Created by Fu on 2015/5/14.
 */
public class CNCarLocationActivity extends CNParentActivity
        implements CNViewClickUtil.NoFastClickListener,
        IPopupItemClickListener, TextWatcher, BDLocationListener, BaiduMap.OnMarkerClickListener,
        BaiduMap.OnMapStatusChangeListener, OnGetPoiSearchResultListener,
        OnGetGeoCoderResultListener {

    private static final String TAG = "CNCarLocationActivity";

    /**
     * 选择的位置
     */
    private Position mSelectedPosition = null;
    /**
     * 选择的地址
     */
    private Address mSelectedAddress = null;
    /**
     * 当前城市位置
     */
    private City mLocatedCity = null;
    /**
     * 当前选择的区域
     */
    private Region mSelectedRegion = null;

    private View mView = null;

    /**
     * 搜索关键字输入窗口
     */
    private AutoCompleteTextView acSearchKey = null;
    private ArrayAdapter<PoiLocationRoad> sugAdapter = null;
    private List<PoiLocationRoad> mSearchKeys = new ArrayList<>();

    private ImageView ivClear = null;
    private LinearLayout llRoadRangeTips = null;
    private TextView tvRoadRangeTips = null;
    private MapView mMapView = null;
    private CNServiceScopeView mServiceArearView = null;

    private BaiduMap mBaiduMap = null;
    private MapStatus mMapStatus = null;
    private Overlay mCurrentLocatlay = null;
    private LatLng mCurrentLatLng = null;
    private LatLng mFirstCurrentMoveLatlng = null;
    private LatLng mCurrentMoveLatLng = null;
    // 当前选择的城市名
    private String mCity = "";
    private List<LatLng> mScope1Pts = null;

    // 定位相关
    private LocationClient mLocClient = null;
    private BitmapDescriptor mCurrentMarker = null;
    private boolean isFirstLoc = true;// 是否首次定位

    // poi
    private PoiSearch mPoiSearch = null;
    private PoiSearch mSuggestPoiSearch = null;

    // 地理编码
    private GeoCoder mGeoCoder = null;
    private GeoCoder mCityGeoCoder = null;

    /**
     * 是否在搜索位置的时候触发的
     */
    private boolean isSearchLocation = false;
    /**
     * 是否在改变地图状态的时候触发的
     */
    private boolean isMapStatusChange = false;
    /**
     * 是否在定位到自己的位置的时候触发的
     */
    private boolean isMyLocationGeoCoder = false;

    /**
     * 是否在服务范围内
     */
    private boolean isInServiceScope = true;

    private TextView tvAddressTips = null;

    private City mSelectedCity = null;

    private PoiLocationRoad mSearchKey = null;

    private boolean isFirstLoadingData = true;
    private List<Region> mRegionsArea = null;
    /**
     * 所有范围的点
     */
    private Map<Region, List<LatLng>> mAllAreas = null;
    /**
     * 搜索出来的地址数据
     */
    private List<BaseInfo> mPoiLocationRoads = null;

    @Override
    protected void initData() {
        SDKInitializer.initialize(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSelectedPosition = (Position) bundle.get(CNCarWashingLogic.KEY_SREVING_LOCATION);
            if (null != mSelectedPosition) {
                mCurrentMoveLatLng = new LatLng(mSelectedPosition.getLatitude(), mSelectedPosition.getLongitude());
                mFirstCurrentMoveLatlng = new LatLng(mCurrentMoveLatLng.latitude, mCurrentMoveLatLng.longitude);
            }
        }

        mLocatedCity = mApp.mLocatedCity;
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_car_location_layout, null);

        acSearchKey = findView(mView, R.id.acSearchKey);
        ivClear = findView(mView, R.id.ivClear);
        llRoadRangeTips = findView(mView, R.id.llRoadRangeTips);
        tvRoadRangeTips = findView(mView, R.id.tvRoadRangeTips);
        mMapView = findView(mView, R.id.bmapView);
        TextView tvLocationCity = findView(mView, R.id.tvLocationCity);
        mServiceArearView = findView(mView, R.id.serviceArearView);

        FrameLayout flMarkerItemView = findView(mView, R.id.flMarkerItemView);
        tvAddressTips = findView(mView, R.id.tvAddressTips);
        ImageView ivCarMarker = findView(mView, R.id.ivCarMarker);

        CNViewClickUtil.setNoFastClickListener(flMarkerItemView, this);
        CNViewClickUtil.setNoFastClickListener(tvAddressTips, this);
        CNViewClickUtil.setNoFastClickListener(ivCarMarker, this);

        sugAdapter = new CNSearchKeyAdapter(mContext, R.layout.cn_search_key_layout, mSearchKeys);
        acSearchKey.setAdapter(sugAdapter);
        acSearchKey.setDropDownWidth((int) (mDeviceSizePoint.x * 0.99f));
        acSearchKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isMapStatusChange = false;
                hideInput(acSearchKey);
                mSearchKey = sugAdapter.getItem(position);

                String keyword = mSearchKey.mName;
                acSearchKey.setText(keyword);
                acSearchKey.setSelection(keyword.length());

                LatLng latLng = new LatLng(mSearchKey.mLatitude, mSearchKey.mLongitude);
                searchGeoCoder(latLng);
            }
        });
        acSearchKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (null != event && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        || actionId == EditorInfo.IME_ACTION_DONE) {
                    // 隐藏键盘
                    hideInput(acSearchKey);
                    return true;
                }
                return false;
            }
        });

        initMap();
        initPOISearch();
        initGeoCoder();

        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);

        acSearchKey.addTextChangedListener(this);
        CNViewClickUtil.setNoFastClickListener(acSearchKey, this);
        CNViewClickUtil.setNoFastClickListener(ivClear, this);
        CNViewClickUtil.setNoFastClickListener(tvLocationCity, this);

        mSelectedCity = mApp.mSelectedCity;
        mCity = mApp.mSelectedCity.getName();
        tvLocationCity.setText(mCity);

        CNProgressDialogUtil.showProgress(this);
        loadingLocCityConfig(mSelectedCity.getId());
        CNInitializeApiDataUtil.getIns().fetchCityRegions(mContext, mSelectedCity.getId());
    }

    /**
     * 获取服务配置
     *
     * @param cityId 城市id
     */
    private void loadingLocCityConfig(final long cityId) {
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                mRegionsArea = CNRegionUtil.getIns().getRegions(cityId);
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                mServiceArearView.initView(mRegionsArea);
                mServiceArearView.setOnServiceAreasChangedListener(mOnServiceAreasChangedListener);

                setRoadRangeTips("");
                setAreas();

                if (null != mRegionsArea && !mRegionsArea.isEmpty()) {
                    CNProgressDialogUtil.dismissProgress(mContext);
                }
            }
        });
    }

    /**
     * 如果是第一次进入时，要在所有范围内去找当时的位置是事在范围内
     */
    private void setAreas() {
        // 用于记住当前位置所在的范围点
        List<LatLng> tempAreasPoint = null;
        if (isFirstLoadingData && null != mRegionsArea) {
            isFirstLoadingData = false;
            boolean isInAreas = false;
            List<LatLng> areasPoint;
            List<LatLng> tempFristAreasPoint = null;
            Region tempFristArea = null;
            if (null == mAllAreas) {
                mAllAreas = new HashMap<>();
            }

            for (int k = Const.NONE; k < mRegionsArea.size(); k++) {
                Region area = mRegionsArea.get(k);

                if (CNStringUtil.isNotEmpty(area.getPoints())) {
                    // 解析范围的坐标
                    areasPoint = new ArrayList<>();
                    String[] arrLatlngs = area.getPoints().split(";");
                    for (String arrLatlng1 : arrLatlngs) {
                        String[] arrLatlng = arrLatlng1.split(",");
                        if (arrLatlng.length == 2) {
                            String strLng = arrLatlng[0];
                            String strLat = arrLatlng[1];
                            double lng = CNNumberUtils.stringToDouble(strLng);
                            double lat = CNNumberUtils.stringToDouble(strLat);
                            LatLng latLng = new LatLng(lat, lng);
                            areasPoint.add(latLng);
                        }
                    }

                    // 把所有服务范围的点都放到内存中
                    mAllAreas.put(area, areasPoint);

                    //构建用户绘制多边形的Option对象
                    OverlayOptions polygonOption = new PolygonOptions()
                            .points(areasPoint)
                            .stroke(new Stroke(5, getColorResources(R.color.cn_area_line))) // 画线的颜色
                            .fillColor(getColorResources(R.color.cn_transparent)); // 要用透明色填充才能看到地图
                    //在地图上添加多边形Option，用于显示
                    if (null != mBaiduMap) {
                        mBaiduMap.addOverlay(polygonOption);
                    }

                    if (k == Const.NONE) {
                        tempFristAreasPoint = areasPoint;
                        tempFristArea = area;
                    }
                    // 去比较当前位置是否在范围内
                    if (!isInAreas) {
                        // 已经找到当前位置所在的范围了，就不会再进入这里了
                        tempAreasPoint = areasPoint;
                        mSelectedRegion = area;
                        // 如果当前位置的，直接取自己位置是否有范围内
                        isInAreas = SpatialRelationUtil.isPolygonContainsPoint(areasPoint, mFirstCurrentMoveLatlng);
                        isInServiceScope = isInAreas;
                        if (!isInAreas) {
                            // 如果最后一个还不是在范围中的，取第一个范围
                            if (k == mRegionsArea.size() - Const.ONE) {
                                tempAreasPoint = tempFristAreasPoint;
                                mSelectedRegion = tempFristArea;
                            }
                        }
                    }
                }
            }

            // 找到在范围内的，绘制线，显示提示
            if (null != tempAreasPoint) {
                if (null != mLocatedCity && !mCity.equals(mLocatedCity.getName())) {
                    if (null != mSelectedRegion) {
                        moveToFocus(mSelectedRegion.getFocus());
                    }
                }

                int selectedAreaIndex = Const.NEGATIVE;
                if (null != mSelectedRegion) {
                    selectedAreaIndex = (int) mSelectedRegion.getId();
                    setRoadRangeTips(mSelectedRegion.getDesc());
                }

                mServiceArearView.setSelectedTab(selectedAreaIndex);

                mScope1Pts = tempAreasPoint;
                setArearsTips();
            }
        }

        // 如果没有取到范围，就根据选择的城市反转取得该城市的经纬度，把地图切换过去
        if (null == tempAreasPoint) {
            // 地址转经纬度
            GeoCodeOption option = new GeoCodeOption()
                    .city(mCity)
                    .address(mCity);
            mCityGeoCoder.geocode(option);
        }
    }

    /**
     * 设置上面的范围边界提示，如果为空，则隐藏不显示
     *
     * @param desc desc
     */
    private void setRoadRangeTips(String desc) {
        if (CNStringUtil.isNotEmpty(desc)) {
            tvRoadRangeTips.setText(desc);
            llRoadRangeTips.setVisibility(View.VISIBLE);
        } else {
            llRoadRangeTips.setVisibility(View.GONE);
        }
    }

    private CNServiceScopeView.OnServiceAreasChangedListener mOnServiceAreasChangedListener = new CNServiceScopeView.OnServiceAreasChangedListener() {
        @Override
        public void onServiceAreasChanged(Region area) {
            mSelectedRegion = area;
            addServiceScope(area);
        }
    };

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        mMapView.showScaleControl(false); // 隐藏缩放控件
        mMapView.showZoomControls(false); // 隐藏缩放控件

        UiSettings uiSettings = mBaiduMap.getUiSettings();
        uiSettings.setCompassEnabled(false); // 禁用指南针图层

        MyLocationConfiguration.LocationMode currentMode = MyLocationConfiguration.LocationMode.NORMAL;

        // 先定位自己所在的位置
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMaxAndMinZoomLevel(20, 10);

        // 如果已经定位成功了，进入地图时，默认显示定位到的城市
        double latitude = AppBridgeUtils.getIns().getLatitude();
        double longitude = AppBridgeUtils.getIns().getLongitude();

        if (latitude != Const.NEGATIVE &&
                longitude != Const.NEGATIVE) {
            LatLng latLng = new LatLng(latitude, longitude);
            setMapStatusHasZoom(latLng);
        } else {
            // 默认把地图移动到杭州
            LatLng latLng = new LatLng(30.297936D, 120.120758D); // 福地二期的经纬度
            setMapStatusHasZoom(latLng);
        }

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();

        //设置当前定位到自己的位置的图标
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.cn_icon_marker_location);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(currentMode, true, mCurrentMarker));

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideInput(mMapView);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /**
     * 初始化poi
     */
    private void initPOISearch() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mSuggestPoiSearch = PoiSearch.newInstance();
        mSuggestPoiSearch.setOnGetPoiSearchResultListener(mSuggestPoiSearchListener);
    }

    /**
     * 初始化GeoCoder
     */
    private void initGeoCoder() {
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);

        // 切换城市时使用
        mCityGeoCoder = GeoCoder.newInstance();
        mCityGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (null == result || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                setMapStatus(result.getLocation());
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            }
        });
    }

    private void moveToFocus(String strFocus) {
        LatLng latLng;
        if (CNStringUtil.isNotEmpty(strFocus)) {
            String[] focus = strFocus.split(",");
            if (focus.length == 2) {
                double lng = CNNumberUtils.stringToDouble(focus[0]);
                double lat = CNNumberUtils.stringToDouble(focus[1]);
                latLng = new LatLng(lat, lng);
                setMapStatus(latLng);
            }
        }
    }

    /**
     * 绘制服务区域
     *
     * @param area 多边形的点
     */
    private void addServiceScope(Region area) {
        setRoadRangeTips(area.getDesc());
        // 将坐标移动中心点
        moveToFocus(area.getFocus());
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (null == location || null == mMapView)
            return;

        // 构造定位数据
        MyLocationData locData = new MyLocationData
                .Builder()
                .accuracy(location.getRadius())
                        // .direction(100) // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc) {
            isFirstLoc = false;
            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
            // 定位完后把定位层关闭，不显示系统的定位层，添加自己定位成功的图标，系统的会把图标放大！
            mBaiduMap.setMyLocationEnabled(false);

            //设定中心点坐标，也是默认定义Maker坐标点
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mCurrentLatLng = latLng;
            if (null == mCurrentMoveLatLng) {
                mCurrentMoveLatLng = latLng;
            }

            // 转换
            searchGeoCoder(mCurrentMoveLatLng);
            setMapStatusHasZoom(mCurrentMoveLatLng);

            /**
             * 如果选择的与当前是同一个城市，添加当前自己的位置图标，
             * 把小车移动到上一次存在的位置，否则要等获取到范围后再把小车移动到第一个范围的中心点
             */
            if (null != mSelectedCity && null != mLocatedCity
                    && mSelectedCity.getId() == mLocatedCity.getId()) {
                isMyLocationGeoCoder = true;

                addMyMarker(latLng);

                initCarMarket();
            }
        }
    }

    private void addMyMarker(LatLng latLng) {
        if (null != mCurrentLocatlay) {
            mCurrentLocatlay.remove();
        }
        // 构建自己位置图标
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions()
                .position(latLng) //设置marker的位置
                .icon(mCurrentMarker)//设置marker图标
                .zIndex(9) //设置marker所在层级
                .draggable(false);
        // 在地图上添加Marker，并显示
        mCurrentLocatlay = mBaiduMap.addOverlay(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        isMapStatusChange = true;
        mCurrentMoveLatLng = mapStatus.target;
        setInfoWindow(mapStatus.target);
        if (!isSearchLocation && !isMyLocationGeoCoder) {
            searchGeoCoder(mapStatus.target);
            mSearchKey = null;
        }
        isMyLocationGeoCoder = false;
        isSearchLocation = false;
    }

    private void initCarMarket() {
        setInfoWindow(mCurrentMoveLatLng);
    }

    /**
     * 更新地图坐标，并设置缩放级别
     *
     * @param latLng 坐标
     */
    private void setMapStatusHasZoom(LatLng latLng) {
        //定义地图状态
        mMapStatus = new MapStatus
                .Builder()
                .target(latLng)
                .zoom(15)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(msu);
    }

    /**
     * 更新地图坐标，但不重新设置缩放级别
     *
     * @param latLng 坐标
     */
    private void setMapStatus(LatLng latLng) {
        if (null == mBaiduMap) {
            return;
        }
        //定义地图状态
        mMapStatus = new MapStatus
                .Builder()
                .target(latLng)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(msu);
    }

    /**
     * 更新Marker提示文字
     *
     * @param latLng 当前坐标
     */
    private void setInfoWindow(LatLng latLng) {
        setRoadRangeTips("");
        if (null != mAllAreas) {
            Region selectedRegion = null;
            // 要在所有范围内找是否支持该区域
            for (Map.Entry<Region, List<LatLng>> entry : mAllAreas.entrySet()) {
                mScope1Pts = entry.getValue();
                isInServiceScope = SpatialRelationUtil.isPolygonContainsPoint(mScope1Pts, latLng);
                // 如果找到了直接跳出循环
                if (isInServiceScope) {
                    selectedRegion = entry.getKey();
                    break;
                }
            }
            int selectedIndex = Const.NEGATIVE;
            if (null != selectedRegion) {
                selectedIndex = (int) selectedRegion.getId();
                setRoadRangeTips(selectedRegion.getDesc());
            }
            mServiceArearView.setSelectedTab(selectedIndex);
        }
        setArearsTips();
    }

    private void setArearsTips() {
        // 设置上面显示的提示文字
        int resId = isInServiceScope ? R.string.cn_marker_in_scope : R.string.cn_marker_out_scope;
        tvAddressTips.setText(getStringResources(resId));
    }

    /**
     * ////////////////////////////////////////////////////////////////////////////
     * //////////////////////////////POI搜索回调//////////////////////////////////
     * ////////////////////////////////////////////////////////////////////////////
     */

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (null == result
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            isMapStatusChange = false;
            if (Const.IS_DEBUG) {
                CNCommonManager.makeText(mContext, "onGetPoiResult未找到结果");
            }
            setNoSearchPoi();
        } else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            // 如果是拖动地图时根据latlng进行的poi搜索，不要再移动地图到中心位置
            if (!isMapStatusChange) {
                if (result.getAllPoi().size() > Const.NONE) {
                    final LatLng latLng = result.getAllPoi().get(Const.NONE).location;
                    setMapStatus(latLng);
                    setInfoWindow(latLng);
                }
            }
            isMapStatusChange = false;

            List<BaseInfo> locationDatas = filterPOIResult(result.getAllPoi());

            setLocationAddress(locationDatas);
        } else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            if (Const.IS_DEBUG) {
                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                StringBuilder buffer = new StringBuilder("onGetPoiResult在");
                for (CityInfo cityInfo : result.getSuggestCityList()) {
                    buffer.append(cityInfo.city);
                    buffer.append(",");
                }
                buffer.append("找到结果");
                CNCommonManager.makeText(mContext, buffer.toString());
            }
        }
    }

    public List<BaseInfo> filterPOIResult(List<PoiInfo> poiInfos) {
        List<BaseInfo> locationDatas = new ArrayList<>();
        if (null != mSearchKey) {
            locationDatas.add(mSearchKey);
        }

        if (null == poiInfos || poiInfos.size() <= Const.NONE) {
            return locationDatas;
        }

        PoiLocationRoad poiRoad;
        for (PoiInfo info : poiInfos) {
            if (isEmptyByPoiInfo(info)) {
                continue;
            }
            // 要在所有范围内找是否支持该区域
            if (null != mAllAreas) {
                boolean isIn = false;
                for (Map.Entry<Region, List<LatLng>> entry : mAllAreas.entrySet()) {
                    LatLng latLng = new LatLng(info.location.latitude, info.location.longitude);
                    isIn = SpatialRelationUtil.isPolygonContainsPoint(entry.getValue(), latLng);
                    // 如果找到了直接跳出循环
                    if (isIn) {
                        break;
                    }
                }
                // 该地址不在区域内，不加载出来
                if (!isIn) {
                    continue;
                }
            }

            if (null != mSearchKey) {
                if (info.name.equals(mSearchKey.mName)
                        && info.address.equals(mSearchKey.mAddress)) {
                    continue;
                } else {
                    poiRoad = getPoiLocationRoad(info);
                }
            } else {
                poiRoad = getPoiLocationRoad(info);
            }

            locationDatas.add(poiRoad);
        }
        return locationDatas;
    }

    @NonNull
    public PoiLocationRoad getPoiLocationRoad(PoiInfo info) {

        Log.trace(TAG, "POI结果：\r\n" + "address：" + info.address + "，name：" + info.name + "，location：" + info.location);

        PoiLocationRoad poiRoad = new PoiLocationRoad();
        poiRoad.hasData = true;
        poiRoad.mName = info.name;
        poiRoad.mUid = info.uid;
        poiRoad.mAddress = info.address;
        poiRoad.mCity = info.city;
        poiRoad.mPhoneNum = info.phoneNum;
        poiRoad.mPostCode = info.postCode;
        poiRoad.mLatitude = info.location.latitude;
        poiRoad.mLongitude = info.location.longitude;
        poiRoad.mHasCaterDetails = info.hasCaterDetails;
        poiRoad.mIsPano = info.isPano;
        return poiRoad;
    }

    private boolean isEmptyByPoiInfo(PoiInfo info) {
        return null == info
                || null == info.location
                || CNStringUtil.isEmpty(info.address)
                || CNStringUtil.isEmpty(info.name);
    }

    private void setNoSearchPoi() {
        PoiLocationRoad poiRoad = new PoiLocationRoad();
        poiRoad.hasData = false;
        poiRoad.mAddress = getStringResources(R.string.cn_no_poi_data);
        mPoiLocationRoads = new ArrayList<>();
        mPoiLocationRoads.add(poiRoad);
    }

    private void setLocationAddress(List<BaseInfo> baseInfos) {
        // 如果不在范围内，地址被过滤完了
        mPoiLocationRoads = new ArrayList<>();
        if (null == baseInfos || baseInfos.size() <= Const.NONE) {
            PoiLocationRoad poiRoad = new PoiLocationRoad();
            poiRoad.hasData = false;
            poiRoad.mAddress = getStringResources(R.string.cn_no_poi_data);

            mPoiLocationRoads.add(poiRoad);
        } else {
            mPoiLocationRoads.addAll(baseInfos);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            setNoSearchPoi();
            if (Const.IS_DEBUG) {
                CNCommonManager.makeText(mContext, "onGetPoiDetailResult未找到结果");
            }
        } else {
            // 如果是拖动地图时根据latlng进行的poi搜索，不要再移动地图到中心位置
            LatLng latLng = result.getLocation();
            if (!isMapStatusChange) {
                setMapStatus(latLng);
                setInfoWindow(latLng);
            }
            isMapStatusChange = false;

            PoiLocationRoad poiRoad = new PoiLocationRoad();
            poiRoad.hasData = true;
            poiRoad.mName = result.getName();
            poiRoad.mUid = result.getUid();
            poiRoad.mAddress = result.getAddress();
            poiRoad.mLatitude = result.getLocation().latitude;
            poiRoad.mLongitude = result.getLocation().longitude;

            mPoiLocationRoads = new ArrayList<>();
            mPoiLocationRoads.add(poiRoad);
        }
    }

    /**
     * 自动提示的poi结果
     */
    private OnGetPoiSearchResultListener mSuggestPoiSearchListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                mSearchKeys = new ArrayList<>();
                for (PoiInfo info : result.getAllPoi()) {
                    if (isEmptyByPoiInfo(info)) {
                        continue;
                    }
                    PoiLocationRoad searchKey = getPoiLocationRoad(info);
                    mSearchKeys.add(searchKey);
                }
                sugAdapter = new CNSearchKeyAdapter(mContext, R.layout.cn_search_key_layout, mSearchKeys);
                acSearchKey.setAdapter(sugAdapter);
                sugAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        }
    };

    /**
     * ////////////////////////////////////////////////////////////////////////////
     * //////////////////////////////POI搜索回调 END //////////////////////////////
     * ////////////////////////////////////////////////////////////////////////////
     */

    /**
     * ////////////////////////////////////////////////////////////////////////////
     * //////////////////////////////地理转码回调//////////////////////////////////
     * ////////////////////////////////////////////////////////////////////////////
     */

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (null == result || result.error != SearchResult.ERRORNO.NO_ERROR) {
            setNoSearchPoi();
            if (Const.IS_DEBUG) {
                CNCommonManager.makeText(mContext, "onGetGeoCodeResult-->抱歉，未能找到结果");
            }
            return;
        }
        if (Const.IS_DEBUG) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("Address-->").append(result.getAddress());
            buffer.append("Location-->").append(result.getLocation().toString());
            Log.trace(TAG, "转换成功：" + buffer);
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (null == result || result.error != SearchResult.ERRORNO.NO_ERROR) {
            setNoSearchPoi();
            if (Const.IS_DEBUG) {
                CNCommonManager.makeText(mContext, "onGetReverseGeoCodeResult-->抱歉，未能找到结果");
            }
            return;
        }
        if (Const.IS_DEBUG) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("Address-->").append(result.getAddress());
            buffer.append("，Location-->").append(result.getLocation().toString());
            buffer.append("，BusinessCircle-->").append(result.getBusinessCircle());
            Log.trace(TAG, "转换成功：" + buffer);
        }

        if (null != result.getAddressDetail()) {
            if (null == mSelectedAddress) {
                mSelectedAddress = new Address();
            }
            mSelectedAddress.setCity(result.getAddressDetail().city);
            mSelectedAddress.setProvince(result.getAddressDetail().province);
            mSelectedAddress.setDistrict(result.getAddressDetail().district);
            mSelectedAddress.setStreet(result.getAddressDetail().street);
        }

        // 如果是拖动地图时根据latlng进行的poi搜索，不要再移动地图到中心位置
        if (!isMapStatusChange) {
            if (null != result.getPoiList() && result.getPoiList().size() > Const.NONE) {
                final LatLng latLng = result.getPoiList().get(Const.NONE).location;
                setMapStatus(latLng);
                setInfoWindow(latLng);
            }
        }
        isMapStatusChange = false;

        // geo出来的直接把poi显示出来
        List<BaseInfo> locationDatas = filterPOIResult(result.getPoiList());
        setLocationAddress(locationDatas);
    }

    /**
     * ////////////////////////////////////////////////////////////////////////////
     * //////////////////////////////地理转码回调 END //////////////////////////////
     * ////////////////////////////////////////////////////////////////////////////
     */

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.unRegisterLocationListener(this);
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);

        mPoiSearch.destroy();
        mSuggestPoiSearch.destroy();
        mGeoCoder.destroy();
        mCityGeoCoder.destroy();

        if (null != mCurrentMarker) {
            mCurrentMarker.recycle();
            mCurrentMarker = null;
        }

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        CNProgressDialogUtil.dismissProgress(this);
        super.onDestroy();
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isCheckSuccess();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 选择位置
        navBar.setNavTitle(getStringResources(R.string.cn_car_location));
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
        // 确定
        navBar.setRightBtn(getStringResources(R.string.cn_confirm));
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft) {
            isCheckSuccess();
        } else if (viewId == R.id.tvNavRight) {
            // 要执行搜索，然后显示弹框
            showLocationPopup();
        }
        return true;
    }

    private void isCheckSuccess() {
        String msg = getStringResources(R.string.cn_no_selected_location);
        DialogManager.onAffirm(mContext, msg, new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CNViewManager.getIns().pop(CNCarLocationActivity.this);
            }
        });
    }

    @Override
    public void onNoFastClick(View v) {
        int vId = v.getId();
        if (vId == R.id.flMarkerItemView
                || vId == R.id.tvAddressTips
                || vId == R.id.ivCarMarker) {
            showLocationPopup();
        } else if (vId == R.id.acSearchKey) {
            // 点击搜索框，得到焦点
            acSearchKey.setFocusable(true);
            acSearchKey.setFocusableInTouchMode(true);
            acSearchKey.requestFocus();

            // 如果框内有内容，点击时，显示提示内容
            String keyword = acSearchKey.getText().toString().trim();
            if (CNStringUtil.isNotEmpty(keyword)) {
                // 进行内容检索
                requestSuggestionPoi(mCity, keyword);
            }
        } else if (vId == R.id.ivClear) {
            // 清空搜索
            acSearchKey.getText().clear();
        } else if (vId == R.id.tvLocationCity) {
            // Notice 点击城市，如果选择的城市与定位的城市相同，点击时移动到当前定位的城市
            // 如果选择的当前城市是一样的，不要再次请求
            if (null != mSelectedCity) {
                if (null != mLocatedCity && mLocatedCity.getId() == mSelectedCity.getId()) {
                    // 如果还是在当前位置，不进行移动
                    if (mCurrentLatLng.latitude != mCurrentMoveLatLng.latitude
                            && mCurrentLatLng.longitude != mCurrentMoveLatLng.longitude) {
                        // 移动到当前所在位置
                        setMapStatus(mCurrentLatLng);
                    }
                }
            }
        }
    }

    @Override
    public void onClickPopupItem(int popupType, int position, View v, BaseInfo baseInfo) {
        switch (popupType) {
            case CNCarWashingLogic.DIALOG_LOCATION_TYPE: // 地址
                PoiLocationRoad poiItem = (PoiLocationRoad) baseInfo;

                if (!poiItem.hasData) {
                    return;
                }

                // 判断选择的位置是否在范围内
                LatLng latLng = new LatLng(poiItem.mLatitude, poiItem.mLongitude);
                setInfoWindow(latLng);

                if (!isInServiceScope) {
                    setMapStatus(latLng);
                    CNCommonManager.makeText(mContext, getStringResources(R.string.cn_marker_out_scope));
                    return;
                }

                String result = poiItem.mName + " " + poiItem.mAddress;
                Log.trace(TAG, "选择的位置为：" + result);

                // 查找选择的位置所在的范围
                ArrayList<Long> mRegionsIds = new ArrayList<>();
                if (null != mAllAreas) {
                    // 要在所有范围内找是否支持该区域
                    for (Map.Entry<Region, List<LatLng>> entry : mAllAreas.entrySet()) {
                        boolean isIn = SpatialRelationUtil.isPolygonContainsPoint(entry.getValue(), latLng);
                        // 如果找到了直接跳出循环
                        if (isIn) {
                            mRegionsIds.add(entry.getKey().getId());
                        }
                    }
                }

                if (null == mSelectedPosition) {
                    mSelectedPosition = new Position();
                }

                if (null != mSelectedAddress) {
                    mSelectedAddress.setOther(result);
                }
                mSelectedPosition.setAddress(mSelectedAddress);
                mSelectedPosition.setLatitude(poiItem.mLatitude);
                mSelectedPosition.setLongitude(poiItem.mLongitude);

                Intent data = new Intent();
                data.putExtra(CNCarWashingLogic.KEY_SELECT_CAR_LOC_CITY, mSelectedCity);
                data.putExtra(CNCarWashingLogic.KEY_SELECT_REGION_AREAS, mRegionsIds);
                data.putExtra(CNCarWashingLogic.KEY_SELECT_CAR_LOCATION, mSelectedPosition);

                setResult(Activity.RESULT_OK, data);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CNViewManager.getIns().pop(CNCarLocationActivity.this);
                    }
                }, 10);
                break;
        }
    }

    private void showLocationPopup() {
        if (null == mPoiLocationRoads || mPoiLocationRoads.isEmpty()) {
            return;
        }
        CNCarWashingLogic.startLocationDialog(mContext, mPoiLocationRoads, this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int before, int count) {
        if (cs.toString().trim().length() <= Const.NONE) {
            ivClear.setVisibility(View.GONE);
            isSearchLocation = false;
            return;
        }

        isSearchLocation = true;
        ivClear.setVisibility(View.VISIBLE);
        acSearchKey.requestFocusFromTouch();
        String keyword = cs.toString().trim();

        // 进行内容检索
        requestSuggestionPoi(mCity, keyword);
    }

    /**
     * 发起经纬度转地址检索
     *
     * @param latLng latLng
     */
    private void searchGeoCoder(LatLng latLng) {
        // 经纬度转地址
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    /**
     * 搜索poi
     *
     * @param city    城市
     * @param keyword 关键字
     */
    private void requestSuggestionPoi(String city, String keyword) {
        Log.trace(TAG, "在<" + city + ">进行搜索的内容：" + keyword);

        PoiCitySearchOption option = new PoiCitySearchOption()
                .city(city)
                .keyword(keyword)
                .pageCapacity(10)
                .pageNum(0);
        mSuggestPoiSearch.searchInCity(option);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_INITIALIZE_REGION_DATA_ACTION);
        filter.addAction(CustomBroadcast.ON_INITIALIZE_COMMOM_CARS_DATA_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        if (null == intent) {
            return;
        }

        String action = intent.getAction();
        /**
         * 初始化城市区域的广播
         * 初始化数据的广播
         */
        if (CustomBroadcast.ON_INITIALIZE_REGION_DATA_ACTION.equals(action)
                || CustomBroadcast.ON_INITIALIZE_COMMOM_CARS_DATA_ACTION.equals(action)) {
            boolean result = intent.getBooleanExtra("result", false);
            if (result) {
                loadingLocCityConfig(mSelectedCity.getId());
            }
            CNProgressDialogUtil.dismissProgress(mContext);
        }
    }
}
