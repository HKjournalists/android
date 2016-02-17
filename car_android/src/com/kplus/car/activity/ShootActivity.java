package com.kplus.car.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kplus.car.KplusApplication;
import com.kplus.car.R;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.util.EventAnalysisUtil;
import com.kplus.car.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShootActivity extends Activity implements SurfaceHolder.Callback, OnClickListener
{
	private static final int ENTER_ALBUM = 1;
	private SurfaceView surface;
	private ImageView ivTakePhoto, ivBack, ivFlashSwitch;
	private View cutView;
	private TextView tvAlbum;
	private SurfaceHolder holder;
	private Camera camera;	
	private String filepath = "";//照片保存路径
	private KplusApplication mApplication;
	private SensorManager sensorManager;
	private Sensor orientationSensor;
	private SensorEventListener sensorEventListener;
	private List<float[]> mlist;
	private double move_Difference;
	private final int ListMaxLen = 3;
	private final int sampleInteval = 200;
	private long lastTime;
	private int sampleCount = 0;
//	private final float moveDifference = 0.08f;
	private final float moveDifference = 0.5f;
	private final float moveDifferencemin = 0.001f;
	private float yAvg,zAvg;
	private CameraInfo cameraInfo;
	private int screenWidth, screenHeight;
	private int flashFlag = 0;
	private String vehicleNumber;
	private int focousFlag = 0;//0 not focoused 1 focousing 2 focoused
	private String uploadLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.daze_camera);
		surface = (SurfaceView) findViewById(R.id.surfaceView);
		holder = surface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(this);
		ivTakePhoto = (ImageView) findViewById(R.id.ivTakePhoto);
		ivTakePhoto.setOnClickListener(this);
		ivFlashSwitch = (ImageView)findViewById(R.id.ivFlashSwitch);
		ivFlashSwitch.setOnClickListener(this);
		cutView = findViewById(R.id.cutView);
		tvAlbum = (TextView) findViewById(R.id.tvAlbum);
		tvAlbum.setOnClickListener(this);
		mApplication = (KplusApplication) getApplication();
		resizeCutView();
		vehicleNumber = getIntent().getStringExtra("vehicleNumber");
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if(orientationSensor != null){
//			ivTakePhoto.setEnabled(false);
			sensorEventListener = new SensorEventListener() {			
				@Override
				public void onSensorChanged(SensorEvent arg0) {
					if(arg0.sensor.getType() == Sensor.TYPE_ORIENTATION){
						long currentTime = System.currentTimeMillis();
						if(lastTime == 0 || currentTime - lastTime > sampleInteval){
							lastTime = currentTime;
							float x = arg0.values[SensorManager.DATA_X];
							float y = arg0.values[SensorManager.DATA_Y];
							float z = arg0.values[SensorManager.DATA_Z];
//							System.out.println("x===>" +x + "   y===>" + y + "   z===>" + z);
							double _moveDifference = getStableFloat(x, y, z);
							if(sampleCount < 3){
								sampleCount++;
							}
//							if(Math.abs(yAvg) < 10 && Math.abs(zAvg) < 10 && _moveDifference <= moveDifference && _moveDifference >= moveDifferencemin)
							if(_moveDifference <= moveDifference && _moveDifference >= moveDifferencemin){
								if(sampleCount == 3){
//									ivTakePhoto.setEnabled(true);
									if(focousFlag == 0){
										focousFlag = 1;
										String fm = camera.getParameters().getFocusMode();
										if(fm != null && fm.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
											try{
												camera.autoFocus(new AutoFocusCallback()
												{
													@Override
													public void onAutoFocus(boolean success, Camera camera)
													{
														if(success)
															focousFlag = 2;
													}
												});
											}catch(Exception e){
												e.printStackTrace();
											}
										}
									}
								}
							}
							else{
								sampleCount = 0;
								focousFlag = 0;
//								ivTakePhoto.setEnabled(false);
							}
						}
					}
				}
				
				@Override
				public void onAccuracyChanged(Sensor arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			};
		}
		else
			ivTakePhoto.setEnabled(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if(camera == null) {
			try{
				int cameraNumber = Camera.getNumberOfCameras();
				if(cameraNumber == 0){
					ToastUtil.TextToast(ShootActivity.this, "该手机不支持摄像功能", Toast.LENGTH_SHORT, Gravity.CENTER);
					holder = null;
					surface = null;
					finish();
				}
				else{
					Camera frontCamera = null;
					CameraInfo frontCameraInfo = null;
					for(int i=0;i<cameraNumber;i++){
						CameraInfo _cameraInfo = new CameraInfo();
						Camera.getCameraInfo(i, _cameraInfo);
						if(_cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK){
							camera = Camera.open(i);
							cameraInfo = _cameraInfo;
							break;
						}
						else{
							frontCamera = Camera.open(i);
							frontCameraInfo = _cameraInfo;
						}
					}
					if(camera == null){
							camera = frontCamera;
							cameraInfo = frontCameraInfo;
					}
				}
				Parameters params = camera.getParameters();
				List<Size> pss = params.getSupportedPictureSizes();
				if(pss != null && !pss.isEmpty()){
					float fRatio = ((float)screenWidth)/screenHeight;
					Size sizeTemp = null;
					for(Size ps : pss){
						if(ps.width > 1024){
							if(sizeTemp == null)						
								sizeTemp = ps;
							else{
								float fRatioPs = ((float)ps.width)/ps.height;
								float fRatioTemp = ((float)sizeTemp.width)/sizeTemp.height;
								if(Math.abs(fRatioPs - fRatio) < Math.abs(fRatioTemp - fRatio))
									sizeTemp = ps;
							}
						}
					}
					if(sizeTemp != null){
						params.setPictureSize(sizeTemp.width, sizeTemp.height);
						camera.setParameters(params);
					}
				}
				List<Size> pps = params.getSupportedPreviewSizes();
				if(pps != null && !pps.isEmpty()){
					float fRatio = ((float)screenWidth)/screenHeight;
					Size sizeTemp = null;
					for(Size ps : pps){
						if(ps.width > 1024){
							if(sizeTemp == null)						
								sizeTemp = ps;
							else{
								float fRatioPs = ((float)ps.width)/ps.height;
								float fRatioTemp = ((float)sizeTemp.width)/sizeTemp.height;
								if(Math.abs(fRatioPs - fRatio) < Math.abs(fRatioTemp - fRatio))
									sizeTemp = ps;
							}
						}
					}
					if(sizeTemp != null){
						params.setPreviewSize(sizeTemp.width, sizeTemp.height);
						camera.setParameters(params);
					}
				}
				params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				camera.setParameters(params);
				camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
				camera.setDisplayOrientation(getPreviewDegree(ShootActivity.this));
//				PreviewCallback pre = new PreviewCallback() {					
//					@Override
//					public void onPreviewFrame(byte[] data, Camera camera) {
//						// TODO Auto-generated method stub
//						
//					}
//				};
//				camera.setPreviewCallback(pre);
				camera.startPreview();//开始预览
			}catch(Exception e){
				finish();					
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		if (camera != null){
			try {
				//当surfaceview关闭时，关闭预览并释放资源
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
				holder = null;
				surface = null;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId()){
			case R.id.ivBack:
				setResult(RESULT_CANCELED);
				finish();
			case R.id.ivTakePhoto:
				uploadLabel = "uploadCert_camera";
//				ivTakePhoto.setEnabled(false);
				try{
					if(focousFlag == 0 || focousFlag == 2){
						String fm = camera.getParameters().getFocusMode();
						if(fm != null && fm.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
							focousFlag = 1;
							camera.autoFocus(new AutoFocusCallback()
							{
								@Override
								public void onAutoFocus(boolean success, Camera camera)
								{
									if(success){
										focousFlag = 2;
										camera.takePicture(null, null, jpeg);//将拍摄到的照片给自定义的对象
									}
								}
							});
						}
						else
							camera.takePicture(null, null, jpeg);
					}
				}catch(Exception e){
					e.printStackTrace();
					if(camera != null){
						camera.stopPreview();
						camera.startPreview();
					}
					if(ivTakePhoto != null)
						ivTakePhoto.setEnabled(true);
				}
				break;
			case R.id.ivFlashSwitch:
				if(camera != null){
					Parameters params = camera.getParameters();
					if(flashFlag == 0){
						flashFlag = 1;
						ivFlashSwitch.setImageResource(R.drawable.daze_light_on);
						params.setFlashMode(Parameters.FLASH_MODE_ON);
					}
					else{
						flashFlag = 0;
						ivFlashSwitch.setImageResource(R.drawable.daze_light_off);
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
					}
					camera.setParameters(params);
				}
				break;
			case R.id.tvAlbum:
				uploadLabel = "uploadCert_album";
				Intent picture = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(picture, ENTER_ALBUM);
				break;
			default:
				break;
		}
		
	}
	
	PictureCallback jpeg = new PictureCallback()
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera)
		{
			Bitmap bitmap = null;
			Bitmap result = null;
			try{
				camera.stopPreview();//关闭预览 处理数据
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				opts.inPurgeable = true;
				opts.inInputShareable = true;
				// opts.inNativeAlloc = true;
				// //属性设置为true，可以不把使用的内存算到VM里。SDK默认不可设置这个变量，只能用反射设置。
				try {
					Field field = BitmapFactory.Options.class.getDeclaredField("inNativeAlloc");
					field.setBoolean(opts, true);
				} catch (Exception e) {
					Log.i("ShootActivity", "Exception inNativeAlloc");
				}
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				opts.inSampleSize = calculateInSampleSize(opts, 500 );
				opts.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				int bitmapWidth = opts.outWidth;
				int bitmapHeight = opts.outHeight;
				int cutWidthTemp = cutWidth*bitmapWidth/screenWidth;
				int cutHeightTemp = cutWidthTemp*30/43;
				result = Bitmap.createBitmap(bitmap, dip2px(ShootActivity.this, 60)*bitmapWidth/screenWidth, (bitmapHeight - cutHeightTemp)/2, cutWidthTemp, cutHeightTemp);
				bitmap.recycle();
				bitmap = null;
				//自定义文件保存路径  以拍摄时间区分命名
				String dirPath =FileUtil.getParentDirectory();
				String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +  ".jpg";
                filepath = dirPath + filename;
                File file = new File(filepath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                result.compress(Bitmap.CompressFormat.JPEG, 80, bos);//将图片压缩的流里面
                bos.flush();// 刷新此缓冲区的输出流
                bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
                camera.startPreview();//数据处理完后继续开始预览
                result.recycle();//回收bitmap空间
                ivTakePhoto.setEnabled(true);
//                Intent intent = getIntent().putExtra("filename", filename);
//                setResult(RESULT_OK, intent);
                Intent intent = new Intent(ShootActivity.this, UploadLicenceActivity.class);
                intent.putExtra("imagePath", filepath);
                intent.putExtra("vehicleNumber", vehicleNumber);
                intent.putExtra("uploadLabel", uploadLabel);
                startActivity(intent);
                finish();
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(bitmap != null && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap = null;
				}
				if(result != null && !result.isRecycled()){
					result.recycle();
					result = null;
				}
			}
		}
	};
	
    private int getPreviewDegree(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();  
        int degree = 0;  
        switch (rotation) {  
        case Surface.ROTATION_0:  
            degree = 0;  
            break;  
        case Surface.ROTATION_90:  
            degree = 90;  
            break;  
        case Surface.ROTATION_180:  
            degree = 270;  
            break;  
        case Surface.ROTATION_270:  
            degree = 180;  
            break;  
        }
        int result;
		if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
			result = (cameraInfo.orientation + degree) % 360;
		else
			result = (cameraInfo.orientation - degree + 360) % 360;
        return result;  
    }  
    
    @Override
	protected void onPause() {
		super.onPause();
		EventAnalysisUtil.onPause(this);
		if(orientationSensor != null){
			sensorManager.unregisterListener(sensorEventListener);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventAnalysisUtil.onResume(this);
		if(orientationSensor != null){
			sensorManager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == ENTER_ALBUM){
			if (resultCode == RESULT_OK){
				try{
					if(data != null){
						Uri selectedImage = data.getData();
						String[] filePathColumns={MediaStore.Images.Media.DATA};
						Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null,null, null);
						c.moveToFirst();
						int columnIndex = c.getColumnIndex(filePathColumns[0]);
						String picturePath= c.getString(columnIndex);
						c.close();
						Intent intent = new Intent(ShootActivity.this, UploadLicenceActivity.class);
						intent.putExtra("imagePath", picturePath);
		                intent.putExtra("vehicleNumber", vehicleNumber);
		                intent.putExtra("uploadLabel", uploadLabel);
		                startActivity(intent);
		                finish();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options, int reqSize) {
		// 源图片的宽度
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;
		if (width*height > reqSize*1024) {
			// 计算出实际宽度和目标宽度的比率
			int widthRatio = Math.round((float)Math.sqrt((double) (width * height) / (double) (reqSize * 1024)));
			inSampleSize = widthRatio;
		}
		return inSampleSize;
	}
	
	private float getStableFloat(float x, float y, float z) {
		if(mlist == null)
			mlist = new ArrayList<float[]>();
		float move_Difference = 0.0f;
		float[] floatdata = { x, y, z };
		if (mlist.size() < ListMaxLen) {
			mlist.add(floatdata);
		} else {
			mlist.remove(0);
			mlist.add(floatdata);
		}
		if (mlist.size() < ListMaxLen) {
			return 0.1f;
		}
		float sumx = 0;
		float sumy = 0;
		float sumz = 0;
		int len = mlist.size();
		for (int i = 0; i < len; i++) {
			float[] dd = (float[]) mlist.get(i);
			sumx += dd[0];
			sumy += dd[1];
			sumz += dd[2];
		}
		float avgx = sumx / len;
		float avgy = sumy / len;
		float avgz = sumz / len;
		yAvg = avgy;
		zAvg = avgz;
		for (int i = 0; i < len; i++) {
			float[] dd = (float[]) mlist.get(i);
			move_Difference += (dd[0] - avgx) * (dd[0] - avgx) + (dd[1] - avgy)
					* (dd[1] - avgy) + (dd[2] - avgz) * (dd[2] - avgz);
		}
		return move_Difference/len;
	}
	private int cutWidth;
	private int cutHeight;
	private void resizeCutView(){
		RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams)cutView.getLayoutParams();
		if(screenWidth == 0)
			screenWidth = getResources().getDisplayMetrics().widthPixels;
		if(screenHeight == 0)
			screenHeight =  getResources().getDisplayMetrics().heightPixels;
		int screenWidthTemp = screenWidth - dip2px(ShootActivity.this, 148);
		int screenHeightTemo = screenHeight - dip2px(ShootActivity.this, 40);
		if(((float)screenWidthTemp)/screenHeightTemo >= (float)43/30){
			cutWidth = (int) (screenHeightTemo*43/30);
			cutHeight = screenHeightTemo;
		}
		else{
			cutWidth = screenWidthTemp;
			cutHeight = (int) (screenWidthTemp/((float)43/30));
		}
		rllp.width = cutWidth;
		rllp.height = cutHeight;
		cutView.setLayoutParams(rllp);
	}
	
	private int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
