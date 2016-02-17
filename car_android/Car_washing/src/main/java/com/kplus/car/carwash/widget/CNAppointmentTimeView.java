package com.kplus.car.carwash.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.CNWeather;
import com.kplus.car.carwash.bean.ServingStatus;
import com.kplus.car.carwash.bean.ServingTime;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;
import com.kplus.car.carwash.utils.DateUtil;
import com.kplus.car.carwash.utils.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description 服务时间控件
 * <br/><br/>Created by Fu on 2015/5/13.
 * <br/><br/>
 */
public class CNAppointmentTimeView extends LinearLayout implements CNViewClickUtil.NoFastClickListener {
    private static final String TAG = "CNAppointmentTimeView";

    /**
     * 默认4列
     */
    private int mTabarColumn = 4;
    /**
     * 默认3行
     */
    private int mTabbarRow = 3;

    /**
     * 今天
     */
    public static final String TAG_TODAY = "tag-today";
    /**
     * 明天
     */
    public static final String TAG_TOMORROW = "tag-tomorrow";
    /**
     * 后天
     */
    public static final String TAG_AFTER_TOMORROW = "tag-after-tomorrow";
    /**
     * 两天后
     */
    public static final String TAG_TWO_DAY_AFTER = "tag-two-day-after";

    /**
     * 标识是单元格view
     */
    public static final String TAG_CELL_ITEM = "tag-cell-item";

    private Context mContext = null;

    private TextView[] mTabView = null;
    private TextView[] mTabWeatcher = null;
    private LinearLayout[] mTabContent = null;

    private OnServiceTimeItemClickListener mClickListener = null;

    private Point mPoint = null;
    private int mWidth = 0;

    private Map<Date, List<ServingStatus>> mListMap = null;
    private List<Date> mTabDate = null;
    private Map<Integer, CellView> mMap = null;
    private ScrollView mScrollView = null;
    private View mCurrentView = null;

    private LayoutInflater mInflater = null;

    private int mTabarHeight = 0;
    private int mCellHeight = 0;

    public CNAppointmentTimeView(Context context) {
        this(context, null);
    }

    public CNAppointmentTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mPoint = CNPixelsUtil.getDeviceSize(mContext);
        mWidth = mPoint.x / mTabarColumn;

