package com.kplus.car.activity;

import java.util.List;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.R;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.json.AuthDetailJson;
import com.kplus.car.model.response.GetAuthDetaiResponse;
import com.kplus.car.model.response.request.GetAuthDetaiRequest;
import com.kplus.car.service.UpdateUserVehicleService;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

public class EmergencyLicenseActivity extends BaseActivity implements OnClickListener {
	private static final int PHONE_REGIST_EMERGENCY_REQUEST = 0x01;
	public static final int CAMERA_REQUEST = 0x02;
	public static final int ENTER_ALBUM = 0x04;

	private TextView tvTitle;
	private View leftView;
	private ImageView ivLeft;
	private TextView uploadBtn;
	private TextView confirmBtn;
	private TextView loginBtn;
	private View llBottom;
	private Button btAlbum,btCamera,btCancel;

	private TextView licenseTip;
	private ImageView licenseImage;
	private TextView licenseStatus;
	private Bitmap bitmap;
	private int nStatus;
	private String imageurl;
	private String vehicleNumber;
	private VehicleAuth vehicleAuth;
	private String uploadLabel;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_emergency_license);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("车辆认证特权");
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		uploadBtn = (TextView) findViewById(R.id.emergency_help_license_uploadBtn);
		confirmBtn = (TextView) findViewById(R.id.emergency_help_license_confirmBtn);
		loginBtn = (TextView) findViewById(R.id.emergency_help_license_loginBtn);
		licenseTip = (TextView) findViewById(R.id.emergency_help_license_tip);
		licenseImage = (ImageView) findViewById(R.id.emergency_help_license);
		licenseStatus = (TextView) findViewById(R.id.emergency_help_license_status);
		llBottom = findViewById(R.id.llBottom);
		btAlbum = (Button) findViewById(R.id.btAlbum);
		btCamera = (Button) findViewById(R.id.btCamera);
		btCancel = (Button) findViewById(R.id.btCancel);
	}

	protected void loadData() {
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicleNumber);
		if(vehicleAuth != null){
			nStatus = vehicleAuth.getStatus() == null ? 0 : vehicleAuth.getStatus();
			imageurl = vehicleAuth.getDrivingLicenceUrl();
		}
		setStatusText(nStatus);
		if(!StringUtils.isEmpty(imageurl))
			setImageView(licenseImage, imageurl,R.drawable.daze_vehice_license_demo);
	}

	private void setStatusText(int status) {
		switch (status) {
		case 0:
			licenseStatus.setText("照片范本");
			licenseStatus.setBackgroundResource(R.drawable.daze_text_pink_s_bg);
			licenseTip.setText("上传行驶证认证车辆，可获认证大礼包");
			uploadBtn.setVisibility(View.VISIBLE);
			return;
		case 1:
			licenseStatus.setText("审核中");
			licenseStatus.setBackgroundResource(R.drawable.daze_text_orange_s_bg);
			licenseTip.setText("正在进行认证审核，一般会在3个工作日内审核完毕，审核结果将以推送消息的方式告知");
			uploadBtn.setText("重新上传");
			uploadBtn.setVisibility(View.VISIBLE);
			llBottom.setVisibility(View.GONE);
			return;
		case 2:
			tvTitle.setText("认证会员");
			licenseStatus.setText("已认证");
			licenseStatus.setBackgroundResource(R.drawable.daze_text_cyan_s_bg);
			licenseTip.setText("您的车辆已认证成功，恭喜您成为橙牛认证会员！");
			uploadBtn.setVisibility(View.GONE);
			confirmBtn.setVisibility(View.GONE);
			if (mApplication.getpId() == 0) {
				loginBtn.setVisibility(View.VISIBLE);
			}
			llBottom.setVisibility(View.GONE);
			return;
		case 3:
			licenseStatus.setText("未通过审核");
			licenseStatus.setBackgroundResource(R.drawable.daze_text_red_s_bg);
			licenseTip.setText("证件模糊不清，文字无法辨识，请拍摄清晰版并上传，系统收到后将重新启动认证流程");
			uploadBtn.setText("重新上传");
			uploadBtn.setVisibility(View.VISIBLE);
			return;

		default:
			licenseStatus.setText("预览");
			licenseStatus.setBackgroundResource(R.drawable.daze_text_pink_s_bg);
			licenseTip.setText("提交前请确认照片清晰可辨识，认证通过后，认证照片将不能进行修改");
			uploadBtn.setText("重新上传");
			uploadBtn.setVisibility(View.VISIBLE);
			return;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHONE_REGIST_EMERGENCY_REQUEST:
			if (mApplication.getpId() != 0) {
				Intent intent = new Intent(this, EmergencyDetailActivity.class);
				intent.putExtra("vehicleNumber", vehicleNumber);
				startActivity(intent);
				finish();
			}
			break;
		case ENTER_ALBUM:
			if (resultCode == RESULT_OK){
				try{
					if(data != null){
						uploadLabel = "uploadCert_album";
						Uri selectedImage = data.getData();
						String[] filePathColumns={MediaStore.Images.Media.DATA};
						Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null,null, null);
						c.moveToFirst();
						int columnIndex = c.getColumnIndex(filePathColumns[0]);
						String picturePath= c.getString(columnIndex);
						c.close();
						Intent intent = new Intent(EmergencyLicenseActivity.this, UploadLicenceActivity.class);
						intent.putExtra("imagePath", picturePath);
						intent.putExtra("uploadLabel", uploadLabel);
		                intent.putExtra("vehicleNumber", vehicleNumber);
		                startActivity(intent);
		                finish();
					}
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);
		btAlbum.setOnClickListener(this);
		btCamera.setOnClickListener(this);
		btCancel.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		licenseImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(leftView)) {
			finish();
			if(nStatus == 0 || nStatus == 3){
				if(mApplication.getRemindPendingIntent() == null){
					Intent intent = new Intent(this,Remind.class);
					intent.putExtra("vehicleNumber", vehicleNumber);
					startActivity(intent);
				}
			}
			else{
				if(mApplication.getRemindPendingIntent() != null){
					((AlarmManager) getSystemService(Service.ALARM_SERVICE)).cancel(mApplication.getRemindPendingIntent());
					mApplication.setRemindPendingIntent(null);
				}
			}
		} else if (v.equals(uploadBtn)) {
			if(llBottom.getVisibility() == View.GONE)
				llBottom.setVisibility(View.VISIBLE);
		}
		else if(v.equals(btAlbum)){
			llBottom.setVisibility(View.GONE);
			Intent picture = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(picture, ENTER_ALBUM);
		}
		else if(v.equals(btCamera)){
			uploadLabel = "uploadCert_camera";
			llBottom.setVisibility(View.GONE);
			Intent intent = new Intent(this,ShootActivity.class);
			intent.putExtra("vehicleNumber", vehicleNumber);
			startActivity(intent);
			ToastUtil.TextToast(this, "请将证件尽量对齐四角\n调整手机位置至文字清晰", Toast.LENGTH_SHORT, Gravity.CENTER);
			finish();
		}
		else if(v.equals(btCancel)){
			llBottom.setVisibility(View.GONE);
		}
		else if(v.equals(loginBtn)){
			Intent intent = new Intent(this, PhoneRegistActivity.class);
			startActivityForResult(intent, PHONE_REGIST_EMERGENCY_REQUEST);
		}
		else if(v.equals(licenseImage)){
			if(imageurl != null){
				Intent intent = new Intent(this, ImageActivity.class);
				intent.putExtra("imageUrl", imageurl);
				startActivity(intent);
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			if(nStatus == 0 || nStatus == 3)
			{
				if(mApplication.getRemindPendingIntent() == null){
					Intent intent = new Intent(this,Remind.class);
					intent.putExtra("vehicleNumber", vehicleNumber);
					startActivity(intent);
				}
			}
			else{
				if(mApplication.getRemindPendingIntent() != null){
					((AlarmManager) getSystemService(Service.ALARM_SERVICE)).cancel(mApplication.getRemindPendingIntent());
					mApplication.setRemindPendingIntent(null);
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		getAuthDetail();
		super.onResume();
	}
	
	private void getAuthDetail(){
		new AsyncTask<Void, Void, GetAuthDetaiResponse>(){
			@Override
			protected GetAuthDetaiResponse doInBackground(Void... params) {
				try{
					GetAuthDetaiRequest request = new GetAuthDetaiRequest();
					request.setParams(mApplication.getUserId(), mApplication.getId(), vehicleNumber);
					return mApplication.client.execute(request);
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			protected void onPostExecute(GetAuthDetaiResponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						AuthDetailJson data = result.getData();
						if(data != null){
							List<VehicleAuth> list = data.getList();
							if(list != null && !list.isEmpty()){
								vehicleAuth = list.get(0);
								if(vehicleAuth != null){
									mApplication.dbCache.saveVehicleAuth(vehicleAuth);
									startService(new Intent(EmergencyLicenseActivity.this, UpdateUserVehicleService.class));
								}
							}
						}
					}
				}
				if(vehicleAuth == null)
					vehicleAuth = mApplication.dbCache.getVehicleAuthByVehicleNumber(vehicleNumber);
				if(vehicleAuth != null){
					nStatus = vehicleAuth.getStatus() == null ? 0 : vehicleAuth.getStatus();
					imageurl = vehicleAuth.getDrivingLicenceUrl();
				}
				setStatusText(nStatus);
				if(!StringUtils.isEmpty(imageurl))
					setImageView(licenseImage, imageurl,R.drawable.daze_vehice_license_demo);
			}
		}.execute();
	}

}
