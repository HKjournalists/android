package com.kplus.car;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.kplus.car.util.EventAnalysisUtil;

/**
 * Description：
 * <br/><br/>Created by FU ZHIXUE on 2015/8/17.
 * <br/><br/>
 */
public class DazeActivityLifeCycleCallback implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "DazeActivityLifeCycleCallback";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        EventAnalysisUtil.analysisLoginActive(activity);
        EventAnalysisUtil.push(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        EventAnalysisUtil.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
