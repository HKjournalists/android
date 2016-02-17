package com.kplus.car.activity;

import java.util.HashMap;
import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.response.ShareResultResponse;
import com.kplus.car.model.response.request.ShareResultRequest;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXShareListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Share extends BaseActivity implements OnClickListener, WXShareListener
{
	private TextView tvShareTitle;
	private ImageView ivCancel;
	private View toWechatmoment, toWechatFriend;
	private ImageView ivWechatmoment, ivWechatFriend;
	private TextView tvWechatmoment, tvWechatFriend;
	
	private int sdkVersion;
	private int sharetype;
	private String strContent;
	private String shareLabel;
	private IWXAPI iwxapi;

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_share);
		tvShareTitle = (TextView) findViewById(R.id.tvShareTitle);
		ivCancel = (ImageView) findViewById(R.id.ivCancel);
		toWechatmoment = findViewById(R.id.toWechatmoment);
		toWechatFriend = findViewById(R.id.toWechatFriend);
		ivWechatmoment = (ImageView) findViewById(R.id.ivWechatmoment);
		ivWechatFriend = (ImageView) findViewById(R.id.ivWechatFriend);
		tvWechatmoment = (TextView) findViewById(R.id.tvWechatmoment);
		tvWechatFriend = (TextView) findViewById(R.id.tvWechatFriend);
	}

	@Override
	protected void loadData()
	{
		sdkVersion = android.os.Build.VERSION.SDK_INT;
		sharetype = getIntent().getIntExtra("type", KplusConstants.SHARE_APP);
		iwxapi = WXAPIFactory.createWXAPI(Share.this, KplusConstants.WECHAT_APPID, true);
		iwxapi.registerApp(KplusConstants.WECHAT_APPID);
		switch(sharetype){
			case KplusConstants.SHARE_APP:
				tvShareTitle.setText("分享得 10元手续费代金券");
				strContent = "我最近在使用橙牛汽车管家，不但能查违章，还能在线缴费呢。现在认证，还有免费救援服务赠送，超划算！有车的小伙伴们也可以下载试试^_^www.chengniu.com/fx/";
				break;
			case KplusConstants.SHARE_ORDER:
				tvShareTitle.setText("分享得 10元手续费代金券");
				strContent = "我刚通过橙牛汽车管家成功处理了我的违章，足不出户，支付宝付款，省时省心！有车的小伙伴们也可以下载试试^_^www.chengniu.com/fx";
				break;
			default:
				break;
		}
	}

	@Override
	protected void setListener()
	{
		ivCancel.setOnClickListener(this);
		toWechatmoment.setOnClickListener(this);
		toWechatFriend.setOnClickListener(this);
		toWechatmoment.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(sdkVersion < 16)
						ivWechatmoment.setAlpha(128);
					else
						ivWechatmoment.setImageAlpha(128);
					if(sdkVersion >= 11)
						tvWechatmoment.setAlpha(0.5f);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					if(sdkVersion < 16)
						ivWechatmoment.setAlpha(255);
					else
						ivWechatmoment.setImageAlpha(255);
					if(sdkVersion >= 11)
						tvWechatmoment.setAlpha(1);
					shareToWechatmoment();
				}
				return false;
			}
		});
		toWechatFriend.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(sdkVersion < 16)
						ivWechatFriend.setAlpha(128);
					else
						ivWechatFriend.setImageAlpha(128);
					if(sdkVersion >= 11)
						tvWechatFriend.setAlpha(0.5f);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					if(sdkVersion < 16)
						ivWechatFriend.setAlpha(255);
					else
						ivWechatFriend.setImageAlpha(255);
					if(sdkVersion >= 11)
						tvWechatFriend.setAlpha(1);
					shareToWechat();
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v.equals(ivCancel)){
			EventAnalysisUtil.onEvent(this, "colse_sharePrize", "点关闭分享有奖说明", null);
			finish();
		}
	}
	
	private void shareToWechatmoment(){
		EventAnalysisUtil.onEvent(this, "click_circleFriend_share", "点通过朋友圈", null);
		shareLabel = "share_friends";
		if(isAppInstalled("com.tencent.mm")){
			wechatShare(1);
		}
		else{
			ToastUtil.TextToast(Share.this, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
	}
	
	private void shareToWechat(){
		EventAnalysisUtil.onEvent(this, "click_friends_share", "点通过微信好友", null);
		shareLabel = "share_wexin";
		if(isAppInstalled("com.tencent.mm")){
			wechatShare(0);
		}
		else{
			ToastUtil.TextToast(Share.this, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
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
				mApplication.setWxShareListener(Share.this);
			    WXWebpageObject webpage = new WXWebpageObject();  
			    webpage.webpageUrl = "http://chengniu.com/fx/";
			    WXMediaMessage msg = new WXMediaMessage(webpage);  
			    msg.title = strContent;  
			    msg.description = "";  
//			    //这里替换一张自己工程里的图片资源  
//			    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_logo);  
//			    msg.setThumbImage(thumb);  
			      
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
		Toast.makeText(Share.this, "分享成功", Toast.LENGTH_SHORT).show();
		mApplication.setWxShareListener(null);
		getShareResult();
	}

	@Override
	public void onShareCancel(BaseResp response) {
		Toast.makeText(Share.this, "已取消分享", Toast.LENGTH_SHORT).show();
		mApplication.setWxShareListener(null);
		finish();
	}

	@Override
	public void onShareFail(BaseResp response) {
		Toast.makeText(Share.this, "分享失败", Toast.LENGTH_SHORT).show();
		mApplication.setWxShareListener(null);
		finish();
	}
	
	private void getShareResult(){
		new AsyncTask<Void, Void, ShareResultResponse>(){

			@Override
			protected ShareResultResponse doInBackground(Void... params) {
				try{
					ShareResultRequest request = new ShareResultRequest();
					request.setParams(KplusConstants.SHARE_APP, mApplication.getpId(), 0,mApplication.getUserId(),null);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(ShareResultResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						if(result.getData().getValue() != null){
							Intent isr = new Intent(Share.this, ShareResult.class);
							isr.putExtra("reward", result.getData().getValue().intValue());
							startActivity(isr);
						}
					}
				}
				finish();
			}
			
		}.execute();
	}
}
