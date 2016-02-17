package com.kplus.car.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.widget.ProgressArc;

/**
 * Created by Administrator on 2015/5/26.
 */
public class JiazhaoDetailViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    public TextView mName;
    public TextView mType;
    public TextView mShowIndex;
    public View mMore;
    public ProgressArc mProgress;
    public View mRemindBtn;
    public View mRemindLabel;
    public TextView mUpdateTime;
    public TextView mScore;
    public View mCheck;
    public View mProgressLayout;
    public View mRefreshingLayout;
    public LinearLayout mLayout;
    private Context mContext;
    public DetailClickListener mListener;
    public int mViewCount = 0;

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.more:
                if (mListener != null)
                    mListener.onMoreIconClick(this, getLayoutPosition());
                break;
            case R.id.remind_btn:
                if (mListener != null)
                    mListener.onRemindIconClick(this, getLayoutPosition());
                break;
            case R.id.check:
                if (mListener != null)
                    mListener.onCheckIconClick(this, getLayoutPosition());
                break;
        }
    }

    public interface DetailClickListener{
        void onMoreIconClick(JiazhaoDetailViewHolder vh, int position);
        void onRemindIconClick(JiazhaoDetailViewHolder vh, int position);
        void onCheckIconClick(JiazhaoDetailViewHolder vh, int position);
    }

    public JiazhaoDetailViewHolder(View itemView, Context context, DetailClickListener l) {
        super(itemView);
        mContext = context;
        mName = (TextView) itemView.findViewById(R.id.name);
        mType = (TextView) itemView.findViewById(R.id.type);
        mShowIndex = (TextView) itemView.findViewById(R.id.showIndex);
        mMore = itemView.findViewById(R.id.more);
        mProgress = (ProgressArc) itemView.findViewById(R.id.progress);
        mRemindBtn = itemView.findViewById(R.id.remind_btn);
        mRemindLabel = itemView.findViewById(R.id.remind_label);
        mUpdateTime = (TextView) itemView.findViewById(R.id.update_time);
        Typeface typeface = ((KplusApplication)mContext.getApplicationContext()).mDin;
        mScore = (TextView) itemView.findViewById(R.id.score);
        mScore.setTypeface(typeface);
        mCheck = itemView.findViewById(R.id.check);
        mProgressLayout = itemView.findViewById(R.id.progress_layout);
        mRefreshingLayout = itemView.findViewById(R.id.refreshing_layout);
        mLayout = (LinearLayout) itemView.findViewById(R.id.layout);
        ClickUtils.setNoFastClickListener(mMore, this);
        ClickUtils.setNoFastClickListener(mRemindBtn, this);
        ClickUtils.setNoFastClickListener(mCheck, this);
        mListener = l;
    }
}
