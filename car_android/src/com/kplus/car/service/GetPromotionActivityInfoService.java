package com.kplus.car.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.kplus.car.KplusApplication;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.model.ImageInfo;
import com.kplus.car.model.response.GetImgItemResponse;
import com.kplus.car.model.response.request.GetImageItemRequest;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

public class GetPromotionActivityInfoService extends IntentService{
	private static final int TYPE_BODY = 1;
	private static final int TYPE_VEHICLE_HEAD = 2;
	
	private KplusApplication mApplication;
	private SharedPreferences sp;
	
	public GetPromotionActivityInfoService(){
		super("GetPromotionActivityInfoService");
	}

	public GetPromotionActivityInfoService(String name) {
		super(name);
	}
	
	@Override
	public void onCreate() {
		mApplication = (KplusApplication) getApplication();
		sp = getSharedPreferences("kplussp", MODE_PRIVATE);
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GetImageItemRequest request = new GetImageItemRequest();
		request.setParams("android", mApplication.getId(), mApplication.getUserId());
		GetImgItemResponse response = null;
		try{
			response = mApplication.client.execute(request);
		}
		catch(Exception e){
			e.printStackTrace();
			response = null;
		}
		if (response != null && response.getCode() != null && response.getCode() == 0) {			
			try{
				List<ImageInfo> body = response.getData().getBody();
				ImageInfo bodySaved = mApplication.dbCache.getBodyImageInfo();
				if(body != null && !body.isEmpty()){
					if(bodySaved == null || (!body.get(0).getImgUrl().equals(bodySaved.getImgUrl()))){
						if(bodySaved != null){
							File file = new File(bodySaved.getImagePath());
							if(file.exists())
								file.delete();
						}
						saveImage(body,TYPE_BODY);
						LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("com.kplus.car.homeactivity.body.loaded"));
					}
					else if(bodySaved != null && body.get(0).getValid().booleanValue() != bodySaved.getValid().booleanValue()){
						bodySaved.setValid(body.get(0).getValid());
						mApplication.dbCache.saveBodyImage(bodySaved);
						LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("com.kplus.car.homeactivity.body.loaded"));
					}
				}
				else{
					if(bodySaved != null){
						mApplication.dbCache.clearBodyImages();
						File file = new File(bodySaved.getImagePath());
						if(file.exists())
							file.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try{
				List<ImageInfo> vehicleHead = response.getData().getVehicleHead();
				ImageInfo vehicleHeadSaved = mApplication.dbCache.getVehicleHeadImageInfo();
				if(vehicleHead != null && !vehicleHead.isEmpty()){
					if(vehicleHeadSaved == null || (!vehicleHead.get(0).getImgUrl().equals(vehicleHeadSaved.getImgUrl()))){
						if(vehicleHeadSaved != null){
							mApplication.dbCache.clearVehicleHeadImages();
							File file = new File(vehicleHeadSaved.getImagePath());
							if(file.exists())
								file.delete();
						}
						sp.edit().putBoolean("showVehicleHeadActivityView", true).commit();
						saveImage(vehicleHead,TYPE_VEHICLE_HEAD);
					}
					else if(vehicleHeadSaved != null && vehicleHead.get(0).getValid().booleanValue() != vehicleHeadSaved.getValid().booleanValue()){
						vehicleHeadSaved.setValid(vehicleHead.get(0).getValid());
						mApplication.dbCache.saveVehicleHeadImage(vehicleHeadSaved);
					}
				}
				else{
					if(vehicleHeadSaved != null){
						mApplication.dbCache.clearVehicleHeadImages();
						File file = new File(vehicleHeadSaved.getImagePath());
						if(file.exists())
							file.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void saveImage(List<ImageInfo> list,int type){
		String dirPath = null;
		if(type == TYPE_BODY)
			dirPath = FileUtil.getParentDirectory() + "body/";
		else if(type == TYPE_VEHICLE_HEAD)
			dirPath = FileUtil.getParentDirectory() + "vehiclehead/";
		File dir = new File(dirPath);
		if(!dir.exists() || !dir.isDirectory())
			dir.mkdirs();
		for(ImageInfo info : list){
			try {
				String filePath = dirPath + info.getImgUrl().substring(info.getImgUrl().lastIndexOf("/")+1);
				FileOutputStream fos = new FileOutputStream(new File(filePath),false);
				InputStream is = new URL(info.getImgUrl()).openStream();
				BufferedInputStream bin = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int data = -1;
				while ((data = bin.read(buffer)) != -1) {
					fos.write(buffer, 0, data);
				}
				fos.close();
				is.close();
				info.setImagePath(filePath);
				if(type == TYPE_BODY)
					mApplication.dbCache.saveBodyImage(info);
				else if(type == TYPE_VEHICLE_HEAD)
					mApplication.dbCache.saveVehicleHeadImage(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
