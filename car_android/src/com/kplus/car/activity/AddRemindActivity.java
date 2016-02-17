package com.kplus.car.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.adapter.RemindAdapter;
import com.kplus.car.adapter.RemindDragAdapter;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.model.RemindCustom;
import com.kplus.car.model.RemindInfo;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.widget.DragSortRecycler;

import java.util.List;

public class AddRemindActivity extends BaseActivity implements ClickUtils.NoFastClickListener, RemindAdapter.DataChangeListener {
    private UserVehicle mUserVehicle;
    private List<RemindInfo> mListRemindInfo;
    private RecyclerView mRecyclerView;
    private RemindAdapter mRemindAdapter;
    private RemindDragAdapter mDragAdapter;
    private boolean mIsSortMode = false;
    private boolean mIsChanged = false;
    private TextView mTvRight;
    private RemindRestrict mRemindRestrict;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_add_remind);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("提醒");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        mTvRight = (TextView) findViewById(R.id.tvRight);
        mTvRight.setText("排序");
    }

    @Override
    protected void loadData() {
        mUserVehicle = (UserVehicle) getIntent().getSerializableExtra("vehicle");
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setText(mUserVehicle.getVehicleNum());

        mListRemindInfo = mApplication.dbCache.getRemindInfo(mUserVehicle.getVehicleNum());
        mRemindRestrict = mApplication.dbCache.getRemindRestrict(mUserVehicle.getVehicleNum());
        mRecyclerView = (RecyclerView) findViewById(R.id.drag_list);
        mRemindAdapter = new RemindAdapter(this, mListRemindInfo, this, mUserVehicle, mRemindRestrict);
        mRecyclerView.setAdapter(mRemindAdapter);
        mRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(null);

        DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.drag_icon);
        dragSortRecycler.setOnItemMovedListener(new DragSortRecycler.OnItemMovedListener() {
            @Override
            public void onItemMoved(int from, int to) {
                mDragAdapter.notifyItemMoved(from,to);
            }
        });
        mRecyclerView.addItemDecoration(dragSortRecycler);
        mRecyclerView.addOnItemTouchListener(dragSortRecycler);
        mRecyclerView.setOnScrollListener(dragSortRecycler.getScrollListener());
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.rightView:
                if (mIsSortMode){
                    mIsChanged = true;
                    mIsSortMode = false;
                    mTvRight.setText("排序");
                    mListRemindInfo = mDragAdapter.getSortedRemindInfo();
                    mRemindAdapter.setRemindInfo(mListRemindInfo);
                    mRecyclerView.setAdapter(mRemindAdapter);
                }
                else{
                    EventAnalysisUtil.onEvent(this, "click_NotifySort", "点击提醒排序编辑", null);
                    mIsSortMode = true;
                    mTvRight.setText("完成");
                    if (mDragAdapter == null)
                        mDragAdapter = new RemindDragAdapter(this, mListRemindInfo);
                    else
                        mDragAdapter.setRemindInfo(mListRemindInfo);
                    mRecyclerView.setAdapter(mDragAdapter);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsChanged) {
            mApplication.dbCache.saveRemindInfo(mListRemindInfo, mUserVehicle.getVehicleNum());
            if (mApplication.getId() != 0)
                new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_REMIND);
            setResult(Constants.RESULT_TYPE_CHANGED);
        }
        super.onBackPressed();
    }

    @Override
    public void onDataChanged() {
        mIsChanged = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_TYPE_DELETE && resultCode == RESULT_OK){
            mRemindAdapter.deleteRemind();
        }
        else if (requestCode == Constants.REQUEST_TYPE_RESTRICT && resultCode == Constants.RESULT_TYPE_CHANGED){
            mRemindRestrict = mApplication.dbCache.getRemindRestrict(mUserVehicle.getVehicleNum());
            mRemindAdapter.setRemindRestrict(mRemindRestrict);
            mRemindAdapter.notifyDataSetChanged();
        }
        else if (resultCode == Constants.RESULT_TYPE_CHANGED){
            mIsChanged = true;
            int position = data.getIntExtra("position", 1);
            RemindInfo info = mListRemindInfo.get(position - 1);
            info.setHidden(false);
            mListRemindInfo.set(position - 1, info);
            mRemindAdapter.notifyItemChanged(position);
        }
        else if (resultCode == Constants.RESULT_TYPE_ADDED){
            EventAnalysisUtil.onEvent(this, "addCustomNotify_success", "添加自定义提醒成功", null);
            mIsChanged = true;
            RemindCustom remindCustom = (RemindCustom) data.getSerializableExtra("RemindCustom");
            RemindInfo info = new RemindInfo();
            info.setVehicleNum(mUserVehicle.getVehicleNum());
            info.setTitle(remindCustom.getName());
            info.setType(Constants.REQUEST_TYPE_CUSTOM);
            mListRemindInfo.add(info);
            mRemindAdapter.notifyItemInserted(mListRemindInfo.size());
        }
        else if (resultCode == Constants.RESULT_TYPE_RELOAD){
            mIsChanged = true;
            int position = data.getIntExtra("position", 1);
            RemindCustom remindCustom = (RemindCustom) data.getSerializableExtra("RemindCustom");
            RemindInfo info = mListRemindInfo.get(position - 1);
            info.setTitle(remindCustom.getName());
            info.setHidden(false);
            mListRemindInfo.set(position - 1, info);
            mRemindAdapter.notifyItemChanged(position);
        }
    }
}
