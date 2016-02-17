package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.fragment.AddShangyexianFragment;
import com.kplus.car.fragment.JiaoqiangxianFragment;
import com.kplus.car.fragment.ShangyexianFragment;
import com.kplus.car.model.RemindChexian;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.widget.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.List;

public class ChexianActivity extends BaseActivity implements ClickUtils.NoFastClickListener, ViewPager.OnPageChangeListener {
    private List<RemindChexian> mListRemindChexian;
    private ViewPager mViewPager;
    private View mRightView;
    private TextView mBaoan;
    private TextView mBaodanhaoLabel;
    private TextView mBaodanhao;
    private ImageView mBaodanPic;
    private TextView mTvTitle;
    private PopupWindow mPopupWindow;
    private int mPosition = 0;
    private int mResult = RESULT_CANCELED;
    private String mVehicleNum;
    private DisplayImageOptions optionsPhoto;
    private String mUrl, mCompany, mPhone;
    private int mCurPage = 0;

    private TextView tvDateLeftLabel = null;
    private TextView tvDate = null;

    private LinearLayout llBaoanParent = null;
    private LinearLayout llBaoan = null;
    private ImageView ivBaoanPhoneIcon = null;
    private TextView tvBaoanPhone = null;
    private TextView tvBaoanHint = null;

    private LinearLayout llBaofei = null;
    private TextView tvBaofei = null;
    private View viewBaofeiBottomLine = null;

