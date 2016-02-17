package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.widget.CNNavigationBar;
import com.kplus.car.carwash.widget.CNSerivePicView;
import com.kplus.car.carwash.widget.CNWebview;

import java.math.BigDecimal;

/**
 * 服务详情界面
 * Created by Fu on 2015/5/12.
 */
public class CNServiceDetailsActivity extends CNParentActivity implements CNViewClickUtil.NoFastClickListener, CNWebview.IWebViewCallbackListener {

    private static final String TAG = "CNServiceDetailsActivity";

    private View mView = null;

    private CNWebview wvWebView = null;

    private OnSiteService mSiteService = null;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mSiteService = (OnSiteService) bundle.get(CNCarWashingLogic.KEY_REVIEW_SERVICE_DETAILS);
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_service_details_layout, null);

        TextView tvOrderPrice = findView(mView, R.id.tvOrderPrice);
        TextView tvOrgOrderPrice = findView(mView, R.id.tvOrgOrderPrice);
        Button btnOrders = findView(mView, R.id.btnOrders);
        CNSerivePicView serivePicView = findView(mView, R.id.serivePicView);
        LinearLayout llTextDesc = findView(mView, R.id.llTextDesc);
//        TextView tvServiceTitle = findView(mView, R.id.tvServiceTitle);
//        TextView tvServiceContent = findView(mView, R.id.tvServiceContent);

        // 现在文字隐藏
        llTextDesc.setVisibility(View.GONE);

        wvWebView = findView(mView, R.id.wvWebView);
        ScrollView svScroll = findView(mView, R.id.svScroll);

        wvWebView.setWebViewCallbackListener(this);

        String value;
        if (null != mSiteService.getPrice()) {
            double price = mSiteService.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            value = String.format(getStringResources(R.string.cn_order_price), price);
            tvOrderPrice.setText(value);
        }
        if (null != mSiteService.getPrice()) {
            double marketPrice = mSiteService.getMarketPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            value = String.format(getStringResources(R.string.cn_org_order_price), marketPrice);
            tvOrgOrderPrice.setText(value);
            // 设置原价为删除线
            tvOrgOrderPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        CNProgressDialogUtil.showProgress(mContext);

        String serviceUrl = mSiteService.getUrl();
        Log.trace(TAG, "服务详情的serviceUrl-->" + serviceUrl);
        if (CNStringUtil.isNotEmpty(serviceUrl)) {
            svScroll.setVisibility(View.GONE);
            wvWebView.setVisibility(View.VISIBLE);
            // Notice 加载url
            String url = CNStringUtil.getHttpUrl(serviceUrl);
            url += "#client";
            Log.trace(TAG, "服务详情的url-->" + url);
            wvWebView.loadUrl(url);
        } else {
            svScroll.setVisibility(View.VISIBLE);
            wvWebView.setVisibility(View.GONE);

            Log.trace(TAG, "服务详情还没有url！");
            // 初始化显示图片
            serivePicView.initView(mSiteService.getPictures());
        }

        CNViewClickUtil.setNoFastClickListener(btnOrders, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);

        // 如果没有url就不显示分享按钮
        if (CNStringUtil.isEmpty(mSiteService.getUrl())) {
            mNavigationBar.setRightBtnVisibility(View.INVISIBLE);
        } else {
            mNavigationBar.setRightBtnVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        navBar.setNavTitle(mSiteService.getName());
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
        // 分享按钮
        navBar.setRightBtn("", R.drawable.btn_share_selector, Const.NONE);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft) {
            CNViewManager.getIns().pop(this);
        } else if (viewId == R.id.tvNavRight) {
            // 分享服务
            Intent data = new Intent(mContext, CNShareActivity.class);
            data.putExtra(CNCarWashingLogic.KEY_SHARE_TYPE, CNCarWashingLogic.SHARE_TYPE_SERVICES);
            data.putExtra(CNCarWashingLogic.KEY_SHARE_CONTENT, mSiteService);
            startActivity(data);
        }
        return true;
    }

    @Override
    public void onNoFastClick(View v) {
        if (v.getId() == R.id.btnOrders) {
            if (null != mSiteService) {
                AppBridgeUtils.getIns().onEvent(mContext, "click_imediaorder_serviceDetail", "服务详情--立即下单");

                mSiteService.setIsChecked(true);
                Intent data = new Intent();
                data.putExtra(CNCarWashingLogic.KEY_REVIEW_SERVICE_DETAILS, mSiteService);
                setResult(Activity.RESULT_OK, data);
                CNViewManager.getIns().pop(this);
            }
        }
    }

    @Override
    public void onCanGoBack(boolean isGoBack) {

    }

    @Override
    public void onCanGoForward(boolean isGoForward) {

    }

    @Override
    public void onProgress(int newProgress) {
    }

    @Override
    public void onWebTitle(String title) {
    }

    @Override
    public void setOnLoad() {
    }

    @Override
    public void setFinishLoad() {
        CNProgressDialogUtil.dismissProgress(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPauseOrResume(false);
    }

    @Override
    protected void onPause() {
        CNProgressDialogUtil.dismissProgress(mContext);
        onPauseOrResume(true);
//        wvWebView.reload();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvWebView = null;
    }

    private void onPauseOrResume(boolean isPause) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (isPause) {
                wvWebView.onPause(); // 暂停网页中正在播放的视频
            } else {
                wvWebView.onResume();
            }
        }
        if (isPause) {
            wvWebView.onWebPause();
        } else {
            wvWebView.onWebResume();
        }
    }
}
