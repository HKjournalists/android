package com.kplus.car.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kplus.car.R;
import com.kplus.car.adapter.InsuranceAdapter;
import com.kplus.car.asynctask.InsuranceTask;
import com.kplus.car.model.json.Insurance;
import com.kplus.car.model.response.InsuranceResponse;
import com.kplus.car.util.ClickUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/3/30 0030.
 */
public class InsuranceListActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private List<Insurance> mListInsurance;
    private RecyclerView mRecyclerView;
    private InsuranceAdapter mAdapter;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_insurance_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("保险公司大全");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("车险");

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void loadData() {
        new InsuranceTask(mApplication){
            @Override
            protected void onPostExecute(InsuranceResponse insuranceResponse) {
                if (insuranceResponse != null){
                    String json = insuranceResponse.getData().getValue();
                    mListInsurance = new Gson().fromJson(json, new TypeToken<List<Insurance>>(){}.getType());
                    mAdapter = new InsuranceAdapter(InsuranceListActivity.this, mListInsurance);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        }.execute();
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
        }
    }
}
