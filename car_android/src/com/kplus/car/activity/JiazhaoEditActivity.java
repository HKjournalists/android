package com.kplus.car.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.JiazhaoDeleteTask;
import com.kplus.car.asynctask.JiazhaoSaveTask;
import com.kplus.car.asynctask.JiazhaoUpdateSpaceTask;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.JiazhaoSaveResponse;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/5/21.
 */
public class JiazhaoEditActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private Jiazhao mJiazhao;
    private View mBtnOnOff;
    private View mDateLayout;
    private TextView mUpdateTime;
    private EditText mZhenghao;
    private EditText mDanganhao;
    private EditText mName;
    private TextView mDate;
    private TimeMenu mMenu;
    private TextView mTvTitle;
    private TextView mSubmit;
    private TextView mDelete;
    private PopupWindow mPopupWindow;
    private boolean mIsEdit = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_jiazhao_edit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        mBtnOnOff = findViewById(R.id.btn_on_off);
        mDateLayout = findViewById(R.id.date_layout);
        mUpdateTime = (TextView) findViewById(R.id.update_time);
        mZhenghao = (EditText) findViewById(R.id.zhenghao);
        mDanganhao = (EditText) findViewById(R.id.danganhao);
        mName = (EditText) findViewById(R.id.name);
        mDate = (TextView) findViewById(R.id.date);
        mSubmit = (TextView) findViewById(R.id.submit);
        mDelete = (TextView) findViewById(R.id.delete);
    }

    @Override
    protected void loadData() {
        mJiazhao = (Jiazhao) getIntent().getSerializableExtra("jiazhao");
        if (mJiazhao != null){
            EventAnalysisUtil.onEvent(this, "edit_Endorse", "修改查询驾驶证", null);
            mIsEdit = true;
            mTvTitle.setText("修改驾驶证信息");
            mSubmit.setText("保存");
            mDelete.setVisibility(View.VISIBLE);
            mZhenghao.setText(mJiazhao.getJszh());
            mDanganhao.setText(mJiazhao.getDabh());
            if (mJiazhao.getXm() != null && !"".equals(mJiazhao.getXm()))
                mName.setText(mJiazhao.getXm());
            boolean remind = getIntent().getBooleanExtra("remind", false);
            if (remind)
                mJiazhao.setIsHidden("0");
            if ("0".equals(mJiazhao.getIsHidden())) {
                mBtnOnOff.setSelected(true);
                mDateLayout.setVisibility(View.VISIBLE);
                mUpdateTime.setVisibility(View.VISIBLE);
                try {
                    mDate.setText(mJiazhao.getStartTime().replaceAll("-", "/"));
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mJiazhao.getDate()));
                    int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), calendar.getTime());
                    mUpdateTime.setText("下次更新时间" + mJiazhao.getDate().replace("-", "/") + " （剩余" + gap + "天）");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                mBtnOnOff.setSelected(false);
                mDateLayout.setVisibility(View.GONE);
                mUpdateTime.setVisibility(View.GONE);
            }
        }
        else {
            mTvTitle.setText("添加查询驾驶证");
            mSubmit.setText("提交，立即查询");
            boolean showIndex = getIntent().getBooleanExtra("showIndex", false);
            mJiazhao = new Jiazhao();
            mJiazhao.setSpace(showIndex ? 0 : -1);
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mBtnOnOff, this);
        ClickUtils.setNoFastClickListener(mSubmit, this);
        ClickUtils.setNoFastClickListener(mDate, this);
        ClickUtils.setNoFastClickListener(mDelete, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.zhenghao_label), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.danganhao_label), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.dl_info), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.zhenghao_label:
            case R.id.dl_info:
                View layout = LayoutInflater.from(this).inflate(R.layout.popup_image, null, false);
                ImageView img = (ImageView) layout.findViewById(R.id.img);
                img.setImageResource(R.drawable.dl_demo);
                ClickUtils.setNoFastClickListener(layout, this);
                mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
                break;
            case R.id.danganhao_label:
                layout = LayoutInflater.from(this).inflate(R.layout.popup_image, null, false);
                img = (ImageView) layout.findViewById(R.id.img);
                img.setImageResource(R.drawable.dl_demo2);
                ClickUtils.setNoFastClickListener(layout, this);
                mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mPopupWindow.showAtLocation(findViewById(R.id.root), Gravity.LEFT | Gravity.TOP, 0, 0);
                break;
            case R.id.popup_img:
                mPopupWindow.dismiss();
                break;
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.btn_on_off:
                if (mBtnOnOff.isSelected()){
                    mBtnOnOff.setSelected(false);
                    mDateLayout.setVisibility(View.GONE);
                    mUpdateTime.setVisibility(View.GONE);
                    mJiazhao.setIsHidden("1");
                }
                else {
                    mBtnOnOff.setSelected(true);
                    mDateLayout.setVisibility(View.VISIBLE);
                    if (!"".equals(mDate.getText()))
                        mUpdateTime.setVisibility(View.VISIBLE);
                    mJiazhao.setIsHidden("0");
                }
                break;
            case R.id.delete:
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "删除驾驶证");
                alertIntent.putExtra("message", "确定要删除该驾驶证吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_DELETE);
                break;
            case R.id.submit:
                String zhenghao = mZhenghao.getText().toString();
                if ("".equals(zhenghao)){
                    ToastUtil.makeShortToast(this, "请填写驾驶证号码");
                    return;
                }
                else if (zhenghao.length() < 10){
                    ToastUtil.makeShortToast(this, "驾驶证号码错误");
                    return;
                }
                String danganhao = mDanganhao.getText().toString();
                if ("".equals(danganhao)){
                    ToastUtil.makeShortToast(this, "请填写驾驶证档案编号");
                    return;
                }
                else if (danganhao.length() < 10){
                    ToastUtil.makeShortToast(this, "驾驶证档案编号错误");
                    return;
                }
                if (mBtnOnOff.isSelected() && "".equals(mDate.getText())){
                    ToastUtil.makeShortToast(this, "请填写领证日期");
                    return;
                }
                mJiazhao.setJszh(zhenghao);
                mJiazhao.setDabh(danganhao);
                mJiazhao.setXm(mName.getText().toString());
                new JiazhaoSaveTask(mApplication){
                    @Override
                    protected void onPostExecute(JiazhaoSaveResponse response) {
                        if (response != null && response.getCode() == 0){
                            Intent it = new Intent();
                            if (mIsEdit){
                                mApplication.dbCache.updateJiazhao(mJiazhao);
                                if (!"0".equals(mJiazhao.getIsHidden()) && mJiazhao.getRemindDate() != null){
                                    EventAnalysisUtil.onEvent(JiazhaoEditActivity.this, "close_EndorseNotify", "关闭驾照分提醒", null);
                                    RemindManager.getInstance(JiazhaoEditActivity.this).cancel(Constants.REQUEST_TYPE_JIAZHAOFEN, mJiazhao.getId());
                                }
                                Intent intent = new Intent("com.kplus.car.jiazhao.edit");
                                LocalBroadcastManager.getInstance(JiazhaoEditActivity.this).sendBroadcast(intent);
                                setResult(Constants.RESULT_TYPE_CHANGED, it);
                            }
                            else {
                                EventAnalysisUtil.onEvent(JiazhaoEditActivity.this, "addEndorse_success", "成功添加驾驶证", null);
                                mJiazhao.setId(response.getData().getValue());
                                mApplication.dbCache.saveJiazhao(mJiazhao);
                                setResult(Constants.RESULT_TYPE_ADDED, it);
                            }
                            if ("0".equals(mJiazhao.getIsHidden())) {
                                EventAnalysisUtil.onEvent(JiazhaoEditActivity.this, "open_EndorseNotify", "开启驾照分提醒", null);
                                RemindManager.getInstance(JiazhaoEditActivity.this).set(Constants.REQUEST_TYPE_JIAZHAOFEN, mJiazhao.getId());
                            }
                            finish();
                        }
                        else {
                            if (mIsEdit)
                                ToastUtil.makeShortToast(JiazhaoEditActivity.this, "修改驾驶证信息失败");
                            else
                                ToastUtil.makeShortToast(JiazhaoEditActivity.this, "添加驾驶证信息失败");
                        }
                    }
                }.execute(mJiazhao);
                break;
            case R.id.date:
                final Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(mJiazhao.getStartTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, calendar, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mMenu.getSlideMenu().hide();
                        mUpdateTime.setVisibility(View.VISIBLE);
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(mMenu.getCalendar().getTime());
                        mDate.setText(date.replaceAll("-", "/"));
                        mJiazhao.setStartTime(date);
                        calendar.setTime(mMenu.getCalendar().getTime());
                        while (calendar.before(Calendar.getInstance()))
                            calendar.add(Calendar.YEAR, 1);
                        mJiazhao.setDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                        int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), calendar.getTime());
                        if (gap > 7){
                            Calendar remindDate = calendar;
                            remindDate.add(Calendar.DATE, -7);
                            remindDate.set(Calendar.HOUR_OF_DAY, 9);
                            remindDate.set(Calendar.MINUTE, 0);
                            mJiazhao.setRemindDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                        }
                        else {
                            Calendar remindDate = Calendar.getInstance();
                            remindDate.add(Calendar.DATE, 1);
                            remindDate.set(Calendar.HOUR_OF_DAY, 9);
                            remindDate.set(Calendar.MINUTE, 0);
                            mJiazhao.setRemindDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(remindDate.getTime()));
                        }
                        mUpdateTime.setText("下次更新时间" + mJiazhao.getDate().replace("-", "/") + " （剩余" + gap + "天）");
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
        switch (requestCode){
            case Constants.REQUEST_TYPE_DELETE:
                if (resultCode == RESULT_OK){
                    new JiazhaoDeleteTask(mApplication){
                        @Override
                        protected void onPostExecute(GetResultResponse response) {
                            if (response != null && response.getCode() == 0){
                                mApplication.dbCache.deleteJiazhao(mJiazhao.getId());
                                RemindManager.getInstance(JiazhaoEditActivity.this).cancel(Constants.REQUEST_TYPE_JIAZHAOFEN, mJiazhao.getId());
                                if (mJiazhao.getSpace() == 0){
                                    final List<Jiazhao> jiazhaoList = mApplication.dbCache.getJiazhaos();
                                    if (jiazhaoList == null || jiazhaoList.size() == 0){
                                        Intent intent = new Intent("com.kplus.car.jiazhao.edit");
                                        LocalBroadcastManager.getInstance(JiazhaoEditActivity.this).sendBroadcast(intent);
                                        finish();
                                        return;
                                    }
                                    new JiazhaoUpdateSpaceTask(mApplication){
                                        @Override
                                        protected void onPostExecute(GetResultResponse response) {
                                            if (response != null && response.getCode() == 0){
                                                Jiazhao jiazhao = jiazhaoList.get(0);
                                                jiazhao.setSpace(0);
                                                mApplication.dbCache.updateJiazhao(jiazhao);
                                                Intent intent = new Intent("com.kplus.car.jiazhao.edit");
                                                LocalBroadcastManager.getInstance(JiazhaoEditActivity.this).sendBroadcast(intent);
                                                finish();
                                            }
                                        }
                                    }.execute(jiazhaoList.get(0).getId());
                                }
                                else {
                                    Intent intent = new Intent("com.kplus.car.jiazhao.edit");
                                    LocalBroadcastManager.getInstance(JiazhaoEditActivity.this).sendBroadcast(intent);
                                    finish();
                                }
                            }
                            else
                                ToastUtil.makeShortToast(JiazhaoEditActivity.this, "删除驾驶证信息失败");
                        }
                    }.execute(mJiazhao.getId());
                }
                break;
        }
    }
}
