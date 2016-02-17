package com.kplus.car.carwash.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Fu on 2015/5/16.
 */
public class CNNoScrollGridView extends GridView {

    public CNNoScrollGridView(Context context) {
        this(context, null, 0);
    }

    public CNNoScrollGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CNNoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
