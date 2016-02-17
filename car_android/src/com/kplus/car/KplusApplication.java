package com.kplus.car;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.wxlib.util.SysUtil;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.igexin.sdk.PushManager;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.asynctask.UpdateClientCityTask;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.comm.DBCache;
import com.kplus.car.comm.FileCache;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.comm.TaskInfo;
import com.kplus.car.model.Advert;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.ProviderInfo;
import com.kplus.car.model.VehicleBrand;
import com.kplus.car.model.json.ProviderInfoJson;
import com.kplus.car.model.response.ClientRegistResponse;
import com.kplus.car.model.response.GetProviderByOpenUserIdResponse;
import com.kplus.car.model.response.request.ClientRegistRequest;
import com.kplus.car.model.response.request.GetProviderByOpenUserIdRequest;
import com.kplus.car.service.InitService;
import com.kplus.car.service.UpgradeDataService;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.MD5;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXAuthListener;
import com.kplus.car.wxapi.WXPayListener;
import com.kplus.car.wxapi.WXShareListener;
import com.lotuseed.android.Lotuseed;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KplusApplication extends CNCarWashApp {

    public static String TAG = "kplus";

    public Context context;

    private long id;//用户ID
    private long userId;//设备ID
    private long pId;
    private String mContactphone = null;

    private String cityId;
    private String cityName;
    private String address;
    private String province;

    private List<CityVehicle> cities;
    private Map<Long, CityVehicle> cityMap;

    private List<VehicleBrand> brands;

    public Client client;

    public FileCache fileCache;

    public ImageLoader imageLoader;

    public static final Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

    public DBCache dbCache;

    private static KplusApplication instance;

    public boolean m_bKeyRight = true;
    public boolean isTabNeedSwitch = false;
    public boolean isFromInApp = false;

    public TelephonyManager telephonyManager;
    public NotificationManager notificationManager;
    public ActivityManager activityManager;
    public String packageName;

    private BDLocation location;

    private PendingIntent remindPendingIntent = null;
    private boolean hasSuccessShare = false;
    private int nScreenWidth, nScreenHeight;
    public int apkVersionCode;
    private Handler mHandler = new Handler();
    private String clientId;
    public YWIMKit mYWIMKIT;
    public int nRegistResult = KplusConstants.REGIST_RESULT_IDEL;
    public static long sUserId = 0;

    public Typeface mDin;
    public Typeface mRegular;

    public static KplusApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initEngineManager();
        /**
         * 继承了MutiDexApplication或者覆写了Application中的attachBaseContext()方法
         * Application 中的静态全局变量会比MutiDex的 instal()方法优先加载，所以建议
         * 避免在Application类中使用main classes.dex文件中可能使用到的静态变量，可以
         * 根据如下所示的方式进行修改
         */
        final Context context = this;
        new Runnable() {
            @Override
            public void run() {
                // 4.0以后实现
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    registerActivityLifecycleCallbacks(new DazeActivityLifeCycleCallback());
                }

                fileCache = new FileCache(context);
                client = new Client(KplusApplication.this, fileCache);
                imageLoader = ImageLoader.getInstance();
                dbCache = new DBCache(context);
                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                packageName = context.getPackageName();
                PackageManager pm = getPackageManager();
                try {
                    PackageInfo pi = pm.getPackageInfo(packageName, 0);
                    apkVersionCode = pi.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                initImageLoader();
                initFont();
                instance = KplusApplication.this;
                setCarWashingListener();

                KplusConstants.initData(getApplicationContext());

                Context appContext =  getApplicationContext();
                CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(appContext); //App的策略Bean
                strategy.setAppChannel(KplusConstants.appChannel);     //设置渠道

                Log.trace(TAG, "当前使用的渠道是：" + KplusConstants.appChannel + "，使用的AppKey是：" + KplusConstants.CLIENT_APP_KEY);
                Log.trace(TAG, "\r\n主客户端的api_url：" + BuildConfig.API_URL_MAIN_APP + "\r\n洗车的api_url：" + BuildConfig.API_URL_CAR_WASHING);

                strategy.setAppVersion("" + apkVersionCode);      //App的版本
                strategy.setAppReportDelay(5000);  //设置SDK处理延时，毫秒
                String appId = "900001325";
                boolean isDebug = false ;
                CrashReport. initCrashReport(appContext, appId, isDebug);  //初始化SDK

                PushManager.getInstance().initialize(getApplicationContext());
                GexinSdkMsgReceiver.nClientIdNUmber = 0;
                if(getUserId() == 0){
                    regist();
                }
                else{
                    startService(new Intent(KplusApplication.this, InitService.class));
                    startService(new Intent(KplusApplication.this, UpgradeDataService.class));
                }
                MobclickAgent.updateOnlineConfig(getApplicationContext());
            }
        }.run();
    }

    /**
     * 设置用户余额
     *
     * @param cashBalance 余额
     */
    public void setCarWashingUserBalance(float cashBalance) {
        AppBridgeUtils.getIns().setUserBalance(new BigDecimal(cashBalance));
    }

    /**
     * 设置用户手机号
     *
     * @param mobile
     */
    public void setCarWashingMobile(String mobile) {
        AppBridgeUtils.getIns().setMobile(mobile);
    }

    /**
     * 设置车牌
     *
     * @param license
     */
    public void setCarWashingLicense(String license) {
        AppBridgeUtils.getIns().setCarLicense(license);
    }

    /**
     * 设置支付的回调监听
     */
    public void setCarWashingListener() {
        AppBridgeAccessManager accessManager = new AppBridgeAccessManager();
        AppBridgeUtils.getIns().setBridgeListener(accessManager);
    }

    public void initTaobao() {
        if (mYWIMKIT == null) {
            SysUtil.setApplication(this);
            if(SysUtil.isTCMSServiceProcess(this))
                return;
            String APP_KEY = "23080156";
            YWAPI.init(this, APP_KEY);
            mYWIMKIT = YWAPI.getIMKitInstance();
            List<String> keys = new ArrayList<String>();
            keys.add("23080156");
            keys.add("23163397");
            YWAPI.prepareTargetAppKeys(keys);
            mYWIMKIT.setEnableNotification(true);
            mYWIMKIT.setAppName("橙牛汽车管家");
            mYWIMKIT.setResId(R.drawable.daze_notification_icon);
        }
    }

    private Map<String, ProviderInfo> openImUsers;

    public Map<String, ProviderInfo> getOpenImUsers() {
        if (openImUsers == null) {
            List<ProviderInfo> providerInfos = dbCache.getProviderInfos();
            if (providerInfos != null && !providerInfos.isEmpty()) {
                openImUsers = new HashMap<String, ProviderInfo>();
                for (ProviderInfo providerInfo : providerInfos) {
                    openImUsers.put(providerInfo.getOpenUserId(), providerInfo);
                }
            }
        }
        return openImUsers == null ? new HashMap<String, ProviderInfo>() : openImUsers;
    }

    public void setOpenImUsers(Map<String, ProviderInfo> openImUsers) {
        this.openImUsers = openImUsers;
    }

    public void loginTaobao(final String userid, String password) {
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
        IYWLoginService mLoginService = mYWIMKIT.getLoginService();
        mLoginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                mYWIMKIT.getIMCore().getContactManager().setContactProfileCallback(new IYWContactProfileCallback() {
                    @Override
                    public IYWContact onFetchContactInfo(final String s) {
                        if (s != null && !s.equals(userid)) {
                            if (openImUsers != null && openImUsers.containsKey(s)) {
                                return new IYWContact() {
                                    @Override
                                    public String getUserId() {
                                        return s;
                                    }

                                    @Override
                                    public String getAppKey() {
                                        return "23163397";
                                    }

                                    @Override
                                    public String getAvatarPath() {
                                        return null;
                                    }

                                    @Override
                                    public String getShowName() {
                                        return openImUsers.get(s).getName();
                                    }
                                };
                            } else {
                                getPeoviderByOpenUserId(s);
                            }
                        }
                        return null;
                    }

                    @Override
                    public Intent onShowProfileActivity(String s) {
                        return null;
                    }
                });
            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void getPeoviderByOpenUserId(final String openUserId) {
        new AsyncTask<Void, Void, GetProviderByOpenUserIdResponse>() {
            @Override
            protected GetProviderByOpenUserIdResponse doInBackground(Void... params) {
                GetProviderByOpenUserIdRequest request = new GetProviderByOpenUserIdRequest();
                request.setParams(openUserId);
                try {
                    return client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(GetProviderByOpenUserIdResponse response) {
                super.onPostExecute(response);
                if (response != null && response.getCode() != null && response.getCode() == 0) {
                    ProviderInfoJson data = response.getData();
                    if (data != null) {
                        List<ProviderInfo> listTemp = data.getList();
                        if (listTemp != null && !listTemp.isEmpty()) {
                            ProviderInfo pi = listTemp.get(0);
                            if (pi != null) {
                                getOpenImUsers().put(openUserId, pi);
                                dbCache.saveProviderInfo(pi);
                            }
                        }
                    }
                }
            }
        }.execute();
    }

    public void logOutTaobao() {
        if (mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success) {
            mYWIMKIT.getIMCore().logout(new IWxCallback() {
                @Override
                public void onSuccess(Object... arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgress(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int arg0, String arg1) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }

    private void initFont(){
        mDin = Typeface.createFromAsset(getAssets(), "fonts/DIN Condensed Bold.ttf");
        mRegular = Typeface.createFromAsset(getAssets(), "fonts/HYQiH2312F45.ttf");
    }

    public void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        imageLoader.init(config);
    }

    public void initEngineManager() {
        SDKInitializer.initialize(this);
    }

    public long getUserId() {
        if (userId == 0) {
            String _userId = dbCache.getValue(KplusConstants.DB_KEY_USER);
            userId = _userId == null ? 0 : Long.parseLong(_userId);
        }
        sUserId = userId;
        return userId;
    }

    public long getpId() {
        try {
            if (pId == 0) {
                String _pId = dbCache.getValue(KplusConstants.DB_KEY_PHONEID);
                pId = _pId == null ? 0 : Long.parseLong(_pId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            pId = 0;
        }
//		if(pId == 0){
//			Account pAccount = dbCache.getAccountByType(0);
//			if(pAccount != null){
//				if(pAccount.getPid() != null){
//					pId = pAccount.getPid();
//					if(pId != 0)
//						setpId(pId);
//					if(!StringUtils.isEmpty(pAccount.getUserName()))
//						dbCache.putValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE, pAccount.getUserName());
//				}
//			}
//		}
        return pId;
    }

    public String getContactphone() {
        if (TextUtils.isEmpty(mContactphone)) {
            try {
                mContactphone = dbCache.getValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mContactphone;
    }

    public void regist() {
        nRegistResult = KplusConstants.REGIST_RESULT_REGISTING;
        new Thread() {
            public void run() {
                Intent i = new Intent(KplusConstants.ACTION_GET_USERID);
                if (!client.isNetWorkConnected()) {
                    nRegistResult = KplusConstants.REGIST_RESULT_NETWORK_DISCONNECT;
                    i.putExtra("isNetWorkDisconnected", true);
                    i.putExtra("issuccess", false);
                    sendBroadcast(i);
                    return;
                }
                long netTime = DateUtil.getTimeFromWeb();
                if (netTime != 0) {
                    long time = System.currentTimeMillis();
                    if (Math.abs(netTime - time) > 5 * 60 * 1000) {
                        nRegistResult = KplusConstants.REGIST_RESULT_TIME_ERROR;
                        i.putExtra("issuccess", false);
                        i.putExtra("isTimeError", true);
                        sendBroadcast(i);
                        return;
                    }
                }
                try {
                    if (StringUtils.isEmpty(clientId)) {
                        String deviceId = telephonyManager.getDeviceId();
                        deviceId = (deviceId == null ? "" : deviceId);
                        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        String macAddress = wm.getConnectionInfo().getMacAddress();
                        macAddress = (macAddress == null ? "" : macAddress);
                        if(!StringUtils.isEmpty(deviceId) || !StringUtils.isEmpty(macAddress)){
                            clientId = MD5.md5(deviceId + macAddress);
                        }
//                        if (telephonyManager.getDeviceId() != null)
//                            clientId = telephonyManager.getDeviceId();
//                        else {
//                            WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                            if (wm.getConnectionInfo().getMacAddress() != null) {
//                                clientId = wm.getConnectionInfo().getMacAddress();
//                            }
//                        }
                        if (StringUtils.isEmpty(clientId)) {
                            String uuidPath = FileUtil.getAppRootPath() + "uuid.txt";
                            File file = new File(uuidPath);
                            if (file.exists()) {
                                InputStream is = new FileInputStream(file);
                                int length = is.available();
                                byte[] buffer = new byte[length];
                                is.read(buffer);
                                clientId = EncodingUtils.getString(buffer, "utf-8");
                                is.close();
                            } else {
                                file.createNewFile();
                                clientId = java.util.UUID.randomUUID().toString();
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(clientId.getBytes("utf-8"));
                                fos.flush();
                                fos.close();
                            }
                        }
                    }
                    if (StringUtils.isEmpty(clientId)) {
                        nRegistResult = KplusConstants.REGIST_RESULT_GET_DEVICE_ID_FAIL;
                        i.putExtra("issuccess", false);
                        i.putExtra("createIdSuccess", false);
                    } else {
                        ClientRegistRequest request = new ClientRegistRequest();
                        request.setParams(clientId, "android", android.os.Build.VERSION.RELEASE, "" + apkVersionCode);
                        final ClientRegistResponse response = client.execute(request);
                        if (response != null && response.getCode() != null && response.getCode() == 0) {
                            long userId = response.getData().getUserId();
                            if (userId != 0) {
                                nRegistResult = KplusConstants.REGIST_RESULT_SUCCESS;
                                i.putExtra("issuccess", true);
                                setUserId(userId);
                                dbCache.putValue(KplusConstants.DB_KEY_CLIENT, clientId);
                                startService(new Intent(KplusApplication.this, InitService.class));
                            } else {
                                nRegistResult = KplusConstants.REGIST_RESULT_FAIL;
                                i.putExtra("issuccess", false);
                            }
                        } else {
                            nRegistResult = KplusConstants.REGIST_RESULT_FAIL;
                            i.putExtra("issuccess", false);
                        }
                    }
                } catch (Exception e) {
                    nRegistResult = KplusConstants.REGIST_RESULT_FAIL;
                    e.printStackTrace();
                    i.putExtra("issuccess", false);
                } finally {
                    sendBroadcast(i);
                }
            }
        }.start();
    }

    public void setUserId(long userId) {
        this.userId = userId;
        sUserId = userId;
        dbCache.putValue(KplusConstants.DB_KEY_USER, "" + userId);
    }

    public void setpId(long pId) {
        this.pId = pId;
        dbCache.putValue(KplusConstants.DB_KEY_PHONEID, "" + pId);
    }

    public String getCityId() {
        if (StringUtils.isEmpty(cityId)) {
            cityId = dbCache.getValue(KplusConstants.DB_KEY_CITY_ID);
        }
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
        String oldCityId = dbCache.getValue(KplusConstants.DB_KEY_CITY_ID);
        if (!TextUtils.isEmpty(cityId) && !cityId.equals(oldCityId)) {
            dbCache.putValue(KplusConstants.DB_KEY_CITY_ID, cityId);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    new UpdateClientCityTask(KplusApplication.this).execute();
                }
            });
            Intent intent = new Intent("com.kplus.car.location.changed");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    public String getCityName() {
        if (StringUtils.isEmpty(cityName)) {
            cityName = dbCache.getValue(KplusConstants.DB_KEY_CITY_NAME);
        }
        return cityName;
    }

    public void setCityName(final String cityName) {
        if (cityName == null)
            return;
        this.cityName = cityName;
        dbCache.putValue(KplusConstants.DB_KEY_CITY_NAME, cityName);
        if (getCities() != null && !getCities().isEmpty()) {
            for (CityVehicle cv : getCities()) {
                if (cv.getName() != null && cv.getName().contains(cityName)) {
                    setCityId("" + cv.getId());
                    setProvince(cv.getProvince());
                    break;
                }
            }
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCityName(cityName);
                }
            }, 5 * 1000);
        }
    }

    public String getProvince() {
        if (StringUtils.isEmpty(province)) {
            province = dbCache.getValue(KplusConstants.DB_KEY_PROVINCE);
        }
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
        dbCache.putValue(KplusConstants.DB_KEY_PROVINCE, province);
    }

    public String getAddress() {
        if (StringUtils.isEmpty(address)) {
            address = dbCache.getValue(KplusConstants.DB_KEY_ADDRESS);
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        dbCache.putValue(KplusConstants.DB_KEY_ADDRESS, address);
    }

    public Map<Long, CityVehicle> getCityMap() {
        if (cityMap == null) {
            cityMap = new HashMap<Long, CityVehicle>();
            for (CityVehicle cityVehicle : getCities()) {
                cityMap.put(cityVehicle.getId(), cityVehicle);
            }
        }
        return cityMap;
    }

    public List<CityVehicle> getCities() {
        if (cities == null) {
            cities = dbCache.getCityVehicles();
        }
        return cities;
    }

    public void setCities(List<CityVehicle> cities) {
        this.cities = cities;
        dbCache.saveCityVehicles(cities);
    }

    public void updateCities(List<CityVehicle> _cities) {
        if (_cities != null && !_cities.isEmpty()) {
            if (cities != null && !cities.isEmpty()) {
                for (CityVehicle _city : _cities) {
                    long id = _city.getId().longValue();
                    for (CityVehicle city : cities) {
                        if (city.getId().longValue() == id) {
                            city.setFrameNumLen(_city.getFrameNumLen());
                            city.setMotorNumLen(_city.getMotorNumLen());
                            city.setAccountLen(_city.getAccountLen());
                            city.setPasswordLen(_city.getPasswordLen());
                            city.setMotorvehiclenumLen(_city.getMotorvehiclenumLen());
                            city.setHot(_city.getHot());
                            city.setOwner(_city.getOwner());
                            city.setValid(_city.getValid());
                            break;
                        }
                    }
                }
            }
        }
    }

    public List<VehicleBrand> getBrands() {
        if (brands == null) {
            brands = new ArrayList<VehicleBrand>();
        }
        return brands;
    }

    public void setBrands(List<VehicleBrand> brands) {
        this.brands = brands;
    }

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }

    public PendingIntent getRemindPendingIntent() {
        return remindPendingIntent;
    }

    public void setRemindPendingIntent(PendingIntent remindPendingIntent) {
        this.remindPendingIntent = remindPendingIntent;
    }

    public boolean isHasSuccessShare() {
        return hasSuccessShare;
    }

    public void setHasSuccessShare(boolean hasSuccessShare) {
        this.hasSuccessShare = hasSuccessShare;
    }

    public int getnScreenWidth() {
        if (nScreenWidth == 0) {
            nScreenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        }
        return nScreenWidth;
    }

    public void setnScreenWidth(int nScreenWidth) {
        this.nScreenWidth = nScreenWidth;
    }

    public int getnScreenHeight() {
        if (nScreenHeight == 0) {
            nScreenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        }
        return nScreenHeight;
    }

    public void setnScreenHeight(int nScreenHeight) {
        this.nScreenHeight = nScreenHeight;
    }

    public long getId() {
        if (id == 0) {
            try {
                String strId = dbCache.getValue(KplusConstants.DB_KEY_USERID);
                id = StringUtils.isEmpty(strId) ? 0 : Long.parseLong(strId);
            } catch (Exception e) {
                e.printStackTrace();
                id = 0;
            }
        }
        return id;
    }

    public void setId(long id) {
        this.id = id;
        dbCache.putValue(KplusConstants.DB_KEY_USERID, "" + id);
    }

    private Map<String, TaskInfo> tasks = null;
    private Object taskLock = new Object();

    public Map<String, TaskInfo> getTasks() {
        synchronized (taskLock) {
            if (tasks == null)
                tasks = new HashMap<String, TaskInfo>();
        }
        return tasks;
    }

    public void addTasks(String vehicleNumber, long updateTime) {
        synchronized (taskLock) {
            if (tasks == null)
                tasks = new HashMap<String, TaskInfo>();
            TaskInfo task = new TaskInfo();
            task.setVehicleNumber(vehicleNumber);
            task.setUpdateTime(updateTime);
            task.setnCount(1);
            task.setStartTime(System.currentTimeMillis());
            tasks.put(vehicleNumber, task);
        }
    }

    public void removeTask(String vehicleNumber) {
        synchronized (taskLock) {
            if (tasks != null) {
                tasks.remove(vehicleNumber);
            }
        }
    }

    public boolean containsTask(String vehicleNumber) {
        boolean result = false;
        synchronized (taskLock) {
            result = (tasks != null) && tasks.containsKey(vehicleNumber);
        }
        return result;
    }

    public TaskInfo getTask(String vehicleNumber) {
        TaskInfo result = null;
        synchronized (taskLock) {
            if (tasks != null)
                result = tasks.get(vehicleNumber);
        }
        return result;
    }

    public void updateTask(String vehicleNumber, TaskInfo taskInfo) {
        synchronized (taskLock) {
            if (tasks != null) {
                tasks.remove(vehicleNumber);
                tasks.put(vehicleNumber, taskInfo);
            }
        }
    }

    private WXShareListener wxShareListener;

    public WXShareListener getWxShareListener() {
        return wxShareListener;
    }

    public void setWxShareListener(WXShareListener wxShareListener) {
        this.wxShareListener = wxShareListener;
    }

    private WXPayListener wxPayListener;

    public WXPayListener getWxPayListener() {
        return wxPayListener;
    }

    public void setWxPayListener(WXPayListener wxPayListener) {
        this.wxPayListener = wxPayListener;
    }

    private WXAuthListener wxAuthListener;

    public WXAuthListener getWxAuthListener() {
        return wxAuthListener;
    }

    public void setWxAuthListener(WXAuthListener wxAuthListener) {
        this.wxAuthListener = wxAuthListener;
    }

    private String openImUserId;
    private String openImPassWord;

    public String getOpenImUserId() {
        if (StringUtils.isEmpty(openImUserId)) {
            openImUserId = dbCache.getValue("openImUserId" + getId());
        }
        return openImUserId;
    }

    public void setOpenImUserId(String openImUserId) {
        dbCache.putValue("openImUserId" + getId(), openImUserId);
        this.openImUserId = openImUserId;
    }

    public String getOpenImPassWord() {
        if (StringUtils.isEmpty(openImPassWord)) {
            openImPassWord = dbCache.getValue("openImPassword" + getId());
        }
        return openImPassWord;
    }

    public void setOpenImPassWord(String openImPassWord) {
        dbCache.putValue("openImPassword" + getId(), openImPassWord);
        this.openImPassWord = openImPassWord;
    }

    /**
     * 判断用户是否已登录
     *
     * @param isShowToast true 显示toast提示用户，不显示toast直接打开登录界面
     * @param tips        提示文字 isShowToast 为false时可传null
     * @return true 已登录，false 未登录，且打开登录界面
     */
    public boolean isUserLogin(boolean isShowToast, String tips) {
        if (this.getId() == 0) {
            // 用户未登录
            if (isShowToast) {
                ToastUtil.TextToast(this, tips, Toast.LENGTH_SHORT, Gravity.CENTER);
            }
            Intent intent = new Intent(this, PhoneRegistActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("isMustPhone", true);
            this.startActivity(intent);
            return false;
        }
        return true;
    }

    private boolean useOpentrade = false;

    public boolean isUseOpentrade() {
        return useOpentrade;
    }

    public void setUseOpentrade(boolean useOpentrade) {
        this.useOpentrade = useOpentrade;
    }

    private boolean useWKF = false;

    public boolean isUseWKF() {
        return useWKF;
    }

    public void setUseWKF(boolean useWKF) {
        this.useWKF = useWKF;
    }

    private ArrayList<Advert> vehicleBodyAdvert;

    public ArrayList<Advert> getVehicleBodyAdvert() {
        return vehicleBodyAdvert;
    }

    public void setVehicleBodyAdvert(ArrayList<Advert> vehicleBodyAdvert) {
        this.vehicleBodyAdvert = vehicleBodyAdvert;
    }

    private ArrayList<Advert> vehicleDetailHeadAdvert;

    public ArrayList<Advert> getVehicleDetailHeadAdvert() {
        return vehicleDetailHeadAdvert;
    }

    public void setVehicleDetailHeadAdvert(ArrayList<Advert> vehicleDetailHeadAdvert) {
        this.vehicleDetailHeadAdvert = vehicleDetailHeadAdvert;
    }

    private ArrayList<Advert> userBodyAdvert;

    public ArrayList<Advert> getUserBodyAdvert() {
        return userBodyAdvert;
    }

    public void setUserBodyAdvert(ArrayList<Advert> userBodyAdvert) {
        this.userBodyAdvert = userBodyAdvert;
    }

    private ArrayList<Advert> homeAdvert;

    public ArrayList<Advert> getHomeAdvert() {
        return homeAdvert;
    }

    public void setHomeAdvert(ArrayList<Advert> homeAdvert) {
        this.homeAdvert = homeAdvert;
    }

    private List<Advert> mNewUserAdvert;

    public List<Advert> getNewUserAdvert() {
        return mNewUserAdvert;
    }

    public void setNewUserAdvert(List<Advert> newUserAdvert) {
        mNewUserAdvert = newUserAdvert;
    }

    private List<Advert> tabAdvert;

    public List<Advert> getTabAdvert() {
        return tabAdvert;
    }

    public void setTabAdvert(List<Advert> tabAdvert) {
        this.tabAdvert = tabAdvert;
    }

    private List<Advert> mServiceHeadAdvert;

    public List<Advert> getServiceHeadAdvert() {
        return mServiceHeadAdvert;
    }

    public void setServiceHeadAdvert(List<Advert> serviceHeadAdvert) {
        mServiceHeadAdvert = serviceHeadAdvert;
    }

    private String locationCityId;
    private String locationCityName;
    private String locationProvince;

    public String getLocationCityId() {
        return locationCityId;
    }

    public void setLocationCityId(String locationCityId) {
        this.locationCityId = locationCityId;
    }

    public String getLocationCityName() {
        return locationCityName;
    }

    public void setLocationCityName(String locationCityName) {
        if (locationCityName == null)
            return;
        this.locationCityName = locationCityName;
        if (getCities() != null && !getCities().isEmpty()) {
            for (CityVehicle cv : getCities()) {
                if (cv.getName() != null && cv.getName().contains(locationCityName)) {
                    setLocationCityId("" + cv.getId());
                    setLocationProvince(cv.getProvince());
                    break;
                }
            }
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setLocationCityName(cityName);
                }
            }, 5 * 1000);
        }
    }

    public String getLocationProvince() {
        return locationProvince;
    }

    public void setLocationProvince(String locationProvince) {
        this.locationProvince = locationProvince;
    }
}
