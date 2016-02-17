package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.kplus.car.KplusApplication;

/**
 * Created by Administrator on 2015/9/11.
 */
public class RegularTextView extends TextView {
    public RegularTextView(Context context) {
        super(context);
        onCreate(context);
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate(context);
    }

    public RegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate(context);
    }

    private void onCreate(Context context){
        KplusApplication app = (KplusApplication) context.getApplicationContext();
        setTypeface(app.mRegular);
    }
}
