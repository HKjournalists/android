package com.kplus.car.carwash.callback;

/**
 * Created by Fu on 2015/5/26.
 */
public interface Future<T> {
    void cancel();

    boolean isCancelled();

    boolean isDone();

    T get();

    void waitDone();
}
