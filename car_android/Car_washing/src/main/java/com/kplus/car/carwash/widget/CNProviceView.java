package com.kplus.car.carwash.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNResourcesUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Description
 * <br/><br/>Created by FU ZHIXUE on 2015/8/5.
 * <br/><br/>
 */
public class CNProviceView extends LinearLayout implements CNViewClickUtil.NoFastClickListener {

    protected static final String TAG = "CNProviceView";

    /**
     * 标识是单元格view
     */
    public static final String TAG_CELL_ITEM = "tag-cell-item";

    private static final int TOTAL_COL = 6;// 列
    private int total_row = 6; // 行

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private int mWidth = 0;
    private Point mPoint = null;

    private static String[] PROCIVES_ARR = {"京", "沪", "浙", "苏", "粤", "鲁", "晋", "冀", "豫",
            "川", "渝", "辽", "吉", "黑", "皖", "鄂", "湘", "赣", "闽", "陕", "甘", "宁",
            "蒙", "津", "贵", "云", "桂", "琼", "青", "新", "藏"};

    public CNProviceView(Context context) {
        this(context, null);
    }

    public CNProviceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPoint = CNPixelsUtil.getDeviceSize(mContext);
        mWidth = mPoint.x / TOTAL_COL;

        total_row = (int) CNCarWashingLogic.getTotalPageCount(TOTAL_COL, PROCIVES_ARR.length);

        initView();
    }

    private <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    private Map<Integer, CellView> mMap = null;

    private class CellView {
        public TextView mCellTime = null;
        public LinearLayout llCellItem = null;
    }

    private void initView() {
        this.removeAllViews();

        // 绘制tab内容的
        LinearLayout.LayoutParams tabRowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tabRowParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams tabColumnParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tabColumnParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams tabItemParams = new LinearLayout.LayoutParams(mWidth, LayoutParams.WRAP_CONTENT);

        LinearLayout llTabRow = new LinearLayout(mContext);
        llTabRow.setGravity(Gravity.CENTER_VERTICAL);
        llTabRow.setLayoutParams(tabRowParams);
        llTabRow.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams tabCellParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tabCellParams.gravity = Gravity.CENTER;

        mMap = new HashMap<>();
        int positionIndex = 0;

        for (int k = 0; k < total_row; k++) {
            LinearLayout llTabColumn = new LinearLayout(mContext);
            llTabColumn.setGravity(Gravity.CENTER_VERTICAL);
            llTabColumn.setLayoutParams(tabColumnParams);
            llTabColumn.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < TOTAL_COL; i++) {
                View view = mInflater.inflate(R.layout.cn_provice_item, null, false);
                LinearLayout llCellItem = findView(view, R.id.llCellItem);
                LinearLayout llProviceItem = findView(view, R.id.llProviceItem);
                llProviceItem.setLayoutParams(tabItemParams);

                LinearLayout llCell = findView(view, R.id.llCell);
                llCell.setLayoutParams(tabCellParams);

                TextView tvProviceItem = findView(view, R.id.tvProviceItem);

                View view_top_hor_line = findView(view, R.id.view_top_hor_line);
                View view_ver_line = findView(view, R.id.view_ver_line);

                // 将view放在map中，后面填充数据方便
                CellView cellView = new CellView();
                cellView.mCellTime = tvProviceItem;
                cellView.llCellItem = llCellItem;

                // 计算得到该view的位置
                llCellItem.setId(positionIndex);
                mMap.put(positionIndex, cellView);
                positionIndex++;
                CNViewClickUtil.setNoFastClickListener(llCellItem, this);
                llCellItem.setTag(TAG_CELL_ITEM);

                // 中间行不显示上面横线
                if (k == 0) {
                    view_top_hor_line.setVisibility(View.VISIBLE);
                } else {
                    view_top_hor_line.setVisibility(View.GONE);
                }

                // 如果是最后一列，不显示竖线
                if (i == TOTAL_COL - 1) {
                    view_ver_line.setVisibility(View.GONE);
                } else {
                    view_ver_line.setVisibility(View.VISIBLE);
                }
                llTabColumn.addView(view, i);
            }
            llTabRow.addView(llTabColumn, k);
        }
        this.addView(llTabRow);
    }

    public void setCellData(String strLicence, OnItemClickListener listener) {
        mListener = listener;

        for (int i = 0; i < PROCIVES_ARR.length; i++) {
            String provice = PROCIVES_ARR[i];

            CellView cellView = mMap.get(i);
            cellView.mCellTime.setText(provice);
            cellView.mCellTime.invalidate();

            if (provice.equals(strLicence)) {
                cellView.llCellItem.setSelected(true);
                cellView.llCellItem.setBackgroundColor(CNResourcesUtil.getColorResources(R.color.cn_oranger_color1));
            } else {
                cellView.llCellItem.setSelected(false);
                cellView.llCellItem.setBackgroundResource(R.drawable.cn_popup_item_selector);
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {
        if (null != v.getTag()) {
            String tag = (String) v.getTag();
            /**
             * 点击的是单元格
             */
            if (TAG_CELL_ITEM.equals(tag)) {
                int cellPosition = v.getId();
                if (PROCIVES_ARR.length <= cellPosition) {
                    return;
                }
                String provice = PROCIVES_ARR[cellPosition];
                if (null != mListener) {
                    mListener.onCellItemClick(provice);
                }
            }
        }
    }

    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onCellItemClick(String provice);
    }
}
