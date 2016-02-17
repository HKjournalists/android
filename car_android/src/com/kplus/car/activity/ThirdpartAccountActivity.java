package com.kplus.car.activity;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.Account;
import com.kplus.car.model.json.ThirdpartBindResultJson;
import com.kplus.car.model.response.BindThirdPartResponse;
import com.kplus.car.model.response.UnBindThirdPartResponse;
import com.kplus.car.model.response.request.BindThirdPartRequest;
import com.kplus.car.model.response.request.UnBindThirdPartRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXAuthListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdpartAccountActivity extends BaseActivity implements OnClickListener, WXAuthListener{
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View notBindedView, bindedView;
	private TextView tvNotBinded, bindThirdPart, tvInfoForBind;
	private TextView tvThirdpartLabel, tvThirdpartID,unBindThirdpart,tvCanNotUnbind;
	private ImageView ivIcon;
	
	private String thirdPartName;
	private int accountType;
	private Account account;
	private List<Account> accounts;
//	private long id;
	private String strRequest = null;
	private String strResponseSuccess = null;
	private String strResponseFail = null;
	private IWXAPI iwxapi;
	private Handler handle = new Handler();

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_third_part_account);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		notBindedView = findViewById(R.id.notBindedView);
		bindedView = findViewById(R.id.bindedView);
		tvNotBinded = (TextView) findViewById(R.id.tvNotBinded);
		bindThirdPart = (TextView) findViewById(R.id.bindThirdPart);
		tvInfoForBind = (TextView) findViewById(R.id.tvInfoForBind);
		tvThirdpartLabel = (TextView) findViewById(R.id.tvThirdpartLabel);
		tvThirdpartID = (TextView) findViewById(R.id.tvThirdpartID);
		unBindThirdpart = (TextView) findViewById(R.id.unBindThirdpart);
		tvCanNotUnbind = (TextView) findViewById(R.id.tvCanNotUnbind);
		ivIcon = (ImageView) findViewById(R.id.ivIcon);
	}

	@Override
	protected void loadData() {
		thirdPartName = getIntent().getStringExtra("thirdPartName");
		iwxapi = WXAPIFactory.createWXAPI(this, KplusConstants.WECHAT_APPID, true);
		iwxapi.registerApp(KplusConstants.WECHAT_APPID);
		if(thirdPartName.equals("weichat")){
			tvTitle.setText("微信账号");
			tvNotBinded.setText("目前没有绑定微信账号");
			bindThirdPart.setText("绑定微信账号");
			tvInfoForBind.setText("绑定后可用微信账号登陆橙牛");
			tvThirdpartLabel.setText("绑定的微信账号");
			unBindThirdpart.setText("解绑微信账号");
			accountType = 2;
		}
		account = mApplication.dbCache.getAccountByType(accountType);
		accounts = mApplication.dbCache.getAccounts();
		if(account != null && !StringUtils.isEmpty(account.getUserName())){
			tvThirdpartID.setText(account.getNickname());
			notBindedView.setVisibility(View.GONE);
			bindedView.setVisibility(View.VISIBLE);
			if(accounts != null && accounts.size() > 1){
				unBindThirdpart.setVisibility(View.VISIBLE);
				tvCanNotUnbind.setVisibility(View.GONE);
			}
			else{
				unBindThirdpart.setVisibility(View.GONE);
				tvCanNotUnbind.setVisibility(View.VISIBLE);
			}
			if(thirdPartName.equals("weichat")){
				ivIcon.setImageResource(R.drawable.linkstatus_wechat_linked);
			}
		}
		else{
			notBindedView.setVisibility(View.VISIBLE);
			bindedView.setVisibility(View.GONE);
			if(thirdPartName.equals("weichat")){
				ivIcon.setImageResource(R.drawable.linkstatus_wechat_unlinked);
			}
		}
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		bindThirdPart.setOnClickListener(this);
		unBindThirdpart.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		showloading(false);
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v.equals(leftView)){
			finish();
		}
		else if(v.equals(bindThirdPart)){
			thirdpartLogin(thirdPartName);
		}
		else if(v.equals(unBindThirdpart)){
			unBind();
		}
	}
	
	private void thirdpartLogin(String thirdpart){
		showloading(true);
		if(thirdpart.equals("weichat")){
			mApplication.setWxAuthListener(this);
			new Thread(new Runnable() {				
				@Override
				public void run() {
					SendAuth.Req req = new SendAuth.Req();
					req.scope = "snsapi_userinfo";
					req.state = "wechat_sdk_demo_test";
					iwxapi.sendReq(req);
				}
			}).start();
		}

	}
	
	private void unBind(){
		new AsyncTask<Void, Void, UnBindThirdPartResponse>(){
			private String errortext = "网络中断，请稍后重试";
			@Override
			protected UnBindThirdPartResponse doInBackground(Void... params) {
				try{
					UnBindThirdPartRequest request = new UnBindThirdPartRequest();
					request.setParams(mApplication.getId(), account.getUserName(), accountType);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					errortext = e.toString();
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(UnBindThirdPartResponse result) {
				try{
					if(result != null){
						if(result.getCode() != null && result.getCode() == 0 ){
							ThirdpartBindResultJson data = result.getData();
							if(data != null){
								if(data.getResult() != null && data.getResult()){
									ToastUtil.TextToast(ThirdpartAccountActivity.this, "解除绑定成功", Toast.LENGTH_SHORT, Gravity.CENTER);
									tvThirdpartID.setText("");
									notBindedView.setVisibility(View.VISIBLE);
									bindedView.setVisibility(View.GONE);
									if(thirdPartName.equals("weichat")){
										ivIcon.setImageResource(R.drawable.linkstatus_wechat_unlinked);
									}
									mApplication.dbCache.deleteAccountByType(accountType);
								}
								else
									ToastUtil.TextToast(ThirdpartAccountActivity.this, "解除绑定失败", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
							else
								ToastUtil.TextToast(ThirdpartAccountActivity.this, "解除绑定失败", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
						else
							ToastUtil.TextToast(ThirdpartAccountActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
					else{
						ToastUtil.TextToast(ThirdpartAccountActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.execute();
	}

	@Override
	public void onAuthSuccess(BaseResp response) {
		mApplication.setWxAuthListener(null);
		SendAuth.Resp sendResp = (SendAuth.Resp) response;
		if(!StringUtils.isEmpty(sendResp.code)){
			final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ KplusConstants.WECHAT_APPID + "&secret=" + KplusConstants.WECHAT_APPSECRET + "&code=" + sendResp.code + "&grant_type=authorization_code";
			new Thread(new Runnable() {				
				@Override
				public void run() {
					HttpGet httpGet = new HttpGet(url);
					try {
						HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
						if(httpResponse.getStatusLine().getStatusCode() == 200){
							String result = EntityUtils.toString(httpResponse.getEntity());
							if(!StringUtils.isEmpty(result)){
								JSONObject jsonResult = new JSONObject(result);
								String access_token = jsonResult.optString("access_token");
								String openid = jsonResult.optString("openid");
								if(!StringUtils.isEmpty(access_token)){
									getUserInfo(access_token, openid);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}).start();
		} 
	}
	
	private void getUserInfo(final String access_token, final String openid){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid; 
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
						if(!StringUtils.isEmpty(result)){
							final JSONObject jsonResult = new JSONObject(result);
							handle.post(new Runnable() {								
								@Override
								public void run() {
									bind(jsonResult.optString("openid"), jsonResult.optString("nickname"),2);
								}
							});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}).start();
	}
	
	private void bind(final String userName, final String nickName, final int accountType){
		strRequest = "bind_wexin";
		strResponseSuccess = "bind_wexin_success";
		strResponseFail = "bind_wexin_fail";
		new AsyncTask<Void, Void, BindThirdPartResponse>(){
			private String errortext = "网络中断，请稍后重试";
			protected void onPreExecute() {
				showloading(true);
			}
			@Override
			protected BindThirdPartResponse doInBackground(Void... params) {
				try{
					BindThirdPartRequest request = new BindThirdPartRequest();
					request.setParams(mApplication.getId(), userName, nickName, accountType);
					return mApplication.client.execute(request);
				}catch(Exception e){
					errortext = e.toString();
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(BindThirdPartResponse result) {
				showloading(false);
				try{
					if(result != null){
						if(result.getCode() != null && result.getCode() == 0){
							ThirdpartBindResultJson data = result.getData();
							if(data != null){
								if(data.getResult() != null && data.getResult() && data.getUid() != null && data.getUid() != 0){
									notBindedView.setVisibility(View.GONE);
									bindedView.setVisibility(View.VISIBLE);
									unBindThirdpart.setVisibility(View.VISIBLE);
									tvCanNotUnbind.setVisibility(View.GONE);
									ivIcon.setImageResource(R.drawable.linkstatus_wechat_linked);
									tvThirdpartID.setText(nickName);
									if(account == null){
										account = new Account();
									}
									account.setNickname(nickName);
									account.setUserName(userName);
									account.setType(accountType);
									mApplication.dbCache.saveAccount(account);
								} else {
									ToastUtil.TextToast(ThirdpartAccountActivity.this, "绑定失败", Toast.LENGTH_SHORT, Gravity.CENTER);
								}
							} else {
								ToastUtil.TextToast(ThirdpartAccountActivity.this, "绑定失败", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
						} else {
							ToastUtil.TextToast(ThirdpartAccountActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
						}
					} else {
						ToastUtil.TextToast(ThirdpartAccountActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.execute();
	}

	@Override
	public void onAuthCancel(BaseResp response) {
		mApplication.setWxAuthListener(null);
		if(!StringUtils.isEmpty(response.errStr))
			ToastUtil.TextToast(ThirdpartAccountActivity.this, response.errStr, Toast.LENGTH_SHORT, Gravity.CENTER);
	}

	@Override
	public void onAuthFail(BaseResp response) {
		mApplication.setWxAuthListener(null);
		if(!StringUtils.isEmpty(response.errStr))
			ToastUtil.TextToast(ThirdpartAccountActivity.this, response.errStr, Toast.LENGTH_SHORT, Gravity.CENTER);
	}
	
}
