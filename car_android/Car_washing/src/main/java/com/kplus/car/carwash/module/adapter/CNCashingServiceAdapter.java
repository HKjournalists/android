package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Description：下单界面已选择服务的适配器
 * <br/><br/>Created by Fu on 2015/6/15.
 * <br/><br/>
 */
public class CNCashingServiceAdapter extends CNBaseListAdapter<OnSiteService> {

    public CNCashingServiceAdapter(Context context, List<OnSiteService> list, OnListItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        WashingServiceViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_washing_service_item, parent, false);
            holder = new WashingServiceViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (WashingServiceViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    class WashingServiceViewHolder extends CNBaseAdapterViewHolder<OnSiteService> {
        private RelativeLayout rlService = null;
        private ImageView ivServiceImg = null;
        private TextView tvServiceName = null;
        private TextView tvServiceDesc = null;
        private TextView tvServicePrice = null;

        public WashingServiceViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);

            rlService = findView(R.id.rlService);
            ivServiceImg = findView(R.id.ivServiceImg);
            tvServiceName = findView(R.id.tvServiceName);
            tvServiceDesc = findView(R.id.tvServiceDesc);
            tvServicePrice = findView(R.id.tvServicePrice);

            CNViewClickUtil.setNoFastClickListener(rlService, this);
        }

        @Override
        public void setValue(int position, OnSiteService siteService) {
            mPosition = position;

            tvServiceName.setText(siteService.getName());
            tvServiceDesc.setText(siteService.getDesc());
            String price = String.format(mContext.getResources().getString(R.string.cn_order_price), siteService.getPrice());
            tvServicePrice.setText(price);

            String url = siteService.getLogo();
            if (CNStringUtil.isNotEmpty(url)) {
                ImageLoader.getInstance().displayImage(url, ivServiceImg, CNImageUtils.getImageOptions());
            }
        }
    }
}
