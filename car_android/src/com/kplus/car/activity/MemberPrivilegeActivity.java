package com.kplus.car.activity;

import com.kplus.car.R;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.GetClientLicenceCountResponse;
import com.kplus.car.model.response.request.GetClientLicenceCountRequest;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberPrivilegeActivity extends BaseActivity implements OnClickListener{
	private TextView tvTitle;
	private View rightView;
	private ImageView ivRight;
	private TextView tvMemberCount;
	private Button btAuthenticate;
	private long count;
	private boolean needRefresh;
	private String vehicleNumber;
	private VehicleAuth vehicleAuth;
	private WebView mWebView;
	private int status;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				tvMemberCount.setText(""+ count);
				getClientLicenceCount();
				break;
			default:
				break;
			}			
		}
	};
	
	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_member_privilege);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		if(getIntent().hasExtra("title"))
			tvTitle.setText(getIntent().getStringExtra("title"));
		else
			tvTitle.setText("认证会员特权");
		rightView = findViewById(R.id.rightView);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivRight.setImageResource(R.drawable.close);
		tvMemberCount = (TextView) findViewById(R.id.tvMemberCount);
		btAuthenticate = (Button) findViewById(R.id.btAuthenticate);
		mWebView = (WebView) findViewById(R.id.webview);
	}

	@Override
	protected void loadData() {
		EventAnalysisUtil.onEvent(this, "pageView_renzheng ", "认证会员页面流量", null);
		count = sp.getLong("authenticationMemberCount", 0);
		tvMemberCount.setText("" + count);
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicleNumber);
		status = 0;
		if(vehicleAuth != null){
			if(vehicleAuth.getStatus() != null){
				status = vehicleAuth.getStatus();
				if(status == 2 && vehicleAuth.getBelong() != null && vehicleAuth.getBelong() == false)
					status = 4;
			}
		}
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl("http://www.chengniu.com/home/renzhen");
	}

	@Override
	protected void setListener() {
		rightView.setOnClickListener(this);
		btAuthenticate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.equals(rightView)){
			finish();
		}
		else if(v.equals(btAuthenticate)){
			EventAnalysisUtil.onEvent(this, "click_identify_centerBanner", "认证就送千元礼包--立即认证", null);
			if(mApplication.getId() == 0){
				Intent intent = new Intent(MemberPrivilegeActivity.this, PhoneRegistActivity.class);
				intent.putExtra("isAuthen", true);
				startActivity(intent);
			}
			else{
				if(StringUtils.isEmpty(vehicleNumber)){
					Intent intent = new Intent(MemberPrivilegeActivity.this, AllVehiclesActivity.class);
					startActivity(intent);
					finish();
				}
				else{
					Intent intent = new Intent(MemberPrivilegeActivity.this, EmergencyLicenseActivity.class);
					intent.putExtra("vehicleNumber", vehicleNumber);
					startActivity(intent);
					finish();
				}
			}
		}
	}
	
	@Override
	protected void onResume() {
		needRefresh = true;
		getClientLicenceCount();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		needRefresh = false;
		handler.removeMessages(1);
		super.onPause();
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
								msg.what = 1;
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

}
