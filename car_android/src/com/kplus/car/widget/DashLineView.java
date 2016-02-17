package com.kplus.car.widget;

import com.kplus.car.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DashLineView extends View
{
	private Context context;

	public DashLineView(Context context)
	{
		super(context);
		this.context = context;
	}

	public DashLineView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
	}

	public DashLineView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint paint = new Paint();  
        paint.setStyle(Paint.Style.STROKE); 
        paint.setStrokeWidth(10);
        paint.setColor(context.getResources().getColor(R.color.daze_darkgrey7));  
        Path path = new Path();       
        path.moveTo(0, 0);  
        path.lineTo(getWidth(),0);
        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
        paint.setPathEffect(effects);  
        canvas.drawPath(path, paint);
        
		super.onDraw(canvas);
	}
}
