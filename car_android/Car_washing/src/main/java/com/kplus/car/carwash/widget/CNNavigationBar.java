package com.kplus.car.carwash.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.utils.CNViewClickUtil;

/**
 * Created by Fu on 2015/5/6.
 */
public class CNNavigationBar implements CNViewClickUtil.NoFastClickListener, Toolbar.OnMenuItemClickListener {

    private static int SET_IMG_AT_RIGHT_OF_TEXT = 1;

    private Activity mActivity = null;

    private Toolbar mToolbar = null;
    private SlidingTabLayout stlNavSlidingTabsLayout = null;

    private TextView tvNavLeft = null;
    private TextView tvNavRight = null;
    private LinearLayout llNavCenterTitle = null;
    private ImageView ivNavTitleLeftImg = null;
    private TextView tvNavTitle = null;
    private ImageView ivNavTitleRightImg = null;


    public CNNavigationBar(Activity activity, Toolbar toolbar) {
        mActivity = activity;
        this.mToolbar = toolbar;

        AppCompatActivity appCompatActivity = ((AppCompatActivity) mActivity);
        appCompatActivity.setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        tvNavLeft = findView(R.id.tvNavLeft);
        tvNavRight = findView(R.id.tvNavRight);
        llNavCenterTitle = findView(R.id.llNavCenterTitle);
        stlNavSlidingTabsLayout = findView(R.id.stlNavSlidingTabsLayout);
        ivNavTitleLeftImg = findView(R.id.ivNavTitleLeftImg);
        tvNavTitle = findView(R.id.tvNavTitle);
        ivNavTitleRightImg = findView(R.id.ivNavTitleRightImg);

        CNViewClickUtil.setNoFastClickListener(tvNavLeft, this);
        CNViewClickUtil.setNoFastClickListener(tvNavRight, this);
        CNViewClickUtil.setNoFastClickListener(llNavCenterTitle, this);
    }

    protected <T extends View> T findView(int id) {
        return (T) mActivity.findViewById(id);
    }

    protected ActionBar getSupportActionBar() {
        return ((AppCompatActivity) mActivity).getSupportActionBar();
    }

    public void setNavTitle(int resId) {
        setNavTitle(resId, Const.NONE, Const.NONE);
    }

    public void setNavTitle(String title) {
        setNavTitle(title, Const.NONE, Const.NONE);
    }

    public void setNavTitle(int resId, int drawId, int direction) {
        String text = mActivity.getResources().getString(resId);
        setNavTitle(text, drawId, direction);
    }

    public void setNavTitle(String text, int drawId, int direction) {
        setNavtitleVisible();
        tvNavTitle.setVisibility(View.VISIBLE);
        tvNavTitle.setText(text);
        setNavTitleDrawable(tvNavTitle, drawId, direction);
    }

    private void setNavtitleVisible() {
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        llNavCenterTitle.setLayoutParams(params);
        llNavCenterTitle.setVisibility(View.VISIBLE);
    }

    private void setNavTitleDrawable(TextView tv, int drawId, int direction) {
        Drawable drawable = null;
        if (drawId != Const.NONE) {
            drawable = mActivity.getResources().getDrawable(drawId);
            int width = drawable.getMinimumWidth();
            int height = drawable.getMinimumHeight();
            drawable.setBounds(0, 0, width, height);
        }

        if (null != drawable) {
            if (direction == SET_IMG_AT_RIGHT_OF_TEXT) {
                tv.setCompoundDrawables(drawable, null, null, null);
            } else {
                tv.setCompoundDrawables(null, null, drawable, null);
            }
        }
    }

