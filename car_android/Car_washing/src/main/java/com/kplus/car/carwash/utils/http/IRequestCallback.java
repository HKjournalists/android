package com.kplus.car.carwash.utils.http;

/**
 * 网络请求回调
 * Created by Fu on 2015/5/8.
 */
public interface IRequestCallback {
    /**
     * 结果回调
     *
     * @param key      请求的接口
     * @param response 返回的结果
     */
    void onFinishWithError(String key, ResponseEntity response);
}
