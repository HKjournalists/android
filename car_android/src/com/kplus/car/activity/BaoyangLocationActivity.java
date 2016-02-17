package com.kplus.car.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.BaoyangShopTask;
import com.kplus.car.model.BaoyangShop;
import com.kplus.car.model.response.BaoyangShopResponse;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.RegularTextView;

import java.util.List;

/**
 * Created by Administrator on 2015/3/23.
 */
public class BaoyangLocationActivity extends BaseActivity implements ClickUtils.NoFastClickListener, BaiduMap.OnMarkerClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView mTvName;
    private TextView mTvAddress;
    private TextView mTvPhone;
    private TextView mModify;
    private TextView mConfirm;
    private TextView mSelectedBrand;
    private EditText mSearchBar;
    private String mName;
    private String mAddress;
    private String mPhone;
    private String mLat;
    private String mLon;
    private View mAddLayout;
    private View mResultLayout;
    private BDLocation mLocation;
    private LinearLayout mTopBar;
    private Marker mLastMarker;
    private BitmapDescriptor mNormal, mSelected;
    private final static String[] BRANDS = {"大众", "奥迪", "别克", "现代", "雪佛兰"};

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_baoyang_location);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_baoyang_location);

        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        mTvName = (TextView) findViewById(R.id.name);
        mTvAddress = (TextView) findViewById(R.id.address);
        mTvPhone = (TextView) findViewById(R.id.phone);
        mModify = (TextView) findViewById(R.id.modify);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mAddLayout = findViewById(R.id.add_layout);
        mResultLayout = findViewById(R.id.result_layout);
        mTopBar = (LinearLayout) findViewById(R.id.top_bar);
        mSearchBar = (EditText) findViewById(R.id.search_bar);
        mNormal = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        mSelected = BitmapDescriptorFactory.fromResource(R.drawable.marker_selected);
        for (final String brand : BRANDS){
            TextView tv = new RegularTextView(this);
            tv.setText(brand);
            tv.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            int padding = BaseActivity.dip2px(this, 8);
            tv.setPadding(padding, padding, padding, padding);
            mTopBar.addView(tv);
            ClickUtils.setNoFastClickListener(tv, new ClickUtils.NoFastClickListener() {
                @Override
                public void onNoFastClick(View v) {
                    if (mSelectedBrand != null)
                        mSelectedBrand.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
                    mSelectedBrand = (TextView) v;
                    mSelectedBrand.setTextColor(getResources().getColor(R.color.daze_orangered5));
                    mSearchBar.setText(brand);
                    getShopList(null, brand);
                }
            });
        }
        TextView other = new RegularTextView(this);
        other.setText("更多");
        other.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
        other.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        int padding = BaseActivity.dip2px(this, 8);
        other.setPadding(padding, padding, padding, padding);
        mTopBar.addView(other);
        ClickUtils.setNoFastClickListener(other, new ClickUtils.NoFastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                Intent it = new Intent(BaoyangLocationActivity.this, BrandListActivity.class);
                startActivityForResult(it, Constants.REQUEST_TYPE_MODEL);
            }
        });
    }

    @Override
    protected void loadData() {
        Intent it = getIntent();
        mName = it.getStringExtra("name");
        mAddress = it.getStringExtra("address");
        mPhone = it.getStringExtra("phone");
        mLat = it.getStringExtra("lat");
        mLon = it.getStringExtra("lon");
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mLocation = mApplication.getLocation();
        if (mLocation != null) {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(mLocation.getRadius()).latitude(mLocation.getLatitude())
                    .longitude(mLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, BitmapDescriptorFactory.fromResource(R.drawable.currentl));
            mBaiduMap.setMyLocationConfigeration(config);
        }
        mBaiduMap.setOnMarkerClickListener(this);
        if (!StringUtils.isEmpty(mName)){
            mResultLayout.setVisibility(View.VISIBLE);
            mTvName.setText(mName);
            if (!StringUtils.isEmpty(mLat) && !StringUtils.isEmpty(mLon)){
                mTvAddress.setVisibility(View.VISIBLE);
                mTvAddress.setText(mAddress);
                mTvPhone.setVisibility(View.VISIBLE);
                mTvPhone.setText(mPhone);
                mModify.setVisibility(View.GONE);
                double lat = Double.parseDouble(mLat);
                double lon = Double.parseDouble(mLon);
                LatLng point = new LatLng(lat, lon);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(point).icon(mSelected);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(point));
            }
            else {
                mTvAddress.setVisibility(View.GONE);
                mTvPhone.setVisibility(View.GONE);
                mModify.setVisibility(View.VISIBLE);
                if (mLocation != null) {
                    LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
                }
            }
        }
        else {
            if (mLocation != null) {
                LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            }
            getShopList(null, null);
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mConfirm, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.add_shop), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.modify), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.search_btn), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.location), this);
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
            case R.id.confirm:
                Intent it = new Intent();
                it.putExtra("name", mName);
                if (!StringUtils.isEmpty(mAddress))
                    it.putExtra("address", mAddress);
                if (!StringUtils.isEmpty(mPhone))
                    it.putExtra("phone", mPhone);
                if (!StringUtils.isEmpty(mLat))
                    it.putExtra("lat", mLat);
                if (!StringUtils.isEmpty(mLon))
                    it.putExtra("lon", mLon);
                setResult(Constants.RESULT_TYPE_CHANGED, it);
                finish();
                break;
            case R.id.add_shop:
            case R.id.modify:
                it = new Intent(this, AlertDialogActivity.class);
                it.putExtra("alertType", KplusConstants.ALERT_INPUT_SHOP_NAME);
                startActivityForResult(it, Constants.REQUEST_TYPE_ADD);
                break;
            case R.id.search_btn:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchBar.getWindowToken(), 0);
                String search = mSearchBar.getText().toString();
                if (!StringUtils.isEmpty(search))
                    getShopList(search, null);
                else
                    ToastUtil.makeShortToast(this, "请输入保养店名");
                break;
            case R.id.location:
                if (mLocation != null) {
                    LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constants.REQUEST_TYPE_ADD:
                if (resultCode == RESULT_OK){
                    mBaiduMap.clear();
                    mAddLayout.setVisibility(View.GONE);
                    mResultLayout.setVisibility(View.VISIBLE);
                    mModify.setVisibility(View.VISIBLE);
                    mTvAddress.setVisibility(View.GONE);
                    mTvPhone.setVisibility(View.GONE);
                    mName = data.getStringExtra("shopName");
                    mTvName.setText(mName);
                    mAddress = null;
                    mPhone = null;
                    mLat = null;
                    mLon = null;
                }
                break;
            case Constants.REQUEST_TYPE_MODEL:
                if (resultCode == Constants.RESULT_TYPE_CHANGED){
                    mSelectedBrand.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
                    mSelectedBrand = null;
                    String brandName = data.getStringExtra("brandName");
                    mSearchBar.setText(brandName);
                    getShopList(null, brandName);
                }
                break;
        }
    }

    private void getShopList(final String name, final String brand){
        new BaoyangShopTask(mApplication){

            @Override
            protected void onPreExecute() {
                showloading(true);
            }

            @Override
            protected void onPostExecute(BaoyangShopResponse response) {
                showloading(false);
                if (response != null && response.getCode() != null && response.getCode() == 0){
                    mBaiduMap.clear();
                    List<BaoyangShop> list = response.getData().getList();
                    if (list != null && list.size() > 0){
                        for (int i = 0; i < list.size(); i++){
                            BaoyangShop shop = list.get(i);
                            double lat = Double.parseDouble(shop.getLat());
                            double lon = Double.parseDouble(shop.getLon());
                            LatLng point = new LatLng(lat, lon);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions().position(point).icon(mNormal);
                            //在地图上添加Marker，并显示
                            Marker marker = (Marker) mBaiduMap.addOverlay(option);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("baoyangShop", shop);
                            marker.setExtraInfo(bundle);
                            if (i == 0) {
                                onMarkerClick(marker);
                            }
                        }
                    }
                    else {
                        if (!StringUtils.isEmpty(name))
                            ToastUtil.makeShortToast(BaoyangLocationActivity.this, "没有搜索到" + name);
                        else
                            ToastUtil.makeShortToast(BaoyangLocationActivity.this, "没有搜索到" + brand);
                    }
                }
            }
        }.execute(name, brand);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        if (bundle == null)
            return false;
        marker.setIcon(mSelected);
        if (mLastMarker != null)
            mLastMarker.setIcon(mNormal);
        mLastMarker = marker;
        BaoyangShop shop = (BaoyangShop) bundle.getSerializable("baoyangShop");
        double lat = Double.parseDouble(shop.getLat());
        double lon = Double.parseDouble(shop.getLon());
        LatLng point = new LatLng(lat, lon);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(point));
        mAddLayout.setVisibility(View.VISIBLE);
        mResultLayout.setVisibility(View.VISIBLE);
        mModify.setVisibility(View.GONE);
        mName = shop.getName();
        mTvName.setText(mName);
        mAddress = shop.getAddress();
        if (!StringUtils.isEmpty(mAddress)) {
            mTvAddress.setVisibility(View.VISIBLE);
            mTvAddress.setText(mAddress);
        }
        else {
            mTvAddress.setVisibility(View.GONE);
        }
        mPhone = shop.getTel();
        if (!StringUtils.isEmpty(mPhone)){
            mTvPhone.setVisibility(View.VISIBLE);
            mTvPhone.setText(mPhone);
        }
        else {
            mTvPhone.setVisibility(View.GONE);
        }
        mLat = shop.getLat();
        mLon = shop.getLon();
        return true;
    }
}
