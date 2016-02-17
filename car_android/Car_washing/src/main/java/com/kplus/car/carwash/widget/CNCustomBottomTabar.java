package com.kplus.car.carwash.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.utils.CNPixelsUtil;
import com.kplus.car.carwash.utils.CNStringUtil;
import com.kplus.car.carwash.utils.CNViewClickUtil;

/**
 * 自定义的主界面下面的tabar
 * Created by Fu on 2015/5/5.
 */
public class CNCustomBottomTabar extends LinearLayout implements CNViewClickUtil.NoFastClickListener {

    private static final String TAG_HOME_TAB = "tag-home";
    private static final String TAG_ORDER_TAB = "tag-order";
    private static final String TAG_INFO_TAB = "tag-info";

    private int tabarCount = 3;
    private int tabarHeight = Const.NONE;

    private Context mContext = null;

    private ImageView[] mTabIcons = null;
    private TextView[] mTabTitles = null;

    private int[] mIconIds_nor = null;
    private int[] mIconIds_press = null;
    private int[] mTitles = null;

    public void setIconIds_nor(int[] iconIds_nor) {
        mIconIds_nor = iconIds_nor;
    }

    public void setIconIds_press(int[] iconIds_press) {
        mIconIds_press = iconIds_press;
    }

    public void setTitles(int[] titles) {
        mTitles = titles;
    }

    public interface CNCustomTabarCallback {
        void onClickSelectedItem(int position);

        void getTabCount(int tabarCount, int[] titles);
    }

    private CNCustomTabarCallback mCallback = null;

    public void setCustomTabarCallback(CNCustomTabarCallback callback) {
        this.mCallback = callback;
    }

    public CNCustomBottomTabar(Context context) {
        this(context, null, Const.NONE);
    }

    public CNCustomBottomTabar(Context context, AttributeSet attrs) {
        this(context, attrs, Const.NONE);
    }

    public CNCustomBottomTabar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.cn_customTabarMenu);
        //读取按钮数量
        tabarCount = a.getInt(R.styleable.cn_customTabarMenu_tabarCount, 3);
        //读取按钮的高度
        tabarHeight = (int) a.getDimension(R.styleable.cn_customTabarMenu_tabarHeight, 30);
        a.recycle();
    }

    /**
     * 填充底部tabar
     */
    public void initTabar() {
        mTabIcons = new ImageView[tabarCount];
        mTabTitles = new TextView[tabarCount];

        this.removeAllViews();

        float density = getResources().getDisplayMetrics().density;
        Point point = CNPixelsUtil.getDeviceSize(mContext);

        int w = point.x / tabarCount;
        float scale = density;
        int h = (int) (50 * scale + 0.5f);

        LayoutParams params = new LayoutParams(w, h, 1);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = Const.NONE; i < tabarCount; i++) {
            View tabView = inflater.inflate(R.layout.cn_main_tab_item, null);
            tabView.setLayoutParams(params);
            tabView.setId(i);

            ImageView ivIcon = findView(tabView, R.id.ivTabIcon);
            mTabIcons[i] = ivIcon;

            TextView tvTitle = findView(tabView, R.id.tvTabTitle);
            mTabTitles[i] = tvTitle;

            CNViewClickUtil.setNoFastClickListener(tabView, this);
            CNViewClickUtil.setNoFastClickListener(ivIcon, this);
            CNViewClickUtil.setNoFastClickListener(tvTitle, this);

            switch (i) {
                case Const.NONE: // 首页
                    tabView.setTag(TAG_HOME_TAB);
                    ivIcon.setTag(TAG_HOME_TAB);
                    tvTitle.setTag(TAG_HOME_TAB);
                    break;
                case Const.ONE: // 订单
                    tabView.setTag(TAG_ORDER_TAB);
                    ivIcon.setTag(TAG_ORDER_TAB);
                    tvTitle.setTag(TAG_ORDER_TAB);
                    break;
                case Const.TWO: // 我的
                    tabView.setTag(TAG_INFO_TAB);
                    ivIcon.setTag(TAG_INFO_TAB);
                    tvTitle.setTag(TAG_INFO_TAB);
                    break;
            }

            mTabIcons[i].setImageResource(mIconIds_nor[i]);
            mTabTitles[i].setText(getResources().getString(mTitles[i]));

            this.addView(tabView, i);
        }

        if (null != mCallback) {
            mCallback.getTabCount(tabarCount, mTitles);
        }
    }

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    @Override
    public void onNoFastClick(View v) {
        if (CNStringUtil.isNotEmpty(v.getTag())) {
            String strTag = v.getTag().toString();
            int index = Const.NEGATIVE;
            resetTabView();
            if (TAG_HOME_TAB.equals(strTag)) {
                // 首页
                index = Const.NONE;
            } else if (TAG_ORDER_TAB.equals(strTag)) {
                // 订单
                index = Const.ONE;
            } else if (TAG_INFO_TAB.equals(strTag)) {
                // 我的
                index = Const.TWO;
            }
            setSelectedTabView(index);
            if (null != mCallback) {
                mCallback.onClickSelectedItem(index);
            }
        }
    }

    /**
     * 重置tab状态
     */
    public void resetTabView() {
        if (null == mTabTitles || null == mTabIcons)
            return;

        for (int i = Const.NONE; i < mTabTitles.length; i++) {
            mTabTitles[i].setTextColor(getResources().getColor(R.color.cn_font_color));
            mTabIcons[i].setImageResource(mIconIds_nor[i]);
        }
    }

    /**
     * 设置tab选中的状态
     *
     * @param position position
     */
    public void setSelectedTabView(int position) {
        if (null == mTabTitles || null == mTabIcons)
            return;
        if (mTabTitles.length < position || mTabIcons.length < position)
            return;

        mTabTitles[position].setTextColor(getResources().getColor(R.color.cn_orangered5));
        mTabIcons[position].setImageResource(mIconIds_press[position]);
    }
}
