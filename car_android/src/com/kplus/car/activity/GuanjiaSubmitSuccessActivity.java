package com.kplus.car.activity;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.util.ClickUtils;

/**
 * Created by Administrator on 2015/10/20.
 */
public class GuanjiaSubmitSuccessActivity extends BaseActivity implements ClickUtils.NoFastClickListener {

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_guanjia_submit_success);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("提交成功");
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
        switch (v.getId()){
            case R.id.leftView:
                finish();
                break;
        }
    }
}
