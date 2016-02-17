package com.kplus.car.carwash.manager;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.kplus.car.carwash.widget.CNActionPopup;

/**
 * Created by Fu on 2015/5/17.
 */
public class CNPopupManager extends BasePopupView {
    private static final String TAG = "CNPopupManager";

    public static final int POPUP_CITY = 1;

    public CNPopupManager(Context context) {
        super(context);
    }

    @Override
    public PopupWindow initPopupWindonw(Context context, int popupTyp, View view, int width, int height) {
        CNActionPopup actionPopup = null;
        switch (popupTyp) {
            case POPUP_CITY: // 初始化城市popup
                actionPopup = new CNActionPopup(context, view, width, height);
                break;
        }
        return actionPopup;
    }
}
