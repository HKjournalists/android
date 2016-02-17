package com.kplus.car.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWUIPushListener;
import com.alibaba.mobileim.login.YWLoginState;
import com.kplus.car.R;
import com.kplus.car.container.DazeContainerController;
import com.kplus.car.container.module.DazeShareModule;
import com.kplus.car.model.UpgradeComponent;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.model.response.GetUserOpenImResponse;
import com.kplus.car.model.response.UserConfirmShareResponse;
import com.kplus.car.model.response.request.GetUserOpenImRequest;
import com.kplus.car.model.response.request.UserConfirmShareRequest;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.antistatic.spinnerwheel.WheelVerticalView;
import com.kplus.car.wxapi.WXPayListener;
import com.kplus.car.wxapi.WXShareListener;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.json.JSONObject;

import java.util.Calendar;

public class VehicleServiceActivity extends BaseActivity implements ClickUtils.NoFastClickListener, WXPayListener, WXShareListener, IYWUIPushListener {
	public static final int REQUEST_FOR_CHOOSE_RESCUE = 11;
	private WebView webView;
	private DazeContainerController viewController;
	private View titleView;
	private TextView tvTitle, tvSubtitle, rightButton;
	private View loadingView;
	private TextView tvLoading;
	private View payTypeSelectView;
	private Button btAlipay, btYinlian, btLianLan, btWeiXin, btCancel;
	private View pictureSelectView;
	private Button btCarema, btAlbum, btPreview, btCancelPicture;
	private View rlProgressbar;
	private ProgressBar pbUpload;
	private TextView tvProgressbar;
	private View callPhoneAlert;
	private TextView tvPhone;
	private Button btNotCall, btCall;
	private TextView tvLeftButton;
	private View cashSelectedView;
	private String appId;
	private View cashBlankView;
	private WheelVerticalView cashView;
	private TextView tvUnuseCash, tvUseCash;
	private boolean isRoot = false;
	private boolean isFirstLoad = true;
	
	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_container_webview);
		webView = (WebView) findViewById(R.id.web_view_content);
