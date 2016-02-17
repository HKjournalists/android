package com.kplus.car.carwash.utils.http;

import android.app.Activity;
import android.content.Context;

import com.kplus.car.carwash.bean.BaseInfo;
import com.kplus.car.carwash.bean.CancelOrderResp;
import com.kplus.car.carwash.bean.FetchCitiesResp;
import com.kplus.car.carwash.bean.FetchCityConfigResp;
import com.kplus.car.carwash.bean.FetchCityRegionResp;
import com.kplus.car.carwash.bean.FetchCityServiceResp;
import com.kplus.car.carwash.bean.FetchCityServingStatusResp;
import com.kplus.car.carwash.bean.FetchCommonCarsResp;
import com.kplus.car.carwash.bean.FetchOrderLogsResp;
import com.kplus.car.carwash.bean.FetchOrderResp;
import com.kplus.car.carwash.bean.FetchPaginationOrderResp;
import com.kplus.car.carwash.bean.FetchServingStatusByRegionsResp;
import com.kplus.car.carwash.bean.FetchSupportCarModelsResp;
import com.kplus.car.carwash.bean.FetchUsableCouponsResp;
import com.kplus.car.carwash.bean.FetchUserHabitsResp;
import com.kplus.car.carwash.bean.InitializeResp;
import com.kplus.car.carwash.bean.LocateServityCityResp;
import com.kplus.car.carwash.bean.SubmitOrderResp;
import com.kplus.car.carwash.module.AppBridgeUtils;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.MD5;
import com.kplus.car.carwash.utils.CNProgressDialogUtil;
import com.kplus.car.carwash.utils.http.asynchttp.AsyncHttpClient;
import com.kplus.car.carwash.utils.http.asynchttp.JsonHttpResponseHandler;
import com.kplus.car.carwash.utils.http.asynchttp.RequestParams;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by Fu on 2015/5/17.
 */
public class ApiFunction implements IApi {
    private static final String TAG = "ApiFunction";

