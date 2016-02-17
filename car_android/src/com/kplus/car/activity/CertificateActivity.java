package com.kplus.car.activity;

import com.kplus.car.R;
import com.kplus.car.util.StringUtils;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class CertificateActivity extends BaseActivity {
	private ImageView ivImage;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_certificate);
		ivImage = (ImageView) findViewById(R.id.ivImage);
	}

	@Override
	protected void loadData() {
		String flag = getIntent().getStringExtra("flag");
		if(!StringUtils.isEmpty(flag)){
			if(flag.equals("driverImageTip")){
				ivImage.setImageResource(R.drawable.drivinglicensedemo);
			}
			else if(flag.equals("drivingImageTip")){
				ivImage.setImageResource(R.drawable.vehiclelicensedemo);
			}
		}
		else
			finish();
		
	}

	@Override
	protected void setListener() {
		ivImage.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(0, R.anim.slide_out_to_right);
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(0, R.anim.slide_out_to_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
