package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNViewClickUtil;

import java.util.List;

/**
 * Description：城市列表适配器
 * <br/><br/>Created by Fu on 2015/5/12.
 * <br/><br/>
 */
public class CNCityAdapter extends CNBaseListAdapter<City> {

    public CNCityAdapter(Context context, List<City> data, OnListItemClickListener listener) {
        super(context, data, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        LocationViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_city_item, parent, false);

            holder = new LocationViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (LocationViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    public class LocationViewHolder extends CNBaseAdapterViewHolder<City> implements CNViewClickUtil.NoFastClickListener {

        private RelativeLayout rlCityItem = null;
        private TextView tvItemName = null;
        private TextView tvMoreLocation = null;

        public LocationViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            rlCityItem = findView(R.id.rlCityItem);
            tvItemName = findView(R.id.tvItemName);
            tvMoreLocation = findView(R.id.tvMoreLocation);

            CNViewClickUtil.setNoFastClickListener(rlCityItem, this);
        }

        public void setValue(int position, City city) {
            mPosition = position;
            // 如果是最后一个,显示一个固定高度的view
            if (mPosition == getCount() - Const.ONE) {
                tvMoreLocation.setVisibility(View.VISIBLE);
            } else {
                tvMoreLocation.setVisibility(View.GONE);
            }
            tvItemName.setText(city.getName());
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
