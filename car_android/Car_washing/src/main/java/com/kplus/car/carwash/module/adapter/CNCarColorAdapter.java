package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.CarColor;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNViewClickUtil;

import java.util.List;

/**
 * Description：车辆颜色适配器
 * <br/><br/>Created by Fu on 2015/5/12.
 * <br/><br/>
 */
public class CNCarColorAdapter extends CNBaseListAdapter<CarColor> {

    public CNCarColorAdapter(Context context, List<CarColor> data, OnListItemClickListener listener) {
        super(context, data, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        CarColorViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_car_color_item, parent, false);
            holder = new CarColorViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (CarColorViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }


    public class CarColorViewHolder extends CNBaseAdapterViewHolder<CarColor> implements CNViewClickUtil.NoFastClickListener {

        private RelativeLayout rlCarColorItem = null;
        private TextView tvItemName = null;
        private View viewBottom = null;

        public CarColorViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            rlCarColorItem = findView(R.id.rlCarColorItem);
            tvItemName = findView(R.id.tvItemName);
            viewBottom = findView(R.id.viewBottom);

            CNViewClickUtil.setNoFastClickListener(rlCarColorItem, this);
        }

        public void setValue(int position, CarColor color) {
            mPosition = position;
            // 如果是最后一个,显示一个固定高度的view
            if (mPosition == getCount() - Const.ONE) {
                viewBottom.setVisibility(View.VISIBLE);
            } else {
                viewBottom.setVisibility(View.GONE);
            }
            tvItemName.setText(color.getName());
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
