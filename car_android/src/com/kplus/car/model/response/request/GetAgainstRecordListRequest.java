package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetAgainstRecordListResponse;
import com.kplus.car.util.MD5;
import com.kplus.car.util.StringUtils;

public class GetAgainstRecordListRequest extends
		BaseRequest<GetAgainstRecordListResponse> {

	@Override
	public String getApiMethodName() {
		return "/against/getRecords.htm";
	}

	@Override
	public Class<GetAgainstRecordListResponse> getResponseClass() {
		return GetAgainstRecordListResponse.class;
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

	public void setParams(long userId, String vehicleNum, long preUpdateTime) {
		addParams("userId", userId).addParams("vehicleNum", vehicleNum);
		addParams("preUpdateTime", preUpdateTime);
	}
}
