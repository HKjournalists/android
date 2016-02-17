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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/16 0016.
 */
public class CustomRemindActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RemindCustom mRemindCustom;
    private TextView mDate;
    private TextView mDateLeft;
    private TextView mTian;
    private TextView mTitleTv;
    private TextView mDateLeftLabel;
    private TextView tvCustomLableName = null;
    private ProgressBar mProgress;
    private PopupWindow mPopupWindow;
    private View mRightView;
    private int mPosition = 0;
    private int mResult = RESULT_CANCELED;
    private RelativeLayout rlLocation = null;
    private TextView mLocation;
    private View viewLine = null;
    private LinearLayout llNote = null;
    private TextView mRemark;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_custom_remind);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        mTitleTv = (TextView) findViewById(R.id.tvTitle);
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
        mDateLeftLabel = (TextView) findViewById(R.id.date_left_label);
        tvCustomLableName = (TextView) findViewById(R.id.tvCustomLableName);
        rlLocation = (RelativeLayout) findViewById(R.id.rlLocation);
        mLocation = (TextView) findViewById(R.id.location);
        viewLine = findViewById(R.id.viewLine);
        llNote = (LinearLayout) findViewById(R.id.llNote);
        mRemark = (TextView) findViewById(R.id.remark);
    }

    @Override
    protected void loadData() {
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindCustom = (RemindCustom) getIntent().getSerializableExtra("RemindCustom");
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText(mRemindCustom.getVehicleNum());
        updateUI();
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mRightView, this);
        ClickUtils.setNoFastClickListener(rlLocation, this);
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
                Intent it = new Intent(this, AddCustomRemindActivity.class);
                it.putExtra("RemindCustom", mRemindCustom);
                startActivityForResult(it, Constants.REQUEST_TYPE_CUSTOM);
                break;
            case R.id.close:
                mPopupWindow.dismiss();
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "关闭" + mRemindCustom.getName() + "提醒");
                alertIntent.putExtra("message", "确定要关闭" + mRemindCustom.getName() + "提醒吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_CLOSE);
                break;
            case R.id.rlLocation:
                Intent intent = new Intent(this, MarkLocationActivity.class);
                intent.putExtra("key-location-value", mRemindCustom.getLocation());
                startActivity(intent);
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
        if (requestCode == Constants.REQUEST_TYPE_CUSTOM && (resultCode == Constants.RESULT_TYPE_CHANGED || resultCode == Constants.RESULT_TYPE_RELOAD)) {
            mResult = resultCode;
            mRemindCustom = (RemindCustom) data.getSerializableExtra("RemindCustom");
            updateUI();
        } else if (requestCode == Constants.REQUEST_TYPE_CLOSE && resultCode == RESULT_OK) {
            Intent it = new Intent();
            it.putExtra("position", mPosition);
            setResult(Constants.RESULT_TYPE_REMOVED, it);
            finish();
        }
    }

    private void updateUI() {
        mTitleTv.setText(mRemindCustom.getName());
        tvCustomLableName.setText("距" + mRemindCustom.getName());
//        mDateLeftLabel.setText("距" + mRemindCustom.getName() + "到期还剩");
//        mDate.setText("(" + mRemindCustom.getDate().replaceAll("-", "/") + ")");
        mDateLeftLabel.setText(mRemindCustom.getName() + "时间");
        mDate.setText(mRemindCustom.getDate().replaceAll("-", "/"));
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindCustom.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
        if (mRemindCustom.getRepeatType() == Constants.REPEAT_TYPE_YEAR && gap <= 7 || gap <= 1) {
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

        String address = mRemindCustom.getLocation();
        String note = mRemindCustom.getRemark();

        if (!StringUtils.isEmpty(address) && StringUtils.isEmpty(note)) {
            rlLocation.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.GONE);
            llNote.setVisibility(View.GONE);
            mLocation.setText(mRemindCustom.getLocation());
        } else if (StringUtils.isEmpty(address) && !StringUtils.isEmpty(note)) {
            rlLocation.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
            llNote.setVisibility(View.VISIBLE);
            mRemark.setText(note);
        } else if (StringUtils.isEmpty(address) && StringUtils.isEmpty(note)) {
            rlLocation.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
            llNote.setVisibility(View.GONE);
        } else {
            rlLocation.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
            llNote.setVisibility(View.VISIBLE);
            mLocation.setText(mRemindCustom.getLocation());
            mRemark.setText(note);
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
