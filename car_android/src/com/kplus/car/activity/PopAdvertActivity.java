package com.kplus.car.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.fragment.AdvertFragment;

public class PopAdvertActivity extends BaseActivity {

    public static final String KEY_VALUE_TYPE = "key-value-type";

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.daze_pop_advert_layout);

        String keyValueType = KplusConstants.ADVERT_HOME;
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            keyValueType = bundle.getString(KEY_VALUE_TYPE);
        }

        if (TextUtils.isEmpty(keyValueType)) {
            keyValueType = KplusConstants.ADVERT_HOME;
        }

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.advertContainer, AdvertFragment.newInstance(keyValueType))
                .commit();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void setListener() {
        findViewById(R.id.ivCloseAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
