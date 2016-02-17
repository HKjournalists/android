package com.kplus.car.carwash.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Fu on 2015/5/26.
 */
public class PriorityThreadFactory implements ThreadFactory {
    private final int mPriority;
    private final AtomicInteger mNumber = new AtomicInteger();
    private final String mName;

    public PriorityThreadFactory(String name, int priority) {
        mName = name;
        mPriority = priority;
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, mName + '-' + mNumber.getAndIncrement()) {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(mPriority);
                super.run();
            }
        };
    }
}
