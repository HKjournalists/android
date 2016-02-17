package com.kplus.car.carwash.module.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.CancelOrderResp;
import com.kplus.car.carwash.bean.FetchOrderLogsResp;
import com.kplus.car.carwash.bean.FetchOrderResp;
import com.kplus.car.carwash.bean.FetchPaginationOrderResp;
import com.kplus.car.carwash.bean.OrderLog;
import com.kplus.car.carwash.bean.ServiceOrder;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.manager.DialogManager;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.OrderStatus;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.module.activites.CNEvaluateServiceActivity;
import com.kplus.car.carwash.module.activites.CNOrderDetailsActivity;
import com.kplus.car.carwash.module.adapter.CNOrderListAdapter;
import com.kplus.car.carwash.utils.CNOrderListUtil;
import com.kplus.car.carwash.utils.CNOrderLogUtil;
import com.kplus.car.carwash.utils.CNPayResultUtil;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.DateUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.http.ApiHandler;
import com.kplus.car.carwash.utils.http.HttpRequestHelper;
import com.kplus.car.carwash.widget.pulltorefresh.PullToRefreshListView;
import com.kplus.car.carwash.widget.pulltorefresh.library.PullToRefreshBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：订单列表
 * <br/><br/>Created by Fu on 2015/5/29.
 * <br/><br/>
 */
public class CNOrderListFragment extends Fragment implements OnListItemClickListener {
    private static final String TAG = "CNOrderListFragment";

    private int mPageIndex = Const.NONE;

    private Context mContext = null;

    private CNCarWashApp mApp = null;
    private View mView = null;
    private PullToRefreshListView plvOrderList = null;
    private TextView tvEmpty = null;

    private CNOrderListAdapter mOrderListAdapter = null;

    /**
     * 点击item的pos
     */
    public int mClickPosition = Const.NEGATIVE;

    private long mTotal = Const.NONE;
    private List<ServiceOrder> mServiceOrders = null;

    private NotfiyBroadcast mBroadcast = null;

    private ServiceOrder mClickServiceOrder = null;

    /**
     * 存放已经获取过的最新的日志
     */
    private HashMap<Long, ArrayList<OrderLog>> mFetchOrderLog = null;
    private HashMap<Long, Integer> mFetchOrderStatu = null;
    /**
     * 用于保存 PAY_PENDING(未支付)状态，如果列表中有未支付的，则会进行定时刷新列表操作
     */
    private Map<Long, Integer> mOrderPayPendingStatus = null;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = getStickyIntentFilter();
        mBroadcast = new NotfiyBroadcast();
        getActivity().registerReceiver(mBroadcast, filter);

