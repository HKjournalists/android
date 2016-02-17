package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2015/6/10.
 */
public class ScrollLinearLayout extends LinearLayout {
    private Scroller mScroller;

    public ScrollLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public void snapToScreen(int whichScreen) {
        int curscrollerx = getScrollX();
        mScroller.startScroll(curscrollerx, 0, whichScreen - curscrollerx, 0, 500);
        invalidate();

    }
}
