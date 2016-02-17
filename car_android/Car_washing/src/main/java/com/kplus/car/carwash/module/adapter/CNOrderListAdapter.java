package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.Address;
import com.kplus.car.carwash.bean.Car;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.bean.Position;
import com.kplus.car.carwash.bean.ServiceOrder;
import com.kplus.car.carwash.bean.ServingTime;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.OrderStatus;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.DateUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description：订单列表适配器
 * <br/><br/>Created by Fu on 2015/5/29.
 * <br/><br/>
 */
public class CNOrderListAdapter extends CNBaseListAdapter<ServiceOrder> {
    private static final String TAG = "CNOrderListAdapter";
    private long mTotalCount = Const.NONE;
    private int mCurrentPageIndex = Const.NONE;
    private long mTotalPage = Const.NONE;

    public CNOrderListAdapter(Context context, List<ServiceOrder> list, OnListItemClickListener listener) {
        super(context, list, listener);
    }

    public void setPage(long totalCount, int currentPageIndex, long totalPage) {
        mTotalCount = totalCount;
        mCurrentPageIndex = currentPageIndex;
        mTotalPage = totalPage;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        OrderListViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_orders_list_item, parent, false);
            holder = new OrderListViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (OrderListViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class OrderListViewHolder extends CNBaseAdapterViewHolder<ServiceOrder> {
        private LinearLayout llMyOrder = null;
        private TextView tvServiceItem = null;
        private TextView tvOrderTime = null;
        private TextView tvOrderStatu = null;
        private TextView tvCarInfo = null;
        private TextView tvCarLocation = null;
        private TextView tvPrice = null;
        private LinearLayout llActionCloseOrPay = null;
        private Button btnCloseOrder = null;
        private Button btnOrderPay = null;
        private Button btnOrderEvaluate = null;
        private LinearLayout llLoadMore = null;
        private TextView tvLoadMore = null;

        public OrderListViewHolder(View view, OnListItemClickListener listener) {
            super(view, listener);

            llMyOrder = findView(R.id.llMyOrder);
            tvServiceItem = findView(R.id.tvServiceItem);
            tvOrderTime = findView(R.id.tvOrderTime);
            tvOrderStatu = findView(R.id.tvOrderStatu);
            tvCarInfo = findView(R.id.tvCarInfo);
            tvCarLocation = findView(R.id.tvCarLocation);
            tvPrice = findView(R.id.tvPrice);
            llActionCloseOrPay = findView(R.id.llActionCloseOrPay);
            btnCloseOrder = findView(R.id.btnCloseOrder);
            btnOrderPay = findView(R.id.btnOrderPay);
            btnOrderEvaluate = findView(R.id.btnOrderEvaluate);

            llLoadMore = findView(R.id.llLoadMore);
            tvLoadMore = findView(R.id.tvLoadMore);

            CNViewClickUtil.setNoFastClickListener(llMyOrder, this);
            CNViewClickUtil.setNoFastClickListener(btnCloseOrder, this);
            CNViewClickUtil.setNoFastClickListener(btnOrderPay, this);
            CNViewClickUtil.setNoFastClickListener(btnOrderEvaluate, this);
            CNViewClickUtil.setNoFastClickListener(llLoadMore, this);
            CNViewClickUtil.setNoFastClickListener(tvLoadMore, this);
        }

        @Override
        public void setValue(int position, ServiceOrder serviceOrder) {
            mPosition = position;

            List<OnSiteService> siteServices = serviceOrder.getServices();
            StringBuffer buffer = new StringBuffer();
            for (OnSiteService service : siteServices) {
                buffer.append(service.getName()).append(" ");
            }

            tvServiceItem.setText(buffer.toString());

            // 服务时间
            ServingTime servingTime = serviceOrder.getServingTime();
            String time = DateUtil.getServingTime(servingTime, true);
            tvOrderTime.setText(time);

            llActionCloseOrPay.setVisibility(View.GONE);
            btnCloseOrder.setVisibility(View.GONE);
            btnOrderPay.setVisibility(View.GONE);
            btnOrderEvaluate.setVisibility(View.GONE);
            // 订单状态
            OrderStatus orderStatus = OrderStatus.valueOf(serviceOrder.getStatus());
            tvOrderStatu.setText(orderStatus.readableName());
            if (orderStatus.value() == OrderStatus.PAY_PENDING.value()) { // 待支付
                btnCloseOrder.setVisibility(View.VISIBLE);
                btnOrderPay.setVisibility(View.VISIBLE);
                llActionCloseOrPay.setVisibility(View.VISIBLE);

                btnOrderEvaluate.setVisibility(View.GONE);
            } else if (orderStatus.value() == OrderStatus.HANDLED.value()) { // 已完成
                btnOrderEvaluate.setVisibility(View.VISIBLE);
                llActionCloseOrPay.setVisibility(View.VISIBLE);

                btnCloseOrder.setVisibility(View.GONE);
                btnOrderPay.setVisibility(View.GONE);
            }

            // 车辆信息
            Car car = serviceOrder.getCar();
            String license = car.getLicense();
            buffer = new StringBuffer(license).append(" ");
            buffer.append(car.getColor().getName()).append(" ");
            buffer.append(car.getBrand().getName()).append(" ");
            buffer.append(car.getModel().getName());
            tvCarInfo.setText(buffer.toString());

            // 车辆位置
            Position carPosition = serviceOrder.getCarPosition();
            Address address = carPosition.getAddress();
            buffer = new StringBuffer();
//            buffer.append(address.getProvince());
//            buffer.append(address.getCity());
//            buffer.append(address.getDistrict());
            buffer.append(address.getStreet());
            buffer.append(address.getOther());
            tvCarLocation.setText(buffer.toString());

            // 用服务价格，减去代金券和减少的钱
            BigDecimal mTotalPrice = serviceOrder.getPrice();
            if (null == mTotalPrice || mTotalPrice.compareTo(new BigDecimal(Const.NONE)) <= Const.NONE) {
                mTotalPrice = new BigDecimal(Const.NONE);
            } else {
                mTotalPrice = mTotalPrice.subtract(serviceOrder.getCouponPrice()).subtract(serviceOrder.getReducePrice());
            }
            // 如果价格等于或小0时，默认为0.01
            if (mTotalPrice.compareTo(new BigDecimal(0f)) <= Const.NONE) {
                mTotalPrice = new BigDecimal(0.01f);
            }
            double payPrice = mTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 显示支付的钱
            String price = mContext.getResources().getString(R.string.cn_order_price);
            price = String.format(price, payPrice);
            tvPrice.setText(price);

            if (mPosition == getCount() - Const.ONE) {
                llLoadMore.setVisibility(View.VISIBLE);
                if (mCurrentPageIndex == mTotalPage) {
                    tvLoadMore.setText("没有更多订单了");
                } else {
                    tvLoadMore.setText("点击加载更多");
                }
            }
            llLoadMore.setVisibility(View.GONE);
        }
    }
}
