package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.model.Weather;

/**
 * Created by Administrator on 2015/7/23.
 */
public class VehicleAddAdapter extends RecyclerView.Adapter {
    private KplusApplication mApp;
    private Context mContext;

    public VehicleAddAdapter(Context context){
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case Constants.REQUEST_TYPE_ADD:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_add, parent, false);
                AddViewHolder addViewHolder = new AddViewHolder(v, mContext);
                return addViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.REQUEST_TYPE_ADD;
    }
}
