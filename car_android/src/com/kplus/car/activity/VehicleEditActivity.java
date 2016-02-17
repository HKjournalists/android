package com.kplus.car.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.Constants;
import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.Response;
import com.kplus.car.asynctask.RemindSyncAllTask;
import com.kplus.car.asynctask.RemindSyncTask;
import com.kplus.car.asynctask.RestrictDeleteTask;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.CommonDictionary;
import com.kplus.car.model.HistoryFrameAndMotor;
import com.kplus.car.model.RemindNianjian;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.VehicleModel;
import com.kplus.car.model.json.AuthDetailJson;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.VehicleAddResponse;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.model.response.request.VehicleAddRequest;
import com.kplus.car.model.response.request.VehicleDelRequest;
import com.kplus.car.model.response.request.VehicleRoportRequest;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.receiver.VehicleAddRemindReceiver;
import com.kplus.car.util.DateUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.TimeMenu;
import com.kplus.car.util.TimeMenuFactory;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.antistatic.spinnerwheel.AbstractWheel;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelChangedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelClickedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleEditActivity extends BaseActivity implements OnClickListener {

	private static final int CITY_REQUEST = 0x01;
	public static final int CITY_RESULT = 0x11;
	private static final int MODEL_REQUEST = 0x02;
	public static final int MODEL_RESULT = 0x12;

	private TextView tvTitle, tvRight;
	private View leftView;
	private ImageView ivLeft;
	private View rightView;
	private ImageView ivRight;
	private Button btDeleteVehicle;
	private TextView tvPrefix;
	private EditText license_plate_view;
	private TextView select_query_city_view;
	private View frame_number_layout, frame_number_guide;
	private EditText frame_number_view;
	private View motor_number_layout, motor_number_guide;
	private EditText motor_number_view;
	private View owner_id_layout;
	private EditText owner_id_view;
	private View driver_id_layout, driver_id_guide;
	private EditText driver_id_view;
	private TextView vehicle_brand_module_view;
	private EditText remark_view;
	private PopupWindow provinceWindow;
	private GridView gridView;
	private TextView provinceCancel;
	private TextView frame_number_notice,motor_number_notice, owner_id_notice, driver_id_notice;
	private View error_correction_layout, error_correction_View;
	private TextView vehicle_type_view;
	private View tianjinView, register_account_view, register_account_guide;
	private EditText website_account_view, website_password_view;
	private TextView register_date_view;
	private View submit_error_layout, submit_error_blank_view;
	private ListView errorListview;
	private View tvCancelSubmitError, tvSubmitError;
	private View cashSelectedView;
	private AbstractWheel cashView;
	private TextView cashTitle,tvUnuseCash, tvUseCash;
	private View cashBlankView;
	private View owner_layout, vehicleRegCerNo_layout, driver_name_layout;
	private EditText owner_view, vehicleRegCerNo_view, driver_name_view;
	private ImageView frameTipImage;
	private AlertDialog alertDialog;
	private TextView tianjinLabel;
    private View vlDemo;
    private TextView nianjian;
    private TimeMenu mMenu;

	private UserVehicle vehicle;
	private String cityId;
	private long modelId;
	private String modelName;
	private String picUrl;
	private int frameLen = 0;
	private int motorLen = 0;
	private int ownerIdLen = 0;
	private int driverIdLen = 0;
	private String strFrame, strMotor;
	private String strPrefix, strNum;
	private ArrayList<CityVehicle> selectedCitys = null;
	private VehicleAuth vehicleAuth;
	private String vehicleNumber;
	private String cityName, desc;
	private String prefixSaved;
	private List<String> errorStringList, selectErrorStringList;
	private ErrorAdapter errorAdapter;
	private List<CommonDictionary> vehicleTypes;
	private VehicleTypeAdapter cashAdapter;
	private int currentIndex;
	private HistoryFrameAndMotor historyFrameAndMotor;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_vehicle_edit);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(getResources().getString(R.string.daze_add_vehicle));
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_goback_24);
		rightView = findViewById(R.id.rightView);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivRight.setVisibility(View.GONE);
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText(getResources().getString(R.string.daze_save));
        btDeleteVehicle = (Button) findViewById(R.id.delete_vehicle);
		// 车牌号
		tvPrefix = (TextView) findViewById(R.id.tvPrefix);
		license_plate_view = (EditText) findViewById(R.id.license_plate_view);
		license_plate_view.setTransformationMethod(new AllCapTransformationMethod());

		// 查询地区、车架号、发动机号
		select_query_city_view = (TextView) findViewById(R.id.select_query_city_view);

		frame_number_layout = findViewById(R.id.frame_number_layout);
		frame_number_guide = findViewById(R.id.frame_number_guide);
		frame_number_view = (EditText) findViewById(R.id.frame_number_view);
		frame_number_view.setTransformationMethod(new AllCapTransformationMethod());
		frame_number_view.setEnabled(true);

		motor_number_layout = findViewById(R.id.motor_number_layout);
		motor_number_guide = findViewById(R.id.motor_number_guide);
		motor_number_view = (EditText) findViewById(R.id.motor_number_view);
		motor_number_view.setTransformationMethod(new AllCapTransformationMethod());
		motor_number_view.setEnabled(true);

		owner_id_layout = findViewById(R.id.owner_id_layout);
		owner_id_view = (EditText) findViewById(R.id.owner_id_view);
		owner_id_view.setTransformationMethod(new AllCapTransformationMethod());
		owner_id_view.setEnabled(true);

		driver_id_layout = findViewById(R.id.driver_id_layout);
		driver_id_guide = findViewById(R.id.driver_id_guide);
		driver_id_view = (EditText) findViewById(R.id.driver_id_view);
		driver_id_view.setTransformationMethod(new AllCapTransformationMethod());
		driver_id_view.setEnabled(true);

		frame_number_notice = (TextView) findViewById(R.id.frame_number_notice);
		motor_number_notice = (TextView) findViewById(R.id.motor_number_notice);
		owner_id_notice = (TextView) findViewById(R.id.owner_id_notice);
		driver_id_notice = (TextView) findViewById(R.id.driver_id_notice);

		// 车型、备注
		vehicle_brand_module_view = (TextView) findViewById(R.id.vehicle_brand_module_view);
		remark_view = (EditText) findViewById(R.id.remark_view);
		error_correction_layout = findViewById(R.id.error_correction_layout);
		error_correction_View = findViewById(R.id.error_correction_View);
		vehicle_type_view = (TextView) findViewById(R.id.vehicle_type_view);
		tianjinView = findViewById(R.id.tianjinView);
		register_account_view = findViewById(R.id.register_account_view);
		register_account_guide = findViewById(R.id.register_account_guide);
		website_account_view = (EditText) findViewById(R.id.website_account_view);
		website_password_view = (EditText) findViewById(R.id.website_password_view);
		register_date_view = (TextView) findViewById(R.id.register_date_view);
		submit_error_layout = findViewById(R.id.submit_error_layout);
		submit_error_blank_view = findViewById(R.id.submit_error_blank_view);
		errorListview = (ListView) findViewById(R.id.errorListview);
		tvCancelSubmitError = findViewById(R.id.tvCancelSubmitError);
		tvSubmitError = findViewById(R.id.tvSubmitError);
		cashSelectedView = findViewById(R.id.cashSelectedView);
		cashView = (AbstractWheel)findViewById(R.id.cashView);
		cashTitle = (TextView) findViewById(R.id.cashTitle);
		cashTitle.setText("选择车辆类型");
		tvUnuseCash = (TextView) findViewById(R.id.tvUnuseCash);
		tvUseCash = (TextView) findViewById(R.id.tvUseCash);
		cashBlankView = findViewById(R.id.cashBlankView);
		owner_layout = findViewById(R.id.owner_layout);
		vehicleRegCerNo_layout = findViewById(R.id.vehicleRegCerNo_layout);
		driver_name_layout = findViewById(R.id.driver_name_layout);
		owner_view = (EditText) findViewById(R.id.owner_view);
		vehicleRegCerNo_view = (EditText) findViewById(R.id.vehicleRegCerNo_view);
		driver_name_view = (EditText) findViewById(R.id.driver_name_view);
		tianjinLabel = (TextView) findViewById(R.id.tianjinLabel);
        vlDemo = findViewById(R.id.vl_info);
        nianjian = (TextView) findViewById(R.id.annual_reminder_view);
	}

	@Override
	protected void loadData() {
		vehicleTypes = mApplication.dbCache.getVehicleTypes();
		if(mApplication.getCities() == null || mApplication.getCities().isEmpty()){
			ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_get_city_vehicle_fail), Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
		if(vehicleTypes == null || vehicleTypes.isEmpty()){
			vehicleTypes = new ArrayList<CommonDictionary>();
			CommonDictionary cd = new CommonDictionary();
			cd.setId(1l);
			cd.setType("vehicle_type");
			cd.setValue("小型车");
			vehicleTypes.add(cd);
		}
        vehicle = (UserVehicle) getIntent().getSerializableExtra("vehicle");
		boolean bHideDel = getIntent().getBooleanExtra("hideDel", false);
        if (bHideDel)
            btDeleteVehicle.setVisibility(View.GONE);
		boolean bHideOptional = getIntent().getBooleanExtra("hideOptional", false);
		if (bHideOptional)
			findViewById(R.id.optional_layout).setVisibility(View.GONE);
        selectedCitys = getIntent().getParcelableArrayListExtra("selectedCity");
        tvTitle.setText(getResources().getString(R.string.daze_edit_vehicle_info));
        vehicleNumber = vehicle.getVehicleNum();
        if(StringUtils.isEmpty(vehicleNumber)){
            ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_no_vehicle_number), Toast.LENGTH_SHORT, Gravity.CENTER);
            finish();
            return;
        }
        setData();
        if(mApplication.getId() != 0){
            getVehicleAuth();
        }
        errorStringList = new ArrayList<String>(2);
        errorStringList.add(getResources().getString(R.string.daze_frame_number_error));
        errorStringList.add(getResources().getString(R.string.daze_motor_number_error));
        selectErrorStringList = new ArrayList<String>();
        errorAdapter = new ErrorAdapter();
        errorListview.setAdapter(errorAdapter);
        cashAdapter = new VehicleTypeAdapter(VehicleEditActivity.this);
        cashView.setViewAdapter(cashAdapter);
        cashView.setVisibleItems(5);
	}
	
	private void getVehicleAuth(){
		new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
			@Override
			protected GetAuthDetaiResponse doInBackground(Void... params) {
				GetAuthDetaiRequest request = new GetAuthDetaiRequest();
				request.setParams(mApplication.getUserId(), mApplication.getId(), vehicleNumber);
				try{
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(GetAuthDetaiResponse result) {
				super.onPostExecute(result);
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						AuthDetailJson data = result.getData();
						if(data != null){
							List<VehicleAuth> list = data.getList();
							if(list != null && !list.isEmpty()){
								vehicleAuth = list.get(0);
							}
						}
					}
				}
				if(vehicleAuth == null)
					vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicleNumber);
				updateUIWithAuth();
			}
		}.execute();
	}

	private void setData() {
		try{
            String nianjianDate = vehicle.getNianjianDate();
            nianjian.setText(nianjianDate == null ? "" : nianjianDate.replaceAll("-", "/"));
			historyFrameAndMotor = mApplication.dbCache.getHistoryFrameAndMotor(vehicleNumber);
			tvPrefix.setEnabled(false);
			strPrefix = vehicleNumber.substring(0, 1);
			tvPrefix.setText(strPrefix);
			license_plate_view.setEnabled(false);
			strNum = vehicleNumber.substring(1);
			license_plate_view.setText(strNum);
			desc = vehicle.getDescr();
			if(!StringUtils.isEmpty(desc))
				remark_view.setText(desc);
			modelId = vehicle.getVehicleModelId();
			modelName = vehicle.getModelName();
			if(!StringUtils.isEmpty(modelName))
				vehicle_brand_module_view.setText(modelName);
			picUrl = vehicle.getPicUrl();
			cityName = null;
			cityId = null;
			if(selectedCitys == null){
				cityName = vehicle.getCityName();
				cityId = vehicle.getCityId();		
			}
			else{
				for (CityVehicle cityVehicle : selectedCitys) {
					if (cityId == null) {
						cityId = "" + cityVehicle.getId();
						cityName = "" + cityVehicle.getName();
					} else {
						cityId += "," + cityVehicle.getId();
						cityName += "," + cityVehicle.getName();
					}
				}
			}
			select_query_city_view.setText(cityName);
			if(!StringUtils.isEmpty(vehicle.getAccount()))
				website_account_view.setText(vehicle.getAccount());
			if(!StringUtils.isEmpty(vehicle.getPassword()))
				website_password_view.setText(vehicle.getPassword());
			if(!StringUtils.isEmpty(vehicle.getVehicleOwner()))
				owner_view.setText(vehicle.getVehicleOwner());
			if(!StringUtils.isEmpty(vehicle.getOwnerId()))
				owner_id_view.setText(vehicle.getOwnerId());
			if(!StringUtils.isEmpty(vehicle.getDriverName()))
				driver_name_view.setText(vehicle.getDriverName());
			if(!StringUtils.isEmpty(vehicle.getDriverId()))
				driver_id_view.setText(vehicle.getDriverId());
			if(!StringUtils.isEmpty(vehicle.getVehicleRegCerNo()))
				vehicleRegCerNo_view.setText(vehicle.getVehicleRegCerNo());
			getFrameAndMotorLength();
			showFrame();
			showMotor();
			showOwnerId();
			showDriverId();
			setLengthFilters();
			strFrame = vehicle.getFrameNum();
			strMotor = vehicle.getMotorNum();
			autofillFrame();
			autofillMotor();
			if(vehicle.getVehicleType() != null){
				for(CommonDictionary cd : vehicleTypes){
					if(cd.getId().longValue() == vehicle.getVehicleType().intValue()){
						vehicle_type_view.setText(cd.getValue());
						cashView.setCurrentItem(vehicleTypes.indexOf(cd));
						break;
					}
				}
				if(StringUtils.isEmpty(vehicle_type_view.getText().toString())){
					vehicle_type_view.setText(vehicleTypes.get(0).getValue());
					cashView.setCurrentItem(0);
				}
			}
			else{
				vehicle_type_view.setText(vehicleTypes.get(0).getValue());
				cashView.setCurrentItem(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(VehicleEditActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void updateUIWithCityVehicle(CityVehicle cityVehicle){
		prefixSaved = cityVehicle.getPrefix();
		cityId = "" + cityVehicle.getId();
		cityName = cityVehicle.getName();
		select_query_city_view.setText(cityName);
		String strTemp = null;
		if(cityVehicle.getAccountLen() != null && cityVehicle.getAccountLen() != 0){
			tianjinView.setVisibility(View.VISIBLE);
			if(strTemp == null)
				strTemp = cityVehicle.getName();
			else
				strTemp += ("," + cityVehicle.getName()); 
			if(cityName.contains("天津")){
				register_account_view.setVisibility(View.VISIBLE);
			}
			else{
				register_account_view.setVisibility(View.GONE);
			}
		}
		else{
			tianjinView.setVisibility(View.GONE);
		}
		if(strTemp != null){
			tianjinLabel.setText(strTemp + "违章查询需要官网账户信息");
		}
		if(vehicle != null){
			if(!StringUtils.isEmpty(vehicle.getAccount()))
				website_account_view.setText(vehicle.getAccount());
			if(!StringUtils.isEmpty(vehicle.getPassword()))
				website_password_view.setText(vehicle.getPassword());
		}
		frameLen = cityVehicle.getFrameNumLen();
		motorLen = cityVehicle.getMotorNumLen();
		ownerIdLen = cityVehicle.getOwnerIdNoLen() != null ? cityVehicle.getOwnerIdNoLen() : 0;
		driverIdLen = cityVehicle.getDrivingLicenseNoLen() != null ? cityVehicle.getDrivingLicenseNoLen() : 0;
		if(frameLen == -1)
			frameLen = 99;
		showFrame();
		if (motorLen == -1)
			motorLen = 99;
		showMotor();
		if (ownerIdLen == -1)
			ownerIdLen = 99;
		showOwnerId();
		updateOwnerIdNoticeView();
		if (driverIdLen == -1)
			driverIdLen = 99;
		showDriverId();
		updateDriverIdNoticeView();
		setLengthFilters();
		strFrame = frame_number_view.getText().toString();
		if(strFrame == null)
			strFrame = "";
		strMotor = motor_number_view.getText().toString();
		if(strMotor == null)
			strMotor = "";
		String strHistoryFrame = null;
		String strHistoryMotor = null;
		if(historyFrameAndMotor != null){
			strHistoryFrame = historyFrameAndMotor.getHistoryFrame();
			strHistoryMotor = historyFrameAndMotor.getHistoryMotor();
			if(frameLen > 0){
				if(StringUtils.isEmpty(strHistoryFrame) || strFrame.length() >= strHistoryFrame.length()){
					historyFrameAndMotor.setHistoryFrame(strFrame);
					if(frameLen == 99){
						if(strFrame.length() == 17)
							frame_number_view.setText(strFrame);
						else
							frame_number_view.setText("");
					}
					else{
						if(strFrame.length() == frameLen)
							frame_number_view.setText(strFrame);
						else if(strFrame.length() < frameLen){
							frame_number_view.setText("");
						}
						else
							frame_number_view.setText(strFrame.substring(strFrame.length() - frameLen));
					}
				}
				else{
					strHistoryFrame = strHistoryFrame.substring(0, strHistoryFrame.length() - strFrame.length());
					strHistoryFrame += strFrame;
					historyFrameAndMotor.setHistoryFrame(strHistoryFrame);
					if(frameLen == 99){
						if(strHistoryFrame.length() == 17)
							frame_number_view.setText(strHistoryFrame);
						else
							frame_number_view.setText("");
					}
					else{
						if(strHistoryFrame.length() == frameLen){
							frame_number_view.setText(strHistoryFrame);
						}
						if(strHistoryFrame.length() < frameLen){
							frame_number_view.setText("");
						}
						else
							frame_number_view.setText(strHistoryFrame.substring(strHistoryFrame.length() - frameLen));
					}
				}
			}
			if(motorLen > 0){
				if(StringUtils.isEmpty(strHistoryMotor) || strMotor.length() >= strHistoryMotor.length()){
					historyFrameAndMotor.setHistoryMotor(strMotor);
					if(motorLen == 99){
						motor_number_view.setText(strMotor);
					}
					else{
						if(strMotor.length() <= motorLen){
							motor_number_view.setText(strMotor);
						}
						else
							motor_number_view.setText(strMotor.substring(strMotor.length() - motorLen));
					}
				}
				else{
					strHistoryMotor = strHistoryMotor.substring(0, strHistoryMotor.length() - strMotor.length());
					strHistoryMotor += strMotor;
					historyFrameAndMotor.setHistoryMotor(strHistoryMotor);
					if(motorLen == 99){
						motor_number_view.setText(strHistoryMotor);
					}
					else{
						if(strHistoryMotor.length() <= motorLen){
							motor_number_view.setText(strHistoryMotor);
						}
						else
							motor_number_view.setText(strHistoryMotor.substring(strHistoryMotor.length() - motorLen));
					}
				}
			}	
		}
		else{
			motor_number_view.setText(strFrame);
			frame_number_view.setText(strMotor);
		}
		if(cityVehicle.getOwner() != null && cityVehicle.getOwner())
			owner_layout.setVisibility(View.VISIBLE);
		else
			owner_layout.setVisibility(View.GONE);
		if(cityVehicle.getMotorvehiclenumLen() != null && cityVehicle.getMotorvehiclenumLen().intValue() != 0)
			vehicleRegCerNo_layout.setVisibility(View.VISIBLE);
		else
			vehicleRegCerNo_layout.setVisibility(View.GONE);
	}
	
	private void showFrame(){
		if (frameLen > 0) {
			frame_number_layout.setVisibility(View.VISIBLE);
			if (frameLen == 99) {
				frame_number_view.setHint("请输入完整车架号");
				frame_number_notice.setText("请输入完整车架号");
			} else {
				frame_number_view.setHint("请输入车架号后" + frameLen + "位");
				frame_number_notice.setText("请输入车架号后" + frameLen + "位");
			}
		}
		else{
			frame_number_layout.setVisibility(View.GONE);
			frame_number_notice.setVisibility(View.GONE);
		}
	}
	
	private void showMotor(){
		if (motorLen > 0) {
			motor_number_layout.setVisibility(View.VISIBLE);
			if (motorLen == 99) {
				motor_number_view.setHint("请输入完整发动机号");
				motor_number_notice.setText("请输入完整发动机号");
			} else {
				motor_number_view.setHint("请输入发动机号后" + motorLen + "位");
				motor_number_notice.setText("请输入发动机号后" + motorLen + "位");
			}
		}
		else{
			motor_number_layout.setVisibility(View.GONE);
			motor_number_notice.setVisibility(View.GONE);
		}
	}

	private void showOwnerId(){
		if (ownerIdLen > 0) {
			owner_id_layout.setVisibility(View.VISIBLE);
			if (ownerIdLen == 99) {
				owner_id_view.setHint("请输入完整车主身份证号");
				owner_id_notice.setText("请输入完整车主身份证号");
			} else {
				owner_id_view.setHint("请输入车主身份证号后" + ownerIdLen + "位");
				owner_id_notice.setText("请输入车主身份证号后" + ownerIdLen + "位");
			}
		}
		else{
			owner_id_layout.setVisibility(View.GONE);
			owner_id_notice.setVisibility(View.GONE);
		}
	}

	private void showDriverId(){
		if (driverIdLen > 0) {
			driver_id_layout.setVisibility(View.VISIBLE);
			if (driverIdLen == 99) {
				driver_id_view.setHint("请输入完整驾驶证号");
				driver_id_notice.setText("请输入完整驾驶证号");
			} else {
				driver_id_view.setHint("请输入驾驶证号后" + driverIdLen + "位");
				driver_id_notice.setText("请输入驾驶证号后" + driverIdLen + "位");
			}
		}
		else{
			driver_id_layout.setVisibility(View.GONE);
			driver_id_notice.setVisibility(View.GONE);
		}
	}

	private void setLengthFilters(){
		setFrameLengthFilter();
		setMotorLengthFilter();
		setOwnerIdLengthFilter();
		setDriverIdLengthFilter();
	}
	
	private void setFrameLengthFilter(){
		if(frameLen > 0 && frameLen != 99){
			frame_number_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(frameLen)});
		}
		else if(frameLen == 99){
			frame_number_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
		}
	}
	
	private void setMotorLengthFilter(){
		if(motorLen > 0 && motorLen != 99){
			motor_number_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(motorLen)});
		}
		else if(motorLen == 99){
			motor_number_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
		}
	}

	private void setOwnerIdLengthFilter(){
		if(ownerIdLen > 0 && ownerIdLen != 99){
			owner_id_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ownerIdLen)});
		}
		else if(ownerIdLen == 99){
			owner_id_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
		}
	}

	private void setDriverIdLengthFilter(){
		if(driverIdLen > 0 && driverIdLen != 99){
			driver_id_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(driverIdLen)});
		}
		else if(driverIdLen == 99){
			driver_id_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
		}
	}

	@Override
	protected void setListener() {
		addFocusChangeListenerToEditText(website_account_view);
		addFocusChangeListenerToEditText(website_password_view);
		addFocusChangeListenerToEditText(remark_view);
		addFocusChangeListenerToEditText(owner_view);
		addFocusChangeListenerToEditText(vehicleRegCerNo_view);
		addFocusChangeListenerToEditText(owner_id_view);
		addFocusChangeListenerToEditText(driver_name_view);
		addFocusChangeListenerToEditText(driver_id_view);
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
        btDeleteVehicle.setOnClickListener(this);
		tvPrefix.setOnClickListener(this);
		select_query_city_view.setOnClickListener(this);
		vehicle_brand_module_view.setOnClickListener(this);
		vehicle_type_view.setOnClickListener(this);
		license_plate_view.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					strNum = license_plate_view.getText().toString();
					if (strNum != null)
						license_plate_view.setSelection(strNum.length());
				}
			}
		});
		license_plate_view.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				strPrefix = tvPrefix.getText().toString();
				if (!StringUtils.isEmpty(strPrefix)) {
					strNum = license_plate_view.getText().toString().toUpperCase();
					if (!StringUtils.isEmpty(strNum)) {
						if (!Character.isLetter(strNum.charAt(0))) {
							ToastUtil.TextToast(VehicleEditActivity.this, "车牌号必须以字母开头", Toast.LENGTH_SHORT, Gravity.CENTER);
							return;
						}
						if (!StringUtils.isNumberOrLetter(strNum)) {
							ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_vehicle_number_constitute_error), Toast.LENGTH_SHORT, Gravity.CENTER);
							int indexTemp = -1;
							int strLen = strNum.length();
							for (int i = 0; i < strLen; i++) {
								char c = strNum.charAt(i);
								if (c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z') {
									indexTemp = i;
									break;
								}
							}
							if (indexTemp != -1) {
								String strError = null;
								if (indexTemp < (strLen - 1)) {
									strError = strNum.substring(indexTemp, indexTemp + 1);
								} else if (indexTemp == (strLen - 1)) {
									strError = strNum.substring(indexTemp);
								}
								if (strError != null) {
									strNum = strNum.replace(strError, "");
									license_plate_view.setText(strNum);
								}
								if (strNum != null)
									license_plate_view.setSelection(strNum.length());
							}
							return;
						}
						if (strNum.length() == 1) {
							if (historyFrameAndMotor != null) {
								mApplication.dbCache.saveHistoryFrameAndMotor(historyFrameAndMotor);
							}
							historyFrameAndMotor = null;
							if (StringUtils.isLetter(strNum)) {
								if (StringUtils.isEmpty(prefixSaved) || !(strPrefix + strNum).equalsIgnoreCase(prefixSaved)) {
									vehicleNumber = strPrefix + strNum.toUpperCase();
									for (CityVehicle cityVehicle : mApplication.getCities()) {
										if (vehicleNumber.equalsIgnoreCase(cityVehicle.getPrefix())) {
											updateUIWithCityVehicle(cityVehicle);
											break;
										}
									}
								}
							}
						} else if (strNum.length() == 6) {
							vehicleNumber = strPrefix + strNum.toUpperCase();
							historyFrameAndMotor = mApplication.dbCache.getHistoryFrameAndMotor(vehicleNumber);
							if (historyFrameAndMotor == null) {
								historyFrameAndMotor = new HistoryFrameAndMotor();
								historyFrameAndMotor.setVehicleNum(vehicleNumber);
							}
							strFrame = frame_number_view.getText().toString();
							if (strFrame == null)
								strFrame = "";
							strMotor = motor_number_view.getText().toString();
							if (strMotor == null)
								strMotor = "";
							String strHistoryFrame = null;
							String strHistoryMotor = null;
							strHistoryFrame = historyFrameAndMotor.getHistoryFrame();
							strHistoryMotor = historyFrameAndMotor.getHistoryMotor();
							if (frameLen > 0) {
								if (StringUtils.isEmpty(strHistoryFrame) || strFrame.length() >= strHistoryFrame.length()) {
									historyFrameAndMotor.setHistoryFrame(strFrame);
									if (frameLen == 99) {
										frame_number_view.setText(strFrame);
									} else {
										if (strFrame.length() <= frameLen) {
											frame_number_view.setText(strFrame);
										} else
											frame_number_view.setText(strFrame.substring(strFrame.length() - frameLen));
									}
								} else {
									strHistoryFrame = strHistoryFrame.substring(0, strHistoryFrame.length() - strFrame.length());
									strHistoryFrame += strFrame;
									historyFrameAndMotor.setHistoryFrame(strHistoryFrame);
									if (frameLen == 99) {
										frame_number_view.setText(strHistoryFrame);
									} else {
										if (strHistoryFrame.length() <= frameLen) {
											frame_number_view.setText(strHistoryFrame);
										} else
											frame_number_view.setText(strHistoryFrame.substring(strHistoryFrame.length() - frameLen));
									}
								}
							}
							if (motorLen > 0) {
								if (StringUtils.isEmpty(strHistoryMotor) || strMotor.length() >= strHistoryMotor.length()) {
									historyFrameAndMotor.setHistoryMotor(strMotor);
									if (motorLen == 99) {
										motor_number_view.setText(strMotor);
									} else {
										if (strMotor.length() <= motorLen) {
											motor_number_view.setText(strMotor);
										} else
											motor_number_view.setText(strMotor.substring(strMotor.length() - motorLen));
									}
								} else {
									strHistoryMotor = strHistoryMotor.substring(0, strHistoryMotor.length() - strMotor.length());
									strHistoryMotor += strMotor;
									historyFrameAndMotor.setHistoryMotor(strHistoryMotor);
									if (motorLen == 99) {
										motor_number_view.setText(strHistoryMotor);
									} else {
										if (strHistoryMotor.length() <= motorLen) {
											motor_number_view.setText(strHistoryMotor);
										} else
											motor_number_view.setText(strHistoryMotor.substring(strHistoryMotor.length() - motorLen));
									}
								}
							}
							if (mApplication.getId() != 0)
								getVehicleAuth();
						} else {
							if (historyFrameAndMotor != null) {
								mApplication.dbCache.saveHistoryFrameAndMotor(historyFrameAndMotor);
							}
							historyFrameAndMotor = null;
						}
					} else {
						if (strNum != null)
							ToastUtil.TextToast(VehicleEditActivity.this, "车牌号不能包含空格", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
			}
		});

		frame_number_view.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(hasFocus){
			        if(frame_number_view.getText().toString() != null)
						frame_number_view.setSelection(frame_number_view.getText().toString().length());
					updateFrameNoticeView();
				}
			}
		});

		frame_number_view.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				updateFrameNoticeView();
			}
		});
		
		motor_number_view.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(hasFocus){
                    if(motor_number_view.getText().toString() != null)
                        motor_number_view.setSelection(motor_number_view.getText().toString().length());
                    updateMotorNoticeView();
				}
			}
		});
		
		motor_number_view.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				updateMotorNoticeView();				
			}
		});
		owner_id_view.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateOwnerIdNoticeView();
			}
		});
		driver_id_view.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				updateDriverIdNoticeView();
			}
		});
		error_correction_View.setOnClickListener(this);
		register_account_view.setOnClickListener(this);
		register_account_guide.setOnClickListener(this);
		frame_number_guide.setOnClickListener(this);
		motor_number_guide.setOnClickListener(this);
        vlDemo.setOnClickListener(this);
        nianjian.setOnClickListener(this);
		submit_error_blank_view.setOnClickListener(this);
		tvCancelSubmitError.setOnClickListener(this);
		tvSubmitError.setOnClickListener(this);
		cashView.addChangingListener(new OnWheelChangedListener() {			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				currentIndex = newValue;
			}
		});
		cashView.addClickingListener(new OnWheelClickedListener() {			
			@Override
			public void onItemClicked(AbstractWheel wheel, int itemIndex) {
				currentIndex = itemIndex;
				cashView.setCurrentItem(itemIndex);
			}
		});
		tvUnuseCash.setOnClickListener(this);
		tvUseCash.setOnClickListener(this);
		cashBlankView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final Intent intent = new Intent();
		if (v.equals(leftView)) {
			if(historyFrameAndMotor != null)
				mApplication.dbCache.saveHistoryFrameAndMotor(historyFrameAndMotor);
			setResult(KplusConstants.FLAG_CANCEL_ADD);
			finish();
		}
        else if (v.equals(btDeleteVehicle)){
            Intent alertIntent = new Intent(this, AlertDialogActivity.class);
            alertIntent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
            alertIntent.putExtra("title", "确定删除车辆" + vehicle.getVehicleNum() + "?");
            alertIntent.putExtra("message", "删除车辆将会清除所有相关数据，该操作不可撤消，请谨慎操作");
            startActivityForResult(alertIntent, Constants.REQUEST_TYPE_DELETE);
        }
        else if (v.equals(nianjian)){
            Calendar calendar = Calendar.getInstance();
            if (vehicle.getNianjianDate() != null){
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(vehicle.getNianjianDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            mMenu = TimeMenuFactory.getInstance(this).buildDateMenu(R.layout.menu_select_date, TimeMenu.START_YEAR, TimeMenu.END_YEAR, calendar, new TimeMenuFactory.onIimerMenuClick() {
                @Override
                public void onFinishClick(View v) {
                    mMenu.getSlideMenu().hide();
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(mMenu.getCalendar().getTime());
                    nianjian.setText(date.replaceAll("-", "/"));
                    vehicle.setNianjianDate(date);
                }

                @Override
                public void onCancelClick(View v) {
                    mMenu.getSlideMenu().hide();
                }
            });
            mMenu.getSlideMenu().show();
        }
        else if (v.equals(rightView)) {
			if(StringUtils.isEmpty(vehicle_type_view.getText().toString())){
				ToastUtil.TextToast(VehicleEditActivity.this, "请选择车辆类型", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			strPrefix = tvPrefix.getText().toString().trim();
			if (StringUtils.isEmpty(strPrefix)) {
				ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_please_select_license_plate_number_prefix), Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			strNum = license_plate_view.getText().toString();
			if (StringUtils.isEmpty(strNum)) {
				ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_please_enter_the_license_plate_number), Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			} else if (strNum.length() != 6) {
				ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_license_plate_number_length_error), Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			else if(!StringUtils.isNumberOrLetter(strNum)){
				ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_vehicle_number_constitute_error), Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}else {
				vehicleNumber = strPrefix + strNum.toUpperCase();
				if(vehicle == null){
					if(!StringUtils.isEmpty(vehicleNumber))
						vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
					if(vehicle == null)
						vehicle = new UserVehicle();
				}
				vehicle.setVehicleNum(vehicleNumber);
				vehicle.setVehicleType((int)vehicleTypes.get(currentIndex).getId().longValue());
			}
			if (StringUtils.isEmpty(cityId)) {
				ToastUtil.TextToast(VehicleEditActivity.this, getResources().getString(R.string.daze_please_select_query_city), Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			} else {
				vehicle.setCityId(cityId);
				vehicle.setCityName(select_query_city_view.getText().toString());
			}
			if(tianjinView.getVisibility() == View.VISIBLE){
				if(StringUtils.isEmpty(website_account_view.getText().toString())){
					ToastUtil.TextToast(VehicleEditActivity.this, "请输入官网账号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				if(StringUtils.isEmpty(website_password_view.getText().toString())){
					ToastUtil.TextToast(VehicleEditActivity.this, "请输入官网密码", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				vehicle.setAccount(website_account_view.getText().toString());
				vehicle.setPassword(website_password_view.getText().toString());
			}
			if (frame_number_layout.isShown()) {
				strFrame = frame_number_view.getText().toString().toUpperCase();
				if(!frame_number_view.isEnabled()){
					vehicle.setFrameNum(strFrame);
				}
				else{
					updateFrameNoticeView();
					if (StringUtils.isEmpty(strFrame)) {
						if (frameLen == 99) {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入完整车架号！", Toast.LENGTH_SHORT, Gravity.CENTER);
						} else {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入车架号后" + frameLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
						frame_number_notice.setVisibility(View.VISIBLE);
						return;
					} else {
						if(frameLen != 99 && strFrame.length() != frameLen){
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入车架号后" + frameLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
							frame_number_notice.setVisibility(View.VISIBLE);
							return;
						}
						else if(frameLen == 99 && strFrame.length() != 17){
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入完整车架号", Toast.LENGTH_SHORT, Gravity.CENTER);
							frame_number_notice.setVisibility(View.VISIBLE);
							return;
						}
						frame_number_notice.setVisibility(View.GONE);
						vehicle.setFrameNum(strFrame);
					}
				}
			}
			if (motor_number_layout.isShown()) {
				strMotor = motor_number_view.getText().toString().toUpperCase();
				if(!motor_number_view.isEnabled()){
					vehicle.setMotorNum(strMotor);
				}
				else{
					updateMotorNoticeView();				
					if (StringUtils.isEmpty(strMotor)) {
						if (motorLen == 99) {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入完整发动机号！", Toast.LENGTH_SHORT, Gravity.CENTER);
						} else {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入发动机号后" + motorLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
						motor_number_notice.setVisibility(View.VISIBLE);
						return;
					} else {
						if(motorLen != 99 && strMotor.length() != motorLen){
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入发动机号后" + motorLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
							motor_number_notice.setVisibility(View.VISIBLE);
							return;
						}
						motor_number_notice.setVisibility(View.GONE);
						vehicle.setMotorNum(strMotor);
					}
				}
			}
			desc = remark_view.getText().toString();
			if (desc != null && desc.length() > 20) {
				ToastUtil.TextToast(VehicleEditActivity.this, "备注内容超过20字符最大限制！", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(!StringUtils.isEmpty(desc))
				vehicle.setDescr(desc);
			if(!StringUtils.isEmpty(picUrl))
				vehicle.setPicUrl(picUrl);
			if(modelId != 0)
				vehicle.setVehicleModelId(modelId);
			if(!StringUtils.isEmpty(modelName))
				vehicle.setModelName(modelName);
			if(owner_layout.getVisibility() == View.VISIBLE){
				if(StringUtils.isEmpty(owner_view.getText().toString())){
					ToastUtil.TextToast(VehicleEditActivity.this, "请输入车主姓名", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				vehicle.setVehicleOwner(owner_view.getText().toString());
			}
			if (owner_id_layout.isShown()) {
				String strOwnerId = owner_id_view.getText().toString().toUpperCase();
				if(!owner_id_view.isEnabled()){
					vehicle.setOwnerId(strOwnerId);
				}
				else{
					updateOwnerIdNoticeView();
					if (StringUtils.isEmpty(strOwnerId)) {
						if (ownerIdLen == 99) {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入完整车主身份证号！", Toast.LENGTH_SHORT, Gravity.CENTER);
						} else {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入车主身份证号后" + ownerIdLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
						owner_id_notice.setVisibility(View.VISIBLE);
						return;
					} else {
						if(ownerIdLen != 99 && strOwnerId.length() != ownerIdLen){
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入车主身份证号后" + ownerIdLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
							owner_id_notice.setVisibility(View.VISIBLE);
							return;
						}
						owner_id_notice.setVisibility(View.GONE);
						vehicle.setOwnerId(strOwnerId);
					}
				}
			}
			if(driver_name_layout.getVisibility() == View.VISIBLE){
				if(StringUtils.isEmpty(driver_name_view.getText().toString())){
					ToastUtil.TextToast(VehicleEditActivity.this, "请输入驾驶证姓名", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				vehicle.setDriverName(driver_name_view.getText().toString());
			}
			if (driver_id_layout.isShown()) {
				String strDriverId = driver_id_view.getText().toString().toUpperCase();
				if(!driver_id_view.isEnabled()){
					vehicle.setDriverId(strDriverId);
				}
				else{
					updateDriverIdNoticeView();
					if (StringUtils.isEmpty(strDriverId)) {
						if (driverIdLen == 99) {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入完整驾驶证号！", Toast.LENGTH_SHORT, Gravity.CENTER);
						} else {
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入驾驶证号后" + driverIdLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
						driver_id_notice.setVisibility(View.VISIBLE);
						return;
					} else {
						if(driverIdLen != 99 && strDriverId.length() != driverIdLen){
							ToastUtil.TextToast(VehicleEditActivity.this, "请输入驾驶证号后" + driverIdLen + "位！", Toast.LENGTH_SHORT, Gravity.CENTER);
							driver_id_notice.setVisibility(View.VISIBLE);
							return;
						}
						driver_id_notice.setVisibility(View.GONE);
						vehicle.setDriverId(strDriverId);
					}
				}
			}
			if(vehicleRegCerNo_layout.getVisibility() == View.VISIBLE){
				if(StringUtils.isEmpty(vehicleRegCerNo_view.getText().toString())){
					ToastUtil.TextToast(VehicleEditActivity.this, "请输入机动车登记证书编号", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				vehicle.setVehicleRegCerNo(vehicleRegCerNo_view.getText().toString());
			}
			if(!StringUtils.isEmpty(register_date_view.getText().toString()))
				vehicle.setRegDate(register_date_view.getText().toString());
			saveVehicle();
		} else if (v.equals(tvPrefix)) {
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VehicleEditActivity.this.getCurrentFocus().getWindowToken(), 0);
			if (provinceWindow == null) {
				initProvinceWindow();
			}
			provinceWindow.showAtLocation(findViewById(R.id.vehicle_add_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		} else if (v.equals(select_query_city_view)) {
			if(historyFrameAndMotor != null){
				strFrame = frame_number_view.getText().toString();
				if(strFrame == null)
					strFrame = "";
				strMotor = motor_number_view.getText().toString();
				if(strMotor == null)
					strMotor = "";
				String strHistoryFrame = null;
				String strHistoryMotor = null;
				strHistoryFrame = historyFrameAndMotor.getHistoryFrame();
				strHistoryMotor = historyFrameAndMotor.getHistoryMotor();
				if(StringUtils.isEmpty(strHistoryFrame) || strFrame.length() >= strHistoryFrame.length()){
					historyFrameAndMotor.setHistoryFrame(strFrame);
				}
				else{
					strHistoryFrame = strHistoryFrame.substring(0, strHistoryFrame.length() - strFrame.length());
					strHistoryFrame += strFrame;
					historyFrameAndMotor.setHistoryFrame(strHistoryFrame);
				}
				if(StringUtils.isEmpty(strHistoryMotor) || strMotor.length() >= strHistoryMotor.length()){
					historyFrameAndMotor.setHistoryMotor(strMotor);
				}
				else{
					strHistoryMotor = strHistoryMotor.substring(0, strHistoryMotor.length() - strMotor.length());
					strHistoryMotor += strMotor;
					historyFrameAndMotor.setHistoryMotor(strHistoryMotor);
				}
				mApplication.dbCache.saveHistoryFrameAndMotor(historyFrameAndMotor);
			}			
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VehicleEditActivity.this.getCurrentFocus().getWindowToken(), 0);
			select_query_city_view.postDelayed(new Runnable() {				
				@Override
				public void run() {
					intent.setClass(VehicleEditActivity.this, CitySelectActivity.class);
					intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_VEHICLE_ADD);
					intent.putExtra("cityId", cityId);
					if(vehicle != null)
						intent.putExtra("vehicle", vehicle);
					startActivityForResult(intent, CITY_REQUEST);
				}
			}, 200);
		} else if (v.equals(vehicle_brand_module_view)) {
			intent.setClass(this, ModelSelectActivity.class);
			startActivityForResult(intent, MODEL_REQUEST);
		} else if (v.equals(provinceCancel)) {
			provinceWindow.dismiss();
		} 
		else if(v.equals(error_correction_View)){
			submit_error_layout.setVisibility(View.VISIBLE);
			if(selectErrorStringList.isEmpty())
				tvSubmitError.setEnabled(false);
			else
				tvSubmitError.setEnabled(true);
		}
		else if (v.equals(register_account_view)){        
	        intent.setAction("android.intent.action.VIEW");    
	        Uri content_url = Uri.parse("http://www.tjits.cn/");   
	        intent.setData(content_url);  
	        startActivity(intent);
		}
		else if (v.equals(frame_number_guide) || v.equals(motor_number_guide) || v.equals(vlDemo)) {
			if(frameTipImage == null){
				frameTipImage = new ImageView(this);
				frameTipImage.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						if(alertDialog != null && alertDialog.isShowing())
							alertDialog.dismiss();
					}
				});
			}
			frameTipImage.setImageDrawable(getResources().getDrawable(R.drawable.vl_demo));
			if(alertDialog == null){
				AlertDialog.Builder builder = new AlertDialog.Builder(VehicleEditActivity.this);
				alertDialog = builder.create();
			}
			alertDialog.setView(frameTipImage);
			alertDialog.show();
		}
		else if (v.equals(driver_id_guide)) {
			if(frameTipImage == null){
				frameTipImage = new ImageView(this);
				frameTipImage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(alertDialog != null && alertDialog.isShowing())
							alertDialog.dismiss();
					}
				});
			}
			frameTipImage.setImageDrawable(getResources().getDrawable(R.drawable.dl_demo));
			if(alertDialog == null){
				AlertDialog.Builder builder = new AlertDialog.Builder(VehicleEditActivity.this);
				alertDialog = builder.create();
			}
			alertDialog.setView(frameTipImage);
			alertDialog.show();
		}
		else if(v.equals(submit_error_blank_view)){
			submit_error_layout.setVisibility(View.GONE);
		}
		else if(v.equals(tvCancelSubmitError)){
			submit_error_layout.setVisibility(View.GONE);
		}
		else if(v.equals(tvSubmitError)){
			submitError();
			submit_error_layout.setVisibility(View.GONE);
		}
		else if(v.equals(vehicle_type_view)){
			if(vehicleTypes != null && !vehicleTypes.isEmpty()){
				cashSelectedView.setVisibility(View.VISIBLE);
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VehicleEditActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
		}
		else if(v.equals(tvUnuseCash)){
			cashSelectedView.setVisibility(View.GONE);
		}
		else if(v.equals(tvUseCash)){
			vehicle_type_view.setText(vehicleTypes.get(currentIndex).getValue());
			cashSelectedView.setVisibility(View.GONE);
		}
		else if(v.equals(cashBlankView)){
			cashSelectedView.setVisibility(View.GONE);
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
							ToastUtil.TextToast(VehicleEditActivity.this,"修改车辆信息成功", Toast.LENGTH_SHORT, Gravity.CENTER);

							if(historyFrameAndMotor != null){
								strFrame = vehicle.getFrameNum();
								if(!StringUtils.isEmpty(strFrame)){
									String strHistoryFrame = historyFrameAndMotor.getHistoryFrame();
									if(StringUtils.isEmpty(strHistoryFrame) || frameLen == 99)
										historyFrameAndMotor.setHistoryFrame(strFrame);
									else{
										if(strFrame.length() >= strHistoryFrame.length())
											historyFrameAndMotor.setHistoryFrame(strFrame);
										else{
											strHistoryFrame = strHistoryFrame.substring(0, strHistoryFrame.length() - strFrame.length());
											strHistoryFrame += strFrame;
											historyFrameAndMotor.setHistoryFrame(strHistoryFrame);
										}
									}
								}
								strMotor = vehicle.getMotorNum();
								if(!StringUtils.isEmpty(strMotor)){
									String strHistoryMotor= historyFrameAndMotor.getHistoryMotor();
									if(StringUtils.isEmpty(strHistoryMotor) || motorLen == 99)
										historyFrameAndMotor.setHistoryMotor(strMotor);
									else{
										if(strMotor.length() >= strHistoryMotor.length())
											historyFrameAndMotor.setHistoryMotor(strMotor);
										else{
											strHistoryMotor = strHistoryMotor.substring(0, strHistoryMotor.length() - strMotor.length());
											strHistoryMotor += strMotor;
											historyFrameAndMotor.setHistoryMotor(strHistoryMotor);
										}
									}
								}
								mApplication.dbCache.saveHistoryFrameAndMotor(historyFrameAndMotor);
							}							
							mApplication.dbCache.saveVehicle(vehicle);
							if(vehicleAuth != null){
								mApplication.dbCache.saveVehicleAuth(vehicleAuth);
							}
							updateNianjian();
							if(sp.getBoolean("hasRemind", false)){
								sp.edit().putBoolean("hasRemind", false).commit();
								PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), VehicleAddRemindReceiver.class), 0);								
								((AlarmManager)getSystemService(Context.ALARM_SERVICE)).cancel(pi);					
							}
                            Intent it = new Intent();
                            it.putExtra("vehicle", vehicle);
                            setResult(Constants.RESULT_TYPE_CHANGED, it);
                            finish();
						} else {
							if(result.getData().getType() != null && result.getData().getType() == 1){
								ToastUtil.TextToast(VehicleEditActivity.this,"车辆信息不符合该城市查询规则", Toast.LENGTH_SHORT, Gravity.CENTER);
								List<CityVehicle> cvs = result.getData().getRule();
								if(cvs != null && !cvs.isEmpty()){
									mApplication.dbCache.saveCityVehicles(cvs);
									mApplication.updateCities(cvs);
									getFrameAndMotorLength();
									if(frame_number_view.isEnabled()){
										showFrame();
										setFrameLengthFilter();
										strFrame = frame_number_view.getText().toString();
										strFrame = strFrame == null ? "" : strFrame;
										autofillFrame();
										frame_number_notice.setVisibility(View.GONE);
										updateFrameNoticeView();
									}
									if(motor_number_view.isEnabled()){
										showMotor();
										setMotorLengthFilter();
										strMotor = motor_number_view.getText().toString();
										strMotor = strMotor == null ? "" : strMotor;
										autofillMotor();
										motor_number_notice.setVisibility(View.GONE);
										updateMotorNoticeView();
									}
								}
							}
							else if(result.getData().getType() != null && result.getData().getType() == 2){
								ToastUtil.TextToast(VehicleEditActivity.this,"车辆信息错误", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
							else{
								ToastUtil.TextToast(VehicleEditActivity.this,StringUtils.isEmpty(result.getMsg()) ? ("修改车辆信息失败") : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
							}
						}
					} else {
						ToastUtil.TextToast(VehicleEditActivity.this,StringUtils.isEmpty(result.getMsg()) ? ("修改车辆信息失败") : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				} else {
					ToastUtil.TextToast(VehicleEditActivity.this,"网络中断，请稍候重试", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CITY_REQUEST:
			if (resultCode == CITY_RESULT) {
				List<CityVehicle> selectedCity = data.getParcelableArrayListExtra("selectedCity");
				if (selectedCity == null)
					return;
				cityId = null;
				cityName = null;
				for (CityVehicle cityVehicle : selectedCity) {
					if (cityId == null) {
						cityId = "" + cityVehicle.getId();
						cityName = "" + cityVehicle.getName();
					} else {
						cityId += "," + cityVehicle.getId();
						cityName += "," + cityVehicle.getName();
					}
				}
				select_query_city_view.setText(cityName);
				frame_number_view.setEnabled(true);
				motor_number_view.setEnabled(true);
				updateUIWithAuth();
				getFrameAndMotorLength();
				if(frame_number_view.isEnabled()){
					showFrame();
					setFrameLengthFilter();
					strFrame = frame_number_view.getText().toString();
					strFrame = strFrame == null ? "" : strFrame;
					autofillFrame();
					frame_number_notice.setVisibility(View.GONE);
					updateFrameNoticeView();
				}
				if(motor_number_view.isEnabled()){
					showMotor();
					setMotorLengthFilter();
					strMotor = motor_number_view.getText().toString();
					strMotor = strMotor == null ? "" : strMotor;
					autofillMotor();
					motor_number_notice.setVisibility(View.GONE);
					updateMotorNoticeView();
				}
				if(owner_id_view.isEnabled()){
					showOwnerId();
					setOwnerIdLengthFilter();
					owner_id_notice.setVisibility(View.GONE);
					updateOwnerIdNoticeView();
				}
				if(driver_id_view.isEnabled()){
					showDriverId();
					setDriverIdLengthFilter();
					driver_id_notice.setVisibility(View.GONE);
					updateDriverIdNoticeView();
				}
			}
			break;
		case MODEL_REQUEST:
			if (resultCode == MODEL_RESULT) {
				VehicleModel vehicleModel = (VehicleModel) data	.getSerializableExtra("vehicleModel");
				modelId = (vehicleModel.getId() == null ? 0 : vehicleModel.getId());
				modelName = vehicleModel.getName();
				picUrl = vehicleModel.getImage();
				vehicle_brand_module_view.setText(vehicleModel.getName());
			}
			break;
        case Constants.REQUEST_TYPE_DELETE:
            if (resultCode == RESULT_OK)
                delVehicle();
            break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String[] provinces = { "京", "沪", "浙", "苏", "粤", "鲁", "晋", "冀", "豫",
			"川", "渝", "辽", "吉", "黑", "皖", "鄂", "湘", "赣", "闽", "陕", "甘", "宁",
			"蒙", "津", "贵", "云", "桂", "琼", "青", "新", "藏" };

	private void initProvinceWindow() {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.daze_window_province_layout, null);
		provinceWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		provinceWindow.setBackgroundDrawable(new ColorDrawable(-10000));
		provinceWindow.setFocusable(true);
		provinceWindow.setOutsideTouchable(true);
		gridView = (GridView) layout.findViewById(R.id.province_add_item);
		provinceCancel = (TextView) layout.findViewById(R.id.province_add_cancel);
		provinceCancel.setOnClickListener(this);
		gridView.setAdapter(new ArrayAdapter<String>(this, R.layout.daze_simple_text_item, R.id.simple_text_item, provinces));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				String province = (String) listView.getAdapter().getItem(position);
				strPrefix = tvPrefix.getText().toString();
				if (StringUtils.isEmpty(strPrefix) || !province.equals(strPrefix)) {
					strPrefix = province;
					tvPrefix.setText(strPrefix);
					strNum = license_plate_view.getText().toString().toUpperCase();
					if (!StringUtils.isEmpty(strNum)) {
						if ((strNum.charAt(0) >= 'A' && strNum.charAt(0) <= 'Z')) {
							String prefixString = (province + strNum.substring(0, 1)).toUpperCase();
							for (CityVehicle cityVehicle : mApplication.getCities()) {
								if (prefixString.equals(cityVehicle.getPrefix())) {
									if (strNum.length() == 6) {
										if (historyFrameAndMotor != null)
											mApplication.dbCache.saveHistoryFrameAndMotor(historyFrameAndMotor);
										vehicleNumber = province + strNum.toUpperCase();
										historyFrameAndMotor = mApplication.dbCache.getHistoryFrameAndMotor(vehicleNumber);
										if (historyFrameAndMotor == null) {
											historyFrameAndMotor = new HistoryFrameAndMotor();
											historyFrameAndMotor.setVehicleNum(vehicleNumber);
										}
										if (mApplication.getId() != 0)
											getVehicleAuth();
									}
									updateUIWithCityVehicle(cityVehicle);
									break;
								}
							}
						}
					}
				}
				provinceWindow.dismiss();
			}
		});
	}

	private class AllCapTransformationMethod extends ReplacementTransformationMethod {
		protected char[] getOriginal() {
			char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
					'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
					'w', 'x', 'y', 'z' };
			return aa;
		}

		protected char[] getReplacement() {
			char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
					'W', 'X', 'Y', 'Z' };
			return cc;
		}
	}
	
	private void getFrameAndMotorLength(){
		boolean isNeedOwner = false;
		boolean isNeedDriver = false;
		int nVrn = 0;
		frameLen = 0;
		motorLen = 0;
		ownerIdLen = 0;
		driverIdLen = 0;
		if(!StringUtils.isEmpty(cityId )){
			String strTemp = null;
			String[] ids = cityId.split(",");
			boolean isTianjinShow = false;
			for(String id : ids){
				for(CityVehicle cv : mApplication.getCities()){
					if(cv.getId().longValue()== Long.parseLong(id)){
						int frameNumLen = cv.getFrameNumLen();
						if (frameNumLen == -1) {
							frameLen = 99;
						} else if (frameNumLen > frameLen) {
							frameLen = frameNumLen;
						}
						int motorNumLen = cv.getMotorNumLen();
						if (motorNumLen == -1) {
							motorLen = 99;
						} else if (motorNumLen > motorLen) {
							motorLen = motorNumLen;
						}
						int ownerIdNoLen = cv.getOwnerIdNoLen() != null ? cv.getOwnerIdNoLen() : 0;
						if (ownerIdNoLen == -1) {
							ownerIdLen = 99;
						} else if (ownerIdNoLen > ownerIdLen) {
							ownerIdLen = ownerIdNoLen;
						}
						int drivingLicenseNoLen = cv.getDrivingLicenseNoLen() != null ? cv.getDrivingLicenseNoLen() : 0;
						if (drivingLicenseNoLen == -1) {
							driverIdLen = 99;
						} else if (drivingLicenseNoLen > driverIdLen) {
							driverIdLen = drivingLicenseNoLen;
						}
						if(cv.getOwner() != null && cv.getOwner())
							isNeedOwner = true;
						if (cv.isDrivingLicenseName() != null && cv.isDrivingLicenseName())
							isNeedDriver = true;
						if(cv.getMotorvehiclenumLen() != null){
							if(cv.getMotorvehiclenumLen().intValue() == -1)
								nVrn = 99;
							else if(cv.getMotorvehiclenumLen().intValue() > nVrn)
								nVrn = cv.getMotorvehiclenumLen().intValue();
						}
						if(cv.getAccountLen() != null && cv.getAccountLen() != 0){
							isTianjinShow = true;
							if(strTemp == null)
								strTemp = cv.getName();
							else
								strTemp += ("," + cv.getName());
						}
						break;
					}
				}
			}
			if(isTianjinShow)
				tianjinView.setVisibility(View.VISIBLE);
			else
				tianjinView.setVisibility(View.GONE);
			if(cityName.contains("天津"))
				register_account_view.setVisibility(View.VISIBLE);
			else
				register_account_view.setVisibility(View.GONE);
			if(strTemp != null){
				tianjinLabel.setText(strTemp + "违章查询需要官网账户信息");
			}
		}
		if(isNeedOwner)
			owner_layout.setVisibility(View.VISIBLE);
		else
			owner_layout.setVisibility(View.GONE);
		if(isNeedDriver)
			driver_name_layout.setVisibility(View.VISIBLE);
		else
			driver_name_layout.setVisibility(View.GONE);
		if(nVrn != 0)
			vehicleRegCerNo_layout.setVisibility(View.VISIBLE);
		else
			vehicleRegCerNo_layout.setVisibility(View.GONE);
	}
	
	private void updateFrameNoticeView(){
		String frameString = frame_number_view.getText().toString();
		if(StringUtils.isEmpty(frameString)){
			updateNotice(frame_number_notice, false);
			if(frame_number_layout.getVisibility() == View.VISIBLE)
				frame_number_notice.setVisibility(View.VISIBLE);
		}
		else if(frameLen == frameString.length()){
			updateNotice(frame_number_notice, true);
			frame_number_notice.setVisibility(View.GONE);
		}
		else{
			if (frameLen == 99) {
				frame_number_notice.setText("请输入完整车架号！");
				if(frameString.length() == 17){
					updateNotice(frame_number_notice, true);
					frame_number_notice.setVisibility(View.GONE);
				}
				else{
					updateNotice(frame_number_notice, false);
					if (frame_number_layout.getVisibility() == View.VISIBLE)
						frame_number_notice.setVisibility(View.VISIBLE);
				}
			} else {
				frame_number_notice.setText("请输入车架号后" + frameLen + "位！");
				if(frameString.length() == frameLen){
					updateNotice(frame_number_notice, true);
					frame_number_notice.setVisibility(View.GONE);
				}
				else{
					updateNotice(frame_number_notice, false);
					if (frame_number_layout.getVisibility() == View.VISIBLE)
						frame_number_notice.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	private void updateMotorNoticeView(){
		String motorString = motor_number_view.getText().toString();
		if(StringUtils.isEmpty(motorString)){
			updateNotice(motor_number_notice, false);
			if (motor_number_layout.getVisibility() == View.VISIBLE)
				motor_number_notice.setVisibility(View.VISIBLE);
		}
		else if(motorLen == motorString.length()){
			updateNotice(motor_number_notice, true);
			motor_number_notice.setVisibility(View.GONE);
		}
		else{
			if (motorLen == 99) {
				motor_number_notice.setText("请输入完整发动机号！");
				updateNotice(motor_number_notice, true);
				motor_number_notice.setVisibility(View.GONE);
			} else {
				motor_number_notice.setText("请输入发动机号后" + motorLen + "位！");
				updateNotice(motor_number_notice, false);
				if (motor_number_layout.getVisibility() == View.VISIBLE)
					motor_number_notice.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateOwnerIdNoticeView(){
		String ownerIdString = owner_id_view.getText().toString();
		if(StringUtils.isEmpty(ownerIdString)){
			updateNotice(owner_id_notice, false);
			if(owner_id_layout.getVisibility() == View.VISIBLE)
				owner_id_notice.setVisibility(View.VISIBLE);
		}
		else if(ownerIdLen == ownerIdString.length()){
			updateNotice(owner_id_notice, true);
			owner_id_notice.setVisibility(View.GONE);
		}
		else{
			if (ownerIdLen == 99) {
				owner_id_notice.setText("请输入完整车主身份证号！");
				updateNotice(owner_id_notice, true);
				owner_id_notice.setVisibility(View.GONE);
			} else {
				owner_id_notice.setText("请输入车主身份证号后" + ownerIdLen + "位！");
				updateNotice(owner_id_notice, false);
				if (owner_id_layout.getVisibility() == View.VISIBLE)
					owner_id_notice.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateDriverIdNoticeView(){
		String driverIdString = driver_id_view.getText().toString();
		if(StringUtils.isEmpty(driverIdString)){
			updateNotice(driver_id_notice, false);
			if(driver_id_layout.getVisibility() == View.VISIBLE)
				driver_id_notice.setVisibility(View.VISIBLE);
		}
		else if(driverIdLen == driverIdString.length()){
			updateNotice(driver_id_notice, true);
			driver_id_notice.setVisibility(View.GONE);
		}
		else{
			if (driverIdLen == 99) {
				driver_id_notice.setText("请输入完整驾驶证号！");
				updateNotice(driver_id_notice, true);
				driver_id_notice.setVisibility(View.GONE);
			} else {
				driver_id_notice.setText("请输入驾驶证号后" + driverIdLen + "位！");
				updateNotice(driver_id_notice, false);
				if (driver_id_layout.getVisibility() == View.VISIBLE)
					driver_id_notice.setVisibility(View.VISIBLE);
			}
		}
	}

	private void updateUIWithAuth(){
		if(vehicleAuth != null && vehicleAuth.getStatus() != null && vehicleAuth.getStatus() == 2
				&& vehicleAuth.getBelong() != null && vehicleAuth.getBelong()){
			String vafn = vehicleAuth.getFrameNum();
			if(!StringUtils.isEmpty(vafn)){
				error_correction_layout.setVisibility(View.VISIBLE);
				frame_number_layout.setVisibility(View.VISIBLE);
				frame_number_view.setEnabled(false);
				frame_number_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
				frame_number_view.setText(vafn);
				frame_number_notice.setVisibility(View.GONE);
			}
			String vamn = vehicleAuth.getMotorNum();
			if(!StringUtils.isEmpty(vamn)){
				error_correction_layout.setVisibility(View.VISIBLE);
				motor_number_layout.setVisibility(View.VISIBLE);
				motor_number_view.setEnabled(false);
				motor_number_view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
				motor_number_view.setText(vamn);
				motor_number_notice.setVisibility(View.GONE);
			}
			if(!StringUtils.isEmpty(vehicleAuth.getAuthDatetime())){
				register_date_view.setText(vehicleAuth.getAuthDatetime());
			}
		}
	}
	
	private void autofillFrame(){
		String historyFrame = null;
		if(historyFrameAndMotor != null){
			historyFrame = historyFrameAndMotor.getHistoryFrame();
		}
		int lf = strFrame.length();
		if(frameLen == lf)
			frame_number_view.setText(strFrame);
		else if(frameLen > lf){
			if(frameLen == 99){
				if(lf == 17){
					frame_number_view.setText(strFrame);
				}
				else{
					if(!StringUtils.isEmpty(historyFrame)){
						int mlf = historyFrame.length();
						if(mlf == 17){
							frame_number_view.setText(historyFrame);
						}
						else{
							frame_number_view.setText("");
						}
					}
					else{
						frame_number_view.setText("");
					}
				}
			}
			else{
				if(!StringUtils.isEmpty(historyFrame)){
					int mlf = historyFrame.length();
					if(mlf >= frameLen){
						frame_number_view.setText(historyFrame.substring(mlf - frameLen));
					}
					else{
						frame_number_view.setText("");
					}
				}
				else{
					frame_number_view.setText("");
				}
			}
		}
		else{
			frame_number_view.setText(strFrame.substring(lf - frameLen));
		}
	}
	
	private void autofillMotor(){
		String historyMotor = null;
		if(historyFrameAndMotor != null){
			historyMotor = historyFrameAndMotor.getHistoryMotor();
		}
		int lm = strMotor.length();
		if(motorLen == lm){
			motor_number_view.setText(strMotor);
		}
		else if(motorLen > lm){
			if(motorLen == 99){
				if(!StringUtils.isEmpty(historyMotor)){
					int mlm = historyMotor.length();
					if(mlm > lm){
						motor_number_view.setText(historyMotor);
					}
					else{
						motor_number_view.setText(strMotor);
					}
				}
				else{
					motor_number_view.setText(strMotor);
				}
			}
			else{
				if(!StringUtils.isEmpty(historyMotor)){
					int mlm = historyMotor.length();
					if(mlm >= motorLen){
						motor_number_view.setText(historyMotor.substring(mlm - motorLen));
					}
					else{
						motor_number_view.setText(mlm > lm ? historyMotor : strMotor);
					}
				}
				else{
					motor_number_view.setText(strMotor);
				}
			}
		}
		else{
			motor_number_view.setText(strMotor.substring(lm - motorLen));
		}
	}

	private void updateNotice(TextView tv, boolean isCorrect){
		if(isCorrect){
			tv.setBackgroundColor(getResources().getColor(R.color.daze_light_green50));
			tv.setTextColor(getResources().getColor(R.color.daze_light_green500));
		}else{
			tv.setBackgroundColor(getResources().getColor(R.color.daze_red50));
			tv.setTextColor(getResources().getColor(R.color.daze_red500));
		}
	}
	
	@Override
	protected void onDestroy()
	{
		if(provinceWindow != null){
			provinceWindow.dismiss();
			provinceWindow = null;
		}
		if(alertDialog != null && alertDialog.isShowing()){
			alertDialog.dismiss();
			alertDialog = null;
		}
		super.onDestroy();
	}
	
	private void submitError(){
		if(errorStringList != null && !errorStringList.isEmpty())
			new AsyncTask<Void, Void, Response>(){
				@Override
				protected Response doInBackground(Void... params) {
					VehicleRoportRequest request = new VehicleRoportRequest();
					String errorKey =null;
					for(String strError : errorStringList){
						String strKetTemp = null;
						if(strError.equals("车架号错误")){
							strKetTemp = "1";
						}
						else if(strError.equals("发动机号错误")){
							strKetTemp = "2";
						}
						else if(strError.equals("注册时间错误")){
							strKetTemp = "3";
						}
						if(strKetTemp != null){
							if(errorKey == null)
								errorKey = strKetTemp;
							else
								errorKey += ("," + strKetTemp);
						}
					}
					request.setParams(errorKey, null, vehicleNumber, "" + mApplication.getUserId());
					try {
						return mApplication.client.execute(request);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}.execute();
	}
	
	class ErrorAdapter extends BaseAdapter{
		LayoutInflater inflater;
		@Override
		public int getCount() {
			if(errorStringList == null || errorStringList.isEmpty())
				return 0;
			return errorStringList.size();
		}

		@Override
		public Object getItem(int position) {
			if(errorStringList == null || errorStringList.isEmpty())
				return null;
			return errorStringList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Map<String, Object> holder = null;
			if(convertView == null){
				holder = new HashMap<String, Object>();
				if(inflater == null)
					inflater = LayoutInflater.from(VehicleEditActivity.this);
				convertView = inflater.inflate(R.layout.daze_list_item6, parent, false);
				TextView textError = (TextView) convertView.findViewById(R.id.textError);
				ImageView imageSelect = (ImageView) convertView.findViewById(R.id.imageSelect);
				holder.put("textError", textError);
				holder.put("imageSelect", imageSelect);
				convertView.setTag(holder);
			}
			else
				holder = (Map<String, Object>) convertView.getTag();
			final TextView textError = (TextView) holder.get("textError");
			final ImageView imageSelect = (ImageView) holder.get("imageSelect");
			String errorTemp = errorStringList.get(position);
			textError.setTag(position);
			imageSelect.setTag(position);
			textError.setText(errorTemp);
			if(selectErrorStringList.contains(errorTemp)){
				textError.setBackgroundColor(getResources().getColor(R.color.daze_white_smoke5));
				textError.setTextColor(getResources().getColor(R.color.daze_darkgrey9));
				imageSelect.setVisibility(View.VISIBLE);
			}
			else{
				textError.setBackgroundColor(getResources().getColor(R.color.daze_white));
				textError.setTextColor(getResources().getColor(R.color.daze_black2));
				imageSelect.setVisibility(View.GONE);
			}
			textError.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					int nTemp = (Integer) textError.getTag();
					if(selectErrorStringList.contains(errorStringList.get(nTemp)))
						selectErrorStringList.remove(errorStringList.get(nTemp));
					else
						selectErrorStringList.add(errorStringList.get(nTemp));
					errorAdapter.notifyDataSetChanged();
					if(selectErrorStringList.isEmpty())
						tvSubmitError.setEnabled(false);
					else
						tvSubmitError.setEnabled(true);
				}
			});
			imageSelect.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					int nTemp = (Integer) imageSelect.getTag();
					if(selectErrorStringList.contains(errorStringList.get(nTemp)))
						selectErrorStringList.remove(errorStringList.get(nTemp));
					errorAdapter.notifyDataSetChanged();
					if(selectErrorStringList.isEmpty())
						tvSubmitError.setEnabled(false);
					else
						tvSubmitError.setEnabled(true);
				}
			});
			return convertView;
		}
		
	}
	
	class VehicleTypeAdapter extends AbstractWheelTextAdapter{

		public VehicleTypeAdapter(Context context) {
			super(context, R.layout.daze_cash_select_item, NO_RESOURCE);
			setItemTextResource(R.id.tvCashInfo);
		}

		@Override
		public int getItemsCount() {
			if(vehicleTypes == null)
				return 0;
			return vehicleTypes.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			if(vehicleTypes == null)
				return null;
			CommonDictionary cd = vehicleTypes.get(index);
			return cd.getValue();
		}
		
		@Override
	    public View getItem(int index, View convertView, ViewGroup parent) {
			View view = super.getItem(index, convertView, parent);
			if(view != null){
				TextView tvCashDate = (TextView) view.findViewById(R.id.tvCashDate);
				CommonDictionary cd = vehicleTypes.get(index);
				tvCashDate.setVisibility(View.GONE);
			}
	        return view;
	    }
	}
	
	private void addFocusChangeListenerToEditText(final EditText view){
		view.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
                    if(view.getText().toString() != null)
                        view.setSelection(view.getText().toString().length());
				}
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(historyFrameAndMotor != null)
				mApplication.dbCache.saveHistoryFrameAndMotor(historyFrameAndMotor);
			setResult(KplusConstants.FLAG_CANCEL_ADD);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

    private void delVehicle() {
		EventAnalysisUtil.onEvent(this, "delCar_Btn", "删除车辆", null);
		showloading(true);
        JSONObject prop = new JSONObject();
        try {
            prop.put("vehicle_num", vehicle.getVehicleNum());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        new AsyncTask<Void, Void, GetResultResponse>() {
            protected GetResultResponse doInBackground(Void... params) {
                VehicleDelRequest request = new VehicleDelRequest();
                request.setParams(mApplication.getUserId(),mApplication.getId(), vehicle.getVehicleNum());
                try {
                    return mApplication.client.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(GetResultResponse result) {
                showloading(false);
                if (result != null) {
                    if (result.getCode() != null && result.getCode() == 0) {
                        if (result.getData().getResult()) {
                            RemindManager.getInstance(VehicleEditActivity.this).cancel(vehicle.getVehicleNum());
							mApplication.dbCache.deleteRemindNianjian(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteRemindChexian(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteRemindBaoyang(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteRemindChedai(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteRemindCustom(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteBaoyangRecord(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteRemindInfo(vehicle.getVehicleNum());
							RemindRestrict restrict = mApplication.dbCache.getRemindRestrict(vehicle.getVehicleNum());
							if (restrict != null) {
								new RestrictDeleteTask(mApplication).execute(restrict.getId());
								mApplication.dbCache.deleteRemindRestrict(vehicle.getVehicleNum());
							}
                            new RemindSyncAllTask(mApplication).execute();
                            mApplication.dbCache.deleteVehicle(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteAgainstRecord(vehicle.getVehicleNum());
                            mApplication.dbCache.deleteVehicleAuthByVehicleNumber(vehicle.getVehicleNum());
							mApplication.dbCache.deleteJiazhaoAgainst(vehicle.getVehicleNum());
                            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
                            JSONObject prop = new JSONObject();
                            try {
                                prop.put("vehicle_num", vehicle.getVehicleNum());
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            Toast.makeText(VehicleEditActivity.this,
                                    "删除车辆成功", Toast.LENGTH_SHORT).show();
                            setResult(Constants.RESULT_TYPE_REMOVED);
                            finish();
                        } else {
                            Toast.makeText(VehicleEditActivity.this,
                                    "删除车辆失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(VehicleEditActivity.this,
                                result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VehicleEditActivity.this, "网络中断，请稍候重试",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void updateNianjian(){
        if (vehicle.getNianjianDate() != null && !"".equals(vehicle.getNianjianDate())){
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
                RemindManager.getInstance(this).set(Constants.REQUEST_TYPE_NIANJIAN, nianjian.getVehicleNum(), 0, null);
                if (mApplication.getId() != 0)
                    new RemindSyncTask(mApplication).execute(Constants.REQUEST_TYPE_NIANJIAN);
            }
        }
    }
}
