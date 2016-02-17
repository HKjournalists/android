package com.kplus.car.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kplus.car.KplusConstants;
import com.kplus.car.R;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.model.OrderDetail;
import com.kplus.car.model.OrderInfo;
import com.kplus.car.model.json.GetOrderDetailJson;
import com.kplus.car.model.response.GetOrderDetailResponse;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.request.GetOrderDetailRequest;
import com.kplus.car.model.response.request.OrderInfoAddRequest;
import com.kplus.car.util.BMapUtil;
import com.kplus.car.util.FileItem;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;
import com.kplus.car.widget.imageview.GestureImageView;

public class OrderInfoAddActivity extends BaseActivity implements OnClickListener {
	public static final int GET_IMAGE_RESULT = 11;
	public static final int CAMERA_RESULT = 12;
	public static final int OWNER_NAME_RESULT = 13;
	private static final int TYPE_OWNER_NAME = 0;

	private View leftView;
	private ImageView ivLeft;
	private TextView tvTitle;
	private View rightView;
	private ImageView ivRight;
	private TextView tvRight;
	private View ownerLayout;
	private TextView ownerText;
	private View add_driving_layout;
	private View add_driver_layout;
	private View add_card_layout;
	private View cardLayout;
	private View cardLayout2;
	private ImageView drivingImage;
	private ImageView driverImage;
	private ImageView cardImage;
	private ImageView cardImage2;
	private TextView drivingImageTip, driverImageTip;	
	private View rlSelectPicture;
	private Button btCarema;
	private Button btAlbum;
	private Button btPreview;
	private Button btCancel;
	protected GestureImageView view;
	private View rlVerify;

