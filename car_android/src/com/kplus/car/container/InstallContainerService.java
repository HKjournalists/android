package com.kplus.car.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kplus.car.KplusConstants;
import com.kplus.car.comm.FileUtil;
import com.kplus.car.util.MD5;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ZipUtil;

import android.app.IntentService;
import android.content.Intent;

public class InstallContainerService extends IntentService{
	private String containerPath, zipPath, unzipPath;
	private String appName;
	
	public InstallContainerService(){
		super("InstallContainerService");
	}

	public InstallContainerService(String name) {
		super(name);
	}
	
	@Override
	public void onCreate() {
		containerPath = FileUtil.getContainerParentDirectory();
		zipPath = containerPath + "daze_service_zip";
		unzipPath = containerPath + "daze_service_unzip";
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			appName = intent.getStringExtra("appName");
			File file = new File(unzipPath);
			if(!file.exists()){
				file.mkdirs();
				install();
			}
			else{
				if(!file.isDirectory()){
					file.mkdirs();
					install();
				}
				else
					install();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void install(){
		try{
			File zipFile = new File(zipPath);
			if(!zipFile.exists() || !zipFile.isDirectory()){
				zipFile.mkdirs();
			}
			File file = null;
			if(appName.equals("apps.zip")){
				InputStream is = getAssets().open(appName);
				file = new File(zipPath, appName);
				OutputStream os = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length = 0;
				while((length = is.read(buffer)) != -1){
					os.write(buffer, 0, length);
					os.flush();
				}
				is.close();
				os.close();
			}
			else{
				file = new File(zipPath, appName);
			}
			ZipUtil.UnZipFolder(zipPath + File.separator + appName, unzipPath);
			file.delete();
//			if(!appName.equals("apps.zip")){
//				boolean isSuccess = false;
//				String containerPath = unzipPath + File.separator + appName.substring(0, appName.lastIndexOf("."));
//				file = new File(containerPath);
//				if(file.exists() && file.isDirectory()){
//					File certFile = new File(containerPath + File.separator + "CERT");
//					if(certFile.exists()){
//						InputStream is = new FileInputStream(certFile);
//						byte[] buffer = new byte[is.available()];
//						is.read(buffer);
//						is.close();
//						String certString = new String(buffer, "utf-8");
//						if(!StringUtils.isEmpty(certString)){
//							JSONObject certJSON = new JSONObject(certString);
//							JSONArray fileVerArray = certJSON.optJSONArray("fileVer");
//							if(fileVerArray != null && fileVerArray.length() > 0){
//								int arrayLength = fileVerArray.length();
//								int i=0;
//								for(;i<arrayLength;i++){
//									JSONObject fileVerObject = fileVerArray.optJSONObject(i);
//									if(fileVerObject != null){
//										String path = fileVerObject.optString("path");
//										if(path != null){
//											if(KplusConstants.isDebug)
//												System.out.println("path===>" + path);
//											File subFile = new File(containerPath + File.separator + path);
//											if(!subFile.exists())
//												break;
//											else{
//												String type = path.substring(path.lastIndexOf(".")+1).toLowerCase();
//												if(!isImage(type)){
//													String hash = fileVerObject.optString("hash");
//													if(hash != null){
//														if(KplusConstants.isDebug)
//															System.out.println("hash===>" + hash);
//														String md5 = MD5.md5File("");
//														if(md5 != null){
//															if(KplusConstants.isDebug)
//																System.out.println("md5===>" + md5);
//															if(!hash.equalsIgnoreCase(md5))
//																break;
//														}
//													}
//												}
//											}
//										}
//									}
//								}
//								if(i == arrayLength){
//									isSuccess = true;
//								}
//							}
//						}
//					}
//				}
//				if(!isSuccess){
//					file.delete();
//				}
//			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	 private boolean isImage(String type) {
//         if (type != null
//                         && (type.equals("jpg") || type.equals("gif")
//                                         || type.equals("png") || type.equals("jpeg")
//                                         || type.equals("bmp") || type.equals("wbmp")
//                                         || type.equals("ico") || type.equals("jpe"))) {
//                 return true;
//         }
//         return false;
//	 }

}
