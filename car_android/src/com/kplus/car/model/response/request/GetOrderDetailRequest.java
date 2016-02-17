package com.kplus.car.model.response.request;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.model.response.GetOrderDetailResponse;
import com.kplus.car.util.MD5;

import java.util.Map;

public class GetOrderDetailRequest extends BaseRequest<GetOrderDetailResponse> {

	@Override
	public String getApiMethodName() {
		return "/formOrder/getDetail.htm";
	}

	@Override
	public Class<GetOrderDetailResponse> getResponseClass() {
		return GetOrderDetailResponse.class;
	}

	public void setParams(long orderId) {
		addParams("orderId", orderId);
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
