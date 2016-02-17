package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/16 0016.
 */
public class BaoyangActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RemindBaoyang mRemindBaoyang;
    private TextView mDate;
    private TextView mDateLeft;
    private TextView mTian;
    private ProgressBar mProgress;
    private PopupWindow mPopupWindow;
    private View mRightView;
    private int mPosition = 0;
    private int mResult = RESULT_CANCELED;
    private TextView mLicheng;
    private View mSetJiange;
    private TextView mJiange;
    private View mSetBaoyang;
    private View mProgressLayout;
    private String mVehicleNum;
    private View mJiangeLine;
    private View mAuth;
    private View mHint;
    private View mLine;

    private LinearLayout llLicheng = null;
    private TextView tvSetLicheng = null;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_baoyang);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("保养提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.moreope);
        mRightView = findViewById(R.id.rightView);

        mDate = (TextView) findViewById(R.id.date);
        mDateLeft = (TextView) findViewById(R.id.date_left);
        mDateLeft.setTypeface(mApplication.mDin);
        mTian = (TextView) findViewById(R.id.tian);
        mTian.setTypeface(mApplication.mDin);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mLicheng = (TextView) findViewById(R.id.licheng);
        mLicheng.setTypeface(mApplication.mDin);
        mSetJiange = findViewById(R.id.set_jiange);
        mJiange = (TextView) findViewById(R.id.jiange);
        mSetBaoyang = findViewById(R.id.set_baoyang);
        mProgressLayout = findViewById(R.id.progress_layout);
        mJiangeLine = findViewById(R.id.jiange_line);
        mAuth = findViewById(R.id.vehicle_auth);
        mHint = findViewById(R.id.hint);
        mLine = findViewById(R.id.line);

        llLicheng = (LinearLayout) findViewById(R.id.llLicheng);
        tvSetLicheng = (TextView) findViewById(R.id.tvSetLicheng);
    }

    @Override
    protected void loadData() {
        EventAnalysisUtil.onEvent(this, "goto_MaintenNotifyDetail", "进入保养提醒详情", null);
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindBaoyang = (RemindBaoyang) getIntent().getSerializableExtra("RemindBaoyang");
        mVehicleNum = mRemindBaoyang != null ? mRemindBaoyang.getVehicleNum() : getIntent().getStringExtra("vehicleNum");
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText(mVehicleNum);
        VehicleAuth vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(mVehicleNum);
        if (vehicleAuth == null || vehicleAuth.getStatus() != 2) {
            mAuth.setVisibility(View.VISIBLE);
            mHint.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
        }
        updateUI();
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mRightView, this);
        ClickUtils.setNoFastClickListener(mSetJiange, this);
        ClickUtils.setNoFastClickListener(mAuth, this);
        ClickUtils.setNoFastClickListener(tvSetLicheng, this);
        ClickUtils.setNoFastClickListener(mSetBaoyang, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
                showPopup();
                break;
            case R.id.tvSetLicheng:
            case R.id.set_jiange:
                EventAnalysisUtil.onEvent(this, "click_setPerMileMainten_Button", "点“下次多少公里去保养”", null);
                Intent it = new Intent(this, BaoyangEditActivity.class);
                it.putExtra("RemindBaoyang", mRemindBaoyang);
                it.putExtra("vehicleNum", mVehicleNum);
                startActivityForResult(it, Constants.REQUEST_TYPE_BAOYANG);
                break;
            case R.id.edit:
                mPopupWindow.dismiss();
            case R.id.set_baoyang:
                it = new Intent(this, BaoyangEditActivity.class);
                it.putExtra("RemindBaoyang", mRemindBaoyang);
                it.putExtra("vehicleNum", mVehicleNum);
                startActivityForResult(it, Constants.REQUEST_TYPE_BAOYANG);
                break;
            case R.id.close:
                EventAnalysisUtil.onEvent(this, "close_MaintenNotify", "关闭保养提醒", null);
                mPopupWindow.dismiss();
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "关闭保养提醒");
                alertIntent.putExtra("message", "确定要关闭保养提醒吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_CLOSE);
                break;
            case R.id.vehicle_auth:
                it = new Intent(this, MemberPrivilegeActivity.class);
                it.putExtra("vehicleNum", mVehicleNum);
                startActivity(it);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        it.putExtra("position", mPosition);
        setResult(mResult, it);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_TYPE_BAOYANG:
                if (resultCode == Constants.RESULT_TYPE_CHANGED) {
                    mResult = resultCode;
                    mRemindBaoyang = (RemindBaoyang) data.getSerializableExtra("RemindBaoyang");
                    updateUI();
                }
                break;
            case Constants.REQUEST_TYPE_CLOSE:
                if (resultCode == RESULT_OK) {
                    Intent it = new Intent();
                    it.putExtra("position", mPosition);
                    setResult(Constants.RESULT_TYPE_REMOVED, it);
                    finish();
                }
                break;
        }

    }

    private void updateUI() {
        if (mRemindBaoyang == null) {
            mSetBaoyang.setVisibility(View.VISIBLE);
            mProgressLayout.setVisibility(View.GONE);
            mJiangeLine.setVisibility(View.GONE);
        } else {
            mSetBaoyang.setVisibility(View.GONE);
            mProgressLayout.setVisibility(View.VISIBLE);
//            mDate.setText("(" + mRemindBaoyang.getDate().replaceAll("-", "/") + ")");
            mDate.setText(mRemindBaoyang.getDate().replaceAll("-", "/"));
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindBaoyang.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
            if (gap <= 7) {
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3_red));
                int color = getResources().getColor(R.color.daze_darkred2);
                mDateLeft.setTextColor(color);
                mTian.setTextColor(color);
            } else {
//                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3));
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3_orangered5));
                int color = getResources().getColor(R.color.daze_black2);
                mDateLeft.setTextColor(color);
                mTian.setTextColor(color);
            }
            mDateLeft.setText(String.valueOf(gap));
            mProgress.setProgress(gap);

            if (mRemindBaoyang.getLicheng() > 0) {
                llLicheng.setVisibility(View.VISIBLE);
                tvSetLicheng.setVisibility(View.GONE);
                mLicheng.setText(mRemindBaoyang.getLicheng() + "km");
            } else {
                llLicheng.setVisibility(View.GONE);
                tvSetLicheng.setVisibility(View.VISIBLE);
            }

            if (mRemindBaoyang.getJiange() > 0) {
                mSetJiange.setVisibility(View.GONE);
                mJiange.setVisibility(View.VISIBLE);
                String strJiange = "每%dkm保养一次";
                mJiange.setText(String.format(strJiange, mRemindBaoyang.getJiange()));
            } else {
                mSetJiange.setVisibility(View.VISIBLE);
                mJiange.setVisibility(View.GONE);
            }
        }
    }

    private void showPopup() {
        View layout = LayoutInflater.from(this).inflate(R.layout.popup_edit_close, null, false);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.edit), this);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.close), this);
        mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(mRightView);
    }
}
