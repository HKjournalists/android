package com.kplus.car.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.adapter.JiazhaoAdapter;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/21.
 */
public class JiazhaoListActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private RecyclerView mRecyclerView;
    private JiazhaoAdapter mAdapter;
    private List<String> mListRefreshing;
    private PopupWindow mRulePopup;
    private boolean mAdd = false;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_jiazhao_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("驾照分查询");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setText("添加");

        mRecyclerView = (RecyclerView) findViewById(R.id.jiazhao_list);
    }

    @Override
    protected void loadData() {
        boolean check = getIntent().getBooleanExtra("check", false);
        mListRefreshing = getIntent().getStringArrayListExtra("refreshingList");
        if (mListRefreshing == null)
            mListRefreshing = new ArrayList<>();
        mAdapter = new JiazhaoAdapter(this, check);
        mAdapter.setRefreshingList(mListRefreshing);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LocalBroadcastManager.getInstance(this).registerReceiver(jiazhaoStartReceiver, new IntentFilter("com.kplus.car.jiazhao.start"));
        LocalBroadcastManager.getInstance(this).registerReceiver(jiazhaoFinishReceiver, new IntentFilter("com.kplus.car.jiazhao.finish"));
        LocalBroadcastManager.getInstance(this).registerReceiver(jiazhaoEditReceiver, new IntentFilter("com.kplus.car.jiazhao.edit"));
        mAdd = getIntent().getBooleanExtra("add", false);
        if (mAdd){
            Intent intent = new Intent(this, JiazhaoEditActivity.class);
            intent.putExtra("showIndex", true);
            startActivityForResult(intent, Constants.REQUEST_TYPE_JIAZHAOFEN);
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rule), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.detail_rule), this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(jiazhaoStartReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(jiazhaoFinishReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(jiazhaoEditReceiver);
        super.onDestroy();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
                Intent intent = new Intent(this, JiazhaoEditActivity.class);
                startActivityForResult(intent, Constants.REQUEST_TYPE_JIAZHAOFEN);
                break;
            case R.id.rule:
                EventAnalysisUtil.onEvent(this, "lookfor_EndorseUpdate_rule", "查看驾照分更新规则", null);
                showRulePopup();
                break;
            case R.id.close_rule:
                mRulePopup.dismiss();
                break;
            case R.id.detail_rule:
                EventAnalysisUtil.onEvent(this, "lookfor_endorseDeduct_rule", "查看驾照分扣分细则", null);
                intent = new Intent(this, JiazhaoDetailRuleActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.REQUEST_TYPE_JIAZHAOFEN:
                if (resultCode == Constants.RESULT_TYPE_ADDED){
                    setResult(Constants.RESULT_TYPE_CHANGED);
                    List<Jiazhao> list = mApplication.dbCache.getJiazhaos();
                    mAdapter.setJiazhaoList(list);
                    mAdapter.setCheckNew(true);
                    mAdapter.notifyDataSetChanged();
                }
                else if (mAdd){
                    onBackPressed();
                }
                break;
        }
    }

    private BroadcastReceiver jiazhaoStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListRefreshing.add(intent.getStringExtra("id"));
            mAdapter.setRefreshingList(mListRefreshing);
            mAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver jiazhaoFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListRefreshing.remove(intent.getStringExtra("id"));
            mAdapter.setRefreshingList(mListRefreshing);
            List<Jiazhao> list = mApplication.dbCache.getJiazhaos();
            mAdapter.setJiazhaoList(list);
            mAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver jiazhaoEditReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            JiazhaoListActivity.this.setResult(Constants.RESULT_TYPE_CHANGED);
            List<Jiazhao> list = mApplication.dbCache.getJiazhaos();
            if (list != null && list.size() > 0){
                mAdapter.setJiazhaoList(list);
                mAdapter.notifyDataSetChanged();
            }
            else {
                finish();
            }
        }
    };

    private void showRulePopup(){
        View layout = LayoutInflater.from(this).inflate(R.layout.popup_rule, null, false);
        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText("驾照分更新规则");
        TextView content = (TextView) layout.findViewById(R.id.content);
        content.setText(this.getString(R.string.jiazhao_rule));
        ClickUtils.setNoFastClickListener(layout.findViewById(R.id.close_rule), this);
        mRulePopup = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRulePopup.showAtLocation(findViewById(R.id.jiazhao_list), Gravity.LEFT | Gravity.TOP, 0, 0);
    }
}
