package com.kplus.car.model.response.request;

import com.kplus.car.model.response.BindThirdPartResponse;
import com.kplus.car.util.StringUtils;

public class BindThirdPartRequest extends BaseRequest<BindThirdPartResponse> {

	@Override
	public String getApiMethodName() {
		return "/user/bind.htm";
	}

	@Override
	public Class<BindThirdPartResponse> getResponseClass() {
		return BindThirdPartResponse.class;
	}
	
	public void setParams(long uid, String userName, String nickname, int type){
		addParams("uid", uid).addParams("userName", userName).addParams("type", type);
		if(!StringUtils.isEmpty(nickname))
			addParams("nickname", nickname);
	}

}
