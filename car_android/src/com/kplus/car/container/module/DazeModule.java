package com.kplus.car.container.module;

import org.json.JSONObject;

import android.util.Log;

import com.kplus.car.container.DazeContainerController;
import com.kplus.car.container.command.DazeCommandDelegate;
import com.kplus.car.container.command.DazeInvokedUrlCommand;

public class DazeModule {
	private static final String TAG = "DazeModule";
	protected DazeCommandDelegate delegate;
	protected DazeContainerController viewController;
	
	public void init(DazeContainerController _viewController, DazeCommandDelegate _delegate){
		viewController = _viewController;
		delegate = _delegate;
	}
	
	public void sendResult(JSONObject result, DazeInvokedUrlCommand command){
		sendResult(result, command, false);
	}

	public void sendResult(JSONObject result, DazeInvokedUrlCommand command, boolean isKeepCallback){
		try{
			JSONObject responseJSON = new JSONObject();
			responseJSON.put("responseData", result);
			responseJSON.put("responseId", command.getCallbackId());
			responseJSON.put("keepCallback", isKeepCallback);
			String response = responseJSON.toString();
			response.replaceAll("\\\\", "\\\\\\\\");
			response.replaceAll("\\\"", "\\\\\\\"");
			response.replaceAll("\\\'", "\\\\\\\'");
			response.replaceAll("\\\n", "\\\\\\\n");
			response.replaceAll("\\\r", "\\\\\\\r");
			response.replaceAll("\\\f", "\\\\\\\f");
			response.replaceAll("\\\u2028", "\\\\\\\u2028");
			response.replaceAll("\\\u2029", "\\\\\\\u2029");
			String javascriptCommand = "DazeJSBridge.__invokeJS('" + response + "')";
			if(delegate != null){
				delegate.evalJS(javascriptCommand);
			}
			else{
				Log.e("TAG", "无法找到对应的 commandDelegate 对象");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
