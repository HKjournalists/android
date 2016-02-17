package com.kplus.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.model.RemindInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/13 0013.
 */
public class RemindDragAdapter extends RecyclerView.Adapter<RemindViewHolder> {
    private Context mContext;
    private List<RemindInfo> mListRemindInfo;
    private List<RemindInfo> mListShowRemind;

    public RemindDragAdapter(Context context, List<RemindInfo> list){
        mContext = context;
        mListRemindInfo = list;
        mListShowRemind = fitlerHiddenRemind(mListRemindInfo);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                if (itemCount == 1){
                    RemindInfo info = mListShowRemind.get(fromPosition);
                    mListShowRemind.remove(fromPosition);
                    mListShowRemind.add(toPosition, info);
                }
            }
        });
    }

    private List<RemindInfo> fitlerHiddenRemind(List<RemindInfo> list){
        List<RemindInfo> newList = new ArrayList<RemindInfo>();
        for (RemindInfo info : list){
            if (!info.isHidden())
                newList.add(info);
        }
        return newList;
    }

    public void setRemindInfo(List<RemindInfo> list){
        mListRemindInfo = list;
        mListShowRemind = fitlerHiddenRemind(mListRemindInfo);
    }

    public List<RemindInfo> getSortedRemindInfo() {
        List<RemindInfo> sortList = new ArrayList<RemindInfo>();
        sortList.addAll(mListShowRemind);
        for (int i = 0; i < mListRemindInfo.size(); i++) {
            RemindInfo info = mListRemindInfo.get(i);
            if (info.isHidden())
                sortList.add(i, info);
        }
        return sortList;
    }

    @Override
    public RemindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_remind_drag, parent, false);
        return new RemindViewHolder(v, null);
    }

    @Override
    public void onBindViewHolder(RemindViewHolder holder, int position) {
        RemindInfo info = mListShowRemind.get(position);
        holder.mTitle.setText(info.getTitle());
        holder.mDesc.setText(info.getDesc());
        switch (info.getType()){
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                holder.mLeftIcon.setImageResource(R.drawable.jiazhaofen);
                break;
            case Constants.REQUEST_TYPE_CHEXIAN:
                holder.mLeftIcon.setImageResource(R.drawable.chexian);
                break;
            case Constants.REQUEST_TYPE_BAOYANG:
                holder.mLeftIcon.setImageResource(R.drawable.baoyang);
                break;
            case Constants.REQUEST_TYPE_NIANJIAN:
                holder.mLeftIcon.setImageResource(R.drawable.nianjian);
                break;
            case Constants.REQUEST_TYPE_CHEDAI:
                holder.mLeftIcon.setImageResource(R.drawable.chedai);
                break;
            case Constants.REQUEST_TYPE_CUSTOM:
                holder.mLeftIcon.setImageResource(R.drawable.notes);
                break;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mListShowRemind != null ? mListShowRemind.size() : 0;
    }
}
