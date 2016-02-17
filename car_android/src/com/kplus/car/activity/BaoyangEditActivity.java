package com.kplus.car.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindBaoyang;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaoyangEditActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RemindBaoyang mRemindBaoyang;
    private TextView mDate;
    private TextView mRemindDate;
    private TimeMenu mMenu;
    private String[] mRemindStrArr;
    private EditText mLicheng;
    private EditText mJiange;
    private int mPosition = 0;
    private boolean mIsEdit = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_baoyang_edit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("设置保养提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("保养");
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("保存");

        mDate = (TextView) findViewById(R.id.date);
        mRemindDate = (TextView) findViewById(R.id.remind_date);
        mRemindStrArr = getResources().getStringArray(R.array.remind_date);
        mLicheng = (EditText) findViewById(R.id.licheng);
        mJiange = (EditText) findViewById(R.id.jiange);
    }

    @Override
    protected void loadData() {
        EventAnalysisUtil.onEvent(this, "goto_SetMaintenNotifyVC", "进入‘设置保养提醒’", null);
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindBaoyang = (RemindBaoyang) getIntent().getSerializableExtra("RemindBaoyang");
        if (mRemindBaoyang != null){
            mIsEdit = true;
            mLicheng.setText(mRemindBaoyang.getLicheng() == 0 ? "" : String.valueOf(mRemindBaoyang.getLicheng()));
            mJiange.setText(mRemindBaoyang.getJiange() == 0 ? "" : String.valueOf(mRemindBaoyang.getJiange()));
            mDate.setText(mRemindBaoyang.getDate().replaceAll("-", "/"));
            try {
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindBaoyang.getRemindDate1());
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindBaoyang.getDate());
                int gap = DateUtil.getGapCount(start, end);
                mRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mRemindBaoyang.getRemindDate1().substring(mRemindBaoyang.getRemindDate1().indexOf(' ')));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            mRemindBaoyang = new RemindBaoyang();
            mRemindBaoyang.setVehicleNum(getIntent().getStringExtra("vehicleNum"));
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mDate, this);
        ClickUtils.setNoFastClickListener(mRemindDate, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
                if ("".equals(mDate.getText())){
                    ToastUtil.makeShortToast(this, "请设置年检时间");
                    return;
                }
                Calendar remindDate = Calendar.getInstance();
                try {
                    remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindBaoyang.getRemindDate1()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (remindDate.before(Calendar.getInstance())){
                    ToastUtil.makeShortToast(this, "提醒时间已过");
                    return;
                }
                ToastUtil.makeShortToast(this, "设置保养提醒成功");
                try{
                    String licheng = mLicheng.getText().toString();
                    if ("".equals(licheng))
                        mRemindBaoyang.setLicheng(0);
                    else
                        mRemindBaoyang.setLicheng(Integer.parseInt(licheng));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    String jiange = mJiange.getText().toString();
                    if ("".equals(jiange))
                        mRemindBaoyang.setJiange(0);
                    else
                        mRemindBaoyang.setJiange(Integer.parseInt(jiange));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if (mIsEdit)
                    mApplication.dbCache.updateRemindBaoyang(mRemindBaoyang);
                else
                    mApplication.dbCache.saveRemindBaoyang(mRemindBaoyang);
                RemindManager.getInstance(this).set(Constants.REQUEST_TYPE_BAOYANG, mRemindBaoyang.getVehicleNum(), 0, null);
                if (mApplication.getId() != 0)
                    new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_BAOYANG);
                Intent it = new Intent();
                it.putExtra("position", mPosition);
                it.putExtra("RemindBaoyang", mRemindBaoyang);
                setResult(Constants.RESULT_TYPE_CHANGED, it);
                EventAnalysisUtil.onEvent(this, "set_MaintenNotify_success", "设置保养提醒成功", null);
                finish();
                break;
            case R.id.date:
                final Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindBaoyang.getDate()));
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
                        mRemindBaoyang.setDate(date);
                        mRemindBaoyang.setOrignalDate(date);
                        int gap = DateUtil.getGapCount(calendar.getTime(), mMenu.getCalendar().getTime());
                        if ("".equals(mRemindDate.getText())){
                            if (gap > 7){
                                Calendar remindDate = mMenu.getCalendar();
                                remindDate.add(Calendar.DATE, -7);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindBaoyang.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mRemindDate.setText("提前七天, 09:00");
                                remindDate.add(Calendar.DATE, 6);
                                mRemindBaoyang.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else {
                                Calendar remindDate = Calendar.getInstance();
                                remindDate.add(Calendar.DATE, 1);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindBaoyang.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                gap = DateUtil.getGapCount(remindDate.getTime(), mMenu.getCalendar().getTime());
                                mRemindDate.setText(mRemindStrArr[gap] + ", 09:00");
                                if (gap > 2){
                                    remindDate = mMenu.getCalendar();
                                    remindDate.add(Calendar.DATE, -1);
                                    remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                    remindDate.set(Calendar.MINUTE, 0);
                                    mRemindBaoyang.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                }
                            }
                        }
                        else {
                            Calendar remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindBaoyang.getRemindDate1()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindBaoyang.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindBaoyang.getRemindDate2()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindBaoyang.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
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
                    ToastUtil.makeShortToast(this, "请先设置保养时间");
                    return;
                }
                Calendar date = Calendar.getInstance();
                Calendar remind = Calendar.getInstance();
                try {
                    date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindBaoyang.getDate()));
                    remind.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindBaoyang.getRemindDate1()));
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
                        mRemindBaoyang.setRemindDate1(strDate);
                        if (mMenu.getFirst().getCurrentItem() == 1){
                            mRemindBaoyang.setRemindDate2("");
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
}
