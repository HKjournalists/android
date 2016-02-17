package com.kplus.car.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindChedai;
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

public class ChedaiEditActivity extends BaseActivity implements ClickUtils.NoFastClickListener, RadioGroup.OnCheckedChangeListener {
    private RemindChedai mRemindChedai;
    private TextView mDate;
    private TextView mRemindDate;
    private TimeMenu mMenu;
    private String[] mRemindStrArr;
    private TextView mMoneyLabel;
    private EditText mMoney;
    private EditText mTotalMoney;
    private EditText mYihuanFenshu;
    private EditText mRemark;
    private View mFenshuLayout;
    private View mTotalLayout;
    private RadioGroup mRadioGroup;
    private int mPosition = 0;
    private String mMonthDate = "";
    private String mMonthRemindDate1 = "";
    private String mMonthOrignalDate = "";
    private String mYearDate = "";
    private String mYearRemindDate1 = "";
    private String mYearRemindDate2 = "";
    private String mYearOrignalDate = "";
    private boolean mIsEdit = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_chedai_edit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("设置车贷提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("车贷");
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("保存");

        mDate = (TextView) findViewById(R.id.date);
        mRemindDate = (TextView) findViewById(R.id.remind_date);
        mRemindStrArr = getResources().getStringArray(R.array.remind_date);
        mMoneyLabel = (TextView) findViewById(R.id.money_label);
        mMoney = (EditText) findViewById(R.id.money);
        mTotalMoney = (EditText) findViewById(R.id.total_money);
        mYihuanFenshu = (EditText) findViewById(R.id.yihuan_fenshu);
        mRemark = (EditText) findViewById(R.id.remark);
        mFenshuLayout = findViewById(R.id.fenshu_layout);
        mTotalLayout = findViewById(R.id.total_layout);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
    }