    private void setNavBtnDrawable(TextView tv, int drawId, int direction) {
        Drawable drawable = null;
        if (drawId != Const.NONE) {
            drawable = mActivity.getResources().getDrawable(drawId);
            int width = drawable.getMinimumWidth();
            int height = drawable.getMinimumHeight();
            drawable.setBounds(0, 0, width, height);
        }

        if (null != drawable) {
            if (direction == SET_IMG_AT_RIGHT_OF_TEXT) {
                tv.setCompoundDrawables(null, null, drawable, null);
            } else {
                tv.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    public void setToolbarVisible(int visible) {
        mToolbar.setVisibility(visible);
    }

    /**
     * 设置标题图片，在title的右边
     *
     * @param resId
     */
    public void setTitleRightImg(int resId) {
        setNavtitleVisible();
        ivNavTitleRightImg.setVisibility(View.VISIBLE);
        ivNavTitleRightImg.setImageResource(resId);
    }

    /**
     * 设置标题图片，在title的左边
     *
     * @param resId
     */
    public void setTitleLeftImg(int resId) {
        setNavtitleVisible();
        ivNavTitleLeftImg.setVisibility(View.VISIBLE);
        ivNavTitleLeftImg.setImageResource(resId);
    }

    private View mCustomView = null;

    /**
     * 中间自定义view
     *
     * @param view
     */
    public void setCenterCustomView(View view) {
        mCustomView = view;
        llNavCenterTitle.setVisibility(View.VISIBLE);
        llNavCenterTitle.addView(view);
    }

    public void setLeftBtn(int resId) {
        setLeftBtn(resId, Const.NONE, Const.NONE);
    }

    public void setLeftBtn(String text) {
        setLeftBtn(text, Const.NONE, Const.NONE);
    }

    public void setLeftBtn(int resId, int drawId, int direction) {
        String text = "";
        if (resId != Const.NONE) {
            text = mActivity.getResources().getString(resId);
        }
        setLeftBtn(text, drawId, direction);
    }

    public void setLeftBtn(String text, int drawId, int direction) {
        tvNavLeft.setVisibility(View.VISIBLE);
        tvNavLeft.setText(text);
        setNavBtnDrawable(tvNavLeft, drawId, direction);
    }

    public void setRightBtn(int resId) {
        setRightBtn(resId, Const.NONE, Const.NONE);
    }

    public void setRightBtnImg(int drawId) {
        setRightBtn(Const.NONE, drawId, Const.NONE);
    }

    public void setRightBtn(String text) {
        setRightBtn(text, Const.NONE, Const.NONE);
    }

    public void setRightBtn(int resId, int drawId, int direction) {
        String text = "";
        if (resId != Const.NONE) {
            text = mActivity.getResources().getString(resId);
        }
        setRightBtn(text, drawId, direction);
    }

    public void setRightBtn(String text, int drawId, int direction) {
        setRightBtnVisibility(View.VISIBLE);
        tvNavRight.setText(text);
        setNavTitleDrawable(tvNavRight, drawId, direction);
    }

    public void setRightBtnVisibility(int visibility) {
        tvNavRight.setVisibility(visibility);
    }

    /**
     * 获取非toolbar默认item的view
     *
     * @param viewId
     * @return
     */
    public View getView(int viewId) {
        return findView(viewId);
    }

    /**
     * 设置tab
     *
     * @param viewPager
     * @param texSize
     * @param textColor
     * @param selectedColor
     * @param backgroundColor
     * @param widthDimenId
     */
    public void setTabs(ViewPager viewPager, int texSize, int textColor, int selectedColor, int backgroundColor, int widthDimenId) {
        stlNavSlidingTabsLayout.setVisibility(View.VISIBLE);

        stlNavSlidingTabsLayout.setDistributeEvenly(true);
        stlNavSlidingTabsLayout.setSelectedIndicatorColors(mActivity.getResources().getColor(R.color.cn_orangered5));
        stlNavSlidingTabsLayout.setSelectedIndicatorColors(Color.GREEN);
        stlNavSlidingTabsLayout.setTextStyle(texSize, mActivity.getResources().getColor(textColor), mActivity.getResources().getColor(selectedColor));
        stlNavSlidingTabsLayout.setBackgroundColor(mActivity.getResources().getColor(backgroundColor));

        int width = mActivity.getResources().getDimensionPixelSize(widthDimenId);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(width, Toolbar.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        stlNavSlidingTabsLayout.setLayoutParams(params);

        stlNavSlidingTabsLayout.setViewPager(viewPager);
        stlNavSlidingTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.RED;
            }
        });
    }

    public void setTabUnlineColor(final int unlineColor) {
        stlNavSlidingTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return mActivity.getResources().getColor(unlineColor);
            }
        });
    }

    public void clear() {
        clearToolbarMenu();
        clearRightBtn();
        clearTitle();
        clearTabs();
        clearLeftBtn();
    }

    public void clearToolbarMenu() {
        mToolbar.getMenu().clear();
    }

    public void clearLeftBtn() {
        tvNavLeft.setText("");
        tvNavLeft.setCompoundDrawables(null, null, null, null);
        tvNavLeft.setVisibility(View.GONE);
    }

    public void clearRightBtn() {
        tvNavRight.setText("");
        tvNavRight.setCompoundDrawables(null, null, null, null);
        tvNavRight.setVisibility(View.GONE);
    }

    public void clearTitle() {
        tvNavTitle.setText("");
        tvNavTitle.setCompoundDrawables(null, null, null, null);

        ivNavTitleRightImg.setImageResource(0);
        ivNavTitleRightImg.setVisibility(View.GONE);

        ivNavTitleLeftImg.setImageResource(0);
        ivNavTitleLeftImg.setVisibility(View.GONE);

        if (null != mCustomView) {
            llNavCenterTitle.removeView(mCustomView);
            mCustomView = null;
        }
        llNavCenterTitle.setVisibility(View.GONE);
    }

    public void clearTabs() {
        stlNavSlidingTabsLayout.setVisibility(View.GONE);
    }

    @Override
    public void onNoFastClick(View v) {
        if (null != mCallback)
            mCallback.onViewClick(v.getId());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (null != mCallback)
            return mCallback.onViewClick(item.getItemId());
        return false;
    }

    private OnToolbarMenuItemClickListener mCallback = null;

    public void setMenuitemClickListener(OnToolbarMenuItemClickListener listener) {
        mCallback = listener;
    }

    public interface OnToolbarMenuItemClickListener {
        boolean onViewClick(int viewId);
    }
}
