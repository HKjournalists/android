package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NianjianActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RemindNianjian mRemindNianjian;
    private TextView mDate;
    private TextView mDateLeft;
    private TextView mTian;
    private ProgressBar mProgress;
    private PopupWindow mPopupWindow;
    private View mRightView;
    private View mAuth;
    private View mHint;
    private int mPosition = 0;
    private int mResult = RESULT_CANCELED;
    private PopupWindow mRulePopup;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_nianjian);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("年检提醒");
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
        mAuth = findViewById(R.id.vehicle_auth);
        mHint = findViewById(R.id.hint);
    }

    @Override
    protected void loadData() {
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindNianjian = (RemindNianjian) getIntent().getSerializableExtra("RemindNianjian");
        if (mRemindNianjian == null) {
            String vehicleNum = getIntent().getStringExtra("vehicleNum");
            Intent it = new Intent(this, NianjianEditActivity.class);
            it.putExtra("vehicleNum", vehicleNum);
            startActivityForResult(it, Constants.REQUEST_TYPE_NIANJIAN);
        } else {
            EventAnalysisUtil.onEvent(this, "goto_InspectionDetail", "进入年检提醒详情", null);
            VehicleAuth vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(mRemindNianjian.getVehicleNum());
            if (vehicleAuth == null || vehicleAuth.getStatus() != 2) {
                mAuth.setVisibility(View.VISIBLE);
                mHint.setVisibility(View.VISIBLE);
            }
            TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
            tvLeft.setText(mRemindNianjian.getVehicleNum());
            updateUI();
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mRightView, this);
        ClickUtils.setNoFastClickListener(mAuth, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rule), this);
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
            case R.id.edit:
                mPopupWindow.dismiss();
                Intent it = new Intent(this, NianjianEditActivity.class);
                it.putExtra("RemindNianjian", mRemindNianjian);
                startActivityForResult(it, Constants.REQUEST_TYPE_NIANJIAN);
                break;
            case R.id.close:
                EventAnalysisUtil.onEvent(this, "close_InspectionNotify", "点‘关闭‘年检提醒", null);
                mPopupWindow.dismiss();
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "关闭年检提醒");
                alertIntent.putExtra("message", "确定要关闭年检提醒吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_CLOSE);
                break;
            case R.id.vehicle_auth:
                it = new Intent(this, MemberPrivilegeActivity.class);
                it.putExtra("vehicleNum", mRemindNianjian.getVehicleNum());
                startActivity(it);
                break;
            case R.id.rule:
                EventAnalysisUtil.onEvent(this, "click_InspectionRule", "点“机动车年检新规定”", null);
                showRulePopup();
                break;
            case R.id.close_rule:
                mRulePopup.dismiss();
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
        if (requestCode == Constants.REQUEST_TYPE_NIANJIAN && resultCode == Constants.RESULT_TYPE_CHANGED) {
            mResult = resultCode;
            mRemindNianjian = (RemindNianjian) data.getSerializableExtra("RemindNianjian");
            updateUI();
        } else if (requestCode == Constants.REQUEST_TYPE_CLOSE && resultCode == RESULT_OK) {
            Intent it = new Intent();
            it.putExtra("position", mPosition);
            setResult(Constants.RESULT_TYPE_REMOVED, it);
            finish();
        }
    }

    private void updateUI() {
//        mDate.setText("(" + mRemindNianjian.getDate().replaceAll("-", "/") + ")");
        mDate.setText(mRemindNianjian.getDate().replaceAll("-", "/"));
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getDate());
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
//            mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3));
            mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress_bar3_orangered5));
            int color = getResources().getColor(R.color.daze_black2);
            mDateLeft.setTextColor(color);
            mTian.setTextColor(color);
        }
        mDateLeft.setText(String.valueOf(gap));
        mProgress.setProgress(gap);
    }

    private void showRulePopup() {
        View layout = LayoutInflater.from(this).inflate(R.layout.popup_rule, null, false);
        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText("机动车年检新规定");
        TextView content = (TextView) layout.findViewById(R.id.content);
        content.setText(R.string.nianjian_rule);
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.close_rule), this);
        mRulePopup = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRulePopup.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
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
