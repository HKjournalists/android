package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.BaoyangActivity;
import com.kplus.car.activity.BaoyangEditActivity;
import com.kplus.car.activity.BaoyangRecordActivity;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.BaoyangRecord;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/3/10 0010.
 */
public class BaoyangViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private RemindBaoyang mRemindBaoyang;
    private List<BaoyangRecord> mListBaoyangRecord;
    private Context mContext;
    private KplusApplication mApp;
    private TextView mLicheng;
    private String mVehicleNum;
    private TextView mTitle;
    private View mAddLayout;
    private View mBaoyangLayout;
    private TextView mDate;
    private TextView mDesc;
    private TextView mBaoyangLabel;

    public BaoyangViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mLicheng = (TextView) itemView.findViewById(R.id.licheng);
        mAddLayout = itemView.findViewById(R.id.add_layout);
        mBaoyangLayout = itemView.findViewById(R.id.baoyang_layout);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mBaoyangLabel = (TextView) itemView.findViewById(R.id.baoyang_label);
        mDate = (TextView) itemView.findViewById(R.id.date);
        mDesc = (TextView) itemView.findViewById(R.id.desc);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.baoyang_record_layout), this);
        ClickUtils.setNoFastClickListener(mAddLayout, this);
        ClickUtils.setNoFastClickListener(mBaoyangLayout, this);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.modify), this);
    }

    public void bind(String vehicleNum){
        mVehicleNum = vehicleNum;
        mRemindBaoyang = mApp.dbCache.getRemindBaoyang(vehicleNum);
        if (mRemindBaoyang != null){
            Calendar oldCal = Calendar.getInstance();
            try {
                oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindBaoyang.getDate()));
            }catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            if (oldCal.before(cal)){
                BaoyangRecord record = new BaoyangRecord(mRemindBaoyang.getVehicleNum());
                record.setDate(mRemindBaoyang.getDate());
                record.setLicheng(mRemindBaoyang.getLicheng());
                mApp.dbCache.addBaoyangRecord(record);
                cal.setTime(oldCal.getTime());
                cal.add(Calendar.YEAR, 1);
                mRemindBaoyang.setDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                int gap = DateUtil.getGapCount(oldCal.getTime(), cal.getTime());
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindBaoyang.getRemindDate1()));
                    cal.add(Calendar.DATE, gap);
                    mRemindBaoyang.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindBaoyang.getRemindDate2()));
                    cal.add(Calendar.DATE, gap);
                    mRemindBaoyang.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mRemindBaoyang.setLicheng(mRemindBaoyang.getLicheng() + mRemindBaoyang.getJiange());
                mApp.dbCache.updateRemindBaoyang(mRemindBaoyang);
                RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_BAOYANG, mRemindBaoyang.getVehicleNum(), 0, null);
                if (mApp.getId() != 0)
                    new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_BAOYANG);
            }
        }
        mListBaoyangRecord = mApp.dbCache.getBaoyangRecord(vehicleNum);
        updateUI();
    }

    private void updateUI(){
        if (mListBaoyangRecord == null || mListBaoyangRecord.size() == 0){
            mDesc.setText("以备下次保养参考和提醒");
        }
        else {
            mDesc.setText("有" + mListBaoyangRecord.size() + "条记录");
        }
        if (mRemindBaoyang == null){
            mAddLayout.setVisibility(View.VISIBLE);
            mBaoyangLayout.setVisibility(View.GONE);
        }
        else {
            mAddLayout.setVisibility(View.GONE);
            mBaoyangLayout.setVisibility(View.VISIBLE);
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindBaoyang.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
            if (gap <= 7){
                int color = mContext.getResources().getColor(R.color.daze_darkred2);
                mTitle.setTextColor(color);
                mDate.setTextColor(color);
                mBaoyangLabel.setTextColor(color);
            }
            else {
                mTitle.setTextColor(mContext.getResources().getColor(R.color.daze_black2));
                int color = mContext.getResources().getColor(R.color.daze_darkgrey9);
                mDate.setTextColor(color);
                mBaoyangLabel.setTextColor(color);
            }
            mDate.setText(mRemindBaoyang.getDate().replace("-", "/"));
            if (mRemindBaoyang.getLicheng() > 0){
                mLicheng.setTextColor(mContext.getResources().getColor(R.color.daze_darkgrey9));
                mLicheng.setText(String.valueOf(mRemindBaoyang.getLicheng()) + "公里");
            }
            else {
                mLicheng.setTextColor(mContext.getResources().getColor(R.color.daze_orangered5));
                mLicheng.setText("去设置");
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.baoyang_layout:
                // 保养提醒
                if(mApp.isUserLogin(true, "保养提醒需要绑定手机号")) {
                    // 用户登录
                    Intent intent = new Intent(mContext, BaoyangActivity.class);
                    intent.putExtra("position", getPosition());
                    intent.putExtra("vehicleNum", mVehicleNum);
                    intent.putExtra("RemindBaoyang", mRemindBaoyang);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_BAOYANG);
                }
                break;
            case R.id.licheng:
                if (mRemindBaoyang.getLicheng() > 0)
                    break;
            case R.id.add_layout:
            case R.id.modify:
                // 保养提醒
                if(mApp.isUserLogin(true, "保养提醒需要绑定手机号")) {
                    // 用户登录
                    Intent intent = new Intent(mContext, BaoyangEditActivity.class);
                    intent.putExtra("position", getPosition());
                    intent.putExtra("vehicleNum", mVehicleNum);
                    intent.putExtra("RemindBaoyang", mRemindBaoyang);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_BAOYANG);
                }
                break;
            case R.id.baoyang_record_layout:
                if(mApp.isUserLogin(true, "保养记录需要绑定手机号")) {
                    // 用户登录
                    Intent intent = new Intent(mContext, BaoyangRecordActivity.class);
                    intent.putExtra("position", getPosition());
                    intent.putExtra("vehicleNum", mVehicleNum);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_BAOYANG_RECORD);
                }
                break;
        }
    }
}
