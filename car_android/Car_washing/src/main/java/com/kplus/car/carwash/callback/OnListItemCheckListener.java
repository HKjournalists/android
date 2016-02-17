package com.kplus.car.carwash.callback;

import com.kplus.car.carwash.bean.BaseInfo;

/**
 * Created by Fu on 2015/5/14.
 */
public interface OnListItemCheckListener {
    void onCheckedItem(boolean isChecked, int position, BaseInfo baseInfo);
}
