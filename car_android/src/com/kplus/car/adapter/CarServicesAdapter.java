package com.kplus.car.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.model.CarService;
import com.kplus.car.util.CarServicesUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Description：汽车服务中其他服务适配器
 * <br/><br/>Created by FU ZHIXUE on 2015/8/10.
 * <br/><br/>
 */
public class CarServicesAdapter extends CNBaseListAdapter<CarService> {

    private final DisplayImageOptions mOptions;
    private final KplusApplication mApp;

    public CarServicesAdapter(Context context, List<CarService> list, OnListItemClickListener listener) {
        super(context, list, listener);

        mApp = KplusApplication.getInstance();

        // ImageLoader参数
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(0)
                .showImageForEmptyUri(0)
                .showImageOnFail(0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        CarServicesViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.view_other_service_item, parent, false);
            holder = new CarServicesViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (CarServicesViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class CarServicesViewHolder extends CNBaseAdapterViewHolder<CarService> {

        private final ImageView ivServiceIcon;
        private final TextView tvServiceName;
        private final ImageView ivIconFlag;
        private final ImageView ivIconFavorableTag;
        private final FrameLayout flFavorableTag;
        private final TextView tvFavorableTag;
        private final TextView tvServiceDesc;
        private final View view_center_line;

        public CarServicesViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            ivServiceIcon = findView(R.id.ivServiceIcon);
            tvServiceName = findView(R.id.tvServiceName);
            ivIconFlag = findView(R.id.ivIconFlag);
            flFavorableTag = findView(R.id.flFavorableTag);
            ivIconFavorableTag = findView(R.id.ivIconFavorableTag);
            tvFavorableTag = findView(R.id.tvFavorableTag);

            tvServiceDesc = findView(R.id.tvServiceDesc);
            view_center_line = findView(R.id.view_center_line);

            CNViewClickUtil.setNoFastClickListener(view, this);
        }

        @Override
        public void setValue(int position, CarService carService) {
            mPosition = position;

            String url = carService.getListIcon();
            mApp.imageLoader.displayImage(url, ivServiceIcon, mOptions);

            tvServiceName.setText(carService.getName());
            tvServiceDesc.setText(carService.getInfo());

            // 设置推荐图标
            int resId = CarServicesUtil.getServiceFlag((null == carService.getFlag() ? 0 : carService.getFlag()));
            if (resId == 0) {
                ivIconFlag.setVisibility(View.GONE);
            } else {
                ivIconFlag.setVisibility(View.VISIBLE);
                ivIconFlag.setImageResource(resId);
            }

            // 设置显示优惠标签
            String favorableTag = carService.getFavorableTag();
            if (TextUtils.isEmpty(favorableTag)) {
                ivIconFavorableTag.setVisibility(View.GONE);
                flFavorableTag.setVisibility(View.GONE);
            } else {
                boolean hasHttp = CNStringUtil.hasHttpUrl(favorableTag);
                if (hasHttp) {
                    ivIconFavorableTag.setVisibility(View.VISIBLE);
                    flFavorableTag.setVisibility(View.GONE);
                    mApp.imageLoader.displayImage(favorableTag, ivIconFavorableTag, mOptions);
                } else {
                    ivIconFavorableTag.setVisibility(View.GONE);
                    flFavorableTag.setVisibility(View.VISIBLE);
                    tvFavorableTag.setText(favorableTag);
                }
            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_center_line.getLayoutParams();
            if (position == getCount() - 1) {
                params.leftMargin = 0;
                view_center_line.setLayoutParams(params);
            } else {
                params.leftMargin = CNPixelsUtil.dip2px(mContext, 78);
                view_center_line.setLayoutParams(params);
            }
        }

        @Override
        public void onNoFastClick(View v) {
            super.onNoFastClick(v);

            // 汽车服务首页分组中点击
            if (null != mCustomListener) {
                mCustomListener.onCustomClick(getItem(mPosition));
            }
        }
    }

    private ICustomClickItemListener mCustomListener = null;

    public void setCustomClickItemListener(ICustomClickItemListener listener) {
        mCustomListener = listener;
    }

    public interface ICustomClickItemListener {
        void onCustomClick(CarService carService);
    }
}
