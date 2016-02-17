package com.kplus.car.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.asynctask.FWDeleteRequestTask;
import com.kplus.car.asynctask.FWProviderListTask;
import com.kplus.car.model.FWProviderInfo;
import com.kplus.car.model.FWRequestInfo;
import com.kplus.car.model.json.FWSearchProviderJson;
import com.kplus.car.model.response.FWGetProviderResponse;
import com.kplus.car.model.response.FWProviderListResponse;
import com.kplus.car.model.response.FWSearchProviderResponse;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.request.FWGetProviderRequest;
import com.kplus.car.model.response.request.FWSearchProviderRequest;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.RatingBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 */
public class GuanjiaDetailActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private FWProviderInfo mProviderInfo;
    private FWRequestInfo mRequestInfo;
    private ImageView mIcon;
    private TextView mName;
    private TextView mAddress;
    private RatingBar mRatingBar;
    private TextView mCommentNum;
    private DisplayImageOptions optionsPhoto;
    private View mProviderLayout;
    private View mCancelRequest;
    private View mBottomLayout;
    private TextView mServiceName;
    private TextView mDate;
    private TextView mStatus;
    private TextView mRemark;
    private int mPosition;

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showProvider();
        }
    };

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_guanjia_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("需求详情");
        mIcon = (ImageView) findViewById(R.id.icon);
        mName = (TextView) findViewById(R.id.name);
        mAddress = (TextView) findViewById(R.id.address);
        mRatingBar = (RatingBar) findViewById(R.id.rating);
        mCommentNum = (TextView) findViewById(R.id.comment_num);
        mProviderLayout = findViewById(R.id.provider_layout);
        mCancelRequest = findViewById(R.id.cancel_request);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mServiceName = (TextView) findViewById(R.id.service_name);
        mDate = (TextView) findViewById(R.id.date);
        mStatus = (TextView) findViewById(R.id.status);
        mRemark = (TextView) findViewById(R.id.remark);
    }

    @Override
    protected void loadData() {
        optionsPhoto = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        mRequestInfo = (FWRequestInfo) getIntent().getSerializableExtra("requestInfo");
        mPosition = getIntent().getIntExtra("position", 0);
        mServiceName.setText(mRequestInfo.getServiceName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mRequestInfo.getCreateTime());
        mDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()));
        if (!StringUtils.isEmpty(mRequestInfo.getExtendsion())) {
            mRemark.setVisibility(View.VISIBLE);
            mRemark.setText(mRequestInfo.getExtendsion());
        }
        else {
            mRemark.setVisibility(View.GONE);
        }
        if ("2".equals(mRequestInfo.getStatus()))
            mStatus.setText("已取消");
        else if ("1".equals(mRequestInfo.getStatus()))
            mStatus.setText("已安排管家");
        else
            mStatus.setText("等待安排管家");
        if ("1".equals(mRequestInfo.getStatus())){
            showProvider();
        }
        else {
            mCancelRequest.setVisibility(View.VISIBLE);
            mBottomLayout.setVisibility(View.VISIBLE);
            mProviderLayout.setVisibility(View.GONE);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(refreshReceiver, new IntentFilter("com.kplus.car.guanjia.refresh"));
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(mCancelRequest, this);
        ClickUtils.setNoFastClickListener(mProviderLayout, this);
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
                finish();
                break;
            case R.id.provider_layout:
                new AsyncTask<Void, Void, FWSearchProviderResponse>(){
                    @Override
                    protected FWSearchProviderResponse doInBackground(Void... params) {
                        FWSearchProviderRequest request = new FWSearchProviderRequest();
                        request.setParams(mProviderInfo.getPhone());
                        try {
                            return mApplication.client.execute(request);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(FWSearchProviderResponse response) {
                        if (response != null && response.getCode() != null && response.getCode() == 0){
                            FWSearchProviderJson data = response.getData();
                            Intent intent = new Intent(GuanjiaDetailActivity.this, VehicleServiceActivity.class);
                            intent.putExtra("appId", "10000012");
                            intent.putExtra("startPage", "supplier-detail.html?id=" + mProviderInfo.getProviderId() + "&serviceId=" + mRequestInfo.getServiceType() + "&cityId=" + data.getCityId() + "&userId=" + mApplication.getUserId());
                            startActivity(intent);
                        }
                        else if (response != null){
                            ToastUtil.makeShortToast(GuanjiaDetailActivity.this, "没有找到指定服务商");
                        }
                    }
                }.execute();
                break;
            case R.id.cancel_request:
                Intent alertIntent = new Intent(this, AlertDialogActivity.class);
                alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
                alertIntent.putExtra("title", "取消需求");
                alertIntent.putExtra("message", "确定要取消需求吗？");
                startActivityForResult(alertIntent, Constants.REQUEST_TYPE_CLOSE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_TYPE_CLOSE:
                if (resultCode == RESULT_OK) {
                    new FWDeleteRequestTask(mApplication){
                        @Override
                        protected void onPostExecute(GetResultResponse response) {
                            if (response != null && response.getCode() != null && response.getCode() == 0){
                                Intent it = new Intent();
                                it.putExtra("position", mPosition);
                                setResult(RESULT_OK, it);
                                finish();
                            }
                            else if (response != null){
                                ToastUtil.makeShortToast(GuanjiaDetailActivity.this, response.getMsg());
                            }
                        }
                    }.execute(mRequestInfo.getCode());
                }
                break;
        }
    }

    private void showProvider(){
        mCancelRequest.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.GONE);
        mProviderLayout.setVisibility(View.VISIBLE);
        mStatus.setText("已安排管家");
        new FWProviderListTask(mApplication){
            @Override
            protected void onPostExecute(FWProviderListResponse response) {
                if (response != null && response.getCode() != null && response.getCode() == 0){
                    mProviderInfo = response.getData().get(0);
                    mApplication.imageLoader.displayImage(mProviderInfo.getImgUrl(), mIcon, optionsPhoto);
                    mName.setText(mProviderInfo.getName());
                    mAddress.setText(mProviderInfo.getAddress());
                    mRatingBar.setRating(5, mProviderInfo.getScore());
                    mCommentNum.setText(String.valueOf(mProviderInfo.getAssessNum()));
                }
            }
        }.execute(mRequestInfo.getCode());
    }
}
