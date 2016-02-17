/**
 *
 */
package com.kplus.car.carwash.module.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.callback.OnListItemClickListener;

import java.util.List;


/**
 * CNBasePagerAdapter
 * 2014-9-1 下午5:00:16
 * author FU ZHIXUE
 *
 * @param <E> Object
 */
public abstract class CNBasePagerAdapter<E> extends PagerAdapter {
    protected List<E> mList = null;
    protected Context mContext = null;
    protected LayoutInflater mInflater = null;
    protected OnListItemClickListener mClickListener = null;

    public CNBasePagerAdapter(Context context, List<E> list, OnListItemClickListener listener) {
        super();
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(mContext);
        this.mClickListener = listener;
    }

    public void setListItemClickListener(OnListItemClickListener listener) {
        mClickListener = listener;
    }

    public List<E> getList() {
        return this.mList;
    }

    public void setList(List<E> list) {
        if (null != list && null != this.mList) {
            this.mList = list;
            notifyDataSetChanged();
        }
    }

    public void append(E e) {
        if (null != e && null != this.mList) {
            this.mList.add(e);
            notifyDataSetChanged();
        }
    }

    public void append(List<E> e) {
        if (null != e && e.size() > Const.NONE && null != this.mList) {
            this.mList.addAll(e);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        if (null != this.mList) {
            this.mList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (null != this.mList) {
            this.mList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return (null == this.mList) ? Const.NONE : this.mList.size();
    }

    public E getItem(int position) {
        if (null != this.mList) {
            if (position < Const.NONE || position >= this.mList.size()) {
                return null;
            }
            return this.mList.get(position);
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0.equals(arg1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return bindView(container, position);
    }

    /**
     * 适配器中创建用户界面与instantiateItem用法相同
     *
     * @param container ViewGroup
     * @param position  position
     * @return Object
     */
    public abstract Object bindView(ViewGroup container, int position);
}
