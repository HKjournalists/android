package com.kplus.car.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.model.Account;
import com.kplus.car.model.CityVehicle;
import com.kplus.car.model.UserInfo;
import com.kplus.car.model.VehicleAuth;
import com.kplus.car.model.response.AddUserInfoReponse;
import com.kplus.car.model.response.UploadCertImgResponse;
import com.kplus.car.model.response.request.AddUserInfoRequest;
import com.kplus.car.model.response.request.UploadCertImgRequest;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.FileItem;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UserInfoActivity extends BaseActivity implements OnClickListener
{
	private static final int REQUEST_PICK_IMAGE = 1;
	private static final int REQUEST_EDIT_NICKNAME = 2;
	private static final int REQUEST_EDIT_INFO = 3;
	private static final int REQUEST_EDIT_LOCATION = 4;
	private static final int REQUEST_PICK_ALBUM = 5;
	private static final int REQUEST_PICK_CAMERA = 6;
	private static final int REQUEST_BIND_PHONE = 7;
	private static final int REQUEST_BIND_WECHAT = 10;
	private static final int REQUEST_QUIT = 11;
	
	private static final String imageDir = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
	
	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private ImageView ivHeadPhoto;
	private View headPhotoView;
	private TextView tvNickName;
	private View nickNameView;
	private TextView tvSex;
	private View sexView;
	private TextView tvLocation;
	private View locationView;
	private TextView tvInfo;
	private View infoView;
	private View sexSelectedView;
	private Button btBlank;
	private Button btMale, btFmale;
	private View phoneView, wechatView;
	private TextView tvPhone, tvWechat;
	private View tvQuit;
	
	private String iconUrl;
	private int sex = 0;
	private String strNickName;
	private String strLocation;
	private String strInfo;
    private UserInfo userInfo;
    private DisplayImageOptions optionsPhoto;
    private String currentImagePath;
    private List<Account> accounts;
    
    @Override
    protected void onResume() {
    	super.onResume();
		tvPhone.setText("未绑定");
		tvWechat.setText("未绑定");
		accounts = mApplication.dbCache.getAccounts();
		if(accounts != null && !accounts.isEmpty()){
			for(Account account : accounts){
				switch(account.getType()){
				case 0:
					tvPhone.setText(account.getUserName());
					break;
				case 2:
					tvWechat.setText(account.getNickname());
					break;
					default:
						break;
				}				
			}
		}
    }

	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_user_info);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("个人资料");
		ivHeadPhoto = (ImageView) findViewById(R.id.ivHeadPhoto);
		headPhotoView = findViewById(R.id.headPhotoView);
		tvNickName = (TextView) findViewById(R.id.tvNickName);
		nickNameView = findViewById(R.id.nickNameView);
		tvSex = (TextView) findViewById(R.id.tvSex);
		tvSex.setText("男");
		sexView = findViewById(R.id.sexView);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		locationView = findViewById(R.id.locationView);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
		infoView = findViewById(R.id.infoView);
		sexSelectedView = findViewById(R.id.sexSelectedView);
		btBlank = (Button)findViewById(R.id.btBlank);
		btMale = (Button) findViewById(R.id.btMale);
		btFmale = (Button) findViewById(R.id.btFmale);
		phoneView = findViewById(R.id.phoneView);
		wechatView = findViewById(R.id.wechatView);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		tvWechat = (TextView) findViewById(R.id.tvWechat);
		tvQuit = findViewById(R.id.tvQuit);
	}

	@Override
	protected void loadData()
	{
		optionsPhoto = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.avatar_defult)
		.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		userInfo = mApplication.dbCache.getUserInfo();
		if(userInfo != null){
			iconUrl = userInfo.getIconUrl();
			mApplication.imageLoader.displayImage(iconUrl, ivHeadPhoto, optionsPhoto);
			strNickName = userInfo.getName();
			if(!StringUtils.isEmpty(strNickName))
				tvNickName.setText(strNickName);
			sex = userInfo.getSex() == null ? -1 : userInfo.getSex().intValue();
			tvSex.setText(sex == -1 ? "请选择您的性别" :(sex == 1 ? "男" : "女"));
			strLocation = mApplication.getCityName();
			if(!StringUtils.isEmpty(strLocation)){
				tvLocation.setText(strLocation);
			}
			strInfo = userInfo.getInfo();
			if(!StringUtils.isEmpty(strInfo))
				tvInfo.setText(strInfo);
		}
	}

	@Override
	protected void setListener()
	{
		leftView.setOnClickListener(this);
		headPhotoView.setOnClickListener(this);
		nickNameView.setOnClickListener(this);
		sexView.setOnClickListener(this);
		locationView.setOnClickListener(this);
		infoView.setOnClickListener(this);
		btBlank.setOnClickListener(this);
		btMale.setOnClickListener(this);
		btFmale.setOnClickListener(this);
		phoneView.setOnClickListener(this);
		wechatView.setOnClickListener(this);
		tvQuit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if(v.equals(leftView)){
			finish();
		}
		else if(v.equals(headPhotoView)){
			startActivityForResult(new Intent(UserInfoActivity.this, PictureObtainTypeActivity.class), REQUEST_PICK_IMAGE);
		}
		else if(v.equals(nickNameView)){
			Intent intent = new Intent(UserInfoActivity.this, EditTextActivity.class);
			intent.putExtra("flag", 0);
			intent.putExtra("content", tvNickName.getText().toString());
			startActivityForResult(intent, REQUEST_EDIT_NICKNAME);
		}
		else if(v.equals(sexView)){
			if(sexSelectedView.getVisibility() == View.GONE)
				sexSelectedView.setVisibility(View.VISIBLE);
		}
		else if(v.equals(locationView)){
			Intent intent = new Intent(UserInfoActivity.this, CitySelectActivity.class);
			intent.putExtra("fromType", KplusConstants.SELECT_CITY_FROM_TYPE_USERINFO);
			intent.putExtra("cityName", tvLocation.getText().toString());
			startActivityForResult(intent, REQUEST_EDIT_LOCATION);
		}
		else if(v.equals(infoView)){
			Intent intent = new Intent(UserInfoActivity.this, EditTextActivity.class);
			intent.putExtra("flag", 1);
			intent.putExtra("content", tvInfo.getText().toString());
			startActivityForResult(intent, REQUEST_EDIT_INFO);
		}
		else if(v.equals(btBlank))
		{
			sexSelectedView.setVisibility(View.GONE);
		}
		else if(v.equals(btMale)){
			sexSelectedView.setVisibility(View.GONE);
			tvSex.setText("男");
			if(sex != 1){
				sex = 1;
				addUserInfo();
			}
		}
		else if(v.equals(btFmale)){
			sexSelectedView.setVisibility(View.GONE);
			tvSex.setText("女");
			if(sex != 0){
				sex = 0;
				addUserInfo();
			}
		}
		else if(v.equals(phoneView)){
			Intent intent = new Intent(UserInfoActivity.this, BindPhoneActivity.class);
			startActivityForResult(intent, REQUEST_BIND_PHONE);
		}
		else if(v.equals(wechatView)){
			Intent intent = new Intent(UserInfoActivity.this, ThirdpartAccountActivity.class);
			intent.putExtra("thirdPartName", "weichat");
			startActivityForResult(intent, REQUEST_BIND_WECHAT);
		}
		else if(v.equals(tvQuit)){
			Intent intent = new Intent(UserInfoActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_CHOOSE_WHETHER_EXECUTE);
			intent.putExtra("message", "是否要退出当前账户？");
			startActivityForResult(intent, REQUEST_QUIT);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try{
			if(requestCode == REQUEST_BIND_PHONE){
				Account account = mApplication.dbCache.getAccountByType(0);
				if(account != null){
					if(!StringUtils.isEmpty(account.getUserName())){
						tvPhone.setText(account.getUserName());
					}
					else{
						tvPhone.setText("未绑定");
					}
				}
				userInfo = mApplication.dbCache.getUserInfo();
				if(userInfo != null){
					iconUrl = userInfo.getIconUrl();
					mApplication.imageLoader.displayImage(iconUrl, ivHeadPhoto, optionsPhoto);
					strNickName = userInfo.getName();
					if(!StringUtils.isEmpty(strNickName))
						tvNickName.setText(strNickName);
					sex = userInfo.getSex() == null ? -1 : userInfo.getSex().intValue();
					tvSex.setText(sex == -1 ? "请选择您的性别" :(sex == 1 ? "男" : "女"));
					strLocation = mApplication.getCityName();
					if(!StringUtils.isEmpty(strLocation)){
						tvLocation.setText(strLocation);
					}
					strInfo = userInfo.getInfo();
					if(!StringUtils.isEmpty(strInfo))
						tvInfo.setText(strInfo);
				}
			}
			else if(requestCode == REQUEST_BIND_WECHAT){
				Account account = mApplication.dbCache.getAccountByType(2);
				if(account != null){
					if(!StringUtils.isEmpty(account.getUserName())){
						tvWechat.setText(account.getUserName());
					}
					else{
						tvWechat.setText("未绑定");
					}
				}
			}
			else if(requestCode == REQUEST_PICK_CAMERA){
				if(resultCode == RESULT_OK){
					addPicture(BMapUtil.cutBitmapFromPath(imageDir + currentImagePath, 200));
				}
				return;
			}
			else if(requestCode == REQUEST_QUIT){
				if(resultCode == RESULT_OK){
					mApplication.setId(0);
					mApplication.setpId(0);
					List<Account> accountList = mApplication.dbCache.getAccounts();
					long accountIds = 0;
					if(accountList != null && accountList.isEmpty()){
						for(Account ac : accountList){
							if(ac != null && ac.getType() != null){
								long accountId = 0;
								switch(ac.getType()){
								case 0:
									accountId = 2;
									break;
								case 1:
									accountId = 4;
									break;
								case 2:
									accountId = 8;
									break;
								case 3:
									accountId = 16;
									break;
								}
								accountIds |= accountId;
							}
						}
					}
					sp.edit().putBoolean("isLogout", true).commit();
					mApplication.dbCache.putValue(KplusConstants.DB_KEY_ORDER_CONTACTPHONE, "");
					mApplication.dbCache.deleteAccounts();
					mApplication.dbCache.clearUserInfo();
					List<VehicleAuth> vehicleAuths = mApplication.dbCache.getVehicleAuths();
						if(vehicleAuths != null && !vehicleAuths.isEmpty()){
						for(VehicleAuth va : vehicleAuths){
							va.setBelong(false);
						}
						mApplication.dbCache.saveVehicleAuths(vehicleAuths);
					}
					mApplication.logOutTaobao();
					LocalBroadcastManager.getInstance(this).sendBroadcastSync(new Intent("com.kplus.car.logout"));
					finish();
				}
				return;
			}
			if(data != null){
				switch (requestCode){
					case REQUEST_PICK_IMAGE:
						if(resultCode == RESULT_OK){
							if(data.getIntExtra("type", 0) == 1){
								File file = new File(imageDir);
								if(!file.exists())
									file.mkdirs();
								String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
								currentImagePath = fileName;
								Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
						        photoPickerIntent.setType("image/*");
						        startActivityForResult(photoPickerIntent, REQUEST_PICK_ALBUM);
							}
							else if(data.getIntExtra("type", 0) == 2){
								try {
									Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
									File file = new File(imageDir);
									if(!file.exists())
										file.mkdirs();
									String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
									currentImagePath = fileName;
									file = new File(imageDir, fileName);
									Uri uri = Uri.fromFile(file);
									intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
									startActivityForResult(intent, REQUEST_PICK_CAMERA);
									Toast.makeText(UserInfoActivity.this, "请横屏拍摄", Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						break;
					case REQUEST_PICK_ALBUM:
						if(resultCode == RESULT_OK){
							Uri uri = data.getData();
							if(uri != null){
		    					addPicture(BMapUtil.cutBitmapFromUri(UserInfoActivity.this, uri, 200));
							}
						}
						break;
					case REQUEST_EDIT_NICKNAME:
						if(resultCode == RESULT_OK){
							String content = data.getStringExtra("content");
							if(!StringUtils.isEmpty(content)){
								if(StringUtils.isEmpty(strNickName) || !content.equals(strNickName)){
									tvNickName.setText(content);
									addUserInfo();
								}
							}
						}
						break;
					case REQUEST_EDIT_INFO:
						if(resultCode == RESULT_OK){
							String content = data.getStringExtra("content");
							if(StringUtils.isEmpty(strInfo) || !content.equals(strInfo)){
								tvInfo.setText(content);
								addUserInfo();
							}
						}
						break;
					case REQUEST_EDIT_LOCATION:
						if(resultCode == RESULT_OK){
							ArrayList<CityVehicle> listTemp = data.getParcelableArrayListExtra("selectedCity");
							if(listTemp != null && !listTemp.isEmpty()){
								CityVehicle cv = listTemp.get(0);
								String content = cv.getName();
								if(!StringUtils.isEmpty(content)){
									if(StringUtils.isEmpty(strLocation) || !content.contains(strLocation)){
										tvLocation.setText(content);
										mApplication.setCityName(content);
									}
								}
							}
						}
						break;
					default:
						break;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	private void addUserInfo(){ 
		new AsyncTask<Void, Void, AddUserInfoReponse>()
		{
			private String errortext = "网络中断，请稍后重试";

			@Override
			protected AddUserInfoReponse doInBackground(Void... params)
			{
				try{
					AddUserInfoRequest request = new AddUserInfoRequest();
					request.setParams(mApplication.getId(), iconUrl, tvNickName.getText().toString(), sex, tvLocation.getText().toString(), tvInfo.getText().toString());
					return mApplication.client.execute(request);
				}
				catch(Exception e){
					e.printStackTrace();
					errortext = e.toString();
					return null;
				}
			}

			protected void onPostExecute(AddUserInfoReponse result) {
				if(result != null){
					if(result.getCode() != null && result.getCode() == 0){
						if(result.getData().getResult()){
							if(!StringUtils.isEmpty(iconUrl))
								userInfo.setIconUrl(iconUrl);
							strNickName = tvNickName.getText().toString();
							if(!StringUtils.isEmpty(strNickName))
								userInfo.setName(strNickName);
							userInfo.setSex(sex);
							strLocation = tvLocation.getText().toString();
							if(!StringUtils.isEmpty(strLocation))
								userInfo.setAddress(strLocation);
							strInfo = tvInfo.getText().toString();
							if(strInfo != null)
								userInfo.setInfo(strInfo);
							mApplication.dbCache.saveUserInfo(userInfo);
						}
						else
							ToastUtil.TextToast(UserInfoActivity.this, "设置失败", Toast.LENGTH_SHORT, Gravity.CENTER);
					}
					else{
						ToastUtil.TextToast(UserInfoActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					ToastUtil.TextToast(UserInfoActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
				}
				showloading(false);
			};
		}.execute();
	}

	private void addPicture(Bitmap result){
		try{
			if(result != null){
				result.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(imageDir + currentImagePath));
				mApplication.imageLoader.displayImage(Uri.fromFile(new File(imageDir + currentImagePath)).toString(), ivHeadPhoto, optionsPhoto);
				iconUrl = Uri.fromFile(new File(imageDir + currentImagePath)).toString();
				if(!StringUtils.isEmpty(iconUrl))
					userInfo.setIconUrl(iconUrl);
				mApplication.dbCache.saveUserInfo(userInfo);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int options = 80;
				result.compress(Bitmap.CompressFormat.JPEG, options, baos);
				final byte[] content = baos.toByteArray();
				if(content.length > 0){
					uploadAvatar(content);
				}
				if(!result.isRecycled()){
					result.recycle();
					result = null;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

    private void uploadAvatar(final byte[] content)
    {
    	new AsyncTask<Void, Void, UploadCertImgResponse>(){
    		private String errortext = "网络中断，请稍后重试";
    		
    		protected void onPreExecute() {
				showloading(true);
			}

			@Override
			protected UploadCertImgResponse doInBackground(Void... params) {
				try{
					UploadCertImgRequest request = new UploadCertImgRequest();
					request.setParams(4);
					HashMap<String, FileItem> fileParams = new HashMap<String, FileItem>();
					fileParams.put("file", new FileItem("" + System.currentTimeMillis(), content));
					return mApplication.client.executePostWithParams(request, fileParams);
				}
				catch(Exception e){
					errortext = e.toString();
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(UploadCertImgResponse result) {
    			showloading(false);
    			if(result != null){
    				if(result.getCode() != null && result.getCode() == 0){
    					iconUrl = result.getData().getValue();
    					addUserInfo();
    				}
    				else
    					ToastUtil.TextToast(UserInfoActivity.this, result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
    			}
    			else
    				ToastUtil.TextToast(UserInfoActivity.this, errortext, Toast.LENGTH_SHORT, Gravity.CENTER);
    		}
    		
    	}.execute();
    }

}