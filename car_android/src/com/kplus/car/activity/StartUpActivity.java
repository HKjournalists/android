package com.kplus.car.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.kplus.car.R;
import com.kplus.car.fragment.StartUpFirstFragment;
import com.kplus.car.fragment.StartUpSecondFragment;
import com.kplus.car.fragment.StartUpThirdFragment;
import com.kplus.car.widget.CirclePageIndicator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Administrator on 2015/5/14.
 */
public class StartUpActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private StartUpPageAdapter mAdapter;
    private CirclePageIndicator mPageIndicator;
    private ObjectAnimator mAnimator;
    private View mBg;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_up);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mAdapter = new StartUpPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mPageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPageIndicator.setViewPager(mViewPager);
        mBg = findViewById(R.id.bg);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void setListener() {
        mPageIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                mBg.setBackgroundResource(R.drawable.s1_bg);
                StartUpFirstFragment ff = (StartUpFirstFragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                if (ff != null)
                    ff.onPageSelected();
                break;
            case 1:
                mBg.setBackgroundResource(R.drawable.s2_bg);
                StartUpSecondFragment sf = (StartUpSecondFragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                if (sf != null) {
                    sf.onPageSelected();
                    if (mAnimator != null)
                        mAnimator.cancel();
                    mPageIndicator.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mPageIndicator, 1);
                }
                break;
            case 2:
                mBg.setBackgroundResource(R.drawable.s3_bg);
                StartUpThirdFragment tf = (StartUpThirdFragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
                if (tf != null) {
                    tf.onPageSelected();
                    mAnimator = ObjectAnimator.ofFloat(mPageIndicator, "Alpha", 1, 0);
                    mAnimator.setDuration(500);
                    mAnimator.setStartDelay(3500);
                    mAnimator.start();
                    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            if ((Float)valueAnimator.getAnimatedValue() == 0)
                                mPageIndicator.setVisibility(View.GONE);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class StartUpPageAdapter extends FragmentPagerAdapter {

        public StartUpPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new StartUpFirstFragment();
                case 1:
                    return new StartUpSecondFragment();
                case 2:
                    return new StartUpThirdFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void finishWithAnim() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.scale_fade_out);
    }
}
