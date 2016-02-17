package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.ShareResultResponse;
import com.kplus.car.util.MD5;

public class ShareResultRequest extends BaseRequest<ShareResultResponse>
{

	@Override
	public String getApiMethodName()
	{
		return "/app/shareResult.htm";
	}

	@Override
	public Class<ShareResultResponse> getResponseClass()
	{
		return ShareResultResponse.class;
	}
	
	public void setParams(int type, long pId, long targetId,long userId, String vehicleNum){
		addParams("type", type);
		if(pId != 0)
			addParams("pId", pId);
		if(targetId != 0)
			addParams("targetId", targetId);
		addParams("userId", userId);
		if(vehicleNum != null)
			addParams("vehicleNum", vehicleNum);
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
