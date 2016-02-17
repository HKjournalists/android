package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetUserInfoResponse;
import com.kplus.car.util.MD5;

public class GetUserInfoRequest extends BaseRequest<GetUserInfoResponse>{

	@Override
	public String getApiMethodName() {
		return "/user/getUserInfo.htm";
	}

	@Override
	public Class<GetUserInfoResponse> getResponseClass() {
		return GetUserInfoResponse.class;
	}
	
	public void setParams(long uid){
		addParams("uid", uid);
	}
	
	@Override
	public Map<String, Object> getTextParams() {
		long time = System.currentTimeMillis();
		map.put("appkey", KplusConstants.CLIENT_APP_KEY);
		map.put("time", time);
		map.put("v", 2);
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
