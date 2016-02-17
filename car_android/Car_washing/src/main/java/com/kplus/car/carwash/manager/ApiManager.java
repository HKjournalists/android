package com.kplus.car.carwash.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.http.ApiFunction;
import com.kplus.car.carwash.utils.http.ApiHandler;
import com.kplus.car.carwash.utils.http.HttpRequestParams;
import com.kplus.car.carwash.utils.http.IApi;
import com.kplus.car.carwash.utils.http.SessionApiHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Fu on 2015/5/17.
 */
public class ApiManager {
    private static final String TAG = "ApiManager";

    private static SessionApiHandler mSessionApiHandler;

    public static void initSessionApiHandler(SessionApiHandler sessionApiHandler) {
        if (mSessionApiHandler == null) {
            mSessionApiHandler = sessionApiHandler;
        }
    }

    public static IApi getInitialize(Context context) {
        return getInitialize(context, false, 0);
    }

    public static IApi getInitialize(final Context context, boolean isProgress, int message) {
        return getInitialize(context, isProgress, message, true);
    }

    public static IApi getInitialize(final Context context, boolean isProgress, int message, boolean isCheckNet) {
        if (isCheckNet) {
            if (!CNCommonManager.isNetworkConnected(context)) {
                isProgress = false;
                int msg = R.string.cn_no_net;
                DialogManager.onAffirm(context, msg, new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        CNCommonManager.startWirelessSetting(context);
                    }
                });
            }
        }
        initSessionApiHandler(new SessionApiHandler() {
            @Override
            public void onSessionError(Context context, ApiHandler handler) {
                // Notice 可以这里去执行自动登录等
                Log.trace(TAG, "onSessionError-->走到了initSessionApiHandler的回调结果里了");
            }
        });

        if (message == 0) {
            message = R.string.cn_loading_now;
        }

        IApi api = new ApiFunction(context, HttpRequestParams.getApiUrl(), isProgress, R.drawable.progress_gif, message, mSessionApiHandler);
        return wrapApi(api);
    }

    private static IApi wrapApi(IApi api) {
        return (IApi) Proxy.newProxyInstance(IApi.class.getClassLoader(), new Class[]{IApi.class}, new ApiInvocationHandler(api));
    }

    private static class ApiInvocationHandler implements InvocationHandler {

        private IApi api;

        public ApiInvocationHandler(IApi api) {
            this.api = api;
        }

        @Override
        public Object invoke(Object proxy, final Method method, final Object[] args)
                throws Throwable {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... arg0) {
                    try {
                        method.invoke(api, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
            return null;
        }
    }
}
