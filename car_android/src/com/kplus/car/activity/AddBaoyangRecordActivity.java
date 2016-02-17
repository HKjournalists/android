package com.kplus.car.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.model.BaoyangRecord;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.FlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/3/25 0025.
 */
public class AddBaoyangRecordActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private LayoutInflater mInflater = null;
    private ArrayList<String> mSelectedBaoyangItemIds = null;
    private ArrayList<String> mSelectedBaoyangItems = null;

    private BaoyangRecord mBaoyangRecord;
    private TextView mDate;
    private TimeMenu mDateMenu;
    private EditText mLicheng;
    private EditText mMoney;
    private TextView mCompany;
    private EditText mPhone;
    private EditText mRemark;
    private int mPosition = 0;

    private RelativeLayout rlBaoyangEmpty = null;
    private FlowLayout rlBaoyangItem = null;

    @Override
    protected void initView() {
        mInflater = LayoutInflater.from(this);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_add_baoyang_record);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("添加保养记录");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("保养");
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("保存");

        mDate = (TextView) findViewById(R.id.date);
        mLicheng = (EditText) findViewById(R.id.licheng);
        mMoney = (EditText) findViewById(R.id.money);
        mCompany = (TextView) findViewById(R.id.company);
        mPhone = (EditText) findViewById(R.id.phone);
        mRemark = (EditText) findViewById(R.id.remark);

        rlBaoyangEmpty = (RelativeLayout) findViewById(R.id.rlBaoyangEmpty);
        rlBaoyangItem = (FlowLayout) findViewById(R.id.rlBaoyangItem);
    }

    @Override
    protected void loadData() {
        mSelectedBaoyangItemIds = new ArrayList<>();
        mSelectedBaoyangItems = new ArrayList<>();
        rlBaoyangItem.removeAllViews();
        mPosition = getIntent().getIntExtra("position", 0);
        mBaoyangRecord = (BaoyangRecord) getIntent().getSerializableExtra("BaoyangRecord");
        if (mBaoyangRecord != null) {
            mDate.setText(mBaoyangRecord.getDate().replaceAll("-", "/"));
            mLicheng.setText(String.valueOf(mBaoyangRecord.getLicheng()));
            mMoney.setText(mBaoyangRecord.getMoney() == 0 ? "" : String.valueOf(mBaoyangRecord.getMoney()));
            mCompany.setText(mBaoyangRecord.getCompany());
            mPhone.setText(mBaoyangRecord.getPhone());
            mRemark.setText(mBaoyangRecord.getRemark());

            String baoyangItem = mBaoyangRecord.getBaoyangItem();
            String baoyangItemId = mBaoyangRecord.getBaoyangItemId();
            if (StringUtils.isEmpty(baoyangItem) || StringUtils.isEmpty(baoyangItemId)) {
                rlBaoyangEmpty.setVisibility(View.VISIBLE);
                rlBaoyangItem.setVisibility(View.GONE);
            } else {
                // 以,分隔
                String[] arrItems = baoyangItem.split("\\,");
                String[] arrItemIds = baoyangItemId.split("\\,");
                if (arrItems.length == 0 || arrItemIds.length == 0) {
                    rlBaoyangEmpty.setVisibility(View.VISIBLE);
                    rlBaoyangItem.setVisibility(View.GONE);
                } else {
                    rlBaoyangEmpty.setVisibility(View.GONE);
                    rlBaoyangItem.setVisibility(View.VISIBLE);
                    int k = 0;
                    for (; k < arrItems.length; k++) {
                        String item = arrItems[k];
                        String itemId = arrItemIds[k];
                        addBaoyangItemToList(itemId, item);

                        View itemView = mInflater.inflate(R.layout.baoyang_routine_item, rlBaoyangItem, false);
                        TextView tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
                        tvItemName.setText(item);
                        rlBaoyangItem.addView(itemView, k);
                    }
                }
            }
        } else {
            String vehicleNum = getIntent().getStringExtra("vehicleNum");
            mBaoyangRecord = new BaoyangRecord(vehicleNum);
            rlBaoyangEmpty.setVisibility(View.VISIBLE);
            rlBaoyangItem.setVisibility(View.GONE);
        }
    }

    private void setBaoyangItemValue() {
        if (null == mSelectedBaoyangItems || mSelectedBaoyangItems.size() == 0) {
            rlBaoyangEmpty.setVisibility(View.VISIBLE);
            rlBaoyangItem.setVisibility(View.GONE);
        } else {
            rlBaoyangEmpty.setVisibility(View.GONE);
            rlBaoyangItem.setVisibility(View.VISIBLE);
            rlBaoyangItem.removeAllViews();
            for (int i = 0; i < mSelectedBaoyangItems.size(); i++) {
                String item = mSelectedBaoyangItems.get(i);
                View itemView = mInflater.inflate(R.layout.baoyang_routine_item, rlBaoyangItem, false);
                TextView tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
                tvItemName.setText(item);
                rlBaoyangItem.addView(itemView, i);
            }
        }
    }

    private void addBaoyangItemToList(String id, String item) {
        if (StringUtils.isEmpty(item)) {
            return;
        }
        mSelectedBaoyangItemIds.add(id);
        mSelectedBaoyangItems.add(item);
    }

    private String getBaoyangItemToString() {
        StringBuilder buffer = new StringBuilder();
        for (String item : mSelectedBaoyangItems) {
            buffer.append(item).append(",");
        }
        String strItems = buffer.toString();
        if (strItems.length() > 0) {
            strItems = strItems.substring(0, strItems.length() - 1);
        }
        return strItems;
    }

    private String getBaoyangItemIdsToString() {
        StringBuilder buffer = new StringBuilder();
        for (String item : mSelectedBaoyangItemIds) {
            buffer.append(item).append(",");
        }
        String strItems = buffer.toString();
        if (strItems.length() > 0) {
            strItems = strItems.substring(0, strItems.length() - 1);
        }
        return strItems;
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(mDate, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        ClickUtils.setNoFastClickListener(mCompany, this);
        ClickUtils.setNoFastClickListener(rlBaoyangEmpty, this);
        ClickUtils.setNoFastClickListener(rlBaoyangItem, this);
    }


    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                final Calendar calendar = Calendar.getInstance();
                mDateMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, calendar, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mDateMenu.getSlideMenu().hide();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(mDateMenu.getCalendar().getTime());
                        mDate.setText(date.replaceAll("-", "/"));
                        mBaoyangRecord.setDate(date);
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mDateMenu.getSlideMenu().hide();
                    }
                });
                mDateMenu.getSlideMenu().show();
                break;
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.company:
                Intent intent = new Intent(this, BaoyangLocationActivity.class);
                intent.putExtra("name", mBaoyangRecord.getCompany());
                intent.putExtra("address", mBaoyangRecord.getAddress());
                intent.putExtra("phone", mBaoyangRecord.getPhone());
                intent.putExtra("lat", mBaoyangRecord.getLat());
                intent.putExtra("lon", mBaoyangRecord.getLon());
                startActivityForResult(intent, Constants.REQUEST_TYPE_BAOYANG_LOCATION);
                break;
            case R.id.rlBaoyangEmpty: // 点击保养项目
            case R.id.rlBaoyangItem:
                intent = new Intent(this, BaoyangItemActivity.class);
                // 要将已选择的保养项目传过去
                intent.putExtra(BaoyangItemActivity.KEY_ITEM_IDS_VALUE, mSelectedBaoyangItemIds);
                intent.putExtra(BaoyangItemActivity.KEY_ITEM_VALUE, mSelectedBaoyangItems);
                startActivityForResult(intent, Constants.REQUEST_TYPE_BAOYANG_ITEM);
                break;
            case R.id.rightView:
                if ("".equals(mDate.getText())) {
                    ToastUtil.makeShortToast(this, "请设置保养时间");
                    return;
                }
                int licheng = 0;
                try {
                    licheng = Integer.parseInt(mLicheng.getText().toString());
                    mBaoyangRecord.setLicheng(licheng);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (licheng <= 0) {
                    ToastUtil.makeShortToast(this, "请设置保养里程");
                    return;
                }
                try {
                    String money = mMoney.getText().toString();
                    if ("".equals(money))
                        mBaoyangRecord.setMoney(0);
                    else
                        mBaoyangRecord.setMoney(Integer.parseInt(money));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBaoyangRecord.setPhone(mPhone.getText().toString());
                mBaoyangRecord.setRemark(mRemark.getText().toString());

                mBaoyangRecord.setBaoyangItemId(getBaoyangItemIdsToString());
                mBaoyangRecord.setBaoyangItem(getBaoyangItemToString());

                Intent it = new Intent();
                it.putExtra("position", mPosition);
                it.putExtra("BaoyangRecord", mBaoyangRecord);
                setResult(Constants.RESULT_TYPE_CHANGED, it);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_TYPE_BAOYANG_ITEM: // 选择保养项目回来
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        mSelectedBaoyangItemIds = bundle.getStringArrayList(BaoyangItemActivity.KEY_ITEM_IDS_VALUE);
                        mSelectedBaoyangItems = bundle.getStringArrayList(BaoyangItemActivity.KEY_ITEM_VALUE);
                        setBaoyangItemValue();
                    }
                }
                break;
            case Constants.REQUEST_TYPE_BAOYANG_LOCATION:
                if (resultCode == Constants.RESULT_TYPE_CHANGED){
                    String name = data.getStringExtra("name");
                    mCompany.setText(name);
                    mBaoyangRecord.setCompany(name);
                    String address = data.getStringExtra("address");
                    mBaoyangRecord.setAddress(address);
                    String phone = data.getStringExtra("phone");
                    mBaoyangRecord.setPhone(phone);
                    if (!StringUtils.isEmpty(phone))
                        mPhone.setText(phone);
                    else
                        mPhone.setText("");
                    String lat = data.getStringExtra("lat");
                    mBaoyangRecord.setLat(lat);
                    String lon = data.getStringExtra("lon");
                    mBaoyangRecord.setLon(lon);
                }
                break;
        }
    }
}
