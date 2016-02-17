package com.kplus.car.carwash.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kplus.car.carwash.R;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.module.CNCarWashApp;
import com.kplus.car.carwash.utils.http.asynchttp.AsyncHttpClient;
import com.kplus.car.carwash.utils.http.asynchttp.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * GPS定位
 * Created on : 2014-6-30 下午2:05:47
 * author FU ZHIXUE
 */
public class CNBDLocation {
    private static final String TAG = "CNBDLocation";

    /**
     * 百度GPS转换的API地址，x为经度，y为纬度
     */
    private static final String bmapBase64Encode = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=%1$s&y=%2$s";

    private static final int GET_SUCCESS = Const.NONE;
    private static final int GET_GPS_FAILED = Const.ONE;
    private static final int GET_CITY = Const.TWO;

    private Context mContext = null;

    public LocationClient mLocationClient = null;
    private TLLocationListener mLocationListener = null;
    private TLLocationCallbackListener mCallbackListener = null;

    private double gpsLat = Const.NEGATIVE;
    private double gpsLon = Const.NEGATIVE;
    private String mCity = null;

    private boolean isFirstLoc = true;// 是否首次定位

    /**
     * 是否要从服务器上根据GPS获取城市名称
     */
    private boolean isGetCityName = false;

    /**
     * 是否从服务器上根据取到的GPS得到城市
     *
     * @param isCityName true 从服务器上获取，false获取
     */
    public void isGetCityName(boolean isCityName) {
        this.isGetCityName = isCityName;
    }

    /**
     * 设置回调
     *
     * @param callback callback
     */
    public void setLocationCallbackListener(TLLocationCallbackListener callback) {
        this.mCallbackListener = callback;
    }

