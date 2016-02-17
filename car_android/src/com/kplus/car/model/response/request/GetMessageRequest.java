package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetMessageResponse;
import com.kplus.car.util.StringUtils;

public class GetMessageRequest extends BaseRequest<GetMessageResponse> {

	@Override
	public String getApiMethodName() {
		return "/notice/getMessages.htm";
	}

	@Override
	public Class<GetMessageResponse> getResponseClass() {
		return GetMessageResponse.class;
	}
	
	public void setParams(String cityId, long uid){
		addParams("cityId", cityId);
		if (uid != 0)
			addParams("uid", uid);
	}

}
