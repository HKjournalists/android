package com.kplus.car.activity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.wxapi.WXShareListener;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class ShareMessage extends BaseActivity implements WXShareListener {
    private View toWechatmoment, toWechatFriend;
    private ImageView ivWechatmoment, ivWechatFriend;
    private TextView tvWechatmoment, tvWechatFriend;
    private Button btCancelShare;
    private String shareType;
    private String title = "";
    private String url;
    private String image;
    private Bitmap bitmap;
    private int sdkVersion;
    private IWXAPI iwxapi;
    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.daze_message_share_layout);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =  WindowManager.LayoutParams.FILL_PARENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        toWechatmoment = findViewById(R.id.toWechatmoment);
        toWechatFriend = findViewById(R.id.toWechatFriend);
        ivWechatmoment = (ImageView) findViewById(R.id.ivWechatmoment);
        ivWechatFriend = (ImageView) findViewById(R.id.ivWechatFriend);
        tvWechatmoment = (TextView) findViewById(R.id.tvWechatmoment);
        tvWechatFriend = (TextView) findViewById(R.id.tvWechatFriend);
        btCancelShare = (Button) findViewById(R.id.btCancelShare);
    }

    @Override
    protected void loadData() {
        try{
            sdkVersion = android.os.Build.VERSION.SDK_INT;
            iwxapi = WXAPIFactory.createWXAPI(ShareMessage.this, KplusConstants.WECHAT_APPID, true);
            iwxapi.registerApp(KplusConstants.WECHAT_APPID);
            shareType = getIntent().getStringExtra("shareType");
            if(StringUtils.isEmpty(shareType)){
                Toast.makeText(this, "获取分享渠道失败", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                title = getIntent().getStringExtra("title");
                url = getIntent().getStringExtra("url");
                image = getIntent().getStringExtra("image");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void setListener() {
        btCancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toWechatmoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        toWechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        toWechatmoment.setOnTouchListener(new View.OnTouchListener() {
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
        toWechatFriend.setOnTouchListener(new View.OnTouchListener() {
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

    private void shareToWechatmoment(){
        if(isAppInstalled("com.tencent.mm")){
            wechatShare(1);
        }
        else{
            ToastUtil.TextToast(ShareMessage.this, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
            finish();
        }
    }

    private void shareToWechat(){
        if(isAppInstalled("com.tencent.mm")){
            wechatShare(0);
        }
        else{
            ToastUtil.TextToast(ShareMessage.this, "微信客户端未安装，无法分享", Toast.LENGTH_SHORT, Gravity.CENTER);
            finish();
        }
    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean installed =false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
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
                mApplication.setWxShareListener(ShareMessage.this);
                WXMediaMessage msg;
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;
                msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = "";
                if(bitmap != null && !bitmap.isRecycled())
                    bitmap.recycle();
                if(!StringUtils.isEmpty(image)){
                    bitmap = BMapUtil.getBitmapFromUrl(ShareMessage.this, image);
                    msg.setThumbImage(bitmap);
                }
                else {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.daze_icon);
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
        Toast.makeText(ShareMessage.this, "分享成功", Toast.LENGTH_SHORT).show();
        mApplication.setHasSuccessShare(true);
        mApplication.setWxShareListener(null);
        finish();
    }

    @Override
    public void onShareCancel(BaseResp response) {
        Toast.makeText(ShareMessage.this, "已取消分享", Toast.LENGTH_SHORT).show();
        mApplication.setWxShareListener(null);
        finish();
    }

    @Override
    public void onShareFail(BaseResp response) {
        Toast.makeText(ShareMessage.this, "分享失败", Toast.LENGTH_SHORT).show();
        mApplication.setWxShareListener(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }
}
