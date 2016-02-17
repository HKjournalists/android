package com.kplus.car.carwash.module.base;

import android.view.View;

import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.carwash.utils.CNViewClickUtil;

/**
 * Description：什么ListView时，的ViewHolder基类
 * <br/><br/>Created by Fu on 2015/5/13.
 * <br/><br/>
 */
public abstract class CNBaseAdapterViewHolder<E> implements CNViewClickUtil.NoFastClickListener {
    protected View mView = null;
    protected int mPosition = Const.NONE;
    protected OnListItemClickListener mClickListener = null;

    public CNBaseAdapterViewHolder(View view, OnListItemClickListener listener) {
        if (null == view) {
            throw new IllegalArgumentException("view is not null~~~");
        }
        mView = view;
        mClickListener = listener;
    }

    public abstract void setValue(int position, E e);

    protected <T extends View> T findView(int id) {
        if (null == mView) {
            throw new IllegalArgumentException("view is not null!");
        }
        return (T) mView.findViewById(id);
    }

    @Override
    public void onNoFastClick(View v) {
        if (null != mClickListener) {
            mClickListener.onClickItem(mPosition, v);
        }
    }
}
