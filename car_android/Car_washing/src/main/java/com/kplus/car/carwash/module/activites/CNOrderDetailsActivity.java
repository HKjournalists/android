package com.kplus.car.carwash.module.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.Address;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.CancelOrderResp;
import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.bean.Contact;
import com.kplus.car.carwash.bean.FetchOrderLogsResp;
import com.kplus.car.carwash.bean.FetchOrderResp;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.bean.OrderLog;
import com.kplus.car.carwash.bean.Position;
import com.kplus.car.carwash.bean.Review;
import com.kplus.car.carwash.bean.ServiceOrder;
import com.kplus.car.carwash.bean.ServingTime;
import com.kplus.car.carwash.bean.Staff;
import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.common.CustomBroadcast;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.manager.DialogManager;
import com.kplus.car.carwash.module.CNThreadPool;
import com.kplus.car.carwash.module.OrderStatus;
import com.kplus.car.carwash.module.adapter.CNOrderDetailServiceAdapter;
import com.kplus.car.carwash.module.adapter.CNOrderLogAdapter;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.utils.CNOrderLogUtil;
import com.kplus.car.carwash.utils.CNPayResultUtil;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.DateUtil;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.http.ApiHandler;
import com.kplus.car.carwash.utils.http.HttpRequestHelper;
import com.kplus.car.carwash.widget.CNNavigationBar;
import com.kplus.car.carwash.widget.CNWashingServiceListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description：订单详情界面
 * <br/><br/>Created by Fu on 2015/5/13.
 * <br/><br/>
 */
