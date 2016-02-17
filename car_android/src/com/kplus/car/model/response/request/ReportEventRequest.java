package com.kplus.car.model.response.request;

import java.util.HashMap;
import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.Response;
import com.kplus.car.util.StringUtils;

public class ReportEventRequest extends BaseRequest<Response>{

	@Override
	public String getApiMethodName() {
		return "/app/reportEvent.htm";
	}

	@Override
	public Class<Response> getResponseClass() {
		return Response.class;
	}
	
//	@Override
//	public String getServerUrl() {
//		return KplusConstants.REPORT_URL;
//	}
	
	@Override
	public Map<String, Object> getTextParams() {
		return map;
	}

	public void setParams(String networkName, String networkType,String outIp, String intIp, 
							String appVer, String appKey, long userId, long uid, String event, String props){
		addParams("event.appId", "1");//应用ID
		addParams("event.os", "android");//系统,ios/android
		addParams("event.osVer", android.os.Build.VERSION.RELEASE);//系统版本
		addParams("event.device", android.os.Build.MODEL);//设备型号
		addParams("event.networkName", networkName);//网络服务商
		addParams("event.networkType", networkType);//网络制式
		if(!StringUtils.isEmpty(outIp))//外网IP
			addParams("event.outIp", outIp);
		if(!StringUtils.isEmpty(intIp))//内网IP
			addParams("event.intIp", intIp);
		addParams("event.appVer", appVer);//应用版本
		addParams("event.appKey", appKey);//应用标记
		addParams("event.userId", userId);//应用设备ID
		if(uid != 0)
			addParams("event.uid", uid);//应用用户ID
		addParams("event.event", event);//具体事件
		if(!StringUtils.isEmpty(props))//事件参数JsonString
			addParams("event.props", props);
	}
	
	protected BaseRequest<Response> addParams(String name, Object value) {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		map.put(name, value);
		return this;
	}
}
