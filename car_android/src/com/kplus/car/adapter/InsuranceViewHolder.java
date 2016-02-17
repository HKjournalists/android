package com.kplus.car.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.util.ClickUtils;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class InsuranceViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    public TextView mCompany;
    public TextView mPhone;
    public ImageView mDial;
    private DialIconClickListener mListener;

    @Override
    public void onNoFastClick(View v) {
        if (mListener != null)
            mListener.onDialIconClick(this, getPosition());
    }

    public interface DialIconClickListener{
        public void onDialIconClick(InsuranceViewHolder vh, int position);
    }

    public InsuranceViewHolder(View itemView, DialIconClickListener l) {
        super(itemView);
        mListener = l;
        mCompany = (TextView) itemView.findViewById(R.id.company);
        mPhone = (TextView) itemView.findViewById(R.id.phone);
        mDial = (ImageView) itemView.findViewById(R.id.dial);
        ClickUtils.setNoFastClickListener(mDial, this);
    }
}
