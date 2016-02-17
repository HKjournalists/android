package com.kplus.car.carwash.module;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.kplus.car.carwash.callback.Future;
import com.kplus.car.carwash.callback.FutureListener;
import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.utils.Log;
import com.kplus.car.carwash.utils.PriorityThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * Created by Fu on 2015/5/26.
 */
public class CNThreadPool {
    private static final String TAG = "CNThreadPool";

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 8;
    private static final int KEEP_ALIVE_TIME = 10; // 10 seconds


    public interface Job<T> {
        T run();
    }

    public interface CancelListener {
        void onCancel();
    }

    class TLMessageHandler<T> extends Handler {
        private final Future<T> mFuture;
        private FutureListener<T> mListener;

        public TLMessageHandler(Looper looper, Future<T> future, FutureListener<T> listener) {
            super(looper);
            this.mFuture = future;
            this.mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Const.NONE) {
                if (null != mListener) {
                    mListener.onFutureDone(mFuture);
                }
            }
        }
    }

    private final Executor mExecutor;

    public CNThreadPool() {
        mExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new PriorityThreadFactory("car-washing-thread-pool", android.os.Process.THREAD_PRIORITY_DEFAULT));
    }

    public <T> Future<T> submit(Job<T> job) {
        return submit(job, null);
    }

    public <T> Future<T> submit(Job<T> job, FutureListener<T> listener) {
        ChatWorker<T> worker = new ChatWorker<T>(job, listener);
        mExecutor.execute(worker);
        return worker;
    }

    private class ChatWorker<T> implements Runnable, Future<T> {
        private Job<T> mJob;
        private CancelListener mCancelListener;
        private TLMessageHandler mHandler;

        private volatile boolean mIsCancelled;
        private boolean mIsDone;
        private T mResult;


        public ChatWorker(Job<T> job, FutureListener<T> listener) {
            this.mJob = job;
            if (null != listener) {
                this.mHandler = new TLMessageHandler(CNCarWashApp.getIns().getMainLooper(), this, listener);
            }
        }

        @Override
        public void run() {
            T result = null;
            try {
                result = mJob.run();
            } catch (Throwable ex) {
                Log.trace(TAG, "Exception in running a job-->" + ex);
                ex.printStackTrace();
            }

            synchronized (this) {
                mResult = result;
                mIsDone = true;
                notifyAll();
            }
            if (null != this.mHandler) {
                this.mHandler.sendEmptyMessage(Const.NONE);
            }
        }

        // Below are the methods for Future.
        public synchronized void cancel() {
            if (mIsCancelled)
                return;
            mIsCancelled = true;
            if (null != mCancelListener) {
                mCancelListener.onCancel();
            }
        }

        public boolean isCancelled() {
            return mIsCancelled;
        }

        public synchronized boolean isDone() {
            return mIsDone;
        }

        public synchronized T get() {
            while (!mIsDone) {
                try {
                    wait();
                } catch (Exception ex) {
                    Log.trace(TAG, "ingore exception-->" + ex);
                    ex.printStackTrace();
                }
            }
            return mResult;
        }

        public void waitDone() {
            get();
        }

        // Below are the methods for JobContext (only called from the
        // thread running the job)
        public synchronized void setCancelListener(CancelListener listener) {
            mCancelListener = listener;
            if (mIsCancelled && null != mCancelListener) {
                mCancelListener.onCancel();
            }
        }
    }
}
