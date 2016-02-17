package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.adapter.CNServiceAdapter;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNInitializeApiDataUtil;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNServicesUtil;
import com.kplus.car.carwash.widget.CNNavigationBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：服务列表界面
 * <br/><br/>Created by Fu on 2015/5/11.
 * <br/><br/>
 */
public class CNServicesActivity extends CNParentActivity implements OnListItemClickListener {
    private static final String TAG = "CNServicesActivity";

    private View mView = null;
    private RecyclerView rlServiceList = null;
    private CNServiceAdapter mAdapter = null;
    private LinearLayout llWashingEmptyTips = null;

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_services_layout, null);
        rlServiceList = findView(mView, R.id.rlServiceList);
        llWashingEmptyTips = findView(mView, R.id.llWashingEmptyTips);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        rlServiceList.setLayoutManager(llm);

        mAdapter = new CNServiceAdapter(mContext, null, this);
        rlServiceList.setAdapter(mAdapter);

        CNProgressDialogUtil.showProgress(mContext);
        loadingLocData(mApp.mSelectedCity.getId());

        // 获取城市服务列表
        if (null != mApp.mSelectedCity) {
            CNInitializeApiDataUtil.getIns().fetchCityService(mContext, mApp.mSelectedCity.getId());
        }
    }

    @Override
    public void onClickItem(int position, View v) {
        int vId = v.getId();
        if (vId == R.id.btnServiceDetails) { // 查看详情
            OnSiteService service = mAdapter.getItem(position);
            Map<String, String> map = new HashMap<>();
            map.put("service_name", service.getName());
            AppBridgeUtils.getIns().onEvent(mContext, "look_washCar", "洗车项目查看", map);

            Intent intent = new Intent(mContext, CNServiceDetailsActivity.class);
            intent.putExtra(CNCarWashingLogic.KEY_REVIEW_SERVICE_DETAILS, service);
            CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_REVIEW_SERVICE_DETAILS);
        } else if (vId == R.id.rlService) {
            AppBridgeUtils.getIns().onEvent(mContext, "click_next_washCar", "选择服务，下一步");
            // 点击item选择
            OnSiteService siteService = mAdapter.getItem(position);
            setServiceResult(siteService);
        }
    }

    /**
     * 读取本地缓存
     */
    private void loadingLocData(final long cityId) {
        // 从缓存中读取数据
        mApp.getThreadPool().submit(new CNThreadPool.Job<List<OnSiteService>>() {
            @Override
            public List<OnSiteService> run() {
                return CNServicesUtil.getIns().getServices(cityId);
            }
        }, new FutureListener<List<OnSiteService>>() {
            @Override
            public void onFutureDone(Future<List<OnSiteService>> future) {
                setDisplayData(future.get());
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        });
    }

    private void setDisplayData(List<OnSiteService> services) {
        mAdapter.clear();
        if (null != services && services.size() > Const.NONE) {

            rlServiceList.setVisibility(View.VISIBLE);
            llWashingEmptyTips.setVisibility(View.GONE);

            mAdapter.appendData(services);
            mAdapter.notifyDataSetChanged();
        } else {
            rlServiceList.setVisibility(View.GONE);
            llWashingEmptyTips.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 选择服务
        navBar.setNavTitle(getStringResources(R.string.cn_select_service));
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CNViewManager.getIns().pop(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data)
            return;
        // 从详情中点击下单回来要更新选择状态
        if (requestCode == CNCarWashingLogic.TYPE_REVIEW_SERVICE_DETAILS) {
            if (resultCode == Activity.RESULT_OK) {
                OnSiteService service = null;
                Bundle bundle = data.getExtras();
                if (null != bundle) {
                    service = (OnSiteService) bundle.get(CNCarWashingLogic.KEY_REVIEW_SERVICE_DETAILS);
                }
                setServiceResult(service);
            }
        }
    }

    private void setServiceResult(OnSiteService service) {
        ArrayList<OnSiteService> services = new ArrayList<>();
        if (null != service) {
            services.add(service);
        }

        Intent data = new Intent();
        // 选择的服务
        data.putExtra(CNCarWashingLogic.KEY_SERVICE_SELECTED, services);
        setResult(Activity.RESULT_OK, data);
        CNViewManager.getIns().pop(this);
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_INITIALIZE_SERVICES_DATA_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        if (null == intent) {
            return;
        }

        String action = intent.getAction();
        /**
         * 服务的广播
         */
        if (CustomBroadcast.ON_INITIALIZE_SERVICES_DATA_ACTION.equals(action)) {
            boolean result = intent.getBooleanExtra("result", false);
            if (result) {
                loadingLocData(mApp.mSelectedCity.getId());
            }
            CNProgressDialogUtil.dismissProgress(mContext);
        }
    }
}
