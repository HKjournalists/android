package com.kplus.car.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kplus.car.Constants;
import com.kplus.car.R;
import com.kplus.car.adapter.SelectInsuranceAdapter;
import com.kplus.car.asynctask.InsuranceTask;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.json.Insurance;
import com.kplus.car.model.response.InsuranceResponse;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.SlideDownMenu;

import java.util.List;

/**
 * Created by Administrator on 2015/3/31 0031.
 */
public class SelectInsuranceActivity extends BaseActivity implements SelectInsuranceAdapter.OnItemSelectedListener, ClickUtils.NoFastClickListener {
    private View mMyInsuranceLayout;
    private List<Insurance> mListInsurance;
    private RecyclerView mRecyclerView;
    private SelectInsuranceAdapter mAdapter;
    private TextView mMyCompany;
    private TextView mMyPhone;
    private SlideDownMenu mSlideDownMenu;
    private View mSelected;
    private boolean mIsChanged = false;
    private String mCompany = "";
    private String mPhone = "";
    private TextView mYixuan;
    private View mAddCustomInsurance;
    private String mVehicleNum;
    private int mPosition = 0;

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_select_insurance);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("选择保险公司");
        ImageView ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivLeft.setImageResource(R.drawable.daze_but_icons_back);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mMyCompany = (TextView) findViewById(R.id.my_company);
        mMyPhone = (TextView) findViewById(R.id.my_phone);
        mMyInsuranceLayout = findViewById(R.id.my_insurance_layout);
        mSelected = findViewById(R.id.selected);
        mYixuan = (TextView) findViewById(R.id.yixuan);
        mAddCustomInsurance = findViewById(R.id.add_custom_insurance);
    }

    @Override
    protected void loadData() {
        mPosition = getIntent().getIntExtra("position", 0);
        mVehicleNum = getIntent().getStringExtra("vehicleNum");
        mCompany = getIntent().getStringExtra("company");
        mPhone = getIntent().getStringExtra("phone");
        mYixuan.setText(mCompany);
        new InsuranceTask(mApplication){
            @Override
            protected void onPostExecute(InsuranceResponse insuranceResponse) {
                if (insuranceResponse != null){
                    String json = insuranceResponse.getData().getValue();
                    mListInsurance = new Gson().fromJson(json, new TypeToken<List<Insurance>>(){}.getType());
                    mAdapter = new SelectInsuranceAdapter(SelectInsuranceActivity.this, mListInsurance, SelectInsuranceActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    if (mCompany != null && !"".equals(mCompany)){
                        int i = 0;
                        if (mListInsurance != null){
                            for (; i < mListInsurance.size(); i++){
                                if (mCompany.equals(mListInsurance.get(i).getCompany()))
                                    break;
                            }
                        }
                        if (mListInsurance != null && i < mListInsurance.size())
                            mAdapter.setCurIndex(i);
                        else {
                            mMyCompany.setText(mCompany);
                            mMyPhone.setText(mPhone);
                            mAddCustomInsurance.setVisibility(View.GONE);
                            mMyInsuranceLayout.setVisibility(View.VISIBLE);
                            mSelected.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }.execute();
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.modify), this);
        ClickUtils.setNoFastClickListener(mMyInsuranceLayout, this);
        ClickUtils.setNoFastClickListener(mAddCustomInsurance, this);
    }

    @Override
    public void onItemSelected(int position) {
        mIsChanged = true;
        Insurance insurance = mListInsurance.get(position);
        mCompany = insurance.getCompany();
        mPhone = insurance.getPhone();
        mYixuan.setText(mCompany);
        mSelected.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (mIsChanged){
            Intent it = new Intent();
            it.putExtra("position", mPosition);
            it.putExtra("company", mCompany);
            it.putExtra("phone", mPhone);
            UserVehicle uv = mApplication.dbCache.getVehicle(mVehicleNum);
            uv.setCompany(mCompany);
            uv.setPhone(mPhone);
            mApplication.dbCache.updateVehicle(uv);
            Intent intent = new Intent("com.kplus.car.set_insurance");
            intent.putExtra("vehicle", uv);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            setResult(Constants.RESULT_TYPE_CHANGED, it);
        }
        super.onBackPressed();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.leftView:
                onBackPressed();
                break;
            case R.id.add_custom_insurance:
            case R.id.modify:
                mSlideDownMenu = new SlideDownMenu(this, R.color.daze_translucence2);
                mSlideDownMenu.setContentView(R.layout.popup_add_custom_insurance, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case R.id.cancel:
                                mSlideDownMenu.hide();
                                break;
                            case R.id.ok:
                                EditText company = (EditText) mSlideDownMenu.getContentView().findViewById(R.id.company_input);
                                EditText phone = (EditText) mSlideDownMenu.getContentView().findViewById(R.id.phone_input);
                                if ("".equals(company.getText().toString())){
                                    ToastUtil.makeShortToast(SelectInsuranceActivity.this, "请输入保险公司名称");
                                    return;
                                }
                                if ("".equals(phone.getText().toString())){
                                    ToastUtil.makeShortToast(SelectInsuranceActivity.this, "请输入联系电话");
                                    return;
                                }
                                mSlideDownMenu.hide();
                                mAddCustomInsurance.setVisibility(View.GONE);
                                mMyInsuranceLayout.setVisibility(View.VISIBLE);
                                mMyCompany.setText(company.getText().toString());
                                mMyPhone.setText("(" + phone.getText().toString() + ")");
                                onNoFastClick(mMyInsuranceLayout);
                                break;
                        }
                    }
                });
                if (v.getId() == R.id.modify){
                    EditText company = (EditText) mSlideDownMenu.getContentView().findViewById(R.id.company_input);
                    company.setText(mMyCompany.getText());
                    EditText phone = (EditText) mSlideDownMenu.getContentView().findViewById(R.id.phone_input);
                    phone.setText(mMyPhone.getText());
                }
                mSlideDownMenu.show();
                break;
            case R.id.my_insurance_layout:
                mIsChanged = true;
                mCompany = mMyCompany.getText().toString();
                mYixuan.setText(mCompany);
                String phone = mMyPhone.getText().toString();
                mPhone = phone.substring(1, phone.length() - 1);
                mSelected.setVisibility(View.VISIBLE);
                if (mAdapter != null)
                    mAdapter.setCurIndex(-1);
                break;
        }
    }
}
