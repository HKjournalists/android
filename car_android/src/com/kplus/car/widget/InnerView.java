package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InnerView extends LinearLayout
{

	public InnerView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InnerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		switch(ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_MOVE:
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_UP:
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_CANCEL:
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
				default:
					break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
