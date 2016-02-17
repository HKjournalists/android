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
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Administrator on 2015/5/14.
 */
public class StartUpSecondFragment extends Fragment {
    private View o1, o2, o3, title;
    private AnimatorSet mSet;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_start_up_second, container, false);
        o1 = layout.findViewById(R.id.o1);
        o2 = layout.findViewById(R.id.o2);
        o3 = layout.findViewById(R.id.o3);
        title = layout.findViewById(R.id.title);
        init();
        onPageSelected();
        return layout;
    }

    private void init(){
        if (mSet != null)
            mSet.cancel();
        ViewCompat.setAlpha(o1, 0);
        ViewCompat.setAlpha(o2, 0);
        ViewCompat.setAlpha(o3, 0);
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
        animator1.setDuration(500);
        animator1.setStartDelay(500);
        ValueAnimator animator2 = ValueAnimator.ofFloat(0, 1);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setScaleX(o2, curValue);
                ViewCompat.setScaleY(o2, curValue);
                ViewCompat.setAlpha(o2, curValue);
            }
        });
        animator2.setInterpolator(new BounceInterpolator());
        animator2.setDuration(1000);
        animator2.setStartDelay(500);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(o3, "Alpha", 0, 1);
        animator3.setRepeatCount(2);
        animator3.setDuration(250);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(title, "Alpha", 0, 1);
        animator4.setDuration(1500);
        animator4.setStartDelay(750);
        mSet.play(animator1);
        mSet.play(animator2).after(animator1);
        mSet.play(animator3).after(animator2);
        mSet.play(animator4).after(animator2);
        mSet.start();
    }
}
