package com.kplus.car.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.comm.TaskInfo;
import com.kplus.car.model.AgainstRecord;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.Jiazhao;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.VehicleModel;
import com.kplus.car.model.response.GetAgainstRecordListResponse;
import com.kplus.car.model.response.request.SetMessageRequest;
import com.kplus.car.receiver.VehicleAddRemindReceiver;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.antistatic.spinnerwheel.AbstractWheel;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelChangedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.OnWheelClickedListener;
import com.kplus.car.widget.antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlertDialogActivity extends BaseActivity implements OnClickListener {
	private TextView alertTitle;
//	private ImageView closeAlert;
	private TextView messageView;
	private View vehicle_mode_view;
	private TextView tvVehicleBrand;
	private EditText etVehicleMode;
	private Button alertConfirmButton, alertLeftButton, alertRightButton;
	private AbstractWheel remindCshView;
	private View verify_code_view;
	private TextView tvVerifyCode;
	private EditText etVerifyCode;
	private View tvBlank;
	private int alertType;
	private int subAlertType;
	private List<VehicleModel> listVehicleModes;
	private long brandId;
	private String vehicleNumber;
	private String sessionId;
	private int[] reminders;
	private int currentIndex;
	private RemindCashAdapter remidCashAdapter;
	private AlarmManager alarmManager;
	private boolean add = false;
	private boolean verifyCodeError = false;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.daze_alert_dialog);
		alertTitle = (TextView) findViewById(R.id.alertTitle);
