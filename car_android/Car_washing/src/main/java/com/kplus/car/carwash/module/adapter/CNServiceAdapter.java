package com.kplus.car.carwash.module.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.OnSiteService;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.utils.CNImageUtils;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNResourcesUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description：服务列表适配器
 * <br/><br/>Created by Fu on 2015/5/11.
 * <br/><br/>
 */
public class CNServiceAdapter extends RecyclerView.Adapter<CNServiceAdapter.WashingViewHolder> {
    private static final String TAG = "CNServiceAdapter";

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private List<OnSiteService> mDatas = null;
    private OnListItemClickListener mClickListener = null;
    private final Point mPoint;
    private final DisplayImageOptions mOptions;

    public CNServiceAdapter(Context context, List<OnSiteService> data, OnListItemClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDatas = (null == data ? new ArrayList<OnSiteService>() : data);
        mClickListener = listener;
        mPoint = CNPixelsUtil.getDeviceSize(mContext);
        int resId = R.drawable.cn_service_loading_bg;
        mOptions = CNImageUtils.getImageOptions(resId, resId, resId);
    }

    public void clear() {
        if (null != mDatas) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public void appendData(List<OnSiteService> data) {
        if (null == mDatas) {
            mDatas = new ArrayList<>();
        }
        if (null != data && data.size() > Const.NONE) {
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public OnSiteService getItem(int position) {
        if (null != mDatas && mDatas.size() > position) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public WashingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cn_services_item, parent, false);
        return new WashingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WashingViewHolder holder, int position) {
        OnSiteService siteService = getItem(position);
        holder.setValue(position, siteService);
    }

    @Override
    public int getItemCount() {
        return (null == mDatas ? Const.NONE : mDatas.size());
    }

    class WashingViewHolder extends RecyclerView.ViewHolder implements CNViewClickUtil.NoFastClickListener {

        private int mPosition = Const.NEGATIVE;

        private final RelativeLayout rlService;
        private final ImageView ivServiceImg;
        private final TextView tvServiceName;
        private final TextView tvYuanUnit;
        private final TextView tvServicePrice;
        private final Button btnServiceDetails;

        public WashingViewHolder(View view) {
            super(view);

            rlService = findView(view, R.id.rlService);
            tvServiceName = findView(view, R.id.tvServiceName);
            ivServiceImg = findView(view, R.id.ivServiceImg);
            tvYuanUnit = findView(view, R.id.tvYuanUnit);
            tvServicePrice = findView(view, R.id.tvServicePrice);
            btnServiceDetails = findView(view, R.id.btnServiceDetails);

            CNViewClickUtil.setNoFastClickListener(rlService, this);
            CNViewClickUtil.setNoFastClickListener(btnServiceDetails, this);

            int imgHeight = (int) (mPoint.x * 0.4f); // 0.4 = 432 / 1080
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlService.getLayoutParams();
            params.height = imgHeight;
            rlService.setLayoutParams(params);
        }

        public void setValue(int position, OnSiteService siteService) {
            mPosition = position;

            tvServiceName.setText(siteService.getName());

            BigDecimal bdPrice = siteService.getPrice();
            if (null == bdPrice) {
                bdPrice = new BigDecimal(0);
            }
            String price = String.valueOf(bdPrice);
            tvServicePrice.setText(price);

            String url = siteService.getPoster();
            ImageLoader.getInstance().displayImage(url, ivServiceImg, mOptions);

            // 设置最大宽度
            int textWidth = CNStringUtil.getTextWidth(tvYuanUnit, CNResourcesUtil.getStringResources(R.string.cn_yuan_unit));
            textWidth += CNStringUtil.getTextWidth(tvServicePrice, price);

            int width = textWidth + btnServiceDetails.getLayoutParams().width;
            width = mPoint.x - (width + CNPixelsUtil.dip2px(mContext, 36));
            tvServiceName.setMaxWidth(width);
        }

        protected <T extends View> T findView(View parent, int id) {
            if (null == parent) {
                throw new NullPointerException("parent is not null!");
            }
            return (T) parent.findViewById(id);
        }

        @Override
        public void onNoFastClick(View v) {
            if (null != mClickListener) {
                mClickListener.onClickItem(mPosition, v);
            }
        }
    }
}
