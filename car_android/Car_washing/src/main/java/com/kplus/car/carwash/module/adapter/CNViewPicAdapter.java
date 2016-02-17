package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.module.base.CNBasePagerAdapter;
import com.kplus.car.carwash.module.photoview.PhotoView;
import com.kplus.car.carwash.module.photoview.PhotoViewAttacher;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.Log;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.List;

/**
 * Description：图片预览适配器
 * <br/><br/>Created by Fu on 2015/6/1.
 * <br/><br/>
 */
public class CNViewPicAdapter extends CNBasePagerAdapter<String> {
    private static final String TAG = "CNViewPicAdapter";

    private final DisplayImageOptions mOptions;
    private OnClickTapListener listener = null;
    private View.OnLongClickListener photoLongClickLisener = null;
    private int mPicType;

    public CNViewPicAdapter(Context context, List<String> list, int picType, OnListItemClickListener listener) {
        super(context, list, listener);
        mPicType = picType;
        if (mPicType == CNCarWashingLogic.TYPE_PIC_HEADER) {
            mOptions = CNImageUtils.getImageOptions(0,
                    R.drawable.cn_icon_worker_default,
                    R.drawable.cn_icon_worker_default);
        } else {
            mOptions = CNImageUtils.getImageOptions(0, 0, 0);
        }
    }

    @Override
    public Object bindView(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.cn_view_pic_item, container, false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.pvImage);
        ProgressBar pbDownProgress = (ProgressBar) view.findViewById(R.id.pbDownProgress);

        /**
         * 设置单击监听
         */
        photoView.setOnPhotoTapListener(photoTapListener);// 点击的是图片的
        photoView.setOnViewTapListener(viewTapListener);// 点击的是除图片之外的
        photoView.setOnLongClickListener(photoLongClickLisener);//长按

        String url = getItem(position);
        String thumUrl;
        String tempUrl;
        if (CNImageUtils.isHttpImgUrl(url)) {
            tempUrl = url;
            // 缩略图地址
            if (mPicType == CNCarWashingLogic.TYPE_PIC_HEADER) {
                // 头像拼大一点的图片
                thumUrl = url + CNCarWashingLogic.THUM_IMAGE_TYPE120x120x120;
            } else {
                thumUrl = url + CNCarWashingLogic.THUM_IMAGE_TYPE100x100x100;
            }
            File file = ImageLoader.getInstance().getDiskCache().get(thumUrl);
            if (null != file) {
                // 有缩略图先显示缩略图
                Bitmap bitmap = CNImageUtils.antiOMDecodeFile(file.getPath(), 2);
                if (null != bitmap) {
                    photoView.setImageBitmap(bitmap);
                }
            }
        } else {
            // 本地图片
            tempUrl = "file://" + url;
        }

        ImageLoader.getInstance().displayImage(tempUrl, photoView, mOptions, new ImageLoading(pbDownProgress));

        container.addView(view, Const.NONE);
        return view;
    }

    private class ImageLoading implements ImageLoadingListener {
        private ProgressBar mProgressBar = null;

        public ImageLoading(ProgressBar progressBar) {
            mProgressBar = progressBar;
        }

        @Override
        public void onLoadingStarted(String url, View view) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String url, View view, FailReason failReason) {
            ImageLoader.getInstance().getMemoryCache().clear();
            ImageLoader.getInstance().getDiskCache().remove(url);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingComplete(String url, View view, Bitmap bitmap) {
            Log.trace(TAG, "加载图片完成-->" + url);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingCancelled(String url, View view) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 点击图片
     */
    private PhotoViewAttacher.OnPhotoTapListener photoTapListener = new PhotoViewAttacher.OnPhotoTapListener() {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            click();
        }
    };

    /**
     * 点击除图片以外的其他控件了
     */
    private PhotoViewAttacher.OnViewTapListener viewTapListener = new PhotoViewAttacher.OnViewTapListener() {

        @Override
        public void onViewTap(View view, float x, float y) {
            click();
        }
    };

    private void click() {
        if (null != listener) {
            listener.onTap();
        }
    }

    public void setOnClickTapListener(OnClickTapListener l) {
        this.listener = l;
    }

    public void setPhotoLongClickLisener(View.OnLongClickListener photoLongClickLisener) {
        this.photoLongClickLisener = photoLongClickLisener;
    }

    public interface OnClickTapListener {
        void onTap();
    }
}
