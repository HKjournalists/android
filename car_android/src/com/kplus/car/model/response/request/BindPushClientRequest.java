package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.BindPushClientResponse;
import com.kplus.car.util.MD5;

public class BindPushClientRequest extends BaseRequest<BindPushClientResponse>{

	@Override
	public String getApiMethodName() {
		return "/app/bindPushClient.htm";
	}

	@Override
	public Class<BindPushClientResponse> getResponseClass() {
		return BindPushClientResponse.class;
	}
	
	public void setParams(long userId, String clientId, int type){
		addParams("userId", userId);
		addParams("clientId", clientId);//第三方推送设备ID
		addParams("type", type);//第三方推送类型，1=个推
	}
	
	@Override
	public Map<String, Object> getTextParams() {
		long time = System.currentTimeMillis();
		map.put("appkey", KplusConstants.CLIENT_APP_KEY);
		map.put("time", time);
		map.put("v", 1);
		if(KplusApplication.sUserId != 0)
			map.put("userId",KplusApplication.sUserId);
		if (jsonObject != null) {
			map.put("params", jsonObject.toString());
			map.put("sign",
					MD5.md5(KplusConstants.CLIENT_APP_KEY
							+ KplusConstants.CLIENT_APP_SECRET + time
							+ jsonObject.toString()));
		} else {
			map.put("sign",
					MD5.md5(KplusConstants.CLIENT_APP_KEY
							+ KplusConstants.CLIENT_APP_SECRET + time));
		}
		return map;
	}

}
