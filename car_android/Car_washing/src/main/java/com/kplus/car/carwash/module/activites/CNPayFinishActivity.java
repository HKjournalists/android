package com.kplus.car.carwash.module.activites;

import android.view.View;
import android.widget.RelativeLayout;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.widget.CNNavigationBar;

/**
 * Description：支付完成界面
 * <br/><br/>Created by Fu on 2015/5/20.
 * <br/><br/>
 */
public class CNPayFinishActivity extends CNParentActivity implements CNViewClickUtil.NoFastClickListener {
    private View mView = null;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_pay_finish_layout, null);

        RelativeLayout rlViewMyorders = findView(mView, R.id.rlViewMyorders);
        CNViewClickUtil.setNoFastClickListener(rlViewMyorders, this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void onNoFastClick(View v) {
        if (v.getId() == R.id.rlViewMyorders) {
            CNViewManager.getIns().showActivity(mContext, CNOrderListActivity.class, true);
        }
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 支付完成
        navBar.setNavTitle(getStringResources(R.string.cn_pay_finish));
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft) {
            CNViewManager.getIns().pop(this);
        }
        return true;
    }
}
