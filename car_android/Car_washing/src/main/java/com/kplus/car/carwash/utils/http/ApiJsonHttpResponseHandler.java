package com.kplus.car.carwash.utils.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.utils.http.asynchttp.JsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * Created by Fu on 2015/5/17.
 */
public class ApiJsonHttpResponseHandler extends JsonHttpResponseHandler {
    private static final String TAG = "ApiJsonHttpResponseHandler";

    public static final String SUCCESS = "ok";

    private ApiHandler mHandler;
    private Context mContext;
    private String mUrl;
    private boolean mIsProgress;
    private int mProgressAnim;
    private int mMessage;

    public ApiJsonHttpResponseHandler(ApiHandler handler) {
        mHandler = handler;
    }

    private boolean isSuccess(BaseInfo info) {
        if (null != info && info.getResult().equals(SUCCESS))
            return true;
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsProgress)
            showProgress(mContext, mMessage);
    }

    public void onSuccess(JSONObject response, final BaseInfo info) {
        super.onSuccess(response);
        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isSuccess(info))
                        mHandler.onSuccess(info);
                    else
                        mHandler.onFailure(info);
                }
            });
        } else {
            if (isSuccess(info))
                mHandler.onSuccess(info);
            else
                mHandler.onFailure(info);
        }

    }

    @Override
    public void onFailure(Throwable error, String content) {
        super.onFailure(error, content);
        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mHandler.onFailure(null);
                }
            });
        } else {
            mHandler.onFailure(null);
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (mIsProgress) {
            dismissProgress(mContext);
        }
    }

    private ProgressDialog progressDialog;

    public void showProgress(final Context context, final int text) {
        if (((Activity) context).isFinishing())
            return;
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog
                            .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage(context.getString(text));
                    progressDialog.setIndeterminateDrawable(context
                            .getResources().getDrawable(mProgressAnim));
                    progressDialog
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface arg0) {
                                }
                            });
                }
                progressDialog.show();
            }
        });
    }

    public boolean dismissProgress(Context context) {
        if (context instanceof Activity
                && ((Activity) context).isFinishing()) {
            return false;
        }

        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                return true;
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                progressDialog = null;
            }
            return true;
        }
        return false;
    }
}
