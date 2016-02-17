package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.LicenceInfo;
import com.kplus.car.model.json.VehicleAuthJson;
import com.kplus.car.model.response.GetLicenceInfoReponse;
import com.kplus.car.model.response.VehicleAuthResponse;
import com.kplus.car.model.response.request.GetLicenceInfoRequest;
import com.kplus.car.model.response.request.VehicleAuthRequest;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class UploadLicenceActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_FOR_CANCEL = 1;
	private static final int REQUEST_FOR_UPLOAD_LICENCE = 2;
	private ImageView ivBack;
	private TextView tvTitle;
	private ImageView ivImage;
	private Button btRetake, btSubmit, btReUpload, btCancelUpload;
	private TextView tvDeclare;
	private View userInfoView;
	private TextView tvVehicleNumber, tvVehicleType, tvOwnerName, tvUseType, tvBrandMode, tvFrameNumber, tvMotorNumber, tvRegistDate, tvAbtainDate;
	private String imagePath;
	private Bitmap bitmap;
	private String vehicleNumber;
	private LicenceInfo licneceInfo;
	private String uploadLabel;
	private String picUrl;
	private boolean useWenTong = true;
	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		setContentView(R.layout.daze_upload_licence);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("车辆认证");
		ivImage = (ImageView) findViewById(R.id.ivImage);
		btRetake = (Button) findViewById(R.id.btRetake);
		btSubmit = (Button) findViewById(R.id.btSubmit);
		btReUpload = (Button) findViewById(R.id.btReUpload);
		btCancelUpload = (Button) findViewById(R.id.btCancelUpload);
		btRetake.setVisibility(View.VISIBLE);
		btSubmit.setVisibility(View.VISIBLE);
		btReUpload.setVisibility(View.GONE);
		btCancelUpload.setVisibility(View.GONE);
		tvDeclare = (TextView) findViewById(R.id.tvDeclare);
		tvDeclare.setVisibility(View.GONE);
		userInfoView = findViewById(R.id.userInfoView);
		userInfoView.setVisibility(View.GONE);
		tvVehicleNumber = (TextView) findViewById(R.id.tvVehicleNumber);
		tvVehicleType = (TextView) findViewById(R.id.tvVehicleType);
		tvOwnerName = (TextView) findViewById(R.id.tvOwnerName);
		tvUseType = (TextView) findViewById(R.id.tvUseType);
		tvBrandMode = (TextView) findViewById(R.id.tvBrandMode);
		tvFrameNumber = (TextView) findViewById(R.id.tvFrameNumber);
		tvMotorNumber = (TextView) findViewById(R.id.tvMotorNumber);
		tvRegistDate = (TextView) findViewById(R.id.tvRegistDate);
		tvAbtainDate = (TextView) findViewById(R.id.tvAbtainDate);
	}

	@Override
	protected void loadData() {
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		imagePath = getIntent().getStringExtra("imagePath");
		uploadLabel = getIntent().getStringExtra("uploadLabel");
		if(!StringUtils.isEmpty(imagePath)){
			bitmap = BMapUtil.getBitmapFromPath(imagePath, 500);
			bitmap = BMapUtil.rotateByExifInfo(bitmap, imagePath);
			if(bitmap != null){
				try{
					ivImage.setImageBitmap(bitmap);
					resizeImageAndInfoView();
					new AsyncTask<Void, Void, String>(){
						@Override
						protected String doInBackground(Void... params) {
							try{
								return MobclickAgent.getConfigParams(UploadLicenceActivity.this, "useWenTong");
							}
							catch(Exception e){
								e.printStackTrace();
								return null;
							}
						}
						
						protected void onPostExecute(String result) {
							if(!StringUtils.isEmpty(result) && result.equals("1")){
								useWenTong = true;
							}
							else
								useWenTong = false;
						}
					}.execute();
				}catch(Exception e){
					finish();
				}
			}
			else{
				ToastUtil.TextToast(this, "获取图片失败", Toast.LENGTH_SHORT, Gravity.CENTER);
				finish();
			}
		}
		else{
			ToastUtil.TextToast(this, "获取图片失败", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
	}

	@Override
	protected void setListener() {
		ivBack.setOnClickListener(this);
		btRetake.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		btReUpload.setOnClickListener(this);
		btCancelUpload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ivBack:
			intent = new Intent(UploadLicenceActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
			intent.putExtra("message", "退出此次认证？");
			startActivityForResult(intent, REQUEST_FOR_CANCEL);
			break;
		case R.id.btRetake:
			if(btRetake.getText().toString().equals("修改信息")){
				intent = new Intent(UploadLicenceActivity.this, InformationConfirmedActivity.class);
				intent.putExtra("vehicleNumber", vehicleNumber);
				intent.putExtra("licneceInfo", licneceInfo);
				intent.putExtra("picUrl", picUrl);
				startActivity(intent);
			}
			else{
				intent = new Intent(UploadLicenceActivity.this, ShootActivity.class);
				intent.putExtra("vehicleNumber", vehicleNumber);
				startActivity(intent);
			}
			finish();
			break;
		case R.id.btSubmit:
			if(btSubmit.getText().toString().equals("提交审核")){
				submitImage(bitmap);
			}
			else{
				if(licneceInfo != null){
					String vn = licneceInfo.getVehicleNum();
					if(StringUtils.isEmpty(vn)){
						ToastUtil.TextToast(this, "缺少车牌号码", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(!vehicleNumber.equals(vn)){
						ToastUtil.TextToast(this, "车牌号码不符", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getVehicleType())){
						ToastUtil.TextToast(this, "缺少车辆类型", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getUseProperty())){
						ToastUtil.TextToast(this, "缺少使用性质", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getBrandModel())){
						ToastUtil.TextToast(this, "缺少品牌型号", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getFrameNum())){
						ToastUtil.TextToast(this, "缺少车辆识别代号", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getMotorNum())){
						ToastUtil.TextToast(this, "缺少发动机号码", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getRegisterDate())){
						ToastUtil.TextToast(this, "缺少注册日期", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getIssueDate())){
						ToastUtil.TextToast(this, "缺少发证日期", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
					if(StringUtils.isEmpty(licneceInfo.getOwner())){
						ToastUtil.TextToast(this, "缺少车主姓名", Toast.LENGTH_SHORT, Gravity.CENTER);
						return;
					}
				}
				submitInfo();
			}
			break;
		case R.id.btReUpload:
			submitImage(bitmap);
			break;
		case R.id.btCancelUpload:
			intent = new Intent(UploadLicenceActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
			intent.putExtra("message", "退出此次认证？");
			startActivityForResult(intent, REQUEST_FOR_CANCEL);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_FOR_CANCEL){
			if(resultCode == RESULT_OK){
				finish();
			}
		}
		else if(requestCode == REQUEST_FOR_UPLOAD_LICENCE){
			if(resultCode == RESULT_OK){
				if(data != null && data.getStringExtra("imageUrl") != null){
					picUrl = data.getStringExtra("imageUrl");
					if(useWenTong)
						getLicenceInfo();
					else {
						licneceInfo = null;
						submitInfo();
					}
				}
			}
			else{
				btRetake.setVisibility(View.VISIBLE);
				btRetake.setText("重拍");
				btSubmit.setVisibility(View.GONE);
				btReUpload.setVisibility(View.VISIBLE);
				btCancelUpload.setVisibility(View.GONE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void resizeImageAndInfoView(){
		RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams)ivImage.getLayoutParams();
		int screenWidth = mApplication.getnScreenWidth();
		screenWidth = screenWidth - dip2px(UploadLicenceActivity.this, 32);
		rllp.width = screenWidth;
		rllp.height = screenWidth*30/43;
		ivImage.setLayoutParams(rllp);
		RelativeLayout.LayoutParams lllp = (RelativeLayout.LayoutParams)userInfoView.getLayoutParams();
		lllp.width = screenWidth;
		lllp.height = screenWidth*30/43;
		userInfoView.setLayoutParams(lllp);
	}
	
	private void submitImage(Bitmap bitmap) {
		byte[] content = BMapUtil.getContentFromBitmap(bitmap);
		if(content.length > 0){
			Intent intent = new Intent(UploadLicenceActivity.this, UploadProgressActivity.class);
			intent.putExtra("type", KplusConstants.UPLOAD_AUTH_LICENCE_PHOTO);
			intent.putExtra("content", content);
			startActivityForResult(intent, REQUEST_FOR_UPLOAD_LICENCE);
		}
	}

	private void getLicenceInfo(){
		new AsyncTask<Void, Void, GetLicenceInfoReponse>(){
			@Override
			protected GetLicenceInfoReponse doInBackground(Void... params) {
				try{
					GetLicenceInfoRequest request = new GetLicenceInfoRequest();
					request.setParams(vehicleNumber, picUrl);
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					return null;
				}
			}
			protected void onPostExecute(GetLicenceInfoReponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						licneceInfo = result.getData();
						if(licneceInfo != null){
							if(licneceInfo.getStatus() != null && licneceInfo.getStatus() == 1){
								btSubmit.setVisibility(View.VISIBLE);
								btSubmit.setText("确认提交");
								btRetake.setVisibility(View.VISIBLE);
								btRetake.setText("修改信息");
								btReUpload.setVisibility(View.GONE);
								btCancelUpload.setVisibility(View.GONE);
								tvDeclare.setVisibility(View.VISIBLE);
								userInfoView.setVisibility(View.VISIBLE);
								if(!StringUtils.isEmpty(licneceInfo.getVehicleNum()))
									tvVehicleNumber.setText(licneceInfo.getVehicleNum());
								if(!StringUtils.isEmpty(licneceInfo.getOwner()))
									tvOwnerName.setText(licneceInfo.getOwner());
								if(!StringUtils.isEmpty(licneceInfo.getVehicleType()))
									tvVehicleType.setText(licneceInfo.getVehicleType());
								if(!StringUtils.isEmpty(licneceInfo.getUseProperty()))
									tvUseType.setText(licneceInfo.getUseProperty());
								if(!StringUtils.isEmpty(licneceInfo.getBrandModel()))
									tvBrandMode.setText(licneceInfo.getBrandModel());
								if(!StringUtils.isEmpty(licneceInfo.getFrameNum()))
									tvFrameNumber.setText(licneceInfo.getFrameNum());
								if(!StringUtils.isEmpty(licneceInfo.getMotorNum()))
									tvMotorNumber.setText(licneceInfo.getMotorNum());
								if(!StringUtils.isEmpty(licneceInfo.getRegisterDate()))
									tvRegistDate.setText(licneceInfo.getRegisterDate());
								if(!StringUtils.isEmpty(licneceInfo.getIssueDate()))
									tvAbtainDate.setText(licneceInfo.getIssueDate());
							}
							else{
								btSubmit.setVisibility(View.GONE);
								btRetake.setVisibility(View.VISIBLE);
								btRetake.setText("重拍");
								btReUpload.setVisibility(View.GONE);
								btCancelUpload.setVisibility(View.GONE);
								ToastUtil.TextToast(UploadLicenceActivity.this, "照片识别失败，请重新拍摄", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
						}
						else{
							btSubmit.setVisibility(View.GONE);
							btRetake.setVisibility(View.VISIBLE);
							btRetake.setText("重拍");
							btReUpload.setVisibility(View.GONE);
							btCancelUpload.setVisibility(View.GONE);
							ToastUtil.TextToast(UploadLicenceActivity.this, "照片识别失败，请重新拍摄", Toast.LENGTH_SHORT, Gravity.CENTER);
						}
					}
					else{
						btSubmit.setVisibility(View.GONE);
						btRetake.setVisibility(View.VISIBLE);
						btRetake.setText("重拍");
						btReUpload.setVisibility(View.GONE);
						btCancelUpload.setVisibility(View.GONE);
						ToastUtil.TextToast(UploadLicenceActivity.this, "照片识别失败，请重新拍摄", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					btSubmit.setVisibility(View.GONE);
					btRetake.setVisibility(View.VISIBLE);
					btRetake.setText("重拍");
					btReUpload.setVisibility(View.VISIBLE);
					btCancelUpload.setVisibility(View.GONE);
					ToastUtil.TextToast(UploadLicenceActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
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
								Intent intent = new Intent(UploadLicenceActivity.this, EmergencyLicenseActivity.class);
								intent.putExtra("vehicleNumber", vehicleNumber);
								startActivity(intent);
								finish();
							}
							else{
								btSubmit.setVisibility(View.GONE);
								btRetake.setVisibility(View.VISIBLE);
								btRetake.setText("重拍");
								btReUpload.setVisibility(View.VISIBLE);
								btCancelUpload.setVisibility(View.GONE);
								ToastUtil.TextToast(UploadLicenceActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
							}
						}
						else{
							btSubmit.setVisibility(View.GONE);
							btRetake.setVisibility(View.VISIBLE);
							btRetake.setText("重拍");
							btReUpload.setVisibility(View.VISIBLE);
							btCancelUpload.setVisibility(View.GONE);
							ToastUtil.TextToast(UploadLicenceActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
						}
					}
					else{
						btSubmit.setVisibility(View.GONE);
						btRetake.setVisibility(View.VISIBLE);
						btRetake.setText("重拍");
						btReUpload.setVisibility(View.VISIBLE);
						btCancelUpload.setVisibility(View.GONE);
						ToastUtil.TextToast(UploadLicenceActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					btSubmit.setVisibility(View.GONE);
					btRetake.setVisibility(View.VISIBLE);
					btRetake.setText("重拍");
					btReUpload.setVisibility(View.VISIBLE);
					btCancelUpload.setVisibility(View.GONE);
					ToastUtil.TextToast(UploadLicenceActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
		}.execute();
	}
	
	@Override
	protected void onDestroy() {
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}
}
