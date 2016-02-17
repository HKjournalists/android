package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.UserLoginResponse;
import com.kplus.car.util.MD5;

public class UserLoginRequest extends BaseRequest<UserLoginResponse>{

	@Override
	public String getApiMethodName() {
		return "/user/login.htm";
	}

	@Override
	public Class<UserLoginResponse> getResponseClass() {
		return UserLoginResponse.class;
	}
	
	public void setParams(String userName, int type, long userId, String nickname){//type:登录类型，0=手机号;1=QQ;2=微信;3=微博
		addParams("userName", userName).addParams("type", type);
		if(userId != 0){
			addParams("userId", userId);
		}
		if(nickname != null){
			addParams("nickname", nickname);
		}
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
