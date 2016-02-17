package com.kplus.car.carwash.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Fu on 2015/5/11.
 */
public class CNWashingServiceListView extends ListView {
    public CNWashingServiceListView(Context context) {
        this(context, null, 0);
    }

    public CNWashingServiceListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CNWashingServiceListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