//		closeAlert = (ImageView) findViewById(R.id.closeAlert);
		messageView = (TextView) findViewById(R.id.messageView);
		vehicle_mode_view = findViewById(R.id.vehicle_mode_view);
		tvVehicleBrand = (TextView) findViewById(R.id.tvVehicleBrand);
		etVehicleMode = (EditText) findViewById(R.id.etVehicleMode);
		alertConfirmButton = (Button) findViewById(R.id.alertConfirmButton);
		alertLeftButton = (Button) findViewById(R.id.alertLeftButton);
		alertRightButton = (Button) findViewById(R.id.alertRightButton);
		verify_code_view = findViewById(R.id.verify_code_view);
		tvVerifyCode = (TextView) findViewById(R.id.tvVerifyCode);
		etVerifyCode = (EditText) findViewById(R.id.etVerifyCode);
		remindCshView = (AbstractWheel) findViewById(R.id.remindCshView);
		tvBlank = findViewById(R.id.tvBlank);
		if(android.os.Build.VERSION.SDK_INT >= 11)
			setFinishOnTouchOutside(false);
	}

	@Override
	protected void loadData() {
		Intent intent = getIntent();
		readParams(intent);
	}
	
	private void readParams(Intent intent){
		add = intent.getBooleanExtra("add", false);
		verifyCodeError = intent.getBooleanExtra("verifyCodeError", false);
		alertType = intent.getIntExtra("alertType", KplusConstants.ALERT_VEHICLE_MODE_SELF_DEFINE);
		subAlertType = intent.getIntExtra("subAlertType", 0);
		vehicleNumber = intent.getStringExtra("vehicleNumber");
		switch(alertType){
		case KplusConstants.ALERT_VEHICLE_MODE_SELF_DEFINE:
			alertTitle.setText("添加您的车型");
			messageView.setVisibility(View.GONE);
			tvBlank.setVisibility(View.GONE);
			vehicle_mode_view.setVisibility(View.VISIBLE);
			verify_code_view.setVisibility(View.GONE);
			remindCshView.setVisibility(View.GONE);
			brandId = intent.getLongExtra("brandId", 0);
			tvVehicleBrand.setText(intent.getStringExtra("vehicleBrand"));
			alertConfirmButton.setVisibility(View.GONE);
			alertLeftButton.setText("取消");
			alertRightButton.setText("添加车型");
			if(intent.hasExtra("modeName")){
				String modeName = intent.getStringExtra("modeName");
				if(!StringUtils.isEmpty(modeName))
					etVehicleMode.setText(modeName);
			}
			break;
		case KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE:
			if(intent.hasExtra("title"))
				alertTitle.setText(intent.getStringExtra("title"));
			else
				alertTitle.setText("提示");
			messageView.setVisibility(View.VISIBLE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.GONE);
			if(subAlertType == KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_WHERT_TO_REMIND){
				tvBlank.setVisibility(View.GONE);
				remindCshView.setVisibility(View.VISIBLE);
				reminders = new int[]{15, 30, 60, 180, 360};
				remidCashAdapter = new RemindCashAdapter(AlertDialogActivity.this);
				remindCshView.setViewAdapter(remidCashAdapter);
				remindCshView.setVisibleItems(3);
				remindCshView.addChangingListener(new OnWheelChangedListener() {			
					@Override
					public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
						currentIndex = newValue;
					}
				});
				remindCshView.addClickingListener(new OnWheelClickedListener() {			
					@Override
					public void onItemClicked(AbstractWheel wheel, int itemIndex) {
						currentIndex = itemIndex;
						remindCshView.setCurrentItem(itemIndex);
					}
				});
			}
            else if (subAlertType == KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_VIEW_AGAINST_RESULT){
                tvBlank.setVisibility(View.VISIBLE);
                remindCshView.setVisibility(View.GONE);
                Intent it = new Intent("com.kplus.car.weizhang.finish");
                it.putExtra("vehicleNum", vehicleNumber);
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);
            }
			else{
				tvBlank.setVisibility(View.VISIBLE);
				remindCshView.setVisibility(View.GONE);
			}
			messageView.setText(intent.getStringExtra("message"));
			messageView.setVisibility(View.VISIBLE);
			alertConfirmButton.setVisibility(View.GONE);
			alertLeftButton.setVisibility(View.VISIBLE);
			alertRightButton.setVisibility(View.VISIBLE);
			if(intent.hasExtra("leftButtonText"))
				alertLeftButton.setText(intent.getStringExtra("leftButtonText"));
			else
				alertLeftButton.setText("取消");
			if(intent.hasExtra("rightButtonText"))
				alertRightButton.setText(intent.getStringExtra("rightButtonText"));
			else
				alertRightButton.setText("确定");
			break;
		case KplusConstants.ALERT_ONLY_SHOW_MESSAGE:
			alertTitle.setText("提示");
			messageView.setVisibility(View.VISIBLE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.GONE);
			remindCshView.setVisibility(View.GONE);
			tvBlank.setVisibility(View.VISIBLE);
			messageView.setText(intent.getStringExtra("message"));
			alertConfirmButton.setVisibility(View.VISIBLE);
			if(intent.hasExtra("ConfirmButtonText") && !StringUtils.isEmpty(intent.getStringExtra("ConfirmButtonText")))
				alertConfirmButton.setText(intent.getStringExtra("ConfirmButtonText"));
			else
				alertConfirmButton.setText("确定");
			alertLeftButton.setVisibility(View.GONE);
			alertRightButton.setVisibility(View.GONE);
			break;
		case KplusConstants.ALERT_INPUT_VERIFY_CODE:
			alertTitle.setText("验证码");
			tvVerifyCode.setText("验证码");
			etVerifyCode.setHint("验证码已经发送到您的注册手机，请注意查收");
			messageView.setVisibility(View.GONE);
			tvBlank.setVisibility(View.GONE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.VISIBLE);
			remindCshView.setVisibility(View.GONE);
			alertConfirmButton.setVisibility(View.GONE);
			alertLeftButton.setVisibility(View.VISIBLE);
			alertRightButton.setVisibility(View.VISIBLE);
			alertLeftButton.setText("取消");
			alertRightButton.setText("确定");
			sessionId = intent.getStringExtra("sessionId");
			break;
		case KplusConstants.ALERT_INPUT_VEHICLE_OWNER_NAME:
			alertTitle.setText("请输入车主姓名");
			tvVerifyCode.setText("车主姓名");
			etVerifyCode.setHint("请输入车主姓名");
			messageView.setVisibility(View.GONE);
			tvBlank.setVisibility(View.GONE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.VISIBLE);
			remindCshView.setVisibility(View.GONE);
			alertConfirmButton.setVisibility(View.GONE);
			alertLeftButton.setVisibility(View.VISIBLE);
			alertRightButton.setVisibility(View.VISIBLE);
			alertLeftButton.setText("取消");
			alertRightButton.setText("确定");
			break;
		case KplusConstants.ALERT_INPUT_REMARK:
			alertTitle.setText("修改备注");
			tvVerifyCode.setVisibility(View.GONE);
			etVerifyCode.setHint("请输入备注");
			etVerifyCode.setText(intent.getStringExtra("message"));
			messageView.setVisibility(View.GONE);
			tvBlank.setVisibility(View.GONE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.VISIBLE);
			remindCshView.setVisibility(View.GONE);
			alertConfirmButton.setVisibility(View.GONE);
			alertLeftButton.setVisibility(View.VISIBLE);
			alertRightButton.setVisibility(View.VISIBLE);
			alertLeftButton.setText("取消");
			alertRightButton.setText("确定");
			break;
		case KplusConstants.ALERT_INPUT_SHOP_NAME:
			alertTitle.setText("添加保养店");
			tvVerifyCode.setVisibility(View.GONE);
			etVerifyCode.setHint("请输入保养店名");
			messageView.setVisibility(View.GONE);
			tvBlank.setVisibility(View.GONE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.VISIBLE);
			remindCshView.setVisibility(View.GONE);
			alertConfirmButton.setVisibility(View.GONE);
			alertLeftButton.setVisibility(View.VISIBLE);
			alertRightButton.setVisibility(View.VISIBLE);
			alertLeftButton.setText("取消");
			alertRightButton.setText("确定");
			break;
		case KplusConstants.ALERT_PROMO_CODE_EXCHANGE:
			alertTitle.setText("兑换成功");
			messageView.setText(intent.getStringExtra("message"));
			messageView.setVisibility(View.VISIBLE);
			tvBlank.setVisibility(View.VISIBLE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.GONE);
			remindCshView.setVisibility(View.GONE);
			alertConfirmButton.setVisibility(View.GONE);
			alertLeftButton.setVisibility(View.VISIBLE);
			alertRightButton.setVisibility(View.VISIBLE);
			alertLeftButton.setText("关闭");
			alertRightButton.setText("前往查看");
			break;
		case KplusConstants.ALERT_JIAZHAO_RESULT:
			messageView.setGravity(Gravity.LEFT);
			messageView.setTextColor(getResources().getColor(R.color.daze_black2));
			messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			messageView.setText(intent.getStringExtra("message"));
			messageView.setVisibility(View.VISIBLE);
			tvBlank.setVisibility(View.VISIBLE);
			vehicle_mode_view.setVisibility(View.GONE);
			verify_code_view.setVisibility(View.GONE);
			remindCshView.setVisibility(View.GONE);
			if (subAlertType == KplusConstants.ALERT_JIAZHAO_RESULT_SUBTYPE_JIAZHAO_SUCCESS){
				alertTitle.setText("查询成功");
				alertConfirmButton.setVisibility(View.VISIBLE);
				alertConfirmButton.setText("关闭");
				alertLeftButton.setVisibility(View.GONE);
				alertRightButton.setVisibility(View.GONE);
			}
			else if (subAlertType == KplusConstants.ALERT_JIAZHAO_RESULT_SUBTYPE_JIAZHAO_FAILED){
				alertTitle.setText("查询失败");
				alertConfirmButton.setVisibility(View.GONE);
				alertLeftButton.setVisibility(View.VISIBLE);
				alertLeftButton.setText("关闭");
				alertRightButton.setVisibility(View.VISIBLE);
				alertRightButton.setText("去修改");
			}
			break;
		default:
			break;
		}
		if(intent.hasExtra("showTime")){
			int showTime = intent.getIntExtra("showTime", 0);
			if(showTime > 0){
				new Handler().postDelayed(new Runnable() {					
					@Override
					public void run() {
						finish();
					}
				}, showTime*1000);
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		readParams(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void setListener() {
//		closeAlert.setOnClickListener(this);
		alertConfirmButton.setOnClickListener(this);
		alertLeftButton.setOnClickListener(this);
		alertRightButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = getIntent();
		switch (arg0.getId()) {
//		case R.id.closeAlert:
//			setResult(RESULT_CANCELED);
//			finish();
//			break;
		case R.id.alertConfirmButton:
			finish();
			break;
		case R.id.alertLeftButton:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.alertRightButton:
			switch(alertType){
			case KplusConstants.ALERT_VEHICLE_MODE_SELF_DEFINE:
				String strVehicleMode = etVehicleMode.getText().toString();
				if(StringUtils.isEmpty(strVehicleMode))
					ToastUtil.TextToast(AlertDialogActivity.this, "请输入您的车型", Toast.LENGTH_SHORT, Gravity.CENTER);
				else{
					if(listVehicleModes == null)
						listVehicleModes = mApplication.dbCache.getSelfDefineModesByBrandId(brandId);
					if(listVehicleModes == null || listVehicleModes.isEmpty()){
						intent.putExtra("modeName", strVehicleMode);
						setResult(RESULT_OK, intent);
						finish();
					}
					else{
						boolean isExit = false;
						for(VehicleModel vm : listVehicleModes){
							if(vm.getName().equals(strVehicleMode)){
								isExit = true;
								ToastUtil.TextToast(AlertDialogActivity.this, "该车型已经存在", Toast.LENGTH_SHORT, Gravity.CENTER);
								break;
							}
						}
						if(!isExit){
							intent.putExtra("modeName", strVehicleMode);
							setResult(RESULT_OK, intent);
							finish();
						}
					}
				}
				break;
			case KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE:
				if(subAlertType == KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_VIEW_AGAINST_RESULT){
					EventAnalysisUtil.onEvent(this, "click_details_recordResult", "违章查询完成之查看结果", null);
					intent.setClass(AlertDialogActivity.this, VehicleDetailActivity.class);
					intent.putExtra("vehicleNumber", vehicleNumber);
					intent.putExtra("add", add);
					intent.putExtra("verifyCodeError", verifyCodeError);
					startActivity(intent);
					finish();
				}
				else if(subAlertType == KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_ADD_VEHICLE){
					EventAnalysisUtil.onEvent(AlertDialogActivity.this, "addCar", "添加车辆", null);
					intent.setClass(AlertDialogActivity.this, VehicleAddNewActivity.class);
					startActivity(intent);
					finish();
				}
				else if(subAlertType == KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_WHERT_TO_REMIND){
					intent.setClass(AlertDialogActivity.this, VehicleAddRemindReceiver.class);
					PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
					if(alarmManager == null)
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + reminders[currentIndex] * 60 * 1000, pi);
					sp.edit().putBoolean("hasRemind", true).commit();
					finish();
				}
				if(subAlertType == KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_WHERT_TO_RESCUE){
					intent.setClass(AlertDialogActivity.this, EmergencyDetailActivity.class);
					intent.putExtra("vehicleNumber", vehicleNumber);
					startActivity(intent);
					finish();
				}
				else{
					setResult(RESULT_OK);
					finish();
				}
				break;
			case KplusConstants.ALERT_INPUT_VERIFY_CODE:
				if(StringUtils.isEmpty(etVerifyCode.getText().toString())){
					ToastUtil.TextToast(AlertDialogActivity.this, "请输入验证码", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				setMessage();
				break;
			case KplusConstants.ALERT_INPUT_VEHICLE_OWNER_NAME:
				if(StringUtils.isEmpty(etVerifyCode.getText().toString())){
					ToastUtil.TextToast(AlertDialogActivity.this, "请输入车主姓名", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				intent.putExtra("ownerName", etVerifyCode.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
				break;
			case KplusConstants.ALERT_INPUT_REMARK:
				if(StringUtils.isEmpty(etVerifyCode.getText().toString())){
					ToastUtil.TextToast(AlertDialogActivity.this, "请输入备注", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				intent.putExtra("remark", etVerifyCode.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
				break;
			case KplusConstants.ALERT_INPUT_SHOP_NAME:
				if(StringUtils.isEmpty(etVerifyCode.getText().toString())){
					ToastUtil.TextToast(AlertDialogActivity.this, "请输入保养店名", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				intent.putExtra("shopName", etVerifyCode.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
				break;
			case KplusConstants.ALERT_JIAZHAO_RESULT:
				Jiazhao jiazhao = (Jiazhao) intent.getSerializableExtra("jiazhao");
				intent = new Intent(this, JiazhaoEditActivity.class);
				intent.putExtra("jiazhao", jiazhao);
				startActivity(intent);
				finish();
				break;
			case KplusConstants.ALERT_PROMO_CODE_EXCHANGE:
				int count = intent.getIntExtra("count", 0);
				intent = new Intent(this, CouponActivity.class);
				intent.putExtra("count", count);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setMessage(){
		new AsyncTask<Void, Void, GetAgainstRecordListResponse>(){
			@Override
			protected GetAgainstRecordListResponse doInBackground(Void... params) {
				SetMessageRequest request = new SetMessageRequest();
				request.setParams(sessionId, etVerifyCode.getText().toString());
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					if(KplusConstants.isDebug){
						System.out.println("setMessage===>" + e.toString());
					}
				}
				return null;
			}
			protected void onPostExecute(GetAgainstRecordListResponse result) {
				if(KplusConstants.isDebug){
					if(result != null && result.getBody() != null)
						System.out.println("setMessage===>result:" + result.getBody());
				}
//				if(!mApplication.containsTask(vehicleNumber)){
//					return;
//				}
				if(!StringUtils.isEmpty(vehicleNumber)){
					UserVehicle vehicle = mApplication.dbCache.getVehicle(vehicleNumber);
					if(vehicle != null){
						if(mApplication.containsTask(vehicleNumber))
						{
							TaskInfo ti = mApplication.getTask(vehicleNumber);
							if(ti != null)
								ti.setHasEnterVerifyCode(true);
							if(result != null && result.getCode() != null && result.getCode() == 0 && result.getData() != null && result.getData().getType() != null){
								String dataType = result.getData().getType();
								boolean add = false;
								if(dataType.contains("0")){
									add = true;
									mApplication.removeTask(vehicleNumber);
									vehicle.setUpdateTime("" + result.getPostDateTime());
									vehicle.setReturnTime(System.currentTimeMillis());
									List<AgainstRecord> listResult = result.getData().getList();
									if(listResult != null && !listResult.isEmpty()){
										if(result.getData().getResultType() != null && result.getData().getResultType() == 1){
											mApplication.dbCache.deleteImageAgainstRecord(vehicleNumber);
											for(AgainstRecord ar : listResult)
												ar.setResultType(1);
										}
										vehicle.setNewRecordNumber(listResult.size());
										mApplication.dbCache.saveAgainstRecords(listResult);
										mApplication.dbCache.updateVehicle(vehicle);
									}
								}
								if(dataType.contains("1")){
									mApplication.removeTask(vehicleNumber);
									List<CityVehicle> cityVehicles = result.getData().getRule();
									mApplication.dbCache.saveCityVehicles(cityVehicles);
									mApplication.updateCities(cityVehicles);
									vehicle.setReturnTime(System.currentTimeMillis());
									vehicle.setHasRuleError(true);
									mApplication.dbCache.updateVehicle(vehicle);
								}
								if(dataType.contains("2")){
									mApplication.removeTask(vehicleNumber);
									vehicle.setReturnTime(System.currentTimeMillis());
									vehicle.setHasParamError(true);
									mApplication.dbCache.updateVehicle(vehicle);
								}
								if(dataType.contains("3")){
									mApplication.removeTask(vehicleNumber);
									vehicle.setReturnTime(System.currentTimeMillis());
									mApplication.dbCache.deleteAgainstRecord(vehicle.getVehicleNum());
									String strCity = result.getData().getCity();
									strCity = strCity.replaceAll(" ", "");
									strCity = strCity.replaceAll("，", ",");
									strCity = strCity.replaceAll("：", "\\:");
									strCity = strCity.replace("{", "");
									strCity = strCity.replace("}", "");
									strCity = strCity.replaceAll("=", "\\:");
//									vehicle.setCityUnValid(strCity);
									mApplication.dbCache.updateVehicle(vehicle);
									String cityIds = null;
									try{
										String[] cityParams = strCity.split(",");
										if(cityParams != null && cityParams.length > 0){
											for(int i=0;i<cityParams.length;i++){
												if(cityParams[i] != null){
													String[] subCityParams = cityParams[i].split("\\:");
													if(subCityParams != null && subCityParams.length > 1){
														if(cityIds == null)
															cityIds = subCityParams[0];
														else
															cityIds += ("," + subCityParams[0]);
													}
												}
											}
										}
									}
									catch(Exception e){
										e.printStackTrace();
									}
									List<CityVehicle> cvs = mApplication.dbCache.getCityVehicles(cityIds);
									if(cvs != null && !cvs.isEmpty()){
										for(CityVehicle cv : cvs){
											cv.setValid(false);
										}
										mApplication.dbCache.saveCityVehicles(cvs);
									}
								}
								if(dataType.contains("4")){
									TaskInfo taskInfo = mApplication.getTask(vehicleNumber);
									if(taskInfo != null){
										if(taskInfo.getnCount() >= 5){
											mApplication.removeTask(vehicleNumber);
											Intent intent = new Intent("com.kplus.car.getagainstRecords");
											intent.putExtra("vehicleNumber", vehicleNumber);
//											intent.putExtra("fail", true);
											LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//											vehicle.setHasSearchFail(true);
											mApplication.dbCache.updateVehicle(vehicle);
											showResult(vehicleNumber, false, false);
										}
									}
								}
								else if(dataType.contains("8")){
									int verifyCodeErrorCount = ti.getVerifyCodeErrorCount();
									verifyCodeErrorCount++;
									if(verifyCodeErrorCount >= 2){
										mApplication.removeTask(vehicleNumber);
										Intent intent = new Intent("com.kplus.car.getagainstRecords");
										intent.putExtra("vehicleNumber", vehicleNumber);
										intent.putExtra("fail", true);
										intent.putExtra("verifyCodeError", true);
										LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
										vehicle.setHasSearchFail(true);
										mApplication.dbCache.updateVehicle(vehicle);
										showResult(vehicleNumber, false, true);
										return;
									}
									ti.setVerifyCodeErrorCount(verifyCodeErrorCount);
									if(!StringUtils.isEmpty(sessionId)){
										Intent intent = new Intent(getApplicationContext(), AlertDialogActivity.class);
										intent.putExtra("alertType", KplusConstants.ALERT_INPUT_VERIFY_CODE);
										intent.putExtra("vehicleNumber", vehicleNumber);
										intent.putExtra("sessionId", sessionId);
										startActivity(intent);
									}
								}
								else{
									if(dataType.contains("0") || dataType.contains("1") || dataType.contains("2") || dataType.contains("3")){
										Intent intent = new Intent("com.kplus.car.getagainstRecords");
										intent.putExtra("type", dataType);
										intent.putExtra("vehicleNumber", vehicleNumber);
										LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
										showResult(vehicleNumber, add, false);
									}
								}
							}
							else{
								mApplication.removeTask(vehicleNumber);
								Intent intent = new Intent("com.kplus.car.getagainstRecords");
								intent.putExtra("fail", true);
								intent.putExtra("vehicleNumber", vehicleNumber);
								LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
								vehicle.setHasSearchFail(true);
								mApplication.dbCache.updateVehicle(vehicle);
								showResult(vehicleNumber,false, false);
							}
						}
					}
				}
				finish();
			}
		}.execute();
	}
	
	private void showResult(String vehicleNumber, boolean add, boolean verifyCodeError){
		Intent intentComplete = new Intent(mApplication, AlertDialogActivity.class);
		intentComplete.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
		intentComplete.putExtra("vehicleNumber", vehicleNumber);
		intentComplete.putExtra("title", "您的违章查询任务已完成");
		intentComplete.putExtra("message", "小牛很高兴地告诉您，你提交的[查询车辆" + vehicleNumber + "违章]任务已经执行完毕");
		intentComplete.putExtra("leftButtonText", "关闭");
		intentComplete.putExtra("rightButtonText", "查看结果");
		intentComplete.putExtra("subAlertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE_SUBTYPE_VIEW_AGAINST_RESULT);
		intentComplete.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentComplete.putExtra("add", add);
		intentComplete.putExtra("verifyCodeError", verifyCodeError);
		startActivity(intentComplete);
	}
	
	public class RemindCashAdapter extends AbstractWheelTextAdapter{
		public RemindCashAdapter(Context context) {
			super(context, R.layout.daze_cash_select_item, NO_RESOURCE);
			setItemTextResource(R.id.tvCashInfo);
		}

		@Override
		public int getItemsCount() {
			if(reminders == null)
				return 0;
			return reminders.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			if(reminders == null || reminders.length == 0)
				return null;
			if(reminders[index] < 60)
				return reminders[index] + "分钟后提醒我";
			return reminders[index]/60 + "小时后提醒我";
		}
		
		@Override
	    public View getItem(int index, View convertView, ViewGroup parent) {
			View view = super.getItem(index, convertView, parent);
	        return view;
	    }

	}

}
