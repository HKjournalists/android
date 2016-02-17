package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.activity.GuanjiaDetailActivity;
import com.kplus.car.model.FWRequestInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/8/13.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> implements RequestViewHolder.RequestClickListener {
    private Context mContext;
    private List<FWRequestInfo> mListRequest;

    public RequestAdapter(Context context, List<FWRequestInfo> list){
        mContext = context;
        mListRequest = list;
    }

    public void setRequestList(List<FWRequestInfo> list){
        mListRequest = list;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        FWRequestInfo requestInfo = mListRequest.get(position);
        holder.mServiceName.setText(requestInfo.getServiceName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(requestInfo.getCreateTime());
        holder.mDate.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(calendar.getTime()));
        if ("2".equals(requestInfo.getStatus())) {
            holder.mStatus.setText("已取消需求");
            holder.mGoto.setVisibility(View.INVISIBLE);
        }
        else if ("1".equals(requestInfo.getStatus())) {
            holder.mStatus.setText("已安排管家");
            holder.mGoto.setVisibility(View.VISIBLE);
        }
        else {
            holder.mStatus.setText("等待安排管家");
            holder.mGoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mListRequest != null ? mListRequest.size() : 0;
    }

    @Override
    public void onRequestClick(RequestViewHolder vh, int position) {
        FWRequestInfo requestInfo = mListRequest.get(position);
        if (!"2".equals(requestInfo.getStatus())) {
            Intent it = new Intent(mContext, GuanjiaDetailActivity.class);
            it.putExtra("requestInfo", requestInfo);
            it.putExtra("position", position);
            ((Activity)mContext).startActivityForResult(it, Constants.REQUEST_TYPE_CLOSE);
        }
    }
}
