package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.Coupon;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.DateUtil;

import java.util.List;

/**
 * Description：代金券适配器
 * <br/><br/>Created by Fu on 2015/5/12.
 * <br/><br/>
 */
public class CNVoucherAdapter extends CNBaseListAdapter<Coupon> {

    public CNVoucherAdapter(Context context, List<Coupon> data, OnListItemClickListener listener) {
        super(context, data, listener);
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        VoucherViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_voucher_item, parent, false);
            holder = new VoucherViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (VoucherViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    public class VoucherViewHolder extends CNBaseAdapterViewHolder<Coupon> implements CNViewClickUtil.NoFastClickListener {

        private LinearLayout llCarVoucherItem = null;
        private TextView tvItemName = null;
        private TextView tvItemPrice = null;
        private TextView tvItemTime = null;
        private View viewBottom = null;

        public VoucherViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);
            llCarVoucherItem = findView(R.id.llCarVoucherItem);
            tvItemName = findView(R.id.tvItemName);
            tvItemPrice = findView(R.id.tvItemPrice);
            tvItemTime = findView(R.id.tvItemTime);
            viewBottom = findView(R.id.viewBottom);

            CNViewClickUtil.setNoFastClickListener(llCarVoucherItem, this);
        }

        public void setValue(int position, Coupon coupon) {
            mPosition = position;
            // 如果是最后一个,显示一个固定高度的view
            if (mPosition == getCount() - Const.ONE) {
                viewBottom.setVisibility(View.VISIBLE);
            } else {
                viewBottom.setVisibility(View.GONE);
            }

            if (null != coupon) {
                if (coupon.getId() == CNCarWashingLogic.TYPE_NOT_USE_COUPON_PAY) {
                    tvItemName.setText(mContext.getResources().getString(R.string.cn_not_use_voucher));
                } else {
                    tvItemPrice.setVisibility(View.VISIBLE);
                    // 到期时间
                    String endTime = DateUtil.format(DateUtil.YYYY_MM_DD, coupon.getEndTime().getTime());
                    String cn_expire = mContext.getResources().getString(R.string.cn_expire);
                    cn_expire = String.format(cn_expire, endTime);

                    tvItemName.setText(coupon.getName());
                    String strPrice = mContext.getResources().getString(R.string.cn_order_price);
                    strPrice = String.format(strPrice, coupon.getAmount());
                    tvItemPrice.setText(strPrice + "，");
                    tvItemTime.setText(cn_expire);

                    int colorId = mContext.getResources().getColor(R.color.cn_font_color);
                    if (coupon.isChecked()) {
                        colorId = mContext.getResources().getColor(R.color.cn_oranger_color);
                    }
                    tvItemName.setTextColor(colorId);
                    tvItemTime.setTextColor(colorId);
                    tvItemPrice.setTextColor(colorId);
                }
            }
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
