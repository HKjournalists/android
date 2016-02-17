package com.kplus.car.activity;

import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RestrictSaveTask;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.response.RestrictSaveResponse;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/7/8.
 */
public class RestrictEditActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private UserVehicle mUserVehicle;
    private RemindRestrict mRemindRestrict;
    private TextView mTitle;
    private View mHint;
    private TextView mRestrictRegion;
    private TextView mDate;
    private View mBtnOnOff;
    private View mPhoneLayout;
    private EditText mPhone;
    private TimeMenu mMenu;
    private boolean mIsEdit = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_restrict_edit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("限行提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("保存");

        mTitle = (TextView) findViewById(R.id.title);
        mHint = findViewById(R.id.hint);
        mRestrictRegion = (TextView) findViewById(R.id.restrict_region);
        mDate = (TextView) findViewById(R.id.date);
        mBtnOnOff = findViewById(R.id.btn_on_off);
        mPhoneLayout = findViewById(R.id.phone_layout);
        mPhone = (EditText) findViewById(R.id.phone);
    }

    @Override
    protected void loadData() {
        mIsEdit = getIntent().getBooleanExtra("edit", false);
        mUserVehicle = (UserVehicle) getIntent().getSerializableExtra("UserVehicle");
        mRemindRestrict = (RemindRestrict) getIntent().getSerializableExtra("RemindRestrict");
        if (mUserVehicle != null){
            mTitle.setText(DateUtil.getRestrictDesc(this, mUserVehicle.getRestrictNums(), mUserVehicle.getVehicleNum()));
            if (!StringUtils.isEmpty(mUserVehicle.getRestrictRegion())){
                mRestrictRegion.setVisibility(View.VISIBLE);
                mRestrictRegion.setText(mUserVehicle.getRestrictRegion());
            }
        }
        if (mRemindRestrict != null){
            if ("0".equals(mRemindRestrict.getIsHidden())){
                mHint.setVisibility(View.VISIBLE);
            }
            else {
                mHint.setVisibility(View.GONE);
            }
            mRemindRestrict.setIsHidden("0");
            String remindDateType = "1".equals(mRemindRestrict.getRemindDateType()) ? "限行前一天" : "限行当天";
            mDate.setText(remindDateType + mRemindRestrict.getRemindDate());
            if ("1".equals(mRemindRestrict.getMessageRemind())){
                mBtnOnOff.setSelected(true);
                mPhoneLayout.setVisibility(View.VISIBLE);
                mPhone.setText(mRemindRestrict.getPhone());
            } else {
                mBtnOnOff.setSelected(false);
                mPhoneLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        ClickUtils.setNoFastClickListener(mBtnOnOff, this);
        ClickUtils.setNoFastClickListener(mDate, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.leftView:
                finish();
                break;
            case R.id.rightView:
                if ("1".equals(mRemindRestrict.getMessageRemind())){
                    if (StringUtils.isEmpty(mPhone.getText().toString())){
                        ToastUtil.makeShortToast(this, "请填写提醒号码");
                        return;
                    }
                    else {
                        mRemindRestrict.setPhone(mPhone.getText().toString());
                    }
                }
                new RestrictSaveTask(mApplication){
                    @Override
                    protected void onPostExecute(RestrictSaveResponse response) {
                        if (response != null && response.getCode() != null && response.getCode() == 0){
                            if (mIsEdit) {
                                ToastUtil.makeShortToast(RestrictEditActivity.this, "修改限行提醒成功");
                            }
                            else {
                                ToastUtil.makeShortToast(RestrictEditActivity.this, "限行提醒已开启");
                            }
                            mApplication.dbCache.updateRemindRestrict(mRemindRestrict);
                            RemindManager.getInstance(RestrictEditActivity.this).set(Constants.REQUEST_TYPE_RESTRICT, mUserVehicle.getVehicleNum(), 0, null);
                            setResult(Constants.RESULT_TYPE_CHANGED);
                            finish();
                        }
                        else {
                            ToastUtil.makeShortToast(RestrictEditActivity.this, "限行提醒保存失败");
                        }
                    }
                }.execute(mRemindRestrict);
                break;
            case R.id.btn_on_off:
                if (mBtnOnOff.isSelected()){
                    mBtnOnOff.setSelected(false);
                    mPhoneLayout.setVisibility(View.GONE);
                    mRemindRestrict.setMessageRemind("0");
                }
                else {
                    mBtnOnOff.setSelected(true);
                    mPhoneLayout.setVisibility(View.VISIBLE);
                    mRemindRestrict.setMessageRemind("1");
                }
                break;
            case R.id.date:
                mMenu = TimeMenuFactory.getInstance(this).buildRestrictMenu(R.layout.menu_select_date, mRemindRestrict.getRemindDateType(), mRemindRestrict.getRemindDate(), new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mMenu.getSlideMenu().hide();
                        mRemindRestrict.setRemindDateType(String.valueOf(mMenu.getFirst().getCurrentItem()));
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, mMenu.getSecond().getCurrentItem());
                        calendar.set(Calendar.MINUTE, mMenu.getThird().getCurrentItem());
                        mRemindRestrict.setRemindDate(new SimpleDateFormat("HH:mm").format(calendar.getTime()));
                        String remindDateType = "1".equals(mRemindRestrict.getRemindDateType()) ? "限行前一天" : "限行当天";
                        mDate.setText(remindDateType + mRemindRestrict.getRemindDate());
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
        }
    }
}
