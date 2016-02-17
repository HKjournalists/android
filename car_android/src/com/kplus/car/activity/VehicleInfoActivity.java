package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.VehicleModel;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

/**
 * Created by Administrator on 2015/7/14.
 */
public class VehicleInfoActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private UserVehicle mUserVehicle;
    private VehicleAuth mVehicleAuth;
    private TextView mTvVehicleNum;
    private ImageView mIvAuth;
    private TextView mTvModel;
    private TextView mTvLicense;
    private TextView mTvRemark;
    private boolean mbChanged = false;

    public static final int MODEL_RESULT = 0x12;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_vehicle_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("车辆信息");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        mTvVehicleNum = (TextView) findViewById(R.id.vehicle_num);
        mIvAuth = (ImageView) findViewById(R.id.vehicle_auth);
        mTvModel = (TextView) findViewById(R.id.model);
        mTvLicense = (TextView) findViewById(R.id.vehicle_license);
        mTvRemark = (TextView) findViewById(R.id.remark);
    }

    @Override
    protected void loadData() {
        mUserVehicle = (UserVehicle) getIntent().getSerializableExtra("vehicle");
        mVehicleAuth = (VehicleAuth) getIntent().getSerializableExtra("auth");
        mTvVehicleNum.setText(mUserVehicle.getVehicleNum());
        mTvVehicleNum.setTypeface(mApplication.mDin);
        if (mVehicleAuth != null && mVehicleAuth.getStatus() == 2) {
            mIvAuth.setImageResource(R.drawable.ver_activate);
            mTvLicense.setText("已认证");
        }
        else {
            mIvAuth.setImageResource(R.drawable.ver_deactivate);
            mTvLicense.setText("认证车主享豪礼");
        }
        mTvModel.setText(mUserVehicle.getModelName());
        mTvRemark.setText(mUserVehicle.getDescr());
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.model_layout), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.license_layout), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.remark_layout), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.modify_layout), this);
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.model_layout:
                Intent intent = new Intent(this, ModelSelectActivity.class);
                startActivityForResult(intent, Constants.REQUEST_TYPE_MODEL);
                break;
            case R.id.license_layout:
                if (mVehicleAuth != null && mVehicleAuth.getStatus() == 2) {
                    intent = new Intent(this, PrivilegeActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(this, MemberPrivilegeActivity.class);
                    intent.putExtra("vehicleNum", mUserVehicle.getVehicleNum());
                    startActivity(intent);
                }
                break;
            case R.id.remark_layout:
                intent = new Intent(this, AlertDialogActivity.class);
                intent.putExtra("alertType", KplusConstants.ALERT_INPUT_REMARK);
                intent.putExtra("message", mUserVehicle.getDescr());
                startActivityForResult(intent, Constants.REQUEST_TYPE_REMARK);
                break;
            case R.id.modify_layout:
                intent = new Intent(this, VehicleEditActivity.class);
                intent.putExtra("vehicle", mUserVehicle);
                intent.putExtra("hideDel", true);
                intent.putExtra("hideOptional", true);
                startActivityForResult(intent, Constants.REQUEST_TYPE_VEHICLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_TYPE_REMARK:
                if (resultCode == RESULT_OK){
                    String remark = data.getStringExtra("remark");
                    mUserVehicle.setDescr(remark);
                    mTvRemark.setText(remark);
                    mbChanged = true;
                }
                break;
            case Constants.REQUEST_TYPE_MODEL:
                if (resultCode == MODEL_RESULT){
                    VehicleModel vehicleModel = (VehicleModel) data.getSerializableExtra("vehicleModel");
                    mUserVehicle.setVehicleModelId(vehicleModel.getId() == null ? 0 : vehicleModel.getId());
                    mUserVehicle.setModelName(vehicleModel.getName());
                    mUserVehicle.setPicUrl(vehicleModel.getImage());
                    mTvModel.setText(vehicleModel.getName());
                    mbChanged = true;
                }
                break;
            case Constants.REQUEST_TYPE_VEHICLE:
                if (resultCode == Constants.RESULT_TYPE_CHANGED){
                    mbChanged = false;
                    mUserVehicle = (UserVehicle) data.getSerializableExtra("vehicle");
                    mTvModel.setText(mUserVehicle.getModelName());
                    mTvRemark.setText(mUserVehicle.getDescr());
                    Intent it = new Intent();
                    it.putExtra("vehicle", mUserVehicle);
                    setResult(Constants.RESULT_TYPE_CHANGED, it);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mbChanged){
            saveVehicle();
        }
        else {
            super.onBackPressed();
        }
    }

    private void saveVehicle() {
        new AsyncTask<Void, Void, VehicleAddResponse>() {
            protected VehicleAddResponse doInBackground(Void... params) {
                VehicleAddRequest request = new VehicleAddRequest();
                request.setParams(mApplication.getUserId(),mApplication.getId(), mUserVehicle);
                try {
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(VehicleAddResponse result) {
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0 && result.getData().getResult()) {
                        ToastUtil.TextToast(VehicleInfoActivity.this, "修改车辆信息成功", Toast.LENGTH_SHORT, Gravity.CENTER);
                        mApplication.dbCache.saveVehicle(mUserVehicle);
                        Intent it = new Intent();
                        it.putExtra("vehicle", mUserVehicle);
                        setResult(Constants.RESULT_TYPE_CHANGED, it);
                    } else {
                        ToastUtil.TextToast(VehicleInfoActivity.this, StringUtils.isEmpty(result.getMsg()) ? ("修改车辆信息失败") : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } else {
                    ToastUtil.TextToast(VehicleInfoActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT, Gravity.CENTER);
                }
                finish();
            }
        }.execute();
    }
}
