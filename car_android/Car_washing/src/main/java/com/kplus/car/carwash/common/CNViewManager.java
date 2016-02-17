package com.kplus.car.carwash.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kplus.car.carwash.R;
import com.kplus.car.carwash.utils.Log;

import java.util.Stack;

/**
 * class to manage all views/activities
 * Created by Fu on 2015/5/5.
 */
public final class CNViewManager {
    private static final String TAG = "CNViewManager";

    /**
     * no intent specify tags
     */
    public static final int NO_FLAGS = Const.NEGATIVE;

    private Stack<Activity> mActivityStack = null;

    private static CNViewManager ins = null;

    public static CNViewManager getIns() {
        if (null == ins) {
            synchronized (CNViewManager.class) {
                ins = new CNViewManager();
            }
        }
        return ins;
    }

    private CNViewManager() {
        mActivityStack = new Stack<>();
    }

    public int getSize() {
        return mActivityStack.size();
    }

    public boolean isExist(String className) {
        for (Activity activity : mActivityStack) {
            if (className.equals(activity.getComponentName().getClassName()))
                return true;
        }
        return false;
    }

    public Activity getActivity(String className) {
        for (Activity activity : mActivityStack) {
            if (className.equals(activity.getComponentName().getClassName()))
                return activity;
        }
        return null;
    }

    /**
     * 通过index获得栈内的activity
     *
     * @param index index
     * @return Activity
     */
    public Activity getActivityAtIndex(int index) {
        if (null == mActivityStack) {
            return null;
        }

        int size = mActivityStack.size();
        if (index < Const.NONE || index >= size) {
            return null;
        }
        return mActivityStack.get(index);
    }

    /**
     * 获取栈内第一个Activity
     *
     * @return Activity
     */
    public Activity getFristActivity() {
        if (null == mActivityStack) {
            return null;
        }
        return getActivityAtIndex(Const.NONE);
    }

    /**
     * 获取栈内最后一个Activity
     *
     * @return Activity
     */
    public Activity getLastActivity() {
        if (null == mActivityStack) {
            return null;
        }
        int size = mActivityStack.size();
        return getActivityAtIndex(size - Const.ONE);
    }

    /**
     * push target activity info the stack
     *
     * @param activity activity
     */
    public void push(Activity activity) {
        if (null == mActivityStack)
            return;
        mActivityStack.push(activity);
    }

    /**
     * pop up the current activity from stack
     */
    public void pop() {
        Activity currentActivity = mActivityStack.pop();
        if (null != currentActivity) {
            pop(currentActivity);
        }
    }

    /**
     * pop up the current activity from stack
     *
     * @param activity activity
     */
    public void pop(Activity activity) {
        if (null == activity)
            return;
        popWithoutFinish(activity);
        activity.finish();
        activity.overridePendingTransition(R.anim.cn_no_anim, R.anim.cn_slide_out_to_right);
    }

    public void pop(Class<?> cls) {
        if (null == cls) {
            return;
        }

        if (null != mActivityStack) {
            Activity act;
            int size = mActivityStack.size();
            for (int i = 0; i < size; i++) {
                act = mActivityStack.get(i);
                if (act.getClass().getName().equals(cls.getName())) {
                    pop(act);
                    break;
                }
            }
        }
    }

    public void pop(Activity activity, int enterAnim, int exitAnim) {
        pop(activity);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * pop up the current activity from stack without finish
     *
     * @param activity
     */
    public void popWithoutFinish(Activity activity) {
        if (null == activity) {
            Log.trace(TAG, "popWithoutFinish activity is null");
            return;
        }
        mActivityStack.remove(activity);
    }

    /**
     * pop all activities
     */
    public synchronized void popAllActivity() {
        if (null != mActivityStack) {
            int size = mActivityStack.size();
            for (int i = Const.NONE; i < size; i++) {
                mActivityStack.get(i).finish();
            }
            mActivityStack.clear();
        }
    }

    public void showActivity(Context context, Class<?> c, boolean isFinish) {
        showActivity(context, c, isFinish, R.anim.cn_slide_in_from_right, R.anim.cn_slide_out_to_left);
    }

    public void showActivity(Context context, Class<?> c, boolean isFinish, int enterAnim, int exitAnim) {
        Intent intent = new Intent(context, c);
        showActivity(context, intent, isFinish, enterAnim, exitAnim);
    }

    public void showActivity(Context context, Intent intent, boolean isFinish) {
        showActivity(context, intent, isFinish, R.anim.cn_slide_in_from_right, R.anim.cn_slide_out_to_left);
    }

    public void showActivity(Context context, Intent intent, boolean isFinish, int enterAnim, int exitAnim) {
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        if (isFinish) {
            pop(((Activity) context));
        }
    }

    public void showActivity(Context context, Intent intent, int requestCode) {
        showActivity(context, intent, requestCode, R.anim.cn_slide_in_from_right, R.anim.cn_slide_out_to_left);
    }

    public void showActivity(Context context, Intent intent, int requestCode, int enterAnim, int exitAnim) {
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
    }
}
