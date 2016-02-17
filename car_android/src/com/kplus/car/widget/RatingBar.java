package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kplus.car.R;

/**
 * Created by Administrator on 2015/8/19.
 */
public class RatingBar extends LinearLayout {

    public RatingBar(Context context) {
        super(context);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRating(int max, float rating){
        if (max <= 0)
            return;
        int iRating = Math.round(rating);
        if (iRating < 0)
            iRating = 0;
        else if (iRating > max)
            iRating = max;
        removeAllViews();
        for (int i = 0; i < iRating; i++){
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(R.drawable.rating_full);
            iv.setPadding(1, 1, 1, 1);
            addView(iv);
        }
        for (int i = iRating; i < max; i++){
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(R.drawable.rating_empty);
            iv.setPadding(1, 1, 1, 1);
            addView(iv);
        }
    }
}
