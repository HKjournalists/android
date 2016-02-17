package com.kplus.car.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.kplus.car.R;
import com.kplus.car.util.StringUtils;

public class EmergencyDetailActivity extends BaseActivity implements
		OnClickListener {

	private static final int PHONE_REGIST_EMERGENCY_REQUEST = 0x01;

	private View leftView;
	private ImageView ivLeft;
	private View rightView;
	private TextView tvRight;
	private TextView tvTitle;
	private View serviceBtn;
	private MapView mapView;
	private TextView addressText;
	private String vehicleNumber;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_emergency_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("紧急救援");
		rightView = findViewById(R.id.rightView);
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText("救援记录");
		serviceBtn = findViewById(R.id.emergency_detail_service_call);

		addressText = (TextView) findViewById(R.id.emergency_detail_address);
		mapView = (MapView) findViewById(R.id.emergency_detail_mapView);
		BaiduMap baiduMap = mapView.getMap();
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		baiduMap.setMyLocationEnabled(true);
		BDLocation bdl = mApplication.getLocation();
		if(bdl != null){
			MyLocationData locData = new MyLocationData.Builder().accuracy(bdl.getRadius())
																 .direction(bdl.getDirection())
																 .latitude(bdl.getLatitude())
																 .longitude(bdl.getLongitude()).build();
			baiduMap.setMyLocationData(locData);
			BitmapDescriptor db = BitmapDescriptorFactory.fromResource(R.drawable.daze_location);
			MyLocationConfiguration mlc = new MyLocationConfiguration(LocationMode.COMPASS, true, db);
			baiduMap.setMyLocationConfigeration(mlc);
			OverlayOptions options = new MarkerOptions().position(new LatLng(bdl.getLatitude(), bdl.getLongitude())).icon(db);
			baiduMap.addOverlay(options);
		}
		baiduMap.setMyLocationEnabled(false);
		if(!StringUtils.isEmpty(mApplication.getAddress()))
			addressText.setText(mApplication.getAddress());
	}

	protected void loadData() {
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
		serviceBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(leftView)) {
			finish();
//			overridePendingTransition(R.anim.slide_in_from_top,
//					R.anim.slide_out_to_bottom);
		} 
		else if(v.equals(rightView)){
			Intent intent = new Intent(this, UseServiceRecord.class);
			intent.putExtra("vehicleNumber", vehicleNumber);
			startActivity(intent);
		}else if (v.equals(serviceBtn)) {
			if (mApplication.getId() == 0) {
				Toast.makeText(this, "使用该服务前需要绑定手机号", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, PhoneRegistActivity.class);
				intent.putExtra("isMustPhone", true);
				startActivityForResult(intent, PHONE_REGIST_EMERGENCY_REQUEST);
				return;
			}
			Intent intent = new Intent(Intent.ACTION_CALL,
					Uri.parse("tel:4008799770"));
			startActivity(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHONE_REGIST_EMERGENCY_REQUEST:
			if (mApplication.getpId() != 0) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008799770"));
				startActivity(intent);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}
