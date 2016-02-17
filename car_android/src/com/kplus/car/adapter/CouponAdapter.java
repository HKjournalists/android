package com.kplus.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.R;
import com.kplus.car.model.Coupon;

import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public class CouponAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Coupon> mListCoupon;
    private int orange, black, dark_gray, white_smoke;

    public CouponAdapter(Context context){
        mContext = context;
        orange = mContext.getResources().getColor(R.color.daze_orangered5);
        black = mContext.getResources().getColor(R.color.daze_black2);
        dark_gray = mContext.getResources().getColor(R.color.daze_darkgrey9);
        white_smoke = mContext.getResources().getColor(R.color.daze_white_smoke2);
    }

    public void setCouponList(List<Coupon> list){
        mListCoupon = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_coupon, parent, false);
        return new CouponViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Coupon coupon = mListCoupon.get(position);
        CouponViewHolder vh = (CouponViewHolder) holder;
        vh.mAmount.setText(String.valueOf(coupon.getAmount() != null ? coupon.getAmount() : 0));
        vh.mName.setText(coupon.getName());
        String beginTime = coupon.getBeginTime().replace('-', '.');;
        if (beginTime.indexOf(' ') != -1)
            beginTime = beginTime.substring(0, beginTime.indexOf(' '));
        String endTime = coupon.getEndTime().replace('-', '.');
        if (endTime.indexOf(' ') != -1)
            endTime = endTime.substring(0, endTime.indexOf(' '));
        vh.mTime.setText(beginTime + " - " + endTime);
        vh.mInfo.setText(coupon.getInfo());
        if (coupon.getStatus() != null){
            if (coupon.getStatus() == 0){
                //未使用
                vh.mAmountUnit.setTextColor(orange);
                vh.mAmount.setTextColor(orange);
                vh.mStatus.setText("未使用");
                vh.mStatus.setBackgroundColor(orange);
                vh.mName.setTextColor(black);
                vh.mTime.setTextColor(dark_gray);
                vh.mInfo.setTextColor(dark_gray);
            }
            else if (coupon.getStatus() == -1){
                //已过期
                vh.mAmountUnit.setTextColor(white_smoke);
                vh.mAmount.setTextColor(white_smoke);
                vh.mStatus.setText("已过期");
                vh.mStatus.setBackgroundColor(white_smoke);
                vh.mName.setTextColor(white_smoke);
                vh.mTime.setTextColor(white_smoke);
                vh.mInfo.setTextColor(white_smoke);
            }
            else {
                //已使用
                vh.mAmountUnit.setTextColor(dark_gray);
                vh.mAmount.setTextColor(dark_gray);
                vh.mStatus.setText("已使用");
                vh.mStatus.setBackgroundColor(dark_gray);
                vh.mName.setTextColor(dark_gray);
                vh.mTime.setTextColor(white_smoke);
                vh.mInfo.setTextColor(white_smoke);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListCoupon != null ? mListCoupon.size() : 0;
    }
}