    /**
     * 在Application中初始化
     *
     * @param context 为getApplicationContext();
     */
    public void initLocation(Context context) {
        this.mContext = context;

        mLocationClient = new LocationClient(context);
        mLocationListener = new TLLocationListener();

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        /**
         * 默认值gcj02
         * 1、bd09ll 返回是百度加密经纬度坐标
         * 2、bd09   返回是百度加密墨卡托经纬度坐标
         * 3、gcj02  返回是国测局加密经纬度坐标
         *
         * 这里注意，如果不使用bd09ll百度加密纬度坐标，反转成真实GPS时会出现错误，只有为bd09ll百度加密纬度坐标时，转成的GPS坐标为真实的
         * */
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.registerLocationListener(mLocationListener);
            mLocationClient.start();
            mLocationClient.requestLocation();
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
            mLocationClient.stop();
        }
    }

    /**
     * 定位成功
     */
    private class TLLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null == location)
                return;

            if (isFirstLoc) {
                isFirstLoc = false;
                return;
            }

            int locType = location.getLocType();
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();
            float radius = location.getRadius();

            String city = location.getCity();

            mCity = city;

            String strMsg = "error code is " + locType;
            strMsg += ", latitude is " + latitude;
            strMsg += ", longitude is " + longitude;
            strMsg += ", radius is " + radius;
            strMsg += ", city is " + city;

            boolean isStop;

            if (locType == BDLocation.TypeGpsLocation) {
                float speed = location.getSpeed();
                int statelliteNumber = location.getSatelliteNumber();

                strMsg += ", speed is " + speed;
                strMsg += ", statelliteNumber is " + statelliteNumber;

                isStop = true;
            } else if (locType == BDLocation.TypeNetWorkLocation) {
                String strAddrStr = location.getAddrStr();
                strMsg += ", strAddrStr is " + strAddrStr;

                isStop = true;
            } else {
                Log.trace(TAG, "异常：error code is " + locType + " 具体参数错误结果");

                isStop = false;
            }

            strMsg = "获取的位置是-->" + strMsg;

            Log.trace(TAG, strMsg);

            // 定位成功了，去停止定位服务
            stopLocation();

            if (isStop) {
                tran(latitude, longitude);
            } else {
//              获取error code：
//              61 ： GPS定位结果
//              62 ： 扫描整合定位依据失败。此时定位结果无效。
//              63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
//              65 ： 定位缓存的结果。
//              66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
//              67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
//              68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
//              161： 表示网络定位结果
//              162~167： 服务端定位失败
//              502：key参数错误
//              505：key不存在或者非法
//              601：key服务被开发者自己禁用
//              602：key mcode不匹配
//              501～700：key验证失败

                if (null != mCallbackListener) {
                    String msg = mContext.getResources().getString(R.string.cn_get_location_failed);
                    mCallbackListener.onFailed(msg);
                }
            }
        }
    }

    /**
     * 将百度经纬度坐标转为真实的GPS坐标
     *
     * @param baiduLat 百度获取的纬度
     * @param baiduLon 百度获取的经度
     */
    private void tran(final double baiduLat, final double baiduLon) {
        // 这里起个线程去解GPS
        String url = String.format(bmapBase64Encode, String.valueOf(baiduLon), String.valueOf(baiduLat));
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);

                String json = content;
                // 如果json为空
                if (CNStringUtil.isEmpty(json)) {
                    mHandler.sendEmptyMessage(GET_GPS_FAILED);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        Log.trace(TAG, jsonObject.toString());

                        int error = jsonObject.getInt("error");

                        if (error == 0) {
                            String x = jsonObject.getString("x");
                            String y = jsonObject.getString("y");

                            Double lon1 = CNNumberUtils.stringToDouble(Base64.decode2String(x));
                            Double lat1 = CNNumberUtils.stringToDouble(Base64.decode2String(y));

                            // 这里的就是GPS坐标 经过下面算法转换基本上与真实的GPS坐标相差无几
                            Double gpsLat = baiduLat * 2 - lat1;
                            Double gpsLon = baiduLon * 2 - lon1;

                            // 保留6位小数
                            NumberFormat numFormat = NumberFormat.getNumberInstance();
                            numFormat.setMaximumFractionDigits(6);
                            String strGpsLat = numFormat.format(gpsLat);
                            String strGpsLon = numFormat.format(gpsLon);
                            gpsLat = Double.parseDouble(strGpsLat);
                            gpsLon = Double.parseDouble(strGpsLon);

                            CNBDLocation.this.gpsLat = gpsLat;
                            CNBDLocation.this.gpsLon = gpsLon;

                            if (!isGetCityName) {
                                // 不获取城市名称
                                sendCityToHandler("");
                            } else {
                                // 要获取城市名称
                                mHandler.sendEmptyMessage(GET_CITY);
                            }
                        }

                    } catch (JSONException e) {
                        mHandler.sendEmptyMessage(GET_GPS_FAILED);
                        Log.trace(TAG, "JSONException " + e);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                mHandler.sendEmptyMessage(GET_GPS_FAILED);
                error.printStackTrace();
            }
        });
