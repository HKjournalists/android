package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;

import java.util.List;

/**
 * Description：订单详情中服务列表适配器
 * <br/><br/>Created by Fu on 2015/5/12.
 * <br/><br/>
 */
public class CNOrderDetailServiceAdapter extends CNBaseListAdapter<OnSiteService> {

    public CNOrderDetailServiceAdapter(Context context, List<OnSiteService> list, OnListItemClickListener listener) {
        super(context, list, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        OrderDetailServiceViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_order_details_service_item, parent, false);
            holder = new OrderDetailServiceViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (OrderDetailServiceViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class OrderDetailServiceViewHolder extends CNBaseAdapterViewHolder<OnSiteService> {
        private TextView tvIconNum = null;
        private TextView tvServiceName = null;
        private TextView tvServicePrice = null;
        private View view_top_line = null;
        private View view_center_line = null;

        public OrderDetailServiceViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);

            tvIconNum = findView(R.id.tvIconNum);
            tvServiceName = findView(R.id.tvServiceName);
            tvServicePrice = findView(R.id.tvServicePrice);
            view_top_line = findView(R.id.view_top_line);
            view_center_line = findView(R.id.view_center_line);
        }

        @Override
        public void setValue(int position, OnSiteService siteService) {
            mPosition = position;
            // 第一个显示上面的线，后面不显示，最后一个item时，把中间短的线隐藏，显示长的的线
            view_top_line.setVisibility(View.GONE);
            view_center_line.setVisibility(View.VISIBLE);

            tvServiceName.setText(siteService.getName());
            String price = mContext.getResources().getString(R.string.cn_order_price);
            price = String.format(price, siteService.getPrice());
            tvServicePrice.setText(price);

            if (mPosition == getCount() - Const.ONE) {
                view_center_line.setVisibility(View.GONE);
            }

            // 设置排序icon
            tvIconNum.setText((mPosition + Const.ONE) + "");
        }
    }
}
