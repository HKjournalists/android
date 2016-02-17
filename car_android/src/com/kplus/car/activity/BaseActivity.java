package com.kplus.car.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public abstract class BaseActivity extends FragmentActivity {
	public KplusApplication mApplication;
	public SharedPreferences sp;
	protected DisplayImageOptions options;

	public void onCreate(Bundle savedInstanceState) {
		Log.i(this.getClass().getName(), this.getClass().getName() + ",start...");
		super.onCreate(savedInstanceState);
		mApplication = (KplusApplication) getApplication();
		sp = getSharedPreferences("kplussp", MODE_PRIVATE);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.daze_car_default)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0)).build();
		initView();
		loadData();
		setListener();
	}

	@Override
	protected void onPause() {
		super.onPause();
		EventAnalysisUtil.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventAnalysisUtil.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventAnalysisUtil.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventAnalysisUtil.onStop(this);
	}

	protected abstract void initView();

	protected abstract void loadData();

	protected abstract void setListener();

	protected void showloading(boolean b) {
		if (b) {
			View loading = findViewById(R.id.page_loading);
			loading.setVisibility(View.VISIBLE);
		} else {
			View loading = findViewById(R.id.page_loading);
			loading.setVisibility(View.GONE);
		}
	}

	public static int dip2px(Context context, float dpValue) {
        float scale = 1;
        if(context != null) {
            try {
                scale = context.getResources().getDisplayMetrics().density;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
		return (int) (dpValue * scale + 0.5f);
	}

	protected int getViewHeight(Drawable tempImg) {
		int width = tempImg.getIntrinsicWidth();
		int height = tempImg.getIntrinsicHeight();
		int viewWidth = getResources().getDisplayMetrics().widthPixels - 20;
		return viewWidth * height / width;
	}

	protected void setImageView(ImageView imageView, String imageUrl) {
		mApplication.imageLoader.displayImage(imageUrl, imageView, options);
	}

	protected void setImageView(ImageView imageView, String imageUrl,
			int emptyResource) {
		DisplayImageOptions myOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(emptyResource).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0))
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		mApplication.imageLoader.displayImage(imageUrl, imageView, myOptions);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			outState.putString("appChannel", KplusConstants.appChannel);
			outState.putString("appKey", KplusConstants.CLIENT_APP_KEY);
			outState.putString("appSecret", KplusConstants.CLIENT_APP_SECRET);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null){
			if(StringUtils.isEmpty(KplusConstants.appChannel)) {
				if(!StringUtils.isEmpty(savedInstanceState.getString("appChannel")))
					KplusConstants.appChannel = savedInstanceState.getString("appChannel");
				else
					KplusConstants.appChannel = "android_default";
			}
			if(StringUtils.isEmpty(KplusConstants.CLIENT_APP_KEY)) {
				if(!StringUtils.isEmpty(savedInstanceState.getString("appKey")))
					KplusConstants.CLIENT_APP_KEY = savedInstanceState.getString("appKey");
				else
					KplusConstants.CLIENT_APP_KEY = "10000";
			}
			if(StringUtils.isEmpty(KplusConstants.CLIENT_APP_SECRET)) {
				if(!StringUtils.isEmpty(savedInstanceState.getString("appSecret")))
					KplusConstants.CLIENT_APP_SECRET = savedInstanceState.getString("appSecret");
				else
					KplusConstants.CLIENT_APP_SECRET = "LWLPg7pU4cwrcyy8PwDeGuaY0BHUoX";
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		if (mApplication.isFromInApp){
			mApplication.isFromInApp = false;
			Intent it = new Intent("finish");
			LocalBroadcastManager.getInstance(this).sendBroadcast(it);
		}
		super.onBackPressed();
	}
}
