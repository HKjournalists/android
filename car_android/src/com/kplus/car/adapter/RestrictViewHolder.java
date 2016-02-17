package com.kplus.car.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.util.ClickUtils;

/**
 * Created by Administrator on 2015/3/14.
 */
public class RestrictViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    public TextView mDesc;
    public ImageView mOnOffIcon;
    public View mDivider;
    public View mRemindLayout;
    public TextView mRemindDate;
    private RestrictIconClickListener mListener;

    public interface RestrictIconClickListener{
        void onOnOffIconClick(RestrictViewHolder vh);
        void onRemindLayoutClick(RestrictViewHolder vh);
    }

    public RestrictViewHolder(View itemView, RestrictIconClickListener l) {
        super(itemView);
        mDesc = (TextView) itemView.findViewById(R.id.desc);
        mOnOffIcon = (ImageView) itemView.findViewById(R.id.on_off_icon);
        mDivider = itemView.findViewById(R.id.divider);
        mRemindLayout = itemView.findViewById(R.id.remind_layout);
        mRemindDate = (TextView) itemView.findViewById(R.id.remind_date);
        ClickUtils.setNoFastClickListener(mOnOffIcon, this);
        ClickUtils.setNoFastClickListener(mRemindLayout, this);
        mListener = l;
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.on_off_icon:
                if (mListener != null)
                    mListener.onOnOffIconClick(this);
                break;
            case R.id.remind_layout:
                if (mListener != null)
                    mListener.onRemindLayoutClick(this);
                break;
        }
    }
}
