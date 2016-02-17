package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.PoiLocationRoad;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNViewClickUtil;

import java.util.List;

/**
 * Description：搜索到的POI的显示适配器
 * <br/><br/>Created by Fu on 2015/5/12.
 * <br/><br/>
 */
public class CNLocationAdapter extends CNBaseListAdapter<BaseInfo> {

    public CNLocationAdapter(Context context, List<BaseInfo> data, OnListItemClickListener listener) {
        super(context, data, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        LocationViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_location_item, parent, false);
            holder = new LocationViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (LocationViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    public class LocationViewHolder extends CNBaseAdapterViewHolder<BaseInfo> implements CNViewClickUtil.NoFastClickListener {

        private LinearLayout llCarColorItem = null;
        private TextView tvLocName = null;
        private TextView tvItemName = null;
        private View viewBottom = null;

        public LocationViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            llCarColorItem = findView(R.id.llCarColorItem);
            tvLocName = findView(R.id.tvLocName);
            tvItemName = findView(R.id.tvItemName);
            viewBottom = findView(R.id.viewBottom);

            CNViewClickUtil.setNoFastClickListener(llCarColorItem, this);
        }

        public void setValue(int position, BaseInfo value) {
            mPosition = position;
            if (mPosition == getCount() - Const.ONE) {
                viewBottom.setVisibility(View.VISIBLE);
            } else {
                viewBottom.setVisibility(View.GONE);
            }

            PoiLocationRoad poiItem = (PoiLocationRoad) value;
            if (poiItem.hasData) {
                tvLocName.setText(poiItem.mName);
                tvLocName.setVisibility(View.VISIBLE);
            } else {
                tvLocName.setVisibility(View.GONE);
            }
            tvItemName.setText(poiItem.mAddress);
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
