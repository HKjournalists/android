package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.adapter.RequestAdapter;
import com.kplus.car.model.FWRequestInfo;
import com.kplus.car.model.response.FWRequestListResponse;
import com.kplus.car.model.response.request.FWRequestListRequest;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 */
public class GuanjiaHistoryActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RecyclerView mRecyclerView;
    private List<FWRequestInfo> mListRequest;
    private RequestAdapter mAdapter;

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getGuanjiaHistory();
        }
    };

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_guanjia_history);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("我提交的需求记录");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void loadData() {
        mListRequest = mApplication.dbCache.getRequestInfo();
        if (mListRequest == null)
            mListRequest = new ArrayList<>();
        mAdapter = new RequestAdapter(GuanjiaHistoryActivity.this, mListRequest);
        mRecyclerView.setAdapter(mAdapter);
        getGuanjiaHistory();
        LocalBroadcastManager.getInstance(this).registerReceiver(refreshReceiver, new IntentFilter("com.kplus.car.guanjia.refresh"));
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(refreshReceiver);
        super.onDestroy();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_TYPE_CLOSE && resultCode == RESULT_OK){
            int position = data.getIntExtra("position", 0);
            FWRequestInfo info = mListRequest.get(position);
            info.setStatus("2");
            mListRequest.set(position, info);
            mApplication.dbCache.saveRequestInfo(mListRequest);
            mAdapter.notifyItemChanged(position);
        }
    }

    private void getGuanjiaHistory(){
        new AsyncTask<Void, Void, FWRequestListResponse>(){
            @Override
            protected FWRequestListResponse doInBackground(Void... params) {
                try {
                    FWRequestListRequest req = new FWRequestListRequest();
                    req.setParams(mApplication.getId(), 0);
                    return mApplication.client.execute(req);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(FWRequestListResponse response) {
                if (response != null && response.getCode() != null && response.getCode() == 0){
                    mListRequest = response.getData().getList();
                    mApplication.dbCache.saveRequestInfo(mListRequest);
                    mAdapter.setRequestList(mListRequest);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }
}
