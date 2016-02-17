package com.kplus.car.carwash.module.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.callback.CNToolbarStateCallback;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.widget.CNNavigationBar;

/**
 * 可按照项目需求，将无冲突的通用函数，虚化到父类
 * Created by Fu on 2015/5/5.
 */
public abstract class CNParentFragment extends Fragment implements CNToolbarStateCallback {
    private static final String TAG = "CNParentFragment";

    public int mAppWidth = Const.NEGATIVE;
    public int mAppHeight = Const.NEGATIVE;
    public float mScaleDensity = Const.ONE;
    public Point mDeviceSizePoint = null;

    public Context mContext = null;
    protected CNParentActivity mParentActivity = null;

    private CNNavigationBar mNavigationBar = null;

    private NotificationBroadcastReceiver mReceiver = null;
    private NotificationBroadcastReceiver mStickyReceiver = null;

    /**
     * Initiation of the data
     */
    protected abstract void initData();

    /**
     * Initiation of the components
     */
    protected abstract void initView();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        mParentActivity = (CNParentActivity) getActivity();

        // 加到栈中
        CNViewManager.getIns().push(getActivity());

        mDeviceSizePoint = CNPixelsUtil.getDeviceSize(getActivity());

        mAppWidth = mDeviceSizePoint.x;

        int statebarHeight = CNPixelsUtil.dip2px(mContext, 25);
        mAppHeight = mDeviceSizePoint.y - statebarHeight;

        mScaleDensity = getResources().getDisplayMetrics().density;

        register(true);
        initData();
        initView();
        // 是否需要toolbar，加载toolbar
        if (hasToolbar() && isCreaterToolbar()) {
            setToolbarStyle(mNavigationBar);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        register(false);
        // 通用事务，不同处在子类实现
    }

    @Override
    public void onPause() {
        unregister(false);
        // 通用事务，不同处在子类实现
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unregister(true);
        unregister(false);
        // 通用事务，不同处在子类实现
        CNViewManager.getIns().popWithoutFinish(getActivity());
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 通用事务，不同处在子类实现
        super.onConfigurationChanged(newConfig);
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    @Override
    public boolean hasToolbar() {
        return true;
    }

    private boolean isCreaterToolbar() {
        mNavigationBar = mParentActivity.mNavigationBar;
        if (null == mNavigationBar)
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
                getActivity().unregisterReceiver(mStickyReceiver);
                mStickyReceiver = null;
            }
        } else {
            if (null != mReceiver) {
                getActivity().unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        }
    }

    private void addBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        try {
            getActivity().registerReceiver(receiver, filter);
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
