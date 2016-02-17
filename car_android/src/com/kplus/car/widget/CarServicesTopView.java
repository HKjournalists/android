package com.kplus.car.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.carwash.module.activites.CNCarWashingLogic;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.model.CarService;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.ServicesActionUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：汽车服务上面的8个服务
 * <br/><br/>Created by FU ZHIXUE on 2015/8/10.
 * <br/><br/>
 */
public class CarServicesTopView extends LinearLayout implements ClickUtils.NoFastClickListener {

    protected static final String TAG = "CarServicesTopView";

    private static final int MAX_TOTAL_COL = 4;
    private static final int MAX_TOTAL_ROW = 2;

    private static final int MAX_CELL = MAX_TOTAL_COL * MAX_TOTAL_ROW;

    private int total_col = MAX_TOTAL_COL;// 列
    private int total_row = MAX_TOTAL_ROW; // 行

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private int mWidth = 0;
    private Point mPoint = null;
    private final int mMargin;

    private final DisplayImageOptions mOptions;
    private final KplusApplication mApp;

    public CarServicesTopView(Context context) {
        this(context, null);
    }

    public CarServicesTopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPoint = CNPixelsUtil.getDeviceSize(mContext);
        mMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.services_margin);

        mApp = KplusApplication.getInstance();

        // ImageLoader参数
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(0)
                .showImageForEmptyUri(0)
                .showImageOnFail(0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    private <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    private Map<Integer, CellView> mMap = null;

    private class CellView {
        public ImageView mServiceIcon = null;
        public ImageView mIconFavorableTag = null;
        public TextView mCellTitle = null;
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

        mMap = new HashMap<>();
        int positionIndex = 0;

        for (int k = 0; k < total_row; k++) {
            LinearLayout llTabColumn = new LinearLayout(mContext);
            llTabColumn.setGravity(Gravity.CENTER);
            llTabColumn.setLayoutParams(tabColumnParams);
            llTabColumn.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < total_col; i++) {
                View view = mInflater.inflate(R.layout.view_service_top_item, this, false);
                LinearLayout llCellItem = findView(view, R.id.llCellItem);

                LinearLayout llItem = findView(view, R.id.llItem);

                tabItemParams = (LayoutParams) llItem.getLayoutParams();
                tabItemParams.width = mWidth;
                llItem.setLayoutParams(tabItemParams);

                ImageView ivServiceIcon = findView(view, R.id.ivServiceIcon);
                ImageView ivIconFavorableTag = findView(view, R.id.ivIconFavorableTag);
                TextView tvServiceName = findView(view, R.id.tvServiceName);

                // 将view放在map中，后面填充数据方便
                CellView cellView = new CellView();
                cellView.mServiceIcon = ivServiceIcon;
                cellView.mIconFavorableTag = ivIconFavorableTag;
                cellView.mCellTitle = tvServiceName;
                cellView.llCellItem = llCellItem;

                // 计算得到该view的位置
                mMap.put(positionIndex, cellView);
                llCellItem.setTag(positionIndex);
                ClickUtils.setNoFastClickListener(llCellItem, this);
                positionIndex++;

                llTabColumn.addView(view, i);
            }
            llTabRow.addView(llTabColumn, k);
        }
        this.addView(llTabRow);
    }

    private List<CarService> mDatas = null;

    /**
     * 如果出现单数把"我的订单"加上
     *
     * @param datas 快捷入口数据
     * @return 快捷入口数据
     */
    private List<CarService> setSingleTopService(List<CarService> datas) {
        if (null == datas || datas.isEmpty()) {
            return datas;
        }
        // 计算是否出现单数
        int size = datas.size();
        if (size <= 8 && size % 2 != 0) {
            CarService carService = new CarService();

            carService.setName("我的订单");
            carService.setTitle("我的订单");
            carService.setInfo("");
            carService.setFlag(0);
            carService.setFavorableTag("0");
            carService.setListIcon("");
            carService.setLinkIcon("");
            carService.setTabIcon("");
            carService.setMotionType(ServicesActionUtil.MOTION_TYPE_NATIVE);
            carService.setMotionValue(ServicesActionUtil.MOTION_VALUE_ORDERLIST);
            carService.setSort(8);

            datas.add(carService);
        }
        return datas;
    }

    /**
     * 计算行和列
     */
    private void computeRowAndCol() {
        int size = mDatas.size();

        if (size > MAX_TOTAL_COL) {
            if (size > MAX_CELL) {
                size = MAX_CELL;
            }
            // 如果是6个按三列二行排
            if (size % 3 == 0) {
                total_col = 3;
            } else {
                total_col = MAX_TOTAL_COL;
            }
        } else {
            total_col = size;
        }
        total_row = (int) CNCarWashingLogic.getTotalPageCount(total_col, size);
        // 计算宽度
        mWidth = (mPoint.x - (mMargin + mMargin)) / total_col;

        initView();
    }

    /**
     * 填充数据
     *
     * @param datas    数据
     * @param listener 回调
     */
    public void setCellData(final List<CarService> datas, OnItemClickListener listener) {
        mListener = listener;
        if (null == datas || datas.isEmpty()) {
            return;
        }

        // 计算如果是单数把"我的订单"加上
        mDatas = setSingleTopService(datas);

        computeRowAndCol();

        for (int i = 0; i < mDatas.size(); i++) {
            if (i >= MAX_CELL) {
                break;
            }
            CarService service = mDatas.get(i);

            if (null == service) {
                continue;
            }

            CellView cellView = mMap.get(i);
            if (null != cellView) {
                if (!TextUtils.isEmpty(service.getName())) {
                    cellView.mCellTitle.setText(service.getTitle());
                    cellView.mCellTitle.invalidate();
                }

                // 设置显示优惠标签
                String favorableTag = service.getFavorableTag();
                if (TextUtils.isEmpty(favorableTag)) {
                    cellView.mIconFavorableTag.setVisibility(View.GONE);
                } else {
                    cellView.mIconFavorableTag.setVisibility(View.VISIBLE);
                    mApp.imageLoader.displayImage(favorableTag, cellView.mIconFavorableTag, mOptions);
                }

                String motionType = service.getMotionType();
                String motionValue = service.getMotionValue();
                if (ServicesActionUtil.MOTION_TYPE_NATIVE.equalsIgnoreCase(motionType)
                        && ServicesActionUtil.MOTION_VALUE_ORDERLIST.equalsIgnoreCase(motionValue)) {
                    // 设置我的订单图标
                    cellView.mServiceIcon.setImageResource(R.drawable.icon_mine_order);
                } else {
                    String url = service.getLinkIcon();
                    mApp.imageLoader.displayImage(url, cellView.mServiceIcon, mOptions);
                }
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {

        if (v.getId() == R.id.llCellItem) {
            int cellPosition = (int) v.getTag();
            if (null == mDatas || mDatas.isEmpty() || mDatas.size() <= cellPosition) {
                return;
            }
            CarService carService = mDatas.get(cellPosition);
            if (null != mListener) {
                mListener.onCellItemClick(carService);
            }
        }
    }

    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onCellItemClick(CarService carService);
    }
}
