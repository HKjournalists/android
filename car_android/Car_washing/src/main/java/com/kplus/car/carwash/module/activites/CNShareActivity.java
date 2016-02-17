package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.bean.ServicePicture;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.FileUtils;
import com.kplus.car.carwash.utils.Log;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fu on 2015/6/2.
 */
public class CNShareActivity extends CNParentActivity implements CNViewClickUtil.NoFastClickListener {
    private static final String TAG = "CNShareActivity";

    private ArrayList<String> mServicePicUrls = null;
    private Bitmap mBitmap = null;
    private String mImagePath = null;
    private String mThumImage = null;

    /**
     * 分享类型
     */
    private int mShareType = CNCarWashingLogic.SHARE_TYPE_ORDERLOG;
    /**
     * 分享到微信内容的类型
     */
    private int mshareContentType = CNCarWashingLogic.SHARE_TEXT_AND_IMAGE_CONTENT_TYPE;

    private OnSiteService mShareService = null;

    private final DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(false)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    private String mShareWebPageUrl = CNCarWashingLogic.SHARE_WEB_URL;
    private String mShareContent = CNCarWashingLogic.SHARE_CONTENT;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mShareType = bundle.getInt(CNCarWashingLogic.KEY_SHARE_TYPE);
            if (mShareType == CNCarWashingLogic.SHARE_TYPE_ORDERLOG) {
                mServicePicUrls = bundle.getStringArrayList(CNCarWashingLogic.KEY_SHARE_IMAGES);
                mShareWebPageUrl = CNCarWashingLogic.SHARE_WEB_URL;
                mShareContent = CNCarWashingLogic.SHARE_CONTENT;
                mshareContentType = CNCarWashingLogic.SHARE_IMAGE_CONTENT_TYPE;
            } else if (mShareType == CNCarWashingLogic.SHARE_TYPE_SERVICES) {
                // 分享服务
                mShareService = (OnSiteService) bundle.get(CNCarWashingLogic.KEY_SHARE_CONTENT);
                if (null != mShareService) {
                    // Notice OnSiteService 中有一个url地址
                    mShareWebPageUrl = mShareService.getUrl();
                    String serviceName = mShareService.getName();

                    if (CNStringUtil.isEmpty(mShareWebPageUrl)) {
                        // 为空的话，使用默认地址
                        mShareWebPageUrl = CNCarWashingLogic.SHARE_WEB_URL;
                    }
                    mShareContent = String.format(CNCarWashingLogic.SHARE_CONTENT_SERVICE, serviceName);
                }
                mshareContentType = CNCarWashingLogic.SHARE_TEXT_AND_IMAGE_CONTENT_TYPE;
            }
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.cn_share_layout);

//        ImageView ivImage = findView(R.id.ivImage);

        LinearLayout llShare = findView(R.id.llShare);
        ImageView ivClose = findView(R.id.ivClose);

        LinearLayout llWeChatMoment = findView(R.id.llWeChatMoment);
        ImageView ivWeChatMoment = findView(R.id.ivWeChatMoment);
        TextView tvWeChatMoment = findView(R.id.tvWeChatMoment);

        LinearLayout llWeChatFriend = findView(R.id.llWeChatFriend);
        ImageView ivWeChatFriend = findView(R.id.ivWeChatFriend);
        TextView tvWeChatFriend = findView(R.id.tvWeChatFriend);

        ViewGroup.LayoutParams params = llShare.getLayoutParams();
        params.width = (int) (mDeviceSizePoint.x * 0.9f);
        llShare.setLayoutParams(params);

        CNViewClickUtil.setNoFastClickListener(ivClose, this);
