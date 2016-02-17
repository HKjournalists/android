package com.kplus.car.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.kplus.car.anim.FadeAnim;
import com.kplus.car.anim.OnAnimEndListener;

public abstract class AnimatedMenu {

	public interface OnMenuBgClickListener {
		public void onMenuBgClicked(AnimatedMenu menu);
	}

	private Activity mActivity;
	private RelativeLayout mRootView;
	private ImageView mBackgroundView;
	private View mContentView;
	private boolean mIsShown = false;
	private int mAnimationTimeout = 300;
	private boolean mHideOnBgClicked = true;
	private OnMenuBgClickListener mListener;

	public AnimatedMenu(Activity parent, int backgroundResourceId) {
		mActivity = parent;
		mRootView = new RelativeLayout(parent);
		mBackgroundView = new ImageView(parent);
		mBackgroundView.setScaleType(ScaleType.CENTER_CROP);
		mBackgroundView.setImageResource(backgroundResourceId);
		mBackgroundView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mHideOnBgClicked) {
					hide();
					if (mListener != null) {
						mListener.onMenuBgClicked(AnimatedMenu.this);
					}
				}
			}
		});
		int width = LayoutParams.MATCH_PARENT;
		int height = LayoutParams.MATCH_PARENT;
		LayoutParams layoutParams = new LayoutParams(width, height);
		mRootView.addView(mBackgroundView, layoutParams);
		mRootView.setVisibility(View.GONE);
		mActivity.addContentView(mRootView, layoutParams);
	}

	public Activity getActivity() {
		return mActivity;
	}

	public int getAnimationTimeout() {
		return mAnimationTimeout;
	}

	public void setAnimationTimeout(int timeout) {
		if (timeout < 0)
			throw new IllegalArgumentException("timeout must >= 0");
		mAnimationTimeout = timeout;
	}

	public boolean getHideOnBgClicked() {
		return mHideOnBgClicked;
	}

	public void setHideOnBgClicked(boolean value) {
		mHideOnBgClicked = value;
	}

	public OnMenuBgClickListener getOnMenuBgClickListener() {
		return mListener;
	}

	public void setOnMenuBgClickListener(OnMenuBgClickListener listener) {
		mListener = listener;
	}

	public View getContentView() {
		return mContentView;
	}

	protected abstract void processContentViewLayoutParams(RelativeLayout.LayoutParams layoutParams);

    private void setOnClickListenerToAll(ViewGroup viewGroup, OnClickListener listener) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup)
                setOnClickListenerToAll((ViewGroup) child, listener);
            else
                child.setOnClickListener(listener);
        }
    }

	public void setContentView(int layoutId, OnClickListener listener) {
		if (mContentView != null)
			mRootView.removeView(mContentView);

		LayoutInflater inflater = mActivity.getLayoutInflater();
		mContentView = inflater.inflate(layoutId, mRootView, false);

		if (listener != null) {
			if (mContentView instanceof ViewGroup)
				setOnClickListenerToAll((ViewGroup) mContentView, listener);
			else
				mContentView.setOnClickListener(listener);
		}

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
		processContentViewLayoutParams(params);
		mRootView.addView(mContentView);
	}

	public boolean isShown() {
		return mIsShown;
	}

	protected abstract void playShowAnimation(View contentView);

	public void show() {
		if (mIsShown)
			return;
		mIsShown = true;
        View v = mActivity.getCurrentFocus();
        if (v != null){
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
		mRootView.setVisibility(View.VISIBLE);
//		FadeAnim.fadeVisible(mAnimationTimeout, mBackgroundView);

		playShowAnimation(mContentView);
	}

	public void showImediately() {
		if (mIsShown)
			return;
		mIsShown = true;

		mRootView.setVisibility(View.VISIBLE);
	}

	public boolean isHidden() {
		return !mIsShown;
	}

	protected abstract void playHideAnimation(View contentView);

	public void hide() {
		if (!mIsShown)
			return;
		mIsShown = false;

		AlphaAnimation anim = new AlphaAnimation(1, 0);
		anim.setDuration(mAnimationTimeout);
		anim.setAnimationListener(new OnAnimEndListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				mRootView.setVisibility(View.GONE);
			}
		});
		mBackgroundView.startAnimation(anim);

		playHideAnimation(mContentView);
	}

	public void hideImediately() {
		if (!mIsShown)
			return;
		mIsShown = false;

		mRootView.setVisibility(View.GONE);
	}

}
