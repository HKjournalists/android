package com.kplus.car.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by 6a209 on 14/10/19.
 *
 * just support ScrollView & RecycleView
 *
 *
 */
public abstract class RefreshLayout extends ViewGroup{

    private static final String TAG = "PullRefreshLayout";

    private static final int PULL_TO_REFRESH_STATUS = 0x00;
    private static final int NORMAL_STATUS = 0x03;

    View mRefreshView;
    private int mCurStatus;
    private int mLastMotionY;
    private int mActionDownY;
    private int mTouchSlop;
    private boolean mIsBeingDragged;
    private int mShortAnimationDuration;
    private int mToPosition;
    private int mOriginalOffsetTop;

    OnRefreshListener mRefreshListener;

    public interface OnRefreshListener {

        /**
         * on pull down status
         * @param dy
         */
        void onPullDown(int dy);
    }

    public void setOnRefreshListener(OnRefreshListener listener){
       mRefreshListener = listener;
    }

    public RefreshLayout(Context context){
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mRefreshView = createRefreshView();
        ViewCompat.setOverScrollMode(mRefreshView, ViewCompat.OVER_SCROLL_NEVER);
        addView(mRefreshView);
    }

    public View getRefreshView(){
        return mRefreshView;
    }

    protected abstract View createRefreshView();

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mRefreshView.layout(l, t, r, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        if(!childIsOnTop()){
            return super.onInterceptTouchEvent(ev);
        }
        final int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = mActionDownY = (int) ev.getY();
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                final int yDiff = y - mActionDownY;
                if(yDiff > mTouchSlop){
                    mLastMotionY = y;
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                break;
        }

        if(mIsBeingDragged){
            return true;
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent ev){
        final int aciont = ev.getAction();
        if(!childIsOnTop()){
            return super.onTouchEvent(ev);
        }
        switch (aciont){
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = mActionDownY = (int) ev.getY();
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                final int yDiff = y - mLastMotionY;

                if(!mIsBeingDragged && yDiff > mTouchSlop){
                    mIsBeingDragged = true;
                }
                int curTop = getCurTop();
                if(curTop <= 0 && yDiff < 0 ){
                    mIsBeingDragged = false;
                }
                if(mIsBeingDragged){
                    int offset = (int) (yDiff / 2 + 0.5);
                    if(offset < 0 && curTop + offset <= 0){
                        offset = - curTop;
                    }
                    setOffsetTopAndBottom(offset);
                    if(mRefreshListener != null){
                       mRefreshListener.onPullDown(curTop + offset);
                    }
                    if(curTop > 0){
                        updateStatus(PULL_TO_REFRESH_STATUS);
                    }else{
                        updateStatus(NORMAL_STATUS);
                    }
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handleRelease();
                break;
            default:
                break;
        }
        if(mIsBeingDragged){
            return true;
        }else{
            return super.onTouchEvent(ev);
        }
    }

    private int getCurTop(){
        return mRefreshView.getTop();
    }

    private void setOffsetTopAndBottom(int offset){
        mRefreshView.offsetTopAndBottom(offset);
    }

    private void handleRelease(){
        int toPostion;
        if(PULL_TO_REFRESH_STATUS == mCurStatus){
            toPostion = 0;
        }else {
            return;
        }
        mToPosition = toPostion;
        mOriginalOffsetTop = getCurTop();
        ValueAnimator animator = ValueAnimator.ofInt(mOriginalOffsetTop, mToPosition);
        animator.setDuration(mShortAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final int curTop = getCurTop();
                if(mToPosition == curTop){
                    return;
                }
                int toTop = (Integer) valueAnimator.getAnimatedValue();
                if(toTop <= 0){
                    toTop = 0;
                    if(PULL_TO_REFRESH_STATUS == mCurStatus){
                        updateStatus(NORMAL_STATUS);
                    }
                }
                if (mRefreshListener != null)
                    mRefreshListener.onPullDown(toTop);
                int offset = toTop - curTop;
                setOffsetTopAndBottom(offset);
            }
        });
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    /**
     *  update the pull status
     * @param status
     */
    private void updateStatus(int status){
        if(mCurStatus == status){
            return;
        }
        mCurStatus = status;
    }

    protected boolean childIsOnTop(){
       if(mRefreshView instanceof ScrollView){
           return mRefreshView.getScrollY() <= 0;
       }else if(mRefreshView instanceof RecyclerView){
           RecyclerView recyclerView = (RecyclerView) mRefreshView;
           View child = recyclerView.getChildAt(0);
           if(null != child){
               return child.getTop() >= 0;
           }
          // RecycleView
          return false;
       }else {
           return false;
       }
    }
}
