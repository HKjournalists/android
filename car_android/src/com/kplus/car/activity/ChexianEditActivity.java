package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.response.UploadCertImgResponse;
import com.kplus.car.model.response.request.UploadCertImgRequest;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.FileItem;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.SlideUpMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/3/16 0016.
 */
public class ChexianEditActivity extends BaseActivity implements ClickUtils.NoFastClickListener, CompoundButton.OnCheckedChangeListener {
    private RemindChexian mRemindJiaoqiangxian;
    private RemindChexian mRemindShangyexian;
    private TextView mJiaoqiangxianDate;
    private TextView mJiaoqiangxianRemindDate;
    private EditText mJiaoqiangxianBaodanhao;
    private View mJiaoqiangxianPicLayout;
    private View mJiaoqiangxianTakePic;
    private TextView mShangyexianDate;
    private CheckBox mCheckBox;
    private TextView mShangyexianRemindDate;
    private EditText mBaofei;
    private EditText mShangyexianBaodanhao;
    private View mShangyexianPicLayout;
    private View mShangyexianTakePic;
    private TimeMenu mMenu;
    private String[] mRemindStrArr;
    private int mPosition = 0;
    private ImageView mJiaoqiangxianPic;
    private ImageView mShangyexianPic;
    private TextView mTvCompany;
    private boolean mIsEditJiaoqiangxian = false;
    private boolean mIsEditShangyexian = false;
    private SlideUpMenu mSlideUpMenu;
    private DisplayImageOptions optionsPhoto;
    private String mJiaoqiangxianPhotoPath;
    private String mShangyexianPhotoPath;
    private AsyncTask<Void, Void, UploadCertImgResponse> mJiaoqiangxianUploadTask;
    private AsyncTask<Void, Void, UploadCertImgResponse> mShangyexianUploadTask;
    private PopupWindow mPopupWindow;
    private int mType = 1;
    private String mCompany, mPhone, mVehicleNum;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_chexian_edit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("设置车险提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("车险");
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("保存");

        mJiaoqiangxianDate = (TextView) findViewById(R.id.jiaoqiangxian_date);
        mJiaoqiangxianRemindDate = (TextView) findViewById(R.id.jiaoqiangxian_remind_date);
        mJiaoqiangxianBaodanhao = (EditText) findViewById(R.id.jiaoqiangxian_baodanhao);
        mRemindStrArr = getResources().getStringArray(R.array.remind_date);
        mShangyexianDate = (TextView) findViewById(R.id.shangyexian_date);
        mCheckBox = (CheckBox) findViewById(R.id.check_box);
        mShangyexianRemindDate = (TextView) findViewById(R.id.shangyexian_remind_date);
        mBaofei = (EditText) findViewById(R.id.baofei);
        mShangyexianBaodanhao = (EditText) findViewById(R.id.shangyexian_baodanhao);
        mJiaoqiangxianPic = (ImageView) findViewById(R.id.jiaoqiangxian_pic);
        mShangyexianPic = (ImageView) findViewById(R.id.shangyexian_pic);
        mTvCompany = (TextView) findViewById(R.id.company);
        mJiaoqiangxianPicLayout = findViewById(R.id.jiaoqiangxian_pic_layout);
        mJiaoqiangxianTakePic = findViewById(R.id.jiaoqiangxian_takepic);
        mShangyexianPicLayout = findViewById(R.id.shangyexian_pic_layout);
        mShangyexianTakePic = findViewById(R.id.shangyexian_takepic);
    }

