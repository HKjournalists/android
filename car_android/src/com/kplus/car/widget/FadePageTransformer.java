package com.kplus.car.widget;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2015/4/8 0008.
 */
public class FadePageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        ViewCompat.setLayerType(page, ViewCompat.LAYER_TYPE_NONE, null);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left
            ViewCompat.setAlpha(page, 0);
        } else if (position <= 0) { // [-1,0]
            // This page is moving out to the left
            ViewCompat.setAlpha(page, 1 + position);
        } else if (position <= 1) { // (0,1]
            // This page is moving in from the right
            ViewCompat.setAlpha(page, 1 - position);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right
            ViewCompat.setAlpha(page, 0);
        }
    }
}
