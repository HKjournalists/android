/**
 *
 */
package com.kplus.car.carwash.module.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kplus.car.carwash.common.Const;
import com.kplus.car.carwash.callback.OnListItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * BaseListAdapter
 * 2014-9-1 下午5:00:16
 * author FU ZHIXUE
 *
 * @param <E> Object
 */
public abstract class CNBaseListAdapter<E> extends BaseAdapter {
    protected List<E> mList = null;
    protected Context mContext = null;
    protected LayoutInflater mInflater = null;
    protected OnListItemClickListener mClickListener = null;

    public CNBaseListAdapter(Context context, List<E> list, OnListItemClickListener listener) {
        super();
        this.mContext = context;
        this.mList = (null == list ? new ArrayList<E>() : list);
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
        if (null != list) {
            this.mList = list;
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

    @Override
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = bindView(position, convertView, parent);
        return convertView;
    }

    /**
     * 适配器中创建用户界面与getView用法相同
     *
     * @param position    position
     * @param convertView convertView
     * @param parent      parent
     * @return view
     */
    public abstract View bindView(int position, View convertView, ViewGroup parent);
}
