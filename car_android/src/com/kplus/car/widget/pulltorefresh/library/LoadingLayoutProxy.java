package com.kplus.car.widget.pulltorefresh.library;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.kplus.car.widget.pulltorefresh.library.internal.LoadingLayout;

import java.util.HashSet;

public class LoadingLayoutProxy implements ILoadingLayout {

	private final HashSet<LoadingLayout> mLoadingLayouts;

	LoadingLayoutProxy() {
		mLoadingLayouts = new HashSet<LoadingLayout>();
	}

	/**
	 * This allows you to add extra LoadingLayout instances to this proxy. This
	 * is only necessary if you keep your own instances, and want to have them
	 * included in any
	 * {@link PullToRefreshBase#createLoadingLayoutProxy(boolean, boolean)
	 * createLoadingLayoutProxy(...)} calls.
	 * 
	 * @param layout - LoadingLayout to have included.
	 */
	public void addLayout(LoadingLayout layout) {
		if (null != layout) {
			mLoadingLayouts.add(layout);
		}
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setLastUpdatedLabel(label);
		}
	}

	@Override
	public void setLoadingDrawable(Drawable drawable) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setLoadingDrawable(drawable);
		}
	}

	@Override
	public void setRefreshingLabel(CharSequence refreshingLabel) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setRefreshingLabel(refreshingLabel);
		}
	}
	
	public void setMyRefreshingLabel(CharSequence mRefreshingLabel) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setmRefreshingLabel(mRefreshingLabel);
		}
	}
	
	public void setMyEndLabel(CharSequence mEndLabel) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setmEndLabel(mEndLabel);
		}
	}
	
	@Override
	public void setRefreshingLabelIm(CharSequence refreshingLabel) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setRefreshingLabelIm(refreshingLabel);
		}
	}

	@Override
	public void setPullLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setPullLabel(label);
		}
	}

	@Override
	public void setReleaseLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setReleaseLabel(label);
		}
	}

	@Override
	public void setmEndLabel(CharSequence mEndLabel) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setmEndLabel(mEndLabel);
		}
	}

	public void setTextTypeface(Typeface tf) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setTextTypeface(tf);
		}
	}
}
