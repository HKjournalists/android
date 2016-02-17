/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Triggertrap Ltd
 * Author Neil Davies
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.kplus.car.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kplus.car.R;

/**
 * 
 * SeekArc.java
 * 
 * This is a class that functions much like a SeekBar but
 * follows a circle path instead of a straight line.
 * 
 * @author Neil Davies
 * 
 */
public class ProgressArc extends View {

	private static final String TAG = ProgressArc.class.getSimpleName();
	private static int INVALID_PROGRESS_VALUE = -1;
	// The initial rotational offset -90 means we start at 12 o'clock
	private final int mAngleOffset = -90;

	/**
	 * The Maximum value that this SeekArc can be set to
	 */
	private int mMax = 100;

	/**
	 * The Current value that the SeekArc is set to
	 */
	private int mProgress = 0;

	/**
	 * The width of the progress line for this SeekArc
	 */
	private int mProgressWidth = 4;

	/**
	 * The Width of the background arc for the SeekArc
	 */
	private int mArcWidth = 2;

	/**
	 * The Angle to start drawing this Arc from
	 */
	private int mStartAngle = 0;

	/**
	 * The Angle through which to draw the arc (Max is 360)
	 */
	private int mSweepAngle = 360;

	/**
	 * The rotation of the SeekArc- 0 is twelve o'clock
	 */
	private int mRotation = 0;

	/**
	 * Give the SeekArc rounded edges
	 */
	private boolean mRoundedEdges = false;

	/**
	 * Will the progress increase clockwise or anti-clockwise
	 */
	private boolean mClockwise = true;

    private boolean mTwoProgressColor = false;

	// Internal variables
	private int mArcRadius = 0;
	private float mProgressSweep = 0;
	private RectF mArcRect = new RectF();
	private Paint mArcPaint;
	private Paint mProgressPaint;
    private Paint mProgressPaint2;
	private int mTranslateX;
	private int mTranslateY;

	public ProgressArc(Context context) {
		super(context);
		init(context, null, 0);
	}

