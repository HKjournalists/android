package com.kplus.car.activity;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.LicenceInfo;
import com.kplus.car.model.json.VehicleAuthJson;
import com.kplus.car.model.response.VehicleAuthResponse;
import com.kplus.car.model.response.request.VehicleAuthRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.method.ReplacementTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InformationConfirmedActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_FOR_CANCEL = 1;
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View rightView;
	private TextView tvRight;
	private EditText etVehicleNumber, etVehicleType, etOwner,etUseType, etVehicleMode, etFrameNumber, etMotorNumber, etRegisterDate, etAbtainDate;
	private Button informationconfirmed;
	private LicenceInfo licneceInfo;
	private String vehicleNumber;
	private String picUrl;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_information_confirmed_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("信息确认");
		rightView = findViewById(R.id.rightView);
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText("确认提交");		
		etVehicleNumber = (EditText)findViewById(R.id.etVehicleNumber);
		etVehicleNumber.setTransformationMethod(new AllCapTransformationMethod());
		etVehicleType = (EditText)findViewById(R.id.etVehicleType);
		etOwner = (EditText)findViewById(R.id.etOwner);
		etUseType = (EditText)findViewById(R.id.etUseType);
		etVehicleMode = (EditText)findViewById(R.id.etVehicleMode);
		etFrameNumber = (EditText)findViewById(R.id.etFrameNumber);
		etFrameNumber.setTransformationMethod(new AllCapTransformationMethod());
		etMotorNumber = (EditText)findViewById(R.id.etMotorNumber);
		etMotorNumber.setTransformationMethod(new AllCapTransformationMethod());
		etRegisterDate = (EditText)findViewById(R.id.etRegisterDate);
		etAbtainDate = (EditText)findViewById(R.id.etAbtainDate);
		informationconfirmed = (Button) findViewById(R.id.informationconfirmed);
	}

	@Override
	protected void loadData() {
		Intent intent = getIntent();
		licneceInfo = (LicenceInfo) intent.getSerializableExtra("licneceInfo");
		picUrl = intent.getStringExtra("picUrl");
		if(licneceInfo != null){
			if(!StringUtils.isEmpty(licneceInfo.getVehicleNum()))
				etVehicleNumber.setText(licneceInfo.getVehicleNum());
			if(!StringUtils.isEmpty(licneceInfo.getVehicleType()))
				etVehicleType.setText(licneceInfo.getVehicleType());
			if(!StringUtils.isEmpty(licneceInfo.getUseProperty()))
				etUseType.setText(licneceInfo.getUseProperty());
			if(!StringUtils.isEmpty(licneceInfo.getBrandModel()))
				etVehicleMode.setText(licneceInfo.getBrandModel());
			if(!StringUtils.isEmpty(licneceInfo.getFrameNum()))
				etFrameNumber.setText(licneceInfo.getFrameNum());
			if(!StringUtils.isEmpty(licneceInfo.getMotorNum()))
				etMotorNumber.setText(licneceInfo.getMotorNum());
			if(!StringUtils.isEmpty(licneceInfo.getRegisterDate()))
				etRegisterDate.setText(licneceInfo.getRegisterDate());
			if(!StringUtils.isEmpty(licneceInfo.getIssueDate()))
				etAbtainDate.setText(licneceInfo.getIssueDate());
			if(!StringUtils.isEmpty(licneceInfo.getOwner()))
				etOwner.setText(licneceInfo.getOwner());
		}
		else{
			ToastUtil.TextToast(this, "缺少参数", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
			return;
		}
		vehicleNumber = intent.getStringExtra("vehicleNumber");
		if(StringUtils.isEmpty(vehicleNumber)){
			ToastUtil.TextToast(this, "缺少参数", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
		informationconfirmed.setOnClickListener(this);
		etVehicleNumber.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etVehicleType.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etUseType.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etVehicleMode.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etFrameNumber.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etMotorNumber.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etRegisterDate.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etAbtainDate.setOnFocusChangeListener(new MyOnFocusChangeListener());
		etOwner.setOnFocusChangeListener(new MyOnFocusChangeListener());
	}


	@Override
	public void onClick(View v) {
		if(v.equals(leftView))
		{
			Intent intent = new Intent(InformationConfirmedActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
			intent.putExtra("message", "退出此次认证？");
			startActivityForResult(intent, REQUEST_FOR_CANCEL);
		}
		else if(v.equals(informationconfirmed) || v.equals(rightView)){
			String vn = etVehicleNumber.getText().toString().toUpperCase();
			if(StringUtils.isEmpty(vn)){
				ToastUtil.TextToast(this, "请输入车牌号码", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			if(!vehicleNumber.equals(vn)){
				ToastUtil.TextToast(this, "车牌号码不符", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setVehicleNum(vn.toUpperCase());
			if(StringUtils.isEmpty(etVehicleType.getText().toString())){
				ToastUtil.TextToast(this, "请输入车辆类型", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setVehicleType(etVehicleType.getText().toString());
			if(StringUtils.isEmpty(etUseType.getText().toString())){
				ToastUtil.TextToast(this, "请输入使用性质", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setUseProperty(etUseType.getText().toString());
			if(StringUtils.isEmpty(etVehicleMode.getText().toString())){
				ToastUtil.TextToast(this, "请输入品牌型号", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setBrandModel(etVehicleMode.getText().toString());
			if(StringUtils.isEmpty(etFrameNumber.getText().toString())){
				ToastUtil.TextToast(this, "请输入车辆识别代号", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setFrameNum(etFrameNumber.getText().toString().toUpperCase());
			if(StringUtils.isEmpty(etMotorNumber.getText().toString())){
				ToastUtil.TextToast(this, "请输入发动机号码", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setMotorNum(etMotorNumber.getText().toString().toUpperCase());
			if(StringUtils.isEmpty(etRegisterDate.getText().toString())){
				ToastUtil.TextToast(this, "请输入注册日期", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setRegisterDate(etRegisterDate.getText().toString());
			if(StringUtils.isEmpty(etAbtainDate.getText().toString())){
				ToastUtil.TextToast(this, "请输入发证日期", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setIssueDate(etAbtainDate.getText().toString());
			if(StringUtils.isEmpty(etOwner.getText().toString())){
				ToastUtil.TextToast(this, "请输入车主姓名", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
			licneceInfo.setOwner(etOwner.getText().toString());
			submitInfo();
		}
	}
	
	private void submitInfo(){
		new AsyncTask<Void, Void, VehicleAuthResponse>(){
			@Override
			protected VehicleAuthResponse doInBackground(Void... params) {
				try{
					VehicleAuthRequest request = new VehicleAuthRequest();
					request.setParams(mApplication.getUserId(), mApplication.getId(), vehicleNumber, picUrl, licneceInfo);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(VehicleAuthResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						VehicleAuthJson data = result.getData();
						if(data != null){
							if(data.getResult() != null && data.getResult()){
								Intent intent = new Intent(InformationConfirmedActivity.this, EmergencyLicenseActivity.class);
								intent.putExtra("vehicleNumber", vehicleNumber);
								startActivity(intent);
								finish();
							}
							else{
								ToastUtil.TextToast(InformationConfirmedActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
							}
						}
						else{
							ToastUtil.TextToast(InformationConfirmedActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
						}
					}
					else{
						ToastUtil.TextToast(InformationConfirmedActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					ToastUtil.TextToast(InformationConfirmedActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
	}
	
	private int length(String value) {
		if (value == null)
			return 0;
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_CANCEL){
			if(resultCode == RESULT_OK){
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class MyOnFocusChangeListener implements OnFocusChangeListener{
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				String strTemp = ((EditText)v).getText().toString();
				if(strTemp != null){
					((EditText)v).setSelection(strTemp.length());
				}
			}
		}
		
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

}
