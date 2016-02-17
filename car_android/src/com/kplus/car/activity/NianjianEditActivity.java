package com.kplus.car.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.UserVehicle;
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

public class NianjianEditActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RemindNianjian mRemindNianjian;
    private TextView mDate;
    private TextView mRemindDate;
    private TimeMenu mMenu;
    private String[] mRemindStrArr;
    private int mPosition = 0;
    private PopupWindow mPopupWindow;
    private boolean mIsEdit = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_nianjian_edit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("设置年检提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("年检");
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("保存");

        mDate = (TextView) findViewById(R.id.date);
        mRemindDate = (TextView) findViewById(R.id.remind_date);
        mRemindStrArr = getResources().getStringArray(R.array.remind_date);
    }

    @Override
    protected void loadData() {
        mPosition = getIntent().getIntExtra("position", 0);
        mRemindNianjian = (RemindNianjian) getIntent().getSerializableExtra("RemindNianjian");
        if (mRemindNianjian != null){
            mIsEdit = true;
            mDate.setText(mRemindNianjian.getOrignalDate().replaceAll("-", "/"));
            try {
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getRemindDate1());
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getDate());
                int gap = DateUtil.getGapCount(start, end);
                mRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mRemindNianjian.getRemindDate1().substring(mRemindNianjian.getRemindDate1().indexOf(' ')));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            mRemindNianjian = new RemindNianjian();
            mRemindNianjian.setVehicleNum(getIntent().getStringExtra("vehicleNum"));
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mDate, this);
        ClickUtils.setNoFastClickListener(mRemindDate, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.vl_info), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
                if ("".equals(mDate.getText())){
                    ToastUtil.makeShortToast(this, "请设置年检日期");
                    return;
                }
                Calendar remindDate = Calendar.getInstance();
                try {
                    remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindNianjian.getRemindDate1()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (remindDate.before(Calendar.getInstance())){
                    ToastUtil.makeShortToast(this, "提醒时间已过");
                    return;
                }
                ToastUtil.makeShortToast(this, "设置年检提醒成功");
                mRemindNianjian.setFromType(0);
                if (mIsEdit)
                    mApplication.dbCache.updateRemindNianjian(mRemindNianjian);
                else
                    mApplication.dbCache.saveRemindNianjian(mRemindNianjian);
                UserVehicle uv = mApplication.dbCache.getVehicle(mRemindNianjian.getVehicleNum());
                uv.setNianjianDate(mRemindNianjian.getOrignalDate());
                mApplication.dbCache.updateVehicle(uv);
                RemindManager.getInstance(this).set(Constants.REQUEST_TYPE_NIANJIAN, mRemindNianjian.getVehicleNum(), 0, null);
                if (mApplication.getId() != 0)
                    new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_NIANJIAN);
                Intent it = new Intent();
                it.putExtra("position", mPosition);
                it.putExtra("RemindNianjian", mRemindNianjian);
                setResult(Constants.RESULT_TYPE_CHANGED, it);
                EventAnalysisUtil.onEvent(this, "set_InspectionDate_success", "设置年检日期成功", null);
                finish();
                break;
            case R.id.date:
                final Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getOrignalDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, calendar, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mMenu.getSlideMenu().hide();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(mMenu.getCalendar().getTime());
                        mDate.setText(date.replaceAll("-", "/"));
                        mRemindNianjian.setOrignalDate(date);
                        calendar.setTime(mMenu.getCalendar().getTime());
                        while (calendar.before(Calendar.getInstance()))
                            calendar.add(Calendar.YEAR, 1);
                        Calendar oldCal = Calendar.getInstance();
                        try {
                            oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getDate()));
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int gap = DateUtil.getGapCount(oldCal.getTime(), calendar.getTime());
                        mRemindNianjian.setDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                        if ("".equals(mRemindDate.getText())){
                            if (gap > 30){
                                Calendar remindDate = calendar;
                                remindDate.add(Calendar.DATE, -30);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindNianjian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mRemindDate.setText("提前30天, 09:00");
                                remindDate.add(Calendar.DATE, 29);
                                mRemindNianjian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else if (gap > 7){
                                Calendar remindDate = calendar;
                                remindDate.add(Calendar.DATE, -7);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindNianjian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mRemindDate.setText("提前七天, 09:00");
                                remindDate.add(Calendar.DATE, 6);
                                mRemindNianjian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else {
                                Calendar remindDate = Calendar.getInstance();
                                remindDate.add(Calendar.DATE, 1);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindNianjian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                gap = DateUtil.getGapCount(remindDate.getTime(), calendar.getTime());
                                mRemindDate.setText(mRemindStrArr[gap] + ", 09:00");
                                if (gap > 2){
                                    remindDate = mMenu.getCalendar();
                                    remindDate.add(Calendar.DATE, -1);
                                    remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                    remindDate.set(Calendar.MINUTE, 0);
                                    mRemindNianjian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                }
                            }
                        }
                        else{
                            Calendar remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindNianjian.getRemindDate1()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindNianjian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindNianjian.getRemindDate2()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindNianjian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
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
                    ToastUtil.makeShortToast(this, "请先设置年检日期");
                    return;
                }
                Calendar date = Calendar.getInstance();
                Calendar remind = Calendar.getInstance();
                try {
                    date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindNianjian.getDate()));
                    remind.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindNianjian.getRemindDate1()));
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
                        mRemindNianjian.setRemindDate1(strDate);
                        if (mMenu.getFirst().getCurrentItem() == 1){
                            mRemindNianjian.setRemindDate2("");
                        }
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
            case R.id.vl_info:
                View layout = LayoutInflater.from(this).inflate(R.layout.popup_image, null, false);
                ImageView img = (ImageView) layout.findViewById(R.id.img);
                img.setImageResource(R.drawable.vl_demo);
                ClickUtils.setNoFastClickListener(layout, this);
                mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
                break;
            case R.id.popup_img:
                mPopupWindow.dismiss();
                break;
        }
    }
}
