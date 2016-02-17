package com.kplus.car.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.ToastUtil;

/**
 * Created by Administrator on 2015/3/23.
 */
public class MarkLocationActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private EditText mTvSearch;
    private ImageView mBtnSearch;
    private TextView mName;
    private TextView mAddress;
    private TextView mSetLocation;
    private View mResultLayout;
    private PoiSearch mPoiSearch;
    private BDLocation mLocation;
    private PoiResult mPoiResult;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_mark_location);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("标注位置");
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("取消");

        mTvSearch = (EditText) findViewById(R.id.tvSearch);
        mBtnSearch = (ImageView) findViewById(R.id.btnSearch);
        mName = (TextView) findViewById(R.id.name);
        mAddress = (TextView) findViewById(R.id.address);
        mSetLocation = (TextView) findViewById(R.id.set_location);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mResultLayout = findViewById(R.id.result_layout);
    }

    @Override
    protected void loadData() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mLocation = mApplication.getLocation();
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(mLocation.getRadius()).latitude(mLocation.getLatitude())
                .longitude(mLocation.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null);
        mBaiduMap.setMyLocationConfigeration(config);
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            String address = bundle.getString("key-location-value");
            poiSearch(address);
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mBtnSearch, this);
        ClickUtils.setNoFastClickListener(mSetLocation, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.btnSearch:
                String str = mTvSearch.getText().toString();
                if (!"".equals(str)){
                    poiSearch(str);
                }
                break;
            case R.id.set_location:
                Intent it = new Intent();
                it.putExtra("location", mName.getText());
                setResult(Constants.RESULT_TYPE_CHANGED, it);
                finish();
                break;
        }
    }

    private class MyPoiOverlay extends PoiOverlay{

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
            PoiInfo info = mPoiResult.getAllPoi().get(i);
            mName.setText(info.name);
            mAddress.setText(info.address);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(info.location));
            return true;
        }
    }

    private void poiSearch(String str){
        mPoiSearch = PoiSearch.newInstance();
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
            public void onGetPoiResult(PoiResult result){
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    ToastUtil.makeShortToast(MarkLocationActivity.this, "未搜索到结果");
                    mResultLayout.setVisibility(View.GONE);
                    return;
                }
                mPoiResult = result;
                mResultLayout.setVisibility(View.VISIBLE);
                mBaiduMap.clear();
                PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result);
                overlay.addToMap();
                overlay.zoomToSpan();
                overlay.onPoiClick(0);
            }
            public void onGetPoiDetailResult(PoiDetailResult result){
            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mPoiSearch.searchNearby(new PoiNearbySearchOption().pageCapacity(10).pageNum(1).keyword(str).location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTvSearch.getWindowToken(), 0);
    }
}