	private long orderId;
	private String vehicleNum;
	private String orderInfoFlag;
	private boolean isAdd = true;
	private String drivingImageUrl;
	private String driverImageUrl;
	private String cardImageUrl;
	private String cardImageUrl2;
	private Bitmap bitmap;
	private int type;
	private String parentDir;
	private String currentImageName;
	private int nCount = 0;
	private int nTotal = 0;

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_order_info_add);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);

		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_but_icons_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("证件管理");
		rightView = findViewById(R.id.rightView);
		ivRight = (ImageView) findViewById(R.id.ivRight);
		ivRight.setVisibility(View.GONE);
		tvRight = (TextView) findViewById(R.id.tvRight);
		tvRight.setText("提交");
		tvRight.setVisibility(View.VISIBLE);
		ownerLayout = findViewById(R.id.order_info_add_ownerName_layout);
		ownerText = (TextView) findViewById(R.id.order_info_add_ownerName);
		add_driving_layout = findViewById(R.id.add_driving_layout);
		add_driver_layout = findViewById(R.id.add_driver_layout);
		add_card_layout = findViewById(R.id.add_card_layout);
		cardLayout = findViewById(R.id.order_info_add_card_layout);
		cardLayout2 = findViewById(R.id.order_info_add_card_layout2);
		drivingImage = (ImageView) findViewById(R.id.order_info_add_driving);
		driverImage = (ImageView) findViewById(R.id.order_info_add_driver);
		cardImage = (ImageView) findViewById(R.id.order_info_add_card);
		cardImage2 = (ImageView) findViewById(R.id.order_info_add_card2);
		drivingImageTip = (TextView) findViewById(R.id.order_info_add_driving_tip);
		driverImageTip = (TextView) findViewById(R.id.order_info_add_driver_tip);
		rlSelectPicture = findViewById(R.id.rlSelectPicture);
		btCarema = (Button) findViewById(R.id.btCarema);
		btAlbum = (Button) findViewById(R.id.btAlbum);
		btPreview = (Button) findViewById(R.id.btPreview);
		btCancel = (Button) findViewById(R.id.btCancelPicture);
		rlVerify = findViewById(R.id.rlVerify);
	}

	protected void loadData() {
		orderId = getIntent().getLongExtra("orderId", 0);
		isAdd = getIntent().getBooleanExtra("isAdd", false);
		parentDir = FileUtil.getParentDirectory();
		getOrderDetail();
		if(isAdd)
			rlVerify.setVisibility(View.GONE);
		else
			rlVerify.setVisibility(View.VISIBLE);
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		ownerLayout.setOnClickListener(this);
		add_driving_layout.setOnClickListener(this);
		add_driver_layout.setOnClickListener(this);
		cardLayout.setOnClickListener(this);
		cardLayout2.setOnClickListener(this);
		drivingImageTip.setOnClickListener(this);
		driverImageTip.setOnClickListener(this);
		btCarema.setOnClickListener(this);
		btAlbum.setOnClickListener(this);
		btPreview.setOnClickListener(this);
		btCancel.setOnClickListener(this);
		rightView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(leftView)) {
			finish();
		} else if (v.equals(ownerLayout)) {
			type = TYPE_OWNER_NAME;
			Intent intent = new Intent(OrderInfoAddActivity.this, AlertDialogActivity.class);
			intent.putExtra("alertType", KplusConstants.ALERT_INPUT_VEHICLE_OWNER_NAME);
			startActivityForResult(intent, OWNER_NAME_RESULT);
		} else if (v.equals(add_driving_layout)) {
			type = KplusConstants.UPLOAD_DRIVING;
			getImage(drivingImageUrl);
		} else if (v.equals(add_driver_layout)) {
			type = KplusConstants.UPLOAD_DRIVER;
			getImage(driverImageUrl);
		} else if (v.equals(cardLayout)) {
			type = KplusConstants.UPLOAD_CARD;
			getImage(cardImageUrl);
		} else if (v.equals(cardLayout2)) {
			type = KplusConstants.UPLOAD_CARD2;
			getImage(cardImageUrl2);
		} else if (v.equals(driverImageTip)) {
			Intent intent = new Intent(OrderInfoAddActivity.this, CertificateActivity.class);
			intent.putExtra("flag", "driverImageTip");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_from_right, 0);
		}else if (v.equals(drivingImageTip)) {
			Intent intent = new Intent(OrderInfoAddActivity.this, CertificateActivity.class);
			intent.putExtra("flag", "drivingImageTip");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_from_right, 0);
		}
		else if(v.equals(btCarema)){
			try {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				currentImageName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
				File mPhotoFile = new File(parentDir, currentImageName);
				if (!mPhotoFile.exists()) {
					mPhotoFile.createNewFile();
				}
				Uri mPhotoOnSDCardUri = Uri.fromFile(mPhotoFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoOnSDCardUri);
				startActivityForResult(intent, CAMERA_RESULT);
				rlSelectPicture.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(v.equals(btAlbum)){
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	        photoPickerIntent.setType("image/*");
	        startActivityForResult(photoPickerIntent, GET_IMAGE_RESULT);
			rlSelectPicture.setVisibility(View.GONE);
		}
		else if(v.equals(btPreview)){
			Intent intent = new Intent(OrderInfoAddActivity.this, PreeViewActivity.class);	
			if(type == KplusConstants.UPLOAD_DRIVING)
				intent.putExtra("imageUrl", drivingImageUrl);
			else if(type == KplusConstants.UPLOAD_DRIVER)
				intent.putExtra("imageUrl", driverImageUrl);
			else if(type == KplusConstants.UPLOAD_CARD)
				intent.putExtra("imageUrl", cardImageUrl);
			else if(type == KplusConstants.UPLOAD_CARD2)
				intent.putExtra("imageUrl", cardImageUrl2);
			startActivity(intent);
			rlSelectPicture.setVisibility(View.GONE);
		}
		else if(v.equals(btCancel)){
			if(rlSelectPicture.getVisibility() == View.VISIBLE)
				rlSelectPicture.setVisibility(View.GONE);
		}
		else if(v.equals(rightView)){
			submitInfos();
		}
	}

	private void infoAdd(final String fileUrl, final int certType, final String ownerName, final byte[] content) {
		if(nCount == 0)
			showloading(true);
		new AsyncTask<Object, Object, GetResultResponse>() {
			@Override
			protected GetResultResponse doInBackground(Object... params) {
				OrderInfoAddRequest request = new OrderInfoAddRequest();
				request.setParams(orderId, fileUrl, certType, ownerName);
				try {
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(GetResultResponse result) {
				if (result != null && result.getCode() != null && result.getCode() == 0) {
					if (result.getData().getResult()) {
						nCount++;
						if(nCount == nTotal){
							Toast.makeText(OrderInfoAddActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
							showloading(false);
							setResult(RESULT_OK);
							finish();
						}
					}
					else {
						showloading(false);
						Toast.makeText(OrderInfoAddActivity.this, !StringUtils.isEmpty(result.getMsg()) ? result.getMsg() : "提交失败！", Toast.LENGTH_SHORT).show();
					}
				} else {
					showloading(false);
					Toast.makeText(OrderInfoAddActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
				}
			}

		}.execute();
	}

	private void getImage(final String imageUrl) {
		if (imageUrl != null) {
			if(rlSelectPicture.getVisibility() == View.GONE){
				rlSelectPicture.setVisibility(View.VISIBLE);
				btPreview.setVisibility(View.VISIBLE);
			}
		} else {
			if(rlSelectPicture.getVisibility() == View.GONE){
				rlSelectPicture.setVisibility(View.VISIBLE);
				btPreview.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GET_IMAGE_RESULT:
				if (resultCode == RESULT_OK){
					try{
						if(data != null){
							Uri selectedImage = data.getData();
							String[] filePathColumns={MediaStore.Images.Media.DATA};
							Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null,null, null);
							c.moveToFirst();
							int columnIndex = c.getColumnIndex(filePathColumns[0]);
							final String picturePath= c.getString(columnIndex);
							c.close();
							if(bitmap != null && !bitmap.isRecycled())
								bitmap.recycle();
							bitmap = BMapUtil.getBitmapFromPath(picturePath, 500);
							bitmap = BMapUtil.rotateByExifInfo(bitmap, picturePath);
							if (bitmap != null) {
								switchUpload(bitmap);
							} else {
								ToastUtil.TextToast(this, "获取图片失败，请重试！", Toast.LENGTH_SHORT, Gravity.CENTER);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}				
				}
				break;
			case CAMERA_RESULT:
				if(resultCode == RESULT_OK){
					if(bitmap != null && !bitmap.isRecycled())
						bitmap.recycle();
					bitmap = BMapUtil.getBitmapFromPath(parentDir + currentImageName, 500);
					bitmap = BMapUtil.rotateByExifInfo(bitmap, parentDir + currentImageName);
					if (bitmap != null) {
						switchUpload(bitmap);
					}
				}
				break;
			case KplusConstants.UPLOAD_DRIVER:
			case KplusConstants.UPLOAD_DRIVING:
			case KplusConstants.UPLOAD_CARD:
			case KplusConstants.UPLOAD_CARD2:
				if(data != null && data.getStringExtra("imageUrl") != null){
					updateImageView(requestCode, data.getStringExtra("imageUrl"));
				}
				break;
			case OWNER_NAME_RESULT:
				if(data != null && data.getStringExtra("ownerName") != null){
					ownerText.setText(data.getStringExtra("ownerName"));
				}
				break;
			default:
				break;
			}
		}
	}

	private void updateImageView(int type, String imageUrl){
		switch(type){
			case KplusConstants.UPLOAD_DRIVER:
				driverImageUrl = imageUrl;
				setImageView(driverImage, driverImageUrl);
				break;
			case KplusConstants.UPLOAD_DRIVING:
				drivingImageUrl = imageUrl;
				setImageView(drivingImage, drivingImageUrl);
				break;
			case KplusConstants.UPLOAD_CARD:
				cardImageUrl = imageUrl;
				setImageView(cardImage, cardImageUrl);
				break;
			case KplusConstants.UPLOAD_CARD2:
				cardImageUrl2 = imageUrl;
				setImageView(cardImage2, cardImageUrl2);
				break;
			default:
				break;
		}
	}

	private void switchUpload(Bitmap bitmap) {
		byte[] content = BMapUtil.getContentFromBitmap(bitmap);
		Intent intent = new Intent(OrderInfoAddActivity.this, UploadProgressActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("content", content);
		intent.putExtra("orderId", orderId);
		startActivityForResult(intent, type);
	}
	
	@Override
	protected void onDestroy()
	{
		if(bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
		bitmap = null;
		super.onDestroy();
	}
	
	private void getOrderDetail(){
		new AsyncTask<Void, Void, GetOrderDetailResponse>() {
			protected void onPreExecute() {
				showloading(true);
			}
			
			protected GetOrderDetailResponse doInBackground(Void... params) {
				GetOrderDetailRequest request = new GetOrderDetailRequest();
				request.setParams(orderId);
				try {
					Thread.sleep(3000);
					return mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(GetOrderDetailResponse result) {
				showloading(false);
				if (result != null) {
					if (result.getCode() != null && result.getCode() == 0) {
						setData(result.getData());
					} else {
						Toast.makeText(OrderInfoAddActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
						finish();
					}
				} else {
					Toast.makeText(OrderInfoAddActivity.this, "网络中断，请稍候重试", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}.execute();
	}
	
	private void setData(GetOrderDetailJson json){
		try{
			OrderDetail od = json.getOrderDetail();
			vehicleNum = od.getVehicleNum();
			orderInfoFlag = od.getOrderInfoFlag();
			if (!StringUtils.isEmpty(orderInfoFlag) && orderInfoFlag.length() >= 4){
				OrderInfo oi = json.getUserInfo();
				if (orderInfoFlag.charAt(0) == '1') {
					nTotal++;
					add_driving_layout.setVisibility(View.VISIBLE);
					drivingImageUrl = oi.getDrivingLicenseImageUrl();
					setImageView(drivingImage, drivingImageUrl);
				}
				if (orderInfoFlag.charAt(1) == '1') {
					nTotal++;
					add_driver_layout.setVisibility(View.VISIBLE);
					driverImageUrl = oi.getDriverLicenseImageUrl();
					setImageView(driverImage, driverImageUrl);
				}
				if (orderInfoFlag.charAt(2) == '1') {
					nTotal++;
					nTotal++;
					add_card_layout.setVisibility(View.VISIBLE);
					cardImageUrl = oi.getCardImageUrl();
					setImageView(cardImage, cardImageUrl);
					cardImageUrl2 = oi.getCardImageUrl2();
					setImageView(cardImage2, cardImageUrl2);
				}
				if (orderInfoFlag.charAt(3) == '1') {
					nTotal++;
					ownerLayout.setVisibility(View.VISIBLE);
					String ownerName = oi.getOwnerName();
					if(!StringUtils.isEmpty(ownerName))
						ownerText.setText(ownerName);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(OrderInfoAddActivity.this, "信息获取失败，请稍候重试", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void submitInfos(){
		nCount = 0;
		if (!StringUtils.isEmpty(orderInfoFlag) && orderInfoFlag.length() >= 4){
			if (orderInfoFlag.charAt(0) == '1') {
				if(StringUtils.isEmpty(drivingImageUrl)){
					ToastUtil.TextToast(OrderInfoAddActivity.this, "请上传行驶证图片", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			if (orderInfoFlag.charAt(1) == '1') {
				if(StringUtils.isEmpty(driverImageUrl)){
					ToastUtil.TextToast(OrderInfoAddActivity.this, "请上传驾驶证图片", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			if (orderInfoFlag.charAt(2) == '1') {
				if(StringUtils.isEmpty(cardImageUrl)){
					ToastUtil.TextToast(OrderInfoAddActivity.this, "请上传身份证正面图片", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
				if(StringUtils.isEmpty(cardImageUrl2)){
					ToastUtil.TextToast(OrderInfoAddActivity.this, "请上传身份证背面图片", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			if (orderInfoFlag.charAt(3) == '1') {
				if(StringUtils.isEmpty(ownerText.getText().toString())){
					ToastUtil.TextToast(OrderInfoAddActivity.this, "请上输入车主姓名", Toast.LENGTH_SHORT, Gravity.CENTER);
					return;
				}
			}
			if (orderInfoFlag.charAt(0) == '1')
				infoAdd(drivingImageUrl, KplusConstants.UPLOAD_DRIVING, null, null);
			if (orderInfoFlag.charAt(1) == '1')
				infoAdd(driverImageUrl, KplusConstants.UPLOAD_DRIVER, null, null);
			if (orderInfoFlag.charAt(2) == '1') {
				infoAdd(cardImageUrl, KplusConstants.UPLOAD_CARD, null, null);
				infoAdd(cardImageUrl2, KplusConstants.UPLOAD_CARD2, null, null);
			}
			if (orderInfoFlag.charAt(3) == '1')
				infoAdd(null, TYPE_OWNER_NAME, ownerText.getText().toString(), null);
//			setResult(RESULT_OK);
//			finish();
		}
	}

}
