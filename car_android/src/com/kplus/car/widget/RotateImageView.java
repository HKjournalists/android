package com.kplus.car.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RotateImageView extends ImageView {
	private float scale;
	public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(scale != 0)
			canvas.scale(scale, scale, getMeasuredWidth()/2, getMeasuredHeight()/2);
		canvas.rotate(90, getMeasuredWidth()/2, getMeasuredHeight()/2);
		super.onDraw(canvas);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}	
}
