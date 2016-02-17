package com.kplus.car.carwash.module;

import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.kplus.car.carwash.bean.City;
import com.kplus.car.carwash.common.CNViewManager;
import com.kplus.car.carwash.common.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：全局Application
 * <br/><br/>Created by Fu on 2015/5/5.
 * <br/><br/>
 */
public class CNCarWashApp extends MultiDexApplication implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CNCarWashApp";

    /**
     * 从服务器取来的城市
     */
    public List<City> mCitys = new ArrayList<>();
    /**
     * 推荐城市的id
     */
    public long mRecommendCityId = Const.NONE;
    /**
     * 当前城市
     */
    public City mLocatedCity = null;
    /**
     * 当前选择的城市，如果没有选择，默认是定位回来的城市
     */
    public City mSelectedCity = null;

    private CNThreadPool mThreadPool = null;

    private static CNCarWashApp ins = null;

    public static CNCarWashApp getIns() {
        return ins;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        /**
         * 继承了MutiDexApplication或者覆写了Application中的attachBaseContext()方法
         * Application 中的静态全局变量会比MutiDex的 instal()方法优先加载，所以建议
         * 避免在Application类中使用main classes.dex文件中可能使用到的静态变量，可以
         * 根据如下所示的方式进行修改
         */
        final Context context = this;
        new Runnable() {
            @Override
            public void run() {
                ins = CNCarWashApp.this;

                // 初始化
                InitializationObject.initialization(context);

                // Notice 拦截异常报警回调
                Thread.setDefaultUncaughtExceptionHandler(CNCarWashApp.this);
            }
        }.run();
    }

    /**
     * 初始化线程池对象
     *
     * @return
     */
    public synchronized CNThreadPool getThreadPool() {
        if (null == mThreadPool) {
            mThreadPool = new CNThreadPool();
        }
        return mThreadPool;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        // clear activity stacks
        CNViewManager.getIns().popAllActivity();
        // Notice 两个作用一样，只会执行一样
//        System.exit(Const.ONE);
        Process.killProcess(Process.myPid());
    }

    /**
     * 退出洗车界面时要，调用清理释放
     */
    public void release() {
        if (null != mCitys) {
            mCitys.clear();
            mCitys = null;
        }
        mRecommendCityId = Const.NONE;
        mLocatedCity = null;
        mSelectedCity = null;
        System.gc();
    }
}