//        client.post(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                if (statusCode == 200) {
//                    String json = new String(responseBody);
//                    // 如果json为空
//                    if (CNStringUtil.isEmpty(json)) {
//                        mHandler.sendEmptyMessage(GET_GPS_FAILED);
//                    } else {
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            Log.trace(TAG, jsonObject.toString());
//
//                            int error = jsonObject.getInt("error");
//
//                            if (error == 0) {
//                                String x = jsonObject.getString("x");
//                                String y = jsonObject.getString("y");
//
//                                Double lon1 = CNNumberUtils.stringToDouble(Base64.decode2String(x));
//                                Double lat1 = CNNumberUtils.stringToDouble(Base64.decode2String(y));
//
//                                // 这里的就是GPS坐标 经过下面算法转换基本上与真实的GPS坐标相差无几
//                                Double gpsLat = baiduLat * 2 - lat1;
//                                Double gpsLon = baiduLon * 2 - lon1;
//
//                                // 保留6位小数
//                                NumberFormat numFormat = NumberFormat.getNumberInstance();
//                                numFormat.setMaximumFractionDigits(6);
//                                String strGpsLat = numFormat.format(gpsLat);
//                                String strGpsLon = numFormat.format(gpsLon);
//                                gpsLat = Double.parseDouble(strGpsLat);
//                                gpsLon = Double.parseDouble(strGpsLon);
//
//                                CNBDLocation.this.gpsLat = gpsLat;
//                                CNBDLocation.this.gpsLon = gpsLon;
//
//                                if (!isGetCityName) {
//                                    // 不获取城市名称
//                                    sendCityToHandler("");
//                                } else {
//                                    // 要获取城市名称
//                                    mHandler.sendEmptyMessage(GET_CITY);
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            mHandler.sendEmptyMessage(GET_GPS_FAILED);
//                            Log.trace(TAG, "JSONException " + e);
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                mHandler.sendEmptyMessage(GET_GPS_FAILED);
//                error.printStackTrace();
//            }
//        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url = String.format(bmapBase64Encode, String.valueOf(baiduLon), String.valueOf(baiduLat));
//
//                // 从网络获取
//                ResponseEntity response = FastHttp.post(url);
//                String json = response.getContentAsString();
//                // 如果json为空
//                if (CNStringUtil.isEmpty(json)) {
//                    mHandler.sendEmptyMessage(GET_GPS_FAILED);
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(json);
//                        Log.trace(TAG, jsonObject.toString());
//
//                        int error = jsonObject.getInt("error");
//
//                        if (error == 0) {
//                            String x = jsonObject.getString("x");
//                            String y = jsonObject.getString("y");
//
//                            Double lon1 = CNNumberUtils.stringToDouble(Base64.decode2String(x));
//                            Double lat1 = CNNumberUtils.stringToDouble(Base64.decode2String(y));
//
//                            // 这里的就是GPS坐标 经过下面算法转换基本上与真实的GPS坐标相差无几
//                            Double gpsLat = baiduLat * 2 - lat1;
//                            Double gpsLon = baiduLon * 2 - lon1;
//
//                            // 保留6位小数
//                            NumberFormat numFormat = NumberFormat.getNumberInstance();
//                            numFormat.setMaximumFractionDigits(6);
//                            String strGpsLat = numFormat.format(gpsLat);
//                            String strGpsLon = numFormat.format(gpsLon);
//                            gpsLat = Double.parseDouble(strGpsLat);
//                            gpsLon = Double.parseDouble(strGpsLon);
//
//                            CNBDLocation.this.gpsLat = gpsLat;
//                            CNBDLocation.this.gpsLon = gpsLon;
//
//                            if (!isGetCityName) {
//                                // 不获取城市名称
//                                sendCityToHandler("");
//                            } else {
//                                // 要获取城市名称
//                                mHandler.sendEmptyMessage(GET_CITY);
//                            }
//                        }
//
//                    } catch (JSONException e) {
//                        mHandler.sendEmptyMessage(GET_GPS_FAILED);
//                        Log.trace(TAG, "JSONException " + e);
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    private Handler mHandler = new Handler(CNCarWashApp.getIns().getMainLooper()) {
        public void handleMessage(Message msg) {
            String errMsg;
            switch (msg.what) {
                case GET_GPS_FAILED:
                    errMsg = mContext.getResources().getString(R.string.cn_get_gps_failed);
                    onFailed(errMsg);
                    break;

                case GET_SUCCESS:
                    String city = "";
                    if (CNStringUtil.isNotEmpty(msg.obj)) {
                        city = msg.obj.toString();
                    }

                    if (null != mCallbackListener) {
                        mCallbackListener.onSuccess(gpsLat, gpsLon, city);
                    }
                    break;

                case GET_CITY:
                    if (null != mCallbackListener) {
                        mCallbackListener.onSuccess(gpsLat, gpsLon, mCity);
                    }
                    break;
            }
        }
    };

    private void sendCityToHandler(String city) {
        Message msg = mHandler.obtainMessage();
        msg.what = GET_SUCCESS;
        msg.obj = city;
        mHandler.sendMessage(msg);
    }

    private void onFailed(String msg) {
        if (null != mCallbackListener) {
            mCallbackListener.onFailed(msg);
        }
    }

    public interface TLLocationCallbackListener {
        void onSuccess(double latitude, double longitude, String city);

        void onFailed(String errorMsg);
    }
}
