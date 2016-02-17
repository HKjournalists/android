package com.kplus.car.activity;

import com.kplus.car.R;

import android.view.Window;
import android.widget.TextView;

public class ShareResult extends BaseActivity
{
	private TextView tvShareResult;
	private int reward;

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_share_result);
		tvShareResult = (TextView)findViewById(R.id.tvShareResult);
	}

	@Override
	protected void loadData()
	{
		reward = getIntent().getIntExtra("reward", 0);
		if(reward == 0)
			if(mApplication.getpId() == 0)
				tvShareResult.setText("需要先绑定手机号才能收取奖励,请登录后再分享");
			else
				tvShareResult.setText("已经领取过奖励，无法重复领取");
		else
			tvShareResult.setText("奖励金+" + reward + "元");
	}

	@Override
	protected void setListener()
	{
		// TODO Auto-generated method stub
		
	}
}
