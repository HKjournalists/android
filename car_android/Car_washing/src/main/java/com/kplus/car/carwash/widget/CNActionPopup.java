package com.kplus.car.carwash.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.Const;

/**
 * Created by Fu on 2015/5/17.
 */
public class CNActionPopup extends PopupWindow {
    private Context mContext = null;

    public CNActionPopup(Context context, View view, int width, int height) {
        super(view, width, height);

        mContext = context;

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setPopAnimationStyle(R.style.PopupAnimation);

        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        this.setWidth(width);
        this.setHeight(height);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(view);
    }

    public void setPopAnimationStyle(int animationStyle) {
        this.setAnimationStyle(animationStyle);
    }

    public void showNoAlpha(View anchor) {
        this.showAtLocation(anchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, Const.NONE, Const.NONE);
        this.update();
    }

    public void show(View anchor) {
        this.showAtLocation(anchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, Const.NONE, Const.NONE);
        this.update();
        backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 0.0-1.0
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
