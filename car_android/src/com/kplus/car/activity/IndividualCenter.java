package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.login.YWLoginState;
import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.GetMessageCountTask;
import com.kplus.car.comm.DazeUserAccount;
import com.kplus.car.fragment.AdvertFragment;
import com.kplus.car.model.Account;
import com.kplus.car.model.Advert;
import com.kplus.car.model.UserInfo;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.json.AdvertJson;
import com.kplus.car.model.json.GetUserInfoJson;
import com.kplus.car.model.response.GetAdvertDataResponse;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.GetClientLicenceCountResponse;
import com.kplus.car.model.response.GetMessageCountResponse;
import com.kplus.car.model.response.GetUserInfoResponse;
import com.kplus.car.model.response.GetUserOpenImResponse;
import com.kplus.car.model.response.request.GetAdvertDataRequest;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.GetClientLicenceCountRequest;
import com.kplus.car.model.response.request.GetUserInfoRequest;
import com.kplus.car.model.response.request.GetUserOpenImRequest;
import com.kplus.car.service.UpdateUserVehicleService;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.fb.FeedbackAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndividualCenter extends BaseActivity implements OnClickListener{
	private final int REQUEST_FOR_SHARE = 1;
	private final int REQUEST_FOR_MESSAGE = 2;
	private View headView;
	private ImageView ivPhoto;
	private TextView tvPhoneNumber;
	private View rlMyOrder;
	private TextView tvOrderNumber;
	private View rlAccountBalance;
	private TextView tvBalance;
	private View rlVoucher;
	private TextView tvVoucher;
	private View inviteView;
	private TextView tvAuthenNum;
	private View rlVehicles;
	private TextView tvVehiclesAuth, tvVehicles;
	private TextView tvQianming, tvLocation;
	//lxm add start
	private View rlMyPrivilege;
	//lxm add end
	private View rlMessages;
	private TextView tvMessageNumber;
	private View newFuli;
	private ImageView ivKefu;
	private View rlShare, rlMore;
	private View advertView;
    private View rlPromoCode;
	private UserInfo userInfo;
	private DisplayImageOptions optionsPhoto;
	private String phoneNumber;
	private DecimalFormat dFormat;
	private String headImgUrl;
	private List<VehicleAuth>  vehicleAuths;
	private List<UserVehicle> userVehicles;
	private List<VehicleAuth> selfSuccessVehicleAuths;
	private long count = 0;
	private boolean needRefresh = false;
	
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 2:
				tvAuthenNum.setText(""+ count);
				getClientLicenceCount();
				break;
			}			
		}
	};
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			updateUI();
		}
	};

	private BroadcastReceiver newMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			getMessageCount();
			newFuli.setVisibility(View.VISIBLE);
		}
	};

	@Override
	protected void onResume() {
		needRefresh = true;
		IntentFilter filter = new IntentFilter(KplusConstants.ACTION_PROCESS_CUSTOM_MESSAGE);
		LocalBroadcastManager.getInstance(IndividualCenter.this).registerReceiver(mReceiver, filter);
		LocalBroadcastManager.getInstance(this).registerReceiver(newMessageReceiver, new IntentFilter("com.kplus.car.GexinSdkMsgReceiver.newmessage"));
		userInfo = mApplication.dbCache.getUserInfo();
		if(mApplication.getId() == 0){
			tvPhoneNumber.setText("请登录/注册");
			headImgUrl = null;
			mApplication.imageLoader.displayImage(headImgUrl, ivPhoto, optionsPhoto);
			tvOrderNumber.setText("0笔新订单");
			tvBalance.setText("0.00");
			tvVoucher.setText("0");
			tvMessageNumber.setText("0");
			newFuli.setVisibility(View.GONE);
			tvLocation.setVisibility(View.GONE);
			tvLocation.setText("");
			tvQianming.setVisibility(View.GONE);
		}
		else{
			getMessageCount();
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
				newFuli.setVisibility(View.GONE);
			else
				newFuli.setVisibility(View.VISIBLE);
			new GetUserInfoTask().execute();
			if(mApplication.getpId() != 0){
				if(StringUtils.isEmpty(phoneNumber))
					phoneNumber = mApplication.dbCache.getValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE);
			}
			if(!StringUtils.isEmpty(phoneNumber)){
				if(userInfo != null && !StringUtils.isEmpty(userInfo.getName()))
					tvPhoneNumber.setText(userInfo.getName());
				else{
					tvPhoneNumber.setText(phoneNumber);
				}
			}		
		}
		if(userInfo != null){
			headImgUrl = userInfo.getIconUrl();
			mApplication.imageLoader.displayImage(headImgUrl, ivPhoto, optionsPhoto);
			tvOrderNumber.setText((userInfo.getOrderCount() == null ? 0 : userInfo.getOrderCount().intValue()) + "笔");
			if(userInfo.getCashBalance() == null || Math.abs(userInfo.getCashBalance() - 0) < 0.0001) {
				tvBalance.setText("0.00");
				mApplication.setCarWashingUserBalance(0);
			}
			else {
				tvBalance.setText(dFormat.format(userInfo.getCashBalance().floatValue()));
				mApplication.setCarWashingUserBalance(userInfo.getCashBalance());
			}
			tvVoucher.setText(String.valueOf(userInfo.getCoupon() == null ? 0 : userInfo.getCoupon().intValue()));
			if(!StringUtils.isEmpty(userInfo.getName())){
				tvPhoneNumber.setText(userInfo.getName());
			}
			String cityName = mApplication.getCityName();
			if(!StringUtils.isEmpty(cityName)){
				tvLocation.setVisibility(View.VISIBLE);
				tvLocation.setText(cityName);
			}
			if(!StringUtils.isEmpty(userInfo.getInfo())){
				tvQianming.setText(userInfo.getInfo());
				tvQianming.setVisibility(View.VISIBLE);
			}
			else
				tvQianming.setVisibility(View.GONE);
		}
		else{
			headImgUrl = null;
			mApplication.imageLoader.displayImage(headImgUrl, ivPhoto, optionsPhoto);
			tvOrderNumber.setText("0笔新订单");
			tvBalance.setText("0.00");
			tvVoucher.setText("0");
			tvLocation.setVisibility(View.GONE);
			tvLocation.setText("");
			tvQianming.setVisibility(View.GONE);
		}
		updateUI();
		super.onResume();
	}

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_individual_center);
		headView = findViewById(R.id.headView);
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
		rlMyOrder = findViewById(R.id.rlMyOrder);
		rlPromoCode = findViewById(R.id.rlPromoCode);
		tvOrderNumber = (TextView) findViewById(R.id.tvOrderNumber);
		rlAccountBalance = findViewById(R.id.cashBalanceLayout);
		tvBalance = (TextView) findViewById(R.id.cashBalanceTextView);
		rlVoucher = findViewById(R.id.voucherLayout);
		tvVoucher = (TextView) findViewById(R.id.voucherTextView);
		inviteView = findViewById(R.id.inviteView);
		tvAuthenNum = (TextView) findViewById(R.id.tvAuthenNum);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvQianming = (TextView) findViewById(R.id.tvQianming);
		rlVehicles = findViewById(R.id.rlVehicles);
		tvVehiclesAuth = (TextView) findViewById(R.id.tvVehiclesAuth);
		tvVehicles = (TextView) findViewById(R.id.tvVehicles);
		rlMyPrivilege = findViewById(R.id.rlMyPrivilege);
		rlMessages = findViewById(R.id.fuliLayout);
		tvMessageNumber = (TextView) findViewById(R.id.fuliTextView);
		ivKefu = (ImageView) findViewById(R.id.ivKefu);
		rlShare = findViewById(R.id.rlShare);
		rlMore = findViewById(R.id.rlMore);
		advertView = findViewById(R.id.advertView);
		advertView.setVisibility(View.GONE);
		newFuli = findViewById(R.id.new_fuli);
	}

	@Override
	protected void loadData() {
		optionsPhoto = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.avatar_defult)
		.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		count = sp.getLong("authenticationMemberCount", 0);
		dFormat = new DecimalFormat("#.##");
		dFormat.setMinimumFractionDigits(2);
		getAdvertData();
	}

	@Override
	protected void setListener() {
		headView.setOnClickListener(this);
		rlMyOrder.setOnClickListener(this);
		rlAccountBalance.setOnClickListener(this);
		rlVoucher.setOnClickListener(this);
		inviteView.setOnClickListener(this);
		rlVehicles.setOnClickListener(this);
		rlMyPrivilege.setOnClickListener(this);
		rlMessages.setOnClickListener(this);
		ivKefu.setOnClickListener(this);
		rlShare.setOnClickListener(this);
		rlMore.setOnClickListener(this);
		rlPromoCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		if(v.equals(headView)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
			}
			else{
				EventAnalysisUtil.onEvent(this, "edit_personalData", "点编辑资料", null);
				startActivity(new Intent(IndividualCenter.this, UserInfoActivity.class));
			}
		}
		else if(v.equals(rlMyOrder)){
			EventAnalysisUtil.onEvent(this, "goto_myOrder", "进入我的订单", null);
			onOrderClick(0);
		}
		else if(v.equals(rlAccountBalance)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
			}
			else{
				EventAnalysisUtil.onEvent(this, "goto_accountBalance", "进入账户余额", null);
				intent.setClass(IndividualCenter.this, VehicleServiceActivity.class);
				intent.putExtra("appId", "10000006");
				String strStartPage = "cash.html?uid=" + mApplication.getId();
				if(mApplication.getpId() != 0)
					strStartPage += ("&pid=" + mApplication.getpId());
				intent.putExtra("startPage", strStartPage);
				startActivity(intent);
			}
		}
		else if(v.equals(rlVoucher)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
			}
			else{
				EventAnalysisUtil.onEvent(this, "goto_voucher", "进入代金券", null);
				intent.setClass(IndividualCenter.this, CouponActivity.class);
				intent.putExtra("count", userInfo.getCoupon() == null ? 0 : userInfo.getCoupon().intValue());
				startActivity(intent);
			}
		}
		else if(v.equals(inviteView)){
			EventAnalysisUtil.onEvent(this, "click_banner_center", "认证就送千元礼包", null);
			startActivity(new Intent(IndividualCenter.this, MemberPrivilegeActivity.class));
		}
		else if(v.equals(rlVehicles)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
			}
			else{
				EventAnalysisUtil.onEvent(this, "goto_myVehicle", "进入我的车辆", null);
				startActivity(new Intent(IndividualCenter.this, AllVehiclesActivity.class));
			}
		}
		else if(v.equals(rlMyPrivilege))
		{
			EventAnalysisUtil.onEvent(this, "goto_myPrivilege", "进入我的特权", null);
			startActivity(new Intent(IndividualCenter.this, PrivilegeActivity.class));
		}
		else if(v.equals(rlMessages)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
			}
			else {
				EventAnalysisUtil.onEvent(this, "goto_message", "进入消息盒子", null);
				startActivity(new Intent(IndividualCenter.this, MessageBoxActivity.class));
			}
		}
		else if(v.equals(ivKefu)){
			EventAnalysisUtil.onEvent(this, "goto_customService", "进入客服", null);
			if(mApplication.isUseWKF()){
				if(mApplication.getId() == 0){
					startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
				}
				else if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
					getUserOpenIm();
				}
				else{
					mApplication.initTaobao();
					if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.success ){
						intent = mApplication.mYWIMKIT.getChattingActivityIntent(new EServiceContact("橙牛汽车管家", 156887186));
						startActivity(intent);
					}
					else if(mApplication.mYWIMKIT.getIMCore().getLoginState() == YWLoginState.logining ){
						ToastUtil.TextToast(IndividualCenter.this, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
					else{
						ToastUtil.TextToast(IndividualCenter.this, "正在打开在线客服", Toast.LENGTH_SHORT, Gravity.CENTER);
						mApplication.loginTaobao(mApplication.getOpenImUserId(), mApplication.getOpenImPassWord());
					}
				}
			}
			else{
				FeedbackAgent agent = new FeedbackAgent(this);
				com.umeng.fb.model.UserInfo ui = agent.getUserInfo();
				if(ui == null)
					ui = new com.umeng.fb.model.UserInfo();
				Map<String,String> contact = ui.getContact();
				if(contact == null)
					contact = new HashMap<String, String>();
				contact.put("uid", "" + mApplication.getUserId());
				ui.setContact(contact);
				agent.setUserInfo(ui);
				agent.startFeedbackActivity();
			}
		}
		else if(v.equals(rlShare)){
			if(mApplication.getId() == 0){
				ToastUtil.TextToast(IndividualCenter.this, "分享需要绑定手机号", Toast.LENGTH_LONG, Gravity.CENTER);
				intent.setClass(this, PhoneRegistActivity.class);
				intent.putExtra("isMustPhone", true);
				intent.putExtra("isShare", true);
				startActivityForResult(intent, REQUEST_FOR_SHARE);
			}
			else{
				EventAnalysisUtil.onEvent(this, "click_sharePrize", "点分享有奖", null);
				intent.setClass(this, Share.class);
				intent.putExtra("type", KplusConstants.SHARE_APP);
				startActivity(intent);
			}
		}
		else if(v.equals(rlMore)){
			EventAnalysisUtil.onEvent(this, "goto_more", "进入更多", null);
			startActivity(new Intent(IndividualCenter.this, MoreFunctionActivity.class));
		}
        else if (v.equals(rlPromoCode)){
			if(mApplication.getId() == 0){
				startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
			}
			else {
				Intent it = new Intent(this, PromoCodeActivity.class);
				startActivityForResult(it, Constants.REQUEST_TYPE_PROMO_CODE);
			}
        }
	}
	
	private void onOrderClick(int ordeStatus){
		if(mApplication.getId() == 0){
			startActivity(new Intent(IndividualCenter.this, PhoneRegistActivity.class));
		}
		else{
			Intent intent = new Intent();
			intent.setClass(IndividualCenter.this, OrderListActivity.class);
			intent.putExtra("ordeStatus", ordeStatus);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(IndividualCenter.this).unregisterReceiver(mReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(newMessageReceiver);
		needRefresh = false;
		handler.removeMessages(2);
		super.onPause();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_SHARE){
			if(mApplication.getpId() != 0){
				Intent intent = new Intent();
				intent.setClass(this, Share.class);
				intent.putExtra("type", KplusConstants.SHARE_APP);
				startActivity(intent);
			}
		}
        else if (requestCode == Constants.REQUEST_TYPE_PROMO_CODE){
            if (resultCode == RESULT_OK){
				int count = data.getIntExtra("count", 1);
				Intent alertIntent = new Intent(this, AlertDialogActivity.class);
				alertIntent.putExtra("alertType", KplusConstants.ALERT_PROMO_CODE_EXCHANGE);
				alertIntent.putExtra("message", "成功兑换" + count + "张代金券");
				alertIntent.putExtra("count", (userInfo.getCoupon() == null ? 0 : userInfo.getCoupon().intValue()) + count);
				startActivityForResult(alertIntent, Constants.REQUEST_TYPE_CLOSE);
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class GetUserInfoTask extends AsyncTask<Void, Void, GetUserInfoResponse>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected GetUserInfoResponse doInBackground(Void... params) {
			try{
				GetUserInfoRequest request = new GetUserInfoRequest();
				request.setParams(mApplication.getId());
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(GetUserInfoResponse result) {
			try{
				if(result != null && result.getCode() != null && result.getCode() == 0){
					GetUserInfoJson data = result.getData();
					if(data != null){
						userInfo = data.getUserInfo();
						headImgUrl = userInfo.getIconUrl();
						mApplication.imageLoader.displayImage(headImgUrl, ivPhoto, optionsPhoto);
						tvOrderNumber.setText((userInfo.getOrderCount() == null ? 0 : userInfo.getOrderCount()) + "笔");
						float balance = 0f;
						if(Math.abs(userInfo.getCashBalance() - 0) < 0.0001) {
							tvBalance.setText("0.00");
							balance = 0;
						}
						else {
							balance = userInfo.getCashBalance();
							tvBalance.setText(dFormat.format(userInfo.getCashBalance()));
						}

						// 设置用户余额，传到洗车那边
						mApplication.setCarWashingUserBalance(balance);
						tvVoucher.setText(String.valueOf(userInfo.getCoupon() == null ? 0 : userInfo.getCoupon().intValue()));
						if(!StringUtils.isEmpty(userInfo.getName()))
							tvPhoneNumber.setText(userInfo.getName());
						if(!StringUtils.isEmpty(userInfo.getInfo())){
							tvQianming.setText(userInfo.getInfo());
							tvQianming.setVisibility(View.VISIBLE);
						}
						else
							tvQianming.setVisibility(View.GONE);
						mApplication.dbCache.saveUserInfo(userInfo);
						DazeUserAccount dua = mApplication.dbCache.getDazeUserAccount(mApplication.getId());
						if(dua == null){
							dua = new DazeUserAccount();
							dua.setUid(mApplication.getId());
							dua.setNickName(userInfo.getName());
						}
						List<Account> accounts = userInfo.getAccounts();
						if(accounts != null && !accounts.isEmpty()){
							mApplication.dbCache.saveAccounts(accounts);
							if(mApplication.getpId() == 0){
								for(Account ac : accounts){
									if(ac.getType() == 0){
										mApplication.setpId(ac.getPid());
										mApplication.dbCache.putValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE, ac.getUserName());
										if(StringUtils.isEmpty(userInfo.getName()))
											tvPhoneNumber.setText(ac.getUserName());
										dua.setPhoneNumber(ac.getUserName());
										dua.setPhoneLoginName(ac.getUserName());
									}
									else if(ac.getType() == 1){
										dua.setQqLoginName(ac.getUserName());
									}
									else if(ac.getType() == 2){
										dua.setWechatLoginName(ac.getUserName());								
									}
									else if(ac.getType() == 3){
										dua.setWeiboLoginName(ac.getUserName());
									}
								}
							}
						}
						mApplication.dbCache.saveDazeUserAccount(dua);
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}
	
	private void updateUI(){
		getClientLicenceCount();
		tvAuthenNum.setText("" + count);
		if(mApplication.getId() == 0){
			tvVehicles.setText("");
			tvVehiclesAuth.setText("");
			inviteView.setVisibility(View.VISIBLE);
		}
		else{
			vehicleAuths = mApplication.dbCache.getVehicleAuths();
			userVehicles = mApplication.dbCache.getVehicles();
			selfSuccessVehicleAuths = new ArrayList<VehicleAuth>();
			if(userVehicles != null && !userVehicles.isEmpty()){
				if(userVehicles.size() == 1)
					tvVehicles.setText(userVehicles.get(0).getVehicleNum());
				else
					tvVehicles.setText(userVehicles.size() + "辆/");
			}
			else
				tvVehicles.setText("");
			if(vehicleAuths != null && !vehicleAuths.isEmpty()){
				for(VehicleAuth va : vehicleAuths){
					if(va.getStatus() == 2){
						if(va.getBelong() != null && va.getBelong() == true){
							selfSuccessVehicleAuths.add(va);
						}
					}
				}
				if(selfSuccessVehicleAuths != null && selfSuccessVehicleAuths.size() > 0){
					if(userVehicles != null && userVehicles.size() == 1)
						tvVehiclesAuth.setText("已认证");
					else
						tvVehiclesAuth.setText(selfSuccessVehicleAuths.size() + "辆已认证");
				}
				else{
					if(userVehicles != null && !userVehicles.isEmpty()){
						tvVehiclesAuth.setText("0辆已认证");
					}
					else
						tvVehiclesAuth.setText("");
				}
			}
			else{
				if(userVehicles != null && !userVehicles.isEmpty()){
					tvVehiclesAuth.setText("0辆已认证");
				}
				else
					tvVehiclesAuth.setText("");
			}
			if(!selfSuccessVehicleAuths.isEmpty()){
				inviteView.setVisibility(View.GONE);
				advertView.setVisibility(View.VISIBLE);
			}
			else{
				inviteView.setVisibility(View.VISIBLE);
				advertView.setVisibility(View.GONE);
			}
			getAuthDetail();
		}		
	}
	
	private void getClientLicenceCount(){
		new Thread(){
			public void run(){
				try{
					GetClientLicenceCountRequest request = new GetClientLicenceCountRequest();
					GetClientLicenceCountResponse result = mApplication.client.execute(request);
					if(result != null){
						if(result.getCode() != null && result.getCode() == 0){
							count = result.getData().getCount();
							sp.edit().putLong("authenticationMemberCount", count).commit();
							if(needRefresh){
								Message msg = new Message();
								msg.what = 2;
								handler.sendMessageDelayed(msg, 15000);
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void getAuthDetail(){
		if(mApplication.getUserId() != 0 && mApplication.getId() != 0){
			long lastAuthDetailUpdateTime = sp.getLong("lastAuthDetailUpdateTime", 0);
			if((System.currentTimeMillis() - lastAuthDetailUpdateTime) > 20*1000){
				sp.edit().putLong("lastAuthDetailUpdateTime", System.currentTimeMillis()).commit();
				final List<UserVehicle> listUVs = mApplication.dbCache.getVehicles();
				if(listUVs != null && !listUVs.isEmpty()){
					StringBuilder sb = null;
					for(UserVehicle uv : listUVs){
						if(!uv.isHiden()){
							String vn = uv.getVehicleNum();
							if(!StringUtils.isEmpty(vn)){
								if(sb == null){
									sb = new StringBuilder();
									sb.append(vn);
								}
								else
									sb.append("," + vn);
							}
						}
					}
					if(!StringUtils.isEmpty(sb.toString())){
						final String strVehicles = sb.toString();
						new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
							@Override
							protected GetAuthDetaiResponse doInBackground(Void... params) {
								try{
									GetAuthDetaiRequest request = new GetAuthDetaiRequest();
									request.setParams(mApplication.getUserId(), mApplication.getId(), strVehicles);
									return mApplication.client.execute(request);
								}
								catch(Exception e){
									e.printStackTrace();
									return null;
								}
							}
							
							protected void onPostExecute(GetAuthDetaiResponse result) {
								if(result != null && result.getCode() != null && result.getCode() == 0){
									List<VehicleAuth> listVA = result.getData().getList();
									if(listVA != null && !listVA.isEmpty()){
										mApplication.dbCache.saveVehicleAuths(listVA);
										updateUI();
										startService(new Intent(IndividualCenter.this, UpdateUserVehicleService.class));
									}
								}
							}
						}.execute();
					}
				}
			}
		}
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
					}
					else{
						ToastUtil.TextToast(IndividualCenter.this, !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					ToastUtil.TextToast(IndividualCenter.this, result != null && !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "获取账户信息失败", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
	}

	private void getAdvertData(){
		new AsyncTask<Void, Void, GetAdvertDataResponse>(){
			@Override
			protected GetAdvertDataResponse doInBackground(Void... params) {
				GetAdvertDataRequest request = new GetAdvertDataRequest();
				if (!StringUtils.isEmpty(mApplication.getCityId())){
					try {
						request.setParams(Long.parseLong(mApplication.getCityId()), mApplication.getUserId(), mApplication.getId(),KplusConstants.ADVERT_USER_BODY);
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
						List<Advert> adverts = data.getUserBody();
						if(adverts != null && !adverts.isEmpty()){
							mApplication.setUserBodyAdvert((ArrayList<Advert>)adverts);
							AdvertFragment adf = AdvertFragment.newInstance(KplusConstants.ADVERT_USER_BODY);
							getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_top, 0).add(R.id.advertView, adf).commit();
						}
					}
				}
			}
		}.execute();
	}

	private void getMessageCount(){
		new GetMessageCountTask(mApplication){
			@Override
			protected void onPostExecute(GetMessageCountResponse response) {
				if (response != null && response.getCode() != null && response.getCode() == 0){
					tvMessageNumber.setText(String.valueOf(response.getData().getValue()));
				}
			}
		}.execute();
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
	}
}
