package com.kplus.car.carwash.manager;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.widget.DialogView;


public class DialogManager {

    public static void onOneButton(Context context, String message, final OnClickListener clickListener) {
        DialogView.getInitialize(context)
                .setContent(0, message)
                .addBlackButton(context.getResources().getString(R.string.cn_affirm), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onClick(v);
                    }
                }).show(false);
    }

    public static void onAffirm(Context context, String message, int buttonText, final OnClickListener clickListener) {
        if (null == context) {
            return;
        }
        DialogView.getInitialize(context).setContent(0, message).addBlackButton(context.getString(R.string.cn_cancel),
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                    }

                })
                .addYellowButton(context.getString(buttonText), new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (clickListener != null)
                            clickListener.onClick(arg0);
                    }
                }).show(false);
    }

    /**
     * 显示立即支付和确定按钮
     *
     * @param context
     * @param message
     * @param rightButtonText
     * @param payClickListener
     * @param affirmClickListener
     */
    public static void onAffirmPay(Context context, String message, int leftButtonText, int rightButtonText,
                                   final OnClickListener payClickListener, final OnClickListener affirmClickListener) {
        DialogView.getInitialize(context).setContent(0, message).addBlackButton(context.getString(leftButtonText),
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (null != payClickListener) {
                            payClickListener.onClick(arg0);
                        }
                    }
                })
                .addYellowButton(context.getResources().getString(rightButtonText), new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (affirmClickListener != null) {
                            affirmClickListener.onClick(arg0);
                        }
                    }
                }).show(false);
    }

    public static void onAffirm(Context context, int message, final OnClickListener clickListener) {
        if (null == context) {
            return;
        }
        onAffirm(context, context.getString(message), R.string.cn_affirm, clickListener);
    }

    public static void onAffirm(Context context, String message, final OnClickListener clickListener) {
        if (null == context) {
            return;
        }
        onAffirm(context, message, R.string.cn_affirm, clickListener);
    }

    public static void onCall(Context context, String message, final OnClickListener clickListener) {
        if (null == context) {
            return;
        }
        onAffirm(context, message, R.string.cn_call, clickListener);
    }
}
