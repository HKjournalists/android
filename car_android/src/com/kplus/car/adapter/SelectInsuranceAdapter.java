package com.kplus.car.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.R;
import com.kplus.car.model.json.Insurance;

import java.util.List;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class SelectInsuranceAdapter extends RecyclerView.Adapter<SelectInsuranceViewHolder> implements SelectInsuranceViewHolder.OnItemClickListener {
    private List<Insurance> mList;
    private Context mContext;
    private int mCurIndex = -1;
    private OnItemSelectedListener mListener;

    public interface OnItemSelectedListener{
        public void onItemSelected(int position);
    }

    public SelectInsuranceAdapter(Context context, List<Insurance> list, OnItemSelectedListener l){
        mContext = context;
        mList = list;
        mListener = l;
    }

    public void setCurIndex(int index){
        int lastIndex = mCurIndex;
        mCurIndex = index;
        if (lastIndex != -1)
            notifyItemChanged(lastIndex);
    }

    @Override
    public SelectInsuranceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_select_insurance, parent, false);
        return new SelectInsuranceViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(SelectInsuranceViewHolder holder, int position) {
        holder.mCompany.setText(mList.get(position).getCompany());
        holder.mPhone.setText("(" + mList.get(position).getPhone() + ")");
        if (position == mCurIndex)
            holder.mSelected.setVisibility(View.VISIBLE);
        else
            holder.mSelected.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public void onItemClick(int position) {
        if (position != mCurIndex){
            int lastIndex = mCurIndex;
            mCurIndex = position;
            if (lastIndex != -1)
                notifyItemChanged(lastIndex);
            notifyItemChanged(mCurIndex);
            if (mListener != null)
                mListener.onItemSelected(position);
        }
    }
}
