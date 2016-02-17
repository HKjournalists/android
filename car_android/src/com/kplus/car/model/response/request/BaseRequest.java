package com.kplus.car.model.response.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.kplus.car.KplusApplication;
import com.kplus.car.KplusConstants;
import com.kplus.car.Request;
import com.kplus.car.Response;
import com.kplus.car.util.MD5;

public abstract class BaseRequest<T extends Response> implements Request<T> {

	protected Map<String, Object> map = new HashMap<String, Object>();

	protected JSONObject jsonObject;

	@Override
	public String getServerUrl() {
		return KplusConstants.SERVER_URL;
	}

	@Override
	public Map<String, Object> getTextParams() {
		long time = System.currentTimeMillis();
		map.put("appkey", KplusConstants.CLIENT_APP_KEY);
		map.put("time", time);
		map.put("v", 0);
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

	protected BaseRequest<T> addParams(String name, Object value) {
		if (jsonObject == null) {
			jsonObject = new JSONObject();
		}
		try {
			jsonObject.put(name, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

}
