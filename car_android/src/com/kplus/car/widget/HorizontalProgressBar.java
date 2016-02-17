package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/4/4.
 */
public class HorizontalProgressBar extends ImageView {
    private int mMax = 0;
    private int mProgress = 0;
    private boolean mbFirst = true;
    private int mWidth = 0;

    public HorizontalProgressBar(Context context) {
        super(context);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMax(int max){
        mMax = max;
    }

    public void setProgress(int progress){
        if (mMax > 0){
            mProgress = progress;
            if (mProgress < 0)
                mProgress = 0;
            else if (mProgress > mMax)
                mProgress = mMax;
            if (mWidth == 0)
                requestLayout();
            else{
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth * mProgress / mMax, getLayoutParams().height);
                setLayoutParams(params);
            }
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        if (mbFirst){
            mbFirst = false;
            mWidth = r - l;
            if (mMax > 0){
                super.layout(l, t, l + mWidth * mProgress / mMax, b);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mWidth * mProgress / mMax, getLayoutParams().height);
                setLayoutParams(params);
            }
        }
        else
            super.layout(l, t, r, b);
    }
}
