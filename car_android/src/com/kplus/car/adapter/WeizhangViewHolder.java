package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.VehicleDetailActivity;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import java.util.List;

/**
 * Created by Administrator on 2015/3/10 0010.
 */
public class WeizhangViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private List<AgainstRecord> mListAgainstRecord;
    private Context mContext;
    private KplusApplication mApp;
    private String mVehicleNum;
    private TextView mWeizhangNum;
    private TextView mNoWeizhang;
    private View mFakuanLayout;
    private TextView mFakuan;
    private TextView mKoufen;
    private ImageView mRefresh;
    private boolean mIsRefreshingWeizhang = false;

    public WeizhangViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mNoWeizhang = (TextView) itemView.findViewById(R.id.no_weizhang);
        mWeizhangNum = (TextView) itemView.findViewById(R.id.weizhang_num);
        mFakuanLayout = itemView.findViewById(R.id.fakuan_layout);
        mFakuan = (TextView) itemView.findViewById(R.id.fakuan);
        mKoufen = (TextView) itemView.findViewById(R.id.koufen);
        mRefresh = (ImageView) itemView.findViewById(R.id.refresh_weizhang);
        mWeizhangNum.setTypeface(mApp.mDin);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.weizhang_layout), this);
        ClickUtils.setNoFastClickListener(mRefresh, this);
    }

    public void bind(String vehicleNum, boolean isRefreshingWeizhang){
        mVehicleNum = vehicleNum;
        mIsRefreshingWeizhang = isRefreshingWeizhang;
        mListAgainstRecord = mApp.dbCache.getAgainstRecordsByNum(vehicleNum);
        updateUI();
    }

    private void updateUI(){
        if (mIsRefreshingWeizhang){
            mNoWeizhang.setVisibility(View.VISIBLE);
            mFakuanLayout.setVisibility(View.GONE);
            mNoWeizhang.setText("查询中...");
            RotateAnimation anim = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(1000);
            anim.setRepeatCount(Animation.INFINITE);
            mRefresh.startAnimation(anim);
        }
        else{
            mRefresh.clearAnimation();
            if (mListAgainstRecord != null && mListAgainstRecord.size() > 0){
                mNoWeizhang.setVisibility(View.GONE);
                mFakuanLayout.setVisibility(View.VISIBLE);
                int money = 0, score = 0, count = 0;
                for (AgainstRecord record : mListAgainstRecord){
                    if (record.getStatus() != 1){
                        count++;
                        money += (record.getMoney() < 0 ? 0 : record.getMoney());
                        score += (record.getScore() < 0 ? 0 : record.getScore());
                    }
                }
                mWeizhangNum.setText(String.valueOf(count));
                mFakuan.setText(money + "元");
                mKoufen.setText(score + "分");
            }
            else{
                mWeizhangNum.setText("0");
                mNoWeizhang.setVisibility(View.VISIBLE);
                mFakuanLayout.setVisibility(View.GONE);
                mNoWeizhang.setText("没有违章哦^_^");
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {
        // 违章
        Intent intent;
        switch (v.getId()){
            case R.id.weizhang_layout:
                if(mApp.isUserLogin(true, "查询违章需要绑定手机号")) {
                    intent = new Intent(mContext, VehicleDetailActivity.class);
                    intent.putExtra("vehicleNumber", mVehicleNum);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.refresh_weizhang:
                EventAnalysisUtil.onEvent(mContext, "refresh_record_home", "我的车首页点击违章刷新按钮", null);
                if(mApp.isUserLogin(true, "查询违章需要绑定手机号")) {
                    intent = new Intent(mContext, VehicleDetailActivity.class);
                    intent.putExtra("vehicleNumber", mVehicleNum);
                    intent.putExtra("add", true);
                    mContext.startActivity(intent);
                }
                break;
        }
    }
}
