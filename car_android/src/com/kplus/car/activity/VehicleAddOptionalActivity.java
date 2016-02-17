package com.kplus.car.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.R;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleModel;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.util.ClickUtils;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/5/5.
 */
public class VehicleAddOptionalActivity extends BaseActivity implements ClickUtils.NoFastClickListener {
    private View vlDemo, dlDemo;
    private TextView nianjian;
    private TimeMenu mMenu;
    private TextView vehicle_brand_module_view;
    private EditText remark_view;
    private String strNianjian = "";
    private long modelId;
    private String modelName, desc;
    private String picUrl;
    private UserVehicle vehicle;
    private ImageView frameTipImage;
    private AlertDialog alertDialog;
    private static final int MODEL_REQUEST = 0x02;
    public static final int MODEL_RESULT = 0x12;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_vehicle_add_optional);
        vlDemo = findViewById(R.id.vl_info);
        dlDemo = findViewById(R.id.dl_info);
        vehicle_brand_module_view = (TextView) findViewById(R.id.vehicle_brand_module_view);
        remark_view = (EditText) findViewById(R.id.remark_view);
        nianjian = (TextView) findViewById(R.id.annual_reminder_view);
    }

    @Override
    protected void loadData() {
        vehicle = (UserVehicle) getIntent().getSerializableExtra("vehicle");
        String nianjianDate = vehicle.getNianjianDate();
        if (nianjianDate != null && !"".equals(nianjianDate)){
            nianjian.setText(nianjianDate);
        }
        String modelName = vehicle.getModelName();
        if (modelName != null && !"".equals(modelName)){
            vehicle_brand_module_view.setText(modelName);
        }
    }

    @Override
    protected void setListener() {
        ClickUtils.setNoFastClickListener(vehicle_brand_module_view, this);
        ClickUtils.setNoFastClickListener(vlDemo, this);
        ClickUtils.setNoFastClickListener(dlDemo, this);
        ClickUtils.setNoFastClickListener(nianjian, this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.rightView), this);
        ClickUtils.setNoFastClickListener(findViewById(R.id.leftView), this);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onNoFastClick(View v) {
        switch (v.getId()){
            case R.id.vl_info:
                if(frameTipImage == null){
                    frameTipImage = new ImageView(this);
                    frameTipImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(alertDialog != null && alertDialog.isShowing())
                                alertDialog.dismiss();
                        }
                    });
                }
                frameTipImage.setImageDrawable(getResources().getDrawable(R.drawable.vl_demo));
                if(alertDialog == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(VehicleAddOptionalActivity.this);
                    builder.setView(frameTipImage);
                    alertDialog = builder.create();
                }
                alertDialog.show();
                break;
            case R.id.annual_reminder_view:
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(strNianjian));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, calendar, new TimeMenuFactory.onIimerMenuClick() {
                    @Override
                    public void onFinishClick(View v) {
                        mMenu.getSlideMenu().hide();
                        strNianjian = new SimpleDateFormat("yyyy-MM-dd").format(mMenu.getCalendar().getTime());
                        nianjian.setText(strNianjian.replaceAll("-", "/"));
                    }

                    @Override
                    public void onCancelClick(View v) {
                        mMenu.getSlideMenu().hide();
                    }
                });
                mMenu.getSlideMenu().show();
                break;
            case R.id.vehicle_brand_module_view:
                final Intent intent = new Intent();
                intent.setClass(this, ModelSelectActivity.class);
                startActivityForResult(intent, MODEL_REQUEST);
                break;
            case R.id.leftView:
                EventAnalysisUtil.onEvent(this, "click_skip_AddCar", "添加车辆页面-跳过按钮被点击", null);
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.rightView:
                desc = remark_view.getText().toString();
                if (desc != null && desc.length() > 20) {
                    ToastUtil.TextToast(this, "备注内容超过20字符最大限制！", Toast.LENGTH_SHORT, Gravity.CENTER);
                    return;
                }
                if(!StringUtils.isEmpty(strNianjian))
                    vehicle.setNianjianDate(strNianjian);
                if(!StringUtils.isEmpty(desc))
                    vehicle.setDescr(desc);
                if(!StringUtils.isEmpty(picUrl))
                    vehicle.setPicUrl(picUrl);
                if(modelId != 0)
                    vehicle.setVehicleModelId(modelId);
                if(!StringUtils.isEmpty(modelName))
                    vehicle.setModelName(modelName);
                saveVehicle();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MODEL_REQUEST:
                if (resultCode == MODEL_RESULT) {
                    VehicleModel vehicleModel = (VehicleModel) data	.getSerializableExtra("vehicleModel");
                    modelId = (vehicleModel.getId() == null ? 0 : vehicleModel.getId());
                    modelName = vehicleModel.getName();
                    picUrl = vehicleModel.getImage();
                    vehicle_brand_module_view.setText(vehicleModel.getName());
                }
                break;
        }
    }

    private void saveVehicle() {
        showloading(true);
        new AsyncTask<Void, Void, VehicleAddResponse>() {
            protected VehicleAddResponse doInBackground(Void... params) {
                VehicleAddRequest request = new VehicleAddRequest();
                request.setParams(mApplication.getUserId(),mApplication.getId(), vehicle);
                try {
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(VehicleAddResponse result) {
                showloading(false);
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0) {
                        if (result.getData().getResult()) {
                            mApplication.dbCache.saveVehicle(vehicle);
                            updateNianjian();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            ToastUtil.TextToast(VehicleAddOptionalActivity.this,StringUtils.isEmpty(result.getMsg()) ? ("修改车辆信息失败") : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                        }
                    } else {
                        ToastUtil.TextToast(VehicleAddOptionalActivity.this,StringUtils.isEmpty(result.getMsg()) ? ("修改车辆信息失败") : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
                    }
                } else {
                    ToastUtil.TextToast(VehicleAddOptionalActivity.this,"网络中断，请稍候重试", Toast.LENGTH_SHORT, Gravity.CENTER);
                }
            }
        }.execute();
    }

    private void updateNianjian(){
        if (!"".equals(vehicle.getNianjianDate())){
            RemindNianjian nianjian = mApplication.dbCache.getRemindNianjian(vehicle.getVehicleNum());
            if (nianjian != null){
                nianjian.setOrignalDate(vehicle.getNianjianDate());
                Calendar calendar = DateUtil.getCalender(vehicle.getNianjianDate(), "yyyy-MM-dd");
                while (calendar.before(Calendar.getInstance()))
                    calendar.add(Calendar.YEAR, 1);
                nianjian.setDate(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                int gap = DateUtil.getGapCount(Calendar.getInstance().getTime(), calendar.getTime());
                if (gap > 7){
                    calendar.add(Calendar.DATE, -7);
                    calendar.set(Calendar.HOUR_OF_DAY, 9);
                    calendar.set(Calendar.MINUTE, 0);
                    nianjian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime()));
                    calendar.add(Calendar.DATE, 6);
                    nianjian.setRemindDate2(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime()));
                }
                else {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 9);
                    calendar.set(Calendar.MINUTE, 0);
                    nianjian.setRemindDate1(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime()));
                }
                mApplication.dbCache.updateRemindNianjian(nianjian);
            }
        }
    }
}
