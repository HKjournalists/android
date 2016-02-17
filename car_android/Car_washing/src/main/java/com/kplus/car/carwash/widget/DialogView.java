package com.kplus.car.carwash.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.utils.CNPixelsUtil;

public class DialogView extends LinearLayout {

    private AlertDialog mDialog;

    private Context mContext;
    private FrameLayout mTitleLayout;
    private LinearLayout mContentLayout;
    private LinearLayout mButtonLayout;

    public DialogView(Context context) {
        super(context);
        mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        mTitleLayout = new FrameLayout(mContext);
        mTitleLayout.setBackgroundResource(R.color.cn_white);
        this.addView(mTitleLayout, -1, CNPixelsUtil.dip2px(mContext, 44));

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.color.cn_white);
        this.addView(linearLayout, -1, -1);

        mContentLayout = new LinearLayout(mContext);
        mContentLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.weight = 1;
        linearLayout.addView(mContentLayout, layoutParams);

        mButtonLayout = new LinearLayout(mContext);
        mButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(mButtonLayout, -1, -2);
    }

    public static DialogView getInitialize(Context context) {
        return new DialogView(context);
    }

    public void show() {
        show(true);
    }

    public void show(boolean isCancelable) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(mContext).create();
        }
        mDialog.show();
        Point point = CNPixelsUtil.getDeviceSize(getContext());
        int width = (int) (point.x * 0.95);//手机屏幕的宽度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, WindowManager.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mDialog.addContentView(this, params);
        mDialog.setCancelable(isCancelable);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.CENTER);
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setOnCancelListener(OnCancelListener listener) {
        mDialog.setOnCancelListener(listener);
    }

    public DialogView setTitle(String title) {
        setTitle(title, false);
        return this;
    }

    public DialogView setTitle(String title, boolean isShowClose) {
        mTitleLayout.setBackgroundResource(R.color.cn_white);
        TextView titleView = new TextView(mContext);
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleView.setTextColor(getResources().getColor(R.color.yellow));
        titleView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = Gravity.CENTER;
        mTitleLayout.addView(titleView, params);

        if (isShowClose) {
            ImageView closeView = new ImageView(mContext);
            closeView.setImageResource(R.drawable.cn_icon_clear);
            closeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dismiss();
                }
            });
            FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(-2, -2);
            closeParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            closeParams.rightMargin = CNPixelsUtil.dip2px(mContext, 10);
            mTitleLayout.addView(closeView, closeParams);
        }
        return this;
    }

    public DialogView setContent(int icon, String content) {
        FrameLayout frameLayout = new FrameLayout(mContext);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView contentView = new TextView(mContext);
        contentView.setText(content);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        contentView.setTextColor(getResources().getColor(R.color.cn_common_cars_bgcolor));
        contentView.setGravity(Gravity.CENTER);
        LayoutParams linearParams = new LayoutParams(-2, -2);
        linearParams.topMargin = 10;
        linearParams.bottomMargin = 30;
        linearParams.gravity = Gravity.CENTER_VERTICAL;
        layout.addView(contentView, linearParams);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(-2, -2);
        frameParams.gravity = Gravity.CENTER;
        frameParams.rightMargin = CNPixelsUtil.dip2px(mContext, 10);
        frameParams.leftMargin = CNPixelsUtil.dip2px(mContext, 10);
        frameLayout.addView(layout, frameParams);
        mContentLayout.addView(frameLayout, -1, -2);
        return this;
    }

    public DialogView addBlackButton(String text, OnClickListener clickListener) {
        addButton(R.color.cn_space_color, text, R.color.cn_common_cars_bgcolor, true, clickListener);
        return this;
    }

    public DialogView addYellowButton(String text, OnClickListener clickListener) {
        addYellowButton(text, true, clickListener);
        return this;
    }

    public DialogView addYellowButton(String text, boolean enabled, OnClickListener clickListener) {
        addButton(R.color.cn_space_color, text, R.color.cn_common_cars_bgcolor, enabled, clickListener);
        return this;
    }

    private void addButton(int background, String text, int textColor, boolean enabled, final OnClickListener clickListener) {
        Button button = new Button(mContext);
        button.setText(text);
        button.setTextColor(getResources().getColor(textColor));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        button.setBackgroundResource(background);
        button.setGravity(Gravity.CENTER);
        button.setEnabled(enabled);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                clickListener.onClick(arg0);
                mDialog.dismiss();
            }
        });
        int margin = CNPixelsUtil.dip2px(mContext, 10);
        LayoutParams params = new LayoutParams(-1, -2);
        params.weight = 1;
        params.topMargin = margin;
        params.bottomMargin = margin;
        params.leftMargin = margin;
        params.rightMargin = margin;
        mButtonLayout.addView(button, params);
    }

    public DialogView addContentView(View view) {
        mContentLayout.addView(view);
        return this;
    }

}
