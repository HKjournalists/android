package com.kplus.car.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.BaoyangRecord;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class BaoyangRecordActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private LayoutInflater mInflater = null;
    private int mPosition = 0;
    private String mVehicleNum = null;
    private int mRecordPosition = -1;
    private boolean mIsChanged = false;

    private List<BaoyangRecord> mListBaoyangRecord = null;
    private BaoyangRecord mEditRecord = null;

    private Context mContext = null;

    private View mRightView;

    private LinearLayout llRecordCount = null;
    private TextView tvRecordCount = null;

    private LinearLayout llEmpty = null;
    private TextView tvAddRecord = null;

    private LinearLayout llBaoyangRecord = null;

    private PopupWindow mPopupWindow = null;

    @Override
    protected void initView() {
        mContext = this;
        mInflater = LayoutInflater.from(this);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_baoyang_record);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("保养记录");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.daze_but_icons_add);
        mRightView = findViewById(R.id.rightView);

        llRecordCount = (LinearLayout) findViewById(R.id.llRecordCount);
        tvRecordCount = (TextView) findViewById(R.id.tvRecordCount);
        llEmpty = (LinearLayout) findViewById(R.id.llEmpty);
        tvAddRecord = (TextView) findViewById(R.id.tvAddRecord);

        llBaoyangRecord = (LinearLayout) findViewById(R.id.llBaoyangRecord);
    }

    @Override
    protected void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mPosition = bundle.getInt("position");
            mVehicleNum = bundle.getString("vehicleNum");
        }

        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText(mVehicleNum);

        mListBaoyangRecord = mApplication.dbCache.getBaoyangRecord(mVehicleNum);
        updateUI();
    }

    private void updateUI() {
        showRecordView();
        updateRecordView();
    }

    private void showRecordView() {
        if (null == mListBaoyangRecord || mListBaoyangRecord.size() == 0) {
            llRecordCount.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
            llBaoyangRecord.removeAllViews();
            llBaoyangRecord.setVisibility(View.GONE);
        } else {
            llRecordCount.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            llBaoyangRecord.setVisibility(View.VISIBLE);
        }
    }

    private void updateRecordView() {
        llBaoyangRecord.removeAllViews();
        if (mListBaoyangRecord != null && mListBaoyangRecord.size() > 0) {
            int money = 0;
            int size = mListBaoyangRecord.size();
            for (int i = 0; i < size; i++) {
                final int pos = i;
                final BaoyangRecord record = mListBaoyangRecord.get(i);
                money += record.getMoney();
                View v = mInflater.inflate(R.layout.item_baoyang_record, llBaoyangRecord, false);
                TextView tvDate = (TextView) v.findViewById(R.id.date);
                tvDate.setText(record.getDate().replace("-", "/"));
                TextView tvLicheng = (TextView) v.findViewById(R.id.licheng);
                tvLicheng.setText(record.getLicheng() + "公里");
                TextView tvMoney = (TextView) v.findViewById(R.id.money);
                tvMoney.setText(record.getMoney() == 0 ? "" : ("¥" + record.getMoney()));
                TextView tvCompany = (TextView) v.findViewById(R.id.company);
                tvCompany.setText(record.getCompany());
                TextView tvPhone = (TextView) v.findViewById(R.id.phone);
                tvPhone.setText(record.getPhone());
                TextView tvRemark = (TextView) v.findViewById(R.id.remark);
                tvRemark.setText(record.getRemark());

                LinearLayout llBaoyangItem = (LinearLayout) v.findViewById(R.id.llBaoyangItem);
                FlowLayout rlBaoyangItem = (FlowLayout) v.findViewById(R.id.rlBaoyangItem);
                rlBaoyangItem.removeAllViews();

                String baoyangItem = record.getBaoyangItem();
                if (StringUtils.isEmpty(baoyangItem)) {
                    llBaoyangItem.setVisibility(View.GONE);
                } else {
                    // 以,分隔
                    String[] arrItems = baoyangItem.split("\\,");
                    if (arrItems.length == 0) {
                        llBaoyangItem.setVisibility(View.GONE);
                    } else {
                        llBaoyangItem.setVisibility(View.VISIBLE);
                        int k = 0;
                        for (; k < arrItems.length; k++) {
                            String item = arrItems[k];
                            View itemView = mInflater.inflate(R.layout.baoyang_routine_item, rlBaoyangItem, false);
                            TextView tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
                            tvItemName.setText(item);
                            rlBaoyangItem.addView(itemView, k);
                        }
                    }
                }

                // 显示编辑、删除操作
                final ImageView ivMore = (ImageView) v.findViewById(R.id.ivMore);
                ClickUtils.setNoFastClickListener(ivMore, new ClickUtils.NoFastClickListener() {
                    @Override
                    public void onNoFastClick(View v) {
                        mRecordPosition = pos;
                        mEditRecord = record;
                        showPopup(ivMore);
                    }
                });
                llBaoyangRecord.addView(v);
            }
            String msg = "爱车共%1$s次保养，总计花费￥%2$s";
            msg = String.format(msg, size, money);
            tvRecordCount.setText(msg);
        }
    }

    private void showPopup(View anchor) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.popup_baoyang_record_more, null, false);
        TextView tvEdit = (TextView) layout.findViewById(R.id.tvEdit);
        TextView tvDelete = (TextView) layout.findViewById(R.id.tvDelete);

        ClickUtils.setNoFastClickListener(tvEdit, this);
        ClickUtils.setNoFastClickListener(tvDelete, this);
        mPopupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(anchor);
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mRightView, this);
        ClickUtils.setNoFastClickListener(tvAddRecord, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
            case R.id.tvAddRecord:
                EventAnalysisUtil.onEvent(this, "click_addRecords", "点“添加纪录”", null);
                Intent intent = new Intent(this, AddBaoyangRecordActivity.class);
                intent.putExtra("position", -1);
                intent.putExtra("vehicleNum", mVehicleNum);
                startActivityForResult(intent, Constants.REQUEST_TYPE_BAOYANG_RECORD);
                break;
            case R.id.tvEdit:
                EventAnalysisUtil.onEvent(this, "edit_Mainten_Record", "编辑保养纪录", null);
                mPopupWindow.dismiss();
                Intent it = new Intent(mContext, AddBaoyangRecordActivity.class);
                it.putExtra("BaoyangRecord", mEditRecord);
                it.putExtra("position", mRecordPosition);
                startActivityForResult(it, Constants.REQUEST_TYPE_BAOYANG_RECORD);
                break;
            case R.id.tvDelete:
                mPopupWindow.dismiss();
                Intent alertIntent = new Intent(mContext, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "删除保养记录");
                alertIntent.putExtra("message", "确定要删除吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_DELETE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_TYPE_BAOYANG_RECORD:
                if (resultCode == Constants.RESULT_TYPE_CHANGED) {
                    EventAnalysisUtil.onEvent(this, "set_MaintenRecord_success", "设置保养纪录成功", null);
                    int position = data.getIntExtra("position", -1);
                    BaoyangRecord record = (BaoyangRecord) data.getSerializableExtra("BaoyangRecord");
                    if (null == mListBaoyangRecord) {
                        mListBaoyangRecord = new ArrayList<>();
                    }
                    if (position == -1) {
                        mListBaoyangRecord.add(record);
                    } else {
                        mListBaoyangRecord.set(position, record);
                    }
                    mApplication.dbCache.saveBaoyangRecord(mListBaoyangRecord);
                    if (mApplication.getId() != 0) {
                        new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_BAOYANG_RECORD);
                    }
                    updateUI();
                    mIsChanged = true;
                }
                mEditRecord = null;
                mRecordPosition = -1;
                break;
            case Constants.REQUEST_TYPE_DELETE:
                if (resultCode == RESULT_OK) {
                    mListBaoyangRecord.remove(mRecordPosition);
                    mApplication.dbCache.saveBaoyangRecord(mListBaoyangRecord);
                    if (mApplication.getId() != 0) {
                        new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_BAOYANG_RECORD);
                    }
                    updateUI();
                    mIsChanged = true;
                }
                mEditRecord = null;
                mRecordPosition = -1;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsChanged){
            Intent it = new Intent();
            it.putExtra("position", mPosition);
            setResult(Constants.RESULT_TYPE_CHANGED, it);
        }
        super.onBackPressed();
    }
}