	public ProgressArc(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, R.attr.seekArcStyle);
	}

	public ProgressArc(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {

		Log.d(TAG, "Initialising SeekArc");
		final Resources res = getResources();
		float density = context.getResources().getDisplayMetrics().density;

		// Defaults, may need to link this into theme settings
		int arcColor = res.getColor(R.color.progress_gray);
		int progressColor = res.getColor(android.R.color.holo_blue_light);
        int progressColor2 = res.getColor(android.R.color.holo_blue_light);
		// Convert progress width to pixels for current density
		mProgressWidth = (int) (mProgressWidth * density);


		if (attrs != null) {
			// Attribute initialization
			final TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.SeekArc, defStyle, 0);

			mMax = a.getInteger(R.styleable.SeekArc_max, mMax);
			mProgress = a.getInteger(R.styleable.SeekArc_progress, mProgress);
			mProgressWidth = (int) a.getDimension(
					R.styleable.SeekArc_progressWidth, mProgressWidth);
			mArcWidth = (int) a.getDimension(R.styleable.SeekArc_arcWidth,
					mArcWidth);
			mStartAngle = a.getInt(R.styleable.SeekArc_startAngle, mStartAngle);
			mSweepAngle = a.getInt(R.styleable.SeekArc_sweepAngle, mSweepAngle);
			mRotation = a.getInt(R.styleable.SeekArc_rotation, mRotation);
			mRoundedEdges = a.getBoolean(R.styleable.SeekArc_roundEdges,
					mRoundedEdges);
			mClockwise = a.getBoolean(R.styleable.SeekArc_clockwise,
					mClockwise);
            mTwoProgressColor = a.getBoolean(R.styleable.SeekArc_twoProgressColor, mTwoProgressColor);

			arcColor = a.getColor(R.styleable.SeekArc_arcColor, arcColor);
			progressColor = a.getColor(R.styleable.SeekArc_progressColor,
					progressColor);
            progressColor2 = a.getColor(R.styleable.SeekArc_progressColor2,
                    progressColor2);

			a.recycle();
		}

		mProgress = (mProgress > mMax) ? mMax : mProgress;
		mProgress = (mProgress < 0) ? 0 : mProgress;

		mSweepAngle = (mSweepAngle > 360) ? 360 : mSweepAngle;
		mSweepAngle = (mSweepAngle < 0) ? 0 : mSweepAngle;

		mStartAngle = (mStartAngle > 360) ? 0 : mStartAngle;
		mStartAngle = (mStartAngle < 0) ? 0 : mStartAngle;

		mArcPaint = new Paint();
		mArcPaint.setColor(arcColor);
		mArcPaint.setAntiAlias(true);
		mArcPaint.setStyle(Paint.Style.STROKE);
		mArcPaint.setStrokeWidth(mArcWidth);
		//mArcPaint.setAlpha(45);

		mProgressPaint = new Paint();
		mProgressPaint.setColor(progressColor);
		mProgressPaint.setAntiAlias(true);
		mProgressPaint.setStyle(Paint.Style.STROKE);
		mProgressPaint.setStrokeWidth(mProgressWidth);

        if (mTwoProgressColor){
            mProgressPaint2 = new Paint();
            mProgressPaint2.setColor(progressColor2);
            mProgressPaint2.setAntiAlias(true);
            mProgressPaint2.setStyle(Paint.Style.STROKE);
            mProgressPaint2.setStrokeWidth(mProgressWidth);
        }

		if (mRoundedEdges) {
			mArcPaint.setStrokeCap(Paint.Cap.ROUND);
			mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the arcs
		final int arcStart = mStartAngle + mAngleOffset + mRotation;
		final int arcSweep = mClockwise ? mSweepAngle : -mSweepAngle;
		canvas.drawArc(mArcRect, arcStart, arcSweep, false, mArcPaint);
        if (mTwoProgressColor){
            int arcOne = arcSweep / mMax;
            for (int i = 0; i < mProgress; i++){
                Paint p = i % 2 == 0 ? mProgressPaint : mProgressPaint2;
                canvas.drawArc(mArcRect, arcStart + arcOne * i, arcOne, false, p);
            }
        }
        else {
            canvas.drawArc(mArcRect, arcStart, mProgressSweep, false, mProgressPaint);
        }
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		final int height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		final int width = getDefaultSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int min = Math.min(width, height);
		float top = 0;
		float left = 0;
		int arcDiameter = 0;

		mTranslateX = (int) (width * 0.5f);
		mTranslateY = (int) (height * 0.5f);

		arcDiameter = min - getPaddingLeft() - mProgressWidth;
		mArcRadius = arcDiameter / 2;
		top = height / 2 - mArcRadius;
		left = width / 2 - mArcRadius;
		mArcRect.set(left, top, left + mArcRadius * 2, top + mArcRadius * 2);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	private void updateProgress(int progress) {

		if (progress == INVALID_PROGRESS_VALUE) {
			return;
		}

		progress = (progress > mMax) ? mMax : progress;
		progress = (mProgress < 0) ? 0 : progress;

		mProgress = progress;
		mProgressSweep = (float) progress / mMax * mSweepAngle;

		invalidate();
	}

	public void setProgress(int progress) {
		updateProgress(progress);
	}

    public void setProgressColor(int color){
        mProgressPaint.setColor(color);
    }

    public void setProgressColor2(int color){
        mProgressPaint2.setColor(color);
    }

	public int getProgressWidth() {
		return mProgressWidth;
	}

	public void setProgressWidth(int mProgressWidth) {
		this.mProgressWidth = mProgressWidth;
		mProgressPaint.setStrokeWidth(mProgressWidth);
	}
	
	public int getArcWidth() {
		return mArcWidth;
	}

	public void setArcWidth(int mArcWidth) {
		this.mArcWidth = mArcWidth;
		mArcPaint.setStrokeWidth(mArcWidth);
	}
	public int getArcRotation() {
		return mRotation;
	}

	public void setArcRotation(int mRotation) {
		this.mRotation = mRotation;
	}

	public int getStartAngle() {
		return mStartAngle;
	}

	public void setStartAngle(int mStartAngle) {
		this.mStartAngle = mStartAngle;
	}

	public int getSweepAngle() {
		return mSweepAngle;
	}

	public void setSweepAngle(int mSweepAngle) {
		this.mSweepAngle = mSweepAngle;
	}
	
	public void setRoundedEdges(boolean isEnabled) {
		mRoundedEdges = isEnabled;
		if (mRoundedEdges) {
			mArcPaint.setStrokeCap(Paint.Cap.ROUND);
			mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
		} else {
			mArcPaint.setStrokeCap(Paint.Cap.SQUARE);
			mProgressPaint.setStrokeCap(Paint.Cap.SQUARE);
		}
	}
	
	public void setClockwise(boolean isClockwise) {
		mClockwise = isClockwise;
	}
}
