package com.kplus.car.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kplus.car.R;

/**
 * Created by Administrator on 2015/11/16.
 */
public class CouponViewHolder extends RecyclerView.ViewHolder {
    public TextView mAmountUnit;
    public TextView mAmount;
    public TextView mName;
    public TextView mTime;
    public TextView mInfo;
    public TextView mStatus;

    public CouponViewHolder(View itemView) {
        super(itemView);
        mAmountUnit = (TextView) itemView.findViewById(R.id.amount_unit);
        mAmount = (TextView) itemView.findViewById(R.id.amount);
        mName = (TextView) itemView.findViewById(R.id.name);
        mTime = (TextView) itemView.findViewById(R.id.time);
        mInfo = (TextView) itemView.findViewById(R.id.info);
        mStatus = (TextView) itemView.findViewById(R.id.status);
    }
}
