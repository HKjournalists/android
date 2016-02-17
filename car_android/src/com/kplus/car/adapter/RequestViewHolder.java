package com.kplus.car.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.util.ClickUtils;

/**
 * Created by Administrator on 2015/8/13.
 */
public class RequestViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private RequestClickListener mListener;
    public TextView mServiceName;
    public TextView mDate;
    public TextView mStatus;
    public View mGoto;

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.item_request:
                if (mListener != null)
                    mListener.onRequestClick(this, getAdapterPosition());
                break;
        }
    }

    public interface RequestClickListener{
        void onRequestClick(RequestViewHolder vh, int position);
    }

    public RequestViewHolder(View itemView, RequestClickListener l) {
        super(itemView);
        mServiceName = (TextView) itemView.findViewById(R.id.service_name);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mStatus = (TextView) itemView.findViewById(R.id.status);
        mGoto = itemView.findViewById(R.id.go_to);
        mListener = l;
        ClickUtils.setNoFastClickListener(itemView, this);
    }
}
