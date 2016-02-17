package com.kplus.car.carwash.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.utils.CNViewClickUtil;

/**
 * 评分的自定义星星控件
 * Created by Fu on 2015/5/16.
 */
public class CNStarGradeLinearlayout extends LinearLayout implements CNViewClickUtil.NoFastClickListener {
    private static final int MAX_STAR_COUNT = 5;
    private static final String TAG_STAR_VIEW = "tag-star-view";

    private Context mContext = null;

    private ImageView[] mStarView = null;

    private int mRating = 4;
    private boolean isClicStar = true;
    private int mStarOffResId = 0;
    private int mStarOnResId = 0;

    public void setIsClicStar(boolean isClicStar) {
        this.isClicStar = isClicStar;
    }

    public void setStarOffResId(int starOffResId) {
        mStarOffResId = starOffResId;
    }

    public void setStarOnResId(int starOnResId) {
        mStarOnResId = starOnResId;
    }

    public int getRating() {
        return mRating;
    }

    public CNStarGradeLinearlayout(Context context) {
        this(context, null, 0);
    }

    public CNStarGradeLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CNStarGradeLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 宽高，为0表示WRAP_CONTENT
     * @param starWidth 0表示WRAP_CONTENT
     * @param starHeight 0表示WRAP_CONTENT
     */
    public void initStar(int starWidth, int starHeight) {
        if (starWidth <= 0) {
            starWidth = LayoutParams.WRAP_CONTENT;
        }

        if (starHeight <= 0) {
            starHeight = LayoutParams.WRAP_CONTENT;
        }

        LayoutParams params = new LayoutParams(starWidth, starHeight);
        params.gravity = Gravity.CENTER_VERTICAL;

        mStarView = new ImageView[MAX_STAR_COUNT];

        for (int i = 0; i < MAX_STAR_COUNT; i++) {
            ImageView ivStar = new ImageView(mContext);
            ivStar.setLayoutParams(params);
            ivStar.setTag(TAG_STAR_VIEW);
            ivStar.setImageResource(mStarOffResId);
            int padding = mContext.getResources().getDimensionPixelOffset(R.dimen.cn_orderlog_star_padding);
            ivStar.setPadding(padding, 0, 0, 0);
            ivStar.setId((i + 1));

            if (isClicStar) {
                CNViewClickUtil.setNoFastClickListener(ivStar, this);
            }

            mStarView[i] = ivStar;

            this.addView(ivStar, i);
        }
    }

    /**
     * 设置用户选择的评分等级
     *
     * @param rating 现在有5个
     */
    public void setRating(int rating) {
        mRating = rating;
        if (mRating <= 0) {
            mRating = 1;
        }

        // rating 为0 ，默认选择第一个
        for (int i = 0; i < mStarView.length; i++) {
            if (i >= mRating) {
                mStarView[i].setImageResource(mStarOffResId);
            } else {
                mStarView[i].setImageResource(mStarOnResId);
            }
        }
    }

    @Override
    public void onNoFastClick(View v) {
        if (!isClicStar)
            return;
        if (TAG_STAR_VIEW.equals(v.getTag())) {
            setRating(v.getId());

            if (null != mStarRatingChangeListener) {
                mStarRatingChangeListener.onStarRatingChanged(mRating);
            }
        }
    }

    private IStarRatingChangeListener mStarRatingChangeListener = null;

    public void setStarRatingChangeListener(IStarRatingChangeListener listener) {
        mStarRatingChangeListener = listener;
    }

    public interface IStarRatingChangeListener {
        void onStarRatingChanged(int rating);
    }
}
