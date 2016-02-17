package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.CustomRemindActivity;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/10 0010.
 */
public class CustomViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private RemindCustom mRemindCustom;
    private Context mContext;
    private KplusApplication mApp;
    private TextView mTitle;
    private TextView mDate;
    private TextView mTian;
    private ProgressBar mProgress;
    private TextView mRemark;
    private TextView mLocation;

    public CustomViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mProgress = (ProgressBar) itemView.findViewById(R.id.progress);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mDate.setTypeface(mApp.mDin);
        mTian = (TextView) itemView.findViewById(R.id.tian);
        mRemark = (TextView) itemView.findViewById(R.id.remark);
        mLocation = (TextView) itemView.findViewById(R.id.location);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.layout), this);
    }

    public boolean bind(String vehicleNum, String name){
        mRemindCustom = mApp.dbCache.getRemindCustom(vehicleNum, name);
        if (mRemindCustom != null){
            Calendar oldCal = Calendar.getInstance();
            try {
                oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindCustom.getDate()));
            }catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            if (oldCal.before(cal)){
                if (mRemindCustom.getRepeatType() == Constants.REPEAT_TYPE_NONE){
                    RemindInfo remindInfo = mApp.dbCache.getRemindInfo(mRemindCustom.getVehicleNum(), mRemindCustom.getName());
                    remindInfo.setHidden(true);
                    mApp.dbCache.updateRemindInfo(remindInfo);
                    if (mApp.getId() != 0)
                        new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_REMIND);
                    return true;
                }
                cal.setTime(oldCal.getTime());
                if (mRemindCustom.getRepeatType() == Constants.REPEAT_TYPE_YEAR) {
                    cal.add(Calendar.YEAR, 1);
                }
                else if (mRemindCustom.getRepeatType() == Constants.REPEAT_TYPE_DAY){
                    cal.add(Calendar.DATE, 1);
                }
                else if (mRemindCustom.getRepeatType() == Constants.REPEAT_TYPE_WEEK) {
                    cal.add(Calendar.DATE, 7);
                }
                else {
                    cal.add(Calendar.MONTH, mRemindCustom.getRepeatType() - Constants.REPEAT_TYPE_NONE);
                }
                mRemindCustom.setDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                int gap = DateUtil.getGapCount(oldCal.getTime(), cal.getTime());
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindCustom.getRemindDate1()));
                    cal.add(Calendar.DATE, gap);
                    mRemindCustom.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindCustom.getRemindDate2()));
                    cal.add(Calendar.DATE, gap);
                    mRemindCustom.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mApp.dbCache.updateRemindCustom(mRemindCustom);
                RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_CUSTOM, mRemindCustom.getVehicleNum(), 0, mRemindCustom.getName());
                if (mApp.getId() != 0)
                    new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_CUSTOM);
            }
            updateUI();
        }
        return false;
    }

    private void updateUI(){
        mTitle.setText(mRemindCustom.getName());
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindCustom.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
        mDate.setText(String.valueOf(gap));
        mRemark.setText(!StringUtils.isEmpty(mRemindCustom.getRemark()) ? mRemindCustom.getRemark() : "");
        mLocation.setText(!StringUtils.isEmpty(mRemindCustom.getLocation()) ? mRemindCustom.getLocation() : "");
        if (mRemindCustom.getRepeatType() == Constants.REPEAT_TYPE_YEAR && gap <= 7 || gap <= 1){
            int color = mContext.getResources().getColor(R.color.daze_darkred2);
            mTitle.setTextColor(color);
            mDate.setTextColor(color);
            mTian.setTextColor(color);
            mProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.circular_progress_bar2_red));
        }
        else {
            int color = mContext.getResources().getColor(R.color.daze_black2);
            mTitle.setTextColor(color);
            mDate.setTextColor(color);
            mTian.setTextColor(color);
            mProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.circular_progress_bar2));
        }
        mProgress.setProgress(gap);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.layout:
                // 自定义提醒
                if(mApp.isUserLogin(true, mRemindCustom.getName() + "需要绑定手机号")) {
                    // 用户登录
                    Intent intent = new Intent(mContext, CustomRemindActivity.class);
                    intent.putExtra("position", getPosition());
                    intent.putExtra("RemindCustom", mRemindCustom);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_CUSTOM);
                }
                break;
        }
    }
}
