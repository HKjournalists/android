package com.kplus.car.container.command;

import org.json.JSONObject;

import com.kplus.car.util.StringUtils;

public class DazeInvokedUrlCommand {
	private JSONObject arguments;
	private String callbackId;
	private String className;
	private String methodName;
	
	public JSONObject getArguments() {
		return arguments;
	}
	
	public void setArguments(JSONObject arguments) {
		this.arguments = arguments;
	}
	
	public String getCallbackId() {
		return callbackId;
	}
	
	public void setCallbackId(String callbackId) {
		this.callbackId = callbackId;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public void commandFromJson(JSONObject jsonEntry){
		String callbackId = jsonEntry.optString("callbackId");
		if(StringUtils.isEmpty(callbackId))
			callbackId = null;
		String className = jsonEntry.optJSONObject("data").optString("moduleName");
		String handlerName = jsonEntry.optString("handlerName");
		JSONObject arguments = jsonEntry.optJSONObject("data");
		initWithArguments(arguments, callbackId, className, handlerName);
	}
	
	private void initWithArguments(JSONObject arguments, String callbackId, String className, String handlerName){
		setArguments(arguments);
		setCallbackId(callbackId);
		setClassName(className);
		setMethodName(handlerName);
	}
}
