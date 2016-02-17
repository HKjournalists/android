package com.kplus.car.carwash.manager;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.kplus.car.carwash.utils.CNPixelsUtil;

/**
 * Created by Fu on 2015/5/17.
 */
public abstract class BasePopupView {

    protected final Context mContext;
    protected final LayoutInflater mInflater;
    protected final Point mPoint;

    public BasePopupView(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPoint = CNPixelsUtil.getDeviceSize(mContext);
    }

    public abstract PopupWindow initPopupWindonw(Context context, int popupTyp, View view, int width, int height);

    protected <T extends View> T findView(View parent, int id) {
        if (null == parent) {
            throw new NullPointerException("parent is not null!");
        }
        return (T) parent.findViewById(id);
    }

    protected String getStringResources(int id) {
        return mContext.getResources().getString(id);
    }

    protected int getColorResources(int id) {
        return mContext.getResources().getColor(id);
    }
}
