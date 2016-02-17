package com.kplus.car.container.command;

import java.io.File;

import android.os.Looper;

import com.kplus.car.comm.FileUtil;

public class DazeCommandDelegate {
	private boolean delayResponse;
	private WebViewOperateInterface woi;
	
	public static String pathForResourcewithAppId(String resourcepath, String appId){
		String result = null;
		String unZipFolderPath = FileUtil.getContainerParentDirectory() + "daze_service_unzip/";
		File file = new File(unZipFolderPath);
		if(file != null && file.exists() && file.isDirectory()){
			String h5AppFolderPath = unZipFolderPath + appId + File.separator;
			file = new File(h5AppFolderPath);
			if(file != null && file.exists() && file.isDirectory()){
				String appPath = h5AppFolderPath + "App/";
				file = new File(appPath);
				if(file != null && file.exists() && file.isDirectory()){
					result = appPath + resourcepath;
				}
			}
		}
		
		return result;
	}
	
	public void evalJSHelper2(String js){
		woi.fetchQueue(js);
	}
	
	public void evalJSHelper(String js){
		if(delayResponse || Looper.myLooper() != Looper.getMainLooper()){
			evalJSHelper2(js);
		}
	}
	
	public void evalJS(String js){
		evalJS(js, false);
	}
	
	public void evalJS(String js, boolean scheduledOnRunLoop){
		if(scheduledOnRunLoop){
			evalJSHelper(js);
		}
		else{
			evalJSHelper2(js);
		}
	}
	
	public WebViewOperateInterface getWoi() {
		return woi;
	}

	public void setWoi(WebViewOperateInterface woi) {
		this.woi = woi;
	}
}
