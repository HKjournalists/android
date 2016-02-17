package com.kplus.car.activity;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.AppLogsTask;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.model.Advert;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.NoticeContent;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.json.AdvertJson;
import com.kplus.car.model.response.GetAdvertDataResponse;
import com.kplus.car.model.response.request.GetAdvertDataRequest;
import com.kplus.car.util.BroadcastReceiverUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.ServicesActionUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.UpdateManager;
import com.kplus.car.widget.RegularTextView;
import com.lotuseed.android.Lotuseed;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainUIActivity extends TabActivity implements BroadcastReceiverUtil.BroadcastListener {
	private static final int REQUEST_FOR_EXIT_APPLICATION = 1;
    private static final int REQUEST_FOR_SET_TIME = 2;
    private static final int REQUEST_FOR_NETWORK = 3;
    private static final int REQUEST_FOR_DATE = 4;
    private static final int REQUEST_FOR_BIND_FAIL = 5;
	private int currentTab = 0;
	private KplusApplication mApplication;
	private RelativeLayout[] layouts = new RelativeLayout[4];
	private ImageView[] imageViews = new ImageView[4];
	private TextView[] textViews = new RegularTextView[4];
	private String[] labels = {"汽车服务","我的车","管家","个人中心"};
	private String[] identities = {"汽车服务","我的车","管家","个人中心"};
	private ImageView[] ivIndecators = new ImageView[4];
	private int[] images = new int[4];
	private int[] imagesActive = new int[4];
	private SharedPreferences sp;
	private LayoutInflater inflater;
	private LayoutParams params;
	private TabHost tHost;
	private BroadcastReceiver tabChangeReceiver;
	private MyLocationListener mLocationListener;
	private LocationClient mLocationClient = null;
	private String mCityName;
	private String mProvince;
	private String mAddress;

	private BroadcastReceiver getUserIdReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null && intent.getBooleanExtra("issuccess", false)){
				urlSchema(getIntent());
			}
		}
	};

	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
			receiver = null;
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	};

	private BroadcastReceiver tabreceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ivIndecators[0].setVisibility(View.VISIBLE);
		}
	};

	private BroadcastReceiver newMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int newMesage = intent.getIntExtra("newMessage", 0);
			if(newMesage <= 0)
				ivIndecators[3].setVisibility(View.GONE);
			else
				ivIndecators[3].setVisibility(View.VISIBLE);
		}
	};

	private BroadcastReceiver locationChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String adType = null;
			if (null != intent && null != intent.getExtras()) {
				// 同时监听新用户登录会发送此广播接收并弹出广告
				Bundle bundle = intent.getExtras();
				adType = bundle.getString("adType");
				getAdvertData(adType);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_main_ui);
		currentTab = getIntent().getIntExtra("currentTab", 0);
		mApplication = (KplusApplication) getApplication();

		// 初始化莲子统计
		Lotuseed.init(getApplicationContext());
		// 自动提交错误报告
		Lotuseed.onCrashLog();
		Lotuseed.setDebugMode(false);

		// 获取资料
		if (mApplication.getId() > 0) {
			AppBridgeUtils.getIns().getUserInfo(this);
		}

		inflater = LayoutInflater.from(this);
		float scale = getResources().getDisplayMetrics().density;
		int h = (int) (52 * scale + 0.5f);
		int w = getResources().getDisplayMetrics().widthPixels / 4;
		params = new LayoutParams(w, h);
		for(int i = 0; i < 4; i++){
			layouts[i] = (RelativeLayout) inflater.inflate(R.layout.daze_tab_item, null);
			layouts[i].setLayoutParams(params);
			textViews[i] = (TextView) layouts[i].findViewById(R.id.tvTitle);
			textViews[i].setTextColor(getResources().getColor(R.color.daze_darkgrey9));
			imageViews[i] = (ImageView) layouts[i].findViewById(R.id.ivIcon);
			textViews[i].setText( labels[i]);
			images[i] = R.drawable.my_car;
			switch(i){
			case 0:
				images[i] = R.drawable.daze_service;
				imagesActive[i] = R.drawable.daze_service_active;
				break;
			case 1:
				images[i] = R.drawable.my_car;
				imagesActive[i] = R.drawable.my_car_active;
				break;
			case 2:
				images[i] = R.drawable.daze_guanjia;
				imagesActive[i] = R.drawable.daze_guanjia_active;
				break;
			case 3:
				images[i] = R.drawable.menu_user;
				imagesActive[i] = R.drawable.menu_user_active;
				break;
			default:
				break;
			}
			imageViews[i].setImageResource(images[i]);
			ivIndecators[i] = (ImageView) layouts[i].findViewById(R.id.ivIndicator);
		}
		String strTabService = mApplication.dbCache.getValue(KplusConstants.DB_KEY_TAB_SERVICE_CHANGE);
		if(!StringUtils.isEmpty(strTabService) && strTabService.equals("1"))
			ivIndecators[1].setVisibility(View.VISIBLE);
		tHost = this.getTabHost();
		Intent guanjiaIntent = new Intent(this, GuanjiaHomeActivity.class);
		guanjiaIntent.putExtra("currentTab", currentTab);
		guanjiaIntent.putExtra("providerId", getIntent().getStringExtra("providerId"));
		guanjiaIntent.putExtra("serviceType", getIntent().getStringExtra("serviceType"));
		guanjiaIntent.putExtra("cityId", getIntent().getStringExtra("cityId"));
		// NOTICE 汽车服务用原生的界面
		tHost.addTab(tHost.newTabSpec(identities[0]).setIndicator(layouts[0]).setContent(new Intent(this, CarServicesActivity.class)));
		tHost.addTab(tHost.newTabSpec(identities[1]).setIndicator(layouts[1]).setContent(new Intent(this, HomeNewActivity.class)));
		tHost.addTab(tHost.newTabSpec(identities[2]).setIndicator(layouts[2]).setContent(guanjiaIntent));
		tHost.addTab(tHost.newTabSpec(identities[3]).setIndicator(layouts[3]).setContent(new Intent(this, IndividualCenter.class)));
		tHost.setPadding(tHost.getPaddingLeft(), tHost.getPaddingTop(), tHost.getPaddingRight(), tHost.getPaddingBottom() - 5);
		tHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				updateTabBackground(tHost);
			}
		});
		tHost.setCurrentTab(currentTab);
		imageViews[currentTab].setImageResource(imagesActive[currentTab]);
        textViews[currentTab].setTextColor(getResources().getColor(R.color.daze_orangered5));
		if(mApplication.getUserId() == 0){
            if(mApplication.nRegistResult != KplusConstants.REGIST_RESULT_SUCCESS) {
                if(mApplication.nRegistResult == KplusConstants.REGIST_RESULT_FAIL){
                    Intent alertIntent = new Intent(MainUIActivity.this, AlertDialogActivity.class);
                    alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
                    alertIntent.putExtra("message", "设备绑定失败，请稍候重试");
                    startActivityForResult(alertIntent, REQUEST_FOR_BIND_FAIL);
                }
                else if(mApplication.nRegistResult == KplusConstants.REGIST_RESULT_NETWORK_DISCONNECT){
                    Intent alertIntent = new Intent(MainUIActivity.this, AlertDialogActivity.class);
                    alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
                    alertIntent.putExtra("message", "网络中断，请稍候重试");
                    startActivityForResult(alertIntent, REQUEST_FOR_NETWORK);
                }
                else if(mApplication.nRegistResult == KplusConstants.REGIST_RESULT_TIME_ERROR){
                    Intent alertIntent = new Intent(MainUIActivity.this, AlertDialogActivity.class);
                    alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
                    alertIntent.putExtra("message", "手机时间错误，请重新设置！");
                    startActivityForResult(alertIntent, REQUEST_FOR_DATE);
                }
                else if(mApplication.nRegistResult == KplusConstants.REGIST_RESULT_GET_DEVICE_ID_FAIL){
                    Intent alertIntent = new Intent(MainUIActivity.this, AlertDialogActivity.class);
                    alertIntent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
                    alertIntent.putExtra("message", "设备ID生成失败，请稍候重试");
                    startActivityForResult(alertIntent, REQUEST_FOR_BIND_FAIL);
                }
                else {
                    Intent intent = new Intent(this, WaitingRegit.class);
                    startActivityForResult(intent, Constants.REQUEST_TYPE_REGIST);
                }
            }
		}
		tabChangeReceiver = BroadcastReceiverUtil.createBroadcastReceiver(this);
		BroadcastReceiverUtil.registerReceiver(this, BroadcastReceiverUtil.ACTION_CHANGE_TAB, tabChangeReceiver);
		IntentFilter filter = new IntentFilter("finish");
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
		LocalBroadcastManager.getInstance(this).registerReceiver(tabreceiver, new IntentFilter("com.kplus.car.GexinSdkMsgReceiver.tab.service.change"));
		LocalBroadcastManager.getInstance(this).registerReceiver(newMessageReceiver, new IntentFilter("com.kplus.car.GexinSdkMsgReceiver.newmessage"));
		LocalBroadcastManager.getInstance(this).registerReceiver(locationChangeReceiver, new IntentFilter("com.kplus.car.location.changed"));
		getApplicationContext().registerReceiver(getUserIdReceiver, new IntentFilter(KplusConstants.ACTION_GET_USERID));
		startLocation();
		sp = getSharedPreferences("kplussp", MODE_PRIVATE);
		checkUpdate();
		try{
			if(getIntent().hasExtra("orderId")){
				int orderType = getIntent().getIntExtra("orderType", 0);
				long orderId = getIntent().getLongExtra("orderId", 0);
				if(orderType == 1){
					Intent intent = new Intent(MainUIActivity.this, OrderActivity.class);
					intent.putExtra("orderId", getIntent().getLongExtra("orderId", 0));
					startActivity(intent);
				}
				/**
				 * 洗车订单通知
				 */
				else if (orderType == 6) {
					CNCarWashingLogic.startOrderDetailsActivity(MainUIActivity.this, orderId, false);
				} else{
					Intent intent = new Intent(MainUIActivity.this, VehicleServiceActivity.class);
					intent.putExtra("appId", "10000009");
					intent.putExtra("startPage", "detail.html?orderId=" + orderId);
					startActivity(intent);
				}
			}
			else if (getIntent().hasExtra("pushType")){
				int pushType = getIntent().getIntExtra("pushType", 0);
				if (pushType == 10){
					String motionType = getIntent().getStringExtra("motionType");
					String motionValue = getIntent().getStringExtra("motionValue");
					String messageId = getIntent().getStringExtra("messageId");
					ServicesActionUtil action = new ServicesActionUtil(this);
					action.onClickAction(motionType, motionValue);
					if (!StringUtils.isEmpty(messageId)){
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("messageId", messageId);
						params.put("userId", String.valueOf(mApplication.getUserId()));
						new AppLogsTask(mApplication, "clickMessageBox", params).execute();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		getOpenTradeParam();
		getOpenImParam();
		if(mApplication.getUserId() != 0){
			urlSchema(getIntent());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		try{
			if(intent.hasExtra("orderId")){
				int orderType = intent.getIntExtra("orderType", 0);
				long orderId = intent.getLongExtra("orderId", 0);
				if(orderType == 1){
					Intent i = new Intent(MainUIActivity.this, OrderActivity.class);
					i.putExtra("orderId", getIntent().getLongExtra("orderId", 0));
					startActivity(i);
				}
				else{
					Intent i = new Intent(MainUIActivity.this, VehicleServiceActivity.class);
					i.putExtra("appId", "10000009");
					i.putExtra("startPage", "detail.html?orderId=" + orderId);
					startActivity(i);
				}
			}
			else if (intent.hasExtra("pushType")){
				int pushType = intent.getIntExtra("pushType", 0);
				if (pushType == 10){
					String motionType = intent.getStringExtra("motionType");
					String motionValue = intent.getStringExtra("motionValue");
					ServicesActionUtil action = new ServicesActionUtil(this);
					action.onClickAction(motionType, motionValue);
				}
			}
			else {
				urlSchema(intent);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onNewIntent(intent);
	}

	/**
	 * 更新Tab标签的背景图
	 *
	 * @param tabHost
	 */
	private void updateTabBackground(final TabHost tabHost) {
		for(int i = 0; i < 4; i++){
			if(i == tabHost.getCurrentTab()){
				imageViews[i].setImageResource(imagesActive[i]);
				textViews[i].setTextColor(getResources().getColor(R.color.daze_orangered5));
			}
			else{
				imageViews[i].setImageResource(images[i]);
				textViews[i].setTextColor(getResources().getColor(R.color.daze_darkgrey9));
			}
		}
		if(tabHost.getCurrentTab() == 0){
			if(ivIndecators[1].getVisibility() == View.VISIBLE){
				ivIndecators[1].setVisibility(View.GONE);
				mApplication.dbCache.putValue(KplusConstants.DB_KEY_TAB_SERVICE_CHANGE, "0");
			}
		}
		else if(tabHost.getCurrentTab() == 1){
			Intent it = new Intent("com.kplus.car.tab.changed");
			LocalBroadcastManager.getInstance(this).sendBroadcast(it);
		}
		else if(tabHost.getCurrentTab() == 2){
			EventAnalysisUtil.onEvent(MainUIActivity.this, "pageView_guanjia", "管家首页流量", null);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		long lastLoginTime = sp.getLong("lastLoginTime", 0);
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastLoginTime > 30*1000){
			sp.edit().putLong("lastLoginTime", currentTime);
		}
		String messageNumber = mApplication.dbCache.getValue(KplusConstants.DB_KEY_NEW_MESSAGE_NUMBER);
		int nMessageNumber = 0;
		if(!StringUtils.isEmpty(messageNumber)){
			try{
				nMessageNumber = Integer.parseInt(messageNumber);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(nMessageNumber <= 0)
			ivIndecators[3].setVisibility(View.GONE);
		else
			ivIndecators[3].setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy()
	{
		if(receiver != null)
			LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(tabreceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(newMessageReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(locationChangeReceiver);
		getApplicationContext().unregisterReceiver(getUserIdReceiver);
		BroadcastReceiverUtil.unRegisterReceiver(this, tabChangeReceiver);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_EXIT_APPLICATION){
			if(resultCode == RESULT_OK){
				MobclickAgent.onKillProcess(MainUIActivity.this);
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
        else if(requestCode == REQUEST_FOR_NETWORK || requestCode == REQUEST_FOR_BIND_FAIL){
            finish();
        }
        else if(requestCode == REQUEST_FOR_DATE){
            try{
                startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), REQUEST_FOR_SET_TIME);
            }
            catch(Exception e){
                e.printStackTrace();
                finish();
            }
        }
		else if (requestCode == Constants.REQUEST_TYPE_SWITCH_CITY){
			if (resultCode == RESULT_OK){
				mApplication.setAddress(mAddress);
				mApplication.setProvince(mProvince);
				mApplication.setCityName(mCityName);
			}
			mApplication.dbCache.putValue("rememberCityName", mApplication.getCityName());
		}
		else if (requestCode == Constants.REQUEST_TYPE_CITY) {
			// 选择城市
			if (resultCode == RESULT_OK) {
				ArrayList<CityVehicle> listTemp = data.getParcelableArrayListExtra("selectedCity");
				if (null != listTemp && !listTemp.isEmpty()) {
					CityVehicle cv = listTemp.get(0);
					String cityName = cv.getName();
					if (!TextUtils.isEmpty(cityName)) {
						mApplication.setCityName(cityName);
					}
					if (null != cv.getProvince()) {
						mApplication.setProvince(cv.getProvince());
					}
				}
			}
		}
		else if (requestCode == Constants.REQUEST_TYPE_REGIST) {
			if (resultCode == RESULT_OK) {
				checkUpdate();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (mApplication.isFromInApp){
				mApplication.isFromInApp = false;
				finish();
				return true;
			}
			Intent intent = new Intent(MainUIActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
			intent.putExtra("message", "是否要退出程序？");
			startActivityForResult(intent, REQUEST_FOR_EXIT_APPLICATION);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void getOpenTradeParam(){
		new AsyncTask<Void, Void, String>(){
			@Override
			protected String doInBackground(Void... params) {
				try{
					return MobclickAgent.getConfigParams(MainUIActivity.this, "useOpentrade");
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}

			protected void onPostExecute(String result) {
				if(!StringUtils.isEmpty(result) && result.equals("1")) {
					mApplication.setUseOpentrade(true);
				}
				else {
					mApplication.setUseOpentrade(false);
				}
			}
		}.execute();
	}

	private void getOpenImParam(){
		new AsyncTask<Void, Void, String>(){
			@Override
			protected String doInBackground(Void... params) {
				try{
					return MobclickAgent.getConfigParams(MainUIActivity.this, "useWKF");
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}

			protected void onPostExecute(String result) {
				if(!StringUtils.isEmpty(result) && result.equals("1")){
					mApplication.setUseWKF(true);
				}
				else{
					mApplication.setUseWKF(false);
				}
			}
		}.execute();
	}

	@Override
	public void onReceiverBroadcast(Intent data) {
		if(data != null){
			String action = data.getAction();
			if(action != null){
				if(action.equals(BroadcastReceiverUtil.ACTION_CHANGE_TAB)){
					int nCurrentTab = data.getIntExtra("currentTab", currentTab);
					if(nCurrentTab >= 0 && nCurrentTab < layouts.length){
						currentTab = nCurrentTab;
						tHost.setCurrentTab(currentTab);
					}
				}
			}
		}
	}

	private void getAdvertData(final String adType){
		new AsyncTask<Void, Void, GetAdvertDataResponse>(){
			@Override
			protected GetAdvertDataResponse doInBackground(Void... params) {
				GetAdvertDataRequest request = new GetAdvertDataRequest();
				if (!StringUtils.isEmpty(mApplication.getCityId())){
					try {
						request.setParams(Long.parseLong(mApplication.getCityId()), mApplication.getUserId(), mApplication.getId(),adType);
						return mApplication.client.execute(request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(GetAdvertDataResponse getAdvertDataResponse) {
				super.onPostExecute(getAdvertDataResponse);
				if(getAdvertDataResponse != null && getAdvertDataResponse.getCode() != null && getAdvertDataResponse.getCode() == 0){
					AdvertJson data = getAdvertDataResponse.getData();
					if(data != null){
						if(adType.equals(KplusConstants.ADVERT_HOME)) {
							List<Advert> advertsHome = data.getHome();
							if (advertsHome != null && !advertsHome.isEmpty()) {
								mApplication.setHomeAdvert((ArrayList<Advert>) advertsHome);
								startActivity(new Intent(MainUIActivity.this, PopAdvertActivity.class));
							}
						}
						else if (adType.equals(KplusConstants.ADVERT_NEW_USER)) {
							// 登录成功，新用户弹出的广告
							List<Advert> newUser = data.getNewUser();
							if (null != newUser && !newUser.isEmpty()) {
								mApplication.setNewUserAdvert(newUser);
								// 新用户广告延迟几秒再打开
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										Intent intent = new Intent(MainUIActivity.this, PopAdvertActivity.class);
										intent.putExtra(PopAdvertActivity.KEY_VALUE_TYPE, KplusConstants.ADVERT_NEW_USER);
										startActivity(intent);
									}
								}, 2500);
							}
						}
					}
				}
			}
		}.execute();
	}

	private void startLocation() {
		mLocationListener = new MyLocationListener();
		mLocationClient = new LocationClient(mApplication);
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setOpenGps(false); // 打开gps
		option.setIsNeedAddress(true);
		option.setAddrType("all");
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(final BDLocation location) {
			if (location == null) {
				chooseCity();
				return;
			}
			mLocationClient.unRegisterLocationListener(mLocationListener);
			mLocationClient.stop();
			mApplication.setLocation(location);
			mApplication.setLocationProvince(location.getProvince());
			String cityName = location.getCity();
			if (cityName != null) {
				cityName = cityName.replace("市", "");
				mApplication.setLocationCityName(cityName);
				if (StringUtils.isEmpty(mApplication.getCityName())) {
					mApplication.setAddress(location.getAddrStr());
					mApplication.setProvince(location.getProvince());
					mApplication.setCityName(cityName);
				}
				else if (!mApplication.getCityName().equals(cityName)) {
					String rememberCityName = mApplication.dbCache.getValue("rememberCityName");
					if (!mApplication.getCityName().equals(rememberCityName)){
						mCityName = cityName;
						mProvince = location.getProvince();
						mAddress = location.getAddrStr();
						Intent alertIntent = new Intent(MainUIActivity.this, AlertDialogActivity.class);
						alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
						alertIntent.putExtra("message", "你目前在" + cityName + "，是否切换城市？");
						alertIntent.putExtra("leftButtonText", "不");
						alertIntent.putExtra("rightButtonText", "切换");
						startActivityForResult(alertIntent, Constants.REQUEST_TYPE_SWITCH_CITY);
					}
				}
				getAdvertData(KplusConstants.ADVERT_HOME);
			}
			else {
				new AsyncTask<Void, Void, String>(){
					@Override
					protected String doInBackground(Void... voids) {
						String resultData = "";
						URL url = null;
						try {
							url = new URL("http://api.map.baidu.com/geocoder/v2/?ak=Vi966H8myjmiBXB8okFa6nW6&mcode=4E:0E:6F:FF:9A:1B:A2:DF:53:C2:AF:EC:00:CC:1C:99:19:BF:13:B1;com.kplus.car&location=" + location.getLatitude() + "," + location.getLongitude() + "&output=json");
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						if (url != null){
							try {
								HttpURLConnection connection = (HttpURLConnection) url.openConnection();
								InputStream stream = connection.getInputStream();
								InputStreamReader in = new InputStreamReader(stream);
								BufferedReader buffer = new BufferedReader(in);
								String line;
								while((line = buffer.readLine()) != null){
									resultData += line + "\n";
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						return resultData;
					}

					@Override
					protected void onPostExecute(String s) {
						try {
							JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
							int status = jsonObject.get("status").getAsInt();
							if (status == 0){
								jsonObject = jsonObject.get("result").getAsJsonObject();
								mAddress = jsonObject.get("formatted_address").getAsString();
								long cityCode = jsonObject.get("cityCode").getAsLong();
								jsonObject = jsonObject.get("addressComponent").getAsJsonObject();
								String cityName = jsonObject.get("city").getAsString();
								mApplication.setLocationProvince(jsonObject.get("province").getAsString());
								if (!TextUtils.isEmpty(cityName) && cityCode != 0){
									cityName = cityName.replace("市", "");
									mApplication.setLocationCityName(cityName);
									if (StringUtils.isEmpty(mApplication.getCityName())) {
										mApplication.setProvince(jsonObject.get("province").getAsString());
										mApplication.setAddress(mAddress);
										mApplication.setCityName(cityName);
									}
									else if (!mApplication.getCityName().equals(cityName)){
										String rememberCityName = mApplication.dbCache.getValue("rememberCityName");
										if (!mApplication.getCityName().equals(rememberCityName)){
											mCityName = cityName;
											mProvince = jsonObject.get("province").getAsString();
											Intent alertIntent = new Intent(MainUIActivity.this, AlertDialogActivity.class);
											alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
											alertIntent.putExtra("message", "你目前在" + cityName + "，是否切换城市？");
											alertIntent.putExtra("leftButtonText", "不");
											alertIntent.putExtra("rightButtonText", "切换");
											startActivityForResult(alertIntent, Constants.REQUEST_TYPE_SWITCH_CITY);
										}
									}
									getAdvertData(KplusConstants.ADVERT_HOME);
								} else {
									// 取到空位置，表示失败了
									chooseCity();
								}
							}
						} catch (Exception e){
							chooseCity();
							e.printStackTrace();
						}
					}
				}.execute();
			}
		}
	}

	private void chooseCity() {
		String cityName = mApplication.getCityName();
		// 没有定位或设置城市要让用户选择
		if (TextUtils.isEmpty(cityName)) {
			// 这里要打开城市选择列表，说明定位失败，汽车服务中要通知显示定位失败界面
			Intent data = new Intent("com.kplus.car.location.changed");
			LocalBroadcastManager.getInstance(this).sendBroadcastSync(data);

			Intent intent = new Intent(this, CitySelectActivity.class);
			intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_HOME);
			intent.putExtra("cityName", mApplication.getCityName());
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivityForResult(intent, Constants.REQUEST_TYPE_CITY);
		}
	}

	private class NoticeComparator implements Comparator<NoticeContent> {
		@Override
		public int compare(NoticeContent o1, NoticeContent o2) {
			if(o1.getNoticeTime().equals(o2.getNoticeTime()))
				return o2.getReadflag() - o1.getReadflag();
			return o2.getNoticeTime().compareToIgnoreCase(o1.getNoticeTime());
		}
	}

	private void urlSchema(Intent intent){
		String action = intent.getAction();
		if(action != null && action.equals(Intent.ACTION_VIEW)){
			try {
				String ds = intent.getDataString();
				if(ds != null){
					ds = ds.toLowerCase();
//					Log.e("dsdsdsds", ds);
					if(ds.contains("huaweivoice://com.huawei.vassistant/vehicle/rechargegascard")){
						Intent i = new Intent(MainUIActivity.this, VehicleServiceActivity.class);
						i.putExtra("appId", "10000005");
						startActivity(i);
						return;
					}
					else if(ds.contains("huaweivoice://com.huawei.vassistant/vehicle/checkpenalty")){
						Uri uri = intent.getData();
						if (uri != null){
							String type = uri.getQueryParameter("type");
							if(type != null){
								if(type.equalsIgnoreCase("vehicle")){
									currentTab = 1;
									tHost.setCurrentTab(currentTab);
									imageViews[currentTab].setImageResource(imagesActive[currentTab]);
									textViews[currentTab].setTextColor(getResources().getColor(R.color.daze_orangered5));
									List<UserVehicle> luv = mApplication.dbCache.getVehicles();
									if(luv != null && !luv.isEmpty()){
										Intent it = new Intent(MainUIActivity.this, VehicleDetailActivity.class);
										it.putExtra("vehicleNumber", luv.get(0).getVehicleNum());
										startActivity(it);
									}
									else{
										startActivity(new Intent(MainUIActivity.this, VehicleAddNewActivity.class));
									}
									return;
								}
								else if (type.equalsIgnoreCase("driverId")){
									currentTab = 1;
									tHost.setCurrentTab(currentTab);
									imageViews[currentTab].setImageResource(imagesActive[currentTab]);
									textViews[currentTab].setTextColor(getResources().getColor(R.color.daze_orangered5));
									List<Jiazhao> ljz = mApplication.dbCache.getJiazhaos();
									if(ljz != null && !ljz.isEmpty()){
										Intent it = new Intent(MainUIActivity.this, JiazhaoListActivity.class);
										intent.putExtra("check", true);
										List<String> lTemp = new ArrayList<String>();
										for(Jiazhao jz : ljz)
											lTemp.add(jz.getId());
										intent.putStringArrayListExtra("refreshingList", (ArrayList<String>) lTemp);
										startActivity(it);
									}
									else{
										startActivity(new Intent(MainUIActivity.this, JiazhaoEditActivity.class));
									}
									return;
								}
							}
						}
					}
				}
				Uri uri = intent.getData();
				if (uri != null) {
					String oper = uri.getQueryParameter("oper");
					if (oper != null && oper.equals("show")) {
						mApplication.isFromInApp = true;
						String type = uri.getQueryParameter("type");
						if (type != null && type.equals("h5")) {
							String appId = uri.getQueryParameter("appId");
							String startPage = uri.getQueryParameter("startPage");
							String serviceId = uri.getQueryParameter("serviceId");
							String serviceName = uri.getQueryParameter("serviceName");
							Intent i = new Intent(MainUIActivity.this, VehicleServiceActivity.class);
							i.putExtra("appId", appId);
							if(!StringUtils.isEmpty(startPage)){
								if (!StringUtils.isEmpty(serviceId) && !StringUtils.isEmpty(serviceName))
									startPage += ("?serviceId=" + serviceId + "&serviceName=" + serviceName);
								i.putExtra("startPage", startPage);
							}
							currentTab = 0;
							tHost.setCurrentTab(currentTab);
							startActivity(i);
						}
						else if (type != null && type.equals("native")){
							String className = uri.getQueryParameter("className");
							if(className != null && className.equals("auth")){
								String vehicleNumber = uri.getQueryParameter("vehicleNumber");
								Intent i = new Intent(MainUIActivity.this, EmergencyLicenseActivity.class);
								i.putExtra("vehicleNumber", vehicleNumber);
								startActivity(i);
							}
							else if (className != null && className.equals("home")){
								currentTab = 1;
								tHost.setCurrentTab(currentTab);
							}
							else if (className != null && className.equals("wash")){
								CNCarWashingLogic.startCarWashingActivity(MainUIActivity.this, false);
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void checkUpdate(){
		if(mApplication.getUserId() != 0){
			int year = sp.getInt("updateCheckYear", 0);
			int mounth = sp.getInt("updateCheckMounth", 0);
			int day = sp.getInt("updateCheckDay", 0);
			Calendar calendar = Calendar.getInstance();
			if(year != calendar.get(Calendar.YEAR) || mounth != calendar.get(Calendar.MONTH) || day != calendar.get(Calendar.DAY_OF_MONTH)){
				ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if(ni != null && cm.getBackgroundDataSetting()){
					if(ni.getType() == ConnectivityManager.TYPE_WIFI && ni.isConnected()){
						UpdateManager um = new UpdateManager(this);
						um.checkUpdate();
						sp.edit().putInt("updateCheckYear", calendar.get(Calendar.YEAR)).commit();
						sp.edit().putInt("updateCheckMounth", calendar.get(Calendar.MONTH)).commit();
						sp.edit().putInt("updateCheckDay", calendar.get(Calendar.DAY_OF_MONTH)).commit();
						return;
					}
				}
			}
		}
		if(KplusConstants.CLIENT_APP_KEY != null && (KplusConstants.CLIENT_APP_KEY.equals("30004")
			|| KplusConstants.CLIENT_APP_KEY.equals("30006") || KplusConstants.CLIENT_APP_KEY.equals("30012")
		    || KplusConstants.CLIENT_APP_KEY.equals("30017"))){
			BDAutoUpdateSDK.uiUpdateAction(MainUIActivity.this, new UICheckUpdateCallback() {
				@Override
				public void onCheckComplete() {

				}
			});
		}
	}
}
