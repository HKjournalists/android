package com.kplus.car.carwash.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.kplus.car.carwash.R;

/**
 * Description：ProgressDialog工具类
 * <br/><br/>Created by Fu on 2015/5/19.
 * <br/><br/>
 */
public class CNProgressDialogUtil {
    private static ProgressDialog mProgressDialog = null;

    public static void showProgress(Context context) {
        showProgress(context, true, "");
    }

    public static void showProgress(Context context, int text) {
        showProgress(context, true, text);
    }

    public static void showProgress(Context context, String text) {
        showProgress(context, true, text);
    }

    public static void showProgress(Context context, boolean isShowProgress, int text) {
        showProgress(context, isShowProgress, context.getResources().getString(text));
    }

    public synchronized static void showProgress(final Context context, final boolean isShowProgress, final String text) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msg = text;
                if (TextUtils.isEmpty(text)) {
                    msg = context.getResources().getString(R.string.cn_loading_now);
                }

                try {
                    if (null == mProgressDialog || !mProgressDialog.isShowing()) {
                        dismissProgress(context);

                        mProgressDialog = new ProgressDialog(context);
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        mProgressDialog.setMessage(msg);
//                    if (isShowProgress) {
//                        mProgressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress_gif));
//                    }
                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface arg0) {
                            }
                        });
                        mProgressDialog.show();
                    } else {
                        mProgressDialog.setMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public synchronized static void dismissProgress(Context context) {
        if (null != context) {
            if (context instanceof Activity && !((Activity) context).isFinishing()) {
                dismiss();
            } else {
                dismiss();
            }
        }
    }

    private static void dismiss() {
        try {
            if (null != mProgressDialog) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
