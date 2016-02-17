package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.kplus.car.R;

import java.util.Hashtable;

/**
 * Description 根据子view计算是否在一行中能显示，否则换行显示
 * <br/><br/>Created by FU ZHIXUE on 2015/7/8.
 * <br/><br/>
 */
public class FlowLayout extends RelativeLayout {
    private Context mContext = null;
    private int margin = 9;

    int mLeft, mRight, mTop, mBottom, currentBottom;
    Hashtable<View, Position> map = new Hashtable<>();

    public FlowLayout(Context context) {
        this(context, null, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        margin = mContext.getResources().getDimensionPixelSize(R.dimen.daze_item_top);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            Position pos = map.get(child);
            if (pos != null) {
                child.layout(pos.left, pos.top, pos.right, pos.bottom);
            }
        }

    }

    public int getPosition(int IndexInRow, int childIndex) {
        if (IndexInRow > 0) {
            return getPosition(IndexInRow - 1, childIndex - 1) + getChildAt(childIndex - 1).getMeasuredWidth() + margin;
        }
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mLeft = 0;
        mRight = 0;
        mTop = 0;
        mBottom = 0;
        int j = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            Position position = new Position();
            View view = getChildAt(i);
            mLeft = getPosition(i - j, i);
            mRight = mLeft + view.getMeasuredWidth();
            if (mRight >= width) {
                j = i;
                mLeft = getPosition(i - j, i);
                mRight = mLeft + view.getMeasuredWidth();
                mTop += getChildAt(i).getMeasuredHeight() + margin;
            }
            mBottom = mTop + view.getMeasuredHeight();
            position.left = mLeft;
            position.top = mTop;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(view, position);
        }
        setMeasuredDimension(width, mBottom);
    }

    private class Position {
        int left, top, right, bottom;
    }

}
