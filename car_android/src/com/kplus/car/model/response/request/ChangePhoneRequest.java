package com.kplus.car.model.response.request;

import com.kplus.car.model.response.ChangePhoneResponse;

public class ChangePhoneRequest extends BaseRequest<ChangePhoneResponse> {

	@Override
	public String getApiMethodName() {
		return "/user/changePhone.htm";
	}

	@Override
	public Class<ChangePhoneResponse> getResponseClass() {
		return ChangePhoneResponse.class;
	}
	
	public void setParams(long userId, long uid, String phone, String code){
		addParams("userId", userId).addParams("uid", uid).addParams("phone", phone).addParams("code", code);
	}

}
