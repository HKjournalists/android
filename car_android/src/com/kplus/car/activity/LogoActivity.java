package com.kplus.car.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.igexin.sdk.PushManager;
import com.kplus.car.BuildConfig;
import com.kplus.car.GexinSdkMsgReceiver;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.model.ImageInfo;
import com.kplus.car.service.InitService;
import com.kplus.car.service.UpgradeDataService;
import com.lotuseed.android.Lotuseed;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class LogoActivity extends BaseActivity {

	private static final String TAG = "LogoActivity";

	protected static final int MSG_FAILURE = 1;
	protected static final int MSG_SUCCESS = 0;
	private static final int REQUEST_FOR_SHOW_MESSAGE = 1;

	private ImageView logoLaunchImage;
	private RelativeLayout channelView;
	private ImageView ivChannel;
	private Bitmap bp;

	protected void loadData() {
		goHome();
	}

	private void goHome() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
				}
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
                String start_up_version = mApplication.dbCache.getValue("start_up_version");
                if (!KplusConstants.START_UP_VERSION.equals(start_up_version)){
                    Intent intent = new Intent(LogoActivity.this, StartUpActivity.class);
                    startActivity(intent);
                }
                else {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    Intent intent = new Intent(LogoActivity.this, MainUIActivity.class);
                    try{
                        if(getIntent().hasExtra("orderId") && getIntent().hasExtra("orderType")){
                            intent.putExtra("orderId", getIntent().getLongExtra("orderId", 0));
                            intent.putExtra("orderType", getIntent().getIntExtra("orderType", 0));
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
				finish();
				overridePendingTransition(R.anim.fade_in, R.anim.scale_fade_out);
				break;
			case MSG_FAILURE:
				handle();
				break;
			}
		}
	};

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_logo);

		initImages();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int nScreenWidth = dm.widthPixels;
		int nScreenHeight = dm.heightPixels;
		mApplication.setnScreenWidth(nScreenWidth);
		mApplication.setnScreenHeight(nScreenHeight);
		channelView = (RelativeLayout) findViewById(R.id.channelView);
		RelativeLayout.LayoutParams pc = (RelativeLayout.LayoutParams) channelView.getLayoutParams();
		pc.height = (int) (nScreenHeight*0.1);
		channelView.setLayoutParams(pc);
		ivChannel = (ImageView) findViewById(R.id.ivChannel);
		ivChannel.setVisibility(View.GONE);
		RelativeLayout.LayoutParams pi = (RelativeLayout.LayoutParams) ivChannel.getLayoutParams();
		pi.height = (int) (nScreenHeight*0.1*0.6);
		ivChannel.setLayoutParams(pi);
	}

	private void initImages(){
		try{
			ImageInfo image = mApplication.dbCache.getFlashImageInfo();
			if(image != null){
				logoLaunchImage = (ImageView) findViewById(R.id.logo_launch_image);
				File file = new File(image.getImagePath());
				if(file.exists()){
					setImageView(logoLaunchImage,Uri.fromFile(file).toString());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void setListener() {
	}

	public void handle() {
		Intent intent = new Intent(LogoActivity.this, AlertDialogActivity.class);
		intent.putExtra("alertType", KplusConstants.ALERT_ONLY_SHOW_MESSAGE);
		intent.putExtra("message", "服务器连接有问题，请检查网络重试！");
		startActivityForResult(intent, REQUEST_FOR_SHOW_MESSAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_SHOW_MESSAGE){
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		if(bp != null && !bp.isRecycled()){
			bp.recycle();
			bp = null;
		}
		super.onDestroy();
	}
}
