package com.kplus.car.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Description：禁止ScrollView在子控件的布局改变时自动滚动的方法
 * <br/><br/>Created by FU ZHIXUE on 2015/8/11.
 * <br/><br/>
 */
public class CustomScrollView extends ScrollView {
    public CustomScrollView(Context context) {
        this(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
