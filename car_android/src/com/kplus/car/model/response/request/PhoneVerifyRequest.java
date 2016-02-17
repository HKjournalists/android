package com.kplus.car.model.response.request;

import java.util.Map;

import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetLongValueResponse;
import com.kplus.car.util.MD5;

public class PhoneVerifyRequest extends BaseRequest<GetLongValueResponse> {

	@Override
	public String getApiMethodName() {
		return "/formOrder/verify.htm";
	}

	@Override
	public Class<GetLongValueResponse> getResponseClass() {
		return GetLongValueResponse.class;
	}

	public void setParams(long userId, String phone, String code) {
		addParams("userId", userId).addParams("phone", phone).addParams("code", code);
	}
	
	@Override
	public Map<String, Object> getTextParams() {
		long time = System.currentTimeMillis();
		map.put("appkey", KplusConstants.CLIENT_APP_KEY);
		map.put("time", time);
		map.put("v", 1);
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
