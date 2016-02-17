/**
 * ****************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package com.kplus.car.carwash.widget.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.widget.pulltorefresh.library.ILoadingLayout;
import com.kplus.car.carwash.widget.pulltorefresh.library.PullToRefreshBase.Mode;
import com.kplus.car.carwash.widget.pulltorefresh.library.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    static final String LOG_TAG = "PullToRefresh-LoadingLayout";

    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

    private FrameLayout mInnerLayout;

    protected final ImageView mHeaderImage;
    protected final ProgressBar mHeaderProgress;

    private boolean mUseIntrinsicAnimation;

    private final TextView mHeaderText;
    private final TextView mSubHeaderText;

    protected final Mode mMode;
    protected final Orientation mScrollDirection;

    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;
    private CharSequence mEndLabel;

    public LoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context);
        mMode = mode;
        mScrollDirection = scrollDirection;

        switch (scrollDirection) {
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(getResources().getIdentifier("pull_to_refresh_header_horizontal", "layout", context.getPackageName()), this);
                break;
            case VERTICAL:
            default:
                LayoutInflater.from(context).inflate(getResources().getIdentifier("pull_to_refresh_header_vertical", "layout", context.getPackageName()), this);
                break;
        }

        mInnerLayout = (FrameLayout) findViewById(getResources().getIdentifier("fl_inner", "id", context.getPackageName()));
        mHeaderText = (TextView) mInnerLayout.findViewById(getResources().getIdentifier("pull_to_refresh_text", "id", context.getPackageName()));
        mHeaderText.setTextColor(getResources().getColor(R.color.cn_load_normal_end));
        mHeaderProgress = (ProgressBar) mInnerLayout.findViewById(getResources().getIdentifier("pull_to_refresh_progress", "id", context.getPackageName()));
        mSubHeaderText = (TextView) mInnerLayout.findViewById(getResources().getIdentifier("pull_to_refresh_sub_text", "id", context.getPackageName()));
        mHeaderText.setTextColor(getResources().getColor(R.color.cn_font_color));
        mHeaderImage = (ImageView) mInnerLayout.findViewById(getResources().getIdentifier("pull_to_refresh_image", "id", context.getPackageName()));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;

                // Load in labels
                mPullLabel = context.getString(getResources().getIdentifier("pull_to_refresh_from_bottom_pull_label", "string", context.getPackageName()));
                mRefreshingLabel = context.getString(getResources().getIdentifier("pull_to_refresh_from_bottom_refreshing_label", "string", context.getPackageName()));
                mReleaseLabel = context.getString(getResources().getIdentifier("pull_to_refresh_from_bottom_release_label", "string", context.getPackageName()));
                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;

                // Load in labels
                mPullLabel = context.getString(getResources().getIdentifier("pull_to_refresh_pull_label", "string", context.getPackageName()));
                mRefreshingLabel = context.getString(getResources().getIdentifier("pull_to_refresh_refreshing_label", "string", context.getPackageName()));
                mReleaseLabel = context.getString(getResources().getIdentifier("pull_to_refresh_release_label", "string", context.getPackageName()));
                mEndLabel = context.getString(getResources().getIdentifier("pull_to_refresh_end_label", "string", context.getPackageName()));
                break;
        }

        if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrHeaderBackground", "styleable", context.getPackageName()))) {
            Drawable background = attrs.getDrawable(getResources().getIdentifier("PullToRefresh_ptrHeaderBackground", "styleable", context.getPackageName()));
            if (null != background) {
                ViewCompat.setBackground(this, background);
            }
        }

        if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrHeaderTextAppearance", "styleable", context.getPackageName()))) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(getResources().getIdentifier("PullToRefresh_ptrHeaderTextAppearance", "styleable", context.getPackageName()), styleID);
            setTextAppearance(styleID.data);
        }
        if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrSubHeaderTextAppearance", "styleable", context.getPackageName()))) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(getResources().getIdentifier("PullToRefresh_ptrSubHeaderTextAppearance", "styleable", context.getPackageName()), styleID);
            setSubTextAppearance(styleID.data);
        }

        // Text Color attrs need to be set after TextAppearance attrs
        if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrHeaderTextColor", "styleable", context.getPackageName()))) {
            ColorStateList colors = attrs.getColorStateList(getResources().getIdentifier("PullToRefresh_ptrHeaderTextColor", "styleable", context.getPackageName()));
            if (null != colors) {
                setTextColor(colors);
            }
        }
        if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrHeaderSubTextColor", "styleable", context.getPackageName()))) {
            ColorStateList colors = attrs.getColorStateList(getResources().getIdentifier("PullToRefresh_ptrHeaderSubTextColor", "styleable", context.getPackageName()));
            if (null != colors) {
                setSubTextColor(colors);
            }
        }

        // Try and get defined drawable from Attrs
        Drawable imageDrawable = null;
        if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrDrawable", "styleable", context.getPackageName()))) {
            imageDrawable = attrs.getDrawable(getResources().getIdentifier("PullToRefresh_ptrDrawable", "styleable", context.getPackageName()));
        }

        // Check Specific Drawable from Attrs, these overrite the generic
        // drawable attr above
        switch (mode) {
            case PULL_FROM_START:
            default:
                if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrDrawableStart", "styleable", context.getPackageName()))) {
                    imageDrawable = attrs.getDrawable(getResources().getIdentifier("PullToRefresh_ptrDrawableStart", "styleable", context.getPackageName()));
                } else if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrDrawableTop", "styleable", context.getPackageName()))) {
                    Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
                    imageDrawable = attrs.getDrawable(getResources().getIdentifier("PullToRefresh_ptrDrawableTop", "styleable", context.getPackageName()));
                }
                break;

            case PULL_FROM_END:
                if (attrs.hasValue(getResources().getIdentifier(
                        "PullToRefresh_ptrDrawableEnd", "styleable", context.getPackageName()))) {
                    imageDrawable = attrs.getDrawable(getResources().getIdentifier("PullToRefresh_ptrDrawableEnd", "styleable", context.getPackageName()));
                } else if (attrs.hasValue(getResources().getIdentifier("PullToRefresh_ptrDrawableBottom", "styleable", context.getPackageName()))) {
                    Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
                    imageDrawable = attrs.getDrawable(getResources().getIdentifier("PullToRefresh_ptrDrawableBottom", "styleable", context.getPackageName()));
                }
                break;
        }

        // If we don't have a user defined drawable, load the default
        if (null == imageDrawable) {
            imageDrawable = context.getResources().getDrawable(
                    getDefaultDrawableResId());
        }

        // Set Drawable, and save width/height
        setLoadingDrawable(imageDrawable);

        reset();
    }

    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    public final int getContentSize() {
        switch (mScrollDirection) {
            case HORIZONTAL:
                return mInnerLayout.getWidth();
            case VERTICAL:
            default:
                return mInnerLayout.getHeight();
        }
    }

    public final void hideAllViews() {
        if (View.VISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mHeaderProgress.getVisibility()) {
            mHeaderProgress.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mHeaderImage.getVisibility()) {
            mHeaderImage.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.INVISIBLE);
        }
    }

    public final void onPull(float scaleOfLayout) {
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout);
        }
    }

    public final void pullToRefresh() {
        if (null != mHeaderText) {
            mHeaderText.setText(mPullLabel);
        }

        // Now call the callback
        pullToRefreshImpl();
    }

    public final void refreshing() {
        if (null != mHeaderText) {
            mHeaderText.setText(mRefreshingLabel);
        }

        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mHeaderImage.getDrawable()).start();
        } else {
            // Now call the callback
            refreshingImpl();
        }

        if (null != mSubHeaderText) {
            mSubHeaderText.setVisibility(View.GONE);
        }
    }

    public final void releaseToRefresh() {
        if (null != mHeaderText) {
            mHeaderText.setText(mReleaseLabel);
        }

        // Now call the callback
        releaseToRefreshImpl();
    }

    public final void reset() {
        if (null != mHeaderText) {
            mHeaderText.setText(mEndLabel);
        }
        mHeaderImage.setVisibility(View.VISIBLE);

        if (mUseIntrinsicAnimation) {
            ((AnimationDrawable) mHeaderImage.getDrawable()).stop();
        } else {
            // Now call the callback
            resetImpl();
        }

        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(mSubHeaderText.getText())) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        setSubHeaderText(label);
    }

    public final void setLoadingDrawable(Drawable imageDrawable) {
        // Set Drawable
        mHeaderImage.setImageDrawable(imageDrawable);
        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

        // Now call the callback
        onLoadingDrawableSet(imageDrawable);
    }

    public void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }

    @Override
    public void setRefreshingLabelIm(CharSequence refreshingLabel) {
        mHeaderText.setText(refreshingLabel);
    }

    @Override
    public void setTextTypeface(Typeface tf) {
        mHeaderText.setTypeface(tf);
    }

    public final void showInvisibleViews() {
        if (View.INVISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mHeaderProgress.getVisibility()) {
            mHeaderProgress.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mHeaderImage.getVisibility()) {
            mHeaderImage.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Callbacks for derivative Layouts
     */

    protected abstract int getDefaultDrawableResId();

    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

    protected abstract void onPullImpl(float scaleOfLayout);

    protected abstract void pullToRefreshImpl();

    protected abstract void refreshingImpl();

    protected abstract void releaseToRefreshImpl();

    protected abstract void resetImpl();

    private void setSubHeaderText(CharSequence label) {
        if (null != mSubHeaderText) {
            if (TextUtils.isEmpty(label)) {
                mSubHeaderText.setVisibility(View.GONE);
            } else {
                mSubHeaderText.setText(label);

                // Only set it to Visible if we're GONE, otherwise VISIBLE will
                // be set soon
                if (View.GONE == mSubHeaderText.getVisibility()) {
                    mSubHeaderText.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setSubTextAppearance(int value) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setSubTextColor(ColorStateList color) {
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    private void setTextAppearance(int value) {
        if (null != mHeaderText) {
            mHeaderText.setTextAppearance(getContext(), value);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextAppearance(getContext(), value);
        }
    }

    private void setTextColor(ColorStateList color) {
        if (null != mHeaderText) {
            mHeaderText.setTextColor(color);
        }
        if (null != mSubHeaderText) {
            mSubHeaderText.setTextColor(color);
        }
    }

    public CharSequence getmRefreshingLabel() {
        return mRefreshingLabel;
    }

    public void setmRefreshingLabel(CharSequence mRefreshingLabel) {
        this.mRefreshingLabel = mRefreshingLabel;
    }

    public CharSequence getmEndLabel() {
        return mEndLabel;
    }

    @Override
    public void setmEndLabel(CharSequence mEndLabel) {
        this.mEndLabel = mEndLabel;
    }
}