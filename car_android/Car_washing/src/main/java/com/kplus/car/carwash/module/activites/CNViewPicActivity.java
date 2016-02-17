package com.kplus.car.carwash.module.activites;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.adapter.CNViewPicAdapter;
import com.kplus.car.carwash.module.base.CNParentActivity;
import com.kplus.car.carwash.module.photoview.HackyViewPager;

import java.util.ArrayList;

/**
 * Description：图片预览界面
 * <br/><br/>Created by Fu on 2015/6/1.
 * <br/><br/>
 */
public class CNViewPicActivity extends CNParentActivity implements ViewPager.OnPageChangeListener {

    private ArrayList<String> mUrls = null;
    private int mSelectedPosition = Const.NONE;
    private int mPicType = CNCarWashingLogic.TYPE_PIC_OTHER;

    private LinearLayout llDot = null;
    private ImageView[] mDots = null;

    private int mCurrentPage = Const.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mUrls = bundle.getStringArrayList(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_URLS);
            mSelectedPosition = bundle.getInt(CNCarWashingLogic.KEY_REVIEW_LOG_PIC_POSITION);
            mPicType = bundle.getInt(CNCarWashingLogic.KEY_REVIEW_PIC_TYPE);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.cn_view_image_layout);

        HackyViewPager hvpPager = findView(R.id.hvpPager);
        llDot = findView(R.id.llDot);
        initDot();

        hvpPager.setOnPageChangeListener(this);

        CNViewPicAdapter picAdapter = new CNViewPicAdapter(mContext, mUrls, mPicType, null);
        picAdapter.setOnClickTapListener(mClickTapListener);
        picAdapter.setPhotoLongClickLisener(mLongClickListener);

        hvpPager.setAdapter(picAdapter);

        hvpPager.setCurrentItem(mSelectedPosition, false);
    }

    /**
     * 点击图片
     */
    private CNViewPicAdapter.OnClickTapListener mClickTapListener = new CNViewPicAdapter.OnClickTapListener() {
        @Override
        public void onTap() {
            CNViewManager.getIns().pop(CNViewPicActivity.this, Const.NONE, R.anim.cn_zoom_exit);
        }
    };


    /**
     * 长按图片
     */
    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    };

    private void initDot() {
        if (null != mUrls) {
            mDots = new ImageView[mUrls.size()];
            if (mUrls.size() <= Const.ONE) {
                llDot.setVisibility(View.GONE);
            } else {
                llDot.removeAllViews();
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                for (int i = Const.NONE; i < mUrls.size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setLayoutParams(lp);
                    imageView.setId(i);
                    imageView.setPadding(5, 0, 5, 5);
                    imageView.setImageResource(R.drawable.cn_dot_selector);

                    llDot.addView(imageView, i);

                    mDots[i] = imageView;
                    mDots[i].setEnabled(true);
                    mDots[i].setTag(i);
                    if (i == mSelectedPosition) {
                        mDots[i].setEnabled(false);
                    }
                }
                mCurrentPage = mSelectedPosition;
            }
        } else {
            llDot.setVisibility(View.GONE);
        }
    }

    /**
     * 切换小红点
     *
     * @param positon 第几页
     */
    private void setCurDot(int positon) {
        if (positon < Const.NONE
                || positon > mCurrentPage - Const.NEGATIVE
                || mCurrentPage == positon
                || mDots.length < positon) {
            return;
        }

        mDots[positon].setEnabled(false);
        mDots[mCurrentPage].setEnabled(true);
        mCurrentPage = positon;
    }

    @Override
    protected void initToolbarView() {
    }

    @Override
    protected boolean isUseDefaultLayoutToolbar() {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
