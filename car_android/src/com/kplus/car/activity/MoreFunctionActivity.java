package com.kplus.car.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;

public class MoreFunctionActivity extends BaseActivity implements OnClickListener
{
	private RelativeLayout rlHelp,rlUseragreement,rlAbout;
	private RelativeLayout rlPushTimeSet;
	private TextView tvPushTime;
	private ImageView ivLeft;
	
	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_more_function);
		rlHelp = (RelativeLayout) findViewById(R.id.rlHelp);
		rlUseragreement = (RelativeLayout) findViewById(R.id.rlUseragreement);
		rlAbout = (RelativeLayout) findViewById(R.id.rlAbout);
		rlPushTimeSet = (RelativeLayout) findViewById(R.id.rlPushTimeSet);
		tvPushTime = (TextView) findViewById(R.id.tvPushTime);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
	}

	@Override
	protected void loadData()
	{
	}

	@Override
	protected void setListener()
	{
		rlHelp.setOnClickListener(this);
		rlUseragreement.setOnClickListener(this);
		rlAbout.setOnClickListener(this);
		rlPushTimeSet.setOnClickListener(this);
		ivLeft.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.rlHelp:
			intent.setClass(MoreFunctionActivity.this, VehicleServiceActivity.class);
			intent.putExtra("startPage", "http://weizhang.51zhangdan.com/content/help.html");
			intent.putExtra("appId", "null");
			startActivity(intent);
			if(getParent() != null)
				getParent().overridePendingTransition(R.anim.slide_in_from_right, 0);
			break;
		case R.id.rlUseragreement:
			intent.setClass(MoreFunctionActivity.this, VehicleServiceActivity.class);
			intent.putExtra("startPage", KplusConstants.SERVER_URL + (KplusConstants.SERVER_URL.endsWith("/") ? "privacy.html" :"/privacy.html"));
			intent.putExtra("appId", "null");
			startActivity(intent);
			if(getParent() != null)
				getParent().overridePendingTransition(R.anim.slide_in_from_right, 0);
			break;
		case R.id.rlAbout:
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.rlPushTimeSet:
			EventAnalysisUtil.onEvent(this, "click_record_push", "点违章消息推送", null);
			intent.setClass(MoreFunctionActivity.this, PushTimeSetActivity.class);
			startActivity(intent);
			break;
		case R.id.ivLeft:
			finish();
			break;
			default:
				break;
		}
	}
	
	@Override
	protected void onResume()
	{
		String strFrom = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_START_TIME);
		if(StringUtils.isEmpty(strFrom))
			strFrom = "08:00";
		String strTo = mApplication.dbCache.getValue(KplusConstants.DB_KEY_PUSH_END_TIME);
		if(StringUtils.isEmpty(strTo))
			strTo = "23:00";
		tvPushTime.setText("推送时间 " + strFrom + "-" + strTo);		
		super.onResume();
	}
}
