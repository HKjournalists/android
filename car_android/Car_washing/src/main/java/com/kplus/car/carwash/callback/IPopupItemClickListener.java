package com.kplus.car.carwash.callback;

import android.view.View;

import com.kplus.car.carwash.bean.BaseInfo;

/**
 * Created by Fu on 2015/5/17.
 */
public interface IPopupItemClickListener {
    void onClickPopupItem(int popupType, int position, View v, BaseInfo data);
}
