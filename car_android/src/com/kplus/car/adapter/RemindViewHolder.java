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
public class RemindViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    public ImageView mLeftIcon;
    public TextView mTitle;
    public TextView mDesc;
    public ImageView mOnOffIcon;
    public ImageView mDeleteIcon;
    private RightIconClickListener mListener;

    public interface RightIconClickListener{
        void onOnOffIconClick(RemindViewHolder vh, int position);
        void onDeleteIconClick(RemindViewHolder vh, int position);
    }

    public RemindViewHolder(View itemView, RightIconClickListener l) {
        super(itemView);
        mLeftIcon = (ImageView) itemView.findViewById(R.id.left_icon);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mDesc = (TextView) itemView.findViewById(R.id.desc);
        mOnOffIcon = (ImageView) itemView.findViewById(R.id.on_off_icon);
        ClickUtils.setNoFastClickListener(mOnOffIcon, this);
        mDeleteIcon = (ImageView) itemView.findViewById(R.id.delete_icon);
        ClickUtils.setNoFastClickListener(mDeleteIcon, this);
        mListener = l;
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.on_off_icon:
                if (mListener != null)
                    mListener.onOnOffIconClick(this, getLayoutPosition());
                break;
            case R.id.delete_icon:
                if (mListener != null)
                    mListener.onDeleteIconClick(this, getLayoutPosition());
                break;
        }
    }
}
