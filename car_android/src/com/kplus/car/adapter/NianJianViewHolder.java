package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.NianjianActivity;
import com.kplus.car.activity.NianjianEditActivity;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.widget.HorizontalProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/10 0010.
 */
public class NianJianViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private RemindNianjian mRemindNianjian;
    private Context mContext;
    private KplusApplication mApp;
    private TextView mDate;
    private TextView mLabel;
    private HorizontalProgressBar mProgress;
    private String mVehicleNum;
    private TextView mTitle;
    private PopupWindow mRulePopup;
    private View mLayout;
    private View mAddLayout;
    private View mProgressLayout;
    private View mBottomLine;
    private View mDateLayout;

    public NianJianViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mDate = (TextView) itemView.findViewById(R.id.date);
        mLabel = (TextView) itemView.findViewById(R.id.label);
        mProgress = (HorizontalProgressBar) itemView.findViewById(R.id.progress);
        mProgress.setMax(365);
        mDate.setTypeface(mApp.mDin);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mLayout = itemView.findViewById(R.id.layout);
        mAddLayout = itemView.findViewById(R.id.add_layout);
        mProgressLayout = itemView.findViewById(R.id.progress_layout);
        mBottomLine = itemView.findViewById(R.id.bottomLine);
        mDateLayout = itemView.findViewById(R.id.date_layout);
        ClickUtils.setNoFastClickListener(mLayout, this);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.rule), this);
    }

    public void bind(String vehicleNum, String nianjianDate, String issueDate){
        mVehicleNum = vehicleNum;
        mRemindNianjian = mApp.dbCache.getRemindNianjian(vehicleNum);
        if (mRemindNianjian == null){
            boolean bAdd = false;
            Calendar cal = Calendar.getInstance();
            if (nianjianDate != null && !"".equals(nianjianDate)){
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(nianjianDate));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                bAdd = true;
            }
            else if (issueDate != null && !"".equals(issueDate)){
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(issueDate));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                bAdd = true;
            }
            if (bAdd){
                mRemindNianjian = new RemindNianjian(vehicleNum, cal);
                mRemindNianjian.setFromType(1);
                mApp.dbCache.saveRemindNianjian(mRemindNianjian);
                RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_NIANJIAN, mRemindNianjian.getVehicleNum(), 0, null);
                if (mApp.getId() != 0)
                    new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_NIANJIAN);
            }
        }
        else {
            Calendar oldCal = Calendar.getInstance();
            try {
                oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getDate()));
            }catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            if (oldCal.before(cal)){
                cal.setTime(oldCal.getTime());
                cal.add(Calendar.YEAR, 1);
                mRemindNianjian.setDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                int gap = DateUtil.getGapCount(oldCal.getTime(), cal.getTime());
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindNianjian.getRemindDate1()));
                    cal.add(Calendar.DATE, gap);
                    mRemindNianjian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindNianjian.getRemindDate2()));
                    cal.add(Calendar.DATE, gap);
                    mRemindNianjian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mApp.dbCache.updateRemindNianjian(mRemindNianjian);
                RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_NIANJIAN, mRemindNianjian.getVehicleNum(), 0, null);
                if (mApp.getId() != 0)
                    new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_NIANJIAN);
            }
        }
        updateUI();
    }

    private void updateUI(){
        if (mRemindNianjian == null){
            mAddLayout.setVisibility(View.VISIBLE);
            mProgressLayout.setVisibility(View.GONE);
        }
        else {
            mAddLayout.setVisibility(View.GONE);
            mProgressLayout.setVisibility(View.VISIBLE);
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
            mDate.setText(String.valueOf(gap));
            if (gap <= 7){
                int color = mContext.getResources().getColor(R.color.daze_darkred2);
                mTitle.setTextColor(color);
                mProgress.setImageDrawable(new ColorDrawable(color));
                mDate.setTextColor(color);
                mLabel.setTextColor(color);
                mBottomLine.setBackgroundColor(color);
            }
            else {
                int color = mContext.getResources().getColor(R.color.daze_black2);
                mTitle.setTextColor(color);
                mProgress.setImageDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.daze_orangered5)));
                mDate.setTextColor(color);
                mLabel.setTextColor(color);
                mBottomLine.setBackgroundColor(mContext.getResources().getColor(R.color.daze_darkgrey7));
            }
            mProgress.setProgress(gap);
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.layout:
                // 年检提醒
                if(mApp.isUserLogin(true, "年检提醒需要绑定手机号")) {
                    // 用户登录
                    if (mRemindNianjian != null){
                        Intent intent = new Intent(mContext, NianjianActivity.class);
                        intent.putExtra("position", getPosition());
                        intent.putExtra("vehicleNum", mVehicleNum);
                        intent.putExtra("RemindNianjian", mRemindNianjian);
                        ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_NIANJIAN);
                    }
                    else {
                        Intent intent = new Intent(mContext, NianjianEditActivity.class);
                        intent.putExtra("position", getPosition());
                        intent.putExtra("vehicleNum", mVehicleNum);
                        ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_NIANJIAN);
                    }
                }
                break;
            case R.id.rule:
                showRulePopup();
                break;
            case R.id.close_rule:
                mRulePopup.dismiss();
                break;
        }
    }

    private void showRulePopup(){
        View layout = LayoutInflater.from(mContext).inflate(R.layout.popup_rule, null, false);
        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText("机动车年检新规定");
        TextView content = (TextView) layout.findViewById(R.id.content);
        content.setText(R.string.nianjian_rule);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.close_rule), this);
        mRulePopup = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRulePopup.showAtLocation(mLayout.getRootView(), Gravity.LEFT | Gravity.TOP, 0, 0);
    }
}
