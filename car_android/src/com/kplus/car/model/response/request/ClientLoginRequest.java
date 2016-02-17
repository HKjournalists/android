package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetResultResponse;
import com.kplus.car.util.MD5;

public class ClientLoginRequest extends BaseRequest<GetResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/app/login.htm";
	}

	@Override
	public Class<GetResultResponse> getResponseClass() {
		return GetResultResponse.class;
	}

	public void setParams(long userId, String clientId, String clientType, String clientVersion, String appVersion) {
		addParams("userId", userId).addParams("clientId", clientId);
		if(clientType != null)
			addParams("clientType", clientType);
		if(clientVersion != null)
			addParams("clientVersion", clientVersion);
		if(appVersion != null)
			addParams("appVersion", appVersion);
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
