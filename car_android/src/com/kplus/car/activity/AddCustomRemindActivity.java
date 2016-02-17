package com.kplus.car.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/17 0017.
 */
public class AddCustomRemindActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RemindCustom mRemindCustom;
    private EditText mName;
    private TextView mDate;
    private TextView mRemindDate;
    private TimeMenu mMenu;
    private TextView mLocation;
    private EditText mRemark;
    private TextView mRepeatType;
    private String[] mRemindStrArr;
    private String[] mRepeatStrArr;
    private int mPosition = 0;
    private boolean mIsEdit = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_add_custom_remind);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("设置自定义提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("提醒");
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("保存");

        mName = (EditText) findViewById(R.id.remind_name);
        mDate = (TextView) findViewById(R.id.date);
        mRemindDate = (TextView) findViewById(R.id.remind_date);
        mLocation = (TextView) findViewById(R.id.location);
        mRemark = (EditText) findViewById(R.id.remark);
        mRepeatType = (TextView) findViewById(R.id.repeat_type);
        mRemindStrArr = getResources().getStringArray(R.array.remind_date);
        mRepeatStrArr = getResources().getStringArray(R.array.repeat_type);
    }

    @Override
    protected void loadData() {
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindCustom = (RemindCustom) getIntent().getSerializableExtra("RemindCustom");
        if (mRemindCustom != null){
            mIsEdit = true;
            mName.setText(mRemindCustom.getName());
            mDate.setText(mRemindCustom.getDate().replaceAll("-", "/"));
            try {
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindCustom.getRemindDate1());
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindCustom.getDate());
                int gap = DateUtil.getGapCount(start, end);
                mRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mRemindCustom.getRemindDate1().substring(mRemindCustom.getRemindDate1().indexOf(' ')));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch (mRemindCustom.getRepeatType()){
                case Constants.REPEAT_TYPE_NONE:
                    mRepeatType.setText(mRepeatStrArr[0]);
                    break;
                case Constants.REPEAT_TYPE_DAY:
                    mRepeatType.setText(mRepeatStrArr[1]);
                    break;
                case Constants.REPEAT_TYPE_WEEK:
                    mRepeatType.setText(mRepeatStrArr[2]);
                    break;
                default:
                    mRepeatType.setText(mRepeatStrArr[mRemindCustom.getRepeatType() - Constants.REPEAT_TYPE_NONE + 2]);
                    break;
            }
            mRemark.setText(mRemindCustom.getRemark());
            mLocation.setText(mRemindCustom.getLocation());
        }
        else {
            String vehicleNum = getIntent().getStringExtra("vehicleNum");
            mRemindCustom = new RemindCustom(vehicleNum);
            mRepeatType.setText(mRepeatStrArr[0]);
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mDate, this);
        ClickUtils.setNoFastClickListener(mRemindDate, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        ClickUtils.setNoFastClickListener(mLocation, this);
        ClickUtils.setNoFastClickListener(mRepeatType, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
                if ("".equals(mName.getText().toString())){
                    ToastUtil.makeShortToast(this, "请设置提醒名称");
                    return;
                }
                if ("".equals(mRemindCustom.getDate())){
                    ToastUtil.makeShortToast(this, "请设置发生时间");
                    return;
                }
                if ("".equals(mRemindCustom.getRemindDate1())){
                    ToastUtil.makeShortToast(this, "请设置提醒时间");
                    return;
                }
                if (!mIsEdit && mApplication.dbCache.getRemindCustom(mRemindCustom.getVehicleNum(), mName.getText().toString()) != null){
                    ToastUtil.makeShortToast(this, "该提醒名称已存在，请重新输入");
                }
                Calendar remindDate = Calendar.getInstance();
                try {
                    remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindCustom.getRemindDate1()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (remindDate.before(Calendar.getInstance())){
                    ToastUtil.makeShortToast(this, "提醒时间已过");
                    return;
                }
                mRemindCustom.setLocation(mLocation.getText().toString());
                mRemindCustom.setRemark(mRemark.getText().toString());
                String originalName = mRemindCustom.getName();
                mRemindCustom.setName(mName.getText().toString());
                ToastUtil.makeShortToast(this, "设置" + mRemindCustom.getName() + "提醒成功");
                if (mIsEdit) {
                    mApplication.dbCache.updateRemindCustom(mRemindCustom);
                    if (!originalName.equals(mRemindCustom.getName())){
                        RemindInfo info = mApplication.dbCache.getRemindInfo(mRemindCustom.getVehicleNum(), originalName);
                        info.setTitle(mRemindCustom.getName());
                        mApplication.dbCache.updateRemindInfo(info);
                        if (mApplication.getId() != 0)
                            new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_REMIND);
                    }
                }
                else {
                   mApplication.dbCache.saveRemindCustom(mRemindCustom);
                }
                RemindManager.getInstance(this).set(Constants.REQUEST_TYPE_CUSTOM, mRemindCustom.getVehicleNum(), 0, mRemindCustom.getName());
                if (mApplication.getId() != 0)
                    new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_CUSTOM);
                Intent it = new Intent();
                it.putExtra("position", mPosition);
                it.putExtra("RemindCustom", mRemindCustom);
                if (mIsEdit) {
                    if (!originalName.equals(mRemindCustom.getName()))
                        setResult(Constants.RESULT_TYPE_RELOAD, it);
                    else
                        setResult(Constants.RESULT_TYPE_CHANGED, it);
                }
                else
                    setResult(Constants.RESULT_TYPE_ADDED, it);
                finish();
                break;
            case R.id.date:
                final Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindCustom.getOrignalDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, calendar, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        if (mMenu.getCalendar().before(Calendar.getInstance())) {
                            ToastUtil.makeShortToast(getApplicationContext(), "无法设置早于当前的时间");
                            return;
                        }
                        mMenu.getSlideMenu().hide();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(mMenu.getCalendar().getTime());
                        mDate.setText(date.replaceAll("-", "/"));
                        mRemindCustom.setDate(date);
                        mRemindCustom.setOrignalDate(date);
                        int gap = DateUtil.getGapCount(calendar.getTime(), mMenu.getCalendar().getTime());
                        if ("".equals(mRemindDate.getText())){
                            if (gap > 7){
                                Calendar remindDate = mMenu.getCalendar();
                                remindDate.add(Calendar.DATE, -7);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindCustom.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mRemindDate.setText("提前七天, 09:00");
                                remindDate.add(Calendar.DATE, 6);
                                mRemindCustom.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else {
                                Calendar remindDate = Calendar.getInstance();
                                remindDate.add(Calendar.DATE, 1);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindCustom.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                gap = DateUtil.getGapCount(remindDate.getTime(), mMenu.getCalendar().getTime());
                                mRemindDate.setText(mRemindStrArr[gap] + ", 09:00");
                                if (gap > 2){
                                    remindDate = mMenu.getCalendar();
                                    remindDate.add(Calendar.DATE, -1);
                                    remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                    remindDate.set(Calendar.MINUTE, 0);
                                    mRemindCustom.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                }
                            }
                        }
                        else {
                            Calendar remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindCustom.getRemindDate1()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindCustom.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindCustom.getRemindDate2()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindCustom.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
            case R.id.remind_date:
                if ("".equals(mDate.getText())){
                    ToastUtil.makeShortToast(this, "请先设置发生时间");
                    return;
                }
                Calendar date = Calendar.getInstance();
                Calendar remind = Calendar.getInstance();
                try {
                    date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindCustom.getDate()));
                    remind.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindCustom.getRemindDate1()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildRemindMenu(R.layout.menu_select_date, remind, date, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        if (mMenu.getCalendar().before(Calendar.getInstance())) {
                            ToastUtil.makeShortToast(getApplicationContext(), "提醒时间已过");
                            return;
                        }
                        mMenu.getSlideMenu().hide();
                        String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mMenu.getCalendar().getTime());
                        mRemindDate.setText(mRemindStrArr[mMenu.getFirst().getCurrentItem()] + "," + strDate.substring(strDate.indexOf(' ')));
                        mRemindCustom.setRemindDate1(strDate);
                        if (mMenu.getFirst().getCurrentItem() == 1){
                            mRemindCustom.setRemindDate2("");
                        }
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
            case R.id.location:
                it = new Intent(this, MarkLocationActivity.class);
                startActivityForResult(it, Constants.REQUEST_TYPE_MAP);
                break;
            case R.id.repeat_type:
                mMenu = TimeMenuFactory.getInstance(this).buildRepeatMenu(R.layout.menu_select_repeat, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mMenu.getSlideMenu().hide();
                        mRepeatType.setText(mRepeatStrArr[mMenu.getFirst().getCurrentItem()]);
                        switch (mMenu.getFirst().getCurrentItem()){
                            case 0:
                                mRemindCustom.setRepeatType(Constants.REPEAT_TYPE_NONE);
                                break;
                            case 1:
                                mRemindCustom.setRepeatType(Constants.REPEAT_TYPE_DAY);
                                break;
                            case 2:
                                mRemindCustom.setRepeatType(Constants.REPEAT_TYPE_WEEK);
                                break;
                            default:
                                mRemindCustom.setRepeatType(Constants.REPEAT_TYPE_NONE + mMenu.getFirst().getCurrentItem() - 2);
                                break;
                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_TYPE_MAP && resultCode == Constants.RESULT_TYPE_CHANGED){
            mLocation.setText(data.getStringExtra("location"));
        }
    }
}
