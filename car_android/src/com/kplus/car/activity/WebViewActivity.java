package com.kplus.car.activity;

import java.io.File;
import java.net.URLDecoder;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.response.GetStringValueResponse;
import com.kplus.car.model.response.GetUserInviteContentResponse;
import com.kplus.car.model.response.UserConfirmShareResponse;
import com.kplus.car.model.response.request.GetUserInviteContentRequest;
import com.kplus.car.model.response.request.OrderPayRequest;
import com.kplus.car.model.response.request.UserConfirmShareRequest;
import com.kplus.car.payment.PaymentUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXPayListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

public class WebViewActivity extends BaseActivity implements OnClickListener, WXPayListener {
	private static final int PHONE_REGIST_REQUEST_FOR_LOGIN = 0x01;
	private static final int PHONE_REGIST_REQUEST_FOR_SHARE = 0x02;
	private static final int REQUEST_FOR_CALL = 0x03;
	private static final int REQUEST_FOR_FILE_CHOOSER = 0x04;

	private ImageView ivClose, ivGoBackword, ivGoForword, ivRefresh;
	private WebView webView;
	private View rlSelectPayType;

	private String url;
	private int type;
	private String shareType;
	private String functionName;
	private Uri callUri;
	private long orderId;
	private boolean useBalance, payOnline;
	private boolean useALI = true;
	private IWXAPI iwxapi;
	private ValueCallback<Uri> mUploadMessage;
	private String mCameraFilePath;

