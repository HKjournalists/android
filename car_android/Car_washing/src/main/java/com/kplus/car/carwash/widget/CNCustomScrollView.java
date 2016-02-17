package com.kplus.car.carwash.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Description：禁止ScrollView在子控件的布局改变时自动滚动的方法
 * <br/><br/>Created by FU ZHIXUE on 2015/8/12.
 * <br/><br/>
 */
public class CNCustomScrollView extends ScrollView {
    public CNCustomScrollView(Context context) {
        this(context, null);
    }

    public CNCustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
