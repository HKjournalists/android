package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BindInviteRecordResponse;

public class BindInviteRecordRequest extends BaseRequest<BindInviteRecordResponse>{

	@Override
	public String getApiMethodName() {
		return "/user/bindInviteRecord.htm";
	}

	@Override
	public Class<BindInviteRecordResponse> getResponseClass() {
		return BindInviteRecordResponse.class;
	}
	
	public void setParams(String shortUrl, String phone){
		addParams("shortUrl", shortUrl).addParams("phone", phone);
	}

}
