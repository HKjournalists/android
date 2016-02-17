package com.kplus.car.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class InnerViewPager extends ViewPager
{
	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = super.dispatchTouchEvent(ev);
		if (ret)
			requestDisallowInterceptTouchEvent(ret);
		return ret;
	}
}
