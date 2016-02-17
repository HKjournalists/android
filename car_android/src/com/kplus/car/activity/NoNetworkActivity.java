package com.kplus.car.activity;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.util.ClickUtils;

/**
 * Description：
 * <br/><br/>Created by FU ZHIXUE on 2015/8/11.
 * <br/><br/>
 */
public class NoNetworkActivity extends BaseActivity implements ClickUtils.NoFastClickListener {

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_no_network);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        findViewById(R.id.title_layout).setBackgroundColor(getResources().getColor(R.color.daze_white_smoke10));
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("无网络连接");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()) {
            case R.id.leftView:
                onBackPressed();
                break;
        }
    }
}
