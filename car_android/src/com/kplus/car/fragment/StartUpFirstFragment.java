package com.kplus.car.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;

import com.kplus.car.R;
import com.kplus.car.activity.BaseActivity;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Administrator on 2015/5/14.
 */
public class StartUpFirstFragment extends Fragment {
    private View o1, o2, o3, o4, title;
    private AnimatorSet mSet;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_start_up_first, container, false);
        o1 = layout.findViewById(R.id.o1);
        o2 = layout.findViewById(R.id.o2);
        o3 = layout.findViewById(R.id.o3);
        o4 = layout.findViewById(R.id.o4);
        title = layout.findViewById(R.id.title);
        init();
        onPageSelected();
        return layout;
    }

    private void init(){
        if (mSet != null)
            mSet.cancel();
        ViewCompat.setTranslationY(o1, BaseActivity.dip2px(getActivity(), -18));
        ViewCompat.setAlpha(o1, 0);
        ViewCompat.setAlpha(o2, 0);
        ViewCompat.setAlpha(o3, 0);
        ViewCompat.setAlpha(o4, 0);
        ViewCompat.setAlpha(title, 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && isAdded())
            init();
    }

    public void onPageSelected(){
        mSet = new AnimatorSet();
        ValueAnimator animator1 = ValueAnimator.ofFloat(0, 1);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setScaleX(o1, curValue);
                ViewCompat.setScaleY(o1, curValue);
                ViewCompat.setAlpha(o1, curValue);
            }
        });
        animator1.setInterpolator(new BounceInterpolator());
        animator1.setDuration(750);
        animator1.setStartDelay(500);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(o1, "TranslationY", BaseActivity.dip2px(getActivity(), -18), 0);
        animator2.setDuration(1000);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(o3, "Alpha", 0, 1);
        animator3.setDuration(2000);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(o2, "Alpha", 0, 1);
        animator4.setDuration(500);
        animator4.setStartDelay(100);
        ValueAnimator animator5 = ValueAnimator.ofFloat(0, 1);
        animator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setScaleX(o4, (1.1f - curValue) * 10);
                ViewCompat.setScaleY(o4, (1.1f - curValue) * 10);
                ViewCompat.setAlpha(o4, curValue);
            }
        });
        animator5.setDuration(400);
        animator5.setStartDelay(500);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(title, "Alpha", 0, 1);
        animator6.setDuration(1500);
        animator6.setStartDelay(500);
        mSet.play(animator1);
        mSet.play(animator2).after(animator1);
        mSet.play(animator3).after(animator1);
        mSet.play(animator4).after(animator1);
        mSet.play(animator5).after(animator1);
        mSet.play(animator6).after(animator1);
        mSet.start();
    }
}
