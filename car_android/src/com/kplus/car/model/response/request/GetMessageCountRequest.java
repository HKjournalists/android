package com.kplus.car.model.response.request;

import com.kplus.car.model.response.GetMessageCountResponse;

public class GetMessageCountRequest extends BaseRequest<GetMessageCountResponse> {

	@Override
	public String getApiMethodName() {
		return "/notice/getMessageCount.htm";
	}

	@Override
	public Class<GetMessageCountResponse> getResponseClass() {
		return GetMessageCountResponse.class;
	}
	
	public void setParams(String cityId, long uid){
		addParams("cityId", cityId);
		if (uid != 0)
			addParams("uid", uid);
	}

}
