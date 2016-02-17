package com.kplus.car.activity;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXShareListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShareInService extends BaseActivity implements WXShareListener{
	private TextView tvTitle;
	private View toWechatmoment, toWechatFriend;
	private ImageView ivWechatmoment, ivWechatFriend;
	private TextView tvWechatmoment, tvWechatFriend;
	private String strContent = "";
	private String strTitle = "";
	private String strSummary = "";
	private String inviteUrl, imgUrl;
	private Bitmap bitmap;
	private IWXAPI iwxapi;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_share_in_service);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width =  WindowManager.LayoutParams.MATCH_PARENT;
		lp.gravity = Gravity.BOTTOM;
		getWindow().setAttributes(lp);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		toWechatmoment = findViewById(R.id.toWechatmoment);
		toWechatFriend = findViewById(R.id.toWechatFriend);
		ivWechatmoment = (ImageView) findViewById(R.id.ivWechatmoment);
		ivWechatFriend = (ImageView) findViewById(R.id.ivWechatFriend);
		tvWechatmoment = (TextView) findViewById(R.id.tvWechatmoment);
		tvWechatFriend = (TextView) findViewById(R.id.tvWechatFriend);
	}

	@Override
	protected void loadData() {
		try{
			strTitle = getIntent().getStringExtra("title");
			strSummary = getIntent().getStringExtra("summary");
			strContent = getIntent().getStringExtra("content");
			inviteUrl = getIntent().getStringExtra("inviteUrl");
			imgUrl = getIntent().getStringExtra("imgUrl");
			strSummary = strSummary.replaceAll("/n", "\n");
			tvTitle.setText(strTitle);
			iwxapi = WXAPIFactory.createWXAPI(ShareInService.this, KplusConstants.WECHAT_APPID, true);
			iwxapi.registerApp(KplusConstants.WECHAT_APPID);
		}catch(Exception e){
			e.printStackTrace();
			finish();
		}
	}

	@Override
	protected void setListener() {
		toWechatmoment.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					ViewCompat.setAlpha(ivWechatmoment, 0.5f);
					ViewCompat.setAlpha(tvWechatmoment, 0.5f);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					ViewCompat.setAlpha(ivWechatmoment, 1f);
					ViewCompat.setAlpha(tvWechatmoment, 1f);
					shareToWechatmoment();
				}
				return true;
			}
		});
		toWechatFriend.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					ViewCompat.setAlpha(ivWechatFriend, 0.5f);
					ViewCompat.setAlpha(tvWechatFriend, 0.5f);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					ViewCompat.setAlpha(ivWechatFriend, 1f);
					ViewCompat.setAlpha(tvWechatFriend, 1f);
					shareToWechat();
				}
				return true;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}
	
	private void shareToWechatmoment(){
		if(isAppInstalled("com.tencent.mm")){
			wechatShare(1);
		}
		else{
			ToastUtil.TextToast(ShareInService.this, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
	}
	
	private void shareToWechat(){
		if(isAppInstalled("com.tencent.mm")){
			wechatShare(0);
		}
		else{
			ToastUtil.TextToast(ShareInService.this, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
	}
	
	private boolean isAppInstalled(String uri) {
		PackageManager pm = getPackageManager();
		boolean installed =false;
		try {
			pm.getPackageInfo(uri,PackageManager.GET_ACTIVITIES);
			installed =true;
		} catch(PackageManager.NameNotFoundException e) {
			installed =false;
		}
		return installed;
	}
	
	private void wechatShare(final int flag){
		new Thread(new Runnable() {			
			@Override
			public void run() {
				mApplication.setWxShareListener(ShareInService.this);
				WXMediaMessage msg;
			    WXWebpageObject webpage = new WXWebpageObject();
			    if(!StringUtils.isEmpty(inviteUrl)){
			    	webpage.webpageUrl = inviteUrl;
			    	msg = new WXMediaMessage(webpage);
					msg.title = strContent;
			    }
			    else{
					if(strContent.contains("http://")){
						webpage.webpageUrl = strContent.substring(strContent.indexOf("http://"));
						msg = new WXMediaMessage(webpage);
						msg.title = strContent;
					}
					else if(strContent.contains("https://")){
						webpage.webpageUrl = strContent.substring(strContent.indexOf("https://"));
						msg = new WXMediaMessage(webpage);
						msg.title = strContent;
					}
					else {
						WXTextObject wxText = new WXTextObject(strContent);
						msg = new WXMediaMessage(wxText);
						msg.title = strTitle;
					}
			    }
			    msg.description = "";
			    if(bitmap != null && !bitmap.isRecycled())
			    	bitmap.recycle();
			    if(!StringUtils.isEmpty(imgUrl)){
			    	bitmap = BMapUtil.getBitmapFromUrl(ShareInService.this, imgUrl);
			    	msg.setThumbImage(bitmap);
			    }
			    SendMessageToWX.Req req = new SendMessageToWX.Req();  
			    req.transaction = String.valueOf(System.currentTimeMillis());  
			    req.message = msg;  
			    req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
			    iwxapi.sendReq(req);  
			}
		}).start();
	}

	@Override
	public void onShareSuccess(BaseResp response) {
		mApplication.setHasSuccessShare(true);
		mApplication.setWxShareListener(null);
		finish();
	}

	@Override
	public void onShareCancel(BaseResp response) {
		Toast.makeText(ShareInService.this, "已取消分享", Toast.LENGTH_SHORT).show();
		mApplication.setWxShareListener(null);
		finish();
	}

	@Override
	public void onShareFail(BaseResp response) {
		Toast.makeText(ShareInService.this, "分享失败", Toast.LENGTH_SHORT).show();
		mApplication.setWxShareListener(null);
		finish();
	}
}