public class CNOrderDetailsActivity extends CNParentActivity implements CNViewClickUtil.NoFastClickListener,
        OnListItemClickListener, CNOrderLogAdapter.OnLogPicClickListener {

    private static final String TAG = "CNOrderDetailsActivity";

    private View mView = null;

    private TextView tvOrderNum = null;
    private TextView tvOrderStatu = null;
    private ScrollView svDetails = null;
    private TextView tvName = null;
    private TextView tvMobile = null;
    private TextView tvCarInfo = null;
    private TextView tvCarLocation = null;
    private TextView tvServiceTime = null;
    private CNWashingServiceListView lvServiceList = null;
    private RelativeLayout rlVoucher = null;
    private TextView tvVoucherPrice = null;
    private View viewVoucherLine = null;
    private LinearLayout llBottom = null;
    private Button btnOrderEvaluate = null;
    private Button btnOrderCancel = null;
    private Button btnOrderPay = null;
    private TextView tvCostPrice = null;
    private CNWashingServiceListView lvOrderLogList = null;

    private CNOrderDetailServiceAdapter mOrderServiceAdapter = null;
    private CNOrderLogAdapter mOrderLogAdapter = null;

    private ServiceOrder mServiceOrder = null;
    private long mOrderId = Const.NEGATIVE;

    private BigDecimal mTotalPrice = new BigDecimal(Const.NONE);

    private ArrayList<OrderLog> mOrderLogs = null;

    /**
     * 开始和结束服务的前面两张图片
     */
    private ArrayList<String> mServicePicUrls = null;
    /**
     * 如果只有orderId传过来，则直接从服务器上获取
     */
    private boolean isLoadingByService = false;

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mServiceOrder = (ServiceOrder) bundle.get(CNCarWashingLogic.KEY_ORDER_DETAILS);
            mOrderId = bundle.getLong(CNCarWashingLogic.KEY_ORDER_DETAILS_ORDER_ID);
            if (null != mServiceOrder) {
                isLoadingByService = false;
                mOrderId = mServiceOrder.getId();
            } else {
                isLoadingByService = true;
            }
        }
    }

    @Override
    protected void initView() {
        mView = mInflater.inflate(R.layout.cn_order_details_layout, null);

        tvOrderNum = findView(mView, R.id.tvOrderNum);
        tvOrderStatu = findView(mView, R.id.tvOrderStatu);
        svDetails = findView(mView, R.id.svDetails);
        tvName = findView(mView, R.id.tvName);
        tvMobile = findView(mView, R.id.tvMobile);
        tvCarInfo = findView(mView, R.id.tvCarInfo);
        tvCarLocation = findView(mView, R.id.tvCarLocation);
        tvServiceTime = findView(mView, R.id.tvServiceTime);
        lvServiceList = findView(mView, R.id.lvServiceList);
        rlVoucher = findView(mView, R.id.rlVoucher);
        tvVoucherPrice = findView(mView, R.id.tvVoucherPrice);
        viewVoucherLine = findView(mView, R.id.viewVoucherLine);
        llBottom = findView(mView, R.id.llBottom);
        btnOrderEvaluate = findView(mView, R.id.btnOrderEvaluate);
        btnOrderCancel = findView(mView, R.id.btnOrderCancel);
        btnOrderPay = findView(mView, R.id.btnOrderPay);
        tvCostPrice = findView(mView, R.id.tvCostPrice);
        lvOrderLogList = findView(mView, R.id.lvOrderLogList);

        CNViewClickUtil.setNoFastClickListener(btnOrderEvaluate, this);
        CNViewClickUtil.setNoFastClickListener(btnOrderCancel, this);
        CNViewClickUtil.setNoFastClickListener(btnOrderPay, this);

        llBottom.setVisibility(View.GONE);
        btnOrderCancel.setVisibility(View.GONE);
        btnOrderPay.setVisibility(View.GONE);
        btnOrderEvaluate.setVisibility(View.GONE);

        mOrderServiceAdapter = new CNOrderDetailServiceAdapter(mContext, null, null);
        lvServiceList.setAdapter(mOrderServiceAdapter);

        mOrderLogAdapter = new CNOrderLogAdapter(mContext, null, mServiceOrder, this);
        lvOrderLogList.setAdapter(mOrderLogAdapter);
        // 设置点击图片的回调
        mOrderLogAdapter.setOnLogPicClickListener(this);

        CNProgressDialogUtil.showProgress(this);
    }

    @Override
    protected void initToolbarView() {
        mLlContent.removeAllViews();
        mLlContent.addView(mView);

        // 如果传过来的有订单信息，先显示出来
        setUIValue();

        lvServiceList.setFocusable(false);
        lvServiceList.setFocusableInTouchMode(false);
        lvOrderLogList.setFocusable(false);
        lvOrderLogList.setFocusableInTouchMode(false);
        svDetails.smoothScrollTo(Const.NONE, Const.NONE);

        // 从服务器上获取订单
        if (isLoadingByService) {
            fetchOrder(mOrderId, true);
        } else {
            mApp.getThreadPool().submit(new CNThreadPool.Job<ArrayList<OrderLog>>() {
                @Override
                public ArrayList<OrderLog> run() {
                    return CNOrderLogUtil.getIns().getOrderLogs(mOrderId);
                }
            }, new FutureListener<ArrayList<OrderLog>>() {
                @Override
                public void onFutureDone(Future<ArrayList<OrderLog>> future) {
                    mOrderLogs = future.get();
                    if (null != mOrderLogs && mOrderLogs.size() > Const.NONE) {
                        // 从缓存中取出来的是已经经过处理的，可直接使用
                        setOrderLogData(mOrderLogs);
                        CNProgressDialogUtil.dismissProgress(mContext);
                    }
                    getFetchOrderLogs(mOrderId, true);
                }
            });
        }
    }

    /**
     * 设置在界面上显示
     */
    private void setUIValue() {
        // 如果传过来的有订单信息，先显示出来
        if (null != mServiceOrder) {
            tvOrderNum.setText(mServiceOrder.getFormOrderNo());

            setOrderStatu(mServiceOrder.getStatus());

            Contact contact = mServiceOrder.getContact();
            tvName.setText(contact.getName());
            tvMobile.setText(contact.getMobile());

            // 车辆信息
            Car car = mServiceOrder.getCar();
            String license = car.getLicense();
            StringBuffer buffer = new StringBuffer(license).append(" ");
            buffer.append(car.getColor().getName()).append(" ");
            buffer.append(car.getBrand().getName()).append(" ");
            buffer.append(car.getModel().getName());
            tvCarInfo.setText(buffer.toString());

            // 车辆位置
            Position carPosition = mServiceOrder.getCarPosition();
            Address address = carPosition.getAddress();
            buffer = new StringBuffer();
            buffer.append(address.getProvince());
            buffer.append(address.getCity());
            buffer.append(address.getDistrict());
            buffer.append(address.getStreet());
            buffer.append(address.getOther());
            tvCarLocation.setText(buffer.toString());

            // 服务时间
            ServingTime servingTime = mServiceOrder.getServingTime();
            String time = DateUtil.getServingTime(servingTime, true);
            tvServiceTime.setText(time);

            // 如果为空，显示为0
            BigDecimal couponPrice = mServiceOrder.getCouponPrice();
            if (null == couponPrice) {
                couponPrice = new BigDecimal(0);
            }
            BigDecimal reducePrice = mServiceOrder.getReducePrice();
            if (null == reducePrice) {
                reducePrice = new BigDecimal(0);
            }

            // 用服务价格，减去代金券和减少的钱
            mTotalPrice = mServiceOrder.getPrice();
            if (null == mTotalPrice || mTotalPrice.compareTo(new BigDecimal(Const.NONE)) <= Const.NONE) {
                mTotalPrice = new BigDecimal(Const.NONE);
            } else {
                mTotalPrice = mTotalPrice.subtract(couponPrice).subtract(reducePrice);
            }

            // 使用的代金券价格
            String tempPriceUint = mContext.getResources().getString(R.string.cn_order_price);
            double voucherPrice = couponPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String price = String.format(tempPriceUint, voucherPrice);
            tvVoucherPrice.setText(price);

            // 如果代金券为0，表示没有使用，就不显示出来
            if (couponPrice.compareTo(new BigDecimal(0)) <= Const.NONE) {
                rlVoucher.setVisibility(View.GONE);
                tvVoucherPrice.setVisibility(View.GONE);
                viewVoucherLine.setVisibility(View.GONE);
            } else {
                rlVoucher.setVisibility(View.VISIBLE);
                tvVoucherPrice.setVisibility(View.VISIBLE);
                viewVoucherLine.setVisibility(View.VISIBLE);
            }

            // 如果价格等于或小0时，默认为0.01
            if (mTotalPrice.compareTo(new BigDecimal(0f)) <= Const.NONE) {
                mTotalPrice = new BigDecimal(0.01f);
            }

            double payPrice = mTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            // 显示支付的钱
            price = String.format(tempPriceUint, payPrice);
            tvCostPrice.setText(price);

            List<OnSiteService> services = mServiceOrder.getServices();
            if (null != services) {
                mOrderServiceAdapter.clear();
                mOrderServiceAdapter.append(services);
            }
        }
    }

    private void setOrderStatu(int statu) {
        // 订单状态
        OrderStatus orderStatus = OrderStatus.valueOf(statu);
        tvOrderStatu.setText(orderStatus.readableName());

        if (orderStatus.value() == OrderStatus.PAY_PENDING.value()) { // 待支付
            setShareView(false);
            llBottom.setVisibility(View.VISIBLE);
            btnOrderCancel.setVisibility(View.VISIBLE);
            btnOrderPay.setVisibility(View.VISIBLE);
            btnOrderEvaluate.setVisibility(View.GONE);
        } else if (orderStatus.value() == OrderStatus.HANDLED.value()) { // 已完成
            llBottom.setVisibility(View.VISIBLE);
            btnOrderEvaluate.setVisibility(View.VISIBLE);
            btnOrderCancel.setVisibility(View.GONE);
            btnOrderPay.setVisibility(View.GONE);
            setShareView(true);
        } else {
            if (orderStatus.value() == OrderStatus.REVIEWED.value()) { // 已评价
                setShareView(true);
            } else {
                setShareView(false);
            }
            llBottom.setVisibility(View.GONE);
            btnOrderCancel.setVisibility(View.GONE);
            btnOrderPay.setVisibility(View.GONE);
            btnOrderEvaluate.setVisibility(View.GONE);
        }
    }

    private void setShareView(boolean isShare) {
        if (isShare) {
            mNavigationBar.setRightBtnVisibility(View.VISIBLE);
        } else {
            mNavigationBar.setRightBtnVisibility(View.GONE);
        }
    }

    private void fetchOrder(long orderId, boolean isProgress) {
        HttpRequestHelper.fetchOrder(mContext, orderId, isProgress, new ApiHandler(mContext) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                FetchOrderResp fetchOrderResp = (FetchOrderResp) baseInfo;
                mServiceOrder = fetchOrderResp.getOrder();
                getFetchOrderLogs(mOrderId, false);
                setUIValue();
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

    private void getFetchOrderLogs(long orderId, boolean isProgress) {
        HttpRequestHelper.getFetchOrderLogs(mContext, orderId, isProgress, new ApiHandler(mContext) {
            @Override
            public void onSuccess(BaseInfo baseInfo) {
                super.onSuccess(baseInfo);
                FetchOrderLogsResp fetchOrderLogsResp = (FetchOrderLogsResp) baseInfo;
                if (null != fetchOrderLogsResp) {
                    List<OrderLog> logs = fetchOrderLogsResp.getLogs();
                    if (null == logs || logs.size() == Const.NONE) {
                        CNCommonManager.makeText(mContext, "该订单还没有订单历史！");
                    } else {

                        if (null != mOrderLogs) {
                            mOrderLogs.clear();
                            mOrderLogs = null;
                        }
                        mOrderLogs = new ArrayList<>(logs);
                        if (null != mServiceOrder) {
                            mOrderLogs = CNCarWashingLogic.addReviewContent(mOrderLogs, mServiceOrder.getReview());
                        }
                        setOrderLogData(mOrderLogs);

                        // 放到缓存中
                        CNOrderLogUtil.getIns().addOrderLogs(mOrderId, mOrderLogs);
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

    private void setOrderLogData(List<OrderLog> orderLogs) {
        mOrderLogAdapter.setServiceOrder(mServiceOrder);
        mOrderLogAdapter.clear();
        lvOrderLogList.setAdapter(mOrderLogAdapter);
        mOrderLogAdapter.append(orderLogs);
    }

    @Override
    public void onClickItem(int position, View v) {
        if (v.getId() == R.id.tvWorkerMobile) {
            // 点击手机号
            OrderLog orderLog = mOrderLogAdapter.getItem(position);
            Staff staff = orderLog.getWorker();
            final String mobile = staff.getMobile();
            DialogManager.onCall(mContext, mobile, new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        // 呼叫
                        Intent intent = new Intent();
                        // 系统默认的action，用来打开默认的电话界面
                        // Intent.ACTION_CALL直接拨打
                        intent.setAction(Intent.ACTION_DIAL);// 调用拨打界面，而不直接拨打
                        // 需要拨打的号码
                        intent.setData(Uri.parse("tel:" + mobile));
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (v.getId() == R.id.ivWorkerHeader) {
            // 点击头像
            OrderLog orderLog = mOrderLogAdapter.getItem(position);
            Staff staff = orderLog.getWorker();
            String url = staff.getAvatar();
            ArrayList<String> urls = new ArrayList<>();
            urls.add(url);
            Intent data = new Intent(mContext, CNViewPicActivity.class);
            data.putExtra(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_URLS, urls);
            data.putExtra(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_POSITION, Const.NONE);
            data.putExtra(CNCarWashingLogic.KEY_REVIEW_PIC_TYPE, CNCarWashingLogic.TYPE_PIC_HEADER);
            CNViewManager.getIns().showActivity(mContext, data, false, R.anim.cn_zoom_enter, Const.NONE);
        }
    }

    /**
     * 日志图片点击的回调
     */
    @Override
    public void onLogPicClick(OrderLog orderLog, String selectedUrl, int selectedPosition) {
        Intent data = new Intent(mContext, CNViewPicActivity.class);
        data.putExtra(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_URLS, new ArrayList<>(orderLog.getImages()));
        data.putExtra(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_POSITION, selectedPosition);
        data.putExtra(CNCarWashingLogic.KEY_REVIEW_PIC_TYPE, CNCarWashingLogic.TYPE_PIC_OTHER);
        CNViewManager.getIns().showActivity(mContext, data, false, R.anim.cn_zoom_enter, Const.NONE);
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return true;
    }

    @Override
    public void setToolbarStyle(CNNavigationBar navBar) {
        // 订单详情
        navBar.setNavTitle(getStringResources(R.string.cn_order_details));
        // 返回
        navBar.setLeftBtn("", R.drawable.btn_back_selector, Const.NONE);
        // 分享按钮
        navBar.setRightBtn("", R.drawable.btn_share_selector, Const.NONE);
        navBar.setRightBtnVisibility(View.GONE);
    }

    @Override
    public boolean onItemSelectedListener(int viewId) {
        // library 中不能用 switch找控件
        if (viewId == R.id.tvNavLeft) {
            CNViewManager.getIns().pop(this);
        } else if (viewId == R.id.tvNavRight) { // 分享
            mServicePicUrls = CNCarWashingLogic.getOrderLogUrls(mOrderLogs);
            Intent data = new Intent(mContext, CNShareActivity.class);
            data.putExtra(CNCarWashingLogic.KEY_SHARE_TYPE, CNCarWashingLogic.SHARE_TYPE_ORDERLOG);
            data.putExtra(CNCarWashingLogic.KEY_SHARE_IMAGES, mServicePicUrls);
            startActivityForResult(data, CNCarWashingLogic.TYPE_SHARE_IMAGES_CODE);
        }
        return true;
    }

    @Override
    public void onNoFastClick(View v) {
        int vId = v.getId();
        if (vId == R.id.btnOrderEvaluate) {
            // 评价
            mServicePicUrls = CNCarWashingLogic.getOrderLogUrls(mOrderLogs);
            Intent intent = new Intent(mContext, CNEvaluateServiceActivity.class);
            intent.putExtra(CNCarWashingLogic.KEY_ORDER_DETAILS, mServiceOrder);
            intent.putExtra(CNCarWashingLogic.KEY_SHARE_IMAGES, mServicePicUrls);
            CNViewManager.getIns().showActivity(mContext, intent, CNCarWashingLogic.TYPE_REVIEW_ORDER_DETAILS);
        } else if (vId == R.id.btnOrderCancel) {
            // 取消订单
            String msg = getStringResources(R.string.cn_order_cancel_confirm);
            DialogManager.onAffirm(mContext, msg, new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    HttpRequestHelper.cancelOrder(mContext, mServiceOrder.getId(), new ApiHandler(mContext) {
                        @Override
                        public void onSuccess(BaseInfo baseInfo) {
                            super.onSuccess(baseInfo);

                            CancelOrderResp cancelOrderResp = (CancelOrderResp) baseInfo;
                            if (null != cancelOrderResp) {
                                if (CNStringUtil.isEmpty(cancelOrderResp.getMsg())) {
                                    mServiceOrder.setStatus(OrderStatus.CLOSED.value());
                                    // 订单状态
                                    setOrderStatu(mServiceOrder.getStatus());

                                    setOrderResult();

                                    CNCommonManager.makeText(mContext, getStringResources(R.string.cn_order_cancel_success));
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
            // 立即支付
            long formOrderId = mServiceOrder.getFormId();
            CNCarWashingLogic.startPayDialog(mContext, formOrderId, mTotalPrice);
        }
    }

    private void setOrderResult() {
        Intent data = new Intent();
        data.putExtra(CNCarWashingLogic.KEY_ORDER_DETAILS, mServiceOrder);
        setResult(Activity.RESULT_OK, data);
    }

    @Override
    protected void onStop() {
        mApp.getThreadPool().submit(new CNThreadPool.Job<Void>() {
            @Override
            public Void run() {
                CNOrderLogUtil.saveOrderlogs();
                return null;
            }
        });
        super.onStop();
    }

    @Override
    protected IntentFilter getStickyIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomBroadcast.ON_PAY_RESULT_ACTION);
        return filter;
    }

    @Override
    protected void onStickyNotify(Context context, Intent intent) {
        String action = intent.getAction();
        if (CustomBroadcast.ON_PAY_RESULT_ACTION.equals(action)) {
            String result = intent.getStringExtra("result");
            boolean isPayResult = CNCarWashingLogic.makePayResult(result);
            if (isPayResult) {
                CNProgressDialogUtil.dismissProgress(mContext);
                mServiceOrder.setStatus(OrderStatus.PAID.value());
                // 订单状态
                setOrderStatu(mServiceOrder.getStatus());

                // Notice 支付成功后手动把本地状态改为已支付，防止服务器支付结果慢的情况
                CNPayResultUtil.getIns().add(mServiceOrder.getId(), mServiceOrder.getFormId(), OrderStatus.PAID.value());

                setOrderResult();
            } else {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data)
            return;
        switch (requestCode) {
            case CNCarWashingLogic.TYPE_REVIEW_ORDER_DETAILS: // 评论回来
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        ServiceOrder serviceOrder = (ServiceOrder) bundle.get(CNCarWashingLogic.KEY_ORDER_DETAILS);
                        if (null != serviceOrder) {
                            mServiceOrder.setStatus(serviceOrder.getStatus());
                            // 订单状态
                            setOrderStatu(mServiceOrder.getStatus());
                            // 评论回来，重新加载订单日志
                            Review review = serviceOrder.getReview();
                            if (null != review) {
                                mServiceOrder.setReview(review);
                                mOrderLogs = CNCarWashingLogic.addReviewContent(mOrderLogs, review);
                            }
                            setOrderLogData(mOrderLogs);
                            setOrderResult();
                        }
                    }
                }
                break;
            case CNCarWashingLogic.TYPE_SHARE_IMAGES_CODE: // 分享回来了
                Log.trace(TAG, "分享返回了");
                break;
            default:
                String str = data.getExtras().getString("pay_result");
                CNCarWashingLogic.makeYinLianPayResult(mContext, str);
                break;
        }
    }
}
