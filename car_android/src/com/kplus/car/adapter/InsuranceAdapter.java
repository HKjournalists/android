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
public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceViewHolder> implements InsuranceViewHolder.DialIconClickListener {
    private List<Insurance> mList;
    private Context mContext;

    public InsuranceAdapter(Context context, List<Insurance> list){
        mContext = context;
        mList = list;
    }

    @Override
    public InsuranceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_insurance, parent, false);
        return new InsuranceViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(InsuranceViewHolder holder, int position) {
        holder.mCompany.setText(mList.get(position).getCompany());
        holder.mPhone.setText(mList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public void onDialIconClick(InsuranceViewHolder vh, int position) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + mList.get(position).getPhone()));
        mContext.startActivity(intent);
    }
}
