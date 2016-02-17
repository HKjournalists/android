package com.kplus.car.carwash.module.activites;

import android.view.KeyEvent;
import android.view.View;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.module.fragments.CNOrderListFragment;
import com.kplus.car.carwash.widget.CNNavigationBar;

/**
 * Description：订单列表
 * <br/><br/>Created by Fu on 2015/6/1.
 * <br/><br/>
 */
public class CNOrderListActivity extends CNParentActivity {
    private View mView = null;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_orderlist_layout, null);

        CNOrderListFragment orderListFragment = new CNOrderListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rlFragmentCotainer, orderListFragment)
                .commit();
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 我的订单
        navBar.setNavTitle(getStringResources(R.string.cn_my_orders));
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

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
