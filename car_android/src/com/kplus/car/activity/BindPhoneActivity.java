package com.kplus.car.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.Response;
import com.kplus.car.model.Account;
import com.kplus.car.model.json.ChangePhoneResultJson;
import com.kplus.car.model.response.ChangePhoneResponse;
import com.kplus.car.model.response.request.ChangePhoneRequest;
import com.kplus.car.model.response.request.GetPhoneVerificationCodeRequest;
import com.kplus.car.util.SIMCardInfo;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

public class BindPhoneActivity extends BaseActivity implements OnClickListener {
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View bindView, switchView;
	private TextView tvBindedPhone, tvToSwitchPhone;
	private EditText phone_regist_phone, phone_regist_code;
	private TextView phone_regist_code_btn;
	private TextView bind_phone_confirm, tvSpeechVerify, tvSpeechVerifyHint;
	private View speechVerifyView, toHelp;
	
	private String bindedPhoneNumber;
	private String phoneNumber;
	private int verifyType;//0 短信验证码 1 语音验证码
	private Account account;
	private long id;
	private Handler handler = new Handler() {
		private int time = 59;

		public void handleMessage(android.os.Message msg) {
			if(verifyType == 0){
				if (time > 0) {
					phone_regist_code_btn.setText("重新获取(" + time + ")");
					time--;
					Message message = new Message();
					handler.sendMessageDelayed(message, 1000);
				} else {
					phone_regist_code_btn.setText("重新获取");
					phone_regist_code_btn.setTextColor(getResources().getColor(R.color.daze_black2));
					tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
					tvSpeechVerifyHint.setVisibility(View.GONE);
					phone_regist_code_btn.setEnabled(true);
					speechVerifyView.setEnabled(true);
					time = 60;
				}
			}
			else if(verifyType == 1){
				if(time > 0){
					speechVerifyView.setVisibility(View.GONE);
					tvSpeechVerifyHint.setVisibility(View.VISIBLE);
					tvSpeechVerifyHint.setText("验证码将通过电话呼入并播报,请输入您听到的验证码。(" + time + ")");
					time--;
					Message message = new Message();
					handler.sendMessageDelayed(message, 1000);
				}
				else{
					speechVerifyView.setVisibility(View.VISIBLE);
					tvSpeechVerifyHint.setVisibility(View.GONE);
					phone_regist_code_btn.setTextColor(getResources().getColor(R.color.daze_black2));
					tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
					phone_regist_code_btn.setEnabled(true);
					speechVerifyView.setEnabled(true);
					time = 60;
				}
			}
		};
	};

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_bind_phone_number);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("绑定手机号");
		bindView = findViewById(R.id.bindView);
		switchView = findViewById(R.id.switchView);
		tvBindedPhone = (TextView) findViewById(R.id.tvBindedPhone);
		tvToSwitchPhone = (TextView) findViewById(R.id.tvToSwitchPhone);
		phone_regist_phone = (EditText) findViewById(R.id.phone_regist_phone);
		phone_regist_code = (EditText) findViewById(R.id.phone_regist_code);
		phone_regist_code_btn = (TextView) findViewById(R.id.phone_regist_code_btn);
		speechVerifyView = findViewById(R.id.speechVerifyView);
		bind_phone_confirm = (TextView) findViewById(R.id.bind_phone_confirm);
		toHelp = findViewById(R.id.toHelp);
		tvSpeechVerify = (TextView) findViewById(R.id.tvSpeechVerify);
		tvSpeechVerifyHint = (TextView) findViewById(R.id.tvSpeechVerifyHint);
	}

	@Override
	protected void loadData() {
		account = mApplication.dbCache.getAccountByType(0);
		if(account == null){
			bindView.setVisibility(View.VISIBLE);
			switchView.setVisibility(View.GONE);
		}
		else{
			bindView.setVisibility(View.GONE);
			switchView.setVisibility(View.VISIBLE);
			bindedPhoneNumber = account.getUserName();
			if(!StringUtils.isEmpty(bindedPhoneNumber))
				tvBindedPhone.setText(bindedPhoneNumber);
		}
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		tvToSwitchPhone.setOnClickListener(this);
		phone_regist_code_btn.setOnClickListener(this);
		speechVerifyView.setOnClickListener(this);
		bind_phone_confirm.setOnClickListener(this);
		toHelp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.equals(leftView)){
			finish();
		}
		else if(v.equals(tvToSwitchPhone)){
			bindView.setVisibility(View.VISIBLE);
			switchView.setVisibility(View.GONE);
		}
		else if(v.equals(phone_regist_code_btn)){
			phoneNumber = phone_regist_phone.getText().toString();
			if(StringUtils.isEmpty(phoneNumber)){
				ToastUtil.TextToast(BindPhoneActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(!SIMCardInfo.isMobileNO(phoneNumber)){
				ToastUtil.TextToast(BindPhoneActivity.this, "手机号码输入错误！", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			phone_regist_code_btn.setEnabled(false);
			speechVerifyView.setEnabled(false);
			phone_regist_code_btn.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
			tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
			phone_regist_code.requestFocus();
			verifyType = 0;
			getVerifyCode(phoneNumber, "0");
		}
		else if(v.equals(speechVerifyView)){
			phoneNumber = phone_regist_phone.getText().toString();
			if(StringUtils.isEmpty(phoneNumber)){
				ToastUtil.TextToast(BindPhoneActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(!SIMCardInfo.isMobileNO(phoneNumber)){
				ToastUtil.TextToast(BindPhoneActivity.this, "手机号码输入错误！", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			phone_regist_code_btn.setEnabled(false);
			speechVerifyView.setEnabled(false);
			phone_regist_code_btn.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
			tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_darkgrey8));
			phone_regist_code.requestFocus();
			verifyType = 1;
			getVerifyCode(phoneNumber, "1");
		}
		else if(v.equals(bind_phone_confirm)){
			phoneNumber = phone_regist_phone.getText().toString();
			if (StringUtils.isEmpty(phoneNumber)) {
				ToastUtil.TextToast(BindPhoneActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			String code = phone_regist_code.getText().toString();
			if (StringUtils.isEmpty(code)) {
				ToastUtil.TextToast(BindPhoneActivity.this, "请输入验证码！", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			bindPhone(code);
		}
		else if(v.equals(toHelp)){
			Intent intent = new Intent(BindPhoneActivity.this, VehicleServiceActivity.class);
			intent.putExtra("startPage", "http://weizhang.51zhangdan.com/content/help.html");
			intent.putExtra("appId", "null");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_from_right, 0);
		}
	}
	
	private void getVerifyCode(final String phone, final String type) {
		showloading(true);
		new AsyncTask<Object, Object, Response>() {
			private String errortext = "网络中断，请稍后重试";
			@Override
			protected Response doInBackground(Object... params) {
				GetPhoneVerificationCodeRequest request = new GetPhoneVerificationCodeRequest();
				request.setParams(phone, type);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					errortext = e.toString();
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(Response result) {
				if (result != null) {
					if (result.getCode() != null && result.getCode() != 0) {
						Toast.makeText(BindPhoneActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
						phone_regist_code_btn.setEnabled(true);
						speechVerifyView.setEnabled(true);
						phone_regist_code_btn.setTextColor(getResources().getColor(R.color.daze_black2));
						tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
					} else {
						Message message = new Message();
						handler.sendMessageDelayed(message, 2);
					}
				} else {
					ToastUtil.TextToast(BindPhoneActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
					phone_regist_code_btn.setEnabled(true);
					speechVerifyView.setEnabled(true);
					phone_regist_code_btn.setTextColor(getResources().getColor(R.color.daze_black2));
					tvSpeechVerify.setTextColor(getResources().getColor(R.color.daze_blue3));
				}
				showloading(false);
			}
		}.execute();
	}
	
	private void bindPhone(final String code){
		new AsyncTask<Void, Void, ChangePhoneResponse>(){
			private String errortext = "网络中断，请稍后重试";
			@Override
			protected ChangePhoneResponse doInBackground(Void... params) {
				try{
					ChangePhoneRequest request = new ChangePhoneRequest();
					request.setParams(mApplication.getUserId(), mApplication.getId(), phoneNumber,code);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					errortext = e.toString();
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(ChangePhoneResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						ChangePhoneResultJson data = result.getData();
						if(data != null){
							if(data.getResult() != null && data.getResult()	&& data.getUid() != null && data.getUid() != 0){
								id = data.getUid();
//								boolean isUidChange = (id == mApplication.getId());
//								if(isUidChange){
//									ArrayList<ArrayList<AccountMode>> accountModeList = AccountUtils.getAccounts();
//									if(accountModeList != null && !accountModeList.isEmpty()){
//										for(ArrayList<AccountMode> subList : accountModeList){
//											if(subList.get(0) != null && subList.get(0).getUid() != null && subList.get(0).getUid() == mApplication.getId()){
//												accountModeList.remove(subList);
//												AccountUtils.saveAccounts(accountModeList);
//												break;
//											}
//										}
//									}
//								}
								ToastUtil.TextToast(BindPhoneActivity.this, "绑定成功", Toast.LENGTH_SHORT, Gravity.CENTER);
//								mApplication.setId(id);
								mApplication.dbCache.putValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE, phoneNumber);
//								Intent intentUpdateUserInfo = new Intent(BindPhoneActivity.this, UpdateUserInfoService.class);
//								intentUpdateUserInfo.putExtra("uid", id);
//								startService(intentUpdateUserInfo);
								finish();
							}
							else{
								ToastUtil.TextToast(BindPhoneActivity.this, "绑定手机号失败，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
						}
						else{
							ToastUtil.TextToast(BindPhoneActivity.this, "绑定手机号失败，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
					}
					else{
						ToastUtil.TextToast(BindPhoneActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					ToastUtil.TextToast(BindPhoneActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
			
		}.execute();
	}

}
