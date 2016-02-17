package com.kplus.car.carwash.callback;

import com.kplus.car.carwash.widget.CNNavigationBar;

/**
 * Created by Fu on 2015/5/5.
 */
public interface CNToolbarStateCallback {
    boolean hasToolbar();

    void setToolbarStyle(CNNavigationBar navBar);

    boolean onItemSelectedListener(int viewId);
}
