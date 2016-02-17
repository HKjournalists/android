package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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
import com.kplus.car.model.RemindChedai;
import com.kplus.car.model.VehicleAuth;
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
public class ChedaiActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RemindChedai mRemindChedai;
    private TextView mDate;
    private TextView mDateLeft;
    private TextView mTian;
    private ProgressBar mProgress;
    private PopupWindow mPopupWindow;
    private View mRightView;
    private View mSetMoney;
    private View mMoneyLayout;
    private TextView mMoney;
    private TextView mTotal;
    private TextView mLeftMonth;
    private TextView tvNote = null;
    private View mAuth;
    private View mHint;
    private View mLine;
    private int mPosition = 0;
    private int mResult = RESULT_CANCELED;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_chedai);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("车贷还款提醒");
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
        mSetMoney = findViewById(R.id.set_money);
        mMoneyLayout = findViewById(R.id.money_layout);
        mMoney = (TextView) findViewById(R.id.money);
        mMoney.setTypeface(mApplication.mDin);
        mTotal = (TextView) findViewById(R.id.total);
        mLeftMonth = (TextView) findViewById(R.id.left_month);
        tvNote = (TextView) findViewById(R.id.tvNote);
        mAuth = findViewById(R.id.vehicle_auth);
        mHint = findViewById(R.id.hint);
        mLine = findViewById(R.id.line);
    }

    @Override
    protected void loadData() {
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindChedai = (RemindChedai) getIntent().getSerializableExtra("RemindChedai");
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText(mRemindChedai.getVehicleNum());
        VehicleAuth vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(mRemindChedai.getVehicleNum());
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
        ClickUtils.setNoFastClickListener(mSetMoney, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.vehicle_auth), this);
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
            case R.id.set_money:
                Intent it = new Intent(this, ChedaiEditActivity.class);
                it.putExtra("RemindChedai", mRemindChedai);
                startActivityForResult(it, Constants.REQUEST_TYPE_CHEDAI);
                break;
            case R.id.close:
                mPopupWindow.dismiss();
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "关闭车贷提醒");
                alertIntent.putExtra("message", "确定要关闭车贷提醒吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_CLOSE);
                break;
            case R.id.vehicle_auth:
                it = new Intent(this, MemberPrivilegeActivity.class);
                it.putExtra("vehicleNum", mRemindChedai.getVehicleNum());
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
        if (requestCode == Constants.REQUEST_TYPE_CHEDAI && resultCode == Constants.RESULT_TYPE_CHANGED) {
            mResult = resultCode;
            mRemindChedai = (RemindChedai) data.getSerializableExtra("RemindChedai");
            updateUI();
        } else if (requestCode == Constants.REQUEST_TYPE_CLOSE && resultCode == RESULT_OK) {
            Intent it = new Intent();
            it.putExtra("position", mPosition);
            setResult(Constants.RESULT_TYPE_REMOVED, it);
            finish();
        }
    }

    private void updateUI() {
//        mDate.setText(mRemindChedai.getDate());
        mDate.setText(mRemindChedai.getDate().replaceAll("-", "/"));
        String note = mRemindChedai.getRemark();
        if (!StringUtils.isEmpty(note)) {
            tvNote.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
            tvNote.setText(note);
        } else {
            tvNote.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
        }
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindChedai.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), date);
        if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_YEAR && gap <= 7 || gap <= 1) {
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
        if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH) {
            mProgress.setMax(30);
            mTotal.setVisibility(View.VISIBLE);
            if (mRemindChedai.getMoney() == 0 || mRemindChedai.getTotal() == 0) {
                mSetMoney.setVisibility(View.VISIBLE);
                mMoneyLayout.setVisibility(View.GONE);
                mLeftMonth.setVisibility(View.GONE);
            } else {
                mSetMoney.setVisibility(View.GONE);
                mMoneyLayout.setVisibility(View.VISIBLE);
                mLeftMonth.setVisibility(View.VISIBLE);
                mMoney.setText("¥" + mRemindChedai.getMoney());
                mTotal.setText("/¥" + mRemindChedai.getTotal());
                int leftMoney = mRemindChedai.getTotal() - mRemindChedai.getMoney() * mRemindChedai.getFenshu();
                if (leftMoney < 0)
                    leftMoney = 0;
                int leftMonth = leftMoney / mRemindChedai.getMoney();
                if (leftMoney % mRemindChedai.getMoney() != 0)
                    leftMonth++;
                mLeftMonth.setText("(剩余" + leftMoney + "元/" + leftMonth + "个月)");
            }
        } else {
            mProgress.setMax(365);
            mTotal.setVisibility(View.GONE);
            mLeftMonth.setVisibility(View.GONE);
            if (mRemindChedai.getMoney() == 0) {
                mSetMoney.setVisibility(View.VISIBLE);
                mMoneyLayout.setVisibility(View.GONE);
            } else {
                mSetMoney.setVisibility(View.GONE);
                mMoneyLayout.setVisibility(View.VISIBLE);
                mMoney.setText("¥" + mRemindChedai.getMoney());
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