        this.setBackgroundColor(Color.WHITE);
        mInflater = LayoutInflater.from(mContext);
    }

    public void clear() {
        this.removeAllViews();
        addEmptyData();
    }

    public void addEmptyData() {
        this.removeAllViews();
        int height = (int) (mPoint.y * CNCarWashingLogic.SERVING_TIME_ITEM_HEIGHT);
        height = height * 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        params.gravity = Gravity.CENTER;
        TextView tvEmpay = new TextView(mContext);
        tvEmpay.setLayoutParams(params);
        tvEmpay.setGravity(Gravity.CENTER);
        tvEmpay.setTextColor(mContext.getResources().getColor(R.color.cn_font_color));
        tvEmpay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        String msg = mContext.getResources().getString(R.string.cn_no_serving_time);
        tvEmpay.setText(msg);
        this.addView(tvEmpay);
    }

    public void setServiceTimeCellData(List<ServingStatus> datas, OnServiceTimeItemClickListener listener) {
        setItemClickListener(listener);
        if (null != datas && datas.size() > Const.NONE) {
            if (null == mListMap) {
                mListMap = new HashMap<>();
            }

            mTabDate = new ArrayList<>();
            List<ServingStatus> tempServingStatuses = new ArrayList<>();

            for (int i = 0; i < datas.size(); i++) {
                ServingStatus servingStatus = datas.get(i);
                ServingTime servingTime = servingStatus.getTime();
                // 新集合中是否有指定的元素，如果没有则加入
                if (!mTabDate.contains(servingTime.getDay())) {
                    mTabDate.add(servingTime.getDay());
                    tempServingStatuses = new ArrayList<>();
                }
                tempServingStatuses.add(servingStatus);
                mListMap.put(servingTime.getDay(), tempServingStatuses);
            }

            mTabarColumn = mTabDate.size();
            mWidth = mPoint.x / mTabarColumn;
            initView();

            // 填充今天的内容
            createTableFillContent(0);
        } else {
            addEmptyData();
        }
    }

    public void setWeather(List<CNWeather> weathers) {
        if (null == mTabWeatcher) {
            return;
        }
        if (null == weathers || weathers.isEmpty()) {
            for (TextView tv : mTabWeatcher) {
                tv.setVisibility(View.GONE);
            }
        } else {
            for (int i = 0; i < mTabWeatcher.length; i++) {
                TextView tv = mTabWeatcher[i];
                if (i <= weathers.size() - 1) {
                    CNWeather weather = weathers.get(i);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(weather.mPhenomenon);
                } else {
                    tv.setVisibility(View.INVISIBLE);
                    tv.setText("");
                }
            }
        }
    }

    /**
     * 创建表格并填充内容
     *
     * @param position
     */
    private void createTableFillContent(int position) {
        mTabbarRow = getColumns(position);
        setTabbarContent();
        setCellData(position);
    }

    /**
     * 获取生成多少行
     *
     * @param position
     * @return
     */
    private int getColumns(int position) {
        Date date = mTabDate.get(position);
        List<ServingStatus> servingStatuses = mListMap.get(date);
        return (int) CNCarWashingLogic.getTotalPageCount(mTabarColumn, servingStatuses.size());
    }

    public void setItemClickListener(OnServiceTimeItemClickListener listener) {
        mClickListener = listener;
    }

    private void initView() {
        this.removeAllViews();
        // 绘制tabbar
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams paramsTabContext = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTabContext.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams paramsTabWeather = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTabWeather.gravity = Gravity.CENTER;
        paramsTabWeather.topMargin = CNPixelsUtil.dip2px(mContext, 5);

        LinearLayout llTab = new LinearLayout(mContext);
        llTab.setGravity(Gravity.CENTER_VERTICAL);
        llTab.setLayoutParams(params);
        llTab.setOrientation(LinearLayout.HORIZONTAL);

        int height = (int) (mPoint.y * 0.13f);
        mTabarHeight = height;
//        params = new LinearLayout.LayoutParams(mWidth, height);
        params = new LinearLayout.LayoutParams(mWidth, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        Log.trace(TAG, "Tabar高度为：" + height);

        mTabView = new TextView[mTabarColumn];
        mTabWeatcher = new TextView[mTabarColumn];
        mTabContent = new LinearLayout[mTabarColumn];

        for (int i = 0; i < mTabarColumn; i++) {
            View view = mInflater.inflate(R.layout.cn_service_time_tab, null, false);
            view.setLayoutParams(params);

            LinearLayout llDayTabLayout = findView(view, R.id.llDayTabLayout);
            TextView tvDay = findView(view, R.id.tvDay);
            TextView tvWeather = findView(view, R.id.tvWeather);
            // 先不显示天气
            tvWeather.setVisibility(View.GONE);
            // 设置最小高度
            llDayTabLayout.setMinimumHeight(height);

            boolean isSelected = false;
            String text;
            Date date = mTabDate.get(i);
            long todayTimes = DateUtil.getTodayData();
            text = DateUtil.format(DateUtil.DAY_FORMAT_SHORT, todayTimes);
            String tempText = DateUtil.format(DateUtil.DAY_FORMAT_SHORT, date.getTime());
            if (text.equals(tempText)) {
                // 今天
                isSelected = true;
                mCurrentView = llDayTabLayout;
                text = mContext.getResources().getString(R.string.cn_today);
                // 第一个是选择的，使用白色背景
                llDayTabLayout.setTag(TAG_TODAY);
            } else {
                text = DateUtil.format(DateUtil.DAY_FORMAT_SHORT, date.getTime());
                switch (i) {
                    case 1: // 明天
                        llDayTabLayout.setTag(TAG_TOMORROW);
                        break;
                    case 2: // 后天
                        llDayTabLayout.setTag(TAG_AFTER_TOMORROW);
                        break;
                    case 3: // 两天后
                        llDayTabLayout.setTag(TAG_TWO_DAY_AFTER);
                        break;
                }
            }

            llDayTabLayout.setId(i);
            tvDay.setText(text);
            // 第一个是选择的，使用白色背景
            setTabViewColor(tvDay, tvWeather, llDayTabLayout, isSelected);

            CNViewClickUtil.setNoFastClickListener(llDayTabLayout, this);

            mTabView[i] = tvDay;
            mTabContent[i] = llDayTabLayout;
            mTabWeatcher[i] = tvWeather;

            llTab.addView(view, i);
        }
        this.addView(llTab, 0);
    }

    private void setTabViewColor(TextView view, TextView tvWeather, LinearLayout llContent, boolean isSelected) {
        int bgColor = R.drawable.layout_item_oranger_selctor;
        int textColor = R.color.cn_white;
        if (isSelected) {
            bgColor = R.drawable.layout_item_bg_selector;
            textColor = R.color.cn_black;
        }
        llContent.setBackgroundResource(bgColor);
        view.setTextColor(mContext.getResources().getColor(textColor));
        tvWeather.setTextColor(mContext.getResources().getColor(textColor));
    }

    /**
     * @param timeView      时间段
     * @param statuView     状态
     * @param isAppointment 是否可约
     */
    private void setCellViewColor(TextView timeView, TextView statuView, boolean isAppointment) {
        int color = R.color.cn_font_color;
        if (isAppointment) {
            color = R.color.cn_oranger_color;
        }
        timeView.setTextColor(mContext.getResources().getColor(color));
        statuView.setTextColor(mContext.getResources().getColor(color));
    }

    private class CellView {
        public TextView mCellTime = null;
        public TextView mCellStatu = null;
        public LinearLayout llCellItem = null;
    }

    /**
     * 生成内容表格
     */
    public void setTabbarContent() {
        if (null != mScrollView) {
            this.removeView(mScrollView);
        }
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

        int height = (int) (mPoint.y * CNCarWashingLogic.SERVING_TIME_ITEM_HEIGHT);
        mCellHeight = height * mTabbarRow;
        Log.trace(TAG, "Cell高度为：" + height);
        LinearLayout.LayoutParams tabCellParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        tabCellParams.gravity = Gravity.CENTER;

        mMap = new HashMap<>();
        int positionIndex = 0;

        for (int k = 0; k < mTabbarRow; k++) {
            LinearLayout llTabColumn = new LinearLayout(mContext);
            llTabColumn.setGravity(Gravity.CENTER_VERTICAL);
            llTabColumn.setLayoutParams(tabColumnParams);
            llTabColumn.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < mTabarColumn; i++) {
                View view = mInflater.inflate(R.layout.cn_service_time_content, null);
                LinearLayout llCellItem = findView(view, R.id.llCellItem);
                LinearLayout llServiceItem = findView(view, R.id.llServiceItem);
                llServiceItem.setLayoutParams(tabItemParams);

                LinearLayout llCell = findView(view, R.id.llCell);
                llCell.setLayoutParams(tabCellParams);

                TextView tvTimetItem = findView(view, R.id.tvTimetItem);
                TextView tvServiceStatuItem = findView(view, R.id.tvServiceStatuItem);

                View view_ver_line = findView(view, R.id.view_ver_line);

                // 将view放在map中，后面填充数据方便
                CellView cellView = new CellView();
                cellView.mCellTime = tvTimetItem;
                cellView.mCellStatu = tvServiceStatuItem;
                cellView.llCellItem = llCellItem;

                // 计算得到该view的位置
                llCellItem.setId(positionIndex);
                mMap.put(positionIndex, cellView);

                positionIndex++;
                llCellItem.setTag(TAG_CELL_ITEM);
                CNViewClickUtil.setNoFastClickListener(llCellItem, this);

                // 如果是最后一列，不显示竖线
                if (i == mTabarColumn - 1) {
                    view_ver_line.setVisibility(View.GONE);
                } else {
                    view_ver_line.setVisibility(View.VISIBLE);
                }
                llTabColumn.addView(view, i);
            }
            llTabRow.addView(llTabColumn, k);
        }

        mScrollView = new ScrollView(mContext);
        mScrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.addView(llTabRow);

        this.addView(mScrollView, 1);

        int popHeight = (mTabarHeight + mCellHeight);
        Log.trace(TAG, "时间POP的高度：" + popHeight);
        if (null != mClickListener) {
            mClickListener.onTimePopHeight(popHeight);
        }
    }

    protected <T extends View> T findView(View view, int id) {
        if (null == view) {
            throw new IllegalArgumentException("view is not null!");
        }
        return (T) view.findViewById(id);
    }

    @Override
    public void onNoFastClick(View v) {
        if (null != v.getTag()) {
            String tag = (String) v.getTag();
            /**
             * 点击日期切换
             */
            if (TAG_TODAY.equals(tag)
                    || TAG_TOMORROW.equals(tag)
                    || TAG_AFTER_TOMORROW.equals(tag)
                    || TAG_TWO_DAY_AFTER.equals(tag)) {
                int pos = v.getId();
                // 今天 明天 后天 两天后
                setSelectedTab(pos);

                createTableFillContent(pos);
            }
            /**
             * 点击的是单元格
             */
            else if (TAG_CELL_ITEM.equals(tag)) {
                int currPos = mCurrentView.getId();
                int cellPosition = v.getId();

                // 先取key
                Date date = mTabDate.get(currPos);
                // 取出值
                List<ServingStatus> servingStatuses = mListMap.get(date);
                // 再从值中取出点击的值
                ServingStatus servingStatus = servingStatuses.get(cellPosition);

                if (null != mClickListener) {
                    mClickListener.onItemClick(servingStatus);
                }
            }
        }
    }

    /**
     * 填充单元格的数据
     */
    private void setCellData(int position) {
        // 切换tab时，重新填充数据
        if (null != mListMap) {
            for (int i = 0; i < mMap.size(); i++) {
                CellView cellView = mMap.get(i);
                cellView.mCellStatu.setText("");
                cellView.mCellTime.setText("");
                cellView.llCellItem.setOnClickListener(null);
                cellView.llCellItem.setEnabled(false);
            }

            Date date = mTabDate.get(position);
            List<ServingStatus> servingStatuses = mListMap.get(date);
            for (int i = 0; i < servingStatuses.size(); i++) {
                ServingStatus servingStatus = servingStatuses.get(i);
                ServingTime servingTime = servingStatus.getTime();

                CellView cellView = mMap.get(i);
                if (null == servingTime) {
                    // 为空表示全天
                    cellView.mCellTime.setText("全天");
                } else {
                    String time = DateUtil.getServingTime(servingTime, false);
                    cellView.mCellTime.setText(time);
                }

                // 是否可预约
                boolean isAppointment;
                String statu;
                if (servingStatus.getStatus() == CNCarWashingLogic.SERVING_STATU_ENABLED) {
                    statu = "可预约";
                    isAppointment = true;
                    CNViewClickUtil.setNoFastClickListener(cellView.llCellItem, this);
                    cellView.llCellItem.setEnabled(true);
                } else {
                    statu = "预约满";
                    isAppointment = false;
                    cellView.llCellItem.setOnClickListener(null);
                    cellView.llCellItem.setEnabled(false);
                }
                cellView.mCellStatu.setText(statu);
                setCellViewColor(cellView.mCellTime, cellView.mCellStatu, isAppointment);
                cellView.mCellTime.invalidate();
                cellView.mCellStatu.invalidate();
            }
        }

        if (null != mScrollView) {
            mScrollView.smoothScrollTo(0, 0);
        }
    }

    private void setSelectedTab(int position) {
        if (null != mCurrentView && mCurrentView.getId() == position) {
            return;
        }
        for (int i = 0; i < mTabView.length; i++) {
            TextView view = mTabView[i];
            TextView viewWeather = mTabWeatcher[i];
            LinearLayout viewContent = mTabContent[i];
            if (i == position) {
                mCurrentView = viewContent;
                setTabViewColor(view, viewWeather, viewContent, true);
            } else {
                setTabViewColor(view, viewWeather, viewContent, false);
            }
        }
    }

    public interface OnServiceTimeItemClickListener {
        void onItemClick(ServingStatus servingStatus);

        void onTimePopHeight(int popHeight);
    }
}
