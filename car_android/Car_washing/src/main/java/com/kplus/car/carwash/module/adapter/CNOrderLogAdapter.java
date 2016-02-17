package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OrderLog;
import com.kplus.car.carwash.bean.ServiceOrder;
import com.kplus.car.carwash.bean.Staff;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.CNCircleBitmapDisplayer;
import com.kplus.car.carwash.module.LogAction;
import com.kplus.car.carwash.module.OrderStatus;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.module.base.CNBaseAdapterViewHolder;
import com.kplus.car.carwash.module.base.CNBaseListAdapter;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.DateUtil;
import com.kplus.car.carwash.widget.CNNoScrollGridView;
import com.kplus.car.carwash.widget.CNStarGradeLinearlayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Description：订单日志适配器
 * <br/><br/>Created by Fu on 2015/5/13.
 * <br/><br/>
 */
public class CNOrderLogAdapter extends CNBaseListAdapter<OrderLog> {

    private final DisplayImageOptions mOptions;
    private ServiceOrder mServiceOrder = null;

    public CNOrderLogAdapter(Context context, List<OrderLog> list, ServiceOrder serviceOrder, OnListItemClickListener listener) {
        super(context, list, listener);
        mServiceOrder = serviceOrder;
        mOptions = CNImageUtils.getImageOptions(R.drawable.cn_icon_worker_default,
                R.drawable.cn_icon_worker_default,
                R.drawable.cn_icon_worker_default,
                new CNCircleBitmapDisplayer());
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.mServiceOrder = serviceOrder;
    }

    @Override
    public View bindView(int position, View view, ViewGroup parent) {
        OrderLogViewHolder holder;
        if (null == view) {
            view = mInflater.inflate(R.layout.cn_timeline_orderlog_item, parent, false);
            holder = new OrderLogViewHolder(view, mClickListener);
            view.setTag(holder);
        } else {
            holder = (OrderLogViewHolder) view.getTag();
        }
        holder.setValue(position, getItem(position));
        return view;
    }

    private class OrderLogViewHolder extends CNBaseAdapterViewHolder<OrderLog> {
        private TextView tvOrderLogTitle = null;
        private LinearLayout llEvaluate = null;
        private TextView tvUserNameForEvaluate = null;
        private TextView tvEvaluateMsg = null;
        private LinearLayout llServiceImg = null;
        private CNNoScrollGridView gvImage = null;
        private CNOrderLogPicAdapter mPicAdapter = null;
        private TextView tvOderLogDate = null;
        private View view_bottom_line = null;
        private CNStarGradeLinearlayout sgStarView = null;

        private LinearLayout llWorkerInfo = null;
        private ImageView ivWorkerHeader = null;
        private TextView tvWorkerName = null;
        private TextView tvWorkerMobile = null;

        private View viewBottom = null;

        public OrderLogViewHolder(View view, final OnListItemClickListener listener) {
            super(view, listener);
            tvOrderLogTitle = findView(R.id.tvOrderLogTitle);
            llEvaluate = findView(R.id.llEvaluate);
            tvUserNameForEvaluate = findView(R.id.tvUserNameForEvaluate);
            tvEvaluateMsg = findView(R.id.tvEvaluateMsg);
            llServiceImg = findView(R.id.llServiceImg);
            gvImage = findView(R.id.gvImage);
            tvOderLogDate = findView(R.id.tvOderLogDate);
            view_bottom_line = findView(R.id.view_bottom_line);

            llWorkerInfo = findView(R.id.llWorkerInfo);
            ivWorkerHeader = findView(R.id.ivWorkerHeader);
            tvWorkerName = findView(R.id.tvWorkerName);
            tvWorkerMobile = findView(R.id.tvWorkerMobile);

            viewBottom = findView(R.id.viewBottom);

            // 初始化显示图片适配器
            mPicAdapter = new CNOrderLogPicAdapter(mContext, null, new OnListItemClickListener() {
                @Override
                public void onClickItem(int position, View v) {
                    // 点击图片
                    if (null != mLogPicClickListener) {
                        String selectedUrl = mPicAdapter.getItem(position);
                        OrderLog orderLog = mPicAdapter.getOrderLog();
                        mLogPicClickListener.onLogPicClick(orderLog, selectedUrl, position);
                    }
                }
            });
            gvImage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gvImage.setAdapter(mPicAdapter);

            CNViewClickUtil.setNoFastClickListener(ivWorkerHeader, this);
            CNViewClickUtil.setNoFastClickListener(tvWorkerMobile, this);

            sgStarView = findView(R.id.sgStarView);
            sgStarView.setIsClicStar(false);
            // 设置星未评分的图标
            sgStarView.setStarOffResId(R.drawable.cn_icon_star_off);
            // 设置星已评分的图标
            sgStarView.setStarOnResId(R.drawable.cn_icon_star_on);
            // 宽高，为0表示WRAP_CONTENT
            sgStarView.initStar(0, 0);
        }

