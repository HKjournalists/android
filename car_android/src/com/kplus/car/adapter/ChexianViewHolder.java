package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.ChexianActivity;
import com.kplus.car.activity.ChexianEditActivity;
import com.kplus.car.activity.SelectInsuranceActivity;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.HorizontalProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/3/10 0010.
 */
public class ChexianViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private List<RemindChexian> mListRemindChexian;
    private TextView mJiaoqiangxianDate;
    private HorizontalProgressBar mJiaoqiangxianProgress;
    private TextView mJiaoqiangxianLabel;
    private View mJiaoqiangxianLayout;
    private TextView mShangyexianDate;
    private HorizontalProgressBar mShangyexianProgress;
    private TextView mShangyexianLabel;
    private View mShangyexianLayout;
    private Context mContext;
    private KplusApplication mApp;
    private TextView mAddShangyexian;
    private TextView mTitle;
    private TextView mBaoxianBtn, mBaoxianModify;
    private TextView mBaoxianCompany, mSelectInsurance;
    private View mInsuranceLayout, mBaoxianDesc;
    private View mAddLayout;
    private String mVehicleNum;
    private String mCompany;
    private String mPhone;

    public ChexianViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        mJiaoqiangxianDate = (TextView) itemView.findViewById(R.id.jiaoqiangxian_date);
        mJiaoqiangxianLayout = itemView.findViewById(R.id.jiaoqiangxian_layout);
        mJiaoqiangxianDate.setTypeface(mApp.mDin);
        mJiaoqiangxianProgress = (HorizontalProgressBar) itemView.findViewById(R.id.jiaoqiangxian_progress);
        mJiaoqiangxianProgress.setMax(365);
        mJiaoqiangxianLabel = (TextView) itemView.findViewById(R.id.jiaoqiangxian_label);
        mAddShangyexian = (TextView) itemView.findViewById(R.id.add_shangyexian);
        mShangyexianLayout = itemView.findViewById(R.id.shangyexian_layout);
        mShangyexianDate = (TextView) itemView.findViewById(R.id.shangyexian_date);
        mShangyexianDate.setTypeface(mApp.mDin);
        mShangyexianProgress = (HorizontalProgressBar) itemView.findViewById(R.id.shangyexian_progress);
        mShangyexianProgress.setMax(365);
        mShangyexianLabel = (TextView) itemView.findViewById(R.id.shangyexian_label);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mSelectInsurance = (TextView) itemView.findViewById(R.id.select_insurance);
        mBaoxianBtn = (TextView) itemView.findViewById(R.id.baoxian);
        mBaoxianCompany = (TextView) itemView.findViewById(R.id.company);
        mInsuranceLayout = itemView.findViewById(R.id.my_insurance_layout);
        mBaoxianDesc = itemView.findViewById(R.id.baoxian_desc);
        mBaoxianModify = (TextView) itemView.findViewById(R.id.baoxian_modify);
        mAddLayout = itemView.findViewById(R.id.add_layout);
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.layout), this);
        ClickUtils.setNoFastClickListener(mAddShangyexian, this);
        ClickUtils.setNoFastClickListener(mSelectInsurance, this);
        ClickUtils.setNoFastClickListener(mBaoxianBtn, this);
        ClickUtils.setNoFastClickListener(mBaoxianModify, this);
    }

    public void bind(String vehicleNum, String company, String phone, String issueDate){
        mVehicleNum = vehicleNum;
        mCompany = company;
        mPhone = phone;
        mListRemindChexian = mApp.dbCache.getRemindChexian(vehicleNum);
        if (mListRemindChexian == null){
            if (issueDate != null && !"".equals(issueDate)){
                mListRemindChexian = new ArrayList<RemindChexian>();
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(issueDate));
                }catch (ParseException e) {
                    e.printStackTrace();
                }
                RemindChexian chexian = new RemindChexian(vehicleNum, 1, cal);
                chexian.setFromType(1);
                mListRemindChexian.add(chexian);
                mApp.dbCache.saveRemindChexian(chexian);
                RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_CHEXIAN, chexian.getVehicleNum(), 1, null);
                if (mApp.getId() != 0)
                    new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_CHEXIAN);
            }
        }
        else {
            boolean bExpired = false;
            for (int i = 0; i < mListRemindChexian.size(); i++){
                RemindChexian chexian = mListRemindChexian.get(i);
                Calendar oldCal = Calendar.getInstance();
                try {
                    oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(chexian.getDate()));
                }catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                if (oldCal.before(cal)){
                    bExpired = true;
                    cal.setTime(oldCal.getTime());
                    cal.add(Calendar.YEAR, 1);
                    chexian.setDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                    int gap = DateUtil.getGapCount(oldCal.getTime(), cal.getTime());
                    try {
                        cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(chexian.getRemindDate1()));
                        cal.add(Calendar.DATE, gap);
                        chexian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(chexian.getRemindDate2()));
                        cal.add(Calendar.DATE, gap);
                        chexian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mApp.dbCache.updateRemindChexian(chexian);
                    RemindManager.getInstance(mContext).set(Constants.REQUEST_TYPE_CHEXIAN, chexian.getVehicleNum(), i + 1, null);
                }
            }
            if (bExpired){
                if (mApp.getId() != 0)
                    new RemindSyncTask(mApp).execute(Constants.REQUEST_TYPE_CHEXIAN);
            }
        }
        updateUI();
    }

    private void updateUI(){
        if (mListRemindChexian == null){
            mAddLayout.setVisibility(View.VISIBLE);
            mAddShangyexian.setVisibility(View.GONE);
            mJiaoqiangxianLayout.setVisibility(View.GONE);
            mShangyexianLayout.setVisibility(View.GONE);
        }
        else {
            mAddLayout.setVisibility(View.GONE);
            mJiaoqiangxianLayout.setVisibility(View.VISIBLE);
            if (mListRemindChexian.size() == 1){
                mAddShangyexian.setVisibility(View.VISIBLE);
                mShangyexianLayout.setVisibility(View.GONE);
            }
            else {
                mAddShangyexian.setVisibility(View.GONE);
                mShangyexianLayout.setVisibility(View.VISIBLE);
            }
            boolean bRed = false;
            for (RemindChexian chexian : mListRemindChexian){
                if (chexian.getType() == 1){
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(chexian.getDate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
                    mJiaoqiangxianDate.setText(String.valueOf(gap));
                    if (gap <= 7) {
                        bRed = true;
                        int color = mContext.getResources().getColor(R.color.daze_darkred2);
                        mJiaoqiangxianProgress.setImageDrawable(new ColorDrawable(color));
                        mJiaoqiangxianDate.setTextColor(color);
                        mJiaoqiangxianLabel.setTextColor(color);
                    }
                    else {
                        mJiaoqiangxianProgress.setImageDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.daze_orangered5)));
                        int color = mContext.getResources().getColor(R.color.daze_black2);
                        mJiaoqiangxianDate.setTextColor(color);
                        mJiaoqiangxianLabel.setTextColor(color);
                    }
                    mJiaoqiangxianProgress.setProgress(gap);
                }
                else {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(chexian.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
                    mShangyexianDate.setText(String.valueOf(gap));
                    if (gap <= 7) {
                        bRed = true;
                        int color = mContext.getResources().getColor(R.color.daze_darkred2);
                        mShangyexianProgress.setImageDrawable(new ColorDrawable(color));
                        mShangyexianDate.setTextColor(color);
                        mShangyexianLabel.setTextColor(color);
                    }
                    else {
                        mShangyexianProgress.setImageDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.daze_orangered4)));
                        int color = mContext.getResources().getColor(R.color.daze_black2);
                        mShangyexianDate.setTextColor(color);
                        mShangyexianLabel.setTextColor(color);
                    }
                    mShangyexianProgress.setProgress(gap);
                }
            }
            if (bRed){
                mTitle.setTextColor(mContext.getResources().getColor(R.color.daze_darkred2));
            }
            else {
                mTitle.setTextColor(mContext.getResources().getColor(R.color.daze_black2));
            }
        }
        if (StringUtils.isEmpty(mPhone) || StringUtils.isEmpty(mCompany)){
            mSelectInsurance.setVisibility(View.VISIBLE);
            mBaoxianDesc.setVisibility(View.VISIBLE);
            mInsuranceLayout.setVisibility(View.GONE);
            mBaoxianBtn.setVisibility(View.GONE);
        }
        else {
            mSelectInsurance.setVisibility(View.GONE);
            mBaoxianDesc.setVisibility(View.GONE);
            mInsuranceLayout.setVisibility(View.VISIBLE);
            mBaoxianBtn.setVisibility(View.VISIBLE);
            mBaoxianCompany.setText(mCompany + "(" + mPhone + ")");
        }
    }

    @Override
    public void onNoFastClick(View v) {
        Intent it;
        switch (v.getId()){
            case R.id.layout:
                // 车险提醒
                if (mApp.isUserLogin(true, "车险提醒需要绑定手机号")) {
                    if (mListRemindChexian != null){
                        it = new Intent(mContext, ChexianActivity.class);
                        it.putExtra("position", getPosition());
                        it.putExtra("vehicleNum", mVehicleNum);
                        it.putExtra("company", mCompany);
                        it.putExtra("phone", mPhone);
                        ((Activity) mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CHEXIAN);
                    }
                    else {
                        it = new Intent(mContext, ChexianEditActivity.class);
                        it.putExtra("position", getPosition());
                        it.putExtra("vehicleNum", mVehicleNum);
                        ((Activity) mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CHEXIAN);
                    }
                }
                break;
            case R.id.add_shangyexian:
                EventAnalysisUtil.onEvent(mContext, "open_SYXNotify_home", "点‘添加商业险提醒’", null);
                // 开启商业险到期提醒
                if (mApp.isUserLogin(true, "车险提醒需要绑定手机号")) {
                    it = new Intent(mContext, ChexianEditActivity.class);
                    it.putExtra("position", getPosition());
                    it.putExtra("vehicleNum", mVehicleNum);
                    ((Activity) mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CHEXIAN);
                }
                break;
            case R.id.select_insurance:
                EventAnalysisUtil.onEvent(mContext, "click_immediaChoose_home", "点击首页‘立即选择’", null);
                if (mApp.isUserLogin(true, "开启一键报险需要绑定手机号")) {
                    it = new Intent(mContext, SelectInsuranceActivity.class);
                    it.putExtra("position", getPosition());
                    it.putExtra("vehicleNum", mVehicleNum);
                    it.putExtra("company", mCompany);
                    it.putExtra("phone", mPhone);
                    ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_INSURANCE);
                }
                break;
            case R.id.baoxian:
                EventAnalysisUtil.onEvent(mContext, "click_photoInsure_home", "点击首页‘一键报险’", null);
                it = new Intent();
                it.setAction("android.intent.action.CALL");
                it.setData(Uri.parse("tel:" + mPhone));
                mContext.startActivity(it);
                break;
            case R.id.baoxian_modify:
                EventAnalysisUtil.onEvent(mContext, "click_modifyCompany_home", "点击首页‘修改’保险公司", null);
                if (mApp.isUserLogin(true, "修改报案电话需要绑定手机号")) {
                    it = new Intent(mContext, SelectInsuranceActivity.class);
                    it.putExtra("position", getPosition());
                    it.putExtra("vehicleNum", mVehicleNum);
                    it.putExtra("company", mCompany);
                    it.putExtra("phone", mPhone);
                    ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_INSURANCE);
                }
                break;
        }
    }
}
