package com.kplus.car.carwash.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.ServicePicture;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Fu on 2015/5/19.
 */
public class CNSerivePicView extends LinearLayout {
    private Context mContext = null;

    public CNSerivePicView(Context context) {
        this(context, null);
    }

    public CNSerivePicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void initView(List<ServicePicture> pictures) {
        this.removeAllViews();
        if (null != pictures && pictures.size() > 0) {
            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.cn_service_pic_margin);
            int indexCount = 0;
            int size = pictures.size();
            if (size == 1) {
                ImageView pic = new ImageView(mContext);
                pic.setLayoutParams(params);
                pic.setId(0);
                String url = pictures.get(0).getUrl();
                loadingImage(url, pic, indexCount);
            } else {
                for (int i = 0; i < size; i++) {
                    // 图片从第二张开始显示
                    if (i == 0)
                        continue;

                    ImageView pic = new ImageView(mContext);
                    pic.setLayoutParams(params);
                    pic.setId(i);

                    String url = pictures.get(i).getUrl();

                    loadingImage(url, pic, indexCount);
                    indexCount++;
                }
            }
        } else {
            CNProgressDialogUtil.dismissProgress(mContext);
        }
    }

    private void loadingImage(String url, ImageView iv, int index) {
        this.addView(iv, index);
        ImageLoader.getInstance().displayImage(url, iv, CNImageUtils.getImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                CNProgressDialogUtil.dismissProgress(mContext);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                CNProgressDialogUtil.dismissProgress(mContext);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        });
    }
}
