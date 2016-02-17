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
public class SelectInsuranceViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    public TextView mCompany;
    public TextView mPhone;
    public ImageView mSelected;
    private OnItemClickListener mListener;

    @Override
    public void onNoFastClick(View v) {
        if (mListener != null)
            mListener.onItemClick(getPosition());
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public SelectInsuranceViewHolder(View itemView, OnItemClickListener l) {
        super(itemView);
        mListener = l;
        mCompany = (TextView) itemView.findViewById(R.id.company);
        mPhone = (TextView) itemView.findViewById(R.id.phone);
        mSelected = (ImageView) itemView.findViewById(R.id.selected);
        ClickUtils.setNoFastClickListener(itemView, this);
    }
}
