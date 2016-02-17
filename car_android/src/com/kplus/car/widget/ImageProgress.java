package com.kplus.car.widget;

import com.kplus.car.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageProgress extends ImageView {
	private Paint mPaint;
	private float nAngel = 100;
	private Context context;
	private int nRadius;
	private int nThirker;

	public ImageProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	public ImageProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		nRadius = dip2px(context, 16);
		nThirker = dip2px(context, 2);
	}

	public ImageProgress(Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(mPaint == null){
			mPaint = new Paint();
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setAntiAlias(true);
		}
		mPaint.setColor(getResources().getColor(R.color.daze_blue3));
		canvas.drawArc(new RectF(0, 0, 2*nRadius, 2*nRadius), 270, nAngel, true, mPaint);
		mPaint.setColor(Color.WHITE);
		if(360-nAngel > 0)
			canvas.drawArc(new RectF(0, 0, 2*nRadius, 2*nRadius), (270 + nAngel)%360, 360-nAngel, true, mPaint);
		mPaint.setColor(getResources().getColor(R.color.daze_light_green50));
		canvas.drawCircle(nRadius, nRadius, nRadius-nThirker, mPaint);
		super.onDraw(canvas);
	}

	public float getnAngel() {
		return nAngel;
	}

	public void setnAngel(float nAngel) {
		this.nAngel = nAngel;
	}

	private int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public void setnRadius(int nRadius) {
		this.nRadius = dip2px(context, nRadius);
	}
	
	

}
