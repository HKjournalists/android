package com.kplus.car.container.command;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.kplus.car.container.module.DazeModule;
import com.kplus.car.util.StringUtils;

public class DazeCommandQueue {
	private final static String TAG = "DazeCommandQueue";
	private List<String> queue = new LinkedList<String>();
	private WebViewOperateInterface woi;
	
	public void enqueueCommandBatch(String batchJSON){
		try{
			if(!StringUtils.isEmpty(batchJSON)){
				queue.add("{\"dataRecived\":" + batchJSON +"}");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void fetchCommandsFromJs(){
		if(woi != null){
			woi.fetchQueue("DazeJSBridge._fetchQueue()");
		}
	}
	
	public void executePending(){
		try{
			while(queue.size() > 0){
				String strCommand = queue.get(0);
				JSONObject commondJson = new JSONObject(strCommand);
				JSONArray ja = commondJson.getJSONArray("dataRecived");
				int nSize = ja.length();
				for(int i=0;i<nSize;i++){
					JSONObject jsonEntry = ja.getJSONObject(i);
					DazeInvokedUrlCommand dc = new DazeInvokedUrlCommand();
					dc.commandFromJson(jsonEntry);
					if(!execute(dc)){
						Log.e(TAG, "命令执行失败:"+dc.getClassName());
					}
				}
				queue.remove(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean execute(DazeInvokedUrlCommand command){
		try{
			long start = System.currentTimeMillis();
			if(StringUtils.isEmpty(command.getClassName()) || StringUtils.isEmpty(command.getMethodName())){
				Log.e(TAG, "ERROR: Classname and/or methodName not found for command.");
				return false;
			}
			DazeModule moduleInstance = woi.getCommandInstance(command.getArguments().optString("moduleName"));
			if(!(moduleInstance instanceof DazeModule)){
				Log.e(TAG, String.format("ERROR: Classname and/or methodName %s not found for command.", command.getClassName()));
				return false;
			}
			String methodName = command.getMethodName();
			Method method = moduleInstance.getClass().getMethod(methodName, DazeInvokedUrlCommand.class);
			if(method == null){
				Log.e(TAG, String.format("ERROR:错误: 方法 '%s 未定义！ 模块: %s", methodName, command.getClassName()));
				return false;
			}
			else{
				method.invoke(moduleInstance, command);
			}
			int interval = (int) (System.currentTimeMillis() - start);
			if(interval > 10){
				Log.e(TAG, "THREAD WARNING: [" + command.getClassName() + "." + command.getMethodName() + "] took " + interval + " ms. Plugin should use a background thread.");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public WebViewOperateInterface getWoi() {
		return woi;
	}

	public void setWoi(WebViewOperateInterface woi) {
		this.woi = woi;
	}
}