    public static final String SUCCESS = "ok";

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(60000);
    }

    private Context mContext = null;
    private String mUrl;
    private boolean mIsProgress;
    private int mProgressAnim;
    private int mMessage;
    private SessionApiHandler mSessionApiHandler = null;

    public ApiFunction(Context context, String url, boolean isShowProgress,
                       int progressAnim, int message, SessionApiHandler sessionApiHandler) {
        mContext = context;
        mUrl = url;
        mIsProgress = isShowProgress;
        mProgressAnim = progressAnim;
        mMessage = message;
        mSessionApiHandler = sessionApiHandler;
    }

    private class ApiJsonHttpResponseHandler extends JsonHttpResponseHandler {
        private ApiHandler mHandler;
        private String mFunckey;

        public ApiJsonHttpResponseHandler(String funckey, ApiHandler handler) {
            this.mFunckey = funckey;
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
            if (mIsProgress) {
                CNProgressDialogUtil.showProgress(mContext, true, mMessage);
            }
        }

        public void onSuccess(JSONObject response, final BaseInfo info) {
            super.onSuccess(response);
            if (mContext instanceof Activity) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess(info)) {
                            mHandler.onSuccess(info);
                            CNProgressDialogUtil.dismissProgress(mContext);
                        } else {
                            mHandler.onFailure(info);
                        }
                    }
                });
            } else {
                if (isSuccess(info)) {
                    mHandler.onSuccess(info);
                    CNProgressDialogUtil.dismissProgress(mContext);
                } else {
                    mHandler.onFailure(info);
                }
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

            Log.trace(TAG, mFunckey + "-->出现异常-->" + content + "\r\n" + error.getMessage());
            error.printStackTrace();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            if (mIsProgress) {
                CNProgressDialogUtil.dismissProgress(mContext);
            }
        }
    }

    @Override
    public void initialize(String funcKey, String jsonParams, ApiHandler handler) {
        request(jsonParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "初始化接口initialize-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, InitializeResp.class));
            }
        });
    }

    @Override
    public void getLocServiceCity(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getLocServiceCity-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, LocateServityCityResp.class));
            }
        });
    }

    @Override
    public void getLocServiceCityV2(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getLocServiceCityV2-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, LocateServityCityResp.class));
            }
        });
    }

    @Override
    public void getFetchUserHabits(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchUserHabits-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchUserHabitsResp.class));
            }
        });
    }

    @Override
    public void getFetchCityServices(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchCityServices-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchCityServiceResp.class));
            }
        });
    }

    @Override
    public void getFetchCityConfig(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchCityConfig-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchCityConfigResp.class));
            }
        });
    }

    @Override
    public void getFetchCityServingStatus(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchCityServingStatus-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchCityServingStatusResp.class));
            }
        });
    }

    @Override
    public void fetchCityServingStatusByRegions(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "fetchCityServingStatusByRegions-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchServingStatusByRegionsResp.class));
            }
        });
    }

    @Override
    public void getFetchUsableCoupons(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchUsableCoupons-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchUsableCouponsResp.class));
            }
        });
    }

    @Override
    public void getFetchSupportCarModel(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchSupportCarModel-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchSupportCarModelsResp.class));
            }
        });
    }

    @Override
    public void submitOrder(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "submitOrder-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, SubmitOrderResp.class));
            }
        });
    }

    @Override
    public void getFetchOrderLogs(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchOrderLogs-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchOrderLogsResp.class));
            }
        });
    }

    @Override
    public void getFetchPaginationOrders(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "getFetchPaginationOrders-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchPaginationOrderResp.class));
            }
        });
    }

    @Override
    public void cancelOrder(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "cancelOrder-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, CancelOrderResp.class));
            }
        });
    }

    @Override
    public void submitReview(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "submitReview-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, BaseInfo.class));
            }
        });
    }

    @Override
    public void fetchCommonCars(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "fetchCommonCars-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchCommonCarsResp.class));
            }
        });
    }

    @Override
    public void fetchOrder(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "fetchOrder-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchOrderResp.class));
            }
        });
    }

    @Override
    public void fetchCities(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "fetchCities-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchCitiesResp.class));
            }
        });
    }

    @Override
    public void fetchCityRegions(String funcKey, LinkedHashMap<String, Object> mapParams, ApiHandler handler) {
        request(mapParams, funcKey, new ApiJsonHttpResponseHandler(funcKey, handler) {
            @Override
            public void onSuccess(JSONObject response) {
                Log.trace(TAG, "fetchCityRegions-->response-->" + response);
                super.onSuccess(response, JsonToObject.getJsonToModel(response, FetchCityRegionResp.class));
            }
        });
    }

    /**
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////
     * ///////////////////////////////////////下面的私有方法////////////////////////////////////////////////////
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    private void request(LinkedHashMap<String, Object> mapParams, String funcKey, ApiJsonHttpResponseHandler jsonHandler) {
        Log.trace(TAG, "request-->mapParams-->" + mapParams);
        // 把参数放到json中
        String data = JsonToObject.getModeltoJson(mapParams);
        Log.trace(TAG, "request-->json data-->" + data);

        String url = getQueryWithUrl(funcKey);
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.trace(TAG, "request-->url-->" + url);
        clientPost(mContext, funcKey, url, entity, jsonHandler);
    }

    private void request(String jsonParams, String funcKey, ApiJsonHttpResponseHandler jsonHandler) {
        // 把参数放到json中
        Log.trace(TAG, "request-->jsonParams-->" + jsonParams);

        String url = getQueryWithUrl(funcKey);
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonParams.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.trace(TAG, "request-->url-->" + url);
        clientPost(mContext, funcKey, url, entity, jsonHandler);
    }

    public void clientPost(final Context context, final String funcKey, final String url, final ByteArrayEntity entity, final ApiJsonHttpResponseHandler apiJsonHttpResponseHandler) {
        client.post(mContext, url, null, entity, HttpRequestParams.CONTENT_TYPE, new ApiJsonHttpResponseHandler(funcKey, new ApiHandler(context)) {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                BaseInfo baseInfo = JsonToObject.getBaseInfo(response);
                if ("session-error".equals(baseInfo.getResult())) {
                    if (mSessionApiHandler != null) {
                        mSessionApiHandler.onSessionError(context, new ApiHandler(context) {
                            @Override
                            public void onSuccess(BaseInfo baseInfo) {
                                super.onSuccess(baseInfo);
                                clientPost(context, funcKey, url, entity, apiJsonHttpResponseHandler);
                            }
                        });
                    }
                } else {
                    apiJsonHttpResponseHandler.onSuccess(response);
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                apiJsonHttpResponseHandler.onFailure(error, content);
            }
        });
    }

    private String getApiUrl(String key) {
        StringBuffer buffer = new StringBuffer(mUrl);
        if (!mUrl.endsWith("/") && !key.startsWith("/")) {
            buffer.append("/");
        }
        return buffer.append(key).toString();
    }

    private String getQueryWithUrl(String funcKey) {
        String url = getApiUrl(funcKey);
        Log.trace(TAG, "调用接口：" + funcKey + " ， 接口地址：" + url);
        RequestParams params = getRequestParams();
        // 重新返回url
        return AsyncHttpClient.getUrlWithQueryString(url, params);
    }

    /**
     * 生成请求签名信息
     *
     * @return RequestParams
     */
    private RequestParams getRequestParams() {
        String clientAppkey = AppBridgeUtils.getIns().getClientAppKey();
        String cliendAppSecret = AppBridgeUtils.getIns().getClientAppSecret();
        long uid = AppBridgeUtils.getIns().getUid();

        long time = System.currentTimeMillis();

        // 生成签名
        StringBuilder buffer = new StringBuilder();
        buffer.append(clientAppkey);
        buffer.append(cliendAppSecret);
        buffer.append(time);

        Log.trace(TAG, "sign：" + buffer);
        String md5Sign = MD5.md5(buffer.toString());
        Log.trace(TAG, "md5 sign：" + md5Sign);

        RequestParams params = new RequestParams();
        params.put(HttpRequestField.APP_KEY, clientAppkey);
        params.put(HttpRequestField.UID, String.valueOf(uid));
        params.put(HttpRequestField.TIME, time + "");
        params.put(HttpRequestField.API_VERSION, String.valueOf(HttpRequestParams.API_VERSION));
        params.put(HttpRequestField.SIGN, md5Sign);

        Log.trace(TAG, "http request params：" + params.toString());
        return params;
    }
}