//		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		titleView = findViewById(R.id.titleView);
		tvTitle = (TextView) findViewById(R.id.titleTxt);
		tvSubtitle = (TextView) findViewById(R.id.tvSecondTitle);
		loadingView = findViewById(R.id.page_loading);
		tvLoading = (TextView) findViewById(R.id.vehicle_add_titleView);
		payTypeSelectView = findViewById(R.id.rlSelectPayType);
		btAlipay = (Button) findViewById(R.id.btAlipay);
		btYinlian = (Button) findViewById(R.id.btYinlian);
        btLianLan = (Button) findViewById(R.id.btLianlian);
		btWeiXin = (Button) findViewById(R.id.btWeiXin);
		btCancel = (Button) findViewById(R.id.btCancel);
		pictureSelectView = findViewById(R.id.rlSelectPicture);
		btPreview = (Button) findViewById(R.id.btPreview);
		btPreview.setVisibility(View.GONE);
		btCarema = (Button) findViewById(R.id.btCarema);
		btAlbum = (Button) findViewById(R.id.btAlbum);
		btCancelPicture = (Button) findViewById(R.id.btCancelPicture);
		rlProgressbar = findViewById(R.id.rlProgressbar);
		pbUpload = (ProgressBar) findViewById(R.id.pbUpload);
		tvProgressbar = (TextView) findViewById(R.id.tvProgressbar);
		callPhoneAlert = findViewById(R.id.callPhoneAlert);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		btNotCall = (Button) findViewById(R.id.btNotCall);
		btCall = (Button) findViewById(R.id.btCall);
		rightButton = (TextView) findViewById(R.id.rightButton);
		tvLeftButton = (TextView) findViewById(R.id.tvLeftButton);
		cashSelectedView = findViewById(R.id.cashSelectedView);
		cashView = (WheelVerticalView) findViewById(R.id.cashView);
		cashBlankView = findViewById(R.id.cashBlankView);
		tvUnuseCash = (TextView) findViewById(R.id.tvUnuseCash);
		tvUseCash = (TextView) findViewById(R.id.tvUseCash);
	}

	@Override
	protected void loadData() {
		appId = getIntent().getStringExtra("appId");
		if(StringUtils.isEmpty(appId)){
			appId = "10000001";
		}
		else if(appId.equals("null"))
			appId = null;
		isRoot = getIntent().getBooleanExtra("isRoot", false);
		titleView.setVisibility(View.VISIBLE);
		viewController = new DazeContainerController(this);
		viewController.isRoot = isRoot;
		viewController.setWebView(webView);
		viewController.setTitleView(titleView);
		viewController.setTvTitle(tvTitle);
		viewController.setTvSubtitle(tvSubtitle);
		viewController.setLoadingView(loadingView);
		viewController.setTvLoading(tvLoading);
		viewController.setPayTypeSelectView(payTypeSelectView);
		viewController.setCallPhoneAlert(callPhoneAlert);
		viewController.setTvPhone(tvPhone);
		viewController.setBtCall(btCall);
		viewController.setBtNotCall(btNotCall);
		viewController.setRightButton(rightButton);
		viewController.setProgressView(rlProgressbar);
		viewController.setProgressBar(pbUpload);
		viewController.setProgressLabel(tvProgressbar);
		viewController.setPictureSelectView(pictureSelectView);
		if(appId != null && (appId.equals("10000001") || appId.equals("10000012")) && isRoot){
			tvLeftButton.setText("");
			tvLeftButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			tvLeftButton.setVisibility(View.VISIBLE);
		}
		else{
			tvLeftButton.setText("");
			tvLeftButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_btn_back, 0);
			tvLeftButton.setVisibility(View.VISIBLE);
		}
		rightButton.setText("");
		rightButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.daze_refresh_background, 0);
		rightButton.setVisibility(View.VISIBLE);
		if (appId != null && appId.equals("10000012") && isRoot){
			View messge = findViewById(R.id.rlMessages);
			messge.setVisibility(View.VISIBLE);
			ClickUtils.setNoFastClickListener(messge, this);
		}
		if (appId != null && appId.equals("10000001") && isRoot){
			EventAnalysisUtil.onEvent(this, "pageView_qichefuwu", "汽车服务首页流量", null);
		}
		viewController.setTvLeftButton(tvLeftButton);
		viewController.setCashSelectedView(cashSelectedView);
		viewController.setmApplication(mApplication);
		viewController.setCashView(cashView);
		String startPage = getIntent().getStringExtra("startPage");
		if(StringUtils.isEmpty(startPage)){
			viewController.initWithAppId(appId);
		}
		else{
			if(appId == null)
				viewController.initWithStartPageAndAppId(startPage, null);
			else
				viewController.initWithStartPageAndAppId(startPage, appId);
		}
	}

	@Override
	protected void setListener() {
        ClickUtils.setNoFastClickListener(btAlipay, this);
        ClickUtils.setNoFastClickListener(btYinlian, this);
        ClickUtils.setNoFastClickListener(btWeiXin, this);
        ClickUtils.setNoFastClickListener(btCancel, this);
        ClickUtils.setNoFastClickListener(btCarema, this);
        ClickUtils.setNoFastClickListener(btAlbum, this);
        ClickUtils.setNoFastClickListener(btCancelPicture, this);
        ClickUtils.setNoFastClickListener(btCall, this);
        ClickUtils.setNoFastClickListener(btNotCall, this);
        ClickUtils.setNoFastClickListener(rightButton, this);
        ClickUtils.setNoFastClickListener(tvLeftButton, this);
        ClickUtils.setNoFastClickListener(cashBlankView, this);
        ClickUtils.setNoFastClickListener(tvUnuseCash, this);
        ClickUtils.setNoFastClickListener(tvUseCash, this);
        ClickUtils.setNoFastClickListener(btLianLan, this);
	}

    @Override
    public void onNoFastClick(View v){
        switch(v.getId()){
            case R.id.btAlipay:
                payTypeSelectView.setVisibility(View.GONE);
                viewController.clickAlipay();
                break;
            case R.id.btYinlian:
                payTypeSelectView.setVisibility(View.GONE);
                viewController.clickYinlianPay();
                break;
            case R.id.btLianlian:
                payTypeSelectView.setVisibility(View.GONE);
                viewController.clickLianlianPay();
                break;
            case R.id.btWeiXin:
                payTypeSelectView.setVisibility(View.GONE);
                viewController.clickWeiXinPay();
                break;
            case R.id.btCancel:
                payTypeSelectView.setVisibility(View.GONE);
                break;
            case R.id.btCall:
                callPhoneAlert.setVisibility(View.GONE);
                viewController.callFacilitator();
                break;
            case R.id.btNotCall:
                callPhoneAlert.setVisibility(View.GONE);
                break;
            case R.id.rightButton:
                viewController.clickRightButton();
                break;
            case R.id.tvLeftButton:
                viewController.clickLeftButton();
                break;
            case R.id.cashBlankView:
                cashSelectedView.setVisibility(View.GONE);
                break;
            case R.id.tvUnuseCash:
                cashSelectedView.setVisibility(View.GONE);
                break;
            case R.id.tvUseCash:
                cashSelectedView.setVisibility(View.GONE);
                viewController.clickUseCash();
                break;
            case R.id.btAlbum:
                pictureSelectView.setVisibility(View.GONE);
                viewController.openAlbum();
                break;
            case R.id.btCarema:
                pictureSelectView.setVisibility(View.GONE);
                viewController.openCarema();
                break;
            case R.id.btCancelPicture:
                pictureSelectView.setVisibility(View.GONE);
                break;
			case R.id.rlMessages:
				if(mApplication.getId() == 0){
					startActivity(new Intent(VehicleServiceActivity.this, PhoneRegistActivity.class));
				}
				else if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
					getUserOpenIm();
				}
				else{
					mApplication.initTaobao();
					mApplication.mYWIMKIT.registerPushListener(this);
					if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
						Intent intent = mApplication.mYWIMKIT.getConversationActivityIntent();
						startActivity(intent);
					}
					else if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.logining ){
						ToastUtil.TextToast(VehicleServiceActivity.this, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
					else{
						ToastUtil.TextToast(VehicleServiceActivity.this, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
						mApplication.loginTaobao(mApplication.getOpenImUserId(), mApplication.getOpenImPassWord());
					}
				}
				break;
            default:
                break;
        }
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		viewController.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		if(mApplication.isHasSuccessShare()){
			mApplication.setHasSuccessShare(false);
			userconfirmInvite();
			Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
			if(viewController.getShareCommand() != null && !StringUtils.isEmpty(viewController.getShareCommand().getCallbackId())){
				viewController.sendShareResult();
			}
		}
		if(mApplication.isTabNeedSwitch == true){
			mApplication.isTabNeedSwitch = false;
			if(getParent() != null)
				((TabActivity)getParent()).getTabHost().setCurrentTab(2);
		}
		if(appId != null && appId.equals("10000001")){
			UpgradeComponent uc = mApplication.dbCache.getUpgradeComponent("10000001");
			if(uc != null){
				if(uc.getHasNew() != null && uc.getHasNew().booleanValue() == true){
					String time = uc.getTime();
					Calendar calendar = Calendar.getInstance();
					String timeToday = "" + calendar.get(Calendar.DAY_OF_YEAR);
					if(StringUtils.isEmpty(time) || !time.equals(timeToday)){
						uc.setTime(timeToday);
						mApplication.dbCache.saveUpgradeCompanentInfo(uc);
						if(uc.getLazy() != null && uc.getLazy().booleanValue() == false){
							viewController.downloadAndInstallAppFile(uc.getDownloadUrl(), null);
						}
						else{
							viewController.downloadAppFile(uc.getDownloadUrl());
						}
					}
				}
			}
		}
		if (appId != null && appId.equals("10000012") && isRoot){
			if (mApplication.getId() != 0){
				if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
					getUserOpenIm();
				}
				else{
					mApplication.initTaobao();
					mApplication.mYWIMKIT.registerPushListener(this);
					if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
						int count = mApplication.mYWIMKIT.getUnreadCount();
						TextView messageCount = (TextView) findViewById(R.id.tvMessageNumber);
						if (count > 0) {
							messageCount.setVisibility(View.VISIBLE);
							messageCount.setText(String.valueOf(count));
						} else {
							messageCount.setVisibility(View.GONE);
						}
					}
				}
			}
		}
