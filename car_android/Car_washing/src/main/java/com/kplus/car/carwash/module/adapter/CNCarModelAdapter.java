package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.CarModel;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Description：车辆车型适配器
 * <br/><br/>Created by Fu on 2015/5/18.
 * <br/><br/>
 */
public class CNCarModelAdapter extends CNBaseListAdapter<CarModel> {

    private final DisplayImageOptions mOptions;

    public CNCarModelAdapter(Context context, List<CarModel> data, OnListItemClickListener listener) {
        super(context, data, listener);
        mOptions = CNImageUtils.getImageOptions();
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        CarModelViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_car_color_item, parent, false);
            holder = new CarModelViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (CarModelViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    public class CarModelViewHolder extends CNBaseAdapterViewHolder<CarModel> implements CNViewClickUtil.NoFastClickListener {

        private LinearLayout llCarColorItem = null;
        private RelativeLayout rlCarColorItem = null;
        private ImageView ivModelIcon = null;
        private TextView tvItemName = null;
        private TextView tvDesc = null;
        private ImageView ivRightArrow = null;
        private View viewBottom = null;

        public CarModelViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            llCarColorItem = findView(R.id.llCarColorItem);
            rlCarColorItem = findView(R.id.rlCarColorItem);
            ivModelIcon = findView(R.id.ivModelIcon);
            tvItemName = findView(R.id.tvItemName);
            tvDesc = findView(R.id.tvDesc);
            viewBottom = findView(R.id.viewBottom);
            ivRightArrow = findView(R.id.ivRightArrow);

            CNViewClickUtil.setNoFastClickListener(rlCarColorItem, this);
            ivModelIcon.setVisibility(View.VISIBLE);
        }

        public void setValue(int position, CarModel model) {
            mPosition = position;
            ivRightArrow.setVisibility(View.GONE);
            // 如果是最后一个,显示一个固定高度的view
            if (mPosition == getCount() - Const.ONE) {
                viewBottom.setVisibility(View.VISIBLE);
            } else {
                viewBottom.setVisibility(View.GONE);
            }
            tvItemName.setText(model.getName());
            llCarColorItem.setEnabled(true);
            CNViewClickUtil.setNoFastClickListener(rlCarColorItem, this);
            tvDesc.setVisibility(View.GONE);

//            switch (model.getStatus()) {
//                case CNCarWashingLogic.CAR_MODEL_SERVICE:
//                    llCarColorItem.setEnabled(true);
//                    CNViewClickUtil.setNoFastClickListener(rlCarColorItem, this);
//                    tvDesc.setVisibility(View.GONE);
//                    break;
//                case CNCarWashingLogic.CAR_MODEL_NOT_SERVICE:
//                    llCarColorItem.setEnabled(false);
//                    rlCarColorItem.setOnClickListener(null);
//                    tvDesc.setVisibility(View.VISIBLE);
//                    break;
//            }
            String url = model.getImage();
            ImageLoader.getInstance().displayImage(url, ivModelIcon, mOptions);
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