        if (AppBridgeUtils.getIns().getUid() <= Const.NONE) {
            AppBridgeUtils.getIns().getUserInfo(getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.cn_order_list_layout, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    protected void initData() {
        mContext = getActivity();
        mApp = CNCarWashApp.getIns();
    }

    protected void initView() {
        tvEmpty = (TextView) mView.findViewById(R.id.tvEmpty);

        plvOrderList = (PullToRefreshListView) mView.findViewById(R.id.plvOrderList);

        plvOrderList.setMode(PullToRefreshBase.Mode.BOTH);

        String msg = getResources().getString(R.string.cn_loading);

        plvOrderList.getLoadingLayoutProxy().setPullLabel("下拉刷新");
        plvOrderList.getLoadingLayoutProxy().setRefreshingLabel(msg);
        plvOrderList.getLoadingLayoutProxy().setReleaseLabel("释放更新");
        plvOrderList.getLoadingLayoutProxy().setRefreshingLabelIm("刷新成功");
        plvOrderList.getLoadingLayoutProxy().setmEndLabel("刷新成功");

        plvOrderList.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
        plvOrderList.getLoadingLayoutProxy().setRefreshingLabel(msg);
        plvOrderList.getLoadingLayoutProxy().setReleaseLabel("释放加载");

        plvOrderList.setOnRefreshListener(mOrderListRefreshListener);

        mOrderListAdapter = new CNOrderListAdapter(mContext, mServiceOrders, this);

        plvOrderList.setAdapter(mOrderListAdapter);

        mApp.getThreadPool().submit(new CNThreadPool.Job<List<ServiceOrder>>() {
            @Override
            public List<ServiceOrder> run() {
                return CNOrderListUtil.getIns().getOrderLists();
            }
        }, new FutureListener<List<ServiceOrder>>() {
            @Override
            public void onFutureDone(Future<List<ServiceOrder>> future) {
                mServiceOrders = future.get();

                plvOrderList.setRefreshing();
                if (!plvOrderList.isRefreshing()) {
                    plvOrderList.setRefreshing(false);
                }

                if (null != mServiceOrders && mServiceOrders.size() > Const.NONE) {
                    setOrderListData();
                }
            }
        });
    }

    /**
     * 自动刷新的次数
     */
    private int mAutoRefreshCount = 1;
    /**
     * 处理定时自动刷新
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.trace(TAG, String.format("执行定时刷新操作，第%d次刷新...", mAutoRefreshCount));
            mAutoRefreshCount++;

            if (null != mOrderPayPendingStatus && mOrderPayPendingStatus.size() > Const.NONE) {
                Log.trace(TAG, "执行定时刷新操作中...");
//                plvOrderList.setRefreshing();
//                if (!plvOrderList.isRefreshing()) {
//                    plvOrderList.setRefreshing(false);
//                }

                for (Map.Entry<Long, Integer> entry : mOrderPayPendingStatus.entrySet()) {
                    fetchOrder(entry.getKey());
                }

                // 轮询执行刷新操作
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, getTimerRefreshTime());
            } else {
                Log.trace(TAG, "执行定时刷新操作--->没有未支付状态，不用刷新");
                mHandler.removeCallbacks(this);
            }
        }
    };

    /**
     * 第一次自动刷新是 5s, 15s, 30s, 55s, 65s, 5m
     *
     * @return 轮询时间
     */
    private long getTimerRefreshTime() {
        long refreshTime;
        switch (mAutoRefreshCount) {
            case 1:
                refreshTime = 5 * 1000;
                break;
            case 2:
                refreshTime = 15 * 1000;
                break;
            case 3:
                refreshTime = 30 * 1000;
                break;
            case 4:
                refreshTime = 55 * 1000;
                break;
            case 5:
                refreshTime = 65 * 1000;
                break;
            default:
                refreshTime = DateUtil.FIVE_MINUTES;
                break;
        }
        return refreshTime;
    }

    /**
     * 如果是未支付订单，轮询调用
     *
     * @param orderId 订单id
     */
    private void fetchOrder(long orderId) {
        HttpRequestHelper.fetchOrder(mContext, orderId, false, new ApiHandler(mContext) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                FetchOrderResp fetchOrderResp = (FetchOrderResp) baseInfo;
                ServiceOrder serviceOrder = fetchOrderResp.getOrder();

                makeData(serviceOrder);
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                }
            }
        });
    }

    /**
     * 轮询获取数据后处理
     *
     * @param serviceOrder 订单
     */
    private synchronized void makeData(ServiceOrder serviceOrder) {
        if (null == serviceOrder)
            return;

        if (null == mOrderPayPendingStatus) {
            mOrderPayPendingStatus = new HashMap<>();
        }

        int status = serviceOrder.getStatus();
        // 如果不是这几个状态就把该缓存记录删除掉
        if (status != OrderStatus.BOOKED.value()
                && status != OrderStatus.UNABLE_PAY.value()
                && status != OrderStatus.PAY_PENDING.value()
                && status != OrderStatus.UNKOWN.value()) {
            CNPayResultUtil.getIns().remove(serviceOrder.getId());
            mOrderPayPendingStatus.remove(serviceOrder.getId());

            if (null != mOrderListAdapter) {
                List<ServiceOrder> serviceOrders = mOrderListAdapter.getList();
                if (null != serviceOrders) {
                    for (int i = 0; i < serviceOrders.size(); i++) {
                        ServiceOrder temp = serviceOrders.get(i);
                        if (temp.getId() == serviceOrder.getId()) {
                            temp.setStatus(serviceOrder.getStatus());
                            break;
                        }
                    }
                    mOrderListAdapter.notifyDataSetChanged();
                }
            }

            CNOrderListUtil.getIns().setOrderStatus(serviceOrder.getId(), serviceOrder.getStatus());
        }
    }

    /**
     * 获取订单列表
     */
    private void getFetchPaginationOrders() {
        if (AppBridgeUtils.getIns().getUid() > Const.NONE) {
            HttpRequestHelper.getFetchPaginationOrders(mContext, mPageIndex, Const.PAGE_SIZE, new ApiHandler(mContext) {
                @Override
                public void onSuccess(BaseInfo baseInfo) {
                    super.onSuccess(baseInfo);

                    if (null != plvOrderList && plvOrderList.isRefreshing()) {
                        plvOrderList.onRefreshComplete();
                    }

                    FetchPaginationOrderResp fetchPaginationOrderResp = (FetchPaginationOrderResp) baseInfo;
                    if (null != fetchPaginationOrderResp && null != fetchPaginationOrderResp.getOrders()) {
                        mTotal = fetchPaginationOrderResp.getTotal();
                        mServiceOrders = fetchPaginationOrderResp.getOrders();

                        if (null == mFetchOrderLog) {
                            mFetchOrderLog = new HashMap<>();
                        }
                        if (null == mFetchOrderStatu) {
                            mFetchOrderStatu = new HashMap<>();
                        }

                        if (null == mOrderPayPendingStatus) {
                            mOrderPayPendingStatus = new HashMap<>();
                        }

                        for (ServiceOrder serviceOrder : mServiceOrders) {
                            int status = serviceOrder.getStatus();

                            // 列表中只有该订单是已完成才去获取一次日志，如果在这里点击评价可取到图片
                            if (status == OrderStatus.HANDLED.value()) {
                                long orderId = serviceOrder.getId();

                                if (null == mFetchOrderLog.get(orderId)) {
                                    Log.trace(TAG, "从服务器上取订单" + orderId + "的日志");
                                    mFetchOrderStatu.put(orderId, serviceOrder.getStatus());
                                    getFetchOrderLogs(orderId, serviceOrder);
                                } else {
                                    if (null != mFetchOrderStatu.get(orderId)) {
                                        int statu = mFetchOrderStatu.get(orderId);
                                        if (statu != serviceOrder.getStatus()) {
                                            // 如果取下来的订单状态不一样，重新获取日志
                                            Log.trace(TAG, "从服务器上取订单" + orderId + "的日志");
                                            mFetchOrderStatu.put(orderId, serviceOrder.getStatus());
                                            getFetchOrderLogs(orderId, serviceOrder);
                                        }
                                    }
                                }
                            } else if (status == OrderStatus.PAY_PENDING.value()) {
                                mOrderPayPendingStatus.put(serviceOrder.getId(), OrderStatus.PAY_PENDING.value());
                                // Notice 如果从服务器上获取到是未支付状态，再从本地获取状态是否已支付过，但服务器上状态还没更新等情况
                                int localOrderStatus = CNPayResultUtil.getIns().getOrderStatu(serviceOrder.getId(), serviceOrder.getFormId());
                                // 如果该订单本地状态是已支付，则把状态改为支付中;
                                if (localOrderStatus == OrderStatus.PAID.value()) {
                                    serviceOrder.setStatus(OrderStatus.PAYING.value());
                                }
                            }

                            // 如果不是这几个状态就把该缓存记录删除掉
                            if (status != OrderStatus.BOOKED.value()
                                    && status != OrderStatus.UNABLE_PAY.value()
                                    && status != OrderStatus.PAY_PENDING.value()
                                    && status != OrderStatus.UNKOWN.value()) {
                                CNPayResultUtil.getIns().remove(serviceOrder.getId());
                                mOrderPayPendingStatus.remove(serviceOrder.getId());
                            }
                        }

                        if (mPageIndex == Const.ONE) {
                            if (null != mOrderListAdapter) {
                                // 第一页刷新时，把列表清空重新加载
                                mOrderListAdapter.clear();
                            }

                            CNOrderListUtil.getIns().addOrderLists(mServiceOrders);
                        }

                        // 如果是最后一页就不显示加载更多
                        long page = CNCarWashingLogic.getTotalPageCount(Const.PAGE_SIZE, mTotal);
                        if (page == mPageIndex) {
                            if (null != plvOrderList) {
                                plvOrderList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                        }

                        if (null != mOrderListAdapter) {
                            mOrderListAdapter.setPage(mTotal, mPageIndex, page);
                            mOrderListAdapter.append(mServiceOrders);
                        }

                        if (null != mHandler) {
                            // 轮询执行刷新操作
                            mHandler.removeCallbacks(mRunnable);
                            mHandler.postDelayed(mRunnable, getTimerRefreshTime());
                        }
                    }
                    showEmptyView();
                }

                @Override
                public void onFailure(BaseInfo baseInfo) {
                    super.onFailure(baseInfo);
                    if (null != plvOrderList) {
                        if (plvOrderList.isRefreshing()) {
                            plvOrderList.onRefreshComplete();
                        }

                        plvOrderList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }

                    if (null != baseInfo) {
                        Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                        CNCommonManager.makeText(mContext, baseInfo.getMsg());
                    }
                }
            });
        }
    }

    private void getFetchOrderLogs(final long orderId, final ServiceOrder serviceOrder) {
        HttpRequestHelper.getFetchOrderLogs(mContext, orderId, false, new ApiHandler(mContext) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                FetchOrderLogsResp fetchOrderLogsResp = (FetchOrderLogsResp) baseInfo;
                if (null != fetchOrderLogsResp) {

                    List<OrderLog> logs = fetchOrderLogsResp.getLogs();
                    if (null == logs || logs.size() == Const.NONE) {
                        Log.trace(TAG, "该订单还没有订单历史！");
                    } else {
                        if (null == mFetchOrderLog) {
                            mFetchOrderLog = new HashMap<>();
                        }

                        ArrayList<OrderLog> tempLogs = new ArrayList<>(logs);
                        tempLogs = CNCarWashingLogic.addReviewContent(tempLogs, serviceOrder.getReview());

                        mFetchOrderLog.put(orderId, tempLogs);
                        // 放到缓存中
                        CNOrderLogUtil.getIns().addOrderLogs(orderId, tempLogs);
                    }
                }
            }

            @Override
            public void onFailure(BaseInfo baseInfo) {
                super.onFailure(baseInfo);
                if (null != baseInfo) {
                    Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                    CNCommonManager.makeText(mContext, baseInfo.getMsg());
                }
            }
        });
    }

    private void setOrderListData() {
        mOrderListAdapter.clear();
        plvOrderList.setAdapter(mOrderListAdapter);
        mOrderListAdapter.append(mServiceOrders);
    }

    private PullToRefreshListView.OnRefreshListener2 mOrderListRefreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            plvOrderList.setMode(PullToRefreshBase.Mode.BOTH);
            mPageIndex = Const.ONE;
            Log.trace(TAG, "当前正在下拉刷新第" + mPageIndex + "页的数据");
            getFetchPaginationOrders();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            mPageIndex += Const.ONE;
            Log.trace(TAG, "当前正在上拉加载第" + mPageIndex + "页的数据");
            getFetchPaginationOrders();
        }
    };

    private void showEmptyView() {
        if (null == mServiceOrders || mServiceOrders.size() == Const.NONE) {
            tvEmpty.setVisibility(View.VISIBLE);
            plvOrderList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppBridgeUtils.getIns().agentOnResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppBridgeUtils.getIns().agentOnPause(getActivity());
    }

    @Override
    public void onStop() {
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                CNOrderListUtil.saveOrderLists();
                return null;
            }
        });
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (null != mBroadcast) {
            getActivity().unregisterReceiver(mBroadcast);
            mBroadcast = null;
        }
        if (null != mFetchOrderLog) {
            mFetchOrderLog.clear();
            mFetchOrderLog = null;
        }
        if (null != mFetchOrderStatu) {
            mFetchOrderStatu.clear();
            mFetchOrderStatu = null;
        }
        if (null != mOrderPayPendingStatus) {
            mOrderPayPendingStatus.clear();
            mOrderPayPendingStatus = null;
        }
        mHandler.removeCallbacks(mRunnable);
        mHandler = null;
        super.onDestroy();
    }

    @Override
    public void onClickItem(final int position, View v) {
        mClickPosition = position;

        // 我的订单
        mClickServiceOrder = mOrderListAdapter.getItem(position);

        int vId = v.getId();
        if (vId == R.id.llMyOrder) {
            // 订单详情
            Intent intent = new Intent(mContext, CNOrderDetailsActivity.class);
            // 直接把该订单传过去
            intent.putExtra(CNCarWashingLogic.KEY_ORDER_DETAILS, mClickServiceOrder);
            CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_ORDER_DETAILS);
        } else if (vId == R.id.btnCloseOrder) {
            // 取消订单
            String msg = mContext.getResources().getString(R.string.cn_order_cancel_confirm);
            DialogManager.onAffirm(mContext, msg, new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    HttpRequestHelper.cancelOrder(mContext, mClickServiceOrder.getId(), new ApiHandler(mContext) {
                        @Override
                        public void onSuccess(BaseInfo baseInfo) {
                            super.onSuccess(baseInfo);

                            CancelOrderResp cancelOrderResp = (CancelOrderResp) baseInfo;
                            if (null != cancelOrderResp) {
                                if (CNStringUtil.isEmpty(cancelOrderResp.getMsg())) {
                                    mClickServiceOrder.setStatus(OrderStatus.CLOSED.value());

                                    // 我的订单
                                    mOrderListAdapter.notifyDataSetInvalidated();

                                    String msg = mContext.getResources().getString(R.string.cn_order_cancel_success);
                                    CNCommonManager.makeText(mContext, msg);
                                } else {
                                    CNCommonManager.makeText(mContext, cancelOrderResp.getMsg());
                                }
                            }
                        }

                        @Override
                        public void onFailure(BaseInfo baseInfo) {
                            super.onFailure(baseInfo);
                            if (null != baseInfo) {
                                Log.trace(TAG, "Error msg is " + baseInfo.getMsg());
                                CNCommonManager.makeText(mContext, baseInfo.getMsg());
                            }
                        }
                    });
                }
            });
        } else if (vId == R.id.btnOrderPay) {
            // 支付
            BigDecimal mTotalPrice;
            // 用服务价格，减去代金券和减少的钱
            mTotalPrice = mClickServiceOrder.getPrice();
            if (null == mTotalPrice || mTotalPrice.compareTo(new BigDecimal(Const.NONE)) <= Const.NONE) {
                mTotalPrice = new BigDecimal(Const.NONE);
            } else {
                mTotalPrice = mTotalPrice.subtract(mClickServiceOrder.getCouponPrice()).subtract(mClickServiceOrder.getReducePrice());
            }

            // 如果价格等于或小0时，默认为0.01
            if (mTotalPrice.compareTo(new BigDecimal(0f)) <= Const.NONE) {
                mTotalPrice = new BigDecimal(0.01f);
            }

            long formOrderId = mClickServiceOrder.getFormId();
            // 支付
            CNCarWashingLogic.startPayDialog(mContext, formOrderId, mTotalPrice);
        } else if (vId == R.id.btnOrderEvaluate) {
            // 评价
            ArrayList<OrderLog> orderLogs = CNOrderLogUtil.getIns().getOrderLogs(mClickServiceOrder.getId());
            ArrayList<String> servicePicUrls = CNCarWashingLogic.getOrderLogUrls(orderLogs);
            Intent intent = new Intent(mContext, CNEvaluateServiceActivity.class);
            intent.putExtra(CNCarWashingLogic.KEY_ORDER_DETAILS, mClickServiceOrder);
            intent.putExtra(CNCarWashingLogic.KEY_SHARE_IMAGES, servicePicUrls);
            CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_REVIEW_ORDER_DETAILS);
        } else if (vId == R.id.llLoadMore
                || vId == R.id.tvLoadMore) {
            // 点击加载更多
            mPageIndex += Const.ONE;
            getFetchPaginationOrders();
        }
    }

    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_PAY_RESULT_ACTION);
        filter.addAction(CustomBroadcast.ON_GET_USER_INFO_ACTION);
        return filter;
    }

    private class NotfiyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent)
                return;

            String action = intent.getAction();
            if (CustomBroadcast.ON_PAY_RESULT_ACTION.equals(action)) {
                String result = intent.getStringExtra("result");
                boolean isPayResult = CNCarWashingLogic.makePayResult(result);
                if (isPayResult) {
                    CNProgressDialogUtil.dismissProgress(mContext);
                    if (null != mClickServiceOrder) {
                        // Notice 支付成功后手动把本地状态改为已支付，防止服务器支付结果慢的情况
                        CNPayResultUtil.getIns().add(mClickServiceOrder.getId(), mClickServiceOrder.getFormId(), OrderStatus.PAID.value());

                        mClickServiceOrder.setStatus(OrderStatus.PAID.value());
                        // 将orderId从自动刷新队列中删除
                        if (null != mOrderPayPendingStatus) {
                            mOrderPayPendingStatus.remove(mClickServiceOrder.getId());
                        }
                        // 订单状态
                        setItemOrderStatu(mClickServiceOrder);
                    }
                } else {
                    CNProgressDialogUtil.dismissProgress(mContext);
                }
            } else if (CustomBroadcast.ON_GET_USER_INFO_ACTION.equals(action)) {
                if (AppBridgeUtils.getIns().getUid() > Const.NONE) {
                    getFetchPaginationOrders();
                } else {
                    if (null != plvOrderList && plvOrderList.isRefreshing()) {
                        plvOrderList.onRefreshComplete();
                    }
                    if (null != getActivity()) {
                        mContext = getActivity();
                        CNCommonManager.makeText(mContext, "用户信息获取失败");
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data)
            return;
        Bundle bundle;
        switch (requestCode) {
            case CNCarWashingLogic.TYPE_REVIEW_ORDER_DETAILS: // 评论回来
            case CNCarWashingLogic.TYPE_ORDER_DETAILS: // 详情回来
                bundle = data.getExtras();
                if (null != bundle) {
                    ServiceOrder serviceOrder = (ServiceOrder) bundle.get(CNCarWashingLogic.KEY_ORDER_DETAILS);
                    setItemOrderStatu(serviceOrder);
                }
                break;
            default:
                String str = data.getExtras().getString("pay_result");
                if (null != getActivity()) {
                    CNCarWashingLogic.makeYinLianPayResult(getActivity(), str);
                }
                break;
        }
    }

    public void setItemOrderStatu(ServiceOrder serviceOrder) {
        ServiceOrder thisOrder = mOrderListAdapter.getItem(mClickPosition);
        thisOrder.setStatus(serviceOrder.getStatus());
        thisOrder.setStatus(serviceOrder.getStatus());
        if (null != serviceOrder.getReview()) {
            thisOrder.setReview(serviceOrder.getReview());
        }
        mOrderListAdapter.notifyDataSetInvalidated();

        CNOrderListUtil.getIns().addOrderLists(mOrderListAdapter.getList());
    }
}
