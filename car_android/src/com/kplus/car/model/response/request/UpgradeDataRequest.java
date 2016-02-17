package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.BooleanResultResponse;
import com.kplus.car.util.MD5;

public class UpgradeDataRequest extends BaseRequest<BooleanResultResponse> {

	@Override
	public String getApiMethodName() {
		return "/app/upgradeData.htm";
	}

	@Override
	public Class<BooleanResultResponse> getResponseClass() {
		return BooleanResultResponse.class;
	}
	
	public void setParams(long userId, long pid, long uid){
		addParams("userId", userId);
		if(pid != 0)
			addParams("pid", pid);
		if(uid != 0)
			addParams("uid", uid);
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
