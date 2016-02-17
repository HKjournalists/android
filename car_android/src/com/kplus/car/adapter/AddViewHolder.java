package com.kplus.car.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.activity.PhoneRegistActivity;
import com.kplus.car.activity.VehicleAddNewActivity;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.ToastUtil;

/**
 * Created by Administrator on 2015/7/24.
 */
public class AddViewHolder extends RecyclerView.ViewHolder implements ClickUtils.NoFastClickListener {
    private Context mContext;
    private KplusApplication mApp;

    public AddViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mApp = (KplusApplication) ((Activity)context).getApplication();
        ClickUtils.setNoFastClickListener(itemView.findViewById(R.id.btn_add_vehicle), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_vehicle:
                EventAnalysisUtil.onEvent(mContext, "click_AddCar_Home", "主页面添加车辆按钮被点击", null);
                if(mApp.getId() == 0){
                    ToastUtil.TextToast(mContext, "添加车辆需要绑定手机号", Toast.LENGTH_SHORT, Gravity.CENTER);
                    Intent intent = new Intent(mContext, PhoneRegistActivity.class);
                    intent.putExtra("isMustPhone", true);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_LOGIN);
                }
                else{
                    Intent intent = new Intent(mContext, VehicleAddNewActivity.class);
                    ((Activity)mContext).startActivityForResult(intent, Constants.REQUEST_TYPE_VEHICLE);
                }
                break;
        }
    }
}
