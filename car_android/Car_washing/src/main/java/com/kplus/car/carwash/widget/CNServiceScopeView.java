package com.kplus.car.carwash.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.Region;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;

import java.util.List;

/**
 * Created by Fu on 2015/5/18.
 */
public class CNServiceScopeView extends LinearLayout implements CNViewClickUtil.NoFastClickListener {
    private static final String TAG_SERVICE_AREAS = "tag-service-areas";

    private Context mContext = null;

    private List<Region> mAreas = null;
    private Button[] btnAreas = null;
    private Point mPoint = null;

    public CNServiceScopeView(Context context) {
        this(context, null);
    }

    public CNServiceScopeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(LinearLayout.HORIZONTAL);
        mContext = context;
        mPoint = CNPixelsUtil.getDeviceSize(mContext);
    }

    public void initView(List<Region> areas) {
        this.removeAllViews();
        mAreas = areas;
        if (null != areas) {
            String btnText;
            int size = areas.size();
            btnAreas = new Button[size];

            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.leftMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.cn_service_arearsMarginleft);
            int padding = mContext.getResources().getDimensionPixelOffset(R.dimen.cn_service_arearsPadding);
            int paddingLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.cn_service_arearsPaddingLeft);
            for (int i = Const.NONE; i < size; i++) {
                // 最后一个设置右边距离
                if (i == size - Const.ONE) {
                    params.rightMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.cn_service_arearsMarginleft);
                }

                Button btnArea = new Button(mContext);
                btnArea.setLayoutParams(params);
                // 如果只有一个服务范围，按钮只显示服务范围
                btnText = areas.get(i).getName();
                btnArea.setText(btnText);
                btnArea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                btnArea.setBackgroundResource(R.drawable.btn_car_loc_unselected_selector);
                btnArea.setTextColor(mContext.getResources().getColor(R.color.cn_oranger_color));
                btnArea.setTag(TAG_SERVICE_AREAS);
                btnArea.setId((int) areas.get(i).getId()); // 用于标识点击的是第几个按钮
                btnArea.setPadding(paddingLeft, padding, paddingLeft, padding);
                CNViewClickUtil.setNoFastClickListener(btnArea, this);

                btnAreas[i] = btnArea;

                this.addView(btnArea, i);
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {
        if (null != v.getTag()
                && TAG_SERVICE_AREAS.equals(v.getTag())) {
            int selectedId = v.getId();
            setSelectedTab(selectedId);
            if (null != mChangedListener) {
                // 获取数据
                Region tempRegion = null;
                for (Region region : mAreas) {
                    if (selectedId == region.getId()) {
                        tempRegion = region;
                        break;
                    }
                }
                mChangedListener.onServiceAreasChanged(tempRegion);
            }
        }
    }

    public void setSelectedTab(int selectedId) {
        for (Button btnArea : btnAreas) {
            if (selectedId == btnArea.getId()) {
                // 表示选择的那个范围
                btnArea.setBackgroundResource(R.drawable.btn_car_loc_selected_selector);
                btnArea.setTextColor(mContext.getResources().getColor(R.color.cn_white));
            } else {
                btnArea.setBackgroundResource(R.drawable.btn_car_loc_unselected_selector);
                btnArea.setTextColor(mContext.getResources().getColor(R.color.cn_oranger_color));
            }
        }
    }

    private OnServiceAreasChangedListener mChangedListener = null;

    public void setOnServiceAreasChangedListener(OnServiceAreasChangedListener listener) {
        mChangedListener = listener;
    }

    public interface OnServiceAreasChangedListener {
        void onServiceAreasChanged(Region area);
    }
}
