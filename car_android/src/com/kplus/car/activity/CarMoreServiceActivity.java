package com.kplus.car.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.adapter.CarServicesAdapter;
import com.kplus.car.carwash.callback.OnListItemClickListener;
import com.kplus.car.model.CarService;
import com.kplus.car.model.CarServiceGroup;
import com.kplus.car.util.CarServicesUtil;
import com.kplus.car.util.ClickUtils;

/**
 * Description：更多服务列表
 * <br/><br/>Created by FU ZHIXUE on 2015/8/13.
 * <br/><br/>
 */
public class CarMoreServiceActivity extends BaseActivity implements ClickUtils.NoFastClickListener, OnListItemClickListener {

    private Context mContext = null;

    private CarServicesAdapter mServicesAdapter = null;

    private TextView tvTitle = null;
    private ImageView ivLeft = null;

    @Override
    protected void initView() {
        mContext = this;

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_car_more_services);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        ListView lvList = (ListView) findViewById(R.id.lvList);

        mServicesAdapter = new CarServicesAdapter(mContext, null, this);
        lvList.setAdapter(mServicesAdapter);
    }

    @Override
    protected void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            CarServiceGroup serviceGroup = (CarServiceGroup) bundle.get(CarServicesUtil.KEY_PARAM_VALUE);
            if (null != serviceGroup) {
                tvTitle.setText(serviceGroup.getName());
                mServicesAdapter.append(serviceGroup.getServices());
            }
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(ivLeft, this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onClickItem(int position, View v) {
        CarService carService = mServicesAdapter.getItem(position);
        CarServicesUtil.onClickCarServiceItem(mContext, carService);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_TYPE_LOGIN) {
            if (resultCode == RESULT_OK) {
                CarServicesUtil.onLoginResult(mContext);
            }
        }
    }
}
