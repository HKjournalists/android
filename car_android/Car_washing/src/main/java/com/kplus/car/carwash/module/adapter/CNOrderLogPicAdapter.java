package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OrderLog;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Description：订单日志适配器
 * <br/><br/>Created by Fu on 2015/5/16.
 * <br/><br/>
 */
public class CNOrderLogPicAdapter extends CNBaseListAdapter<String> {
    private static final String TAG = "CNOrderLogPicAdapter";

    private final DisplayImageOptions mOptions;
    private OrderLog mOrderLog = null;

    public CNOrderLogPicAdapter(Context context, List<String> list, OnListItemClickListener listener) {
        super(context, list, listener);
        mOptions = CNImageUtils.getImageOptions();
    }

    public void setOrderLog(OrderLog orderLog) {
        mOrderLog = orderLog;
    }

    public OrderLog getOrderLog() {
        return mOrderLog;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        PicViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_orderlog_pic_item, parent, false);
            holder = new PicViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (PicViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class PicViewHolder extends CNBaseAdapterViewHolder<String> {
        private FrameLayout flPic = null;
        private ImageView ivPic = null;
        private ProgressBar pbDownProgress = null;

        public PicViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            flPic = findView(R.id.flPic);
            ivPic = findView(R.id.ivPic);
            pbDownProgress = findView(R.id.pbDownProgress);

            CNViewClickUtil.setNoFastClickListener(flPic, this);
            CNViewClickUtil.setNoFastClickListener(ivPic, this);
        }

        @Override
        public void setValue(int position, String url) {
            mPosition = position;
            // 如果是图片的前两张把原图也下载下来
            if (mPosition == Const.NONE || mPosition == Const.ONE) {
                // 先把图片加载到内存或sd中
                ImageLoader.getInstance().loadImage(url, mOptions, null);
            }

            // 取缩略图
            String thumUrl = url + CNCarWashingLogic.THUM_IMAGE_TYPE100x100x100;
            ImageLoader.getInstance().displayImage(thumUrl, ivPic, mOptions, new ImageLoading(pbDownProgress));
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
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String url, View view) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