//		if(mApplication.getId() != 0) {
//			if(Build.VERSION.SDK_INT >= 19)
//				webView.evaluateJavascript("javascript:Daze.loginFromNative(" + mApplication.getUserId() + "," + mApplication.getId() + "," + mApplication.getpId() + ")", null);
//			else
//            	webView.loadUrl("javascript:Daze.loginFromNative(" + mApplication.getUserId() + "," + mApplication.getId() + "," + mApplication.getpId() + ")");
//            if(getSharedPreferences("kplussp", Context.MODE_PRIVATE).getBoolean("isLogout", false)){
//                getSharedPreferences("kplussp", Context.MODE_PRIVATE).edit().putBoolean("isLogout", false);
//				if(Build.VERSION.SDK_INT >= 19)
//					webView.evaluateJavascript("javascript:Daze.logout()", null);
//				else
//               		webView.loadUrl("javascript:Daze.logout()");
//            }
//        }else {
//			if(Build.VERSION.SDK_INT >= 19)
//				webView.evaluateJavascript("javascript:Daze.logout()", null);
//			else
//				webView.loadUrl("javascript:Daze.logout()");
//		}
		if(isFirstLoad)
			isFirstLoad = false;
		else {
			if(appId != null && (appId.equals("10000001") || appId.equals("10000012")) && isRoot) {
				try {
					JSONObject argements = new JSONObject();
					if (!StringUtils.isEmpty(mApplication.getCityId()))
						argements.put("cityId", mApplication.getCityId());
					if (!StringUtils.isEmpty(mApplication.getCityName()))
						argements.put("cityName", mApplication.getCityName());
					if (mApplication.getLocation() != null) {
						argements.put("lat", mApplication.getLocation().getLatitude());
						argements.put("lng", mApplication.getLocation().getLongitude());
					}
					if (!StringUtils.isEmpty(mApplication.getProvince()))
						argements.put("provience", mApplication.getProvince());
					if(Build.VERSION.SDK_INT >= 19)
						webView.evaluateJavascript("javascript:Daze.cityChange(" + argements.toString() + ")", null);
					else
						webView.loadUrl("javascript:Daze.cityChange(" + argements.toString() + ")");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (mApplication.isFromInApp){
				mApplication.isFromInApp = false;
				Intent it = new Intent("finish");
				LocalBroadcastManager.getInstance(this).sendBroadcast(it);
				finish();
				return true;
			}
			try{
			WebBackForwardList backList = viewController.getWebView().copyBackForwardList();
			if(appId != null && (appId.equals("10000001") || appId.equals("10000012")) && isRoot){
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
					if(step == 1)
						return false;
					if(i < 0){
						i = 0;
					}
					if(webView.canGoBackOrForward(i-currentIndex)){
						webView.goBackOrForward(i-currentIndex);
						return true;
					}
				}
				else{
					return false;
				}
			}
			else{
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
						finish();
						overridePendingTransition(0, R.anim.slide_out_to_right);
						return true;
					}
					if(i < 0){
						i = 0;
					}
					if(webView.canGoBackOrForward(i-currentIndex)){
						webView.goBackOrForward(i-currentIndex);
						return true;
					}
				}
				else{
					finish();
					overridePendingTransition(0, R.anim.slide_out_to_right);
					return true;
				}
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void userconfirmInvite(){
		new AsyncTask<Void, Void, UserConfirmShareResponse>(){
			@Override
			protected UserConfirmShareResponse doInBackground(Void... params) {
				try{
					UserConfirmShareRequest request = new UserConfirmShareRequest();
					request.setParams(mApplication.getId(), DazeShareModule.contentType);
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
						if(DazeShareModule.contentType == 11){
							Intent intent = new Intent("com.kplus.car.activity.hideimage");
							LocalBroadcastManager.getInstance(VehicleServiceActivity.this).sendBroadcast(intent);
						}
					}
				}
			}
			
		}.execute();
	}

	@Override
	public void onPaySuccess(BaseResp response) {
		viewController.onPaySuccess(response);
	}

	@Override
	public void onPayCancel(BaseResp response) {
		viewController.onPayCancel(response);
	}

	@Override
	public void onPayFail(BaseResp response) {
		viewController.onPayFail(response);
	}

	@Override
	public void onShareSuccess(BaseResp response) {
		viewController.onShareSuccess(response);
	}

	@Override
	public void onShareCancel(BaseResp response) {
		viewController.onShareCancel(response);
	}

	@Override
	public void onShareFail(BaseResp response) {
		viewController.onShareFail(response);
	}

	private void getUserOpenIm(){
		new AsyncTask<Void, Void, GetUserOpenImResponse>(){
			@Override
			protected GetUserOpenImResponse doInBackground(Void... params) {
				GetUserOpenImRequest request = new GetUserOpenImRequest();
				request.setParams(mApplication.getId());
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(GetUserOpenImResponse result) {
				if(result != null && result.getCode() != null && result.getCode() == 0){
					UserOpenIm data = result.getData();
					if(data != null && !StringUtils.isEmpty(data.getOpenUserid()) && !StringUtils.isEmpty(data.getOpenPassword())){
						mApplication.initTaobao();
						mApplication.loginTaobao(data.getOpenUserid(), data.getOpenPassword());
						mApplication.setOpenImUserId(data.getOpenUserid());
						mApplication.setOpenImPassWord(data.getOpenPassword());
						mApplication.mYWIMKIT.registerPushListener(VehicleServiceActivity.this);
					}
					else{
						ToastUtil.TextToast(VehicleServiceActivity.this, !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					ToastUtil.TextToast(VehicleServiceActivity.this, result != null && !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
	}

	@Override
	public void onMessageComing() {
		if (mApplication.mYWIMKIT != null) {
			int count = mApplication.mYWIMKIT.getUnreadCount();
			TextView messageCount = (TextView) findViewById(R.id.tvMessageNumber);
			if (count > 0) {
				messageCount.setVisibility(View.VISIBLE);
				messageCount.setText(String.valueOf(count));
			} else {
				messageCount.setVisibility(View.GONE);
			}
		}
	}
}
