package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.PoiLocationRoad;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.utils.CNStringUtil;

import java.util.List;

/**
 * Description：车辆位置关键字搜索提示适配器
 * <br/><br/>Created by FU ZHIXUE on 2015/6/29.
 * <br/><br/>
 */
public class CNSearchKeyAdapter extends ArrayAdapter<PoiLocationRoad> {

    private LayoutInflater mInflater = null;
    private int mResource;

    public CNSearchKeyAdapter(Context context, int resource, List<PoiLocationRoad> searchKeys) {
        super(context, resource, searchKeys);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SearchKeyViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(mResource, parent, false);
            holder = new SearchKeyViewHolder(view, null);
            view.setTag(holder);
        } else {
            holder = (SearchKeyViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class SearchKeyViewHolder extends CNBaseAdapterViewHolder<PoiLocationRoad> {

        private TextView tvKey = null;
        private TextView tvAddress = null;

        public SearchKeyViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);

            tvKey = findView(R.id.tvKey);
            tvAddress = findView(R.id.tvAddress);
        }

        @Override
        public void setValue(int position, PoiLocationRoad searchKey) {
            mPosition = position;

            tvKey.setText(searchKey.mName);

            tvAddress.setVisibility(View.GONE);
            if (CNStringUtil.isNotEmpty(searchKey.mAddress)) {
                tvAddress.setText(searchKey.mAddress);
                tvAddress.setVisibility(View.VISIBLE);
            }
        }
    }
}