    @Override
    protected void loadData() {
        optionsPhoto = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        mVehicleNum = getIntent().getStringExtra("vehicleNum");
        mPosition = getIntent().getIntExtra("position", 0);
        UserVehicle userVehicle = mApplication.dbCache.getVehicle(mVehicleNum);
        mCompany = userVehicle.getCompany();
        mPhone = userVehicle.getPhone();
        mTvCompany.setText(mCompany != null ? mCompany : "");
        List<RemindChexian> listRemindChexian = mApplication.dbCache.getRemindChexian(mVehicleNum);
        if (listRemindChexian != null){
            EventAnalysisUtil.onEvent(this, "edit_InsureNotify", "编辑车险提醒", null);
            mIsEditJiaoqiangxian = true;
            mRemindJiaoqiangxian = listRemindChexian.get(0);
            mJiaoqiangxianDate.setText(mRemindJiaoqiangxian.getOrignalDate().replaceAll("-", "/"));
            try {
                Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindJiaoqiangxian.getRemindDate1());
                Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindJiaoqiangxian.getDate());
                int gap = DateUtil.getGapCount(start, end);
                mJiaoqiangxianRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mRemindJiaoqiangxian.getRemindDate1().substring(mRemindJiaoqiangxian.getRemindDate1().indexOf(' ')));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mJiaoqiangxianBaodanhao.setText(mRemindJiaoqiangxian.getBaodanhao());
            if (!"".equals(mRemindJiaoqiangxian.getPhoto())){
                mJiaoqiangxianPicLayout.setVisibility(View.VISIBLE);
                mJiaoqiangxianTakePic.setVisibility(View.GONE);
                String url = mRemindJiaoqiangxian.getPhoto();
                if (!url.startsWith("http"))
                    url = ImageDownloader.Scheme.FILE.wrap(url);
                mApplication.imageLoader.displayImage(url, mJiaoqiangxianPic, optionsPhoto);
            }
            if (listRemindChexian.size() == 1){
                mRemindShangyexian = new RemindChexian();
                mRemindShangyexian.setVehicleNum(mVehicleNum);
                mRemindShangyexian.setType(2);
            }
            else {
                mIsEditShangyexian = true;
                mRemindShangyexian = listRemindChexian.get(1);
                mShangyexianDate.setText(mRemindShangyexian.getOrignalDate().replaceAll("-", "/"));
                try {
                    Date start = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindShangyexian.getRemindDate1());
                    Date end = new SimpleDateFormat("yyyy-MM-dd").parse(mRemindShangyexian.getDate());
                    int gap = DateUtil.getGapCount(start, end);
                    mShangyexianRemindDate.setText(DateUtil.getRemindLabel(mRemindStrArr, gap) + "," + mRemindShangyexian.getRemindDate1().substring(mRemindShangyexian.getRemindDate1().indexOf(' ')));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mBaofei.setText(mRemindShangyexian.getMoney() == 0 ? "" : String.valueOf(mRemindShangyexian.getMoney()));
                mShangyexianBaodanhao.setText(mRemindShangyexian.getBaodanhao());
                if (mRemindShangyexian.getSame() == 1) {
                    mCheckBox.setChecked(true);
                    mShangyexianDate.setEnabled(false);
                    mShangyexianRemindDate.setEnabled(false);
                }
                if (!"".equals(mRemindShangyexian.getPhoto())){
                    mShangyexianPicLayout.setVisibility(View.VISIBLE);
                    mShangyexianTakePic.setVisibility(View.GONE);
                    String url = mRemindShangyexian.getPhoto();
                    if (!url.startsWith("http"))
                        url = ImageDownloader.Scheme.FILE.wrap(url);
                    mApplication.imageLoader.displayImage(url, mShangyexianPic, optionsPhoto);
                }
            }
        }
        else {
            mRemindJiaoqiangxian = new RemindChexian();
            mRemindJiaoqiangxian.setVehicleNum(mVehicleNum);
            mRemindJiaoqiangxian.setType(1);
            mRemindShangyexian = new RemindChexian();
            mRemindShangyexian.setVehicleNum(mVehicleNum);
            mRemindShangyexian.setType(2);
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mJiaoqiangxianDate, this);
        ClickUtils.setNoFastClickListener(mJiaoqiangxianRemindDate, this);
        ClickUtils.setNoFastClickListener(mShangyexianDate, this);
        ClickUtils.setNoFastClickListener(mShangyexianRemindDate,this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        ClickUtils.setNoFastClickListener(mJiaoqiangxianTakePic , this);
        ClickUtils.setNoFastClickListener(mShangyexianTakePic , this);
        ClickUtils.setNoFastClickListener(mJiaoqiangxianPic, this);
        ClickUtils.setNoFastClickListener(mShangyexianPic, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.jiaoqiangxian_retake), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.shangyexian_retake), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.jiaoqiangxian_delete_pic), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.shangyexian_delete_pic), this);
        ClickUtils.setNoFastClickListener(mTvCompany, this);
        mCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                finish();
                break;
            case R.id.rightView:
                if ("".equals(mJiaoqiangxianDate.getText())){
                    ToastUtil.makeShortToast(this, "请设置交强险办理时间");
                    return;
                }
                Calendar remindDate = Calendar.getInstance();
                try {
                    remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindJiaoqiangxian.getRemindDate1()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (remindDate.before(Calendar.getInstance())){
                    ToastUtil.makeShortToast(this, "提醒时间已过");
                    return;
                }
                ToastUtil.makeShortToast(this, "设置车险提醒成功");
                mRemindJiaoqiangxian.setBaodanhao(mJiaoqiangxianBaodanhao.getText().toString());
                mRemindJiaoqiangxian.setFromType(0);
                if (mIsEditJiaoqiangxian)
                    mApplication.dbCache.updateRemindChexian(mRemindJiaoqiangxian);
                else
                    mApplication.dbCache.saveRemindChexian(mRemindJiaoqiangxian);
                RemindManager.getInstance(this).set(Constants.REQUEST_TYPE_CHEXIAN, mRemindJiaoqiangxian.getVehicleNum(), 1, null);
                if (!"".equals(mRemindShangyexian.getDate())){
                    try {
                        if (StringUtils.isEmpty(mBaofei.getText().toString()))
                            mRemindShangyexian.setMoney(0);
                        else
                            mRemindShangyexian.setMoney(Integer.parseInt(mBaofei.getText().toString()));
                    }catch (Exception e){}
                    mRemindShangyexian.setBaodanhao(mShangyexianBaodanhao.getText().toString());
                    if (mIsEditShangyexian)
                        mApplication.dbCache.updateRemindChexian(mRemindShangyexian);
                    else
                        mApplication.dbCache.saveRemindChexian(mRemindShangyexian);
                    RemindManager.getInstance(this).set(Constants.REQUEST_TYPE_CHEXIAN, mRemindShangyexian.getVehicleNum(), 2, null);
                }
                if (mApplication.getId() != 0)
                    new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_CHEXIAN);
                Intent it = new Intent();
                it.putExtra("position", mPosition);
                setResult(Constants.RESULT_TYPE_CHANGED, it);
                finish();
                break;
            case R.id.jiaoqiangxian_date:
                final Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindJiaoqiangxian.getOrignalDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, calendar, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mMenu.getSlideMenu().hide();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(mMenu.getCalendar().getTime());
                        mJiaoqiangxianDate.setText(date.replaceAll("-", "/"));
                        mRemindJiaoqiangxian.setOrignalDate(date);
                        calendar.setTime(mMenu.getCalendar().getTime());
                        while (calendar.before(Calendar.getInstance()))
                            calendar.add(Calendar.YEAR, 1);
                        Calendar oldCal = Calendar.getInstance();
                        try {
                            oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindJiaoqiangxian.getDate()));
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int gap = DateUtil.getGapCount(oldCal.getTime(), calendar.getTime());
                        mRemindJiaoqiangxian.setDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                        if ("".equals(mJiaoqiangxianRemindDate.getText())){
                            if (gap > 30){
                                Calendar remindDate = calendar;
                                remindDate.add(Calendar.DATE, -30);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindJiaoqiangxian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mJiaoqiangxianRemindDate.setText("提前30天, 09:00");
                                remindDate.add(Calendar.DATE, 29);
                                mRemindJiaoqiangxian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else if (gap > 7){
                                Calendar remindDate = calendar;
                                remindDate.add(Calendar.DATE, -7);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindJiaoqiangxian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mJiaoqiangxianRemindDate.setText("提前七天, 09:00");
                                remindDate.add(Calendar.DATE, 6);
                                mRemindJiaoqiangxian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else {
                                Calendar remindDate = Calendar.getInstance();
                                remindDate.add(Calendar.DATE, 1);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindJiaoqiangxian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                gap = DateUtil.getGapCount(remindDate.getTime(), calendar.getTime());
                                mJiaoqiangxianRemindDate.setText(mRemindStrArr[gap] + ", 09:00");
                                if (gap > 2){
                                    remindDate = calendar;
                                    remindDate.add(Calendar.DATE, -1);
                                    remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                    remindDate.set(Calendar.MINUTE, 0);
                                    mRemindJiaoqiangxian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                }
                            }
                        }
                        else {
                            Calendar remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindJiaoqiangxian.getRemindDate1()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindJiaoqiangxian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindJiaoqiangxian.getRemindDate2()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindJiaoqiangxian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!mShangyexianDate.isEnabled()){
                            mShangyexianDate.setText(mJiaoqiangxianDate.getText());
                            mRemindShangyexian.setOrignalDate(mRemindJiaoqiangxian.getOrignalDate());
                            mRemindShangyexian.setDate(mRemindJiaoqiangxian.getDate());
                            mRemindShangyexian.setRemindDate1(mRemindJiaoqiangxian.getRemindDate1());
                            mRemindShangyexian.setRemindDate2(mRemindJiaoqiangxian.getRemindDate2());
                        }
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
            case R.id.jiaoqiangxian_remind_date:
                if ("".equals(mJiaoqiangxianDate.getText())){
                    ToastUtil.makeShortToast(this, "请先设置办理时间");
                    return;
                }
                Calendar date = Calendar.getInstance();
                Calendar remind = Calendar.getInstance();
                try {
                    date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindJiaoqiangxian.getDate()));
                    remind.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindJiaoqiangxian.getRemindDate1()));
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
                        mJiaoqiangxianRemindDate.setText(mRemindStrArr[mMenu.getFirst().getCurrentItem()] + "," + strDate.substring(strDate.indexOf(' ')));
                        mRemindJiaoqiangxian.setRemindDate1(strDate);
                        if (mMenu.getFirst().getCurrentItem() == 1){
                            mRemindJiaoqiangxian.setRemindDate2("");
                        }
                        if (!mShangyexianDate.isEnabled()){
                            mShangyexianRemindDate.setText(mJiaoqiangxianRemindDate.getText());
                            mRemindShangyexian.setRemindDate1(mRemindJiaoqiangxian.getRemindDate1());
                            mRemindShangyexian.setRemindDate2(mRemindJiaoqiangxian.getRemindDate2());
                        }
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
            case R.id.shangyexian_date:
                final Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindShangyexian.getOrignalDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, cal, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mMenu.getSlideMenu().hide();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(mMenu.getCalendar().getTime());
                        mShangyexianDate.setText(date.replaceAll("-", "/"));
                        mRemindShangyexian.setOrignalDate(date);
                        cal.setTime(mMenu.getCalendar().getTime());
                        while (cal.before(Calendar.getInstance()))
                            cal.add(Calendar.YEAR, 1);
                        Calendar oldCal = Calendar.getInstance();
                        try {
                            oldCal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindShangyexian.getDate()));
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int gap = DateUtil.getGapCount(oldCal.getTime(), cal.getTime());
                        mRemindShangyexian.setDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                        if ("".equals(mShangyexianRemindDate.getText())){
                            if (gap > 30){
                                Calendar remindDate = cal;
                                remindDate.add(Calendar.DATE, -30);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindShangyexian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mShangyexianRemindDate.setText("提前30天, 09:00");
                                remindDate.add(Calendar.DATE, 29);
                                mRemindShangyexian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else if (gap > 7){
                                Calendar remindDate = cal;
                                remindDate.add(Calendar.DATE, -7);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindShangyexian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                mShangyexianRemindDate.setText("提前七天, 09:00");
                                remindDate.add(Calendar.DATE, 6);
                                mRemindShangyexian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            }
                            else {
                                Calendar remindDate = Calendar.getInstance();
                                remindDate.add(Calendar.DATE, 1);
                                remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                remindDate.set(Calendar.MINUTE, 0);
                                mRemindShangyexian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                gap = DateUtil.getGapCount(remindDate.getTime(), cal.getTime());
                                mShangyexianRemindDate.setText(mRemindStrArr[gap] + ", 09:00");
                                if (gap > 2){
                                    remindDate = cal;
                                    remindDate.add(Calendar.DATE, -1);
                                    remindDate.set(Calendar.HOUR_OF_DAY, 9);
                                    remindDate.set(Calendar.MINUTE, 0);
                                    mRemindShangyexian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                                }
                            }
                        }
                        else {
                            Calendar remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindShangyexian.getRemindDate1()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindShangyexian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            remindDate = Calendar.getInstance();
                            try {
                                remindDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindShangyexian.getRemindDate2()));
                                remindDate.add(Calendar.DATE, gap);
                                mRemindShangyexian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
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
            case R.id.shangyexian_remind_date:
                if ("".equals(mShangyexianDate.getText())){
                    ToastUtil.makeShortToast(this, "请先设置办理时间");
                    return;
                }
                date = Calendar.getInstance();
                remind = Calendar.getInstance();
                try {
                    date.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mRemindShangyexian.getDate()));
                    remind.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(mRemindShangyexian.getRemindDate1()));
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
                        mShangyexianRemindDate.setText(mRemindStrArr[mMenu.getFirst().getCurrentItem()] + "," + strDate.substring(strDate.indexOf(' ')));
                        mRemindShangyexian.setRemindDate1(strDate);
                        if (mMenu.getFirst().getCurrentItem() == 1){
                            mRemindShangyexian.setRemindDate2("");
                        }
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
            case R.id.company:
                it = new Intent(this, SelectInsuranceActivity.class);
                it.putExtra("vehicleNum", mVehicleNum);
                it.putExtra("company", mCompany);
                it.putExtra("phone", mPhone);
                startActivityForResult(it, Constants.REQUEST_TYPE_INSURANCE);
                break;
            case R.id.jiaoqiangxian_takepic:
            case R.id.jiaoqiangxian_retake:
                EventAnalysisUtil.onEvent(this, "takePhoto_JQS_Baodan", "点交强险保单拍照", null);
                mType = 1;
                mSlideUpMenu = new SlideUpMenu(this, R.color.daze_translucence2);
                mSlideUpMenu.setContentView(R.layout.menu_photo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case R.id.btAlbum:
                                mSlideUpMenu.hide();
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, Constants.REQUEST_TYPE_GET_PHOTO);
                                break;
                            case R.id.btCamera:
                                mSlideUpMenu.hide();
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                mJiaoqiangxianPhotoPath = FileUtil.getParentDirectory() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +  ".jpg";
                                File file = new File(mJiaoqiangxianPhotoPath);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                startActivityForResult(intent, Constants.REQUEST_TYPE_TAKE_PHOTO);
                                break;
                            case R.id.btCancel:
                                mSlideUpMenu.hide();
                                break;
                        }
                    }
                });
                mSlideUpMenu.show();
                break;
            case R.id.shangyexian_takepic:
            case R.id.shangyexian_retake:
                EventAnalysisUtil.onEvent(this, "takePhoto_SYX", "点商业险保单拍照", null);
                mType = 2;
                mSlideUpMenu = new SlideUpMenu(this, R.color.daze_translucence2);
                mSlideUpMenu.setContentView(R.layout.menu_photo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case R.id.btAlbum:
                                mSlideUpMenu.hide();
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, Constants.REQUEST_TYPE_GET_PHOTO);
                                break;
                            case R.id.btCamera:
                                mSlideUpMenu.hide();
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                mShangyexianPhotoPath = FileUtil.getParentDirectory() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +  ".jpg";
                                File file = new File(mShangyexianPhotoPath);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                startActivityForResult(intent, Constants.REQUEST_TYPE_TAKE_PHOTO);
                                break;
                            case R.id.btCancel:
                                mSlideUpMenu.hide();
                                break;
                        }
                    }
                });
                mSlideUpMenu.show();
                break;
            case R.id.jiaoqiangxian_delete_pic:
                mType = 1;
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "删除保单照片");
                alertIntent.putExtra("message", "确定要删除吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_DELETE);
                break;
            case R.id.shangyexian_delete_pic:
                mType = 2;
                alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "删除保单照片");
                alertIntent.putExtra("message", "确定要删除吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_DELETE);
                break;
            case R.id.jiaoqiangxian_pic:
                mType = 1;
                View layout = LayoutInflater.from(this).inflate(R.layout.popup_image, null, false);
                ImageView img = (ImageView) layout.findViewById(R.id.img);
                String url = mRemindJiaoqiangxian.getPhoto();
                if (!url.startsWith("http"))
                    url = ImageDownloader.Scheme.FILE.wrap(url);
                mApplication.imageLoader.displayImage(url, img, optionsPhoto);
                ClickUtils.setNoFastClickListener(layout, this);
                mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
                break;
            case R.id.shangyexian_pic:
                mType = 2;
                layout = LayoutInflater.from(this).inflate(R.layout.popup_image, null, false);
                img = (ImageView) layout.findViewById(R.id.img);
                url = mRemindShangyexian.getPhoto();
                if (!url.startsWith("http"))
                    url = ImageDownloader.Scheme.FILE.wrap(url);
                mApplication.imageLoader.displayImage(url, img, optionsPhoto);
                ClickUtils.setNoFastClickListener(layout, this);
                mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
                break;
            case R.id.popup_img:
                mPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            EventAnalysisUtil.onEvent(this, "click_Equal_JQX", "点“与交强险一致”", null);
            if ("".equals(mJiaoqiangxianDate.getText())){
                ToastUtil.makeShortToast(ChexianEditActivity.this, "请设置交强险办理时间");
                compoundButton.setChecked(false);
                return;
            }
            mShangyexianDate.setEnabled(false);
            mShangyexianDate.setText(mJiaoqiangxianDate.getText());
            mShangyexianRemindDate.setEnabled(false);
            mShangyexianRemindDate.setText(mJiaoqiangxianRemindDate.getText());
            mRemindShangyexian.setDate(mRemindJiaoqiangxian.getDate());
            mRemindShangyexian.setOrignalDate(mRemindJiaoqiangxian.getOrignalDate());
            mRemindShangyexian.setRemindDate1(mRemindJiaoqiangxian.getRemindDate1());
            mRemindShangyexian.setRemindDate2(mRemindJiaoqiangxian.getRemindDate2());
            mRemindShangyexian.setSame(1);
        }
        else{
            mShangyexianDate.setEnabled(true);
            mShangyexianRemindDate.setEnabled(true);
            mRemindShangyexian.setSame(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constants.REQUEST_TYPE_INSURANCE:
                if (resultCode == Constants.RESULT_TYPE_CHANGED){
                    mCompany = data.getStringExtra("company");
                    mPhone = data.getStringExtra("phone");
                    mTvCompany.setText(mCompany);
                }
                break;
            case Constants.REQUEST_TYPE_TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    if (mType == 1){
                        mJiaoqiangxianPicLayout.setVisibility(View.VISIBLE);
                        mJiaoqiangxianTakePic.setVisibility(View.GONE);
                        Bitmap bmp = BMapUtil.decodeSampledBitmapFromResource(mJiaoqiangxianPhotoPath, 600);
                        bmp = BMapUtil.rotateByExifInfo(bmp, mJiaoqiangxianPhotoPath);
                        mJiaoqiangxianPic.setImageBitmap(bmp);
                        mRemindJiaoqiangxian.setPhoto(mJiaoqiangxianPhotoPath);
                        switchUpload(bmp);
                    }
                    else {
                        mShangyexianPicLayout.setVisibility(View.VISIBLE);
                        mShangyexianTakePic.setVisibility(View.GONE);
                        Bitmap bmp = BMapUtil.decodeSampledBitmapFromResource(mShangyexianPhotoPath, 600);
                        bmp = BMapUtil.rotateByExifInfo(bmp, mShangyexianPhotoPath);
                        mShangyexianPic.setImageBitmap(bmp);
                        mRemindShangyexian.setPhoto(mShangyexianPhotoPath);
                        switchUpload(bmp);
                    }
                }
                break;
            case Constants.REQUEST_TYPE_GET_PHOTO:
                if (resultCode == RESULT_OK){
                    if (mType == 1){
                        mJiaoqiangxianPicLayout.setVisibility(View.VISIBLE);
                        mJiaoqiangxianTakePic.setVisibility(View.GONE);
                        mJiaoqiangxianPhotoPath = BMapUtil.getPhotoPathByLocalUri(this, data);
                        if (mJiaoqiangxianPhotoPath != null) {
                            Bitmap bmp = BMapUtil.decodeSampledBitmapFromResource(mJiaoqiangxianPhotoPath, 600);
                            mJiaoqiangxianPic.setImageBitmap(bmp);
                            mRemindJiaoqiangxian.setPhoto(mJiaoqiangxianPhotoPath);
                            switchUpload(bmp);
                        }
                    }
                    else {
                        mShangyexianPicLayout.setVisibility(View.VISIBLE);
                        mShangyexianTakePic.setVisibility(View.GONE);
                        mShangyexianPhotoPath = BMapUtil.getPhotoPathByLocalUri(this, data);
                        if (mShangyexianPhotoPath != null) {
                            Bitmap bmp = BMapUtil.decodeSampledBitmapFromResource(mShangyexianPhotoPath, 600);
                            mShangyexianPic.setImageBitmap(bmp);
                            mRemindShangyexian.setPhoto(mShangyexianPhotoPath);
                            switchUpload(bmp);
                        }
                    }
                }
                break;
            case Constants.REQUEST_TYPE_DELETE:
                if (resultCode == RESULT_OK){
                    if (mType == 1){
                        if (mJiaoqiangxianUploadTask != null)
                            mJiaoqiangxianUploadTask.cancel(true);
                        mJiaoqiangxianPicLayout.setVisibility(View.GONE);
                        mJiaoqiangxianTakePic.setVisibility(View.VISIBLE);
                        mJiaoqiangxianPic.setImageResource(0);
                        mRemindJiaoqiangxian.setPhoto("");
                    }
                    else {
                        if (mShangyexianUploadTask != null)
                            mShangyexianUploadTask.cancel(true);
                        mShangyexianPicLayout.setVisibility(View.GONE);
                        mShangyexianTakePic.setVisibility(View.VISIBLE);
                        mShangyexianPic.setImageResource(0);
                        mRemindShangyexian.setPhoto("");
                    }
                }
                break;
        }
    }

    private void switchUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里500表示不压缩，把压缩后的数据存放到baos中
        while (baos.toByteArray().length / 1000 > 500) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        byte[] content = baos.toByteArray();
        if(content.length > 0){
            uploadPicture(content);
        }
    }

    private void uploadPicture(final byte[] content){
        if (mType == 1){
            if (mJiaoqiangxianUploadTask != null)
                mJiaoqiangxianUploadTask.cancel(true);
            mJiaoqiangxianUploadTask = new AsyncTask<Void, Void, UploadCertImgResponse>(){
                @Override
                protected UploadCertImgResponse doInBackground(Void... voids) {
                    try{
                        UploadCertImgRequest request = new UploadCertImgRequest();
                        request.setParams(10);
                        HashMap<String, FileItem> fileParams = new HashMap<String, FileItem>();
                        fileParams.put("file", new FileItem("" + System.currentTimeMillis(), content));
                        return mApplication.client.executePostWithParams(request, fileParams);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(UploadCertImgResponse uploadCertImgResponse) {
                    if (uploadCertImgResponse != null && uploadCertImgResponse.getCode() != null && uploadCertImgResponse.getCode() == 0){
                        EventAnalysisUtil.onEvent(ChexianEditActivity.this, "upload_JQSBaodan_success", "上传交强险保单号成功", null);
                        String url = uploadCertImgResponse.getData().getValue();
                        mRemindJiaoqiangxian.setPhoto(url);
                    }
                    else {
                        ToastUtil.makeShortToast(ChexianEditActivity.this, "上传图片失败");
                    }
                }
            };
            mJiaoqiangxianUploadTask.execute();
        }
        else {
            if (mShangyexianUploadTask != null)
                mShangyexianUploadTask.cancel(true);
            mShangyexianUploadTask = new AsyncTask<Void, Void, UploadCertImgResponse>(){
                @Override
                protected UploadCertImgResponse doInBackground(Void... voids) {
                    try{
                        UploadCertImgRequest request = new UploadCertImgRequest();
                        request.setParams(10);
                        HashMap<String, FileItem> fileParams = new HashMap<String, FileItem>();
                        fileParams.put("file", new FileItem("" + System.currentTimeMillis(), content));
                        return mApplication.client.executePostWithParams(request, fileParams);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(UploadCertImgResponse uploadCertImgResponse) {
                    if (uploadCertImgResponse != null && uploadCertImgResponse.getCode() != null && uploadCertImgResponse.getCode() == 0){
                        EventAnalysisUtil.onEvent(ChexianEditActivity.this, "upload_SYX_success", "上传商业险保单号成功", null);
                        String url = uploadCertImgResponse.getData().getValue();
                        mRemindShangyexian.setPhoto(url);
                    }
                    else {
                        ToastUtil.makeShortToast(ChexianEditActivity.this, "上传图片失败");
                    }
                }
            };
            mShangyexianUploadTask.execute();
        }
    }
}
