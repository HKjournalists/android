package com.kplus.car.carwash.callback;


/**
 * Created by Fu on 2015/5/26.
 */
public interface FutureListener<T> {
    void onFutureDone(Future<T> future);
}
