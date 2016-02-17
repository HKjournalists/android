package com.kplus.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.kplus.car.R;

/**
 * Created by Administrator on 2015/6/10.
 */
public class DelSlideListView extends ListView implements GestureDetector.OnGestureListener, View.OnTouchListener {
    private GestureDetector mDetector;
    private int standard_touch_target_size = 0;
    private float mLastMotionX;
    // 有item被拉出
    public boolean deleteView = false;
    // 当前拉出的view
    private ScrollLinearLayout mScrollLinearLayout = null;
    // 滑动着
    private boolean scroll = false;
    // 禁止拖动
    private boolean forbidScroll = false;
    // 禁止拖动
    private boolean clicksameone = false;
    // 当前拉出的位置
    private int position;
    // 消息冻结
    private boolean freeze = false;

    public DelSlideListView(Context context) {
        super(context);
        init(context);
    }

    public DelSlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context mContext) {
        mDetector = new GestureDetector(mContext, this);
        standard_touch_target_size = (int) getResources().getDimension(R.dimen.delete_action_len);
        this.setOnTouchListener(this);
    }

    public void reset(boolean noaction) {
        position = -1;
        deleteView = false;
        if (mScrollLinearLayout != null) {
            if (!noaction) {
                mScrollLinearLayout.snapToScreen(0);
            } else {
                mScrollLinearLayout.scrollTo(0, 0);
            }
            mScrollLinearLayout = null;
        }
        scroll = false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mLastMotionX = e.getX();
        int p = this.pointToPosition((int) e.getX(), (int) e.getY()) - this.getFirstVisiblePosition();
        if (deleteView) {
            if (p != position) {
                // 吃掉，不在有消息
                freeze = true;
                return true;
            } else {
                clicksameone = true;
            }
        }
        position = p;
        scroll = false;
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // 第二次
        if (scroll) {
            int deltaX = (int) (mLastMotionX - e2.getX());
            if (deleteView) {
                deltaX += standard_touch_target_size;
            }
            if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
                mScrollLinearLayout.scrollBy(deltaX - mScrollLinearLayout.getScrollX(), 0);
            }
            return true;
        }
        if (!forbidScroll) {
            forbidScroll = true;
            // x方向滑动，才开始拉动
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                mScrollLinearLayout = (ScrollLinearLayout) this.getChildAt(position);
                scroll = true;
                if (mScrollLinearLayout != null) {
                    int deltaX = (int) (mLastMotionX - e2.getX());
                    if (deleteView) {
                        // 再次点击的时候，要把deltaX增加
                        deltaX += standard_touch_target_size;
                    }
                    if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
                        mScrollLinearLayout.scrollBy((int) (e1.getX() - e2.getX()), 0);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            boolean isfreeze = freeze;
            boolean isclicksameone = clicksameone;
            forbidScroll = false;
            clicksameone = false;
            freeze = false;
            if (isfreeze) {
                // 上一个跟当前点击不一致 还原
                reset(false);
                return true;
            }
            int deltaX2 = (int) (mLastMotionX - event.getX());
            // 不存在
            if (scroll && deltaX2 >= standard_touch_target_size / 2) {
                mScrollLinearLayout.snapToScreen(standard_touch_target_size);
                deleteView = true;
                scroll = false;
                return true;
            }
            if (deleteView && scroll && deltaX2 >= -standard_touch_target_size / 2) {
                mScrollLinearLayout.snapToScreen(standard_touch_target_size);
                deleteView = true;
                scroll = false;
                return true;
            }
            if(isclicksameone||scroll){
                reset(false);
                return true;
            }
            reset(false);
        }
        if (freeze) {
            return true;
        }
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scroll || deleteView) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
