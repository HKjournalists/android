package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/4/15 0015.
 */
public class MyTextView extends RegularTextView {

    public MyTextView(Context context) {
        super(context);
        onCreate();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onCreate();
    }

    private void onCreate(){
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && view.isInTouchMode()){
                    view.performClick();
                }
            }
        });
    }
}
