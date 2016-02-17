package com.kplus.car.carwash.module.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.callback.CNToolbarStateCallback;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.widget.CNNavigationBar;

/**
 * 可按照项目需求，将无冲突的通用函数，虚化到父类
 * Created by Fu on 2015/5/5.
 */
public abstract class CNParentActivity extends AppCompatActivity implements CNToolbarStateCallback {
    private static final String TAG = "CNParentActivity";

    public int mAppWidth = Const.NEGATIVE;
    public int mAppHeight = Const.NEGATIVE;
    public float mScaleDensity = Const.ONE;
    public Point mDeviceSizePoint = null;

    protected CNCarWashApp mApp = null;
    public Context mContext = null;
    protected LayoutInflater mInflater = null;
    private InputMethodManager mIMM = null;

    private NotificationBroadcastReceiver mReceiver = null;
    private NotificationBroadcastReceiver mStickyReceiver = null;

    public Toolbar mToolbar = null;
    public LinearLayout mLlParentRoot = null;
    public LinearLayout mLlContent = null;
    public CNNavigationBar mNavigationBar = null;

    /**
     * Initiation of the data
     */
    protected abstract void initData();

    /**
     * Initiation of the components
     */
    protected abstract void initView();

    /**
     * 如果要使用toolbar，或使用默认的toolbar布局，一定要在这里初始化控件
     */
    protected abstract void initToolbarView();

    /**
     * notification broadcast
     *
     * @param context 上下文
     * @param intent  通知内容
     */
    protected void onNotify(Context context, Intent intent) {
    }

    /**
     * init broadcast intent filter
     *
     * @return IntentFilter
     */
    protected IntentFilter getIntentFilter() {
        return null;
    }

    /**
     * 常驻广播通知回调
     *
     * @param context 上下文
     * @param intent  通知内容
     */
    protected void onStickyNotify(Context context, Intent intent) {
    }

    /**
     * 初始化常驻广播过滤器
     *
     * @return IntentFilter
     */
    protected IntentFilter getStickyIntentFilter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mApp = CNCarWashApp.getIns();
        mInflater = LayoutInflater.from(this);
        // 加到栈中
        CNViewManager.getIns().push(this);

        mDeviceSizePoint = CNPixelsUtil.getDeviceSize(this);

        mAppWidth = mDeviceSizePoint.x;

        int statebarHeight = CNPixelsUtil.dip2px(this, 25);
        mAppHeight = mDeviceSizePoint.y - statebarHeight;

        mScaleDensity = getResources().getDisplayMetrics().density;

        mIMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        register(true);
        initData();
        initView();
        // 是否需要toolbar，加载toolbar
        initToolbar();
        initToolbarView();
    }

    private void initToolbar() {
        // 是否需要toolbar，加载toolbar
        if (hasToolbar()) {
            if (isUseDefaultLayoutToolbar()) {
                setContentView(R.layout.cn_toolbar_main);
            }
            if (isCreaterToolbar()) {
                mToolbar = findView(R.id.toolbar);
                mLlParentRoot = findView(R.id.llParentRoot);
                mLlContent = findView(R.id.llContent);
                mNavigationBar = new CNNavigationBar(this, mToolbar);
                mNavigationBar.setMenuitemClickListener(new CNNavigationBar.OnToolbarMenuItemClickListener() {
                    @Override
                    public boolean onViewClick(int viewId) {
                        return onItemSelectedListener(viewId);
                    }
                });
                setToolbarStyle(mNavigationBar);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppBridgeUtils.getIns().agentOnStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppBridgeUtils.getIns().agentOnResume(this);
        register(false);
        // 通用事务，不同处在子类实现
    }

    @Override
    protected void onPause() {
        unregister(false);
        // 通用事务，不同处在子类实现
        super.onPause();
        AppBridgeUtils.getIns().agentOnPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppBridgeUtils.getIns().agentOnStop(this);
    }

    @Override
    protected void onDestroy() {
        unregister(true);
        unregister(false);
        // 通用事务，不同处在子类实现
        CNViewManager.getIns().popWithoutFinish(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 通用事务，不同处在子类实现
        super.onConfigurationChanged(newConfig);
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    protected String getStringResources(int id) {
        return getResources().getString(id);
    }

    protected int getColorResources(int id) {
        return getResources().getColor(id);
    }

    protected void showInput(View view) {
        mIMM.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    protected void hideInput(View view) {
        mIMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected boolean isInputActive() {
        return mIMM.isActive();
    }

    @Override
    public boolean hasToolbar() {
        return true;
    }

    /**
     * 是否使用默认的toolbar布局，主界面不需要
     *
     * @return true 默认的，使用
     */
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    private boolean isCreaterToolbar() {
        mToolbar = findView(R.id.toolbar);
        if (null == mToolbar)
            return false;
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        return false;
    }

    /**********************************************************************/
    /******************************broadcast receiver**********************/
    /**********************************************************************/

    /**
     * 消除常驻广播接收器
     */
    protected void detachStickyBroadcastReceiver() {
        unregister(true);
    }

    /**
     * 添加注册广播
     *
     * @param isSticky 是否常驻的广播
     */
    private void register(boolean isSticky) {
        IntentFilter filter;
        if (isSticky) {
            if (null == mStickyReceiver) {
                filter = getStickyIntentFilter();
                if (null != filter) {
                    mStickyReceiver = new NotificationBroadcastReceiver(true);
                    addBroadcastReceiver(mStickyReceiver, filter);
                }
            }
        } else {
            if (null == mReceiver) {
                filter = getIntentFilter();
                if (null != filter) {
                    mReceiver = new NotificationBroadcastReceiver(false);
                    addBroadcastReceiver(mReceiver, filter);
                }
            }
        }
    }

    /**
     * 注销广播
     *
     * @param isSticky 是否为常驻广播
     */
    private void unregister(boolean isSticky) {
        if (isSticky) {
            if (null != mStickyReceiver) {
                unregisterReceiver(mStickyReceiver);
                mStickyReceiver = null;
            }
        } else {
            if (null != mReceiver) {
                unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        }
    }

    private void addBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        try {
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            Log.trace(TAG, "register exception " + e.getMessage());
        }
    }

    private class NotificationBroadcastReceiver extends BroadcastReceiver {
        private boolean isSticky = false;

        public NotificationBroadcastReceiver(boolean isSticky) {
            this.isSticky = isSticky;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isSticky) {
                onStickyNotify(context, intent);
            } else {
                onNotify(context, intent);
            }
        }
    }
}