        @Override
        public void setValue(int position, OrderLog orderLog) {
            mPosition = position;

            llWorkerInfo.setVisibility(View.GONE);
            tvOrderLogTitle.setText("");
            tvUserNameForEvaluate.setText("");
            tvEvaluateMsg.setText("");
            tvOderLogDate.setText("");
            mPicAdapter.clear();
            tvWorkerName.setText("");
            tvWorkerMobile.setText("");

            String createTime;
            if (null != orderLog.getCreateTime()) {
                createTime = DateUtil.format(DateUtil.YYYY_P_MM_P_DD_HHMMSS_FORMAT, orderLog.getCreateTime().getTime());
                tvOderLogDate.setText(createTime);
            }

            tvOrderLogTitle.setText(orderLog.getContent());

            String action = orderLog.getAction().toUpperCase();
            Staff worker = (null == orderLog.getWorker() ? new Staff() : orderLog.getWorker());
            if (action.equals(LogAction.ORDER.name().toUpperCase())) { // 用户下单

                llEvaluate.setVisibility(View.GONE);
                sgStarView.setVisibility(View.GONE);
                tvEvaluateMsg.setVisibility(View.GONE);
                llServiceImg.setVisibility(View.GONE);
            } else if (action.equals(LogAction.ASSIGN.name().toUpperCase()) // 指派
                    || action.equals(LogAction.REASSIGN.name().toUpperCase())) { // 重新指派
                llWorkerInfo.setVisibility(View.VISIBLE);
                tvWorkerName.setText(worker.getName());
                tvWorkerMobile.setText(worker.getMobile());

                String url = worker.getAvatar();
                // 头像缩略图
                String thumUrl = url + CNCarWashingLogic.THUM_IMAGE_TYPE120x120x120;
                ImageLoader.getInstance().displayImage(thumUrl, ivWorkerHeader, mOptions);

                llEvaluate.setVisibility(View.GONE);
                sgStarView.setVisibility(View.GONE);
                tvEvaluateMsg.setVisibility(View.GONE);
                llServiceImg.setVisibility(View.GONE);
            } else if (action.equals(LogAction.START.name().toUpperCase()) // 开始服务
                    || action.equals(LogAction.FINISH.name().toUpperCase())) { // 结束服务
                // 填充图片地址
                mPicAdapter.append(orderLog.getImages());
                mPicAdapter.setOrderLog(orderLog);

                llEvaluate.setVisibility(View.GONE);
                sgStarView.setVisibility(View.GONE);
                tvEvaluateMsg.setVisibility(View.GONE);
                llServiceImg.setVisibility(View.VISIBLE);

            } else if (action.equals(LogAction.CANCEL.name().toUpperCase())) { // 取消
                tvOrderLogTitle.setText(orderLog.getContent());
            } else if (action.equals(LogAction.CLOSE.name().toUpperCase())) { // 关闭
                tvOrderLogTitle.setText(orderLog.getContent());
            } else if (action.equals(LogAction.UNPAID_CANCEL.name().toUpperCase())) { // 关闭未支付订单
                llEvaluate.setVisibility(View.GONE);
                sgStarView.setVisibility(View.GONE);
                tvEvaluateMsg.setVisibility(View.GONE);
                llServiceImg.setVisibility(View.GONE);
            } else if ((CNStringUtil.isEmpty(action)
                    || action.equals(LogAction.REVIEW.name().toUpperCase()))
                    && mServiceOrder.getStatus() == OrderStatus.REVIEWED.value()) { // 已评价

                llEvaluate.setVisibility(View.VISIBLE);
                sgStarView.setVisibility(View.VISIBLE);

                tvUserNameForEvaluate.setText(mServiceOrder.getContact().getName());
                tvEvaluateMsg.setText(mServiceOrder.getReview().getContent());

                tvEvaluateMsg.setVisibility(View.VISIBLE);
                llServiceImg.setVisibility(View.GONE);
                // 设置获取到的用户评分等级
                int rating = mServiceOrder.getReview().getRank();
                sgStarView.setRating(rating);
            } else {
                // 其他日志直接显示
                llEvaluate.setVisibility(View.GONE);
                sgStarView.setVisibility(View.GONE);
                tvEvaluateMsg.setVisibility(View.GONE);
                llServiceImg.setVisibility(View.GONE);
            }

            // 最后一个item
            view_bottom_line.setVisibility(View.VISIBLE);
            if (mPosition == getCount() - Const.ONE) {
                view_bottom_line.setVisibility(View.GONE);
                viewBottom.setVisibility(View.VISIBLE);
            } else {
                viewBottom.setVisibility(View.GONE);
            }
        }
    }

    private OnLogPicClickListener mLogPicClickListener = null;

    public void setOnLogPicClickListener(OnLogPicClickListener l) {
        mLogPicClickListener = l;
    }

    public interface OnLogPicClickListener {
        void onLogPicClick(OrderLog orderLog, String selectedUrl, int selectedPosition);
    }
}
