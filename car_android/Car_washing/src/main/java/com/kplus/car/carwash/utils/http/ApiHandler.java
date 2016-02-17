package com.kplus.car.carwash.utils.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.manager.CNCommonManager;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;

/**
 * Created by Fu on 2015/5/17.
 */
public class ApiHandler {
    private Context mContext = null;

    public ApiHandler(Context context) {
        mContext = context;
    }

    public void onSuccess(BaseInfo baseInfo) {
    }

    public void onFailure(BaseInfo baseInfo) {
        CNProgressDialogUtil.dismissProgress(mContext);
        if (null == baseInfo) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            boolean isAvailable = false;
            if (mNetworkInfo != null) {
                isAvailable = mNetworkInfo.isAvailable();
            }
            if (isAvailable) {
                String msg = mContext.getResources().getString(R.string.cn_service_error);
                CNCommonManager.makeText(mContext, msg);
            }
        } else {
            CNCommonManager.makeText(mContext, baseInfo.getMsg());
        }
    }


}