    @Override
    protected void loadData() {
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindChedai = (RemindChedai) getIntent().getSerializableExtra("RemindChedai");
        if (mRemindChedai != null){
            mIsEdit = true;
            try {
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindChedai.getRemindDate1());
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindChedai.getDate());
                int gap = DateUtil.getGapCount(start, end);
                mRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mRemindChedai.getRemindDate1().substring(mRemindChedai.getRemindDate1().indexOf(' ')));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH) {
                mMoneyLabel.setText("月还款金额");
                mFenshuLayout.setVisibility(View.VISIBLE);
                mTotalLayout.setVisibility(View.VISIBLE);
                mYihuanFenshu.setText(mRemindChedai.getFenshu() == 0 ? "" : String.valueOf(mRemindChedai.getFenshu()));
                mTotalMoney.setText(mRemindChedai.getTotal() == 0 ? "" : String.valueOf(mRemindChedai.getTotal()));
                mRadioGroup.check(R.id.radio_month);
                mMonthDate = mRemindChedai.getDate();
                mMonthRemindDate1 = mRemindChedai.getRemindDate1();
                mMonthOrignalDate = mRemindChedai.getOrignalDate();
                mDate.setText("".equals(mMonthDate) ? "" : "每月" + mRemindChedai.getDayOfMonth() + "日");
            }
            else {
                mMoneyLabel.setText("还款金额");
                mFenshuLayout.setVisibility(View.GONE);
                mTotalLayout.setVisibility(View.GONE);
                mRadioGroup.check(R.id.radio_year);
                mYearDate = mRemindChedai.getDate();
                mYearRemindDate1 = mRemindChedai.getRemindDate1();
                mYearRemindDate2 = mRemindChedai.getRemindDate2();
                mYearOrignalDate = mRemindChedai.getOrignalDate();
                mDate.setText(mRemindChedai.getDate().replaceAll("-", "/"));
            }
            mMoney.setText(mRemindChedai.getMoney() == 0 ? "" : String.valueOf(mRemindChedai.getMoney()));
            mRemark.setText(mRemindChedai.getRemark());
        }
        else{
            String vehicleNum = getIntent().getStringExtra("vehicleNum");
            mRemindChedai = new RemindChedai(vehicleNum, Calendar.getInstance());
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mDate, this);
        ClickUtils.setNoFastClickListener(mRemindDate, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
                if ("".equals(mDate.getText())){
                    ToastUtil.makeShortToast(this, "请设置还款时间");
                    return;
                }
                try{
                    String money = mMoney.getText().toString();
                    if ("".equals(money))
                        mRemindChedai.setMoney(0);
                    else
                        mRemindChedai.setMoney(Integer.parseInt(money));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    String total = mTotalMoney.getText().toString();
                    if ("".equals(total))
                        mRemindChedai.setTotal(0);
                    else
                        mRemindChedai.setTotal(Integer.parseInt(total));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    String fenshu = mYihuanFenshu.getText().toString();
                    if ("".equals(fenshu))
                        mRemindChedai.setFenshu(0);
                    else
                        mRemindChedai.setFenshu(Integer.parseInt(fenshu));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                mRemindChedai.setRemark(mRemark.getText().toString());
                if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH){
                    if (mRemindChedai.getMoney() > 0 && mRemindChedai.getMoney() * (mRemindChedai.getFenshu() - 1) >= mRemindChedai.getTotal()){
                        ToastUtil.makeShortToast(this, "已还金额不能大于贷款总额");
                        return;
                    }
                    Calendar remindDate = DateUtil.getCalender(mMonthRemindDate1, "yyyy-MM-dd HH:mm");
                    if (remindDate.before(Calendar.getInstance())){
                        Calendar oldDate = DateUtil.getCalender(mMonthDate, "yyyy-MM-dd");
                        Calendar date = Calendar.getInstance();
                        date.setTime(oldDate.getTime());
                        date.add(Calendar.MONTH, 1);
                        int day = mRemindChedai.getDayOfMonth();
                        if (day > date.getActualMaximum(Calendar.DAY_OF_MONTH))
                            day = date.getActualMaximum(Calendar.DAY_OF_MONTH);
                        date.set(Calendar.DATE, day);
                        mMonthDate = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
                        mMonthOrignalDate = mMonthDate;
                        int gap = DateUtil.getGapCount(oldDate.getTime(), date.getTime());
                        remindDate.add(Calendar.DATE, gap);
                        mMonthRemindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
                    }
                    mRemindChedai.setDate(mMonthDate);
                    mRemindChedai.setOrignalDate(mMonthOrignalDate);
                    mRemindChedai.setRemindDate1(mMonthRemindDate1);
                }
                else {
                    mRemindChedai.setDate(mYearDate);
                    mRemindChedai.setOrignalDate(mYearOrignalDate);
                    mRemindChedai.setRemindDate1(mYearRemindDate1);
                    mRemindChedai.setRemindDate2(mYearRemindDate2);
                    Calendar remindDate = Calendar.getInstance();
                    try {
                        remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindChedai.getRemindDate1()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (remindDate.before(Calendar.getInstance())){
                        ToastUtil.makeShortToast(this, "提醒时间已过");
                        return;
                    }
                }
                ToastUtil.makeShortToast(this, "设置车贷提醒成功");
                if (mIsEdit)
                    mApplication.dbCache.updateRemindChedai(mRemindChedai);
                else
                    mApplication.dbCache.saveRemindChedai(mRemindChedai);
                RemindManager.getInstance(this).set(Constants.REQUEST_TYPE_CHEDAI, mRemindChedai.getVehicleNum(), 0, null);
                if (mApplication.getId() != 0)
                    new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_CHEDAI);
                Intent it = new Intent();
                it.putExtra("position", mPosition);
                it.putExtra("RemindChedai", mRemindChedai);
                setResult(Constants.RESULT_TYPE_CHANGED, it);
                finish();
                break;
            case R.id.date:
                final Calendar calendar = Calendar.getInstance();
                try {
                    if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH)
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mMonthDate));
                    else
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mYearDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH){
                    mMenu = TimeMenuFactory.getInstance(this).buildDayOfMonthMenu(R.layout.menu_select_repeat, calendar, new TimeMenuFactory.onIimerMenuClick() {
                        @Override
                        public void onFinishClick(View v) {
                            mMenu.getSlideMenu().hide();
                            int day = mMenu.getFirst().getCurrentItem() + 1;
                            mDate.setText("每月" + day + "日");
                            mRemindChedai.setDayOfMonth(day);
                            Calendar cal = Calendar.getInstance();
                            if (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                                day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            cal.set(Calendar.DATE, day);
                            if (cal.before(Calendar.getInstance()))
                                cal.add(Calendar.MONTH, 1);
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                            mMonthDate = date;
                            if ("".equals(mRemindDate.getText())){
                                Calendar remindDate = cal;
                                remindDate.add(Calendar.DATE, -1);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mMonthRemindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
                                mRemindDate.setText("提前一天, 09:00");
                            }
                            else {
                                int gap = DateUtil.getGapCount(calendar.getTime(), cal.getTime());
                                Calendar remindDate = Calendar.getInstance();
                                try {
                                    remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mMonthRemindDate1));
                                    remindDate.add(Calendar.DATE, gap);
                                    mMonthRemindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
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
                }
                else {
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
                            mYearDate = date;
                            int gap = DateUtil.getGapCount(calendar.getTime(), mMenu.getCalendar().getTime());
                            if ("".equals(mRemindDate.getText())){
                                if (gap > 7){
                                    Calendar remindDate = mMenu.getCalendar();
                                    remindDate.add(Calendar.DATE, -7);
                                    remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                    remindDate.set(Calendar.MINUTE, 0);
                                    mYearRemindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
                                    mRemindDate.setText("提前七天, 09:00");
                                    remindDate.add(Calendar.DATE, 6);
                                    mYearRemindDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
                                }
                                else {
                                    Calendar remindDate = Calendar.getInstance();
                                    remindDate.add(Calendar.DATE, 1);
                                    remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                    remindDate.set(Calendar.MINUTE, 0);
                                    mYearRemindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
                                    gap = DateUtil.getGapCount(remindDate.getTime(), mMenu.getCalendar().getTime());
                                    mRemindDate.setText(mRemindStrArr[gap] + ", 09:00");
                                    if (gap > 2){
                                        remindDate = mMenu.getCalendar();
                                        remindDate.add(Calendar.DATE, -1);
                                        remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                        remindDate.set(Calendar.MINUTE, 0);
                                        mYearRemindDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
                                    }
                                }
                            }
                            else {
                                Calendar remindDate = Calendar.getInstance();
                                try {
                                    remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mYearRemindDate1));
                                    remindDate.add(Calendar.DATE, gap);
                                    mYearRemindDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                remindDate = Calendar.getInstance();
                                try {
                                    remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mYearRemindDate2));
                                    remindDate.add(Calendar.DATE, gap);
                                    mYearRemindDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime());
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
                }
                mMenu.getSlideMenu().show();
                break;
            case R.id.remind_date:
                if ("".equals(mDate.getText())){
                    ToastUtil.makeShortToast(this, "请先设置还款时间");
                    return;
                }
                int arrayId = R.array.remind_date;
                Calendar date = Calendar.getInstance();
                Calendar remind = Calendar.getInstance();
                try {
                    if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH){
                        arrayId = R.array.remind_date_2;
                        date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mMonthDate));
                        remind.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mMonthRemindDate1));
                    }
                    else {
                        arrayId = R.array.remind_date;
                        date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mYearDate));
                        remind.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mYearRemindDate1));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildRemindMenu(R.layout.menu_select_date, arrayId, remind, date, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_YEAR && mMenu.getCalendar().before(Calendar.getInstance())) {
                            ToastUtil.makeShortToast(getApplicationContext(), "提醒时间已过");
                            return;
                        }
                        mMenu.getSlideMenu().hide();
                        String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mMenu.getCalendar().getTime());
                        mRemindDate.setText(mRemindStrArr[mMenu.getFirst().getCurrentItem()] + "," + strDate.substring(strDate.indexOf(' ')));
                        if (mRemindChedai.getRepeatType() == Constants.REPEAT_TYPE_MONTH){
                            mMonthRemindDate1 = strDate;
                        }
                        else {
                            mYearRemindDate1 = strDate;
                            if (mMenu.getFirst().getCurrentItem() == 1){
                                mYearRemindDate2 = "";
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
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton btn = (RadioButton) radioGroup.findViewById(i);
        btn.toggle();
        switch (i){
            case R.id.radio_month:
                mRemindChedai.setRepeatType(Constants.REPEAT_TYPE_MONTH);
                mMoneyLabel.setText("月还款金额");
                mFenshuLayout.setVisibility(View.VISIBLE);
                mTotalLayout.setVisibility(View.VISIBLE);
                mDate.setText("".equals(mMonthDate) ? "" : "每月" + mRemindChedai.getDayOfMonth() + "日");
                try {
                    Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mMonthRemindDate1);
                    Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mMonthDate);
                    int gap = DateUtil.getGapCount(start, end);
                    mRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mMonthRemindDate1.substring(mMonthRemindDate1.indexOf(' ')));
                } catch (ParseException e) {
                    mRemindDate.setText("");
                }
                break;
            case R.id.radio_year:
                mRemindChedai.setRepeatType(Constants.REPEAT_TYPE_YEAR);
                mMoneyLabel.setText("还款金额");
                mFenshuLayout.setVisibility(View.GONE);
                mTotalLayout.setVisibility(View.GONE);
                mDate.setText(mYearDate);
                try {
                    Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mYearRemindDate1);
                    Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mYearDate);
                    int gap = DateUtil.getGapCount(start, end);
                    mRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mYearRemindDate1.substring(mYearRemindDate1.indexOf(' ')));
                } catch (ParseException e) {
                    mRemindDate.setText("");
                }
                break;
        }
    }
}
