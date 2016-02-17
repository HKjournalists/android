package com.kplus.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.activity.BaseActivity;
import com.kplus.car.activity.MainUIActivity;
import com.kplus.car.activity.StartUpActivity;
import com.kplus.car.util.ClickUtils;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Administrator on 2015/5/14.
 */
public class StartUpThirdFragment extends Fragment implements ClickUtils.NoFastClickListener {
    private View o1, o2, o3, o4, o5, o6, o7, title;
    private AnimatorSet mSet;
    private TextView btn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_start_up_third, container, false);
        o1 = layout.findViewById(R.id.o1);
        o2 = layout.findViewById(R.id.o2);
        o3 = layout.findViewById(R.id.o3);
        o4 = layout.findViewById(R.id.o4);
        o5 = layout.findViewById(R.id.o5);
        o6 = layout.findViewById(R.id.o6);
        o7 = layout.findViewById(R.id.o7);
        title = layout.findViewById(R.id.title);
        btn = (TextView) layout.findViewById(R.id.btn);
        ClickUtils.setNoFastClickListener(btn, this);
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
        ViewCompat.setAlpha(o4, 0);
        ViewCompat.setAlpha(o5, 0);
        ViewCompat.setAlpha(o6, 0);
        ViewCompat.setAlpha(o7, 0);
        ViewCompat.setAlpha(title, 0);
        ViewCompat.setAlpha(btn, 0);
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
                ViewCompat.setTranslationX(o2, (1 - curValue) * BaseActivity.dip2px(getActivity(), 100));
                ViewCompat.setTranslationY(o2, (1 - curValue) * BaseActivity.dip2px(getActivity(), 100));
            }
        });
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setDuration(800);
        ValueAnimator animator3 = ValueAnimator.ofFloat(0, 1);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setScaleX(o3, curValue);
                ViewCompat.setScaleY(o3, curValue);
                ViewCompat.setAlpha(o3, curValue);
                ViewCompat.setTranslationX(o3, (1 - curValue) * BaseActivity.dip2px(getActivity(), -100));
                ViewCompat.setTranslationY(o3, (1 - curValue) * BaseActivity.dip2px(getActivity(), 100));
            }
        });
        animator3.setInterpolator(new DecelerateInterpolator());
        animator3.setDuration(800);
        animator3.setStartDelay(300);
        ValueAnimator animator4 = ValueAnimator.ofFloat(0, 1);
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setScaleX(o4, curValue);
                ViewCompat.setScaleY(o4, curValue);
                ViewCompat.setAlpha(o4, curValue);
                ViewCompat.setTranslationX(o4, (1 - curValue) * BaseActivity.dip2px(getActivity(), 135));
                ViewCompat.setTranslationY(o4, (1 - curValue) * BaseActivity.dip2px(getActivity(), 15));
            }
        });
        animator4.setInterpolator(new DecelerateInterpolator());
        animator4.setDuration(800);
        animator4.setStartDelay(500);
        ValueAnimator animator5 = ValueAnimator.ofFloat(0, 1);
        animator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setScaleX(o5, curValue);
                ViewCompat.setScaleY(o5, curValue);
                ViewCompat.setAlpha(o5, curValue);
                ViewCompat.setTranslationY(o5, (1 - curValue) * BaseActivity.dip2px(getActivity(), 125));
            }
        });
        animator5.setInterpolator(new DecelerateInterpolator());
        animator5.setDuration(800);
        animator5.setStartDelay(700);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(o6, "Alpha", 0, 1);
        animator6.setInterpolator(new BounceInterpolator());
        animator6.setDuration(500);
        animator6.setStartDelay(800);
        ValueAnimator animator7 = ValueAnimator.ofFloat(0, 1);
        animator7.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setScaleX(o7, curValue);
                ViewCompat.setScaleY(o7, curValue);
                ViewCompat.setAlpha(o7, curValue);
                ViewCompat.setTranslationX(o7, (1 - curValue) * BaseActivity.dip2px(getActivity(), -130));
                ViewCompat.setTranslationY(o7, (1 - curValue) * BaseActivity.dip2px(getActivity(), 20));
            }
        });
        animator7.setInterpolator(new DecelerateInterpolator());
        animator7.setDuration(800);
        animator7.setStartDelay(900);
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(title, "Alpha", 0, 1);
        animator8.setDuration(1500);
        animator8.setStartDelay(1500);
        ValueAnimator animator9 = ValueAnimator.ofFloat(0, 1);
        animator9.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (Float) valueAnimator.getAnimatedValue();
                ViewCompat.setAlpha(btn, curValue);
                ViewCompat.setTranslationY(btn, (1 - curValue) * BaseActivity.dip2px(getActivity(), 50));
            }
        });
        animator9.setInterpolator(new BounceInterpolator());
        animator9.setDuration(1000);
        animator9.setStartDelay(2500);
        mSet.play(animator1);
        mSet.play(animator2).after(animator1);
        mSet.play(animator3).after(animator1);
        mSet.play(animator4).after(animator1);
        mSet.play(animator5).after(animator1);
        mSet.play(animator6).after(animator1);
        mSet.play(animator7).after(animator1);
        mSet.play(animator8).after(animator1);
        mSet.play(animator9).after(animator1);
        mSet.start();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                KplusApplication app = (KplusApplication) getActivity().getApplication();
                app.dbCache.putValue("start_up_version", KplusConstants.START_UP_VERSION);
                Intent intent = new Intent(getActivity(), MainUIActivity.class);
                startActivity(intent);
                ((StartUpActivity)getActivity()).finishWithAnim();
                break;
        }
    }
}
