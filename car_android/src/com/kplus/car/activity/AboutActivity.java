package com.kplus.car.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ancun.service.AppService;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.util.BaseHelper;
import com.kplus.car.util.SIMCardInfo;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

public class AboutActivity extends BaseActivity implements OnClickListener
{
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View phone;
	private TextView tvPhone;
	private String toPhone;
	private TextView appVersion;

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_about);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("联系我们");
		phone = findViewById(R.id.llPhone);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		appVersion = (TextView) findViewById(R.id.about_app_version);
		appVersion.setText("V" + BaseHelper.getVersion(this));
	}

	@Override
	protected void loadData()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener()
	{
		leftView.setOnClickListener(this);
		phone.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.leftView:
				finish();
				break;
			case R.id.llPhone:
//				SIMCardInfo sim = new SIMCardInfo(AboutActivity.this);
				toPhone = tvPhone.getText().toString().trim();
//				String callPhone = mApplication.dbCache.getValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE);
//				if(StringUtils.isEmpty(callPhone))
//					callPhone = sim.getNativePhoneNumber();
//				if(!StringUtils.isEmpty(callPhone) && SIMCardInfo.isMobileNO(callPhone)){
//					AppService appService = new AppService(AboutActivity.this, mHandler);
////					appService.call(callPhone, toPhone, "4008066330");
//					appService.call(callPhone, "13758116235", "4008066330");
//				}
//				else{
//					ToastUtil.TextToast(AboutActivity.this, "登陆可开启电话录音功能", Toast.LENGTH_LONG, Gravity.CENTER);
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+toPhone));
					startActivity(intent);
//				}				
				break;
			default:
				break;
			}
	}
	
	private Handler mHandler=new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what==com.ancun.core.Constant.Handler.CALL_HANDLER_SUCCESS){
				//呼叫成功处理
				ToastUtil.TextToast(AboutActivity.this, "已开启电话录音功能", Toast.LENGTH_LONG, Gravity.CENTER);
			}else{
				//其它异常处理
				Toast.makeText(AboutActivity.this, String.valueOf(msg.obj), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+toPhone));
				startActivity(intent);
			}
			return false;
		}
		
	});


}
