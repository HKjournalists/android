package com.kplus.car.container;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.activity.AlertDialogActivity;
import com.kplus.car.activity.EmergencyDetailActivity;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.activity.UploadProgressActivity;
import com.kplus.car.activity.VehicleServiceActivity;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.container.command.DazeCommandDelegate;
import com.kplus.car.container.command.DazeCommandQueue;
import com.kplus.car.container.command.DazeInvokedUrlCommand;
import com.kplus.car.container.command.WebViewOperateInterface;
import com.kplus.car.container.module.DazeModule;
import com.kplus.car.model.Coupon;
import com.kplus.car.model.UpgradeComponent;
import com.kplus.car.model.response.GetStringValueResponse;
import com.kplus.car.model.response.UpgradeComponentResponse;
import com.kplus.car.model.response.request.OrderPayRequest;
import com.kplus.car.model.response.request.UpgradeComponentRequest;
import com.kplus.car.payment.PaymentUtil;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.util.ZipUtil;
import com.kplus.car.widget.CashSelectAdapter;
import com.kplus.car.widget.RegularTextView;
import com.kplus.car.widget.antistatic.spinnerwheel.AbstractWheel;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelChangedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelClickedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.WheelVerticalView;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DazeContainerController implements WebViewOperateInterface{
	private final static String TAG = "DZH5ContainerController";
	private final static String kCustomProtocolScheme = "dazebridge";
	
	public static final int REQUEST_LOGIN = 1;
	private final static int REQUEST_FOR_ALBUM = 2;
	private final static int REQUEST_FOR_CAMERA = 3;
	private final static int REQUEST_FOR_UPLOAD_TICKET = 4;
	private final static int REQUEST_FOR_FILE_CHOOSER = 5;
	
	private Context context;
	private WebView webView;
	private TextView tvTitle, tvSubtitle;
	private View titleView;
	private View payTypeSelectView;
	private View loadingView;
	private TextView tvLoading;
	private View rlProgressbar;
	private View callPhoneAlert;
	private TextView tvPhone;
	private Button btNotCall, btCall;
	private TextView rightButton;
	private TextView tvLeftButton;
	private View cashSelectedView;
	private WheelVerticalView cashView;
	private View pictureSelectView;
	private View progressView;
	private TextView progressLabel;
	private ProgressBar progressBar;
	private PopupWindow provinceWindow;
	private GridView provinceGridView;
	private TextView provinceCancel;

	private KplusApplication mApplication;
	private DazeCommandDelegate delegate;
	private DazeCommandQueue queue;
	private Map<String, String > moduleMap;
	private Map<String, DazeModule > moduleObjects;
	private List<String> startupMethodNames;
	private String appId;
	private String appFolderName;
	public String startPage;
	private String userAgent;
	private JSONObject startupParams;
	private Handler mHandler = new Handler();
	private long orderId;
	private DazeInvokedUrlCommand paycommand, cashSelectCommand, photoCommand, shareCommand, mCarWashingCommand, showProvienceWindowCommand;
	private boolean useBalance;
	private boolean payOnline;
	private int payType;
	private String phoneUrl;
	public String mainUrl;
	private boolean loadPage = true;
	private List<Coupon> couponList;
	private CashSelectAdapter cashAdapter;
	private int currentCouponIndex;
	private boolean isReadey = false;
	private String currentImagePath;
	public String vehicleNumber;
	private IWXAPI iwxapi;
	private BroadcastReceiver receiver;
	public boolean isRoot = false;
	public Bitmap shareBitmap;
	private List<String> whiteList;
	private ValueCallback<Uri> mUploadMessage;
	private String mCameraFilePath;
	private HashMap<String, BroadcastReceiver> receivers;
	
	public DazeContainerController(Context context){
		setContext(context);
		iwxapi = WXAPIFactory.createWXAPI(context, KplusConstants.WECHAT_APPID, true);
		iwxapi.registerApp(KplusConstants.WECHAT_APPID);
	}

	public WebView getWebView() {
		return webView;
	}

	public void setWebView(WebView webView) {
		this.webView = webView;
	}
	
	public TextView getTvTitle() {
		return tvTitle;
	}

	public void setTvTitle(TextView tvTitle) {
		this.tvTitle = tvTitle;
	}

	public TextView getTvSubtitle() {
		return tvSubtitle;
	}

	public void setTvSubtitle(TextView tvSubtitle) {
		this.tvSubtitle = tvSubtitle;
	}

	public View getTitleView() {
		return titleView;
	}

	public void setTitleView(View titleView) {
		this.titleView = titleView;
	}

	public View getPayTypeSelectView() {
		return payTypeSelectView;
	}

	public void setPayTypeSelectView(View payTypeSelectView) {
		this.payTypeSelectView = payTypeSelectView;
	}

	public View getLoadingView() {
		return loadingView;
	}

	public void setLoadingView(View loadingView) {
		this.loadingView = loadingView;
	}

	public TextView getTvLoading() {
		return tvLoading;
	}

	public void setTvLoading(TextView tvLoading) {
		this.tvLoading = tvLoading;
	}

	public View getRlProgressbar() {
		return rlProgressbar;
	}

	public void setRlProgressbar(View rlProgressbar) {
		this.rlProgressbar = rlProgressbar;
	}

	public View getCallPhoneAlert() {
		return callPhoneAlert;
	}

	public void setCallPhoneAlert(View callPhoneAlert) {
		this.callPhoneAlert = callPhoneAlert;
	}

	public TextView getTvPhone() {
		return tvPhone;
	}

	public void setTvPhone(TextView tvPhone) {
		this.tvPhone = tvPhone;
	}

	public Button getBtNotCall() {
		return btNotCall;
	}

	public void setBtNotCall(Button btNotCall) {
		this.btNotCall = btNotCall;
	}

	public Button getBtCall() {
		return btCall;
	}

	public void setBtCall(Button btCall) {
		this.btCall = btCall;
	}

	public TextView getRightButton() {
		return rightButton;
	}

	public void setRightButton(TextView rightButton) {
		this.rightButton = rightButton;
	}

	public View getCashSelectedView() {
		return cashSelectedView;
	}

	public void setCashSelectedView(View cashSelectedView) {
		this.cashSelectedView = cashSelectedView;
	}
	
	public WheelVerticalView getCashView() {
		return cashView;
	}

	public void setCashView(WheelVerticalView _cashView) {
		cashView = _cashView;
		cashAdapter = new CashSelectAdapter(context);
		cashView.setViewAdapter(cashAdapter);
		cashView.setVisibleItems(5);
		cashView.addChangingListener(new OnWheelChangedListener() {			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				currentCouponIndex = newValue;
			}
		});
		cashView.addClickingListener(new OnWheelClickedListener() {			
			@Override
			public void onItemClicked(AbstractWheel wheel, int itemIndex) {
				currentCouponIndex = itemIndex;
				cashView.setCurrentItem(itemIndex);
			}
		});
	}

	public View getPictureSelectView() {
		return pictureSelectView;
	}

	public void setPictureSelectView(View pictureSelectView) {
		this.pictureSelectView = pictureSelectView;
	}

	public View getProgressView() {
		return progressView;
	}

	public void setProgressView(View progressView) {
		this.progressView = progressView;
	}

	public TextView getProgressLabel() {
		return progressLabel;
	}

	public void setProgressLabel(TextView progressLabel) {
		this.progressLabel = progressLabel;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public KplusApplication getmApplication() {
		return mApplication;
	}

	public TextView getTvLeftButton() {
		return tvLeftButton;
	}

	public void setTvLeftButton(TextView tvLeftButton) {
		this.tvLeftButton = tvLeftButton;
	}

	public void setmApplication(KplusApplication mApplication) {
		this.mApplication = mApplication;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public DazeInvokedUrlCommand getShareCommand() {
		return shareCommand;
	}

	public void setShareCommand(DazeInvokedUrlCommand shareCommand) {
		this.shareCommand = shareCommand;
	}

	public IWXAPI getIwxapi() {
		return iwxapi;
	}

	public void setIwxapi(IWXAPI iwxapi) {
		this.iwxapi = iwxapi;
	}

	public void initWithAppId(String _appId){
		initWithStartPageAndAppId(null, _appId);
	}
	
	public void initWithStartPageAndAppId(String _startPage, String _appId){
		initWithStartPageAndAppIdAndStartupParams(_startPage, _appId, null);
	}
	
	public void initWithStartPageAndAppIdAndStartupParams(String _startPage, String _appId, JSONObject _startupParams){
		appId = _appId;
		startPage = _startPage;
		startupParams = _startupParams;
		init();
	}
	
	public void init(){
		if(webView != null){
			webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			WebSettings webSettings = webView.getSettings();
			webSettings.setJavaScriptEnabled(true);
//			webSettings.setUseWideViewPort(true);
//			webSettings.setLoadWithOverviewMode(true);
//			webSettings.setBuiltInZoomControls(true);
//			if(Build.VERSION.SDK_INT > 11) {
//				webSettings.setDisplayZoomControls(false);
//            }
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			webSettings.setDefaultTextEncodingName("utf-8");
			webSettings.setDomStorageEnabled(true);						
			webSettings.setAppCacheMaxSize(1024*1024*8);
			String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
			if(!StringUtils.isEmpty(appCachePath))
				webSettings.setAppCachePath(appCachePath);
			webSettings.setAppCacheEnabled(true);
			webSettings.setAllowFileAccess(true);
			String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
			if(dir != null){
				webSettings.setDatabasePath(dir);
				webSettings.setGeolocationDatabasePath(dir);
			}
			webSettings.setDatabaseEnabled(true);
			webSettings.setGeolocationEnabled(true);
			if(android.os.Build.VERSION.SDK_INT >= 16){
				webSettings.setAllowFileAccessFromFileURLs(true);
				webSettings.setAllowUniversalAccessFromFileURLs(true);
			}
			if(android.os.Build.VERSION.SDK_INT >= 19){
				WebView.setWebContentsDebuggingEnabled(true);
			}
			webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
			webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
			try {
				Method gingerbread_getMethod =  WebSettings.class.getMethod("setNavDump", new Class[] { boolean.class });

				String manufacturer = android.os.Build.MANUFACTURER;
				Log.d(TAG, "CordovaWebView is running on device made by: " + manufacturer);
				if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB &&
						android.os.Build.MANUFACTURER.contains("HTC"))
				{
					gingerbread_getMethod.invoke(webSettings, true);
				}
			} catch (NoSuchMethodException e) {
				Log.d(TAG, "We are on a modern version of Android, we will deprecate HTC 2.3 devices in 2.8");
			} catch (IllegalArgumentException e) {
				Log.d(TAG, "Doing the NavDump failed with bad arguments");
			} catch (IllegalAccessException e) {
				Log.d(TAG, "This should never happen: IllegalAccessException means this isn't Android anymore");
			} catch (InvocationTargetException e) {
				Log.d(TAG, "This should never happen: InvocationTargetException means this isn't Android anymore.");
			}
			addJavascriptInterface();
			webView.setWebChromeClient(new WebChromeClient(){
				public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {  
				    callback.invoke(origin, true, false);

				}
				@Override
				public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
					if(Build.VERSION.SDK_INT > 7 && consoleMessage.message() != null)
						Log.e("container", consoleMessage.message());
					return super.onConsoleMessage(consoleMessage);
				}

				public void openFileChooser(ValueCallback<Uri> uploadMsg) {
					this.openFileChooser(uploadMsg, "*/*");
				}

				public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType ) {
					this.openFileChooser(uploadMsg, acceptType, null);
				}

				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
				{
					mUploadMessage = uploadMsg;
					Intent i = new Intent(Intent.ACTION_GET_CONTENT);
					i.addCategory(Intent.CATEGORY_OPENABLE);
					i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
					Intent chooser = createChooserIntent(createCameraIntent());
					chooser.putExtra(Intent.EXTRA_INTENT, i);
					((Activity)context).startActivityForResult(chooser, REQUEST_FOR_FILE_CHOOSER);
				}
			});
			webView.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageStarted(WebView view, String url,	Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					isReadey = false;
				}
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					loadJSFile();
				}
				
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if(url.contains(kCustomProtocolScheme)){
						queue.fetchCommandsFromJs();
					}
					else if(url.contains("tel:")){
						callPhoneAlert.setVisibility(View.VISIBLE);
						tvPhone.setText(url.substring(url.indexOf(":")+1));
						phoneUrl = url;
						return true;
					}
					else{
						webView.loadUrl(url);
					}
					return true;
				}
				
				@Override
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					try{
						if(!StringUtils.isEmpty(failingUrl)){
							if(failingUrl.contains(kCustomProtocolScheme)){
								queue.fetchCommandsFromJs();
							}
							else
								super.onReceivedError(view, errorCode, description, failingUrl);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			queue = new DazeCommandQueue();
			queue.setWoi(this);
			delegate = new DazeCommandDelegate();
			delegate.setWoi(this);
			moduleObjects = new HashMap<String, DazeModule>();
			loadSettings();
			if(loadPage)
				loadStartPage();
		}
		else{
			Log.e(TAG, "webView = null");
		}
	}
	
	private void loadSettings(){
		try{
			if(appId != null && !appId.equals("10000001")){
				UpgradeComponent uc = mApplication.dbCache.getUpgradeComponent(appId);
				if(uc != null){
					if(uc.getHasNew() != null && uc.getHasNew().booleanValue() == true){
						String time = uc.getTime();
						Calendar calendar = Calendar.getInstance();
						String timeToday = "" + calendar.get(Calendar.DAY_OF_YEAR);
						if(StringUtils.isEmpty(time) || !time.equals(timeToday)){
							uc.setTime(timeToday);
							mApplication.dbCache.saveUpgradeCompanentInfo(uc);
							if(uc.getLazy() != null && uc.getLazy().booleanValue() == false){
								downloadAndInstallAppFile(uc.getDownloadUrl(), appId);
								loadPage = false;
								return;
							}
							else{
								downloadAppFile(uc.getDownloadUrl());
							}
						}
					}
				}
			}
			loadPage = true;
			if(StringUtils.isEmpty(appId)){
				moduleMap = new HashMap<String, String>();
				return;
			}
			String filePath = getAppFilePath();
			if(StringUtils.isEmpty(filePath)){
				getContainer();
				loadPage = false;
				return;
			}
			String configFilePath = filePath + "package";
			FileInputStream fis = new FileInputStream(configFilePath);
			int length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
			String strConfig = EncodingUtils.getString(buffer, "utf-8");
			fis.close();
			DazeConfigParser parser = new DazeConfigParser();
			parser.initWithJson(strConfig);
			moduleMap = parser.getModulesDict();
			startupMethodNames = parser.getStartupMethodNames();
			whiteList = parser.getWhiteList();
			appFolderName = "App";
			if(startPage == null){
				startPage = parser.getStartPage();
			}
			if(StringUtils.isEmpty(startPage)){
				startPage = "index.html";
			}
			if(startupMethodNames.size() > 0){
				for(String smn : startupMethodNames){
					if(smn.contains("login")){
						if(mApplication.getId() == 0){
							ToastUtil.TextToast(context, "使用该功能需要绑定手机号", Toast.LENGTH_LONG, Gravity.CENTER);
							Intent i = new Intent(context, PhoneRegistActivity.class);
							i.putExtra("isMustPhone", true);
							((Activity) context).startActivityForResult(i, REQUEST_LOGIN);
							loadPage = false;
						}
						break;
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void loadDefaultSettings(){
		if(moduleMap == null)
			moduleMap = new HashMap<String, String>();
		moduleMap.put("system", "com.kplus.car.container.module.DazeSystemModule");
		moduleMap.put("navigator", "com.kplus.car.container.module.DazeNavigationModule");
		moduleMap.put("network", "com.kplus.car.container.module.DazeNetworkModule");
		moduleMap.put("geolocation", "com.kplus.car.container.module.DazeGeolocationModule");
		moduleMap.put("login", "com.kplus.car.container.module.DazeLoginModule");
		moduleMap.put("pay", "com.kplus.car.container.module.DazePayModule");
		moduleMap.put("share", "com.kplus.car.container.module.DazeShareModule");
		moduleMap.put("photo", "com.kplus.car.container.module.DazePhotoModule");
		moduleMap.put("coupon", "com.kplus.car.container.module.DazeCouponModule");
		moduleMap.put("feedback", "com.kplus.car.container.module.DazeFeedbackModule");
		moduleMap.put("advert", "com.kplus.car.container.module.DazeAdvertModule");
	}
	
	private void loadStartPage(){
		loadDefaultSettings();
		try{
			URL appURL = null;
		    String loadErr = null;
		    if(startPage.indexOf("://") != -1){
		    	appURL = new URL(startPage);
		    }
		    else if(appFolderName.indexOf("://") != -1){
		    	appURL = new URL(appFolderName + "/" + startPage);
		    }
		    else{
		    	String startFilePath = DazeCommandDelegate.pathForResourcewithAppId(startPage, appId);
		    	if(startFilePath == null){
		    		loadErr = String.format("Error:启动页没有找到%s/%s", appFolderName,startPage);
		    		appURL = null;
		    	}
		    	else{
				    webView.getSettings().setUserAgentString(userAgent());
				    mainUrl = "file:///" + startFilePath;
				    webView.loadUrl("file:///" + startFilePath);
		    		return;
		    	}
		    }
		    webView.getSettings().setUserAgentString(userAgent());
		    if(loadErr == null){
		    	mainUrl = appURL.toString();
		    	webView.loadUrl(appURL.toString());
		    }
		    else{
		    	String html = String.format("<html><body> %s </body></html>", loadErr);
		    	webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		    }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String userAgent(){
		if(userAgent == null){
			String originalUserAgent = webView.getSettings().getUserAgentString();
			PackageManager pm = context.getPackageManager();
			try {
				PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
				userAgent = originalUserAgent + "/DazeClient_android_" + pi.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return userAgent;
	}
	
	private String getAppFilePath(){
		String result = null;
		String unZipFolderPath = FileUtil.getContainerParentDirectory() + "daze_service_unzip/";
		File file = new File(unZipFolderPath);
		if(file != null && file.exists() && file.isDirectory()){
			String h5AppFolderPath = unZipFolderPath + appId + File.separator;
			file = new File(h5AppFolderPath);
			if(file != null && file.exists() && file.isDirectory()){
				result = h5AppFolderPath;
			}
		}
		return result;
	}
	
	private void registerModulewithClassName(DazeModule module, String className){
		moduleObjects.put(className, module);
	}
	
	private void registerModulewithModuleName(DazeModule module, String moduleName){
		String className = module.getClass().getName();
		moduleObjects.put(className, module);
		moduleMap.put(moduleName.toLowerCase(), className);
	}
	
	public void bindModuleToJSBridge(){
		try{
			Set<Entry<String, String>> sets = moduleMap.entrySet();
			Iterator<Entry<String, String>> iterator = sets.iterator();
			while(iterator.hasNext()){
				Entry<String, String> entry = iterator.next();
				String moduleName = entry.getKey();
				String className = entry.getValue();
				Class<?> classTemp = Class.forName(className);
				Object objectTemp = classTemp.newInstance();
				if(objectTemp instanceof DazeModule){
					Method method = null;
					try {
						method = classTemp.getMethod("methodsForJS");
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						method = null;
					}
					 if(method != null){
						 String result = (String) method.invoke(objectTemp);
						 if(!StringUtils.isEmpty(result)){
							 String jsCommand = String.format("Daze.bindModule(%s, \"%s\")", result,moduleName);
							 delegate.evalJS(jsCommand);
						 }
					 }					 
				}
			}
			if(mApplication.getpId() == 0){
				if(Build.VERSION.SDK_INT >= 19)
					webView.evaluateJavascript("javascript:Daze.logout()", null);
				else
					webView.loadUrl("javascript:Daze.logout()");
			}
			else {
				String jsString = "javascript:Daze.loginFromNative(" + mApplication.getUserId() + "," + mApplication.getId() + "," + mApplication.getpId() + ")";
				if(Build.VERSION.SDK_INT >= 19)
					webView.evaluateJavascript(jsString, null);
				else
					webView.loadUrl(jsString);
				if(context.getSharedPreferences("kplussp", Context.MODE_PRIVATE).getBoolean("isLogout", false)){
					context.getSharedPreferences("kplussp", Context.MODE_PRIVATE).edit().putBoolean("isLogout", false).commit();
					if(Build.VERSION.SDK_INT >= 19)
						webView.evaluateJavascript("javascript:Daze.logout()", null);
					else
						webView.loadUrl("javascript:Daze.logout()");
				}
			}
//			if(appId != null && (appId.equals("10000001") || appId.equals("10000012")) && isRoot) {
//				try {
//					JSONObject argements = new JSONObject();
//					if (!StringUtils.isEmpty(mApplication.getCityId()))
//						argements.put("cityId", mApplication.getCityId());
//					if (!StringUtils.isEmpty(mApplication.getCityName()))
//						argements.put("cityName", mApplication.getCityName());
//					if (mApplication.getLocation() != null) {
//						argements.put("lat", mApplication.getLocation().getLatitude());
//						argements.put("lng", mApplication.getLocation().getLongitude());
//					}
//					if (!StringUtils.isEmpty(mApplication.getProvince()))
//						argements.put("provience", mApplication.getProvince());
//					if(Build.VERSION.SDK_INT >= 19)
//						webView.evaluateJavascript("javascript:Daze.cityChange(" + argements.toString() + ")", null);
//					else
//						webView.loadUrl("javascript:Daze.cityChange(" + argements.toString() + ")");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void showAlert(DazeInvokedUrlCommand command){
		
	}
	
	public void whetherDazeJSBridgeExist(DazeInvokedUrlCommand command){

	}
	
	public void pay(DazeInvokedUrlCommand command){
		try{
			loadingView.setVisibility(View.GONE);
			paycommand = command;
			JSONObject arguments = command.getArguments();
			orderId = arguments.optLong("orderId");
			useBalance = arguments.optBoolean("useBalance", false);
			payOnline = arguments.optBoolean("payOnline", false);
			payType = arguments.optInt("payType", -1);
			if(payType != -1){
				payRequest(payType);
			}
			else{
				String type = arguments.optString("type");
				if(!StringUtils.isEmpty(type)){
					type = type.replaceAll(" ", "");
					if(!type.contains("1") && !type.contains("2") && !type.contains("3") && !type.contains("4")){
						if(useBalance == true && payOnline == true){
							payTypeSelectView.setVisibility(View.VISIBLE);
						}
						else if(useBalance == true){
							payRequest(KplusConstants.BALANCE_PAY);
						}
						else if(payOnline == true)
							payTypeSelectView.setVisibility(View.VISIBLE);
						return;
					}
					if(type.length() > 1){
						if(useBalance == true && payOnline == true || payOnline == true){
							if(type.contains("1")){
								payTypeSelectView.findViewById(R.id.btAlipay).setVisibility(View.VISIBLE);
								payTypeSelectView.findViewById(R.id.alipayDevider).setVisibility(View.VISIBLE);
							}
							else {
								payTypeSelectView.findViewById(R.id.btAlipay).setVisibility(View.GONE);
								payTypeSelectView.findViewById(R.id.alipayDevider).setVisibility(View.GONE);
							}
							if(type.contains("2")){
								payTypeSelectView.findViewById(R.id.btLianlian).setVisibility(View.VISIBLE);
								payTypeSelectView.findViewById(R.id.yinhangkaDevider).setVisibility(View.VISIBLE);
							}
							else {
								payTypeSelectView.findViewById(R.id.btLianlian).setVisibility(View.GONE);
								payTypeSelectView.findViewById(R.id.yinhangkaDevider).setVisibility(View.GONE);
							}
							if(type.contains("3")){
								payTypeSelectView.findViewById(R.id.btLianlian).setVisibility(View.VISIBLE);
								payTypeSelectView.findViewById(R.id.yinhangkaDevider).setVisibility(View.VISIBLE);
							}
							else {
								payTypeSelectView.findViewById(R.id.btLianlian).setVisibility(View.GONE);
								payTypeSelectView.findViewById(R.id.yinhangkaDevider).setVisibility(View.GONE);
							}
							if(type.contains("4")){
								payTypeSelectView.findViewById(R.id.btWeiXin).setVisibility(View.VISIBLE);
							}
							else {
								payTypeSelectView.findViewById(R.id.btWeiXin).setVisibility(View.GONE);
							}
							payTypeSelectView.setVisibility(View.VISIBLE);
						}
						else if(useBalance == true){
							payRequest(KplusConstants.BALANCE_PAY);
						}
					}
					else if(type.contains("2")){
						if(useBalance == true && payOnline == true)
							payRequest(KplusConstants.BALANCE_UPOMP_PAY);
						else if(useBalance == true)
							payRequest(KplusConstants.BALANCE_PAY);
						else if(payOnline == true)
							payRequest(KplusConstants.UPOMP_PAY);
					}
					else if(type.contains("1")){
						if(useBalance == true && payOnline == true)
							payRequest(KplusConstants.BALANCE_ALI_PAY);
						else if(useBalance == true)
							payRequest(KplusConstants.BALANCE_PAY);
						else if(payOnline == true)
							payRequest(KplusConstants.ALI_PAY);
					}
					else if(type.contains("3")){
						if(useBalance == true && payOnline == true)
							payRequest(KplusConstants.BALANCE_LIANLIAN_PAY);
						else if(useBalance == true)
							payRequest(KplusConstants.BALANCE_PAY);
						else if(payOnline == true)
							payRequest(KplusConstants.LIANLIAN_PAY);
					}
					else if(type.contains("4")){
						if(useBalance == true && payOnline == true)
							payRequest(KplusConstants.BALANCE_WECHAT_PAY);
						else if(useBalance == true)
							payRequest(KplusConstants.BALANCE_PAY);
						else if(payOnline == true)
							payRequest(KplusConstants.WECHAT_PAY);
					}
				}
				else{
					if(useBalance == true && payOnline == true){
						payTypeSelectView.setVisibility(View.VISIBLE);
					}
					else if(useBalance == true){
						payRequest(KplusConstants.BALANCE_PAY);
					}
					else if(payOnline == true)
						payTypeSelectView.setVisibility(View.VISIBLE);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void payRequest(final int type){
		if(type == KplusConstants.BALANCE_PAY){
			loadingView.setVisibility(View.VISIBLE);
			new AsyncTask<Object, Object, GetStringValueResponse>() {
				String strError = "网络中断，请稍候重试";
				@Override
				protected GetStringValueResponse doInBackground(Object... params) {
					OrderPayRequest request = new OrderPayRequest();
					request.setParams(orderId, type);
					try {
						return mApplication.client.execute(request);
					} catch (Exception e) {
						e.printStackTrace();
						strError = e.toString();
						return null;
					}
				}

				@Override
				protected void onPostExecute(GetStringValueResponse result) {
					loadingView.setVisibility(View.GONE);
					if (result != null) {
						if (result.getCode() != null && result.getCode() == 0) {
							Toast.makeText(context, "支付成功",	Toast.LENGTH_SHORT).show();
							try{
								JSONObject resultJson = new JSONObject();
								resultJson.put("isSuccess", true);
								sendResult(resultJson, paycommand, false);
							}catch(Exception e){
								e.printStackTrace();
							}
						} else {
							Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(context, strError,Toast.LENGTH_SHORT).show();
					}
				}

			}.execute();
		}
		else {
			PaymentUtil paymentUtil = new PaymentUtil(context, orderId, loadingView, paycommand, delegate);
			paymentUtil.payRequest(type);
		}
	}

	@Override
	public void fetchQueue(String command) {
		if(Build.VERSION.SDK_INT >= 19)
			webView.evaluateJavascript("javascript:" + command, null);
		else
			webView.loadUrl("javascript:" + command);
	}

	@Override
	public DazeModule getCommandInstance(String moduleName) {
		String className = moduleMap.get(moduleName.toLowerCase());
		if(StringUtils.isEmpty(className))
			return null;
		DazeModule obj = moduleObjects.get(className);
		if(obj == null){
			try {
				Class<?> mclass = Class.forName(className);
				obj = (DazeModule) mclass.newInstance();
				obj.init(this, delegate);
				if(obj != null){
					registerModulewithClassName(obj,className);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return obj;
	}
	
	private String getAssetFileContent(String fileName){
		String result = null;
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try{
			AssetManager am = context.getAssets();
			reader = new BufferedReader(new InputStreamReader(am.open(fileName)));
			String line = "";
			while((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}
			result = sb.toString();
		}catch(Exception e){
			e.printStackTrace();
			result = "";
		}
		finally{
			try{
				if(reader != null)
					reader.close();
				sb.setLength(0);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private void addJavascriptInterface(){
		if(android.os.Build.VERSION.SDK_INT >= 17){
			webView.addJavascriptInterface(new Object(){
				@JavascriptInterface
				public void fetchQueueResult(final String result){
					mHandler.post(new Runnable() {						
						@Override
						public void run() {
							if(KplusConstants.isDebug)
								System.out.println("fetchQueueResult===>" + result);
							queue.enqueueCommandBatch(result);
							queue.executePending();
						}
					});
				}
			}, "JSResult");
		}
		else{
			webView.addJavascriptInterface(new Object(){
				public void fetchQueueResult(final String result){
					mHandler.post(new Runnable() {						
						@Override
						public void run() {
							if(KplusConstants.isDebug)
								System.out.println("fetchQueueResult===>" + result);
							queue.enqueueCommandBatch(result);
							queue.executePending();
						}
					});
				}
			}, "JSResult");
		}
	}
	
	public void clickAlipay(){
		if(useBalance == true && payOnline == true)
			payRequest(KplusConstants.BALANCE_OPENTRADE_PAY);
		else if(useBalance == false)
			payRequest(KplusConstants.OPENTRADE_PAY);
	}
	
	public void clickYinlianPay(){
		if(useBalance == true && payOnline == true)
			payRequest(KplusConstants.BALANCE_UPOMP_PAY);
		else if(useBalance == false)
			payRequest(KplusConstants.UPOMP_PAY);
	}

    public void clickLianlianPay(){
        if(useBalance == true && payOnline == true)
            payRequest(KplusConstants.BALANCE_LIANLIAN_PAY);
        else if(useBalance == false)
            payRequest(KplusConstants.LIANLIAN_PAY);
    }
	
	public void clickWeiXinPay(){
		if(useBalance == true && payOnline == true)
			payRequest(KplusConstants.BALANCE_WECHAT_PAY);
		else if(useBalance == false)
			payRequest(KplusConstants.WECHAT_PAY);
	}
	
	public void callFacilitator(){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneUrl));
		context.startActivity(intent);
	}
	
	public void clickRightButton(){
		DazeInvokedUrlCommand menuTemp = null;
		try {
			menuTemp = (DazeInvokedUrlCommand) rightButton.getTag();
		}catch (Exception e){
			e.printStackTrace();
		}
		if(menuTemp == null){
			webView.reload();
		}
		else {
			try {
				sendResult(menuTemp.getArguments(), menuTemp, true);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void clickLeftButton(){
		DazeInvokedUrlCommand menuTemp = null;
		try {
			menuTemp = (DazeInvokedUrlCommand) tvLeftButton.getTag();
		}catch (Exception e){
			e.printStackTrace();
		}
		if(menuTemp == null){
			goBack();
		}
		else {
			try {
				sendResult(menuTemp.getArguments(), menuTemp, true);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void clickUseCash(){
		try{
			if(couponList != null && couponList.size() > 0){
				JSONObject jsonResult = new JSONObject();
				if(currentCouponIndex == 0){
					sendResult(jsonResult, cashSelectCommand, false);
				}
				else{
					Coupon cp = couponList.get(currentCouponIndex);
					jsonResult.put("id", cp.getId());
					jsonResult.put("name", cp.getName());
					jsonResult.put("info", cp.getInfo());
					jsonResult.put("amount", cp.getAmount());
					jsonResult.put("condition", cp.getCondition());
					jsonResult.put("beginTime", cp.getBeginTime());
					jsonResult.put("endTime", cp.getEndTime());
					sendResult(jsonResult, cashSelectCommand, false);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_LOGIN){
			if(loadPage == false){
				if(mApplication.getpId() == 0){
					((Activity) context).finish();
				}
				else{
					loadStartPage();
				}
			}			
			return;
		}
		else if(requestCode == REQUEST_FOR_ALBUM){
			if(resultCode == Activity.RESULT_OK){
				if(data != null){
					Uri uri = data.getData();
					if(uri != null){
						switchUpload(BMapUtil.getBitmapFromUri(context, uri));
					}
				}
			}
			return;
		}
		else if(requestCode == REQUEST_FOR_CAMERA){
			if(resultCode == Activity.RESULT_OK){
				if(!StringUtils.isEmpty(currentImagePath))
					switchUpload(BMapUtil.getBitmapFromPath(currentImagePath));
			}
			return;
		}
		else if(requestCode == VehicleServiceActivity.REQUEST_FOR_CHOOSE_RESCUE){
			Intent intent = new Intent(context, EmergencyDetailActivity.class);
			intent.putExtra("vehicleNumber", vehicleNumber);
			context.startActivity(intent);
			return;
		}
		else if(requestCode == REQUEST_FOR_UPLOAD_TICKET){
			if(resultCode == Activity.RESULT_OK){
				if(data != null && data.getStringExtra("imageUrl")!= null) {
					JSONObject json = new JSONObject();
					try {
						json.put("url", data.getStringExtra("imageUrl"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					sendResult(json, photoCommand, false);
				}
			}
			return;
		}
		else if(requestCode == REQUEST_FOR_FILE_CHOOSER){
			if(mUploadMessage != null){
				Uri result = null;
				if(mCameraFilePath != null){
					try {
						result = getImageContentUri(context, new File(mCameraFilePath));
					}catch(Exception e){
						e.printStackTrace();
					}
					mCameraFilePath = null;
				}
				if(result == null){
					result = (data == null || resultCode != Activity.RESULT_OK ? null : data.getData());
				}
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;
			}
			return;
		}
		if(data != null){
			{
				String msg = "";
				String str = data.getExtras().getString("pay_result");
		        if (str.equalsIgnoreCase("success")) {
		            msg = "支付成功！";
		        } else if (str.equalsIgnoreCase("fail")) {
		            msg = "支付失败！";
		        } else if (str.equalsIgnoreCase("cancel")) {
		            msg = "用户取消了支付";
		        }
		        ToastUtil.TextToast(context, msg, Toast.LENGTH_SHORT, Gravity.CENTER);
		        loadingView.setVisibility(View.GONE);
		        if(msg != null && msg.equals("支付成功！")){
            		try{
						JSONObject resultJson = new JSONObject();
						resultJson.put("isSuccess", true);
						sendResult(resultJson, paycommand, false);
					}catch(Exception e){
						e.printStackTrace();
					}
            	}
            	else{
            		try{
						JSONObject resultJson = new JSONObject();
						resultJson.put("isSuccess", false);
						sendResult(resultJson, paycommand, false);
					}catch(Exception e){
						e.printStackTrace();
					}
            	}
		        Intent intent = new Intent(context, AlertDialogActivity.class);
		        intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
		        intent.putExtra("message", msg);
		        context.startActivity(intent);
			}
		}
	}
	
	private void sendResult(JSONObject result, DazeInvokedUrlCommand command, boolean isKeepCallback){
		try{
			JSONObject responseJSON = new JSONObject();
			responseJSON.put("responseData", result);
			responseJSON.put("responseId", command.getCallbackId());
			responseJSON.put("keepCallback", isKeepCallback);
			String response = responseJSON.toString();
			response.replaceAll("\\\\", "\\\\\\\\");
			response.replaceAll("\\\"", "\\\\\\\"");
			response.replaceAll("\\\'", "\\\\\\\'");
			response.replaceAll("\\\n", "\\\\\\\n");
			response.replaceAll("\\\r", "\\\\\\\r");
			response.replaceAll("\\\f", "\\\\\\\f");
			response.replaceAll("\\\u2028", "\\\\\\\u2028");
			response.replaceAll("\\\u2029", "\\\\\\\u2029");
			String javascriptCommand = "DazeJSBridge.__invokeJS('" + response + "')";
			if(delegate != null){
				delegate.evalJS(javascriptCommand);
			}
			else{
				Log.e("TAG", "无法找到对应的 commandDelegate 对象");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void downloadAppFile(final String downloadUrl){
		new AsyncTask<Void, Void, String>(){
			@Override
			protected String doInBackground(Void... params) {
				try{
					URL url = new URL(downloadUrl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.connect();
					int responseCode = connection.getResponseCode();
					if(responseCode == 200){
						String containerPath = FileUtil.getContainerParentDirectory();
						File file = new File(containerPath, "daze_service_zip");
						if(file == null || !file.exists() || !file.isDirectory())
							file.mkdirs();
						file = new File(containerPath + "daze_service_zip", appId + ".zip");
						FileOutputStream fos = new FileOutputStream(file);
						InputStream is = connection.getInputStream();
						byte[] buffer = new byte[1024];
						int length = 0;
						while((length = is.read(buffer)) != -1){
							fos.write(buffer, 0, length);
							fos.flush();
						}
						is.close();
						fos.close();
						mApplication.dbCache.deleteUpgradeComponentInfo(appId);
						return "succeed";
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}
	
	public void downloadAndInstallAppFile(final String downloadUrl, final String id){
		new AsyncTask<Void, Void, String>(){
			protected void onPreExecute() {
				loadingView.setVisibility(View.VISIBLE);
				tvLoading.setText("正在更新...");
			}
			@Override
			protected String doInBackground(Void... params) {
				try{
					URL url = new URL(downloadUrl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.connect();
					int responseCode = connection.getResponseCode();
					if(responseCode == 200){
						String containerPath = FileUtil.getContainerParentDirectory();
						File file = new File(containerPath, "daze_service_zip");
						if(file == null || !file.exists() || !file.isDirectory())
							file.mkdirs();
						file = new File(containerPath + "daze_service_zip", appId + ".zip");
						FileOutputStream fos = new FileOutputStream(file);
						InputStream is = connection.getInputStream();
						byte[] buffer = new byte[1024];
						int length = 0;
						while((length = is.read(buffer)) != -1){
							fos.write(buffer, 0, length);
							fos.flush();
						}
						is.close();
						fos.close();
						ZipUtil.UnZipFolder(containerPath + "daze_service_zip" + File.separator + appId + ".zip", containerPath + "daze_service_unzip");
						file.delete();
						return "succeed";
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(String result) {
				loadingView.setVisibility(View.GONE);
				tvLoading.setText("");
				if(result == null){
					ToastUtil.TextToast(context, "安装失败", 2000, Gravity.CENTER);
				}
				else{
					mApplication.dbCache.deleteUpgradeComponentInfo(appId);
					webView.clearCache(true);
					loadSettings();
					if(loadPage)
						loadStartPage();
				}
			}
		}.execute();
	}
	
	private void loadJSFile(){
		try{
			if(!isReadey){
				isReadey = true;
				String jsCommand = "";
				if(startupParams != null){
					jsCommand += String.format("(function(){window.DAZEH5STARTUPPARAMS= %s}());", startupParams.toString());
				}
				jsCommand += getAssetFileContent("DazeWebviewJSBridge.txt");
				jsCommand += getAssetFileContent("DazeJSObj.txt");
				delegate.evalJS(jsCommand);
			}
			WebBackForwardList backList = webView.copyBackForwardList();
			String titleTemp = backList.getCurrentItem().getTitle();
			if(!StringUtils.isEmpty(titleTemp)){
				tvTitle.setText(titleTemp);
				if(titleTemp.equals("支付完成"))
					tvLeftButton.setVisibility(View.GONE);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addMenu(DazeInvokedUrlCommand command){
		try{
			JSONObject arguments = command.getArguments();
			JSONArray ja = arguments.optJSONArray("items");
			if(ja != null && ja.length() > 0){
				JSONObject jo = ja.getJSONObject(0);
				String title = jo.optString("title");
				String id = jo.optString("id");
				String side = jo.optString("side");
				Menu m = new Menu(title, id, command);
				m.item = jo.toString();
				if(side.equals("left")){
					tvLeftButton.setVisibility(View.VISIBLE);
					tvLeftButton.setTag(command);
					if(title.contains("ν")){
						title = title.substring(0, title.indexOf("ν"));
						tvLeftButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
					}
					else{
						tvLeftButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					}
					if(title != null && title.length() > 3)
						title = (title.substring(0, 3) + "...");
					tvLeftButton.setText(title);
					tvLeftButton.setBackgroundResource(R.drawable.daze_title_bar);
				}
				else if(side.equals("right")){
					rightButton.setTag(command);
					tvLeftButton.setText(title);
					tvLeftButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class Menu{
		public String title;
		public String id;
		public DazeInvokedUrlCommand command;
		public String item;
		public Menu(String _title, String _id, DazeInvokedUrlCommand _command){
			title = _title;
			id = _id;
			command = _command;
		}
	}
	
	private int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	private void getContainer(){
		new AsyncTask<Void, Void, UpgradeComponentResponse>(){
			protected void onPreExecute(){
				loadingView.setVisibility(View.VISIBLE);
				tvLoading.setText("正在获取应用，请稍后...");
			};
			@Override
			protected UpgradeComponentResponse doInBackground(Void... params) {
				try{
					UpgradeComponentRequest request = new UpgradeComponentRequest();
					request.setParams(appId, "0", "android");
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(UpgradeComponentResponse result) {
				loadingView.setVisibility(View.GONE);
				tvLoading.setText("");
				try{
					if(result != null && result.getCode() != null && result.getCode() == 0){
						List<UpgradeComponent> ucs = result.getData().getList();
						if(ucs != null && !ucs.isEmpty()){
							mApplication.dbCache.saveUpgradeCompanentInfo(ucs.get(0));
							downloadAndInstallAppFile(ucs.get(0).getDownloadUrl(),appId);
						}
						else{
							ToastUtil.TextToast(context, "获取应用失败，请稍后再试", 2000, Gravity.CENTER);
							((Activity) context).finish();
						}
					}
					else{
						ToastUtil.TextToast(context, "获取应用失败，请稍后再试", 2000, Gravity.CENTER);
						((Activity) context).finish();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					ToastUtil.TextToast(context, "获取应用失败，请稍后再试", 2000, Gravity.CENTER);
					((Activity) context).finish();
				}
			}
		}.execute();
	}
	
	public void showCashSelectWin(DazeInvokedUrlCommand command){
		try{
			cashSelectCommand = command;
			JSONObject arguments = command.getArguments();
			JSONArray ja = arguments.getJSONArray("list");
			if(ja != null && ja.length() > 0){
				couponList = new ArrayList<Coupon>();
				for(int i=0;i<ja.length();i++){
					JSONObject item = ja.getJSONObject(i);
					Coupon coupon = new Coupon();
					coupon.setId(item.optLong("id"));
					coupon.setName(item.optString("name"));
					coupon.setInfo(item.optString("info"));
					coupon.setCondition(item.optInt("condition"));
					coupon.setAmount(item.optInt("amount"));
					coupon.setBeginTime(item.optString("beginTime"));
					coupon.setEndTime(item.optString("endTime"));
					couponList.add(coupon);
				}
				Collections.sort(couponList, new Comparator<Coupon>() {
					@Override
					public int compare(Coupon lhs, Coupon rhs) {
						if (lhs.getEndTime().equals(rhs.getEndTime())) {
							return lhs.getAmount().intValue() - rhs.getAmount().intValue();
						}
						return lhs.getEndTime().compareTo(rhs.getEndTime());
					}
				});
				Coupon cp = new Coupon();
				cp.setName("不选择");
				couponList.add(0, cp);
				cashAdapter.setList(couponList);
				cashView.setViewAdapter(cashAdapter);
				cashView.setCurrentItem(1);
				cashSelectedView.setVisibility(View.VISIBLE);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void goBack(){
		try{
			WebBackForwardList backList = webView.copyBackForwardList();
//			if(appId != null && appId.equals("10000001")){
//				if(backList != null && backList.getSize() >0){
//					WebHistoryItem currentItem = backList.getCurrentItem();
//					int currentIndex = backList.getCurrentIndex();
//					WebHistoryItem lastItem = currentItem;
//					int i;
//					int step = 1;
//					for(i=currentIndex-1;i>=0;i--){
//						WebHistoryItem itemTemp = backList.getItemAtIndex(i);
//						if(!itemTemp.getOriginalUrl().equals(lastItem.getOriginalUrl())){
//							lastItem = itemTemp;
//							step--;
//							break;
//						}
//					}
//					if(step == 1)
//						return;
//					if(i < 0){
//							i = 0;						
//					}
//					if(webView.canGoBackOrForward(i-currentIndex)){
//						webView.goBackOrForward(i-currentIndex);
//					}
//				}
//			}
//			else
			{
				if(backList != null && backList.getSize() >0){
					WebHistoryItem currentItem = backList.getCurrentItem();
					int currentIndex = backList.getCurrentIndex();
					WebHistoryItem lastItem = currentItem;
					int i;
					int step = 1;
					for(i=currentIndex-1;i>=0;i--){
						WebHistoryItem itemTemp = backList.getItemAtIndex(i);
						if(!itemTemp.getOriginalUrl().equals(lastItem.getOriginalUrl())){
							lastItem = itemTemp;
							step--;
							break;
						}
					}
					if(step == 1){
						if(appId != null && (appId.equals("10000001") || appId.equals("10000012")) && isRoot){

						}
						else {
							((Activity)context).finish();
							((Activity)context).overridePendingTransition(0, R.anim.slide_out_to_right);
						}
						return;
					}
					if(i < 0){
						i = 0;
					}
					if(webView.canGoBackOrForward(i-currentIndex)){
						webView.goBackOrForward(i-currentIndex);
					}
				}
				else{
					if(appId != null && (appId.equals("10000001") || appId.equals("10000012")) && isRoot){

					}
					else {
						((Activity) context).finish();
						((Activity) context).overridePendingTransition(0, R.anim.slide_out_to_right);
					}
				}
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public void photo(DazeInvokedUrlCommand command){
		pictureSelectView.setVisibility(View.VISIBLE);
		photoCommand = command;
	}
	
	public void openAlbum(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        ((Activity)context).startActivityForResult(photoPickerIntent, REQUEST_FOR_ALBUM);
	}
	
	public void openCarema(){
		try {
			Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
			currentImagePath = FileUtil.getParentDirectory() + fileName;
			File file = new File(FileUtil.getParentDirectory(), fileName);
			Uri uri = Uri.fromFile(file);
			intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			((Activity)context).startActivityForResult(intentCamera, REQUEST_FOR_CAMERA);
			Toast.makeText(context, "请横屏拍摄", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void switchUpload(Bitmap bitmap) {
		if(bitmap == null)
			return;
		try {
			byte[] content = BMapUtil.getContentFromBitmap(bitmap);
			if (content.length > 0) {
				Intent intent = new Intent(context, UploadProgressActivity.class);
				intent.putExtra("type", KplusConstants.UPLOAD_TICKET);
				intent.putExtra("content", content);
				intent.putExtra("typeParam", photoCommand.getArguments().getInt("type"));
				((Activity) context).startActivityForResult(intent, REQUEST_FOR_UPLOAD_TICKET);
			}
			bitmap.recycle();
			bitmap = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void sendShareResult(){
		JSONObject result = new JSONObject();
		try {
			result.put("status", "成功");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		sendResult(result, shareCommand, false);
	}
	
	public void registerReceiver(DazeInvokedUrlCommand command){
		JSONObject jsonArgument = command.getArguments();
		final String name = jsonArgument.optString("name");
		if(receivers == null){
			receivers = new HashMap<String, BroadcastReceiver>();
			BroadcastReceiver mBr = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if(intent != null) {
						try {
							final String eventData = intent.getStringExtra("eventData");
							if(eventData != null) {
								webView.postDelayed(new Runnable() {
									@Override
									public void run() {
										if(Build.VERSION.SDK_INT >= 19)
											webView.evaluateJavascript("javascript:Daze.dispatch(\"" + name + "\"," + eventData + ")", null);
										else
											webView.loadUrl("javascript:Daze.dispatch(\"" + name + "\"," + eventData + ")");
									}
								}, 1000);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					if(receivers != null && receivers.get(name) != null) {
						LocalBroadcastManager.getInstance(context.getApplicationContext()).unregisterReceiver(receivers.get(name));
					}
				}
			};
			receivers.put("name", mBr);
			LocalBroadcastManager.getInstance(context.getApplicationContext()).registerReceiver(mBr, new IntentFilter("com.kplus.car" + name));
		}
//		if(receiver == null){
//			receiver = new BroadcastReceiver() {
//				@Override
//				public void onReceive(Context arg0, Intent arg1) {
//					if(arg1 != null) {
//						try {
//							final String eventData = arg1.getStringExtra("eventData");
//							if(eventData != null) {
////								if (Build.VERSION.SDK_INT >= 19) {
////									webView.evaluateJavascript("javascript:Daze.dispatch('" + name + "'," + eventData + ")", new ValueCallback<String>() {
////										@Override
////										public void onReceiveValue(String value) {
////											if (value != null)
////												Log.e("container", value);
////										}
////									});
////								} else
//								webView.postDelayed(new Runnable() {
//									@Override
//									public void run() {
//										if(Build.VERSION.SDK_INT >= 19)
//											webView.evaluateJavascript("javascript:Daze.dispatch(\"" + name + "\"," + eventData + ")", null);
//										else
//											webView.loadUrl("javascript:Daze.dispatch(\"" + name + "\"," + eventData + ")");
//									}
//								}, 1000);
//							}
//						}catch(Exception e){
//							e.printStackTrace();
//						}
//					}
//					LocalBroadcastManager.getInstance(context.getApplicationContext()).unregisterReceiver(receiver);
//					receiver = null;
//				}
//			};
//			LocalBroadcastManager.getInstance(context.getApplicationContext()).registerReceiver(receiver , new IntentFilter("com.kplus.car" + name));
//		}
	}
	
	public void unRegisterReceiver(){
		if(receiver != null){
			LocalBroadcastManager.getInstance(context.getApplicationContext()).unregisterReceiver(receiver);
			receiver = null;
		}
	}
	
	public void sendBroadcast(DazeInvokedUrlCommand command){
		JSONObject jsonArgument = command.getArguments();
		String name = jsonArgument.optString("name");
		Intent intent = new Intent("com.kplus.car" + name);
		JSONObject eventData = jsonArgument.optJSONObject("eventData");
		if(eventData != null) {
			if(name.equals("cityChange")){
				if(!StringUtils.isEmpty(eventData.optString("name")))
					mApplication.setCityName(eventData.optString("name"));
				if(!StringUtils.isEmpty(eventData.optString("province")))
					mApplication.setProvince(eventData.optString("province"));
//				webView.reload();
			}
			intent.putExtra("eventData", eventData.toString());
		}
		LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
	}

	public DazeInvokedUrlCommand getCarWashingCommand() {
		return mCarWashingCommand;
	}

	public void setCarWashingCommand(DazeInvokedUrlCommand carWashingCommand) {
		mCarWashingCommand = carWashingCommand;
	}

	public void carWash(DazeInvokedUrlCommand command) {
		if (KplusApplication.getInstance().isUserLogin(true, "上门洗车需要绑定手机号")) {
			CNCarWashingLogic.startCarWashingActivity(context, false);
		}
	}

	public void onShareSuccess(BaseResp response) {
		mApplication.setWxShareListener(null);
		ToastUtil.TextToast(context, "分享成功", Toast.LENGTH_SHORT, Gravity.CENTER);
		if(shareBitmap != null && !shareBitmap.isRecycled()){
			shareBitmap.recycle();
			shareBitmap = null;
		}
		if(getShareCommand() != null && !StringUtils.isEmpty(getShareCommand().getCallbackId())){
			sendShareResult();
		}
	}

	public void onShareCancel(BaseResp response) {
		mApplication.setWxShareListener(null);
		ToastUtil.TextToast(context, "取消分享", Toast.LENGTH_SHORT, Gravity.CENTER);
		if(shareBitmap != null && !shareBitmap.isRecycled()){
			shareBitmap.recycle();
			shareBitmap = null;
		}
	}

	public void onShareFail(BaseResp response) {
		mApplication.setWxShareListener(null);
		ToastUtil.TextToast(context, "分享失败", Toast.LENGTH_SHORT, Gravity.CENTER);
		if(shareBitmap != null && !shareBitmap.isRecycled()){
			shareBitmap.recycle();
			shareBitmap = null;
		}
	}

	public boolean isUrlWhite(String url){
		if(whiteList == null || whiteList.isEmpty())
			return false;
		for(String item : whiteList){
			if(item != null && item.contains(url))
				return true;
		}
		return false;
	}

	private String[] provinces = { "京", "沪", "浙", "苏", "粤", "鲁", "晋", "冀", "豫",
			"川", "渝", "辽", "吉", "黑", "皖", "鄂", "湘", "赣", "闽", "陕", "甘", "宁",
			"蒙", "津", "贵", "云", "桂", "琼", "青", "新", "藏" };

	private void initProvinceWindow() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.daze_window_province_layout, null);
		provinceWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		provinceWindow.setBackgroundDrawable(new ColorDrawable(-10000));
		provinceWindow.setFocusable(true);
		provinceWindow.setOutsideTouchable(true);
		provinceGridView = (GridView) layout.findViewById(R.id.province_add_item);
		provinceCancel = (TextView) layout.findViewById(R.id.province_add_cancel);
		provinceCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				provinceWindow.dismiss();
			}
		});
		provinceGridView.setAdapter(new ArrayAdapter<String>(context, R.layout.daze_simple_text_item, R.id.simple_text_item, provinces));
		provinceGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				if (position >= 0 && position < provinces.length) {
					String province = (String) listView.getAdapter().getItem(position);
					JSONObject result = new JSONObject();
					try {
						result.put("province", province);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					sendResult(result, showProvienceWindowCommand, false);
				}
				provinceWindow.dismiss();
			}
		});
	}

	public void showProvienceWindow(DazeInvokedUrlCommand command){
		showProvienceWindowCommand = command;
		if(provinceWindow == null)
			initProvinceWindow();
		provinceWindow.showAtLocation(((Activity)context).findViewById(R.id.vehicleServiceRoot), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
		chooser.putExtra(Intent.EXTRA_TITLE, "选择图片");
		return chooser;
	}

	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File cameraDataDir = new File(externalDataDir.getAbsolutePath()
				+ File.separator + "didi");
		cameraDataDir.mkdirs();
		String mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
		this.mCameraFilePath = mCameraFilePath;
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
	}

	public Uri getImageContentUri(Context context, java.io.File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[]{MediaStore.Images.Media._ID},
				MediaStore.Images.Media.DATA + "=? ",
				new String[]{filePath}, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	public void onPaySuccess(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(context, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
		if(paycommand != null){
			try{
				JSONObject resultJson = new JSONObject();
				resultJson.put("isSuccess", true);
				sendResult(resultJson, paycommand, false);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void onPayCancel(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(context, "支付取消", Toast.LENGTH_SHORT, Gravity.CENTER);
	}

	public void onPayFail(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(context, StringUtils.isEmpty(response.errStr) ? "支付失败" : response.errStr, Toast.LENGTH_SHORT, Gravity.CENTER);
	}
	
}
