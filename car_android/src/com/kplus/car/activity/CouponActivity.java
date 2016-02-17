package com.kplus.car.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kplus.car.R;
import com.kplus.car.adapter.CouponAdapter;
import com.kplus.car.model.Coupon;
import com.kplus.car.model.response.GetCouponListResponse;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class CouponActivity extends BaseActivity implements ClickUtils.NoFastClickListener, RadioGroup.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private RadioGroup mRadioGroup;
    private List<Coupon> mListCoupon;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mCheckId = R.id.radio_1;
    private CouponAdapter mAdapter;
    private TextView mUnusedCount;
    private int mPageIndex = 1;
    private int mPageSize = 10;
    private LinearLayoutManager mLayoutManager;
    private int mTotal = 0;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_coupon);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("代金券");
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText("返回");

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.check(R.id.radio_1);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mUnusedCount = (TextView) findViewById(R.id.unused_count);
    }

    @Override
    protected void loadData() {
        mListCoupon = new ArrayList<>();
        mUnusedCount.setText(getIntent().getIntExtra("count", 0) + "张");
        mAdapter = new CouponAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.help_layout), this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mSwipeRefreshLayout.isRefreshing() && mPageIndex * mPageSize < mTotal && dy > 0 && mLayoutManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 3){
                    mSwipeRefreshLayout.setRefreshing(true);
                    mPageIndex++;
                }
            }
        });
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                finish();
                break;
            case R.id.help_layout:
                Intent intent = new Intent(this, VehicleServiceActivity.class);
                intent.putExtra("appId", "10000006");
                intent.putExtra("startPage", "how-to-use.html");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton btn = (RadioButton) group.findViewById(checkedId);
        btn.toggle();
        mCheckId = checkedId;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        mListCoupon.clear();
    }
}
