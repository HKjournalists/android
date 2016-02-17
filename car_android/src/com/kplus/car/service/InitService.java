package com.kplus.car.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import com.kplus.car.Constants;
import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.asynctask.RestrictSaveTask;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.container.InstallContainerService;
import com.kplus.car.model.CommonDictionary;
import com.kplus.car.model.ImageInfo;
import com.kplus.car.model.RemindRestrict;
import com.kplus.car.model.UpgradeComponent;
import com.kplus.car.model.UserOpenIm;
import com.kplus.car.model.UserVehicle;
import com.kplus.car.model.json.CommonDictionaryJson;
import com.kplus.car.model.response.GetCityVehicleListResponse;
import com.kplus.car.model.response.GetCommonDictionaryResponse;
import com.kplus.car.model.response.GetImgItemResponse;
import com.kplus.car.model.response.GetRestrictNumResponse;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.model.response.GetUserOpenImResponse;
import com.kplus.car.model.response.GetVehicleModelListResponse;
import com.kplus.car.model.response.RestrictListResponse;
import com.kplus.car.model.response.RestrictSaveResponse;
import com.kplus.car.model.response.UpgradeComponentResponse;
import com.kplus.car.model.response.request.ClientLoginRequest;
import com.kplus.car.model.response.request.GetCityVehicleListRequest;
import com.kplus.car.model.response.request.GetCommonDictionaryRequest;
import com.kplus.car.model.response.request.GetImageItemRequest;
import com.kplus.car.model.response.request.GetRestrictNumRequest;
import com.kplus.car.model.response.request.GetUserOpenImRequest;
import com.kplus.car.model.response.request.GetVehicleModelListRequest;
import com.kplus.car.model.response.request.RestrictListRequest;
import com.kplus.car.model.response.request.UpgradeComponentRequest;
import com.kplus.car.receiver.RemindManager;
import com.kplus.car.util.MD5;
import com.kplus.car.util.StringUtils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class InitService extends IntentService
{
	private static final int TYPE_FLASH = 0;
	private static final int TYPE_HOME_PAGE = 1;
	private static final int TYPE_BODY = 2;
	private static final int TYPE_VEHICLE_HEAD = 3;
	
	private KplusApplication mApplication;
	private SharedPreferences sp;

	public InitService(){
		super("InitService");
	}

	public InitService(String name) {
		super(name);
	}

	@Override
	public void onCreate()
	{
		mApplication = (KplusApplication) getApplication();
		sp = getSharedPreferences("kplussp", MODE_PRIVATE);
		super.onCreate();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		installContainers();
		initCityData();
		getContainerUpgradeInfo();
		initTaoBao();
		login();
		getVehicleTypes();
		initBrandModelData();
		getImages();
		loadRestrictNum();
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void login() {
		try {
			ClientLoginRequest request = new ClientLoginRequest();
			String clientId = "";
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String deviceId = tm.getDeviceId();
			deviceId = (deviceId == null ? "" : deviceId);
			WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			String macAddress = wm.getConnectionInfo().getMacAddress();
			macAddress = (macAddress == null ? "" : macAddress);
			if(!StringUtils.isEmpty(deviceId) || !StringUtils.isEmpty(macAddress)){
				clientId = MD5.md5(deviceId + macAddress);
			}
//			if(!StringUtils.isEmpty(tm.getDeviceId()))
//				clientId = tm.getDeviceId();
//			else{
//				WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//				if(wm.getConnectionInfo().getMacAddress() != null){
//					clientId = wm.getConnectionInfo().getMacAddress();
//				}
//			}
			if(StringUtils.isEmpty(clientId)){
				try{
					String uuidPath = FileUtil.getAppRootPath() + "uuid.txt";
					File file = new File(uuidPath);
					if(file.exists()){
						InputStream is = new FileInputStream(file);
						int length = is.available();
						byte[] buffer = new byte[length];
						is.read(buffer);
						clientId = EncodingUtils.getString(buffer, "utf-8");
						is.close();
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			if(!StringUtils.isEmpty(clientId)){
				Log.i("InitService", "clientId = " + clientId);
				request.setParams(mApplication.getUserId(), clientId,"android",android.os.Build.VERSION.SDK,""+mApplication.apkVersionCode);
				GetResultResponse response = mApplication.client.execute(request);
				if(response != null && response.getCode() != null && response.getCode() == 0 && response.getData().getResult() != null && response.getData().getResult()){
					Log.i("InitService", "login succees");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initCityData() {
		if (mApplication.getCities().isEmpty() || (!mApplication.getCities().isEmpty()&&updateCityData()))
		{
			GetCityVehicleListRequest request = new GetCityVehicleListRequest();
			try {
				GetCityVehicleListResponse result = mApplication.client.execute(request);
				if (result != null && result.getCode() != null && result.getCode() == 0) {
					mApplication.setCities(result.getData().getList());
					mApplication.dbCache.putValue(KplusConstants.DB_KEY_CITY_DATA_TIME, ""+ System.currentTimeMillis());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean updateCityData() {
		// 显示最后更新时间
		long reftime = 0;
		try {
			String _reftime = mApplication.dbCache.getValue(KplusConstants.DB_KEY_CITY_DATA_TIME);
			reftime = _reftime == null ? 0 : Long.parseLong(_reftime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long curTime = System.currentTimeMillis();
		if (curTime - reftime > 1 * 24 * 60 * 60 * 1000)
			return true;
		return false;
	}
	
	private void initBrandModelData() {
		if(mApplication.getBrands().isEmpty() || (!mApplication.getBrands().isEmpty() && updateBrandModelData())){			
			GetVehicleModelListRequest request = new GetVehicleModelListRequest();
			try {
				GetVehicleModelListResponse result = mApplication.client.execute(request);
				if (result != null && result.getCode() != null && result.getCode() == 0) {
					mApplication.setBrands(result.getData().getList());
					mApplication.dbCache.putValue(KplusConstants.DB_KEY_BRAND_MODE_TIME, ""+ System.currentTimeMillis());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean updateBrandModelData() {
		// 显示最后更新时间
		long reftime = 0;
		try {
			String _reftime = mApplication.dbCache.getValue(KplusConstants.DB_KEY_BRAND_MODE_TIME);
			reftime = _reftime == null ? 0 : Long.parseLong(_reftime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long curTime = System.currentTimeMillis();
		if (curTime - reftime > 14 * 24 * 60 * 60 * 1000)
			return true;
		return false;
	}
	
	private void getImages() {
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
			try {
				List<ImageInfo> flash = response.getData().getFlash();
				ImageInfo flashSaved = mApplication.dbCache.getFlashImageInfo();
				if(flash != null && !flash.isEmpty()){
					if(flashSaved == null || (!flash.get(0).getImgUrl().equals(flashSaved.getImgUrl()))){
						if(flashSaved != null){
//							mApplication.dbCache.clearFlashImages();
							File file = new File(flashSaved.getImagePath());
							if(file.exists())
								file.delete();
						}
						saveImage(flash,TYPE_FLASH);
					}
				}
				else{
					if(flashSaved != null){
						mApplication.dbCache.clearFlashImages();
						File file = new File(flashSaved.getImagePath());
						if(file.exists())
							file.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try{
				List<ImageInfo> body = response.getData().getBody();
				ImageInfo bodySaved = mApplication.dbCache.getBodyImageInfo();
				if(body != null && !body.isEmpty()){
					if(bodySaved == null || (!body.get(0).getImgUrl().equals(bodySaved.getImgUrl()))){
						if(bodySaved != null){
//							mApplication.dbCache.clearBodyImages();
							File file = new File(bodySaved.getImagePath());
							if(file.exists())
								file.delete();
						}
						saveImage(body,TYPE_BODY);
						LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("com.kplus.car.homeactivity.body.loaded"));
					}
					else if(body.get(0).getValid().booleanValue() != bodySaved.getValid().booleanValue()){
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
//							mApplication.dbCache.clearVehicleHeadImages();
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
	
	private boolean isUpdate(List<ImageInfo> receive, List<ImageInfo> save){
		if(receive == null || receive.isEmpty())
			return false;
		if(save == null || save.isEmpty())
			return true;
		if(receive.size() != save.size())
			return true;
		Collections.sort(receive, new ImageinfoComparator());
		Collections.sort(save, new ImageinfoComparator());
		for(ImageInfo rI : receive){
			for(ImageInfo rS : save){
				if(!rI.getImgUrl().equals(rS.getImgUrl()))
					return true;
			}
		}
		
		return false;
	}
	
	class ImageinfoComparator implements Comparator<ImageInfo>{
		@Override
		public int compare(ImageInfo lhs, ImageInfo rhs) {
			return lhs.getOrderId() - rhs.getOrderId();
		}		
	}
	
	private void deleteImages(List<ImageInfo> list){
		if(list == null || list.isEmpty())
			return;
		for(ImageInfo info : list){
			File file = new File(info.getImagePath());
			if(file.exists())
				file.delete();
		}
	}
	
	private void saveImage(List<ImageInfo> list,int type){
		String dirPath = null;
		if(type == TYPE_FLASH)
			dirPath = FileUtil.getParentDirectory() + "flash/";
		else if(type == TYPE_HOME_PAGE)
			dirPath = FileUtil.getParentDirectory() + "homepage/";
		else if(type == TYPE_BODY)
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
				if(type == TYPE_FLASH)
					mApplication.dbCache.saveFlashImage(info);
				else if(type == TYPE_HOME_PAGE)
					mApplication.dbCache.saveHeadImage(info);
				else if(type == TYPE_BODY)
					mApplication.dbCache.saveBodyImage(info);
				else if(type == TYPE_VEHICLE_HEAD)
					mApplication.dbCache.saveVehicleHeadImage(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void installContainers(){
		try{
			String containerPath = FileUtil.getContainerParentDirectory();
			File file = new File(containerPath + "daze_service_unzip");
			File[] files = file.listFiles();
			if(files == null || files.length <= 0){
				Intent intent = new Intent(InitService.this, InstallContainerService.class);
				intent.putExtra("appName", "apps.zip");
				startService(intent);
			}
			file = new File(containerPath + "daze_service_zip");
			files = file.listFiles();
			if(files != null && files.length > 0){
				for(File f : files){
					String fileNname = f.getName();
					Intent intent = new Intent(InitService.this, InstallContainerService.class);
					intent.putExtra("appName", fileNname);
					startService(intent);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void getContainerUpgradeInfo(){
		try{
			String unzipPath = FileUtil.getContainerParentDirectory() + "daze_service_unzip";
			File file = new File(unzipPath);
			String comId = "";
			String lastModified = "";
			if(file.exists() && file.isDirectory()){
				File[] files = file.listFiles();
				if(files != null && files.length > 0){
					for(File f : files){
						String fileName = f.getName();
						if(f.isDirectory() && fileName.startsWith("10000")){
							String certtime = parseCertWithAppId(fileName);
							if(StringUtils.isEmpty(certtime))
								certtime = "0";
							if(comId.equals("")){
								comId += fileName;
								lastModified += certtime;
							}
							else{
								comId += ("," + fileName);
								lastModified += ("," + certtime);
							}
						}
					}
					if(StringUtils.isEmpty(comId) || StringUtils.isEmpty(lastModified)){
						return;
					}
					UpgradeComponentRequest request = new UpgradeComponentRequest();
					request.setParams(comId, lastModified, "android");
					UpgradeComponentResponse result = mApplication.client.execute(request);
					if(result != null){
						if(result.getCode() != null && result.getCode() == 0){
							List<UpgradeComponent> ucs = result.getData().getList();
							if(ucs != null && !ucs.isEmpty()){
								mApplication.dbCache.saveUpgradeCompanentInfos(ucs);
							}
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String parseCertWithAppId(String appId){
		try{
			String unzipPath = FileUtil.getContainerParentDirectory() + "daze_service_unzip";
			File file = new File(unzipPath, appId);
			if(file.exists() && file.isDirectory()){
				file = new File(unzipPath + File.separator + appId + File.separator + "CERT");
				if(file.exists() && file.isFile()){
					InputStream is = new FileInputStream(file);
					int length = is.available();
					byte[] buffer = new byte[length];
					is.read(buffer);
					is.close();
					String content = EncodingUtils.getString(buffer, "utf-8");
					JSONObject jsonContent = new JSONObject(content);
					return jsonContent.optString("lastmodified", "0");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private void getVehicleTypes(){
		try{
			GetCommonDictionaryRequest request = new GetCommonDictionaryRequest();
			request.setParams("vehicle_type");
			 GetCommonDictionaryResponse result = mApplication.client.execute(request);
			 if(result != null){
				 if(result.getCode() != null && result.getCode() == 0){
					 CommonDictionaryJson data = result.getData();
					 if(data != null){
						 List<CommonDictionary> vehicleTypes = data.getList();
						 if(vehicleTypes != null && !vehicleTypes.isEmpty()){
							 mApplication.dbCache.saveVehicleTypes(vehicleTypes);
						 }
					 }
				 }
			 }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void initTaoBao(){
		if(mApplication.getId() != 0){
			if(StringUtils.isEmpty(mApplication.getOpenImUserId())){
				GetUserOpenImRequest request = new GetUserOpenImRequest();
				request.setParams(mApplication.getId());
				GetUserOpenImResponse response = null;
				try {
					response = mApplication.client.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(response != null && response.getCode() != null && response.getCode() == 0){
					UserOpenIm data = response.getData();
					if(data != null && !StringUtils.isEmpty(data.getOpenUserid()) && !StringUtils.isEmpty(data.getOpenPassword())){
						mApplication.initTaobao();
						mApplication.loginTaobao(data.getOpenUserid(), data.getOpenPassword());
						mApplication.setOpenImUserId(data.getOpenUserid());
						mApplication.setOpenImPassWord(data.getOpenPassword());
					}
				}
			}
			else{
				mApplication.initTaobao();
				mApplication.loginTaobao(mApplication.getOpenImUserId(), mApplication.getOpenImPassWord());
			}
		}
	}

	private void loadRestrictNum(){
		try {
			List<UserVehicle> userVehicleList = mApplication.dbCache.getVehicles();
			if (userVehicleList != null){
				for (int i = 0; i < userVehicleList.size(); i++){
					UserVehicle uv = userVehicleList.get(i);
					GetRestrictNumRequest restrictNumRequest = new GetRestrictNumRequest();
					restrictNumRequest.setParams(uv.getCityName());
					GetRestrictNumResponse restrictNumResponse = mApplication.client.execute(restrictNumRequest);
					if (restrictNumResponse != null && restrictNumResponse.getCode() == 0 && restrictNumResponse.getData() != null){
						uv.setRestrictNum(restrictNumResponse.getData().getNum());
						uv.setRestrictRegion(restrictNumResponse.getData().getMessage());
						uv.setRestrictNums(restrictNumResponse.getData().getRestrictNums());
						userVehicleList.set(i, uv);
					}
				}
				mApplication.dbCache.saveUserVehicles(userVehicleList);
				if (mApplication.getId() != 0) {
					RestrictListRequest request = new RestrictListRequest();
					request.setParams(mApplication.getId());
					RestrictListResponse response = mApplication.client.execute(request);
					if (response != null && response.getCode() != null && response.getCode() == 0) {
						RemindManager.getInstance(InitService.this).cancelAll(Constants.REQUEST_TYPE_RESTRICT);
						List<RemindRestrict> list = response.getData().getList();
						mApplication.dbCache.saveRemindRestricts(list);
						RemindManager.getInstance(InitService.this).setAll(Constants.REQUEST_TYPE_RESTRICT);
					}
					for (final UserVehicle userVehicle : userVehicleList) {
						RemindRestrict restrict = mApplication.dbCache.getRemindRestrict(userVehicle.getVehicleNum());
						if (!StringUtils.isEmpty(userVehicle.getRestrictNums()) && restrict == null) {
							final RemindRestrict remindRestrict = new RemindRestrict();
							remindRestrict.setVehicleNum(userVehicle.getVehicleNum());
							new RestrictSaveTask(mApplication) {
								@Override
								protected void onPostExecute(RestrictSaveResponse response) {
									if (response != null && response.getCode() != null && response.getCode() == 0) {
										remindRestrict.setId(response.getData().getId());
										remindRestrict.setVehicleCityId(userVehicle.getCityId());
										mApplication.dbCache.saveRemindRestrict(remindRestrict);
										RemindManager.getInstance(InitService.this).set(Constants.REQUEST_TYPE_RESTRICT, userVehicle.getVehicleNum(), 0, null);
									}
								}
							}.execute(remindRestrict);
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