	@Override
	protected void initView() {
		setContentView(R.layout.daze_web_view);
		ivClose = (ImageView) findViewById(R.id.ivClose);
		ivGoBackword = (ImageView) findViewById(R.id.ivGoBackword);
		ivGoForword = (ImageView) findViewById(R.id.ivGoForword);
		ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
		webView = (WebView) findViewById(R.id.web_view_content);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setDomStorageEnabled(true);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
		if(!StringUtils.isEmpty(appCachePath))
			webSettings.setAppCachePath(appCachePath);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAllowFileAccess(true);
		String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
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
		webView.setWebChromeClient(new WebChromeClient() {
			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);

			}
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				this.openFileChooser(uploadMsg, "*/*");
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				this.openFileChooser(uploadMsg, acceptType, null);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				Intent chooser = createChooserIntent(createCameraIntent());
				chooser.putExtra(Intent.EXTRA_INTENT, i);
				startActivityForResult(chooser, REQUEST_FOR_FILE_CHOOSER);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent intent = null;
				if(url.contains("bridge://showShareWin")){
					shareType = url.substring(url.indexOf("_") + 1);
					if(mApplication.getId() == 0){
						intent = new Intent(WebViewActivity.this, PhoneRegistActivity.class);
						intent.putExtra("isShare", true);
						startActivityForResult(intent, PHONE_REGIST_REQUEST_FOR_SHARE);
					}
					else{
						new GetInviteContentTask().execute();
					}					
				}
				else if(url.contains("bridge://loading")){
					if(url.contains("true")){
						showloading(true);
					}
					else{
						showloading(false);
					}
				}
				else if(url.contains("tel:")){
					callUri = Uri.parse("tel:"+url.substring(url.indexOf("tel:") + "tel:".length()));
					intent = new Intent(WebViewActivity.this, AlertDialogActivity.class);
					intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
					intent.putExtra("message", "是否要拨打电话" + url.substring(url.indexOf("tel:") + "tel:".length()));
					startActivityForResult(intent, REQUEST_FOR_CALL);
				}
				else if(url.contains("bridge://getPid")){
					functionName = url.substring(url.lastIndexOf("_") + 1);
					if(mApplication.getpId() == 0){
						intent = new Intent(WebViewActivity.this, PhoneRegistActivity.class);
						startActivityForResult(intent, PHONE_REGIST_REQUEST_FOR_LOGIN);
					}
					else{
						if(Build.VERSION.SDK_INT >= 19)
							webView.evaluateJavascript("javascript:DZ.setPid(" + mApplication.getpId() + "," + functionName + "," + mApplication.getId() + ")", null);
						else
							webView.loadUrl("javascript:DZ.setPid(" + mApplication.getpId() + "," + functionName + "," + mApplication.getId() + ")");
					}
				}
				else if(url.contains("bridge://showMsg")){
					try{
						String msg = url.substring(url.indexOf("_")+1);
						msg = URLDecoder.decode(msg, "utf-8");
						Toast.makeText(WebViewActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
					catch(Exception e){
						
					}
					showloading(false);
				}
				else if(url.contains("bridge://pay")){
					payRequest(url);
				}
				else if(url.contains("bridge://")){
					
				}
				else {
					view.loadUrl(url);
				}
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				showloading(false);
				super.onPageFinished(view, url);
			}
		});
		rlSelectPayType = findViewById(R.id.rlSelectPayType);
	}

	protected void loadData() {
		try{
			iwxapi = WXAPIFactory.createWXAPI(this, KplusConstants.WECHAT_APPID, true);
			iwxapi.registerApp(KplusConstants.WECHAT_APPID);
			url = getIntent().getStringExtra("url");
			if (url == null) {
				finish();
			}
			else{
				if(url.contains("?type")){
					String[] params = url.split("\\?");
					url = params[0];
					String[] subParams = params[1].split("=");
					type = Integer.parseInt(subParams[1]);
				}
			}
			showloading(true);
			webView.getSettings().setUserAgentString(userAgent());
			webView.loadUrl(url);
			if(mApplication.getpId() == 0) {
				if(Build.VERSION.SDK_INT >= 19)
					webView.evaluateJavascript("javascript:DZ.logout()", null);
				else
					webView.loadUrl("javascript:DZ.logout()");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == PHONE_REGIST_REQUEST_FOR_LOGIN){
			if (mApplication.getpId() != 0) {
				webView.loadUrl("javascript:DZ.setPid(" + mApplication.getpId() + "," + functionName + "," + mApplication.getId() + ")");
			}
			return;
		}
		else if(requestCode == PHONE_REGIST_REQUEST_FOR_SHARE){
			if (mApplication.getId() != 0) {
				new GetInviteContentTask().execute();
			}
			return;
		}
		else if(requestCode == REQUEST_FOR_CALL){
			if(resultCode == RESULT_OK){
				Intent telIntent = new Intent(Intent.ACTION_CALL, callUri);
				startActivity(telIntent);
			}
			return;
		}
		else if(requestCode == REQUEST_FOR_FILE_CHOOSER){
			if(mUploadMessage != null){
				Uri result = null;
				if(mCameraFilePath != null){
					try {
						result = getImageContentUri(this, new File(mCameraFilePath));
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
		if(data == null){
			return;
		}
		String msg = "";
		String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
        ToastUtil.TextToast(WebViewActivity.this, msg, Toast.LENGTH_SHORT, Gravity.CENTER);
        Intent intent = new Intent(WebViewActivity.this, AlertDialogActivity.class);
        intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
        intent.putExtra("message", msg);
        startActivity(intent);
        
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void setListener() {
		ivClose.setOnClickListener(this);
		ivGoBackword.setOnClickListener(this);
		ivGoForword.setOnClickListener(this);
		ivRefresh.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(ivClose)) {
			finish();
//			overridePendingTransition(0, R.anim.slide_out_to_bottom);
			overridePendingTransition(0, R.anim.slide_out_to_right);
		}
		else if(v.equals(ivGoBackword)){
			webView.goBack();
		}
		else if(v.equals(ivGoForword)){
			webView.goForward();
		}
		else if(v.equals(ivRefresh)) {
			webView.reload();
		}
	}
	
	@Override
	protected void onResume() {
		if(mApplication.isHasSuccessShare()){
			mApplication.setHasSuccessShare(false);
			Toast.makeText(WebViewActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
			userconfirmInvite();
		}
		super.onResume();
	}
	
	private void userconfirmInvite(){
		new AsyncTask<Void, Void, UserConfirmShareResponse>(){
			@Override
			protected UserConfirmShareResponse doInBackground(Void... params) {
				try{
					UserConfirmShareRequest request = new UserConfirmShareRequest();
					request.setParams(mApplication.getId(), type);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(UserConfirmShareResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
                        if(type == 11){
							Intent intent = new Intent("com.kplus.car.activity.hideimage");
							LocalBroadcastManager.getInstance(WebViewActivity.this).sendBroadcast(intent);
							ToastUtil.TextToast(WebViewActivity.this, "您已经成功获取110元补贴金", 2000, Gravity.CENTER);
						}
					}
				}
			}
			
		}.execute();
	}
	
	class GetInviteContentTask extends AsyncTask<Void, Void, GetUserInviteContentResponse>{
		private String errorText = "网络中断，请稍后重试";
		@Override
		protected void onPreExecute() {
			showloading(true);
			super.onPreExecute();
		}
		
		@Override
		protected GetUserInviteContentResponse doInBackground(Void... params) {
			try{
				GetUserInviteContentRequest request = new GetUserInviteContentRequest();
				request.setParams(mApplication.getId(), type);
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				errorText = e.toString();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(GetUserInviteContentResponse result) {
			try{
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						Intent intent = new Intent(WebViewActivity.this, ShareInService.class);
						intent.putExtra("shareType", shareType);
						intent.putExtra("contentType", type);
						intent.putExtra("title", result.getData().getTitle());
						intent.putExtra("summary", result.getData().getSummary());
						intent.putExtra("content", result.getData().getContent());
						intent.putExtra("inviteUrl", result.getData().getInviteUrl());
						intent.putExtra("imgUrl", result.getData().getImgUrl());
						startActivity(intent);
					}
					else{
						Toast.makeText(WebViewActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(WebViewActivity.this, errorText, Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(WebViewActivity.this, "获取分享内容失败", Toast.LENGTH_SHORT).show();
			}
			showloading(false);
			super.onPostExecute(result);
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(webView.canGoBack())
				webView.goBack();
			else{
				finish();
//				overridePendingTransition(0, R.anim.slide_out_to_bottom);
				overridePendingTransition(0, R.anim.slide_out_to_right);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void payRequest(String url){
		showloading(false);
		String[] temp = url.split("_");
		try{
			orderId = Long.parseLong(temp[1]);
			functionName = temp[2];
			if(temp[3].equals("undefined"))
				useBalance = false;
			else
				useBalance = Boolean.parseBoolean(temp[3]);
			if(temp[4].equals("undefined"))
				payOnline = true;
			else
				payOnline = Boolean.parseBoolean(temp[4]);
			if(temp.length >= 6){
				if(!StringUtils.isEmpty(temp[5]) && !temp[5].equals("undefined") && !temp[5].contains("1")){
					useALI = false;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
		if(rlSelectPayType.getVisibility() == View.GONE)
			if(!useALI){
				if(useBalance == true && payOnline == true)
					payRequest(KplusConstants.BALANCE_UPOMP_PAY);
				else if(useBalance == true)
					payRequest(KplusConstants.BALANCE_PAY);
				else if(payOnline == true)
					payRequest(KplusConstants.UPOMP_PAY);
			}
			else{
				if(useBalance == true && payOnline == true){
					rlSelectPayType.setVisibility(View.VISIBLE);
				}
				else if(useBalance == true){
					payRequest(KplusConstants.BALANCE_PAY);
				}
				else if(payOnline == true)
					rlSelectPayType.setVisibility(View.VISIBLE);
			}
	}
	
	private void payRequest(final int type){
		if(type == KplusConstants.BALANCE_PAY){
			showloading(true);
			new AsyncTask<Object, Object, GetStringValueResponse>() {
				@Override
				protected GetStringValueResponse doInBackground(Object... params) {
					OrderPayRequest request = new OrderPayRequest();
					request.setParams(orderId, type);
					try {
						return mApplication.client.execute(request);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				protected void onPostExecute(GetStringValueResponse result) {
					showloading(false);
					if (result != null) {
						if (result.getCode() != null && result.getCode() == 0) {
								Toast.makeText(WebViewActivity.this, "支付成功",	Toast.LENGTH_SHORT).show();
								webView.loadUrl("javascript:DZ.payResult(" + true + "," + functionName + ")");
						} else {
							Toast.makeText(WebViewActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(WebViewActivity.this, "网络异常，请稍后重试",	Toast.LENGTH_SHORT).show();
					}
				}

			}.execute();
		}
		else {
			PaymentUtil paymentUtil = new PaymentUtil(WebViewActivity.this, orderId, findViewById(R.id.page_loading), null, null);
			paymentUtil.payRequest(type);
		}

	}
	
	public void click(View v){
		switch(v.getId()){
			case R.id.btAlipay:
				if(useBalance == true && payOnline == true)
					payRequest(KplusConstants.BALANCE_ALI_PAY);
				else if(useBalance == false)
					payRequest(KplusConstants.ALI_PAY);
				break;
			case R.id.btYinlian:
				if(useBalance == true && payOnline == true)
					payRequest(KplusConstants.BALANCE_UPOMP_PAY);
				else if(useBalance == false)
					payRequest(KplusConstants.UPOMP_PAY);
				break;
			case R.id.btWeiXin:
				if(useBalance == true && payOnline == true)
					payRequest(KplusConstants.BALANCE_WECHAT_PAY);
				else if(useBalance == false)
					payRequest(KplusConstants.WECHAT_PAY);
				break;
			case R.id.btCancel:
				break;
		}
		if(rlSelectPayType.getVisibility() == View.VISIBLE)
			rlSelectPayType.setVisibility(View.GONE);
	}

	@Override
	public void onPaySuccess(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(WebViewActivity.this, "支付成功", Toast.LENGTH_SHORT, Gravity.CENTER);
	}

	@Override
	public void onPayCancel(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(WebViewActivity.this, "支付取消", Toast.LENGTH_SHORT, Gravity.CENTER);
	}

	@Override
	public void onPayFail(BaseResp response) {
		mApplication.setWxPayListener(null);
		ToastUtil.TextToast(WebViewActivity.this, StringUtils.isEmpty(response.errStr) ? "支付失败" : response.errStr, Toast.LENGTH_SHORT, Gravity.CENTER);
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
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(mCameraFilePath)));
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

	private String userAgent(){
		String userAgent = null;
		String originalUserAgent = webView.getSettings().getUserAgentString();
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			userAgent = originalUserAgent + "/DazeClient_android_" + pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return userAgent;
	}

}