    private RelativeLayout rlBaodan = null;
    private View viewBaodanLine = null;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_chexian);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        ImageView ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.moreope);
        mRightView = findViewById(R.id.rightView);
        mBaoan = (TextView) findViewById(R.id.baoan);
        mBaodanhaoLabel = (TextView) findViewById(R.id.baodanhao_label);
        mBaodanhao = (TextView) findViewById(R.id.baodanhao);
        mBaodanPic = (ImageView) findViewById(R.id.baodan_pic);

        tvDateLeftLabel = (TextView) findViewById(R.id.tvDateLeftLabel);
        tvDate = (TextView) findViewById(R.id.tvDate);

        llBaoanParent = (LinearLayout) findViewById(R.id.llBaoanParent);
        llBaoan = (LinearLayout) findViewById(R.id.llBaoan);
        ivBaoanPhoneIcon = (ImageView) findViewById(R.id.ivBaoanPhoneIcon);
        tvBaoanPhone = (TextView) findViewById(R.id.tvBaoanPhone);
        tvBaoanHint = (TextView) findViewById(R.id.baoan_hint);

        llBaofei = (LinearLayout) findViewById(R.id.llBaofei);
        tvBaofei = (TextView) findViewById(R.id.tvBaofei);
        viewBaofeiBottomLine = findViewById(R.id.viewBaofeiBottomLine);

        rlBaodan = (RelativeLayout) findViewById(R.id.rlBaodan);
        viewBaodanLine = findViewById(R.id.viewBaodanLine);
    }

    @Override
    protected void loadData() {
        EventAnalysisUtil.onEvent(this, "goto_CarinsureDetail", "进入车险提醒详情", null);
        optionsPhoto = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        mPosition = getIntent().getIntExtra("position", 0);
        mVehicleNum = getIntent().getStringExtra("vehicleNum");
        int type = getIntent().getIntExtra("type", 0);
        mCompany = getIntent().getStringExtra("company");
        mPhone = getIntent().getStringExtra("phone");
        mListRemindChexian = mApplication.dbCache.getRemindChexian(mVehicleNum);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText(mListRemindChexian.get(0).getVehicleNum());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ChexianAdapter(getSupportFragmentManager()));
        CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setViewPager(mViewPager);
        pageIndicator.setOnPageChangeListener(this);
        if (mListRemindChexian.size() == 2 && type == 1)
            updateUI(1);
        else
            updateUI(0);
        LocalBroadcastManager.getInstance(this).registerReceiver(insuranceReceiver, new IntentFilter("com.kplus.car.set_insurance"));
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mRightView, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.insurance), this);
        ClickUtils.setNoFastClickListener(mBaoan, this);
        ClickUtils.setNoFastClickListener(mBaodanPic, this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(insuranceReceiver);
        super.onDestroy();
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
                Intent it = new Intent(this, ChexianEditActivity.class);
                it.putExtra("vehicleNum", mVehicleNum);
                startActivityForResult(it, Constants.REQUEST_TYPE_CHEXIAN);
                break;
            case R.id.close:
                EventAnalysisUtil.onEvent(this, "close_InsureNotify", "关闭车险提醒", null);
                mPopupWindow.dismiss();
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "关闭车险提醒");
                alertIntent.putExtra("message", "确定要关闭车险提醒吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_CLOSE);
                break;
            case R.id.insurance:
                EventAnalysisUtil.onEvent(this, "click_InsureCompanys", "点保险公司大全", null);
                it = new Intent(this, InsuranceListActivity.class);
                startActivity(it);
                break;
            case R.id.baoan:
                if ("立即选择".equals(mBaoan.getText())) {
                    it = new Intent(this, SelectInsuranceActivity.class);
                    it.putExtra("vehicleNum", mVehicleNum);
                    it.putExtra("company", mCompany);
                    it.putExtra("phone", mPhone);
                    startActivityForResult(it, Constants.REQUEST_TYPE_INSURANCE);
                } else {
                    it = new Intent();
                    it.setAction("android.intent.action.CALL");
                    it.setData(Uri.parse("tel:" + mPhone));
                    startActivity(it);
                }
                break;
            case R.id.baodan_pic:
                View layout = LayoutInflater.from(this).inflate(R.layout.popup_image, null, false);
                ImageView img = (ImageView) layout.findViewById(R.id.img);
                mApplication.imageLoader.displayImage(mUrl, img, optionsPhoto);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurPage = position;
        llBaoanParent.setVisibility(View.GONE);
        llBaoan.setVisibility(View.GONE);
        tvBaoanHint.setVisibility(View.GONE);
        mBaoan.setVisibility(View.GONE);
        tvBaoanPhone.setVisibility(View.GONE);
        mBaodanhaoLabel.setVisibility(View.GONE);
        mBaodanhao.setVisibility(View.GONE);
        mBaodanPic.setVisibility(View.GONE);
        if (position == 1 && mListRemindChexian.size() == 1) {
            mTvTitle.setText("商业险提醒");
        } else if (position == 0 && mListRemindChexian.size() == 2) {
            mTvTitle.setText("商业险提醒");
            mBaoan.setVisibility(View.VISIBLE);
            llBaoan.setVisibility(View.VISIBLE);
            llBaoanParent.setVisibility(View.VISIBLE);
            RemindChexian shangyexian = mListRemindChexian.get(1);

            tvDateLeftLabel.setText("商业险到期时间");
            tvDate.setText(shangyexian.getDate().replaceAll("-", "/"));

            if (StringUtils.isEmpty(mPhone)) {
                tvBaoanHint.setVisibility(View.VISIBLE);
                mBaoan.setText("立即选择");
                llBaoan.setBackgroundResource(R.drawable.stroke_corner_orange);
                tvBaoanPhone.setVisibility(View.GONE);
                ivBaoanPhoneIcon.setVisibility(View.GONE);
                mBaoan.setTextColor(getResources().getColor(R.color.daze_orangered5));
            } else {
                tvBaoanHint.setVisibility(View.GONE);
                mBaoan.setText("一键报险");
                llBaoan.setBackgroundResource(R.drawable.stroke_fill_orange);
                tvBaoanPhone.setVisibility(View.VISIBLE);
                tvBaoanPhone.setText(mCompany + " (" + mPhone + ")");
                ivBaoanPhoneIcon.setVisibility(View.VISIBLE);
                mBaoan.setTextColor(getResources().getColor(R.color.daze_white));
            }

            viewBaofeiBottomLine.setVisibility(View.VISIBLE);
            int money = shangyexian.getMoney();
            if (money > 0) {
                llBaofei.setVisibility(View.VISIBLE);
                tvBaofei.setText("￥" + String.valueOf(money));
            } else {
                llBaofei.setVisibility(View.GONE);
            }

            String baodanhao = shangyexian.getBaodanhao();
            if (!StringUtils.isEmpty(baodanhao)) {
                mBaodanhaoLabel.setVisibility(View.VISIBLE);
                rlBaodan.setVisibility(View.VISIBLE);
                mBaodanhao.setVisibility(View.VISIBLE);
                viewBaodanLine.setVisibility(View.VISIBLE);

                mBaodanhaoLabel.setText("商业险保单号");
                mBaodanhao.setText(baodanhao);
            } else {
                mBaodanhao.setVisibility(View.GONE);
                rlBaodan.setVisibility(View.GONE);
                viewBaodanLine.setVisibility(View.GONE);
            }

            if (!"".equals(shangyexian.getPhoto())) {
                mBaodanhaoLabel.setVisibility(View.VISIBLE);
                rlBaodan.setVisibility(View.VISIBLE);
                viewBaodanLine.setVisibility(View.VISIBLE);
                mBaodanPic.setVisibility(View.VISIBLE);

                mBaodanhaoLabel.setText("商业险保单号");

                mUrl = shangyexian.getPhoto();
                if (!mUrl.startsWith("http"))
                    mUrl = ImageDownloader.Scheme.FILE.wrap(mUrl);
                mApplication.imageLoader.displayImage(mUrl, mBaodanPic, optionsPhoto);
            }
        } else {
            mTvTitle.setText("交强险提醒");
            RemindChexian jiaoqiangxian = mListRemindChexian.get(0);

            tvDateLeftLabel.setText("交强险到期时间");
            tvDate.setText(jiaoqiangxian.getDate().replaceAll("-", "/"));

            viewBaofeiBottomLine.setVisibility(View.VISIBLE);
            int money = jiaoqiangxian.getMoney();
            if (money > 0) {
                llBaofei.setVisibility(View.VISIBLE);
                tvBaofei.setText("￥" + String.valueOf(money));
            } else {
                llBaofei.setVisibility(View.INVISIBLE);
            }

            String baodanhao = jiaoqiangxian.getBaodanhao();
            if (!StringUtils.isEmpty(baodanhao)) {
                mBaodanhaoLabel.setVisibility(View.VISIBLE);
                rlBaodan.setVisibility(View.VISIBLE);
                mBaodanhao.setVisibility(View.VISIBLE);
                viewBaodanLine.setVisibility(View.VISIBLE);

                mBaodanhaoLabel.setText("交强险保单号");
                mBaodanhao.setText(baodanhao);
            } else {
                mBaodanhao.setVisibility(View.GONE);
                rlBaodan.setVisibility(View.GONE);
                viewBaodanLine.setVisibility(View.GONE);
            }

            if (!"".equals(jiaoqiangxian.getPhoto())) {
                mBaodanhaoLabel.setVisibility(View.VISIBLE);
                rlBaodan.setVisibility(View.VISIBLE);
                viewBaodanLine.setVisibility(View.VISIBLE);
                mBaodanPic.setVisibility(View.VISIBLE);

                mBaodanhaoLabel.setText("交强险保单号");

                mUrl = jiaoqiangxian.getPhoto();
                if (!mUrl.startsWith("http"))
                    mUrl = ImageDownloader.Scheme.FILE.wrap(mUrl);
                mApplication.imageLoader.displayImage(mUrl, mBaodanPic, optionsPhoto);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class ChexianAdapter extends FragmentStatePagerAdapter {

        public ChexianAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mListRemindChexian.size() == 1) {
                if (position == 0)
                    return JiaoqiangxianFragment.newInstance(mListRemindChexian.get(0));
                else
                    return AddShangyexianFragment.newInstance(mVehicleNum);
            } else {
                if (position == 0)
                    return ShangyexianFragment.newInstance(mListRemindChexian.get(1));
                else
                    return JiaoqiangxianFragment.newInstance(mListRemindChexian.get(0));
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
        if (requestCode == Constants.REQUEST_TYPE_CHEXIAN && resultCode == Constants.RESULT_TYPE_CHANGED) {
            mResult = resultCode;
            mListRemindChexian = mApplication.dbCache.getRemindChexian(mVehicleNum);
            updateUI(0);
        } else if (requestCode == Constants.REQUEST_TYPE_CLOSE && resultCode == RESULT_OK) {
            Intent it = new Intent();
            it.putExtra("position", mPosition);
            setResult(Constants.RESULT_TYPE_REMOVED, it);
            finish();
        }
    }

    private void updateUI(int page) {
        mViewPager.setAdapter(new ChexianAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(page);
        onPageSelected(page);
    }

    private BroadcastReceiver insuranceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UserVehicle userVehicle = (UserVehicle) intent.getSerializableExtra("vehicle");
            if (userVehicle.getVehicleNum().equals(mVehicleNum)) {
                mCompany = userVehicle.getCompany();
                mPhone = userVehicle.getPhone();
                updateUI(mCurPage);
            }
        }
    };
}
