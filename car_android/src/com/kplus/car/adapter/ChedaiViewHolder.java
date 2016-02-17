package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.ChedaiActivity;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindChedai;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/10 0010.
 */
public class ChedaiViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private RemindChedai mRemindChedai;
    private KplusApplication mApp;
    private Context mContext;
    private TextView mYinghuan;
    private TextView mDate;
    private TextView mYihuan;
    private TextView mTotal;
    private ImageView mProgress;
    private TextView mTitle;
    private TextView mDateLabel;
    private ImageView mDateIcon;
    private TextView mProgressLabel;
    private View mProgressLayout;
    private View mDateProgressLayout;
    private ProgressBar mDateProgress;
    private TextView mDate2;
    private TextView mTian;

    public ChedaiViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mYinghuan = (TextView) itemView.findViewById(R.id.yinghuan);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mYihuan = (TextView) itemView.findViewById(R.id.yihuan);
        mTotal = (TextView) itemView.findViewById(R.id.total);
        mProgress = (ImageView) itemView.findViewById(R.id.progress);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mDateLabel = (TextView) itemView.findViewById(R.id.date_label);
        mDateIcon = (ImageView) itemView.findViewById(R.id.date_icon);
        mProgressLabel = (TextView) itemView.findViewById(R.id.progress_label);
        mProgressLayout = itemView.findViewById(R.id.progress_layout);
        mDateProgressLayout = itemView.findViewById(R.id.date_progress_layout);
        mDateProgress = (ProgressBar) itemView.findViewById(R.id.date_progress);
        mDate2 = (TextView) itemView.findViewById(R.id.date2);
        mDate2.setTypeface(mApp.mDin);
        mTian = (TextView) itemView.findViewById(R.id.tian);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.layout), this);
    }

    public void bind(String vehicleNum){
        mRemindChedai = mApp.dbCache.getRemindChedai(vehicleNum);
        if (mRemindChedai != null){
            Calendar oldCal = Calendar.getInstance();
            try {
                oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindChedai.getDate()));
            }catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            if (oldCal.before(cal)){
                cal.setTime(oldCal.getTime());
                if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH){
                    mRemindChedai.setFenshu(mRemindChedai.getFenshu() + 1);
                    if (mRemindChedai.getMoney() * mRemindChedai.getFenshu() < mRemindChedai.getTotal()){
                        cal.add(Calendar.MONTH, 1);
                        int day = mRemindChedai.getDayOfMonth();
                        if (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                            day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        cal.set(Calendar.DATE, day);
                    }
                }
                else
                    cal.add(Calendar.YEAR, 1);
                mRemindChedai.setDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                int gap = DateUtil.getGapCount(oldCal.getTime(), cal.getTime());
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindChedai.getRemindDate1()));
                    cal.add(Calendar.DATE, gap);
                    mRemindChedai.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindChedai.getRemindDate2()));
                    cal.add(Calendar.DATE, gap);
                    mRemindChedai.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mApp.dbCache.updateRemindChedai(mRemindChedai);
                RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_CHEDAI, mRemindChedai.getVehicleNum(), 0, null);
                if (mApp.getId() != 0)
                    new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_CHEDAI);
            }
            updateUI();
        }
    }

    private void updateUI(){
        mYinghuan.setText("¥" + mRemindChedai.getMoney());
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindChedai.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
        if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH){
            mDate.setText(mRemindChedai.getDate().replaceAll("-", "/"));
            ClipDrawable clip = (ClipDrawable) mProgress.getDrawable();
            int level = 0;
            mProgressLayout.setVisibility(View.VISIBLE);
            mDateProgressLayout.setVisibility(View.GONE);
            int yihuan = mRemindChedai.getFenshu() * mRemindChedai.getMoney();
            mYihuan.setText("¥" + (yihuan > mRemindChedai.getTotal() ? mRemindChedai.getTotal() : yihuan));
            mTotal.setText("/¥" + mRemindChedai.getTotal());
            if (mRemindChedai.getTotal() > 0)
                level = 10000 * mRemindChedai.getFenshu() * mRemindChedai.getMoney() / mRemindChedai.getTotal();
            if (level < 0)
                level = 0;
            else if (level > 10000)
                level = 10000;
            clip.setLevel(level);
            mDateIcon.setVisibility(View.VISIBLE);
            mDateLabel.setVisibility(View.VISIBLE);
            mDate.setVisibility(View.VISIBLE);
            mProgressLabel.setText("已还款/总额");
            mProgressLabel.setTextColor(mContext.getResources().getColor(R.color.daze_darkgrey9));
            mProgressLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            if (gap <= 1){
                int color = mContext.getResources().getColor(R.color.daze_darkred2);
                mTitle.setTextColor(color);
                mDateLabel.setTextColor(color);
                mDate.setTextColor(color);
                mDateIcon.setImageResource(R.drawable.warning_not_time);
            }
            else {
                mTitle.setTextColor(mContext.getResources().getColor(R.color.daze_black2));
                int color = mContext.getResources().getColor(R.color.daze_darkgrey9);
                mDateLabel.setTextColor(color);
                mDate.setTextColor(color);
                mDateIcon.setImageResource(R.drawable.tixing);
            }
        }
        else {
            mDate2.setText(String.valueOf(gap));
            mProgressLayout.setVisibility(View.GONE);
            mDateProgressLayout.setVisibility(View.VISIBLE);
            mDateIcon.setVisibility(View.GONE);
            mDateLabel.setVisibility(View.GONE);
            mDate.setVisibility(View.GONE);
            mProgressLabel.setText("距下次还款还有");
            if (gap <= 7){
                int color = mContext.getResources().getColor(R.color.daze_darkred2);
                mTitle.setTextColor(color);
                mDate2.setTextColor(color);
                mTian.setTextColor(color);
                mDateProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.circular_progress_bar2_red));
                mProgressLabel.setTextColor(color);
                mProgressLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning_not_text, 0, 0, 0);
            }
            else {
                int color = mContext.getResources().getColor(R.color.daze_black2);
                mTitle.setTextColor(color);
                mDate2.setTextColor(color);
                mTian.setTextColor(color);
                mDateProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.circular_progress_bar2));
                mProgressLabel.setTextColor(mContext.getResources().getColor(R.color.daze_darkgrey9));
                mProgressLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            mDateProgress.setProgress(gap);
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.layout:
                // 车贷提醒
                if(mApp.isUserLogin(true, "车贷提醒需要绑定手机号")) {
                    // 用户登录
                    Intent intent = new Intent(mContext, ChedaiActivity.class);
                    intent.putExtra("position", getPosition());
                    intent.putExtra("RemindChedai", mRemindChedai);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_CHEDAI);
                }
                break;
        }
    }
}