//        CNViewClickUtil.setNoFastClickListener(ivImage, this);

        CNViewClickUtil.setNoFastClickListener(llWeChatMoment, this);
        CNViewClickUtil.setNoFastClickListener(ivWeChatMoment, this);
        CNViewClickUtil.setNoFastClickListener(tvWeChatMoment, this);

        CNViewClickUtil.setNoFastClickListener(llWeChatFriend, this);
        CNViewClickUtil.setNoFastClickListener(ivWeChatFriend, this);
        CNViewClickUtil.setNoFastClickListener(tvWeChatFriend, this);
    }

    private boolean isFristLo = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isFristLo) {
            isFristLo = false;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CNProgressDialogUtil.showProgress(mContext, R.string.cn_loading_share_content);

                    if (mShareType == CNCarWashingLogic.SHARE_TYPE_ORDERLOG) {
                        // 分享订单日志的图片
                        makeSharImages();
                    } else if (mShareType == CNCarWashingLogic.SHARE_TYPE_SERVICES) {
                        // 分享服务项目
                        makeShareService();
                    }
                }
            }, 1000);
        }
    }

    private void makeShareService() {
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                List<ServicePicture> pictures = mShareService.getPictures();
                if (null != pictures && !pictures.isEmpty()) {
                    String src = "";
                    int imgWidth = Const.NONE, imgHeight = Const.NONE;
                    for (int i = Const.NONE; i < pictures.size(); i++) {
                        ServicePicture servicePicture = pictures.get(i);
                        String url = servicePicture.getUrl();
                        File file = ImageLoader.getInstance().getDiskCache().get(url);
                        if (null != file) {
                            byte[] bytes;
                            bytes = CNImageUtils.readData(file.toString());
                            Bitmap bitmap = CNImageUtils.bytes2Bitmap(bytes);

                            if (null != bitmap) {
                                // 找到图片
                                src = file.toString();
                                imgWidth = bitmap.getWidth();
                                imgHeight = bitmap.getHeight();
                                bitmap.recycle();
                                break;
                            }
                        }
                    }

                    if (CNStringUtil.isNotEmpty(src)) {
                        if (imgWidth == Const.NONE || imgHeight == Const.NONE) {
                            Point point = CNImageUtils.getImgWidthAndHeight(src);
                            imgWidth = point.x;
                            imgHeight = point.y;
                        }
                        // 压缩小图
                        makeThumImages(src, imgWidth, imgHeight);
                    }
                }
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                if (null != mBitmap && !mBitmap.isRecycled()) {
//                    ivImage.setVisibility(View.GONE);
//                    ivImage.setImageBitmap(mBitmap);

                    mBitmap.recycle();
                    mBitmap = null;
                }
//                CNProgressDialogUtil.dismissProgress(mContext);
                dismiss();
            }
        });
    }

    private void makeSharImages() {
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                final ArrayList<Bitmap> bitmaps = new ArrayList<>();
                if (null != mServicePicUrls) {
                    for (int i = Const.NONE; i < mServicePicUrls.size(); i++) {
                        String url = mServicePicUrls.get(i);
                        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url, mOptions);
                        if (null != bitmap) {
                            bitmaps.add(bitmap);
                        }
                    }
                }

                mBitmap = CNImageUtils.mergeBitmap(mContext, bitmaps);

                if (null != mBitmap) {
                    int imgWidth = mBitmap.getWidth();
                    int imgHeight = mBitmap.getHeight();

                    String fileFolder = getFileFolder();

                    String tempPath = fileFolder + "temp_car_washing_share.jpg";
                    FileUtils.saveBitmap(tempPath, mBitmap);
                    mImagePath = fileFolder + "car_washing_share.jpg";
                    // 压缩大图
                    boolean isSuccess = CNImageUtils.compressBitmap(tempPath, mImagePath, imgWidth, imgHeight, Bitmap.CompressFormat.JPEG);
                    if (isSuccess) {
                        FileUtils.deleteFile(tempPath);
                    } else {
                        mImagePath = tempPath;
                    }
                    makeThumImages(mImagePath, imgWidth, imgHeight);
                }
                return null;
            }
        }, new FutureListener<Void>() {
            @Override
            public void onFutureDone(Future<Void> future) {
                if (null != mBitmap && !mBitmap.isRecycled()) {
//                    ivImage.setVisibility(View.GONE);
//                    ivImage.setImageBitmap(mBitmap);

                    mBitmap.recycle();
                    mBitmap = null;
                }
//                CNProgressDialogUtil.dismissProgress(mContext);
                dismiss();
            }
        });
    }

    private void dismiss() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }, 2000);
    }

    private String getFileFolder() {
        String fileFolder = FileUtils.getAppRootPath() + "shareimage/";
        FileUtils.createNomediaFile(fileFolder);
        return fileFolder;
    }

    private void makeThumImages(String src, int imgWidth, int imgHeight) {
        // 处理压缩小图
        String fileFolder = getFileFolder();
        String fileTempPath = fileFolder + "car_washing_share_thum.jpg";

        int maxWidth = 200;
        int maxHeight = 200;

        int desiredWidth = CNImageUtils.getResizedDimension(maxWidth, maxHeight, imgWidth, imgHeight);
        int desiredHeight = CNImageUtils.getResizedDimension(maxHeight, maxWidth, imgHeight, imgWidth);

        boolean isSuccess = CNImageUtils.compressBitmap(src, fileTempPath, desiredWidth, desiredHeight, Bitmap.CompressFormat.JPEG);
        if (isSuccess) {
            mThumImage = fileTempPath;
            long len = FileUtils.getFileLength(fileTempPath);
            int index = 0;
            // 微信的缩略图不能超过32kb
            while ((len / 1024) >= 32) {
                if (index >= 5) {
                    break;
                }
                desiredWidth = (int) (desiredWidth * 0.5f);
                desiredHeight = (int) (desiredHeight * 0.5f);
                CNImageUtils.compressBitmap(src, fileTempPath, desiredWidth, desiredHeight, Bitmap.CompressFormat.JPEG);
                len = FileUtils.getFileLength(fileTempPath);
                index++;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void initToolbarView() {
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return false;
    }

    @Override
    protected void onDestroy() {
        CNProgressDialogUtil.dismissProgress(mContext);
        // 将图片资源释放
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        clearShareImg();
        setRresult();
        onBack();
    }

    @Override
    public void onNoFastClick(View v) {
        int vId = v.getId();
        if (vId == R.id.ivClose) {
            back();
        } else if (vId == R.id.ivImage) {
            ArrayList<String> urls = new ArrayList<>();
            urls.add(mImagePath);

            Intent data = new Intent(mContext, CNViewPicActivity.class);
            data.putExtra(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_URLS, urls);
            data.putExtra(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_POSITION, 1);
            data.putExtra(CNCarWashingLogic.KEY_REVIEW_PIC_TYPE, CNCarWashingLogic.TYPE_PIC_OTHER);
            CNViewManager.getIns().showActivity(mContext, data, false, R.anim.cn_zoom_enter, Const.NONE);
        }
        /**
         * 朋友圈
         */
        else if (vId == R.id.llWeChatMoment
                || vId == R.id.ivWeChatMoment
                || vId == R.id.tvWeChatMoment) {
            AppBridgeUtils.getIns().doShareWeChatMoment(mContext, mshareContentType, mShareWebPageUrl,
                    mShareContent, mImagePath, mThumImage);
        }
        /**
         * 微信好友
         */
        else if (vId == R.id.llWeChatFriend
                || vId == R.id.ivWeChatFriend
                || vId == R.id.tvWeChatFriend) {
            AppBridgeUtils.getIns().doShareWeCharFriend(mContext, mshareContentType, mShareWebPageUrl,
                    mShareContent, mImagePath, mThumImage);
        }
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_SHARE_RESULT_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        String action = intent.getAction();
        if (CustomBroadcast.ON_SHARE_RESULT_ACTION.equals(action)) {
            int resultCode = intent.getExtras().getInt(CNCarWashingLogic.KEY_SHARE_RESULT_CODE);
            switch (resultCode) {
                case CNCarWashingLogic.SHARE_RESULT_SUCCESS: // 成功
                    Log.trace(TAG, "分享成功");
                    setRresult();
//                    clearShareImg();
                    onBack();
                    break;
                case CNCarWashingLogic.SHARE_RESULT_CANCEL: // 取消
                    Log.trace(TAG, "分享取消");
                    clearShareImg();
                    setRresult();
                    onBack();
                    break;
                case CNCarWashingLogic.SHARE_RESULT_FAILED: // 失败
                    Log.trace(TAG, "分享失败");
                    clearShareImg();
                    setRresult();
                    onBack();
                    break;
            }
        }
    }

    private void clearShareImg() {
        FileUtils.deleteFile(mImagePath);
        FileUtils.deleteFile(mThumImage);
        ImageLoader.getInstance().getDiskCache().remove("file://" + mImagePath);
        ImageLoader.getInstance().getDiskCache().remove("file://" + mThumImage);
    }

    private void setRresult() {
        setResult(Activity.RESULT_OK);
    }

    private void onBack() {
        CNViewManager.getIns().popWithoutFinish(this);
        finish();
    }
}
